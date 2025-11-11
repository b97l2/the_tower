package mia.the_tower.initialisation.biomes;

import mia.the_tower.initialisation.world.ModPlacedFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;

//to create a new biome, it needs one of these, it needs its own line in The_TowerDataGenerator, load() needs to be called, and it needs
//to be mentioned in the_frage json under dimension dir

public class GoldenWeald {

    public static final RegistryKey<Biome> GOLDEN_WEALD = RegistryKey.of(RegistryKeys.BIOME, Identifier.of("the_tower", "golden_weald"));

    public static void bootstrap(Registerable<Biome> context) {
        context.register(GOLDEN_WEALD, createGoldenWeald(context));
    }

    public static Biome createGoldenWeald(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        //spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(ModEntities.PORCUPINE, 2, 3, 5)); //spawning mod entities

        //general
        var placedLookup = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE); //?
        var carverLookup  = context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(placedLookup, carverLookup);

        //for carver
//        RegistryKey<ConfiguredCarver<?>> PIT_CARVER_KEY =
//                RegistryKey.of(RegistryKeys.CONFIGURED_CARVER, Identifier.of("the_tower", "pit_carver"));
//
//        gen.carver(PIT_CARVER_KEY);

        //add ores here as well
        biomeBuilder.feature(
                GenerationStep.Feature.UNDERGROUND_ORES,
                placedLookup.getOrThrow(ModPlacedFeatures.DRAGONS_EYE_ORE_PLACED_KEY)
        );
        biomeBuilder.feature(
                GenerationStep.Feature.UNDERGROUND_ORES,
                placedLookup.getOrThrow(ModPlacedFeatures.SILVER_VEIN_PLACED_KEY)
        );
        biomeBuilder.feature(
                GenerationStep.Feature.UNDERGROUND_ORES,
                placedLookup.getOrThrow(ModPlacedFeatures.SILVER_ORE_PLACED_KEY)
        );

        //globalOverworldGeneration(biomeBuilder); //this is for if you want it to spawn in the overworld
        //DefaultBiomeFeatures.addMossyRocks(biomeBuilder);
        //DefaultBiomeFeatures.addDefaultOres(biomeBuilder);
        //DefaultBiomeFeatures.addExtraGoldOre(biomeBuilder);

        //vegetation
        biomeBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.CERULEAN_GRASS_PATCH_PLACED_KEY);
        biomeBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.GINKGO_PLACED_KEY);

        return new Biome.Builder()
                .precipitation(true)
                .downfall(0f)
                .temperature(0.3f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(0x0c2242)
                        .waterFogColor(0x203554)
                        .skyColor(0x203554)
                        .grassColor(0x143d78)
                        .foliageColor(0x143d78)
                        .fogColor(0x465c7d)
                        //.moodSound(BiomeMoodSound.CAVE)
                        //.music(MusicType.createIngameMusic(RegistryEntry.of(ModSounds.BAR_BRAWL))) //custom music
                        .build())
                .build();
    }

    public static void load(){}

}
