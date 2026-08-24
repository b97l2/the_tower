package mia.the_tower.initialisation.fluid;

import mia.the_tower.The_Tower;
import mia.the_tower.initialisation.item_init;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

public class fluid_init {
    public static final FlowableFluid STILL_BLOOD = Registry.register(Registries.FLUID,
            RegistryKey.of(RegistryKeys.FLUID, The_Tower.id("blood")),
            new BloodFluid.Still());

    public static final FlowableFluid FLOWING_BLOOD = Registry.register(Registries.FLUID,
            RegistryKey.of(RegistryKeys.FLUID, The_Tower.id("flowing_blood")),
            new BloodFluid.Flowing());

    public static final Block BLOOD = Registry.register(Registries.BLOCK,
            RegistryKey.of(RegistryKeys.BLOCK, The_Tower.id("blood")),
            new BloodFluidBlock(STILL_BLOOD, AbstractBlock.Settings.create()
                    .replaceable()
                    .mapColor(MapColor.DARK_RED)
                    .nonOpaque()
                    .strength(100.0F)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .dropsNothing()
                    .liquid()
                    .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, The_Tower.id("blood")))));

    public static final FlowableFluid STILL_GUNK = Registry.register(Registries.FLUID,
            RegistryKey.of(RegistryKeys.FLUID, The_Tower.id("gunk")),
            new GunkFluid.Still());

    public static final FlowableFluid FLOWING_GUNK = Registry.register(Registries.FLUID,
            RegistryKey.of(RegistryKeys.FLUID, The_Tower.id("flowing_gunk")),
            new GunkFluid.Flowing());

    public static final Block GUNK = Registry.register(Registries.BLOCK,
            RegistryKey.of(RegistryKeys.BLOCK, The_Tower.id("gunk")),
            new GunkFluidBlock(STILL_GUNK, AbstractBlock.Settings.create()
                    .replaceable()
                    .mapColor(MapColor.BLACK)
                    .nonOpaque()
                    .strength(400.0F)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .dropsNothing()
                    .liquid()
                    .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, The_Tower.id("gunk")))));

    public static final FluidLogging.Slot BLOOD_LOGGING_SLOT = registerLoggedFluid(
            FluidLogging.Slot.BLOOD,
            STILL_BLOOD,
            FLOWING_BLOOD,
            () -> item_init.CHALICE_OF_BLOOD
    );

    public static final FluidLogging.Slot GUNK_LOGGING_SLOT = registerLoggedFluid(
            FluidLogging.Slot.GUNK,
            STILL_GUNK,
            FLOWING_GUNK,
            () -> item_init.CUP_OF_GUNK
    );

    public static void load() {
    }

    private static FluidLogging.Slot registerLoggedFluid(
            FluidLogging.Slot slot,
            FlowableFluid still,
            FlowableFluid flowing,
            java.util.function.Supplier<net.minecraft.item.Item> bucketItem
    ) {
        FluidLogging.register(slot, still, flowing, () -> new net.minecraft.item.ItemStack(bucketItem.get()));
        return slot;
    }
}
