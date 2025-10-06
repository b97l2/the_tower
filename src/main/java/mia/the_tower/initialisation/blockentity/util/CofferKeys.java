package mia.the_tower.initialisation.blockentity.util;

import mia.the_tower.initialisation.util.CustomTags;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public final class CofferKeys {
    public static @Nullable Identifier keyOf(ItemStack stack) {
        if (stack.isEmpty()) return null;
//restricting it to items with tag coffer_keys
        if (!stack.isIn(CustomTags.Items.COFFER_KEYS)) return null;
        return Registries.ITEM.getId(stack.getItem());
    }
}
