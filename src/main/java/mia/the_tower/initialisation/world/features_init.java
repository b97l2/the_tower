package mia.the_tower.initialisation.world;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

public class features_init {
    //for registering my custom features


    public static final RegistryKey<Feature<?>> BLAJ_FEATURE_KEY =
            RegistryKey.of(RegistryKeys.FEATURE, Identifier.of("the_tower", "blaj_feature"));

    public static final Feature<DefaultFeatureConfig> BLAJ_FEATURE = new BlajFeature(); // your Feature subclass

    // Called by your RegistryBuilder / bootstrap wiring
    public static void bootstrap(Registerable<Feature<?>> ctx) {
        ctx.register(BLAJ_FEATURE_KEY, BLAJ_FEATURE);
    }


}
