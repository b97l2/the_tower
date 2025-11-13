package mia.the_tower.initialisation.blockentity;

import mia.the_tower.The_Tower;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import mia.the_tower.initialisation.block_init;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class blockentity_init {

    //for a block entity you need to register the block in block_init and register the block entity here as well (top and bottom).

    public static BlockEntityType<ColouredCofferBlockEntity> COLORED_COFFER;
    public static BlockEntityType<StorageTemplateBlockEntity> GRAVESTONE;

    private static Block[] cofferBlocks() {
        return new Block[] {
                block_init.WHITE_COFFER,
                block_init.ORANGE_COFFER,
                block_init.MAGENTA_COFFER,
                block_init.LIGHT_BLUE_COFFER,
                block_init.YELLOW_COFFER,
                block_init.LIME_COFFER,
                block_init.PINK_COFFER,
                block_init.GREY_COFFER,
                block_init.LIGHT_GREY_COFFER,
                block_init.CYAN_COFFER,
                block_init.PURPLE_COFFER,
                block_init.BLUE_COFFER,
                block_init.BROWN_COFFER,
                block_init.GREEN_COFFER,
                block_init.RED_COFFER,
                block_init.BLACK_COFFER
        };
    }


    public static void register() {
        COLORED_COFFER = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                The_Tower.id("coloured_coffer"),
                FabricBlockEntityTypeBuilder.create(ColouredCofferBlockEntity::new, cofferBlocks()).build(null)
        );
        GRAVESTONE = Registry.register(
                Registries.BLOCK_ENTITY_TYPE,
                The_Tower.id("gravestone"),
                FabricBlockEntityTypeBuilder.create(StorageTemplateBlockEntity::new, block_init.GRAVESTONE).build(null)
        );

        The_Tower.LOGGER.info("[the_tower] COLORED_COFFER registered: {}", COLORED_COFFER);
        The_Tower.LOGGER.info("[the_tower] COLORED_COFFER supports {} blocks", cofferBlocks().length);
    }

    public static void load() {
        register();
    }
}