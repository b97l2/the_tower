package mia.the_tower.initialisation.weather;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class FrageWeatherNetworking {
    private static boolean registered;

    private FrageWeatherNetworking() {
    }

    public static void registerPayloads() {
        if (registered) {
            return;
        }

        PayloadTypeRegistry.playS2C().register(FrageWeatherSyncPayload.ID, FrageWeatherSyncPayload.CODEC);
        registered = true;
    }
}
