package mia.the_tower.initialisation.entity;

import mia.the_tower.initialisation.entity.custom.VoidMothEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {
    //to make an entity, you give it two lines here
    //you make its own entity class of course under /custom
    //and then you need to make four classes, Animations, Model, Renderer, and RenderState, which handle the appearance and rendering in the client portion
    //then you put a line in The_Tower
    //lastly, you put in a needed line in The_TowerClient and put in the texture

    //for the egg you of course need to make a line in item_init and put it in creative inventory

    //one here
    private static final RegistryKey<EntityType<?>> VOID_MOTH_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("the_tower", "void_moth"));

    //one here
    public static final EntityType<VoidMothEntity> VOID_MOTH = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("the_tower", "void_moth"),
            EntityType.Builder.create(VoidMothEntity::new, SpawnGroup.CREATURE)
                    .dimensions(1f, 0.4f).build(VOID_MOTH_KEY));

    public static void load() {}

    }
