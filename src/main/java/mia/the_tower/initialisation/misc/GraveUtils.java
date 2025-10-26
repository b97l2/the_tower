package mia.the_tower.initialisation.misc;

import mia.the_tower.initialisation.block_init;
import mia.the_tower.initialisation.blockentity.StorageTemplateBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public final class GraveUtils {
    private GraveUtils() {}

    /** Copy main, hotbar, armor, offhand (everything in PlayerInventory). */
    public static List<ItemStack> snapshotPlayerInventory(ServerPlayerEntity player) {
        var inv = player.getInventory();
        List<ItemStack> out = new ArrayList<>(inv.main.size() + inv.armor.size() + inv.offHand.size());
        // main (includes hotbar)
        for (ItemStack s : inv.main) out.add(s.copy());
        // armor
        for (ItemStack s : inv.armor) out.add(s.copy());
        // offhand
        for (ItemStack s : inv.offHand) out.add(s.copy());
        return out;
    }

    /** Wipe all player stacks so vanilla has nothing to drop. */
    public static void clearPlayerInventory(ServerPlayerEntity player) {
        var inv = player.getInventory();
        for (int i = 0; i < inv.main.size(); i++) inv.main.set(i, ItemStack.EMPTY);
        for (int i = 0; i < inv.armor.size(); i++) inv.armor.set(i, ItemStack.EMPTY);
        for (int i = 0; i < inv.offHand.size(); i++) inv.offHand.set(i, ItemStack.EMPTY);
        player.currentScreenHandler.sendContentUpdates(); // keep client in sync
    }

    /** Restore back to player (fallback path). */
    public static void restoreToPlayer(ServerPlayerEntity player, List<ItemStack> stacks) {
        var inv = player.getInventory();
        int idx = 0;
        for (; idx < inv.main.size() && idx < stacks.size(); idx++) inv.main.set(idx, stacks.get(idx));
        // armor
        for (int a = 0; a < inv.armor.size() && idx < stacks.size(); a++, idx++) inv.armor.set(a, stacks.get(idx));
        // offhand
        for (int o = 0; o < inv.offHand.size() && idx < stacks.size(); o++, idx++) inv.offHand.set(o, stacks.get(idx));
        player.currentScreenHandler.sendContentUpdates();
    }

    /** Try to place the grave and insert stacks; drop overflow. */
    public static boolean placeAndFillGrave(ServerWorld world, BlockPos pos, ServerPlayerEntity owner, List<ItemStack> stacks) {
        BlockState state = block_init.GRAVESTONE.getDefaultState();
        if (!canPlaceAt(world, pos)) pos = findSafeGravePos(world, pos);

        if (!world.setBlockState(pos, state)) return false;

        BlockEntity be = world.getBlockEntity(pos);
        if (!(be instanceof StorageTemplateBlockEntity grave)) return false;

        // Insert all stacks, dropping leftovers at the grave if it overflows.
        for (ItemStack s : stacks) {
            if (s.isEmpty()) continue;
            s = s.copy(); // do not mutate snapshot list
            s = insertAsMuchAsPossible(grave, s);
            if (!s.isEmpty()) ItemScatterer.spawn(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, s);
        }
        grave.markDirty();
        world.updateListeners(pos, state, state, 3);
        return true;
    }

    /** Simple “merge then fill empty” inserter. Adjust to your BE’s API. */
    private static ItemStack insertAsMuchAsPossible(StorageTemplateBlockEntity inv, ItemStack stack) {
        // 1) merge into existing stacks
        for (int i = 0; i < inv.size() && !stack.isEmpty(); i++) {
            var slot = inv.getStack(i);
            if (!slot.isEmpty() && ItemStack.areItemsEqual(slot, stack) && slot.getCount() < slot.getMaxCount()) {
                int toMove = Math.min(slot.getMaxCount() - slot.getCount(), stack.getCount());
                if (toMove > 0) {
                    slot.increment(toMove);
                    stack.decrement(toMove);
                    inv.setStack(i, slot);
                }
            }
        }
        // 2) fill empty slots
        for (int i = 0; i < inv.size() && !stack.isEmpty(); i++) {
            if (inv.getStack(i).isEmpty()) {
                inv.setStack(i, stack.copy());
                stack.setCount(0);
            }
        }
        return stack; // leftover (if any)
    }

    private static boolean isReplaceable(ServerWorld world, BlockPos pos) {
        return world.getBlockState(pos).isReplaceable();
    }

     /* New policy:
            * - If below world bottom (void), clamp to bottomY + 1 (can be in air).
            * - Allow air or fluids (any replaceable) without adjustment.
            * - If inside a non-replaceable block, move up until we find a replaceable one.
     * - Do not require solid ground underneath.
            */
    public static BlockPos findSafeGravePos(ServerWorld world, BlockPos deathPos) {
        final int minY = world.getBottomY();
        final int maxY = world.getTopYInclusive() - 1; // highest buildable Y in most mappings

        // Start from the death position, clamped to the world height range.
        BlockPos pos = deathPos;
        if (pos.getY() < minY) pos = new BlockPos(pos.getX(), minY + 1, pos.getZ());
        if (pos.getY() > maxY) pos = new BlockPos(pos.getX(), maxY, pos.getZ());

        // If not replaceable, nudge upward until we find a replaceable block or hit the ceiling.
        int guard = (maxY - pos.getY()) + 2; // simple safety cap
        while (guard-- > 0 && !isReplaceable(world, pos) && pos.getY() < maxY) {
            pos = pos.up();
        }
        return pos;
    }

    /** With the new policy, "can we place here" simply means "is this block replaceable". */
    private static boolean canPlaceAt(ServerWorld world, BlockPos pos) {
        return isReplaceable(world, pos);
    }

    // In your placeAndFillGrave(...), keep calling findSafeGravePos(...) before setBlockState(...)
    // and keep the overflow/drop + restore fallback exactly as you had it.
}
