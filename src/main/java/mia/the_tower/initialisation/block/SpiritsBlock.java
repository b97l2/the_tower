package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

public class SpiritsBlock extends HorizontalFacingBlock {

    public static final MapCodec<SpiritsBlock> CODEC = createCodec(SpiritsBlock::new);

    public SpiritsBlock(Settings settings) {
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
                return VoxelShapes.cuboid(0.375F, 0F, 0.375F, 0.625F, 0.8125F, 0.625F);
            case SOUTH:
                return VoxelShapes.cuboid(0.375F, 0F, 0.375F, 0.625F, 0.8125F, 0.625F);
            case EAST:
                return VoxelShapes.cuboid(0.375F, 0F, 0.375F, 0.625F, 0.8125F, 0.625F);
            case WEST:
                return VoxelShapes.cuboid(0.375F, 0F, 0.375F, 0.625F, 0.8125F, 0.625F);
            default:
                return VoxelShapes.cuboid(0.375F, 0F, 0.375F, 0.625F, 0.8125F, 0.625F);
        }
    }
}
