package mia.the_tower.initialisation;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class TwoBlockTallRod extends Block {
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;
    public static final EnumProperty<Direction> FACING = EnumProperty.of("facing", Direction.class);

    public TwoBlockTallRod(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(FACING, Direction.UP)
                .with(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction direction = ctx.getSide(); // This is the face the player clicked
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();

        BlockPos topPos = pos.offset(direction);
        if (!world.getBlockState(topPos).canReplace(ctx)) {
            return null;
        }

        return this.getDefaultState()
                .with(FACING, direction)
                .with(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        super.onPlaced(world, pos, state, placer, itemStack);
        Direction direction = state.get(FACING);
        BlockPos upperPos = pos.offset(direction);

        BlockState upperState = this.getDefaultState()
                .with(FACING, direction)
                .with(HALF, DoubleBlockHalf.UPPER);

        world.setBlockState(upperPos, upperState, Block.NOTIFY_ALL);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        DoubleBlockHalf half = state.get(HALF);
        Direction direction = state.get(FACING);
        BlockPos otherPos = (half == DoubleBlockHalf.LOWER) ? pos.offset(direction) : pos.offset(direction.getOpposite());
        BlockState otherState = world.getBlockState(otherPos);

        if (otherState.isOf(this) && otherState.get(HALF) != half) {
            world.breakBlock(otherPos, false);
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public BlockState getStateForNeighborUpdate(
            BlockState state,
            WorldView world,
            ScheduledTickView tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            Random random
    ) {
        DoubleBlockHalf half = state.get(HALF);
        Direction facing = state.get(FACING);
        Direction expected = (half == DoubleBlockHalf.LOWER) ? facing : facing.getOpposite();

        if (direction == expected) {
            if (!neighborState.isOf(this) || neighborState.get(HALF) == half) {
                return Blocks.AIR.getDefaultState();
            }
        }

        return state;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Direction facing = state.get(FACING);
        switch (facing) {
            case NORTH:
                return VoxelShapes.cuboid(0.375F, 0.375F, 0F, 0.625F, 0.625F, 1.0F);
            case SOUTH:
                return VoxelShapes.cuboid(0.375F, 0.375F, 0.0F, 0.625F, 0.625F, 1F);
            case WEST:
                return VoxelShapes.cuboid(0F, 0.375F, 0.375F, 1.0F, 0.625F, 0.625F);
            case EAST:
                return VoxelShapes.cuboid(0.0F, 0.375F, 0.375F, 1F, 0.625F, 0.625F);
            case DOWN:
                return VoxelShapes.cuboid(0.375F, 1F, 0.375F, 0.625F, 1.0F, 0.625F);
            case UP:
            default:
                return VoxelShapes.cuboid(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
        }
    }

}
