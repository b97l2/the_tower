package mia.the_tower.initialisation.misc;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;

import java.util.List;

public final class ModDeaths {
    public static void init() {
        ServerLivingEntityEvents.ALLOW_DEATH.register(ModDeaths::onAllowDeath);
    }

    // Return true to let death proceed (we do); false would cancel death entirely.
    private static boolean onAllowDeath(LivingEntity entity, DamageSource src, float amount) {
        if (!(entity instanceof ServerPlayerEntity player)) return true;

        ServerWorld world = player.getServerWorld();
        // Respect keepInventory and creative/spectator
        if (world.getGameRules().getBoolean(GameRules.KEEP_INVENTORY)) return true;
        if (player.isCreative() || player.isSpectator()) return true;

        // 1) Snapshot everything we plan to store
        List<ItemStack> snapshot = GraveUtils.snapshotPlayerInventory(player);
        if (snapshot.isEmpty()) return true;

        // 2) Clear the player so vanilla has nothing to drop
        GraveUtils.clearPlayerInventory(player);

        // 3) Place + fill the grave
        BlockPos base = BlockPos.ofFloored(player.getPos());
        BlockPos gravePos = GraveUtils.findSafeGravePos(world, base);
        boolean placed = GraveUtils.placeAndFillGrave(world, gravePos, player, snapshot);

        // If placement somehow failed, put stacks back so vanilla can drop them as usual.
        if (!placed) GraveUtils.restoreToPlayer(player, snapshot);

        return true;
    }
}
