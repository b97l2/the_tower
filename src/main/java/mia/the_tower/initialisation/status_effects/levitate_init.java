package mia.the_tower.initialisation.status_effects;

import mia.the_tower.initialisation.block_init;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.server.world.ServerWorld;

public class levitate_init extends StatusEffect {

    protected levitate_init() {
        super(StatusEffectCategory.BENEFICIAL, 0x020070); // Light blue color
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        BlockPos below = entity.getBlockPos().down();

        if (entity.isSneaking()) {
            if (world.getBlockState(below).isOf(block_init.BARRIER)) {
                world.setBlockState(below, Blocks.AIR.getDefaultState());
            }
            return true;
        }

        if (world.getBlockState(below).isAir()) {
            world.setBlockState(below, block_init.BARRIER.getDefaultState());
        }

        return true; // Must return true to keep applying every tick
    }


    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // Apply every tick
    }

    // Internal registration of the status effect
    public static final RegistryEntry<StatusEffect> LEVITATE =
            Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of("the_tower", "levitate"), new levitate_init());

    public static void load(){}
}
