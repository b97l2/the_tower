package mia.the_tower.initialisation.carver;

import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.carver.Carver;

public final class ModCarvers {

    public static final Identifier PIT_ID = Identifier.of("the_tower", "pit_carver");
    public static final Carver<PitCarverConfig> PIT_CARVER =
            Registry.register(Registries.CARVER, PIT_ID, new PitCarver());




    public static void load(){}
}

