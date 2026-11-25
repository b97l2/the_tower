package mia.the_tower.initialisation;

import mia.the_tower.The_Tower;
import mia.the_tower.initialisation.block.column_init;
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
                        entries.add(block_init.CUBE_OF_FLESH_STAIRS);
                        entries.add(block_init.CUBE_OF_FLESH_SLAB);
                        entries.add(block_init.CUBE_OF_FLESH_VERTICAL_SLAB);
                        entries.add(block_init.CUBE_OF_FLESH_WALL);
                        entries.add(block_init.CUBE_OF_FLESH_COLUMN);
                        entries.add(block_init.CUBE_OF_PERCEIVING);
                        entries.add(block_init.FILTH);
                        entries.add(block_init.PULLED_TEETH);
                        entries.add(block_init.SKIN);
                        entries.add(block_init.STITCHED_FLESH);
                        entries.add(block_init.ITORE);
                        entries.add(block_init.GRAVESTONE);

                        //plants
                        entries.add(block_init.VEINS);
                        entries.add(block_init.PILE_OF_MEAT);

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
                        entries.add(item_init.INTESTINE);
                        entries.add(item_init.TOMATO);


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
                        entries.add(block_init.DARKNESS);
                        entries.add(block_init.OXIDISED_DARKNESS);
                        entries.add(block_init.BLAJ_ORE);
                        entries.add(block_init.BLAJ_LESSER_ORE);
                        entries.add(block_init.INFESTED_ROCK);
                        entries.add(block_init.DRAGONS_EYE_ORE);
                        entries.add(block_init.SILVER_ORE);
                        entries.add(block_init.SILVER_VEIN);
                        entries.add(block_init.SILVER_BLOCK);
                        entries.add(block_init.SILVER_WALL);
                        entries.add(block_init.SILVER_COLUMN);
                        entries.add(block_init.SILVER_SLAB);
                        entries.add(block_init.SILVER_VERTICAL_SLAB);
                        entries.add(block_init.SILVER_STAIRS);
                        entries.add(block_init.RAW_SILVER_BLOCK);
                        entries.add(block_init.PETRIFIED_STONE);
                        entries.add(block_init.CRACKED_PETRIFIED_STONE);
                        entries.add(block_init.EMBOSSED_PETRIFIED_STONE);
                        entries.add(block_init.CRACKED_EMBOSSED_PETRIFIED_STONE);
                        entries.add(block_init.TILED_PETRIFIED_STONE);
                        entries.add(block_init.CRACKED_TILED_PETRIFIED_STONE);
                        entries.add(block_init.ADORNED_PETRIFIED_STONE);
                        entries.add(block_init.PETRIFIED_STONE_STAIRS);
                        entries.add(block_init.PETRIFIED_STONE_WALL);
                        entries.add(block_init.PETRIFIED_STONE_COLUMN);
                        entries.add(block_init.PETRIFIED_STONE_SLAB);
                        entries.add(block_init.PETRIFIED_STONE_VERTICAL_SLAB);
                        entries.add(block_init.EMBOSSED_PETRIFIED_STONE_STAIRS);
                        entries.add(block_init.EMBOSSED_PETRIFIED_STONE_WALL);
                        entries.add(block_init.EMBOSSED_PETRIFIED_STONE_COLUMN);
                        entries.add(block_init.EMBOSSED_PETRIFIED_STONE_SLAB);
                        entries.add(block_init.EMBOSSED_PETRIFIED_STONE_VERTICAL_SLAB);
                        entries.add(block_init.TILED_PETRIFIED_STONE_STAIRS);
                        entries.add(block_init.TILED_PETRIFIED_STONE_WALL);
                        entries.add(block_init.TILED_PETRIFIED_STONE_COLUMN);
                        entries.add(block_init.TILED_PETRIFIED_STONE_SLAB);
                        entries.add(block_init.TILED_PETRIFIED_STONE_VERTICAL_SLAB);
                        entries.add(block_init.ADORNED_PETRIFIED_STONE_STAIRS);
                        entries.add(block_init.ADORNED_PETRIFIED_STONE_WALL);
                        entries.add(block_init.ADORNED_PETRIFIED_STONE_COLUMN);
                        entries.add(block_init.ADORNED_PETRIFIED_STONE_SLAB);
                        entries.add(block_init.ADORNED_PETRIFIED_STONE_VERTICAL_SLAB);
                        entries.add(block_init.GROTESQUE);
                        entries.add(block_init.OLD_SOIL);
                        entries.add(block_init.OLD_MEADOW);
                        entries.add(block_init.SALT);
                        entries.add(block_init.AFHD);
                        entries.add(block_init.SLATE);

                        //plants
                        entries.add(item_init.SKYNUT);
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
                        entries.add(item_init.TWINTAIL_BERRY);
                        entries.add(item_init.YUMP);
                        entries.add(item_init.BLAJ);
                        entries.add(item_init.HORNWORM);
                        entries.add(item_init.LAMP_FLY_IN_A_BOTTLE);
                        entries.add(item_init.DRAGONS_EYE);
                        entries.add(item_init.MY_FUEL);
                        entries.add(item_init.FATIGUE);
                        entries.add(item_init.PILE_OF_SALT);
                        entries.add(item_init.PILLAR_OF_SALT);
                        entries.add(item_init.GABRIELS_TRUMPET);
                        entries.add(item_init.PALE_AXE);
                        entries.add(item_init.SHEPHERDS_STAFF);
                        entries.add(item_init.CIRCLET_OF_GLUT);
                        entries.add(item_init.DIVINING_ROD);
                        entries.add(item_init.AZURE_WAND);
                        entries.add(item_init.VOID_MOTH_SPAWN_EGG);
                        entries.add(item_init.LAMP_FLY_SPAWN_EGG);
                        entries.add(item_init.KEY);
                        entries.add(item_init.WHITE_KEY);
                        entries.add(item_init.ORANGE_KEY);
                        entries.add(item_init.MAGENTA_KEY);
                        entries.add(item_init.LIGHT_BLUE_KEY);
                        entries.add(item_init.YELLOW_KEY);
                        entries.add(item_init.LIME_KEY);
                        entries.add(item_init.PINK_KEY);
                        entries.add(item_init.GREY_KEY);
                        entries.add(item_init.LIGHT_GREY_KEY);
                        entries.add(item_init.CYAN_KEY);
                        entries.add(item_init.PURPLE_KEY);
                        entries.add(item_init.BLUE_KEY);
                        entries.add(item_init.BROWN_KEY);
                        entries.add(item_init.GREEN_KEY);
                        entries.add(item_init.RED_KEY);
                        entries.add(item_init.BLACK_KEY);

                        //non-usable items
                        entries.add(item_init.SUPERIOR_BLAJ_ORE_ITEM);
                        entries.add(item_init.LESSER_BLAJ_ORE_ITEM);
                        entries.add(item_init.ITORE_ORE);
                        entries.add(item_init.RAW_SILVER);
                        entries.add(item_init.RAW_SILVER_NUGGET);
                        entries.add(item_init.SILVER_INGOT);
                        entries.add(item_init.SILVER_WIRE);
                        entries.add(item_init.RUFESCENT_PEARL);

                        //craftable items
                        entries.add(block_init.DRAGONS_LEAP);
                        entries.add(block_init.ROD_OF_LUMINESCENCE);
                        entries.add(block_init.SIMPLE_COFFER);
                        entries.add(block_init.SILVER_LANTERN);
                        entries.add(block_init.SILVER_THURIBLE);
                    }).build());

    public static final ItemGroup FUSION_ITEM_GROUP = Registry.register(Registries.ITEM_GROUP,
            Identifier.of("the_tower", "fusion_items"),
            FabricItemGroup.builder().icon(() -> new ItemStack(item_init.STAVE))
                    .displayName(Text.translatable("itemgroup.the_tower.fusion_items"))
                    .entries((displayContext, entries) -> {
                        entries.add(item_init.STAVE);
                        entries.add(item_init.GLOOP);
                        entries.add(item_init.CHOCOLATE_BAR);
                        entries.add(item_init.TOAST);
                        entries.add(item_init.SAUSAGE);
                        entries.add(item_init.FULL_BREAKFAST);
                        entries.add(item_init.TOFFEE_APPLE);

                        entries.add(block_init.CAN_OF_RETARDATION);
                        entries.add(block_init.SHADOW);
                        entries.add(block_init.JESTERS_HAT);
                        entries.add(block_init.LOAF);
                        entries.add(block_init.TANKARD);
                        entries.add(block_init.SPIRITS);
                        entries.add(block_init.SAUERKRAUT);
                        entries.add(item_init.PLATE);

                        //golden
                        entries.add(block_init.GOLDEN_THURIBLE);
                        entries.add(block_init.GOLD_SLAB);
                        entries.add(block_init.GOLD_VERTICAL_SLAB);
                        entries.add(block_init.GOLD_STAIRS);
                        entries.add(block_init.GOLD_COLUMN);
                        entries.add(block_init.GOLD_WALL);
                        entries.add(block_init.GOLD_BARS);
                        entries.add(block_init.GOLD_TRAPDOOR);
                        entries.add(block_init.GOLD_LADDER);
                        entries.add(block_init.GOLD_BUTTON);
                        entries.add(item_init.GOLDEN_SEED);
                        entries.add(item_init.GINKGO_LEAF);
                        entries.add(block_init.GINKGO_LOG);
                        entries.add(block_init.STRIPPED_GINKGO_LOG);
                        entries.add(block_init.GINKGO_PLANKS);
                        entries.add(block_init.GINKGO_SLAB);
                        entries.add(block_init.GINKGO_VERTICAL_SLAB);
                        entries.add(block_init.GINKGO_STAIRS);
                        entries.add(block_init.GINKGO_TRAPDOOR);
                        entries.add(block_init.GINKGO_BUTTON);
                        entries.add(block_init.GINKGO_LEAVES);
                        entries.add(item_init.GINKGO_FRUIT);
                        entries.add(block_init.CHANTERELLE);
                        entries.add(item_init.LAVALIERE_OF_THE_GOLDEN_SUN);
                        entries.add(block_init.SUNKISSED_SHORTGRASS);

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

                        //jade
                        entries.add(item_init.MYSTERY_JADE);
                        entries.add(block_init.IMPERIAL_JADE_BLOCK);
                        entries.add(block_init.IMPERIAL_JADE_STAIRS);
                        entries.add(block_init.IMPERIAL_JADE_SLAB);
                        entries.add(block_init.IMPERIAL_JADE_VERTICAL_SLAB);
                        entries.add(block_init.IMPERIAL_JADE_WALL);
                        entries.add(block_init.IMPERIAL_JADE_COLUMN);
                        entries.add(block_init.BLOOD_JADE_BLOCK);
                        entries.add(block_init.BLOOD_JADE_STAIRS);
                        entries.add(block_init.BLOOD_JADE_SLAB);
                        entries.add(block_init.BLOOD_JADE_VERTICAL_SLAB);
                        entries.add(block_init.BLOOD_JADE_WALL);
                        entries.add(block_init.BLOOD_JADE_COLUMN);
                        entries.add(block_init.UNCUT_JADEITE_STONE);
                        entries.add(block_init.JADEITE_BLOCK);
                        entries.add(block_init.JADEITE_STAIRS);
                        entries.add(block_init.JADEITE_SLAB);
                        entries.add(block_init.JADEITE_VERTICAL_SLAB);
                        entries.add(block_init.JADEITE_WALL);
                        entries.add(block_init.JADEITE_COLUMN);
                        entries.add(block_init.MUTTON_FAT_JADE_BLOCK);
                        entries.add(block_init.MUTTON_FAT_JADE_STAIRS);
                        entries.add(block_init.MUTTON_FAT_JADE_SLAB);
                        entries.add(block_init.MUTTON_FAT_JADE_VERTICAL_SLAB);
                        entries.add(block_init.MUTTON_FAT_JADE_WALL);
                        entries.add(block_init.MUTTON_FAT_JADE_COLUMN);
                        entries.add(block_init.LAVENDER_JADE_BLOCK);
                        entries.add(block_init.LAVENDER_JADE_STAIRS);
                        entries.add(block_init.LAVENDER_JADE_SLAB);
                        entries.add(block_init.LAVENDER_JADE_VERTICAL_SLAB);
                        entries.add(block_init.LAVENDER_JADE_WALL);
                        entries.add(block_init.LAVENDER_JADE_COLUMN);
                        entries.add(block_init.LOTUS_JADE_BLOCK);
                        entries.add(block_init.LOTUS_JADE_STAIRS);
                        entries.add(block_init.LOTUS_JADE_SLAB);
                        entries.add(block_init.LOTUS_JADE_VERTICAL_SLAB);
                        entries.add(block_init.LOTUS_JADE_WALL);
                        entries.add(block_init.LOTUS_JADE_COLUMN);
                        entries.add(block_init.PHOENIX_BLOOD_JADE_BLOCK);
                        entries.add(block_init.PHOENIX_BLOOD_JADE_STAIRS);
                        entries.add(block_init.PHOENIX_BLOOD_JADE_SLAB);
                        entries.add(block_init.PHOENIX_BLOOD_JADE_VERTICAL_SLAB);
                        entries.add(block_init.PHOENIX_BLOOD_JADE_WALL);
                        entries.add(block_init.PHOENIX_BLOOD_JADE_COLUMN);
                        entries.add(block_init.HEAVENLY_DEW_JADE_BLOCK);
                        entries.add(block_init.HEAVENLY_DEW_JADE_STAIRS);
                        entries.add(block_init.HEAVENLY_DEW_JADE_SLAB);
                        entries.add(block_init.HEAVENLY_DEW_JADE_VERTICAL_SLAB);
                        entries.add(block_init.HEAVENLY_DEW_JADE_WALL);
                        entries.add(block_init.HEAVENLY_DEW_JADE_COLUMN);
                        entries.add(block_init.GOLDEN_THREAD_JADE_BLOCK);
                        entries.add(block_init.GOLDEN_THREAD_JADE_STAIRS);
                        entries.add(block_init.GOLDEN_THREAD_JADE_SLAB);
                        entries.add(block_init.GOLDEN_THREAD_JADE_VERTICAL_SLAB);
                        entries.add(block_init.GOLDEN_THREAD_JADE_WALL);
                        entries.add(block_init.GOLDEN_THREAD_JADE_COLUMN);
                        entries.add(block_init.IMPERIAL_JADE_LANTERN);

                        //emerald
                        entries.add(block_init.EMERALD_COLUMN);
                        entries.add(block_init.EMERALD_SLAB);
                        entries.add(block_init.EMERALD_VERTICAL_SLAB);
                        entries.add(block_init.EMERALD_STAIRS);
                        entries.add(block_init.EMERALD_WALL);

                        //porcelain
                        entries.add(item_init.CHUNK_OF_KAOLIN);
                        entries.add(block_init.KAOLIN);
                        entries.add(item_init.PORCELAIN);

                        //redstone
                        entries.add(block_init.REDSTONE_LANTERN);
                        entries.add(block_init.REDSTONE_CHAIN);


                    }).build());


    public static void load() {
        The_Tower.LOGGER.info("Registering Item Groups for " + "the_tower");
    }
}
