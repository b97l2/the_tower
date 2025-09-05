package mia.the_tower.initialisation;

import mia.the_tower.The_Tower;
import mia.the_tower.initialisation.biomes.ExtraTallFlower;
import mia.the_tower.initialisation.block.BarrierBlock;
import mia.the_tower.initialisation.block.GoldThurible;
import mia.the_tower.initialisation.block.LaunchBlock;
import mia.the_tower.initialisation.block.MarkerBlock;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class block_init {


    public static final Block DARKNESS = registerBlock("darkness", //!this creates a block of specified name
            new Block(AbstractBlock.Settings.create() //this begins the part where we put the block settings
                    //here you list the settings of the block.
                    //you can also copy the settings of a vanilla block
                    .strength(1.0F, 800.0F)
                    .requiresTool()
                    .slipperiness(0.99999F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "darkness")))));

    public static final Block OXIDISED_DARKNESS = registerBlock("oxidised_darkness",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .requiresTool()
                    .slipperiness(0.9F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "oxidised_darkness")))));

    public static final Block CUBE_OF_FLESH = registerBlock("cube_of_flesh",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cube_of_flesh")))));

    public static final Block STITCHED_FLESH = registerBlock("stitched_flesh",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "stitched_flesh")))));

    public static final Block CUBE_OF_PERCEIVING = registerBlock("cube_of_perceiving",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.6F, 4.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cube_of_perceiving")))));

    public static final Block PULLED_TEETH = registerBlock("pulled_teeth",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 8.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "pulled_teeth")))));

    public static final Block SKIN = registerBlock("skin",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "skin")))));

    public static final Block HEAVENS_LIGHT = registerBlock("heavens_light",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .requiresTool()
                    .noBlockBreakParticles()
                    .noCollision()
                    .nonOpaque()
                    .luminance(value -> 10)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "heavens_light")))));

    public static final Block POTENT_HEAVENS_LIGHT = registerBlock("potent_heavens_light",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .requiresTool()
                    .noBlockBreakParticles()
                    .noCollision()
                    .nonOpaque()
                    .luminance(value -> 15)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "potent_heavens_light")))));

    public static final Block HEAVENS_EARTH = registerBlock("heavens_earth",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .requiresTool()
                    .noBlockBreakParticles()
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "heavens_earth")))));

    public static final Block CUBE_OF_GLUH = registerBlock("cube_of_gluh",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.6F, 8.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cube_of_gluh")))));

    public static final Block GLUH_VINE_STAGE0 = registerBlock("gluh_vine_stage0",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .nonOpaque()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gluh_vine_stage0")))));

    public static final Block GLUH_VINE_STAGE1 = registerBlock("gluh_vine_stage1",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .nonOpaque()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gluh_vine_stage1")))));

    public static final Block GLUH_VINE_STAGE2 = registerBlock("gluh_vine_stage2",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .nonOpaque()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gluh_vine_stage2")))));

    public static final Block GLUH_VINE_STAGE3 = registerBlock("gluh_vine_stage3",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .nonOpaque()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gluh_vine_stage3")))));

    public static final Block GLUH_VINE_STAGE4 = registerBlock("gluh_vine_stage4",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .nonOpaque()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gluh_vine_stage4")))));

    public static final Block GLUH_VINE_STAGE5 = registerBlock("gluh_vine_stage5",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .nonOpaque()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gluh_vine_stage5")))));

    public static final Block GLUH_VINE_STAGE6 = registerBlock("gluh_vine_stage6",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .nonOpaque()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gluh_vine_stage6")))));

    public static final Block GLUH_VINE_STAGE7 = registerBlock("gluh_vine_stage7",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .nonOpaque()
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gluh_vine_stage7")))));

    public static final Block BARRIER = registerBlock("barrier",
            new BarrierBlock(AbstractBlock.Settings.create()
                    .strength(0.5F, 1.0F)
                    .breakInstantly()
                    .nonOpaque()
                    .luminance(value -> 3)
                    .sounds(BlockSoundGroup.INTENTIONALLY_EMPTY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "barrier")))));

    public static final Block CERULEAN_PETALS = registerBlock("cerulean_petals", //!*texture
            new FlowerbedBlock(AbstractBlock.Settings.create()
                    .noCollision()
                    .sounds(BlockSoundGroup.PINK_PETALS)
                    .mapColor(DyeColor.CYAN)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cerulean_petals")))));

    public static final Block BLUE_BELL = registerBlock("blue_bell",
            new FlowerBlock(
                    StatusEffects.SATURATION, // or any other StatusEffect
                    7.0F, // duration in seconds
                    AbstractBlock.Settings.create()
    			    .mapColor(MapColor.CYAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "blue_bell")))));

    public static final Block BLUE_FOXGLOVE = registerBlock("blue_foxglove", //!*texture
            new ExtraTallFlower(AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "blue_foxglove")))));

    public static final Block CERULEAN_WILDGRASS = registerBlock("cerulean_wildgrass", //!*texture
            new TallPlantBlock(AbstractBlock.Settings.create()
        			.mapColor(MapColor.CYAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .replaceable()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cerulean_wildgrass")))));

    public static final Block CERULEAN_SHORTGRASS = registerBlock("cerulean_shortgrass", //!*texture
            new ShortPlantBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .replaceable()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cerulean_shortgrass")))));

    public static final Block STARLIGHT_LICHEN = registerBlock("starlight_lichen", //!*texture
            new GlowLichenBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .noCollision()
                    .strength(0.2F)
                    .sounds(BlockSoundGroup.GLOW_LICHEN)
                    .luminance(GlowLichenBlock.getLuminanceSupplier(7))
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "starlight_lichen")))));

    public static final Block GABRIELS_TRUMPETS_BUSH = registerBlock("gabriels_trumpets_bush", //!*texture
            new SweetBerryBushBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .ticksRandomly()
                    .noCollision()
                    .sounds(BlockSoundGroup.SWEET_BERRY_BUSH)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .strength(0.2F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gabriels_trumpets_bush")))));

    public static final Block SAPPHIRE_ROSE = registerBlock("sapphire_rose",
            new FlowerBlock(
                    StatusEffects.SATURATION, // or any other StatusEffect
                    7.0F, // duration in seconds
                    AbstractBlock.Settings.create()
                            .mapColor(MapColor.CYAN)
                            .noCollision()
                            .sounds(BlockSoundGroup.GRASS)
                            .pistonBehavior(PistonBehavior.DESTROY)
                            .breakInstantly()
                            .offset(AbstractBlock.OffsetType.XZ)
                            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "sapphire_rose")))));

    public static Block DRAGONS_LEAP = registerBlock("dragons_leap",
            new LaunchBlock(AbstractBlock.Settings.create()
                    //.sounds(BlockSoundGroup.STONE)
                    .strength(0.7F, 8.0F)
                    .noCollision()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "dragons_leap")))));

    public static Block JESTERS_HAT = registerBlock("jesters_hat",
            new LaunchBlock(AbstractBlock.Settings.create()
                    //.sounds(BlockSoundGroup.STONE)
                    .strength(0.7F, 8.0F)
                    .noCollision()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "jesters_hat")))));

    public static Block LOAF = registerBlock("loaf",
            new loaf_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GRASS)
                    .strength(0.5F, 1.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "loaf")))));


    public static final Block INFESTED_ROCK = registerBlock("infested_rock",
            new CustomDirt(AbstractBlock.Settings.create()
                    .strength(1.0F, 2.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "infested_rock")))));

    public static final Block ROD_OF_LUMINESCENCE = registerBlock("rod_of_luminescence",
            new TwoBlockTallRod(AbstractBlock.Settings.create()
                    .strength(1.0F, 0.3F)
                    .luminance(value -> 15)
                    .sounds(BlockSoundGroup.GLASS)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "rod_of_luminescence")))));

    public static final Block CAN_OF_RETARDATION = registerBlock("can_of_retardation",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 0.3F)
                    .requiresTool()
                    .luminance(value -> 15)
                    .slipperiness(0.1F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "can_of_retardation")))));

    public static final FlowableFluid STILL_BLOOD = Registry.register(Registries.FLUID,
            RegistryKey.of(RegistryKeys.FLUID, The_Tower.id("blood")),
            new BloodFluid.Still());

    public static final FlowableFluid FLOWING_BLOOD = Registry.register(Registries.FLUID,
            RegistryKey.of(RegistryKeys.FLUID, The_Tower.id("flowing_blood")),
            new BloodFluid.Flowing());

    public static final Block BLOOD = Registry.register(Registries.BLOCK,
            RegistryKey.of(RegistryKeys.BLOCK, The_Tower.id("blood")),
            new FluidBlock(STILL_BLOOD, AbstractBlock.Settings.create()
                    .replaceable()
                    .mapColor(MapColor.DARK_RED)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, The_Tower.id("blood")))));

    public static final FlowableFluid STILL_GUNK = Registry.register(Registries.FLUID,
            RegistryKey.of(RegistryKeys.FLUID, The_Tower.id("gunk")),
            new GunkFluid.Still());

    public static final FlowableFluid FLOWING_GUNK = Registry.register(Registries.FLUID,
            RegistryKey.of(RegistryKeys.FLUID, The_Tower.id("flowing_gunk")),
            new GunkFluid.Flowing());

    public static final Block GUNK = Registry.register(Registries.BLOCK,
            RegistryKey.of(RegistryKeys.BLOCK, The_Tower.id("gunk")),
            new GunkFluidBlock(STILL_GUNK, AbstractBlock.Settings.create()
                    .replaceable()
                    .mapColor(MapColor.BLACK)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, The_Tower.id("gunk")))));

    public static final Block COFFER = registerBlock("coffer",
            new BarrelBlock(AbstractBlock.Settings.create()
                    .strength(3.0F, 0.3F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "coffer")))));

    public static final Block SALT = registerBlock("salt",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.9F, 0.3F)
                    .mapColor(MapColor.WHITE)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "salt")))));

    public static final Block SALT_CRYSTAL = registerBlock("salt_crystal", //!add custom class
            new PointedDripstoneBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .solid()
                    .nonOpaque()
                    .sounds(BlockSoundGroup.POINTED_DRIPSTONE)
                    .ticksRandomly()
                    .strength(1.5F, 3.0F)
                    .dynamicBounds()
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(Blocks::never)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "salt_crystal")))));

    public static final Block SHADOW = registerBlock("shadow",
            new Block(AbstractBlock.Settings.create()
                    .breakInstantly()
                    .noCollision()
                    .nonOpaque()
                    .noBlockBreakParticles()
                    .requiresTool()
                    .mapColor(MapColor.BLACK)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "shadow")))));

    public static final Block FRAGE_MARKER = registerBlock("frage_marker",
            new MarkerBlock(AbstractBlock.Settings.create()
                    .breakInstantly()
                    .noCollision()
                    .nonOpaque()
                    .noBlockBreakParticles()
                    .dropsNothing()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "frage_marker")))));

    public static final Block BLUE_HOSTAS = registerBlock("blue_hostas",
            new ShortPlantBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .replaceable()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "blue_hostas")))));

    public static final Block GOLDEN_THURIBLE = registerBlock("golden_thurible",
            new GoldThurible(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .solid()
                    .strength(3.5F)
                    .sounds(BlockSoundGroup.LANTERN)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "golden_thurible")))));

    public static final Block GOLD_WALL = registerBlock("gold_wall",
            new WallBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_wall")))));

    public static final Block GOLD_STAIRS = registerBlock("gold_stairs",
            new StairsBlock(Blocks.ANDESITE_WALL.getDefaultState(), AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_stairs")))));

    public static final Block GOLD_SLAB = registerBlock("gold_slab",
            new SlabBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_slab")))));

    public static final Block GOLD_COLUMN = registerBlock("gold_column",
            new column_init(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_column")))));

    ;


    public static Block registerBlock(String name, Block block) { //this is the method to register a new (non-item) block
        Identifier id = Identifier.of("the_tower", name);
        RegistryKey<Block> blockKey = RegistryKey.of(RegistryKeys.BLOCK, id);
        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    public static void registerBlockItem(String name, Block block) {
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, The_Tower.id(name));
        BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey));
        item_init.register(name, blockItem);
    }


    public static void load() {
        registerBlockItem("darkness",DARKNESS);
        registerBlockItem("oxidised_darkness",OXIDISED_DARKNESS);
        registerBlockItem("cube_of_flesh",CUBE_OF_FLESH);
        registerBlockItem("cube_of_perceiving", CUBE_OF_PERCEIVING); //*add texture
        registerBlockItem("pulled_teeth", PULLED_TEETH);
        registerBlockItem("skin", SKIN);
        registerBlockItem("heavens_light", HEAVENS_LIGHT);
        registerBlockItem("potent_heavens_light", POTENT_HEAVENS_LIGHT);
        registerBlockItem("heavens_earth", HEAVENS_EARTH);
        registerBlockItem("cube_of_gluh", CUBE_OF_GLUH);
        registerBlockItem("stitched_flesh", STITCHED_FLESH);
        registerBlockItem("gluh_vine_stage0",GLUH_VINE_STAGE0);
        registerBlockItem("gluh_vine_stage1",GLUH_VINE_STAGE1);
        registerBlockItem("gluh_vine_stage2",GLUH_VINE_STAGE2);
        registerBlockItem("gluh_vine_stage3",GLUH_VINE_STAGE3);
        registerBlockItem("gluh_vine_stage4",GLUH_VINE_STAGE4);
        registerBlockItem("gluh_vine_stage5",GLUH_VINE_STAGE5);
        registerBlockItem("gluh_vine_stage6",GLUH_VINE_STAGE6);
        registerBlockItem("gluh_vine_stage7",GLUH_VINE_STAGE7);
        registerBlockItem("cerulean_petals", CERULEAN_PETALS);
        registerBlockItem("blue_bell", BLUE_BELL);
        registerBlockItem("blue_foxglove", BLUE_FOXGLOVE);
        registerBlockItem("cerulean_wildgrass", CERULEAN_WILDGRASS);
        registerBlockItem("starlight_lichen", STARLIGHT_LICHEN);
        registerBlockItem("gabriels_trumpets_bush", GABRIELS_TRUMPETS_BUSH); // make berries and change loot table
        registerBlockItem("sapphire_rose", SAPPHIRE_ROSE);
        registerBlockItem("infested_rock", INFESTED_ROCK);
        registerBlockItem("cerulean_shortgrass", CERULEAN_SHORTGRASS);
        registerBlockItem("rod_of_luminescence", ROD_OF_LUMINESCENCE);
        registerBlockItem("can_of_retardation", CAN_OF_RETARDATION);
        registerBlockItem("dragons_leap", DRAGONS_LEAP);
        registerBlockItem("jesters_hat", JESTERS_HAT);
        registerBlockItem("loaf", LOAF);
        registerBlockItem("coffer", COFFER);
        registerBlockItem("shadow", SHADOW);
        registerBlockItem("blue_hostas", BLUE_HOSTAS);
        registerBlockItem("salt", SALT);
        registerBlockItem("golden_thurible", GOLDEN_THURIBLE);
        registerBlockItem("gold_wall", GOLD_WALL);
        registerBlockItem("gold_stairs", GOLD_STAIRS);
        registerBlockItem("gold_slab", GOLD_SLAB);
        registerBlockItem("gold_column", GOLD_COLUMN);

    } //to load into game

}
