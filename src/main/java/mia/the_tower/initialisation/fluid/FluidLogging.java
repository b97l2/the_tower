package mia.the_tower.initialisation.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

public final class FluidLogging {
    public static final EnumProperty<Slot> FLUID = EnumProperty.of("fluid", Slot.class);

    private static final EnumMap<Slot, Entry> ENTRIES = new EnumMap<>(Slot.class);
    private static final Map<Fluid, Slot> FLUID_TO_SLOT = new IdentityHashMap<>();

    static {
        register(Slot.WATER, Fluids.WATER, Fluids.FLOWING_WATER, () -> new ItemStack(Items.WATER_BUCKET));
    }

    private FluidLogging() {
    }

    public static void appendProperty(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FLUID);
    }

    public static BlockState withDefaultFluid(BlockState state) {
        return state.with(FLUID, Slot.NONE);
    }

    @Nullable
    public static BlockState withPlacementFluid(@Nullable BlockState state, ItemPlacementContext ctx) {
        if (state == null) {
            return null;
        }

        return withFluid(state, ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid());
    }

    public static BlockState withFluid(BlockState state, Fluid fluid) {
        return withSlot(state, getSlot(fluid));
    }

    public static BlockState withSlot(BlockState state, Slot slot) {
        if (state.contains(FLUID)) {
            state = state.with(FLUID, slot);
        }
        if (state.contains(Properties.WATERLOGGED)) {
            state = state.with(Properties.WATERLOGGED, slot == Slot.WATER);
        }
        return state;
    }

    public static BlockState withoutFluidProperty(BlockState state) {
        return state.contains(FLUID) ? state.with(FLUID, Slot.NONE) : state;
    }

    public static boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return getLoggedSlot(state) == Slot.NONE && getSlot(fluid) != Slot.NONE;
    }

    public static boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        Slot slot = getSlot(fluidState.getFluid());
        if (getLoggedSlot(state) != Slot.NONE || slot == Slot.NONE) {
            return false;
        }

        if (!world.isClient()) {
            BlockState newState = withSlot(state, slot);
            world.setBlockState(pos, newState, Block.NOTIFY_ALL);
            scheduleFluidTick(newState, world, pos);
        }

        return true;
    }

    public static ItemStack tryDrainFluid(WorldAccess world, BlockPos pos, BlockState state) {
        Slot slot = getLoggedSlot(state);
        if (slot == Slot.NONE) {
            return ItemStack.EMPTY;
        }

        world.setBlockState(pos, withSlot(state, Slot.NONE), Block.NOTIFY_ALL);
        if (!state.canPlaceAt(world, pos)) {
            world.breakBlock(pos, true);
        }

        Entry entry = ENTRIES.get(slot);
        return entry == null ? ItemStack.EMPTY : entry.bucketStack().copy();
    }

    public static Optional<SoundEvent> getBucketFillSound() {
        return Fluids.WATER.getBucketFillSound();
    }

    public static FluidState getFluidState(BlockState state, FluidState fallback) {
        Entry entry = ENTRIES.get(getLoggedSlot(state));
        return entry == null ? fallback : entry.still().getDefaultState();
    }

    public static void scheduleFluidTick(BlockState state, ScheduledTickView tickView, WorldView world, BlockPos pos) {
        Entry entry = ENTRIES.get(getLoggedSlot(state));
        if (entry != null) {
            tickView.scheduleFluidTick(pos, entry.still(), entry.still().getTickRate(world));
        }
    }

    public static void scheduleFluidTick(BlockState state, WorldAccess world, BlockPos pos) {
        Entry entry = ENTRIES.get(getLoggedSlot(state));
        if (entry != null) {
            world.scheduleFluidTick(pos, entry.still(), entry.still().getTickRate(world));
        }
    }

    public static boolean isFluidLogged(BlockState state) {
        return getLoggedSlot(state) != Slot.NONE;
    }

    public static Slot getLoggedSlot(BlockState state) {
        if (state.contains(FLUID)) {
            Slot slot = state.get(FLUID);
            if (slot != Slot.NONE) {
                return slot;
            }
        }
        if (state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED)) {
            return Slot.WATER;
        }
        return Slot.NONE;
    }

    public static Slot getSlot(Fluid fluid) {
        return FLUID_TO_SLOT.getOrDefault(fluid, Slot.NONE);
    }

    public static void register(Slot slot, Fluid still, @Nullable Fluid flowing, Supplier<ItemStack> bucketStackSupplier) {
        if (slot == Slot.NONE) {
            throw new IllegalArgumentException("Cannot register a logged fluid to the none slot");
        }
        if (ENTRIES.containsKey(slot)) {
            throw new IllegalStateException("Logged fluid slot is already registered: " + slot.asString());
        }

        Entry entry = new Entry(still, bucketStackSupplier);
        ENTRIES.put(slot, entry);
        FLUID_TO_SLOT.put(still, slot);
        if (flowing != null) {
            FLUID_TO_SLOT.put(flowing, slot);
        }
    }

    public static Slot register(Fluid still, @Nullable Fluid flowing, Supplier<ItemStack> bucketStackSupplier) {
        for (Slot slot : Slot.values()) {
            if (slot != Slot.NONE && !ENTRIES.containsKey(slot)) {
                register(slot, still, flowing, bucketStackSupplier);
                return slot;
            }
        }

        throw new IllegalStateException("No free logged fluid slots remain");
    }

    private record Entry(Fluid still, Supplier<ItemStack> bucketStackSupplier) {
        ItemStack bucketStack() {
            return this.bucketStackSupplier.get();
        }
    }

    public enum Slot implements StringIdentifiable {
        NONE("none"),
        WATER("water"),
        BLOOD("blood"),
        GUNK("gunk"),
        FLUID_4("fluid_4"),
        FLUID_5("fluid_5"),
        FLUID_6("fluid_6"),
        FLUID_7("fluid_7"),
        FLUID_8("fluid_8"),
        FLUID_9("fluid_9"),
        FLUID_10("fluid_10"),
        FLUID_11("fluid_11"),
        FLUID_12("fluid_12"),
        FLUID_13("fluid_13"),
        FLUID_14("fluid_14"),
        FLUID_15("fluid_15");

        private final String name;

        Slot(String name) {
            this.name = name;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}
