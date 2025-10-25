package mia.the_tower.initialisation.world;

import mia.the_tower.initialisation.block_init;
import mia.the_tower.initialisation.util.CustomTags;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.JungleFoliagePlacer;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.NoiseBlockStateProvider;
import net.minecraft.world.gen.trunk.ForkingTrunkPlacer;

import java.util.List;
//relevant mc files to look at: VegetationConfiguredFeatures
public class ModConfiguredFeatures {

    //for features, start here
    //to load a feature, it needs a line here

    //ore
    public static final RegistryKey<ConfiguredFeature<?, ?>> AFHD_KEY = registerKey("afhd");
    public static final RegistryKey<ConfiguredFeature<?, ?>> DRAGONS_EYE_ORE_KEY = registerKey("dragons_eye_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> ITORE_KEY = registerKey("itore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SILVER_ORE_KEY = registerKey("silver_ore");
    public static final RegistryKey<ConfiguredFeature<?, ?>> SILVER_VEIN_KEY = registerKey("silver_vein");

    //tree
    public static final RegistryKey<ConfiguredFeature<?, ?>> GINKGO_KEY = registerKey("ginkgo");

    //vegetation
    public static final RegistryKey<ConfiguredFeature<?, ?>> CERULEAN_GRASS_PATCH_KEY = registerKey("cerulean_grass_patch");

    //other
    public static final RegistryKey<ConfiguredFeature<?, ?>> BLAJ_KEY = registerKey("blaj");





    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {

        //ore
        //this dictates which blocks the ore can replace
        RuleTest ceruleanCoastReplaceables = new TagMatchRuleTest(CustomTags.Blocks.CERULEAN_COAST_REPLACEABLE);
        RuleTest shadowlandsReplaceables = new TagMatchRuleTest(CustomTags.Blocks.SHADOWLANDS_REPLACEABLE);
        //This is where each type of ore is defined
        List<OreFeatureConfig.Target> frageAfhdOres =
                List.of(OreFeatureConfig.createTarget(ceruleanCoastReplaceables, block_init.AFHD.getDefaultState())
                        //can include other variations of the same ore, eg iron in deepslate and not rock
                        //OreFeatureConfig.createTarget(shadowlandsReplaceables, block_init.RANDOM_ORE.getDefaultState())
                        );
        List<OreFeatureConfig.Target> frageDragonsEyeOres =
                List.of(OreFeatureConfig.createTarget(ceruleanCoastReplaceables, block_init.DRAGONS_EYE_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> frageSilverOres =
                List.of(OreFeatureConfig.createTarget(ceruleanCoastReplaceables, block_init.SILVER_ORE.getDefaultState()));
        List<OreFeatureConfig.Target> frageSilverVeinOres =
                List.of(OreFeatureConfig.createTarget(ceruleanCoastReplaceables, block_init.SILVER_VEIN.getDefaultState()));
        List<OreFeatureConfig.Target> frageItoreOres =
                List.of(OreFeatureConfig.createTarget(shadowlandsReplaceables, block_init.ITORE.getDefaultState()));
        //this defines how big the vein is with the size parameter
        register(context, AFHD_KEY, Feature.ORE, new OreFeatureConfig(frageAfhdOres, 40));
        register(context, DRAGONS_EYE_ORE_KEY, Feature.ORE, new OreFeatureConfig(frageDragonsEyeOres, 9));
        register(context, ITORE_KEY, Feature.ORE, new OreFeatureConfig(frageItoreOres, 3));
        register(context, SILVER_ORE_KEY, Feature.ORE, new OreFeatureConfig(frageSilverOres, 25));
        register(context, SILVER_VEIN_KEY, Feature.ORE, new OreFeatureConfig(frageSilverVeinOres, 9));

        //for tree
        register(context, GINKGO_KEY, Feature.TREE, new TreeFeatureConfig.Builder(
                BlockStateProvider.of(block_init.GINKGO_LOG),
                new ForkingTrunkPlacer(7, 8, 5),

                BlockStateProvider.of(block_init.GINKGO_LEAVES),
                new JungleFoliagePlacer(ConstantIntProvider.create(4), ConstantIntProvider.create(6), 5),

                new TwoLayersFeatureSize(3, 0, 4)).build());

        //for vegetation
        ConfiguredFeatures.register(
                context,
                CERULEAN_GRASS_PATCH_KEY,
                Feature.RANDOM_PATCH,
                ConfiguredFeatures.createRandomPatchFeatureConfig(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(new NoiseBlockStateProvider( //this is for multiple types of blocks
                                2345L,
                                new DoublePerlinNoiseSampler.NoiseParameters(0, 1.0),
                                0.020833334F,
                                List.of(
                                        block_init.CERULEAN_SHORTGRASS.getDefaultState(),
                                        block_init.CERULEAN_WILDGRASS.getDefaultState()
                                )
                        ))));
        //add one here for cerulean petals, also with randomised direction and amount (not sure how to do. look at vanilla)

        //other
        register(context, BLAJ_KEY, features_init.BLAJ_FEATURE, DefaultFeatureConfig.INSTANCE);


        //for features you want naturally generated in the world (incl ore), then we go into ModPlacedFeatures

    }




//this is the method to load a configured feature
    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of("the_tower", name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}