package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.LanternBlock;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ThuribleBlock extends LanternBlock {

    public static final MapCodec<ThuribleBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            ParticleTypes.TYPE_CODEC.fieldOf("particle").forGetter(block -> block.particle),
                            createSettingsCodec()
                    )
                    .apply(instance, ThuribleBlock::new)
    );

    private static final int   PARTICLES_PER_SPAWN   = 2;     // how many each time it triggers
    private static final double AREA_HALF_SIZE       = 2.5;   // 5x5 area => +/- 2.5 from center
    private final ParticleEffect particle;

    public ThuribleBlock(ParticleEffect particle, AbstractBlock.Settings settings) {
        super(settings);
        this.particle = particle;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random rand) {
        // ~20% chance per client tick
        final double baseY = pos.getY() + (state.get(HANGING) ? 0.30 : 0.70);

        for (int i = 0; i < PARTICLES_PER_SPAWN; i++) {
            double x = pos.getX() + 0.5 + (rand.nextDouble() * (AREA_HALF_SIZE * 2.0) - AREA_HALF_SIZE);
            double z = pos.getZ() + 0.5 + (rand.nextDouble() * (AREA_HALF_SIZE * 2.0) - AREA_HALF_SIZE);
            double y = baseY + (rand.nextDouble() * 0.15 - 0.05); // small vertical jitter

            double vx = 0.0;
            double vy = 0.0; //0.01 + rand.nextDouble() * 0.01;
            double vz = 0.0;

            world.addParticle(particle, x, y, z, 0.0, 0.00, 0.0);

            // Example: only if not waterlogged
            // if (!state.get(WATERLOGGED)) { ... }
        }
    }

}
