package mia.the_tower.initialisation.block;

import mia.the_tower.initialisation.particle.CustomParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class GoldThurible extends LanternBlock {

    private static final float SPAWN_CHANCE_PER_TICK = 0.35f; // per display tick
    private static final int   PARTICLES_PER_SPAWN   = 2;     // how many each time it triggers
    private static final double AREA_HALF_SIZE       = 2.5;   // 5x5 area => +/- 2.5 from center


    public GoldThurible(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
        // This method is client-side; no need for a block entity
        // Throttle: ~20% chance per client tick
        final double baseY = pos.getY() + (state.get(HANGING) ? 0.30 : 0.70);

        for (int i = 0; i < PARTICLES_PER_SPAWN; i++) {
            double x = pos.getX() + 0.5 + (rand.nextDouble() * (AREA_HALF_SIZE * 2.0) - AREA_HALF_SIZE);
            double z = pos.getZ() + 0.5 + (rand.nextDouble() * (AREA_HALF_SIZE * 2.0) - AREA_HALF_SIZE);
            double y = baseY + (rand.nextDouble() * 0.15 - 0.05); // small vertical jitter

            // Mild upward drift; tweak as needed
            double vx = 0.0;
            double vy = 0.0; //0.01 + rand.nextDouble() * 0.01;
            double vz = 0.0;

            world.addParticle(CustomParticles.GOLD_INCENSE, x, y, z, 0.0, 0.00, 0.0);

            // Example: only if not waterlogged
            // if (!state.get(WATERLOGGED)) { ... }
        }
    }

}
