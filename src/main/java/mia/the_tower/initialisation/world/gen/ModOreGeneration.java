package mia.the_tower.initialisation.world.gen;

import mia.the_tower.initialisation.biomes.CeruleanCoast;
import mia.the_tower.initialisation.biomes.GoldenWeald;
import mia.the_tower.initialisation.biomes.Shadowlands;
import mia.the_tower.initialisation.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.gen.GenerationStep;

public class ModOreGeneration {
    public static void generateOres() {
        //tell where your ore generates in here
        //for multiple biomes, you just include a , after the first one and write another
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(CeruleanCoast.CERULEAN_COAST), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.DRAGONS_EYE_ORE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(CeruleanCoast.CERULEAN_COAST), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.AFHD_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Shadowlands.SHADOWLANDS), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.ITORE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(CeruleanCoast.CERULEAN_COAST), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.SILVER_ORE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(CeruleanCoast.CERULEAN_COAST), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.SLATE_SHEET_ORE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(CeruleanCoast.CERULEAN_COAST), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.SLATE_SHEET_ORE_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(GoldenWeald.GOLDEN_WEALD), GenerationStep.Feature.UNDERGROUND_ORES,
                ModPlacedFeatures.KAOLIN_SHEET_ORE_PLACED_KEY);

        //then last of all, go to the biome class and include the ore there
    }
}
