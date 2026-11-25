package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import mia.the_tower.initialisation.fluid.FluidLogging;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.enums.SlabType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

public class VerticalSlabBlock extends Block implements ModFluidLoggable {
    public static final MapCodec<VerticalSlabBlock> CODEC = createCodec(VerticalSlabBlock::new);
    public static final EnumProperty<SlabType> TYPE = Properties.SLAB_TYPE;
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;

    private static final VoxelShape BOTTOM_NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
    private static final VoxelShape TOP_NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 8.0, 16.0, 16.0, 16.0);
    private static final VoxelShape BOTTOM_EAST_SHAPE = Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape TOP_EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 16.0, 16.0);
    private static final VoxelShape BOTTOM_SOUTH_SHAPE = TOP_NORTH_SHAPE;
    private static final VoxelShape TOP_SOUTH_SHAPE = BOTTOM_NORTH_SHAPE;
    private static final VoxelShape BOTTOM_WEST_SHAPE = TOP_EAST_SHAPE;
    private static final VoxelShape TOP_WEST_SHAPE = BOTTOM_EAST_SHAPE;

    public VerticalSlabBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(FluidLogging.withDefaultFluid(
                this.getDefaultState()
                        .with(TYPE, SlabType.BOTTOM)
                        .with(FACING, Direction.NORTH)
        ));
    }

    @Override
    protected MapCodec<? extends Block> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState currentState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (currentState.isOf(this)) {
            return FluidLogging.withSlot(currentState.with(TYPE, SlabType.DOUBLE), FluidLogging.Slot.NONE);
        }

        Direction facing = getPlacementFacing(ctx);
        SlabType type = getPlacementType(ctx, facing);
        return FluidLogging.withPlacementFluid(
                this.getDefaultState()
                        .with(TYPE, type)
                        .with(FACING, facing),
                ctx
        );
    }

    @Override
    protected boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (state.get(TYPE) == SlabType.DOUBLE || !context.getStack().isOf(this.asItem())) {
            return false;
        }

        if (context.canReplaceExisting()) {
            Direction side = context.getSide();
            SlabType clickedType = state.get(TYPE);
            return state.get(FACING).getAxis() == side.getAxis()
                    && ((clickedType == SlabType.BOTTOM && side == state.get(FACING).getOpposite())
                    || (clickedType == SlabType.TOP && side == state.get(FACING)));
        }

        return true;
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
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    private static VoxelShape getShape(BlockState state) {
        if (state.get(TYPE) == SlabType.DOUBLE) {
            return VoxelShapes.fullCube();
        }

        return switch (state.get(FACING)) {
            case EAST -> state.get(TYPE) == SlabType.BOTTOM ? BOTTOM_EAST_SHAPE : TOP_EAST_SHAPE;
            case SOUTH -> state.get(TYPE) == SlabType.BOTTOM ? BOTTOM_SOUTH_SHAPE : TOP_SOUTH_SHAPE;
            case WEST -> state.get(TYPE) == SlabType.BOTTOM ? BOTTOM_WEST_SHAPE : TOP_WEST_SHAPE;
            case NORTH, UP, DOWN -> state.get(TYPE) == SlabType.BOTTOM ? BOTTOM_NORTH_SHAPE : TOP_NORTH_SHAPE;
        };
    }

    private static Direction getPlacementFacing(ItemPlacementContext ctx) {
        Direction side = ctx.getSide();
        if (side.getAxis().isHorizontal()) {
            return side;
        }

        Vec3d hitPos = ctx.getHitPos();
        BlockPos blockPos = ctx.getBlockPos();
        double x = hitPos.x - blockPos.getX();
        double z = hitPos.z - blockPos.getZ();

        if (Math.abs(x - 0.5) > Math.abs(z - 0.5)) {
            return x < 0.5 ? Direction.WEST : Direction.EAST;
        }
        return z < 0.5 ? Direction.NORTH : Direction.SOUTH;
    }

    private static SlabType getPlacementType(ItemPlacementContext ctx, Direction facing) {
        Vec3d hitPos = ctx.getHitPos();
        BlockPos blockPos = ctx.getBlockPos();
        double offset = facing.getAxis() == Direction.Axis.X
                ? hitPos.x - blockPos.getX()
                : hitPos.z - blockPos.getZ();

        return switch (facing) {
            case EAST, SOUTH -> offset < 0.5 ? SlabType.TOP : SlabType.BOTTOM;
            case WEST, NORTH -> offset < 0.5 ? SlabType.BOTTOM : SlabType.TOP;
            case UP, DOWN -> SlabType.BOTTOM;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TYPE, FACING);
        FluidLogging.appendProperty(builder);
    }
}
