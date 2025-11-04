package mia.the_tower.initialisation;

import mia.the_tower.The_Tower;
import mia.the_tower.initialisation.block.stake_init;
import mia.the_tower.initialisation.entity.ModEntities;
import mia.the_tower.initialisation.items.*;
import mia.the_tower.initialisation.particle.CustomParticles;
import mia.the_tower.initialisation.status_effects.InstantMineEffect;
import mia.the_tower.initialisation.status_effects.SanguineStatusEffect;
import mia.the_tower.initialisation.status_effects.levitate_init;
import mia.the_tower.initialisation.status_effects.pale_death_init;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.*;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.*;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

import static mia.the_tower.initialisation.block_init.*;

public class item_init { //this is the class that initialises items in the mod

    public static Item GLOOP;
    public static Item GLUH;
    public static Item YUMP; //*add textures
    public static Item KLAEN;
    public static Item BLAJ;
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
    public static Item PALE_AXE;
    public static Item MY_RAGE;
    public static Item MY_FUEL;
    public static Item SHEPHERDS_STAFF;
    public static Item VOID_MOTH_SPAWN_EGG;
    public static Item GINKGO_LEAF;
    public static Item CIRCLET_OF_GLUT;
    public static Item PLATE;
    public static Item WHITE_KEY;
    public static Item ORANGE_KEY;
    public static Item MAGENTA_KEY;
    public static Item LIGHT_BLUE_KEY;
    public static Item YELLOW_KEY;
    public static Item LIME_KEY;
    public static Item PINK_KEY;
    public static Item GREY_KEY;
    public static Item LIGHT_GREY_KEY;
    public static Item CYAN_KEY;
    public static Item PURPLE_KEY;
    public static Item BLUE_KEY;
    public static Item BROWN_KEY;
    public static Item GREEN_KEY;
    public static Item RED_KEY;
    public static Item BLACK_KEY;
    public static Item KEY;
    public static Item GINKGO_FRUIT;
    public static Item SUPERIOR_BLAJ_ORE_ITEM;
    public static Item LESSER_BLAJ_ORE_ITEM;
    public static Item ITORE_ORE;
    public static Item GOLDEN_SEED;
    public static Item RAW_SILVER;
    public static Item RAW_SILVER_NUGGET;
    public static Item SILVER_INGOT;
    public static Item SILVER_WIRE;
    public static Item RUFESCENT_PEARL;
    public static Item TWINTAIL_BERRY;
    public static Item CHOCOLATE_BAR;
    public static Item FRAYED_NOTE;
    public static Item DIVINING_ROD;
    public static Item TOAST;
    public static Item TOFFEE_APPLE;

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

        BLAJ = register("blaj", new BlajItem(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("blaj")))));

        SUPERIOR_BLAJ_ORE_ITEM = register("superior_blaj_ore_item", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("superior_blaj_ore_item")))));

        LESSER_BLAJ_ORE_ITEM = register("lesser_blaj_ore_item", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("lesser_blaj_ore_item")))));

        ITORE_ORE = register("itore_ore", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("itore_ore")))));

        KLAEN = register("klaen", new BlockItem(
                stake_init.STAKE, //change
                new Item.Settings().food(
                                new FoodComponent.Builder()
                                        .alwaysEdible()
                                        .build(),
                                new ConsumableComponents().food()
                                        .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(pale_death_init.PALE_DEATH, 13 * 20, 1), 1.0f))
                                        .build())
                        .maxCount(9)
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

        PILLAR_OF_SALT = register("pillar_of_salt", new PillarOfSaltItem(new Item.Settings()
                .maxCount(1)
                .useCooldown(3)
                .rarity(Rarity.UNCOMMON)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("pillar_of_salt")))));

        DIVINING_ROD = register("divining_rod", new SeekingItem(block_init.GRAVESTONE, CustomParticles.GOLD_ORB, new Item.Settings()
                .maxCount(1)
                .useCooldown(3)
                .rarity(Rarity.UNCOMMON)
                .maxDamage(10)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("divining_rod")))));

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
                .maxCount(9)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("sanguine_flask"))))); //only gotten through distilling process

        FINGER = register("finger", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("finger")))));

        BLOODY_NAIL = register("bloody_nail", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("bloody_nail")))));

        TOOTH = register("tooth", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("tooth")))));

        SANGUINE_SYRINGE = register("sanguine_syringe", new Item(new Item.Settings()
                .maxCount(9)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("sanguine_syringe")))));

        CONTRACT = register("contract", new Item(new Item.Settings()
                .maxCount(1)
                .rarity(Rarity.EPIC)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("contract")))));

        PALE_AXE = register("pale_axe", new Item(new Item.Settings()
                .rarity(Rarity.EPIC)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("pale_axe")))));

        MY_RAGE = register("my_rage", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("my_rage")))));

        MY_FUEL = register("my_fuel", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("my_fuel")))));

        SHEPHERDS_STAFF = register("shepherds_staff", new ShepherdsStaffItem(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("shepherds_staff")))));

        VOID_MOTH_SPAWN_EGG = register("void_moth_spawn_egg", new SpawnEggItem(ModEntities.VOID_MOTH, new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("void_moth_spawn_egg")))));

        GINKGO_LEAF = register("ginkgo_leaf", new Item(new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("ginkgo_leaf")))));

        GOLDEN_SEED = register("golden_seed", new BlockItem(
                GINKGO_SAPLING, //change
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("golden_seed")))));

        GINKGO_FRUIT = register("ginkgo_fruit", new Item(new Item.Settings()
                .food(
                        new FoodComponent.Builder()
                                .alwaysEdible()
                                .build(),
                        new ConsumableComponents().food()
                                .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.POISON, 15 * 20, 1), 1.0f))
                                .build())
                .useRemainder(GOLDEN_SEED)
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("ginkgo_fruit")))));

        CIRCLET_OF_GLUT = register("circlet_of_glut", new CircletOfGlutItem(new Item.Settings()
                //.equippable(EquipmentSlot.HEAD)
                .rarity(Rarity.EPIC)
                .maxCount(1)
                .component(DataComponentTypes.ATTRIBUTE_MODIFIERS, AttributeModifiersComponent.builder()
                        .add(
                                EntityAttributes.ATTACK_DAMAGE, // +4 max HP
                                new EntityAttributeModifier(
                                        Identifier.of("the_tower","circlet_of_glut_attack_damage"),
                                        -3000,
                                        EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.HEAD
                        )
                        .add(
                                EntityAttributes.ARMOR, // +4 max HP
                                new EntityAttributeModifier(
                                        Identifier.of("the_tower","circlet_of_glut_armour"),
                                        -40,
                                        EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.HEAD
                        )
                        .add(
                                EntityAttributes.ARMOR_TOUGHNESS, // +4 max HP
                                new EntityAttributeModifier(
                                        Identifier.of("the_tower","circlet_of_glut_armour_toughness"),
                                        -40,
                                        EntityAttributeModifier.Operation.ADD_VALUE),
                                AttributeModifierSlot.HEAD
                        )
                        .build()
                )
                .component(
                        DataComponentTypes.EQUIPPABLE,
                        EquippableComponent.builder(EquipmentSlot.HEAD)
                                .equipSound(SoundEvents.ITEM_ARMOR_EQUIP_CHAIN)
                                .model(CustomEquipmentAssets.CIRCLET_OF_GLUT)
                                .damageOnHurt(false)
                                .build()
                )
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("circlet_of_glut")))));

        PLATE = register("plate", new ItemFrameItem(ModEntities.PLATE,
                new Item.Settings()
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("plate")))));

        WHITE_KEY = register("white_key", new KeyItem(DyeColor.WHITE,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("white_key")))));

        ORANGE_KEY = register("orange_key", new KeyItem(DyeColor.ORANGE,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("orange_key")))));

        MAGENTA_KEY = register("magenta_key", new KeyItem(DyeColor.MAGENTA,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("magenta_key")))));

        LIGHT_BLUE_KEY = register("light_blue_key", new KeyItem(DyeColor.LIGHT_BLUE,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("light_blue_key")))));

        YELLOW_KEY = register("yellow_key", new KeyItem(DyeColor.YELLOW,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("yellow_key")))));

        LIME_KEY = register("lime_key", new KeyItem(DyeColor.LIME,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("lime_key")))));

        PINK_KEY = register("pink_key", new KeyItem(DyeColor.PINK,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("pink_key")))));

        GREY_KEY = register("grey_key", new KeyItem(DyeColor.GRAY,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("grey_key")))));

        LIGHT_GREY_KEY = register("light_grey_key", new KeyItem(DyeColor.LIGHT_GRAY,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("light_grey_key")))));

        CYAN_KEY = register("cyan_key", new KeyItem(DyeColor.CYAN,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("cyan_key")))));

        PURPLE_KEY = register("purple_key", new KeyItem(DyeColor.PURPLE,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("purple_key")))));

        BLUE_KEY = register("blue_key", new KeyItem(DyeColor.BLUE,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("blue_key")))));

        BROWN_KEY = register("brown_key", new KeyItem(DyeColor.BROWN,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("brown_key")))));

        GREEN_KEY = register("green_key", new KeyItem(DyeColor.GREEN,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("green_key")))));

        RED_KEY = register("red_key", new KeyItem(DyeColor.RED,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("red_key")))));

        BLACK_KEY = register("black_key", new KeyItem(DyeColor.BLACK,
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("black_key")))));

        KEY = register("key", new Item(
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("key")))));

        RAW_SILVER = register("raw_silver", new Item(
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("raw_silver")))));

        RAW_SILVER_NUGGET = register("raw_silver_nugget", new Item(
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("raw_silver_nugget")))));

        SILVER_INGOT = register("silver_ingot", new Item(
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("silver_ingot")))));

        SILVER_WIRE = register("silver_wire", new Item(
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("silver_wire")))));

        RUFESCENT_PEARL = register("rufescent_pearl", new Item(
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("rufescent_pearl")))));

        TWINTAIL_BERRY = register("twintail_berry", new Item(new Item.Settings()
                .food(
                        new FoodComponent.Builder()
                                .nutrition(3)
                                .saturationModifier(0.4F)
                                .build())
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("twintail_berry")))));

        CHOCOLATE_BAR = register("chocolate_bar", new Item(new Item.Settings()
                .food(
                        new FoodComponent.Builder()
                                .nutrition(6)
                                .saturationModifier(0.2F)
                                .build())
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("chocolate_bar")))));

        TOAST = register("toast", new Item(new Item.Settings()
                .food(
                        new FoodComponent.Builder()
                                .nutrition(2)
                                .saturationModifier(0.2F)
                                .build())
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("toast")))));

        TOFFEE_APPLE = register("toffee_apple", new Item(new Item.Settings()
                .food(
                        new FoodComponent.Builder()
                                .nutrition(4)
                                .saturationModifier(0.3F)
                                .build())
                .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("toffee_apple")))));

        FRAYED_NOTE = register("frayed_note", new Item(
                new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, The_Tower.id("frayed_note")))));

    }

    public static Item register(String name, Item item) {
        Identifier id = The_Tower.id(name);
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, id);
        return Registry.register(Registries.ITEM, itemKey, item);
    }


    //public static void load() {} //This method allows us to load the items here into the game

}

