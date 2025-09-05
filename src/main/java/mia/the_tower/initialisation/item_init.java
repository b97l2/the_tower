package mia.the_tower.initialisation;

import mia.the_tower.The_Tower;
import mia.the_tower.initialisation.block.stake_init;
import mia.the_tower.initialisation.items.StaveItem;
import mia.the_tower.initialisation.status_effects.InstantMineEffect;
import mia.the_tower.initialisation.status_effects.SanguineStatusEffect;
import mia.the_tower.initialisation.status_effects.levitate_init;
import mia.the_tower.initialisation.status_effects.pale_death_init;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import static mia.the_tower.initialisation.block_init.*;

public class item_init { //this is the class that initialises items in the mod

    public static Item GLOOP;
    public static Item GLUH;
    public static Item YUMP; //*add textures
    public static Item KLAEN;
    public static Item CHALICE_OF_BLOOD;
    public static Item CUP_OF_GUNK;
    public static Item STAVE;
    public static Item PILLAR_OF_SALT;
    public static Item PILE_OF_SALT;
    public static Item GABRIELS_TRUMPET;
    public static Item DRAGONS_EYE;
    public static Item PEARL_OF_BLOOD;
    public static Item HORNWORM;
    public static Item SANGUINE_FLASK;
    public static Item BUCKET_OF_BILE;
    public static Item FINGER;
    public static Item BLOODY_NAIL;
    public static Item TOOTH;
    public static Item SANGUINE_SYRINGE;
    public static Item CONTRACT;


    public static void load() {
        GLOOP = register("gloop", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("gloop")))));

        GLUH = register("gluh", new BlockItem(
                crop_init.GLUH_VINE,
                new Item.Settings().food(
                        new FoodComponent.Builder()
                                .alwaysEdible()
                                .build(),
                        new ConsumableComponents().food()
                                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(levitate_init.LEVITATE, 60 * 20, 1), 1.0f))
                                .build())
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("gluh")))));

        YUMP = register("yump", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("yump")))));

        KLAEN = register("klaen", new BlockItem(
                stake_init.STAKE, //change
                new Item.Settings().food(
                                new FoodComponent.Builder()
                                        .alwaysEdible()
                                        .build(),
                                new ConsumableComponents().food()
                                        .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(pale_death_init.PALE_DEATH, 13 * 20, 1), 1.0f))
                                        .build())
                        .maxCount(16)
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("klaen")))));

        CHALICE_OF_BLOOD = Registry.register(Registries.ITEM,
                RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("chalice_of_blood")),
                new BucketItem(STILL_BLOOD, new Item.Settings()
                        .recipeRemainder(net.minecraft.item.Items.BUCKET)
                        .maxCount(1)
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("chalice_of_blood")))));

        CUP_OF_GUNK = Registry.register(Registries.ITEM,
                RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("cup_of_gunk")),
                new BucketItem(STILL_GUNK, new Item.Settings()
                        .recipeRemainder(net.minecraft.item.Items.BUCKET)
                        .maxCount(1)
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("cup_of_gunk")))));

        BUCKET_OF_BILE = Registry.register(Registries.ITEM,
                RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("bucket_of_bile")),
                new BucketItem(STILL_GUNK, new Item.Settings()
                        .recipeRemainder(net.minecraft.item.Items.BUCKET)
                        .maxCount(1)
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("bucket_of_bile")))));

        STAVE = register("stave", new StaveItem(new Item.Settings()
                .maxCount(1)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("stave")))));

        PILLAR_OF_SALT = register("pillar_of_salt", new Item(new Item.Settings()
                .maxCount(1)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("pillar_of_salt"))))); //add logic in custom class

        PILE_OF_SALT = register("pile_of_salt", new Item(new Item.Settings().food(
                        new FoodComponent.Builder()
                                .alwaysEdible()
                                .build(),
                        new ConsumableComponents().food()
                                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(InstantMineEffect.WHAMMY_HAND, 49 * 20, 1), 1.0f))
                                .build())
                .maxCount(9)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("pile_of_salt"))))); //crafting exclusive, change later on

        GABRIELS_TRUMPET = register("gabriels_trumpet", new Item(new Item.Settings()
                .maxCount(1)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("gabriels_trumpet"))))); //add logic in custom class

        DRAGONS_EYE = register("dragons_eye", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("dragons_eye"))))); //add logic in custom class

        PEARL_OF_BLOOD = register("pearl_of_blood", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("pearl_of_blood")))));

        HORNWORM = register("hornworm", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("hornworm")))));

        SANGUINE_FLASK = register("sanguine_flask", new Item(new Item.Settings().food(
                        new FoodComponent.Builder()
                                .alwaysEdible()
                                .build(),
                        new ConsumableComponents().drink()
                                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(SanguineStatusEffect.SANGUINE, 120 * 20, 0), 1.0f))
                                .build())
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("sanguine_flask"))))); //only gotten through distilling process

        FINGER = register("finger", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("finger")))));

        BLOODY_NAIL = register("bloody_nail", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("bloody_nail")))));

        TOOTH = register("tooth", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("tooth")))));

        SANGUINE_SYRINGE = register("sanguine_syringe", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("sanguine_syringe")))));

        CONTRACT = register("contract", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("contract")))));
    }

    public static Item register(String name, Item item) {
        Identifier id = The_Tower.id(name);
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
        return Registry.register(Registries.ITEM, itemKey, item);
    }


    //public static void load() {} //This method allows us to load the items here into the game

}

