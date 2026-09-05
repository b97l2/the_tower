package mia.the_tower.initialisation.weather;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public final class FrageWeatherClient {
    private FrageWeatherClient() {
    }

    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(FrageWeatherSyncPayload.ID,
                (payload, context) -> FrageWeatherClientState.apply(payload));
        ClientTickEvents.END_CLIENT_TICK.register(client -> FrageWeatherClientState.tick());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> FrageWeatherClientState.reset());
    }
}
