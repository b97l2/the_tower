package mia.the_tower.initialisation.items.util;

import mia.the_tower.initialisation.block_init;
import mia.the_tower.initialisation.item_init;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.util.DyeColor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.IdentityHashMap;
import java.util.Map;

public class CofferToKey {
    private CofferToKey() {}

    // Use IdentityHashMap since Blocks are singletons; HashMap also works.
    private static Map<Block, Item> KEY_BY_COFFER = Collections.emptyMap();

    /** Call this AFTER block_init.load() and item_init.load() in your mod initializer. */
    public static void load() {
        IdentityHashMap<Block, Item> m = new IdentityHashMap<>();
        m.put(block_init.WHITE_COFFER,      item_init.WHITE_KEY);
        m.put(block_init.ORANGE_COFFER,     item_init.ORANGE_KEY);
        m.put(block_init.MAGENTA_COFFER,    item_init.MAGENTA_KEY);
        m.put(block_init.LIGHT_BLUE_COFFER, item_init.LIGHT_BLUE_KEY);
        m.put(block_init.YELLOW_COFFER,     item_init.YELLOW_KEY);
        m.put(block_init.LIME_COFFER,       item_init.LIME_KEY);
        m.put(block_init.PINK_COFFER,       item_init.PINK_KEY);
        m.put(block_init.GREY_COFFER,       item_init.GREY_KEY);
        m.put(block_init.LIGHT_GREY_COFFER, item_init.LIGHT_GREY_KEY);
        m.put(block_init.CYAN_COFFER,       item_init.CYAN_KEY);
        m.put(block_init.PURPLE_COFFER,     item_init.PURPLE_KEY);
        m.put(block_init.BLUE_COFFER,       item_init.BLUE_KEY);
        m.put(block_init.BROWN_COFFER,      item_init.BROWN_KEY);
        m.put(block_init.GREEN_COFFER,      item_init.GREEN_KEY);
        m.put(block_init.RED_COFFER,        item_init.RED_KEY);
        m.put(block_init.BLACK_COFFER,      item_init.BLACK_KEY);

        KEY_BY_COFFER = Collections.unmodifiableMap(m);
    }

    /** Replace your key-for-coffer selection with this single map lookup. */
    public static Item keyForCoffer(Block cofferBlock) {
        return KEY_BY_COFFER.get(cofferBlock);
    }
}