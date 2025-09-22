package mia.the_tower.initialisation.carver;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.CarverConfig;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.heightprovider.HeightProvider;


/**
 * Extends CarverConfig to satisfy Carver<C extends CarverConfig>.
 * We reuse the parent fields (probability, y, yScale, lavaLevel, debugConfig, replaceable)
 * and add our own.
 */


public class PitCarverConfig extends CarverConfig {

    public static final MapCodec<PitCarverConfig> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            CarverConfig.CONFIG_CODEC.forGetter(c -> c),
            // Custom fields
            IntProvider.VALUE_CODEC.fieldOf("radius").forGetter(c -> c.radius),
            IntProvider.VALUE_CODEC.fieldOf("depth").forGetter(c -> c.depth),
            IntProvider.VALUE_CODEC.fieldOf("taper_height").forGetter(c -> c.taperHeight),
            IntProvider.VALUE_CODEC.fieldOf("radius_jitter").forGetter(c -> c.radiusJitter),
            FloatProvider.VALUE_CODEC.fieldOf("horizontal_jitter_per_y").forGetter(c -> c.horizontalJitterPerY)
    ).apply(inst, (base, radius, depth, taper, jitter, hJitter) ->
            new PitCarverConfig(
                    base.probability, base.y, base.yScale, base.lavaLevel, base.debugConfig, base.replaceable,
                    radius, depth, taper, jitter, hJitter
            )
    ));

    // Expose a plain Codec for the Carver constructor
    public static final Codec<PitCarverConfig> DIRECT_CODEC = CODEC.codec();

    public final IntProvider radius;
    public final IntProvider depth;
    public final IntProvider taperHeight;
    public final IntProvider radiusJitter;
    public final FloatProvider horizontalJitterPerY;

    public PitCarverConfig(
            float probability,
            HeightProvider y,
            FloatProvider yScale,
            YOffset lavaLevel,
            CarverDebugConfig debugConfig,
            RegistryEntryList<Block> replaceable,
            IntProvider radius,
            IntProvider depth,
            IntProvider taperHeight,
            IntProvider radiusJitter,
            FloatProvider horizontalJitterPerY
    ) {
        super(probability, y, yScale, lavaLevel, debugConfig, replaceable);
        this.radius = radius;
        this.depth  = depth;
        this.taperHeight = taperHeight;
        this.radiusJitter = radiusJitter;
        this.horizontalJitterPerY = horizontalJitterPerY;
    }


}


