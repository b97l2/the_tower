package mia.the_tower.initialisation.status_effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;


public class SanguineStatusEffect extends StatusEffect {
    public static final RegistryEntry<StatusEffect> SANGUINE = register(
            "sanguine",
            new SanguineStatusEffect(StatusEffectCategory.BENEFICIAL, 0x420000)
                    .addAttributeModifier(
                            EntityAttributes.ATTACK_DAMAGE,
                            Identifier.of("the_tower", "effect.sanguine.attack_damage"),
                            4, //2 times
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.ATTACK_SPEED,
                            Identifier.of("the_tower", "effect.sanguine.attack_speed"),
                            9, //2.5 times
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.ENTITY_INTERACTION_RANGE,
                            Identifier.of("the_tower", "effect.sanguine.entity_interaction_range"),
                            3,//doubles
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.JUMP_STRENGTH,
                            Identifier.of("the_tower", "effect.sanguine.jump_strength"),
                            0.42, //doubles
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.LUCK,
                            Identifier.of("the_tower", "effect.sanguine.luck"),
                            3, //set
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.MAX_HEALTH,
                            Identifier.of("the_tower", "effect.sanguine.max_health"),
                            10, //1.5 times
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.MOVEMENT_EFFICIENCY,
                            Identifier.of("the_tower", "effect.sanguine.movement_efficiency"),
                            0.40, //40 percent increase
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.MOVEMENT_SPEED,
                            Identifier.of("the_tower", "effect.sanguine.movement_speed"),
                            0.12, //adds 0.12 to 0.7
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.WATER_MOVEMENT_EFFICIENCY,
                            Identifier.of("the_tower", "effect.sanguine.water_movement_efficiency"),
                            0.5, //adds 10 percent
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.OXYGEN_BONUS,
                            Identifier.of("the_tower", "effect.sanguine.oxygen_bonus"),
                            10, //set
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.SAFE_FALL_DISTANCE,
                            Identifier.of("the_tower", "effect.sanguine.safe_fall_distance"),
                            17, //sets to 10
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.SNEAKING_SPEED,
                            Identifier.of("the_tower", "effect.sanguine.sneaking_speed"),
                            0.5, //2.5 times
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.FLYING_SPEED,
                            Identifier.of("the_tower", "effect.sanguine.flying_speed"),
                    0.45, //sets to 0.55 from 0.4
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.FALL_DAMAGE_MULTIPLIER,
                            Identifier.of("the_tower", "effect.sanguine.fall_damage_multiplier"),
                            -0.8, //one third
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.GRAVITY,
                            Identifier.of("the_tower", "effect.sanguine.gravity"),
                            -0.03, //sets to 0.5 from 0.8
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.STEP_HEIGHT,
                            Identifier.of("the_tower", "effect.sanguine.step_height"),
                    2, //sets to 2.6 from 0.6
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        //updates every 5 seconds
        return duration % 20*5 == 0;
    }


    @Override
    public boolean applyUpdateEffect(ServerWorld world, LivingEntity entity, int amplifier) {
        if (!(entity instanceof PlayerEntity player)) return true;

        // Don’t do AI work for client, spectators, or creative players
        if (world.isClient() || player.isSpectator() || player.isCreative()) return true;

        final double radius = 64.0;
        final Box area = player.getBoundingBox().expand(radius);

        ServerWorld sw = (ServerWorld) world;
        // Pull all hostile mobs in a 64-block cube around the player
        var mobs = sw.getEntitiesByClass(
                HostileEntity.class,
                area,
                mob -> mob.isAlive() && mob.isAttackable()
        );

        for (HostileEntity mob : mobs) {
            // Point them at the player
            if (mob.getTarget() != player) {
                mob.setTarget(player);
            }
            mob.setAttacking(true);

            // Keep "anger"-based mobs (piglins, endermen, etc.) actually angry
            if (mob instanceof Angerable angry) {
                angry.setAngryAt(player.getUuid());
                //refreshes to 60 seconds of anger time
                angry.setAngerTime(20*60);
            }

            // Nudge their pathfinder so they start moving right away
            mob.getNavigation().startMovingTo(player, 1.2D);
        }
        return true;
    }

    public SanguineStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    private static RegistryEntry<StatusEffect> register(String path, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of("the_tower", path), effect);
    }

    public static void load(){}
}