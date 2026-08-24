package mia.the_tower.initialisation.carver;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverDebugConfig;
import net.minecraft.world.gen.carver.ConfiguredCarver;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;

public final class ModCarvers {

    // Single source of truth for the id
    public static final Identifier PIT_ID = Identifier.of("the_tower", "pit_carver");

    // Register the CARVER TYPE with its CODEC
    public static final Carver<PitCarverConfig> PIT_CARVER =
            Registry.register(Registries.CARVER, PIT_ID, new PitCarver());

    // Key that points to the JSON-configured carver.
    public static final RegistryKey<ConfiguredCarver<?>> PIT_CARVER_KEY =
            RegistryKey.of(RegistryKeys.CONFIGURED_CARVER, Identifier.of("the_tower", "pit"));

    public static void bootstrap(Registerable<ConfiguredCarver<?>> context) {
        RegistryEntryLookup<Block> blockLookup = context.getRegistryLookup(RegistryKeys.BLOCK);

        context.register(PIT_CARVER_KEY, PIT_CARVER.configure(new PitCarverConfig(
                0.035f,
                UniformHeightProvider.create(YOffset.belowTop(0), YOffset.belowTop(0)),
                ConstantFloatProvider.create(1.0f),
                YOffset.fixed(-56),
                CarverDebugConfig.create(false, Blocks.CRIMSON_BUTTON.getDefaultState()),
                blockLookup.getOrThrow(BlockTags.OVERWORLD_CARVER_REPLACEABLES),
                UniformIntProvider.create(20, 40),
                UniformIntProvider.create(300, 500),
                UniformIntProvider.create(12, 28),
                ConstantIntProvider.create(0),
                UniformFloatProvider.create(0.0f, 0.25f)
        )));
    }

    public static void load(){}
}
