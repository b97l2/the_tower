package mia.the_tower.initialisation.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class LaunchBlock extends Block {

    public LaunchBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState()
                .with(NORTH, false)
                .with(EAST, false)
                .with(SOUTH, false)
                .with(WEST, false));
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!world.isClient) {
            Vec3d velocity = entity.getVelocity();
            entity.setVelocity(velocity.x, velocity.y+1.3, velocity.z);
            entity.velocityModified = true;

        }
    }

    //for connected textures
    public static final BooleanProperty NORTH = BooleanProperty.of("north");
    public static final BooleanProperty EAST = BooleanProperty.of("east");
    public static final BooleanProperty SOUTH = BooleanProperty.of("south");
    public static final BooleanProperty WEST = BooleanProperty.of("west");

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NORTH, EAST, SOUTH, WEST);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        WorldAccess world = ctx.getWorld();
        BlockPos pos = ctx.getBlockPos();

        return this.getDefaultState()
                .with(NORTH, connectsTo(world, pos.north()))
                .with(EAST,  connectsTo(world, pos.east()))
                .with(SOUTH, connectsTo(world, pos.south()))
                .with(WEST,  connectsTo(world, pos.west()));
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            updateNeighbors(world, pos);
        }
        super.onStateReplaced(state, world, pos, newState, moved);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        updateNeighbors(world, pos);
    }

    private boolean connectsTo(WorldAccess world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() == this;
    }

    private void updateNeighbors(World world, BlockPos pos) {
        for (Direction dir : Direction.Type.HORIZONTAL) {
            BlockPos neighborPos = pos.offset(dir);
            BlockState neighborState = world.getBlockState(neighborPos);
            if (neighborState.getBlock() == this) {
                world.setBlockState(neighborPos, getUpdatedState(world, neighborPos), Block.NOTIFY_ALL);
            }
        }
    }

    private BlockState getUpdatedState(WorldAccess world, BlockPos pos) {
        return this.getDefaultState()
                .with(NORTH, connectsTo(world, pos.north()))
                .with(EAST,  connectsTo(world, pos.east()))
                .with(SOUTH, connectsTo(world, pos.south()))
                .with(WEST,  connectsTo(world, pos.west()));
    }

}