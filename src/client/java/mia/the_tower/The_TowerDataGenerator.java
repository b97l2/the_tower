package mia.the_tower;

import mia.the_tower.initialisation.biomes.*;
import mia.the_tower.initialisation.datagen.ModWorldGenerator;
import mia.the_tower.initialisation.dimentions.TheFrage;
import mia.the_tower.initialisation.world.ModConfiguredFeatures;
import mia.the_tower.initialisation.world.ModPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class The_TowerDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		pack.addProvider(ModWorldGenerator::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) { //need one for datagen here
		registryBuilder.addRegistry(RegistryKeys.BIOME, Shadowlands::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.BIOME, ThePit::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.BIOME, CeruleanCoast::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.DIMENSION_TYPE, TheFrage::bootstrapType);
		registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap);
		registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModPlacedFeatures::bootstrap);
	}
}
