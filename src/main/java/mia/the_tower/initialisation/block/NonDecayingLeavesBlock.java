package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ParticleLeavesBlock;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.particle.ParticleUtil;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class NonDecayingLeavesBlock extends Block {
    public static final MapCodec<NonDecayingLeavesBlock> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                            Codecs.POSITIVE_INT.fieldOf("chance").forGetter(block -> block.chance),
                            ParticleTypes.TYPE_CODEC.fieldOf("particle").forGetter(block -> block.particle),
                            createSettingsCodec()
                    )
                    .apply(instance, NonDecayingLeavesBlock::new)
    );
    private final ParticleEffect particle;
    private final int chance;

    @Override
    public MapCodec<NonDecayingLeavesBlock> getCodec() {
        return CODEC;
    }

    public NonDecayingLeavesBlock(int chance, ParticleEffect particle, AbstractBlock.Settings settings) {
        super(settings);
        this.chance = chance;
        this.particle = particle;
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        if (random.nextInt(this.chance) == 0) {
            BlockPos blockPos = pos.down();
            BlockState blockState = world.getBlockState(blockPos);
            if (!isFaceFullSquare(blockState.getCollisionShape(world, blockPos), Direction.UP)) {
                ParticleUtil.spawnParticle(world, pos, random, this.particle);
            }
        }
    }
}
