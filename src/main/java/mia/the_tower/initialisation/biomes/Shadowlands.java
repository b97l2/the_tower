package mia.the_tower.initialisation.biomes;

import mia.the_tower.initialisation.world.ModPlacedFeatures;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.DefaultBiomeFeatures;
import net.minecraft.world.biome.BiomeEffects;
import net.minecraft.sound.BiomeMoodSound;
import net.minecraft.world.gen.feature.VegetationPlacedFeatures;

//to create a new biome, it needs one of these, it needs its own line in The_TowerDataGenerator, load() needs to be called, and it needs
//to be mentioned in the frage class

public class Shadowlands {

    public static final RegistryKey<Biome> SHADOWLANDS = RegistryKey.of(RegistryKeys.BIOME, Identifier.of("the_tower", "shadowlands"));

    public static void bootstrap(Registerable<Biome> context) {
        context.register(SHADOWLANDS, createShadowlands(context));
    }

    public static Biome createShadowlands(Registerable<Biome> context) {
        SpawnSettings.Builder spawnBuilder = new SpawnSettings.Builder();
        //spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(ModEntities.PORCUPINE, 2, 3, 5)); //spawning mod entities

        spawnBuilder.spawn(SpawnGroup.CREATURE, new SpawnSettings.SpawnEntry(EntityType.WOLF, 5, 4, 4));

        DefaultBiomeFeatures.addFarmAnimals(spawnBuilder);
        DefaultBiomeFeatures.addBatsAndMonsters(spawnBuilder);

        GenerationSettings.LookupBackedBuilder biomeBuilder =
                new GenerationSettings.LookupBackedBuilder(context.getRegistryLookup(RegistryKeys.PLACED_FEATURE),
                        context.getRegistryLookup(RegistryKeys.CONFIGURED_CARVER));

        //globalOverworldGeneration(biomeBuilder); //this is for if you want it to spawn in the overworld
        DefaultBiomeFeatures.addMossyRocks(biomeBuilder);
        biomeBuilder.feature(GenerationStep.Feature.UNDERGROUND_ORES, ModPlacedFeatures.ITORE_PLACED_KEY);

        biomeBuilder.feature(GenerationStep.Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.TREES_PLAINS);
        DefaultBiomeFeatures.addForestFlowers(biomeBuilder);
        DefaultBiomeFeatures.addLargeFerns(biomeBuilder);

        DefaultBiomeFeatures.addDefaultMushrooms(biomeBuilder);
        DefaultBiomeFeatures.addDefaultVegetation(biomeBuilder);

        return new Biome.Builder()
                .precipitation(true)
                .downfall(0.4f)
                .temperature(0.7f)
                .generationSettings(biomeBuilder.build())
                .spawnSettings(spawnBuilder.build())
                .effects((new BiomeEffects.Builder())
                        .waterColor(0x19191a)
                        .waterFogColor(0x0a0a0a)
                        .skyColor(0x0a0a0a)
                        .grassColor(0x2b2b2b)
                        .foliageColor(0x35384a)
                        .fogColor(0x35384a)
                        .moodSound(BiomeMoodSound.CAVE)
                        //.music(MusicType.createIngameMusic(RegistryEntry.of(ModSounds.BAR_BRAWL))) //custom music
                        .build())
                .build();
    }

    public static void load(){}

}
