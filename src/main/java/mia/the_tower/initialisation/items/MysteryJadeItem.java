package mia.the_tower.initialisation.items;

import mia.the_tower.initialisation.block_init;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class MysteryJadeItem extends Item {
    public MysteryJadeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onCraftByPlayer(ItemStack stack, World world, PlayerEntity player) {
        super.onCraftByPlayer(stack, world, player);
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.getServer().send(
                    serverWorld.getServer().createTask(() -> resolvePlayerStacks(player)));
        }
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player && stack.isOf(this)) {
            player.getInventory().setStack(slot, createResult(player.getRandom()));
        }
    }

    private void resolvePlayerStacks(PlayerEntity player) {
        ItemStack cursorStack = player.currentScreenHandler.getCursorStack();
        if (cursorStack.isOf(this)) {
            player.currentScreenHandler.setCursorStack(createResult(player.getRandom()));
        }

        for (int slot = 0; slot < player.getInventory().size(); slot++) {
            if (player.getInventory().getStack(slot).isOf(this)) {
                player.getInventory().setStack(slot, createResult(player.getRandom()));
            }
        }
    }

    private static ItemStack createResult(Random random) {
        int roll = random.nextInt(1000);

        if (roll == 0) {
            return new ItemStack(block_init.PHOENIX_BLOOD_JADE_BLOCK);
        }
        if (roll == 1) {
            return new ItemStack(block_init.HEAVENLY_DEW_JADE_BLOCK);
        }
        if (roll < 7) {
            return new ItemStack(block_init.IMPERIAL_JADE_BLOCK);
        }
        if (roll < 12) {
            return new ItemStack(block_init.LOTUS_JADE_BLOCK);
        }
        if (roll < 32) {
            return new ItemStack(block_init.BLOOD_JADE_BLOCK);
        }
        if (roll < 52) {
            return new ItemStack(block_init.LAVENDER_JADE_BLOCK);
        }
        if (roll < 72) {
            return new ItemStack(block_init.GOLDEN_THREAD_JADE_BLOCK);
        }
        if (roll < 122) {
            return new ItemStack(block_init.MUTTON_FAT_JADE_BLOCK);
        }
        return new ItemStack(block_init.JADEITE_BLOCK);
    }
}
