package mia.the_tower.initialisation.status_effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

import java.util.UUID;
import java.util.WeakHashMap;

public class DecayStatusEffect extends StatusEffect {
    public DecayStatusEffect() {
        super(StatusEffectCategory.HARMFUL, 0x000000); // black
    }
    private static final WeakHashMap<UUID, Vec3d> lastPositions = new WeakHashMap<>();

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        // Run logic every 20 ticks (1 second)
        return duration % 20 == 0;
    }

    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        UUID uuid = entity.getUuid();
        Vec3d currentPos = entity.getPos();
        Vec3d lastPos = lastPositions.get(uuid);

        // Detect if the entity has moved since last tick
        boolean hasMoved = lastPos == null || !currentPos.equals(lastPos);

        if (hasMoved) {
            float damage = (amplifier + 1) * 2.0f; // 2 hearts per decay level
            entity.damage(world, entity.getDamageSources().magic(), damage);
            lastPositions.put(uuid, currentPos);
            return true;
        }

        return true;
    }

    public static final RegistryEntry<StatusEffect> DECAY =
            Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of("the_tower", "decay"), new DecayStatusEffect());

    public static void load(){}
}
