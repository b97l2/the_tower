package mia.the_tower.initialisation.block;

import mia.the_tower.initialisation.block_init;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

import java.util.List;

public class CustomGrass extends GrassBlock implements Fertilizable {
    public CustomGrass(Settings settings) {
        super(settings);
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (!canSurvive(state, world, pos)) {
            world.setBlockState(pos, block_init.OLD_SOIL.getDefaultState());
        } else {
            if (world.getLightLevel(pos.up()) >= 2) {
                BlockState blockState = this.getDefaultState();

                for (int i = 0; i < 4; i++) {
                    BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    if (world.getBlockState(blockPos).isOf(block_init.OLD_SOIL) && canSpread(blockState, world, blockPos)) {
                        world.setBlockState(blockPos, blockState.with(SNOWY, isSnow(world.getBlockState(blockPos.up()))));
                    }
                }
            }
        }
    }

    private static boolean canSurvive(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.isOf(Blocks.SNOW) && (Integer)blockState.get(SnowBlock.LAYERS) == 1) {
            return true;
        } else if (blockState.getFluidState().getLevel() == 8) {
            return false;
        } else {
            int i = ChunkLightProvider.getRealisticOpacity(state, blockState, Direction.UP, blockState.getOpacity());
            return i < 15;
        }
    }

    private static boolean canSpread(BlockState state, WorldView world, BlockPos pos) {
        BlockPos blockPos = pos.up();
        return canSurvive(state, world, pos) && !world.getFluidState(blockPos).isIn(FluidTags.WATER);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return world.getBlockState(pos.up()).isAir();
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    public static Block pickRandomPlant(Random random) {
        List<Block> plantPool = List.of(
                block_init.BLUE_FOXGLOVE,
                block_init.SAPPHIRE_ROSE,
                block_init.CERULEAN_PETALS,
                block_init.CERULEAN_SHORTGRASS,
                block_init.BLUE_HOSTAS,
                block_init.BLUE_BELL,
                block_init.CERULEAN_WILDGRASS
        );
        return plantPool.get(random.nextInt(plantPool.size()));
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
// Try placing up to 32 random plants around the dirt block
        for (int i = 0; i < 32; ++i) {
            BlockPos targetPos = pos.add(
                    random.nextInt(7) - 3, // x offset -3 to +3
                    1,
                    random.nextInt(7) - 3  // z offset -3 to +3
            );

            if (!world.getBlockState(targetPos.down()).isOf(this)) continue;
            if (!world.getBlockState(targetPos).isAir()) continue;

            Block chosen = CustomGrass.pickRandomPlant(random); // your own method to pick a plant

            // If it's a tall plant
            if (chosen instanceof TallPlantBlock) {
                if (!world.getBlockState(targetPos.up()).isAir()) continue;

                BlockState lower = chosen.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER);
                BlockState upper = chosen.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.UPPER);

                world.setBlockState(targetPos, lower, Block.NOTIFY_ALL);
                world.setBlockState(targetPos.up(), upper, Block.NOTIFY_ALL);
            }
            // If it's a custom 3-block plant
            else if (chosen instanceof ExtraTallFlower) {
                if (!world.getBlockState(targetPos.up()).isAir() || !world.getBlockState(targetPos.up(2)).isAir()) continue;

                world.setBlockState(targetPos, chosen.getDefaultState().with(ExtraTallFlower.THIRD, TripleBlockPart.LOWER));
                world.setBlockState(targetPos.up(), chosen.getDefaultState().with(ExtraTallFlower.THIRD, TripleBlockPart.MIDDLE));
                world.setBlockState(targetPos.up(2), chosen.getDefaultState().with(ExtraTallFlower.THIRD, TripleBlockPart.UPPER));
            }
            // Normal plant
            else {
                world.setBlockState(targetPos, chosen.getDefaultState(), Block.NOTIFY_ALL);
            }
        }
    }
}