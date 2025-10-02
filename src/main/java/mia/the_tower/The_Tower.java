package mia.the_tower;

import mia.the_tower.initialisation.*;
import mia.the_tower.initialisation.biomes.CeruleanCoast;
import mia.the_tower.initialisation.biomes.Shadowlands;
import mia.the_tower.initialisation.biomes.ThePit;
import mia.the_tower.initialisation.block.column_init;
import mia.the_tower.initialisation.block.stake_init;
import mia.the_tower.initialisation.blockentity.blockentity_init;
import mia.the_tower.initialisation.carver.ModCarvers;
import mia.the_tower.initialisation.dimentions.TheFrage;
import mia.the_tower.initialisation.entity.ModEntities;
import mia.the_tower.initialisation.entity.custom.PlateEntity;
import mia.the_tower.initialisation.entity.custom.VoidMothEntity;
import mia.the_tower.initialisation.fluid.GunkFluidBlock;
import mia.the_tower.initialisation.particle.CustomParticles;
import mia.the_tower.initialisation.sounds.CustomSounds;
import mia.the_tower.initialisation.status_effects.*;
import mia.the_tower.initialisation.util.CustomTags;
import mia.the_tower.initialisation.world.gen.ModWorldGeneration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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

		item_init.load(); //this loads all items inside the file item_init into minecraft
		block_init.load();
		crop_init.load();
		column_init.load();
		levitate_init.load();
		pale_death_init.load();
		stake_init.load();
		InstantMineEffect.load();
		DecayStatusEffect.load();
		Shadowlands.load();
		ThePit.load();
		CeruleanCoast.load();
		TheFrage.load();
		CustomItemGroups.load();
		SanguineStatusEffect.load();
		CustomSounds.load();
		CustomParticles.load();
		ModWorldGeneration.generateModWorldGen();
		CustomTags.load();
		ModCarvers.load();
		ModEntities.load();
		//blockentity_init.load();

		//for entities
		FabricDefaultAttributeRegistry.register(ModEntities.VOID_MOTH, VoidMothEntity.createAttributes());

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