package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mia.the_tower.initialisation.fluid.FluidLogging;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StairsBlock;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class FluidloggableStairsBlock extends StairsBlock implements ModFluidLoggable {
    public static final MapCodec<FluidloggableStairsBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(BlockState.CODEC.fieldOf("base_state").forGetter(block -> block.baseBlockState), createSettingsCodec())
                    .apply(instance, FluidloggableStairsBlock::new)
    );

    public FluidloggableStairsBlock(BlockState baseBlockState, AbstractBlock.Settings settings) {
        super(baseBlockState, settings);
        this.setDefaultState(FluidLogging.withDefaultFluid(this.getDefaultState()));
    }

    @Override
    public MapCodec<? extends StairsBlock> getCodec() {
        return CODEC;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return FluidLogging.withPlacementFluid(super.getPlacementState(ctx), ctx);
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
