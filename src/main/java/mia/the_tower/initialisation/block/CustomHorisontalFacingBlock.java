package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import mia.the_tower.initialisation.fluid.FluidLogging;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class CustomHorisontalFacingBlock extends HorizontalFacingBlock implements ModFluidLoggable {

    public static final MapCodec<CustomHorisontalFacingBlock> CODEC = createCodec(CustomHorisontalFacingBlock::new);

    public CustomHorisontalFacingBlock(Settings settings) {
        super(settings);
        this.setDefaultState(FluidLogging.withDefaultFluid(this.getDefaultState().with(FACING, Direction.NORTH)));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return FluidLogging.withPlacementFluid(
                this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()),
                ctx
        );
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
        FluidLogging.appendProperty(builder);
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

}
