package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class loaf_init extends HorizontalFacingBlock {

    public static final MapCodec<loaf_init> CODEC = createCodec(loaf_init::new);

    public loaf_init(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {

        switch ((Direction)state.get(FACING)) {
            case NORTH:
                return VoxelShapes.cuboid(0.3125F, 0F, 0.125F, 0.6875F, 0.4375F, 0.875F);
            case SOUTH:
                return VoxelShapes.cuboid(0.3125F, 0F, 0.125F, 0.6875F, 0.4375F, 0.875F);
            case EAST:
                return VoxelShapes.cuboid(0.125F, 0F, 0.3125F, 0.875F, 0.4375F, 0.6875F);
            case WEST:
                return VoxelShapes.cuboid(0.125F, 0F, 0.3125F, 0.875F, 0.4375F, 0.6875F);
            default:
                return VoxelShapes.cuboid(0.125F, 0F, 0.3125F, 0.875F, 0.4375F, 0.6875F);
        }
    }
/*
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction.fromHorizontalQuarterTurns(45) axis = state.get(AXIS);
        switch (axis) {
            case X:
                return VoxelShapes.cuboid(0.0F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
            case Y:
                return VoxelShapes.cuboid(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
            case Z:
            default:
                return VoxelShapes.cuboid(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 1.0F);
        }

    }

 */



}
