package mia.the_tower.initialisation.items;

import mia.the_tower.initialisation.item_init;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.GlobalPos;

import java.util.List;

public class DeathMarkerItem extends Item {

    public DeathMarkerItem(Settings settings) {
        super(settings.maxCount(1)); // unstackable
    }

    /** Factory: build a marker with lore lines from a GlobalPos. */
    public static ItemStack of(net.minecraft.util.math.GlobalPos pos) {
        ItemStack stack = new ItemStack(item_init.FRAYED_NOTE); // change to your registry ref
        setLore(stack, pos);
        return stack;
    }

    /** Overwrite the lore to contain coords (line 0) and dimension id (line 1). */
    public static void setLore(ItemStack stack, GlobalPos gp) {
        BlockPos p = gp.pos();
        String dim = gp.dimension().getValue().toString(); // e.g., "minecraft:overworld"

        // Line 0: coordinates; Line 1: dimension id (styled like your example)
        Text line0 = Text.literal(p.getX() + ", " + p.getY() + ", " + p.getZ()).formatted(Formatting.DARK_AQUA, Formatting.ITALIC);
        Text line1 = Text.literal(dim).formatted(Formatting.DARK_AQUA, Formatting.ITALIC);

        stack.set(DataComponentTypes.LORE, new LoreComponent(List.of(line0, line1)));
    }
}