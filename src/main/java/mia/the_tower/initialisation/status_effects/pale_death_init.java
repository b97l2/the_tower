package mia.the_tower.initialisation.status_effects;

import mia.the_tower.The_Tower;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.GameMode;

public class pale_death_init extends StatusEffect {

    public pale_death_init() {
        super(StatusEffectCategory.BENEFICIAL, 0x888888); // gray color
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (!(entity instanceof ServerPlayerEntity player)) return true;

        // Save original game mode if not saved
        if (!The_Tower.isSpectating(player)) {
            The_Tower.setSpectator(player, player.interactionManager.getGameMode());
            player.changeGameMode(GameMode.SPECTATOR);
        }
        // Set health and hunger
        player.setHealth(2.0f); // 1 heart
        player.getHungerManager().setFoodLevel(6); // 1.5 hunger shanks

        return true; // keep updating each tick
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true; // Called every tick
    }

    // Internal registration of the status effect
    public static final RegistryEntry<StatusEffect> PALE_DEATH =
            Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of("the_tower", "pale_death"), new pale_death_init());

    public static void load(){}

}