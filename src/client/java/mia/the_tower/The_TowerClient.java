package mia.the_tower;

import mia.the_tower.entity.plate.PlateRenderer;
import mia.the_tower.entity.void_moth.VoidMothModel;
import mia.the_tower.entity.void_moth.VoidMothRenderer;
import mia.the_tower.initialisation.*;
import mia.the_tower.initialisation.block.stake_init;
import mia.the_tower.initialisation.entity.ModEntities;
import mia.the_tower.initialisation.particle.CustomParticles;
import mia.the_tower.particle.*;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.ItemFrameEntityRenderer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;


public class The_TowerClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.

		// this is for transparent or translucent blocks
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.HEAVENS_LIGHT, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.POTENT_HEAVENS_LIGHT, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.HEAVENS_EARTH, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(), crop_init.GLUH_VINE);
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.GLUH_VINE_STAGE0, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.GLUH_VINE_STAGE1, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.GLUH_VINE_STAGE2, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.GLUH_VINE_STAGE3, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.GLUH_VINE_STAGE4, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.GLUH_VINE_STAGE5, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.GLUH_VINE_STAGE6, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.GLUH_VINE_STAGE7, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.BARRIER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(stake_init.STAKE_BUD, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(stake_init.STAKE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.CERULEAN_PETALS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.SAPPHIRE_ROSE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.DRAGONS_LEAP, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.JESTERS_HAT, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.BLUE_FOXGLOVE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.CERULEAN_SHORTGRASS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.ROD_OF_LUMINESCENCE, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.BLOOD, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putFluids(
				RenderLayer.getTranslucent(),
				block_init.STILL_BLOOD,
				block_init.FLOWING_BLOOD
		);
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.GABRIELS_TRUMPETS_BUSH, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.SHADOW, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.FRAGE_MARKER, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.BLUE_HOSTAS, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.GOLDEN_THURIBLE, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.SPIRITS, RenderLayer.getTranslucent());
		BlockRenderLayerMap.INSTANCE.putBlock(block_init.SAUERKRAUT, RenderLayer.getTranslucent());

		//this is for rendering fluids
		FluidRenderHandlerRegistry.INSTANCE.register(
				block_init.STILL_BLOOD,
				block_init.FLOWING_BLOOD,
				new SimpleFluidRenderHandler(
						The_Tower.id("block/blood"),
						The_Tower.id("block/blood"),
						0xFFFFFF
				)
		);

		FluidRenderHandlerRegistry.INSTANCE.register(
				block_init.STILL_GUNK,
				block_init.FLOWING_GUNK,
				new SimpleFluidRenderHandler(
						The_Tower.id("block/gunk_still"),
						The_Tower.id("block/gunk_still"),
						0xFFFFFF
				)
		);


		//this is for rendering particles
		ParticleFactoryRegistry.getInstance().register(CustomParticles.EMBERS_OF_THE_FRAGE, EmbersOfTheFrage.Factory::new);
		ParticleFactoryRegistry.getInstance().register(CustomParticles.UNDERBLOOD, Underblood.Factory::new);
		ParticleFactoryRegistry.getInstance().register(CustomParticles.GOLD_INCENSE, GoldIncense.Factory::new);
		ParticleFactoryRegistry.getInstance().register(CustomParticles.GLUH_PARTICLE, GluhParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(CustomParticles.STAVE_PARTICLE, StaveParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register(CustomParticles.WHITE_INCENSE, WhiteIncense.Factory::new);


		//this is for entities
		EntityModelLayerRegistry.registerModelLayer(VoidMothModel.VOID_MOTH, VoidMothModel::getTexturedModelData);
		EntityRendererRegistry.register(ModEntities.VOID_MOTH, VoidMothRenderer::new);

		EntityRendererRegistry.register(ModEntities.PLATE, PlateRenderer::new);
		ModelLoadingPlugin.register(ctx -> {
			ctx.addModels(Identifier.of("the_tower", "block/plate"));
			// add more if needed:
			// ctx.addModels(Identifier.of("the_tower", "block/plate_map"));
		});
	}
}