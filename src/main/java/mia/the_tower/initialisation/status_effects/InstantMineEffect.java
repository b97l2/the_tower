package mia.the_tower.initialisation.status_effects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;


public class InstantMineEffect extends StatusEffect {
    public static final RegistryEntry<StatusEffect> WHAMMY_HAND = register(
            "whammy_hand",
            new InstantMineEffect(StatusEffectCategory.BENEFICIAL, 0x542E3B)
                    .addAttributeModifier(
                            EntityAttributes.MINING_EFFICIENCY,
                            Identifier.of("the_tower", "effect.whammy_hand.mining_efficiency"),
                            1000,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.BLOCK_BREAK_SPEED,
                            Identifier.of("the_tower", "effect.whammy_hand.block_break_speed"),
                            1000,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.SUBMERGED_MINING_SPEED,
                            Identifier.of("the_tower", "effect.whammy_hand.submerged_mining_speed"),
                            18,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.BLOCK_INTERACTION_RANGE,
                            Identifier.of("the_tower", "effect.whammy_hand.block_interaction_range"),
                            50,
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.ATTACK_DAMAGE,
                            Identifier.of("the_tower", "effect.sanguine.attack_damage"),
                            -1500, //sets to 0
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
                    .addAttributeModifier(
                            EntityAttributes.ENTITY_INTERACTION_RANGE,
                            Identifier.of("the_tower", "effect.sanguine.entity_interaction_range"),
                            -60,//sets to 0
                            EntityAttributeModifier.Operation.ADD_VALUE
                    )
    );

    public InstantMineEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    private static RegistryEntry<StatusEffect> register(String path, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of("the_tower", path), effect);
    }

    public static void load(){}
}