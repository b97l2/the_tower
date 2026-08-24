package mia.the_tower.initialisation.world.ore;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.List;

import static mia.the_tower.The_Tower.id;

public class SheetOreFeature extends Feature<SheetOreFeature.Config> {
    public static record Config(
            List<OreFeatureConfig.Target> targets,
            IntProvider width,
            IntProvider height,
            float density,
            float discardOnAirChance
    ) implements FeatureConfig {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        Codec.list(OreFeatureConfig.Target.CODEC)
                                .fieldOf("targets")
                                .forGetter(Config::targets),
                        IntProvider.createValidatingCodec(1, 128)
                                .fieldOf("width")
                                .forGetter(Config::width),
                        IntProvider.createValidatingCodec(1, 16)
                                .fieldOf("height")
                                .forGetter(Config::height),
                        Codec.floatRange(0.0F, 1.0F)
                                .optionalFieldOf("density", 0.9F)
                                .forGetter(Config::density),
                        Codec.floatRange(0.0F, 1.0F)
                                .optionalFieldOf("discard_chance_on_air_exposure", 0.0F)
                                .forGetter(Config::discardOnAirChance)
                ).apply(instance, Config::new)
        );
    }

    public static final Feature<Config> SHEET_ORE =
            Registry.register(Registries.FEATURE, id("sheet_ore"), new SheetOreFeature());

    public SheetOreFeature() {
        super(Config.CODEC);
    }

    public static void init() {}

    @Override
    public boolean generate(FeatureContext<Config> context) {
        Config config = context.getConfig();
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos origin = context.getOrigin();

        int width = config.width().get(random);
        int height = config.height().get(random);
        if (width <= 0 || height <= 0 || config.targets().isEmpty()) {
            return false;
        }

        int xRadius = Math.max(1, width / 2 + random.nextBetween(-Math.max(1, width / 8), Math.max(1, width / 8)));
        int zRadius = Math.max(1, width / 2 + random.nextBetween(-Math.max(1, width / 8), Math.max(1, width / 8)));
        int baseY = origin.getY() - height / 2;
        int layerRadiusFuzz = Math.max(1, width / 10);
        int layerDrift = Math.max(1, width / 12);
        int placed = 0;

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int dy = 0; dy < height; dy++) {
            int y = baseY + dy;
            if (world.isOutOfHeightLimit(y)) {
                continue;
            }

            int layerXRadius = Math.max(1, xRadius + random.nextBetween(-layerRadiusFuzz, layerRadiusFuzz));
            int layerZRadius = Math.max(1, zRadius + random.nextBetween(-layerRadiusFuzz, layerRadiusFuzz));
            int layerX = origin.getX() + random.nextBetween(-layerDrift, layerDrift);
            int layerZ = origin.getZ() + random.nextBetween(-layerDrift, layerDrift);

            for (int dx = -layerXRadius; dx <= layerXRadius; dx++) {
                double normalizedX = (double) dx / (double) layerXRadius;

                for (int dz = -layerZRadius; dz <= layerZRadius; dz++) {
                    double normalizedZ = (double) dz / (double) layerZRadius;
                    double distance = normalizedX * normalizedX + normalizedZ * normalizedZ;
                    if (distance > 1.0D) {
                        continue;
                    }

                    mutable.set(layerX + dx, y, layerZ + dz);
                    if (!world.isValidForSetBlock(mutable)) {
                        continue;
                    }

                    BlockState currentState = world.getBlockState(mutable);
                    for (OreFeatureConfig.Target target : config.targets()) {
                        if (shouldPlace(currentState, world, random, config, target, mutable)) {
                            world.setBlockState(mutable, target.state, Block.NOTIFY_LISTENERS);
                            placed++;
                            break;
                        }
                    }
                }
            }
        }

        return placed > 0;
    }

    private static boolean shouldPlace(BlockState state, StructureWorldAccess world, Random random, Config config,
                                       OreFeatureConfig.Target target, BlockPos.Mutable pos) {
        if (!target.target.test(state, random)) {
            return false;
        }

        if (config.discardOnAirChance() <= 0.0F || random.nextFloat() >= config.discardOnAirChance()) {
            return true;
        }

        return !Feature.isExposedToAir(world::getBlockState, pos);
    }
}
