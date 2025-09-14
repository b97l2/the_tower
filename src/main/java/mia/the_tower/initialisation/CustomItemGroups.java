package mia.the_tower.initialisation;

import mia.the_tower.The_Tower;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CustomItemGroups {
    public static final ItemGroup FLESH_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of("the_tower", "flesh_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(block_init.CUBE_OF_FLESH))
                    .displayName(Text.translatable("itemgroup.the_tower.flesh_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(block_init.CUBE_OF_FLESH);
                        entries.add(block_init.CUBE_OF_PERCEIVING);
                        entries.add(block_init.FILTH);
                        entries.add(block_init.PULLED_TEETH);
                        entries.add(block_init.SKIN);
                        entries.add(block_init.STITCHED_FLESH);
                        entries.add(block_init.DARKNESS);
                        entries.add(block_init.OXIDISED_DARKNESS);
                        entries.add(block_init.ITORE);

                        //items
                        entries.add(item_init.KLAEN);
                        entries.add(item_init.FINGER);
                        entries.add(item_init.BLOODY_NAIL);
                        entries.add(item_init.PEARL_OF_BLOOD);
                        entries.add(item_init.SANGUINE_FLASK);
                        entries.add(item_init.SANGUINE_SYRINGE);
                        entries.add(item_init.CONTRACT);
                        entries.add(item_init.TOOTH);
                        entries.add(item_init.MY_RAGE);


                        //fluids
                        entries.add(item_init.CHALICE_OF_BLOOD);
                        entries.add(item_init.CUP_OF_GUNK);
                        entries.add(item_init.BUCKET_OF_BILE);
                    }).build());

    public static final ItemGroup CERULEAN_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of("the_tower", "cerulean_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(item_init.GLUH))
                    .displayName(Text.translatable("itemgroup.the_tower.cerulean_items"))
                    .entries((displayContext, entries) -> {
                        //blocks
                        entries.add(block_init.CUBE_OF_GLUH);
                        entries.add(block_init.HEAVENS_EARTH);
                        entries.add(block_init.HEAVENS_LIGHT);
                        entries.add(block_init.POTENT_HEAVENS_LIGHT);
                        entries.add(block_init.INFESTED_ROCK);
                        entries.add(block_init.DRAGONS_EYE_ORE);
                        entries.add(block_init.PETRIFIED_STONE);
                        entries.add(block_init.CRACKED_PETRIFIED_STONE);
                        entries.add(block_init.EMBOSSED_PETRIFIED_STONE);
                        entries.add(block_init.CRACKED_EMBOSSED_PETRIFIED_STONE);
                        entries.add(block_init.TILED_PETRIFIED_STONE);
                        entries.add(block_init.CRACKED_TILED_PETRIFIED_STONE);
                        entries.add(block_init.ADORNED_PETRIFIED_STONE);
                        entries.add(block_init.SALT);
                        entries.add(block_init.AFHD);
                        entries.add(block_init.SLATE);

                        //plants
                        entries.add(block_init.GLUH_VINE_STAGE0);
                        entries.add(block_init.GLUH_VINE_STAGE1);
                        entries.add(block_init.GLUH_VINE_STAGE2);
                        entries.add(block_init.GLUH_VINE_STAGE3);
                        entries.add(block_init.GLUH_VINE_STAGE4);
                        entries.add(block_init.GLUH_VINE_STAGE5);
                        entries.add(block_init.GLUH_VINE_STAGE6);
                        entries.add(block_init.GLUH_VINE_STAGE7);
                        entries.add(block_init.CERULEAN_PETALS);
                        entries.add(block_init.BLUE_FOXGLOVE);
                        entries.add(block_init.CERULEAN_SHORTGRASS);
                        entries.add(block_init.CERULEAN_WILDGRASS);
                        entries.add(block_init.SAPPHIRE_ROSE);
                        entries.add(block_init.BLUE_BELL);
                        entries.add(block_init.STARLIGHT_LICHEN);
                        entries.add(block_init.GABRIELS_TRUMPETS_BUSH);
                        entries.add(block_init.BLUE_HOSTAS);

                        //usable items
                        entries.add(item_init.GLUH);
                        entries.add(item_init.YUMP);
                        entries.add(item_init.BLAJ);
                        entries.add(item_init.HORNWORM);
                        entries.add(item_init.DRAGONS_EYE);
                        entries.add(item_init.MY_FUEL);
                        entries.add(item_init.PILE_OF_SALT);
                        entries.add(item_init.PILLAR_OF_SALT);
                        entries.add(item_init.GABRIELS_TRUMPET);
                        entries.add(item_init.PALE_AXE);
                        entries.add(item_init.SHEPHERDS_STAFF);

                        //craftable items
                        entries.add(block_init.DRAGONS_LEAP);
                        entries.add(block_init.ROD_OF_LUMINESCENCE);
                    }).build());

    public static final ItemGroup FUSION_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of("the_tower", "fusion_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(item_init.STAVE))
                    .displayName(Text.translatable("itemgroup.the_tower.fusion_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(item_init.STAVE);
                        entries.add(item_init.GLOOP);

                        entries.add(block_init.CAN_OF_RETARDATION);
                        entries.add(block_init.SHADOW);
                        entries.add(block_init.JESTERS_HAT);
                        entries.add(block_init.LOAF);
                        entries.add(block_init.TANKARD);
                        entries.add(block_init.SPIRITS);

                        //golden
                        entries.add(block_init.GOLDEN_THURIBLE);
                        entries.add(block_init.GOLD_SLAB);
                        entries.add(block_init.GOLD_STAIRS);
                        entries.add(block_init.GOLD_COLUMN);
                        entries.add(block_init.GOLD_WALL);

                        //stone
                        entries.add(column_init.STONE_COLUMN);
                        entries.add(column_init.COBBLESTONE_COLUMN);
                        entries.add(column_init.STONE_BRICK_COLUMN);
                        entries.add(column_init.SMOOTH_STONE_COLUMN);
                        entries.add(column_init.COBBLED_DEEPSLATE_COLUMN);
                        entries.add(column_init.POLISHED_DEEPSLATE_COLUMN);
                        entries.add(column_init.DEEPSLATE_BRICK_COLUMN);
                        entries.add(column_init.DEEPSLATE_TILE_COLUMN);
                        entries.add(column_init.BLACKSTONE_COLUMN);
                        entries.add(column_init.POLISHED_BLACKSTONE_BRICK_COLUMN);
                        entries.add(column_init.POLISHED_BLACKSTONE_COLUMN);




                    }).build());


    public static void load() {
        The_Tower.LOGGER.info("Registering Item Groups for " + "the_tower");
    }
}