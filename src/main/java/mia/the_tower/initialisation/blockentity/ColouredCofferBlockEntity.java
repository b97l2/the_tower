package mia.the_tower.initialisation.blockentity;

import mia.the_tower.initialisation.blockentity.util.ColouredInventory;
import net.minecraft.block.BarrelBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ViewerCountManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

public class ColouredCofferBlockEntity extends BlockEntity implements NamedScreenHandlerFactory, Inventory {
    private final ViewerCountManager stateManager = new ViewerCountManager() {
        @Override
        protected void onContainerOpen(World world, BlockPos pos, BlockState state) {
            ColouredCofferBlockEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_OPEN);
            ColouredCofferBlockEntity.this.setOpen(state, true);
        }

        @Override
        protected void onContainerClose(World world, BlockPos pos, BlockState state) {
            ColouredCofferBlockEntity.this.playSound(state, SoundEvents.BLOCK_BARREL_CLOSE); //change these to custom sound
            ColouredCofferBlockEntity.this.setOpen(state, false);
        }

        @Override
        protected void onViewerCountUpdate(World world, BlockPos pos, BlockState state, int oldViewerCount, int newViewerCount) {
        }

        @Override
        protected boolean isPlayerViewing(PlayerEntity player) {
            if (player.currentScreenHandler instanceof GenericContainerScreenHandler) {
                Inventory inventory = ((GenericContainerScreenHandler)player.currentScreenHandler).getInventory();
                return inventory == ColouredCofferBlockEntity.this;
            } else {
                return false;
            }
        }
    };

    void playSound(BlockState state, SoundEvent soundEvent) {
        Vec3i vec3i = ((Direction)state.get(BarrelBlock.FACING)).getVector();
        double d = this.pos.getX() + 0.5 + vec3i.getX() / 2.0;
        double e = this.pos.getY() + 0.5 + vec3i.getY() / 2.0;
        double f = this.pos.getZ() + 0.5 + vec3i.getZ() / 2.0;
        this.world.playSound(null, d, e, f, soundEvent, SoundCategory.BLOCKS, 0.5F, this.world.random.nextFloat() * 0.1F + 0.9F);
    }

    void setOpen(BlockState state, boolean open) {
        this.world.setBlockState(this.getPos(), state.with(ColouredCofferBlock.OPEN, open), Block.NOTIFY_ALL);
    }

    private boolean counted = false;
    private DyeColor color = DyeColor.WHITE;

    public ColouredCofferBlockEntity(BlockPos pos, BlockState state) {
        super(java.util.Objects.requireNonNull(blockentity_init.COLORED_COFFER, "COLORED_COFFER not registered"), pos, state);
        //super(blockentity_init.COLORED_COFFER, pos, state);
        if (state.getBlock() instanceof ColouredCofferBlock cofferBlock) {
            this.color = cofferBlock.getColor();
        }
    }

    public static void serverTick(net.minecraft.world.World world, BlockPos pos, BlockState state, ColouredCofferBlockEntity be) {
        if (world.isClient) return;
        if (!be.counted) {
            be.ensureCounted((ServerWorld) world);
        }
    }

    private void ensureCounted(ServerWorld sw) {
        ColouredInventory inv = ColouredInventory.get(sw, color);
        inv.onContainerAdded();
        counted = true;
        markDirty();
    }

    public DyeColor getColor() {
        return color;
    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putBoolean("counted", counted);
        nbt.putInt("color", color.getId()); // informational; block type already fixes color
    }

    @Override
    public void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        this.counted = nbt.getBoolean("counted");
        // color remains from constructor (block decides); the stored int is optional
    }

    //for the screen handler bellow


    /* ---------- NamedScreenHandlerFactory ---------- */
    @Override
    public Text getDisplayName() {
        return Text.translatable("container.coloured_coffer", Text.translatable("color.minecraft." + color.getName()));
    }

    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory playerInv, PlayerEntity player) {
        if (!(this.getWorld() instanceof ServerWorld sw)) return null;
        Inventory backing = ColouredInventory.get(sw, color); // full shared inventory for this color
        return new mia.the_tower.initialisation.screen.CofferScreenHandler(syncId, playerInv, backing,
                net.minecraft.screen.ScreenHandlerContext.create(sw, this.getPos()),
                this.getCachedState().getBlock());
    }

    /* ---------- Inventory (delegate to the shared inventory) ---------- */
    private static final Inventory CLIENT_PLACEHOLDER = new SimpleInventory(0); // zero-size, safe no-op

    private Inventory backing() {
        if (!(this.getWorld() instanceof ServerWorld sw)) {
            // Hoppers only run server-side; UI also creates handlers only server-side.
            // If ever called client-side, do nothing sensible.
            return CLIENT_PLACEHOLDER;
        }
        return ColouredInventory.get((ServerWorld) this.getWorld(), color);
    }

    @Override public int size() { return backing().size(); }
    @Override public boolean isEmpty() { return backing().isEmpty(); }
    @Override public net.minecraft.item.ItemStack getStack(int slot) { return backing().getStack(slot); }
    @Override public net.minecraft.item.ItemStack removeStack(int slot, int amount) { return backing().removeStack(slot, amount); }
    @Override public net.minecraft.item.ItemStack removeStack(int slot) { return backing().removeStack(slot); }
    @Override public void setStack(int slot, net.minecraft.item.ItemStack stack) { backing().setStack(slot, stack); }
    @Override public void markDirty() { backing().markDirty(); super.markDirty(); }
    @Override public boolean canPlayerUse(PlayerEntity player) { return backing().canPlayerUse(player); }
    @Override public void clear() { backing().clear(); }

}