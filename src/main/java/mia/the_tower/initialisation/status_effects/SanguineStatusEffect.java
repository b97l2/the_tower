package mia.the_tower.initialisation.status_effects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;


public class SanguineStatusEffect extends StatusEffect {
    public static final RegistryEntry<StatusEffect> SANGUINE = register(
            "sanguine",
            new InstantMineEffect(StatusEffectCategory.BENEFICIAL, 0x420000)
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

    public SanguineStatusEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    private static RegistryEntry<StatusEffect> register(String path, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of("the_tower", path), effect);
    }

    public static void load(){}
}