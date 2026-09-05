package mia.the_tower.initialisation.weather;

import mia.the_tower.initialisation.dimentions.TheFrage;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;

public final class FrageWeatherState {
    private static final FrageWeatherState INSTANCE = new FrageWeatherState();
    private static final int SYNC_INTERVAL_TICKS = 20;
    private static final float OVERCAST_TARGET_STRENGTH = 1.0F;
    private static final float TRANSITION_STEP = 0.01F;

    private FrageWeatherPattern pattern = FrageWeatherPattern.OVERCAST;
    private float overcastStrength = OVERCAST_TARGET_STRENGTH;
    private int syncDelay;

    private FrageWeatherState() {
    }

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(FrageWeatherState::tickWorld);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> syncPlayer(handler.getPlayer()));
    }

    private static void tickWorld(ServerWorld world) {
        if (!world.getRegistryKey().equals(TheFrage.THE_FRAGE_LEVEL_KEY)) {
            return;
        }

        INSTANCE.tick();
        INSTANCE.syncDelay--;
        if (INSTANCE.syncDelay <= 0) {
            INSTANCE.syncDelay = SYNC_INTERVAL_TICKS;
            for (ServerPlayerEntity player : world.getPlayers()) {
                syncPlayer(player);
            }
        }
    }

    private void tick() {
        float targetStrength = this.pattern == FrageWeatherPattern.OVERCAST ? OVERCAST_TARGET_STRENGTH : 0.0F;
        this.overcastStrength = MathHelper.clamp(
                this.overcastStrength + MathHelper.clamp(targetStrength - this.overcastStrength, -TRANSITION_STEP, TRANSITION_STEP),
                0.0F,
                1.0F
        );
    }

    private static void syncPlayer(ServerPlayerEntity player) {
        if (!player.getWorld().getRegistryKey().equals(TheFrage.THE_FRAGE_LEVEL_KEY)) {
            return;
        }

        if (ServerPlayNetworking.canSend(player, FrageWeatherSyncPayload.ID)) {
            ServerPlayNetworking.send(player, INSTANCE.createPayload());
        }
    }

    private FrageWeatherSyncPayload createPayload() {
        return new FrageWeatherSyncPayload(this.pattern, this.overcastStrength);
    }
}
