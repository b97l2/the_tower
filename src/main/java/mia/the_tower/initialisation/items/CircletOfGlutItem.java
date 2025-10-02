package mia.the_tower.initialisation.items;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class CircletOfGlutItem extends Item {
    public CircletOfGlutItem(Settings settings) {
        super(settings);
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient || !(entity instanceof PlayerEntity player)) return;

        // Only act if THIS stack is actually equipped on the head
        if (player.getEquippedStack(EquipmentSlot.HEAD) != stack) return;

        // Run ~once per second to reduce churn
        if ((player.age % 20) == 0) {
            HungerManager hm = player.getHungerManager();
            if (hm.getFoodLevel() < 20) hm.setFoodLevel(20);
            if (hm.getSaturationLevel() < 20f) hm.setSaturationLevel(20f);
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack,
                              TooltipContext context,
                              List<Text> tooltip,
                              TooltipType type) {
        tooltip.add(Text.literal("Their end is destruction,").formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.literal("their god is their belly,").formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.literal("and they glory in their shame,").formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.literal("with minds set on earthly things.").formatted(Formatting.GRAY, Formatting.ITALIC));
    }

}
