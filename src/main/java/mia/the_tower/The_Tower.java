package mia.the_tower;

import mia.the_tower.initialisation.*;
import mia.the_tower.initialisation.biomes.CeruleanCoast;
import mia.the_tower.initialisation.biomes.GoldenWeald;
import mia.the_tower.initialisation.biomes.Shadowlands;
import mia.the_tower.initialisation.biomes.ThePit;
import mia.the_tower.initialisation.block.column_init;
import mia.the_tower.initialisation.block.stake_init;
import mia.the_tower.initialisation.blockentity.blockentity_init;
import mia.the_tower.initialisation.carver.ModCarvers;
import mia.the_tower.initialisation.dimentions.TheFrage;
import mia.the_tower.initialisation.entity.ModEntities;
import mia.the_tower.initialisation.entity.custom.LampFlyEntity;
import mia.the_tower.initialisation.entity.custom.VoidMothEntity;
import mia.the_tower.initialisation.fluid.GunkFluidBlock;
import mia.the_tower.initialisation.items.util.CofferToKey;
import mia.the_tower.initialisation.misc.ModDeaths;
import mia.the_tower.initialisation.particle.CustomParticles;
import mia.the_tower.initialisation.screen.screen_init;
import mia.the_tower.initialisation.sounds.CustomSounds;
import mia.the_tower.initialisation.status_effects.*;
import mia.the_tower.initialisation.util.CustomTags;
import mia.the_tower.initialisation.world.gen.ModWorldGeneration;

import mia.the_tower.initialisation.world.tree.GiantFoliagePlacer;
import mia.the_tower.initialisation.world.tree.GiantTrunkPlacer;
import mia.the_tower.initialisation.world.tree.MediumTrunkPlacer;
import mia.the_tower.initialisation.world.vegetation.TripleAwareSimpleBlockFeature;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

import static mia.the_tower.initialisation.status_effects.pale_death_init.PALE_DEATH;

public class The_Tower implements ModInitializer {
	public static final String MOD_ID = "the_tower"; //this is to make calling the name easier i think?

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final WeakHashMap<ServerPlayerEntity, GameMode> spectatingPlayers = new WeakHashMap<>();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

	 //this loads all items inside the file item_init into minecraft
		item_init.load();
		block_init.load();
		blockentity_init.load();
		crop_init.load();
		ModCarvers.load();
		column_init.load();
		levitate_init.load();
		pale_death_init.load();
		stake_init.load();
		InstantMineEffect.load();
		DecayStatusEffect.load();
		ThePit.load();
		Shadowlands.load();
		CeruleanCoast.load();
		TheFrage.load();
		CustomItemGroups.load();
		SanguineStatusEffect.load();
		CustomSounds.load();
		CustomParticles.load();
		ModWorldGeneration.generateModWorldGen();
		CustomTags.load();
		ModEntities.load();
		screen_init.load();
		CofferToKey.load();
		ModDeaths.init();
		GiantTrunkPlacer.init();
		MediumTrunkPlacer.init();
		GiantFoliagePlacer.init();
		GoldenWeald.load();
		TripleAwareSimpleBlockFeature.init();

		//for flammable blocks
		FlammableBlockRegistry.getDefaultInstance().add(block_init.GINKGO_LOG, 5, 5);
		FlammableBlockRegistry.getDefaultInstance().add(block_init.GINKGO_LEAVES, 40, 60);

		//for strippable blocks
		//StrippableBlockRegistry.register(block_init.GINKGO_LOG, block_init.STRIPPED_GINKGO_LOG);

		//for entities
		FabricDefaultAttributeRegistry.register(ModEntities.VOID_MOTH, VoidMothEntity.createAttributes());

		FabricDefaultAttributeRegistry.register(ModEntities.LAMP_FLY, LampFlyEntity.createAttributes());

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			Iterator<Map.Entry<ServerPlayerEntity, GameMode>> iterator = spectatingPlayers.entrySet().iterator();

			while (iterator.hasNext()) {
				Map.Entry<ServerPlayerEntity, GameMode> entry = iterator.next();
				ServerPlayerEntity player = entry.getKey();

				if (!player.hasStatusEffect(PALE_DEATH)) {
					GameMode original = entry.getValue();
					player.changeGameMode(original);
					iterator.remove();
				}
			}
		});

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			if (world.isClient()) return;

			for (PlayerEntity player : world.getPlayers()) {
				if (!GunkFluidBlock.playerTimeInside.containsKey(player)) continue;

				// Increase outside timer
				int outsideTime = GunkFluidBlock.playerTimeOutside.getOrDefault(player, 0) + 1;
				GunkFluidBlock.playerTimeOutside.put(player, outsideTime);

				// If outside for 10 seconds (200 ticks), reset
				if (outsideTime >= 200) {
					GunkFluidBlock.playerTimeInside.remove(player);
					GunkFluidBlock.playerTimeOutside.remove(player);
				}
			}
		});

		LOGGER.info("Hello Fabric world!");
	}

	public static Identifier id(String path) { //this is to name things easily i think
		return Identifier.of(MOD_ID, path);
	}

	public static void setSpectator(ServerPlayerEntity player, GameMode originalMode) {
		if (!spectatingPlayers.containsKey(player)) {
			spectatingPlayers.put(player, originalMode);
		}
	}

	public static boolean isSpectating(ServerPlayerEntity player) {
		return spectatingPlayers.containsKey(player);
	}

}