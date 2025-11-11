package mia.the_tower.initialisation.entity;

import mia.the_tower.initialisation.entity.custom.LampFlyEntity;
import mia.the_tower.initialisation.entity.custom.PlateEntity;
import mia.the_tower.initialisation.entity.custom.VoidMothEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;

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

    private static final RegistryKey<EntityType<?>> LAMP_FLY_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("the_tower", "lamp_fly"));

    private static final RegistryKey<EntityType<?>> PLATE_KEY =
            RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("the_tower", "plate"));



    //one here
    public static final EntityType<VoidMothEntity> VOID_MOTH = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("the_tower", "void_moth"),
            EntityType.Builder.create(VoidMothEntity::new, SpawnGroup.CREATURE)
                    .dimensions(1f, 0.4f).build(VOID_MOTH_KEY));

    public static final EntityType<LampFlyEntity> LAMP_FLY = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("the_tower", "lamp_fly"),
            EntityType.Builder.create(LampFlyEntity::new, SpawnGroup.AMBIENT)
                    .dimensions(0.2f, 0.2f).build(LAMP_FLY_KEY));

    public static final EntityType<PlateEntity> PLATE = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of("the_tower", "plate"),
            EntityType.Builder.<PlateEntity>create(PlateEntity::new, SpawnGroup.MISC)
                    .dropsNothing()
                    .dimensions(0.5F, 0.5F)
                    .eyeHeight(0.0F)
                    .maxTrackingRange(10)
                    .trackingTickInterval(Integer.MAX_VALUE)
                    .dimensions(1f, 0.4f)
                    .build(PLATE_KEY));

    public static void load() {
        //spawn restrictions, not strictly needed
        FabricDefaultAttributeRegistry.register(LAMP_FLY, LampFlyEntity.createAttributes());
        // **spawn restriction**: which placement pass + heightmap + predicate
        SpawnRestriction.register(
                LAMP_FLY,
                SpawnLocationTypes.UNRESTRICTED,           // flying/air checks yourself
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,            // where spawner queries height
                LampFlyEntity::canSpawn                               // your predicate
        );

    }

    }
