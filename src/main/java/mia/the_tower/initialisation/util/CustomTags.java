package mia.the_tower.initialisation.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class CustomTags {
//this is for making a custom tag. Include the tag here and then add it to the proper tags file under data/the_tower/tags
        public static class Blocks {
            public static final TagKey<Block> CERULEAN_COAST_REPLACEABLE = createTag("cerulean_coast_replaceable");
            public static final TagKey<Block> SHADOWLANDS_REPLACEABLE = createTag("shadowlands_replaceable");

    private static TagKey<Block> createTag(String name) {
                return TagKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", name));
            }
        }

        public static class Items {
            public static final TagKey<Item> COFFER_KEYS = createTag("coffer_keys");

            private static TagKey<Item> createTag(String name) {
                return TagKey.of(RegistryKeys.ITEM, Identifier.of("the_tower", name));
            }
        }

        public static void load(){}
    }

