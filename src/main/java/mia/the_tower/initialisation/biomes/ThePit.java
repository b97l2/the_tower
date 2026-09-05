package mia.the_tower.initialisation.biomes;


import mia.the_tower.initialisation.carver.ModCarvers;
import mia.the_tower.initialisation.world.ModPlacedFeatures;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;


public class ThePit {

    public static final RegistryKey<Biome> THE_PIT = RegistryKey.of(RegistryKeys.BIOME, Identifier.of("the_tower", "the_pit"));

    public static void bootstrap(Registerable<Biome> context) {
        context.register(THE_PIT, createThePit(context));
    }

    public static Biome createThePit(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        //spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(ModEntities.PORCUPINE, 2, 3, 5)); //spawning mod entities

        //general
        var placedLookup = context.getRegistryLookup(RegistryKeys.PLACED_FEATURE); //?
        var carverLookup  = context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER);

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(placedLookup, carverLookup);

        biomeBuilder.carver(ModCarvers.PIT_CARVER_KEY);

        biomeBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, ModPlacedFeatures.PILE_OF_MEAT_PLACED_KEY);
        //biomeBuilder.feature(GenerationStep.Feature.LAKES, ModPlacedFeatures.DECORATED_PIT_PLACED_KEY);

        return new Biome.Builder()
                .precipitation(false)
                .downfall(0.4f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(0x470404)
                        .waterFogColor(0x470404)
                        .skyColor(0x470404)
                        .grassColor(0x470404)
                        .foliageColor(0x470404)
                        .fogColor(0x470404)
                        .moodSound(BiomeMoodSound.CAVE)
                        //.music(MusicType.createIngameMusic(RegistryEntry.of(ModSounds.BAR_BRAWL))) //custom music
                        .build())
                .build();
    }

    public static void load(){}

}
