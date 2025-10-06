package mia.the_tower.initialisation.screen;

import mia.the_tower.The_Tower;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;

public class screen_init {

    public static ScreenHandlerType<CofferScreenHandler> COFFER;

    private screen_init() {}

    public static void load() {
        // Factory is the CLIENT constructor: (syncId, playerInv) -> new CofferScreenHandler(syncId, playerInv)
        COFFER = Registry.register(
                Registries.SCREEN_HANDLER,
                The_Tower.id("coffer"),
                new ScreenHandlerType<>(CofferScreenHandler::new, net.minecraft.resource.featuretoggle.FeatureFlags.VANILLA_FEATURES)
        );
    }

}
