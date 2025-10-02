package mia.the_tower.initialisation.fluid;

import mia.the_tower.initialisation.block_init;
import mia.the_tower.initialisation.item_init;
import net.minecraft.block.BlockState;

import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;

import java.util.Optional;

public abstract class GunkFluid extends BaseFluid {
    @Override public Fluid getStill() { return block_init.STILL_GUNK; }
    @Override public Fluid getFlowing() { return block_init.FLOWING_GUNK; }
    @Override public Item getBucketItem() { return item_init.CUP_OF_GUNK; }
    @Override protected BlockState toBlockState(FluidState state) {
        return block_init.GUNK.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(state));
    }

    public static class Flowing extends GunkFluid {
        @Override protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }
        @Override public int getLevel(FluidState state) { return state.get(LEVEL); }
        @Override public boolean isStill(FluidState state) { return false; }
    }

    public static class Still extends GunkFluid {
        @Override public int getLevel(FluidState state) { return 8; }
        @Override public boolean isStill(FluidState state) { return true; }
    }

    @Override
    public int getMaxFlowDistance(WorldView world) {
        return 7;
    }

    @Override protected int getLevelDecreasePerBlock(WorldView world) { return 1; }
    @Override public int getTickRate(WorldView world) { return 20; }


    @Override
    public void randomDisplayTick(World world, BlockPos pos, FluidState state, Random random) {
        if (!state.isStill() && !(Boolean)state.get(FALLING)) {
            if (random.nextInt(64) == 0) {
                world.playSound(
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        SoundEvents.BLOCK_WATER_AMBIENT,
                        SoundCategory.BLOCKS,
                        random.nextFloat() * 0.25F + 0.75F,
                        random.nextFloat() + 0.5F,
                        false
                );
            }
        } else if (random.nextInt(10) == 0) {
            world.addParticle(
                    ParticleTypes.ASH, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble(), pos.getZ() + random.nextDouble(), 0.0, 0.0, 0.0
            );
        }
    }

    /*
    @Override
    public boolean matchesType(Fluid fluid) {
        return fluid == Fluids.WATER || fluid == Fluids.FLOWING_WATER;
    }

     */

    @Override
    public Optional<SoundEvent> getBucketFillSound() {
        return Optional.of(SoundEvents.ITEM_BUCKET_FILL);
    }

}
