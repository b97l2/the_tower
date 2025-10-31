package mia.the_tower.initialisation.world;

import mia.the_tower.initialisation.block_init;
import mia.the_tower.initialisation.util.CustomTags;
import mia.the_tower.initialisation.world.tree.GiantFoliagePlacer;
import mia.the_tower.initialisation.world.tree.GiantTrunkPlacer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.DarkOakFoliagePlacer;
import net.minecraft.world.gen.foliage.JungleFoliagePlacer;
import net.minecraft.world.gen.foliage.RandomSpreadFoliagePlacer;
import net.minecraft.world.gen.foliage.SpruceFoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.NoiseBlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;
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
    public static final RegistryKey<ConfiguredFeature<?, ?>> CERULEAN_COAST_FLOWERS_PATCH_KEY = registerKey("cerulean_coast_flowers_patch_key");

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
                new GiantTrunkPlacer(5, 88, 95, 20, 3, 0.5f, 3, 0.3f, 3, 0.4f, 0.8f, 3.5f),

                BlockStateProvider.of(block_init.GINKGO_LEAVES),
                new GiantFoliagePlacer(        ConstantIntProvider.create(14),   // radius seed (big crowns)
                        ConstantIntProvider.create(0),    // offset seed
                        UniformIntProvider.create(18, 42),// max_droop: long hanging curtains
                        0.9f,                            // strand_chance (per perimeter leaf)
                        0.90f,                            // strand_continue_chance
                        0.30f,                            // drift_chance
                        0.70f,                            // vertical_taper (radius loss per layer)
                        2                                 // layer_fuzz (+/-)
                        ),

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
        ConfiguredFeatures.register(
                context,
                CERULEAN_COAST_FLOWERS_PATCH_KEY,
                Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(
                        64, // tries per patch
                        6,  // xz spread
                        2,  // y spread
                        PlacedFeatures.createEntry(
                                Feature.SIMPLE_BLOCK,
                                new SimpleBlockFeatureConfig(
                                        new WeightedBlockStateProvider(
                                                DataPool.<BlockState>builder() //this gives weighted distribution within a single patch
                                                        .add(block_init.SAPPHIRE_ROSE.getDefaultState(), 2)
                                                        .add(block_init.BLUE_BELL.getDefaultState(), 3)
                                                        .add(block_init.BLUE_FOXGLOVE.getDefaultState(), 1)
                                                        .add(block_init.BLUE_HOSTAS.getDefaultState(), 4)
                                                        .add(block_init.CERULEAN_PETALS.getDefaultState(), 7)
                                                // add more plants + weights as you like
                                        )
                                ),
                                // Optional: add survival/ground predicates so every variant checks it can live
                                BlockPredicate.wouldSurvive(block_init.BLUE_FOXGLOVE.getDefaultState(), BlockPos.ORIGIN)
                        )
                )
        );
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