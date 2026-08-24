package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import mia.the_tower.initialisation.fluid.FluidLogging;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class FluidloggableSlabBlock extends SlabBlock implements ModFluidLoggable {
    public static final MapCodec<FluidloggableSlabBlock> CODEC = createCodec(FluidloggableSlabBlock::new);

    public FluidloggableSlabBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(FluidLogging.withDefaultFluid(this.getDefaultState()));
    }

    @Override
    public MapCodec<? extends SlabBlock> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState state = super.getPlacementState(ctx);
        if (state == null) {
            return null;
        }
        if (state.get(TYPE) == SlabType.DOUBLE) {
            return FluidLogging.withSlot(state, FluidLogging.Slot.NONE);
        }
        return FluidLogging.withPlacementFluid(state, ctx);
    }

    @Override
    public boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return state.get(TYPE) != SlabType.DOUBLE && ModFluidLoggable.super.canFillWithFluid(player, world, pos, state, fluid);
    }

    @Override
    public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        return state.get(TYPE) != SlabType.DOUBLE && ModFluidLoggable.super.tryFillWithFluid(world, pos, state, fluidState);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state,
            WorldView world,
            ScheduledTickView tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            Random random
    ) {
        FluidLogging.scheduleFluidTick(state, tickView, world, pos);
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        return FluidLogging.getFluidState(state, super.getFluidState(state));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        FluidLogging.appendProperty(builder);
    }
}
