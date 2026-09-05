package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import mia.the_tower.initialisation.fluid.FluidLogging;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.SideShapeType;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class SmallDecorativeBlock extends HorizontalFacingBlock implements ModFluidLoggable {
    public static final MapCodec<SmallDecorativeBlock> CODEC = createCodec(
            settings -> new SmallDecorativeBlock(settings, VoxelShapes.fullCube())
    );

    private final VoxelShape northShape;
    private final VoxelShape southShape;
    private final VoxelShape eastShape;
    private final VoxelShape westShape;

    public SmallDecorativeBlock(Settings settings, VoxelShape shape) {
        this(settings, shape, shape, shape, shape);
    }

    public SmallDecorativeBlock(
            Settings settings,
            VoxelShape northShape,
            VoxelShape southShape,
            VoxelShape eastShape,
            VoxelShape westShape
    ) {
        super(settings);
        this.northShape = northShape;
        this.southShape = southShape;
        this.eastShape = eastShape;
        this.westShape = westShape;
        this.setDefaultState(FluidLogging.withDefaultFluid(this.getDefaultState().with(FACING, Direction.NORTH)));
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        if (!canPlaceOnSurface(ctx.getWorld(), ctx.getBlockPos())) {
            return null;
        }

        return FluidLogging.withPlacementFluid(
                this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite()),
                ctx
        );
    }

    private static boolean canPlaceOnSurface(WorldView world, BlockPos pos) {
        BlockPos surfacePos = pos.down();
        BlockState surfaceState = world.getBlockState(surfacePos);
        return !(surfaceState.getBlock() instanceof SmallDecorativeBlock)
                && surfaceState.isSideSolid(world, surfacePos, Direction.UP, SideShapeType.FULL);
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

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Vec3d offset = getShapeOffset(state, pos);
        return getShape(state).offset(offset.x, offset.y, offset.z);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Vec3d offset = getShapeOffset(state, pos);
        return getShape(state).offset(offset.x, offset.y, offset.z);
    }

    private VoxelShape getShape(BlockState state) {
        return switch (state.get(FACING)) {
            case SOUTH -> southShape;
            case EAST -> eastShape;
            case WEST -> westShape;
            case NORTH, UP, DOWN -> northShape;
        };
    }

    private static Vec3d getShapeOffset(BlockState state, BlockPos pos) {
        return state.getModelOffset(pos);
    }
}
