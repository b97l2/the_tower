package mia.the_tower.initialisation.carver;

import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.ConfiguredCarver;

public final class ModCarvers {

    // Single source of truth for the id
    public static final Identifier PIT_ID = Identifier.of("the_tower", "pit_carver");

    // Register the CARVER TYPE with its CODEC
    public static final Carver<PitCarverConfig> PIT_CARVER =
            Registry.register(Registries.CARVER, PIT_ID, new PitCarver());

    // Key that points to your JSON-configured carver: data/the_tower/worldgen/configured_carver/pit_carver.json
    public static final RegistryKey<ConfiguredCarver<?>> PIT_CARVER_KEY =
            RegistryKey.of(RegistryKeys.CONFIGURED_CARVER, PIT_ID);

    public static void load(){}
}

