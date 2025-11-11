package mia.the_tower.initialisation.world;

import mia.the_tower.initialisation.block_init;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.List;
//relevant mc files to look at: VegetationPlacedFeatures
public class ModPlacedFeatures {

    //features need a line here

    //ore
    public static final RegistryKey<PlacedFeature> AFHD_PLACED_KEY = registerKey("afhd_placed");
    public static final RegistryKey<PlacedFeature> DRAGONS_EYE_ORE_PLACED_KEY = registerKey("dragons_eye_ore_placed");
    public static final RegistryKey<PlacedFeature> ITORE_PLACED_KEY = registerKey("itore_placed");
    public static final RegistryKey<PlacedFeature> SILVER_ORE_PLACED_KEY = registerKey("silver_ore_placed");
    public static final RegistryKey<PlacedFeature> SILVER_VEIN_PLACED_KEY = registerKey("silver_vein_placed");

    //vegetation
    public static final RegistryKey<PlacedFeature> CERULEAN_GRASS_PATCH_PLACED_KEY = registerKey("cerulean_grass_patch_placed");
    public static final RegistryKey<PlacedFeature> CERULEAN_COAST_FLOWERS_PATCH_PLACED_KEY = registerKey("cerulean_coast_flowers_patch_placed");

    //tree
    public static final RegistryKey<PlacedFeature> GINKGO_PLACED_KEY = registerKey("ginkgo_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        //ore
        //needs a line here
        register(context, AFHD_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.AFHD_KEY),
                ModOrePlacement.modifiersWithCount(3, //veins per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-120), YOffset.fixed(30)))); //first one is min y level, second is max y level
        //you can use other HeightRangePlacementModifiers as well, or make your owm.
        //uniform gives uniform distribution in between min and max y
        //trapezoid gives highest probability in the middle between the two values which decreases either side
        register(context, DRAGONS_EYE_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DRAGONS_EYE_ORE_KEY),
                ModOrePlacement.modifiersWithCount(3, //veins per chunk
                        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-200), YOffset.fixed(-30))));
        register(context, ITORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ITORE_KEY),
                ModOrePlacement.modifiersWithCount(5,
                        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-140), YOffset.fixed(60))));
        register(context, SILVER_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SILVER_ORE_KEY),
                ModOrePlacement.modifiersWithCount(5, //veins per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-120), YOffset.fixed(0))));
        register(context, SILVER_VEIN_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.SILVER_VEIN_KEY),
                ModOrePlacement.modifiersWithCount(2, //veins per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-160), YOffset.fixed(-60))));
        //for ore then we go to gen/ModOreGeneration

        //vegetation
        PlacedFeatures.register(
                context,
                CERULEAN_GRASS_PATCH_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.CERULEAN_GRASS_PATCH_KEY),
                CountPlacementModifier.of(3),
                RarityFilterPlacementModifier.of(2),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of()
        );
        PlacedFeatures.register(
                context,
                CERULEAN_COAST_FLOWERS_PATCH_PLACED_KEY,
                configuredFeatures.getOrThrow(ModConfiguredFeatures.CERULEAN_COAST_FLOWERS_PATCH_KEY),
                CountPlacementModifier.of(3),
                RarityFilterPlacementModifier.of(2),
                SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP,
                BiomePlacementModifier.of()
        );

        //tree
        register(context, GINKGO_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.GINKGO_KEY),
                VegetationPlacedFeatures.treeModifiersWithWouldSurvive(
                        RarityFilterPlacementModifier.of(6), // ~1 attempt per 8 chunks
                        block_init.GINKGO_SAPLING));

        //for any feature then you go to the gen/ModXXXGeneration file, or create it.
        //I am not sure if this is necessary for spawning in custom biomes, or if it is only for vanilla.
        //Best include it anyways I suppose

    }
//this is the method to load a placed feature
    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of("the_tower", name));
    }

    private static void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key, RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<PlacedFeature> context, RegistryKey<PlacedFeature> key,
                                                                                   RegistryEntry<ConfiguredFeature<?, ?>> configuration,
                                                                                   PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }
}