package mia.the_tower.initialisation.block;

import mia.the_tower.initialisation.particle.CustomParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;

import net.minecraft.util.math.random.Random;

public class BarrierBlock extends Block {

    public BarrierBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient) {
            world.scheduleBlockTick(pos, this, 10);
        }
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        boolean playerAbove = !world.getEntitiesByClass(
                net.minecraft.entity.player.PlayerEntity.class,
                new Box(pos.up()),
                player -> player.getBlockPos().down().equals(pos)
        ).isEmpty(); //checks if there is a player ontop of it

        if (!playerAbove) { //if not, then the block breaks
            world.breakBlock(pos, false);
        } else {
            world.scheduleBlockTick(pos, this, 10);
        }
    }

    private static final int   PARTICLES_PER_SPAWN   = 2;     // how many each time it triggers
    private static final double AREA_HALF_SIZE       = 1;   // 5x5 area => +/- 2.5 from center

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {

        for (int i = 0; i < PARTICLES_PER_SPAWN; i++) {
            double x = pos.getX() + 0.5 + (rand.nextDouble() * (AREA_HALF_SIZE * 2.0) - AREA_HALF_SIZE);
            double z = pos.getZ() + 0.5 + (rand.nextDouble() * (AREA_HALF_SIZE * 2.0) - AREA_HALF_SIZE);
            double y = pos.getY() + 0.5 + (rand.nextDouble() * (AREA_HALF_SIZE * 2.0) - AREA_HALF_SIZE);

            double vx = 0.0;
            double vy = 0.0;
            double vz = 0.0;

            world.addParticle(CustomParticles.GLUH_PARTICLE, x, y, z, 0.0, 0.00, 0.0);

        }
    }
}