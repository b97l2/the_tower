package mia.the_tower.initialisation.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

public class ModPlacedFeatures {

    //ore needs a line here
    public static final RegistryKey<PlacedFeature> AFHD_PLACED_KEY = registerKey("afhd_placed");
    public static final RegistryKey<PlacedFeature> DRAGONS_EYE_ORE_PLACED_KEY = registerKey("dragons_eye_ore_placed");
    public static final RegistryKey<PlacedFeature> ITORE_PLACED_KEY = registerKey("itore_placed");

    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        //ore needs a line here
        register(context, AFHD_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.AFHD_KEY),
                ModOrePlacement.modifiersWithCount(14, //veins per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-120), YOffset.fixed(30)))); //first one is min y level, second is max y level
        //you can use other HeightRangePlacementModifiers as well, or make your owm.
        //univorm gives univorm distribution in between min and max y
        //trapezoid gives highest probability in the middle between the two values which decreases either side
        register(context, DRAGONS_EYE_ORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.DRAGONS_EYE_ORE_KEY),
                ModOrePlacement.modifiersWithCount(3, //veins per chunk
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-120), YOffset.fixed(0))));
        register(context, ITORE_PLACED_KEY, configuredFeatures.getOrThrow(ModConfiguredFeatures.ITORE_KEY),
                ModOrePlacement.modifiersWithCount(14,
                        HeightRangePlacementModifier.trapezoid(YOffset.fixed(-140), YOffset.fixed(60))));

        //for ore then we go to gen/ModOreGeneration

    }
//this is the method to register a placed feature
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