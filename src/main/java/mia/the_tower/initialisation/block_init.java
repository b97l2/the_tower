package mia.the_tower.initialisation;

import mia.the_tower.The_Tower;
import mia.the_tower.initialisation.block.ExtraTallFlower;
import mia.the_tower.initialisation.block.*;
import mia.the_tower.initialisation.block.BarrierBlock;
import mia.the_tower.initialisation.blockentity.ColouredCofferBlock;
import mia.the_tower.initialisation.blockentity.StorageTemplateBlock;
import mia.the_tower.initialisation.fluid.BloodFluid;
import mia.the_tower.initialisation.fluid.BloodFluidBlock;
import mia.the_tower.initialisation.fluid.GunkFluid;
import mia.the_tower.initialisation.fluid.GunkFluidBlock;
import mia.the_tower.initialisation.fluid.fluid_init;
import mia.the_tower.initialisation.particle.CustomParticles;
import mia.the_tower.initialisation.sounds.CustomSounds;
import mia.the_tower.initialisation.world.ModConfiguredFeatures;
import mia.the_tower.initialisation.world.tree.ModSaplingGenerators;
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


    public static final Block DARKNESS = registerBlock("darkness", //this creates a block of specified name
            new Block(AbstractBlock.Settings.create() //this begins the part where we put the block settings
                    //here you list the settings of the block.
                    //you can also copy the settings of a vanilla block
                    .strength(1.0F, 1000.0F)
                    .requiresTool()
                    .slipperiness(0.99999F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "darkness")))));

    public static final Block BLAJ_ORE = registerBlock("blaj_ore",
            new Block(AbstractBlock.Settings.create()
                    .strength(2.0F, 1000.0F)
                    .requiresTool()
                    .emissiveLighting(Blocks::always)
                    .sounds(CustomSounds.BLAJ_BLOCK)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "blaj_ore")))));

    public static final Block BLAJ_LESSER_ORE = registerBlock("blaj_lesser_ore",
            new Block(AbstractBlock.Settings.create()
                    .strength(2.0F, 1000.0F)
                    .requiresTool()
                    .emissiveLighting(Blocks::always)
                    .sounds(CustomSounds.BLAJ_BLOCK)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "blaj_lesser_ore")))));


    public static final Block OXIDISED_DARKNESS = registerBlock("oxidised_darkness",
            new Block(AbstractBlock.Settings.create()
                    .strength(1.0F, 800.0F)
                    .requiresTool()
                    .slipperiness(0.9F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "oxidised_darkness")))));

    public static final Block CUBE_OF_FLESH = registerBlock("cube_of_flesh",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .mapColor(MapColor.RED)
                    .sounds(BlockSoundGroup.SLIME)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cube_of_flesh")))));

    public static final Block CUBE_OF_FLESH_WALL = registerBlock("cube_of_flesh_wall",
            new FluidloggableWallBlock(AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .mapColor(MapColor.RED)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.SLIME)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cube_of_flesh_wall")))));

    public static final Block CUBE_OF_FLESH_STAIRS = registerBlock("cube_of_flesh_stairs",
            new FluidloggableStairsBlock(CUBE_OF_FLESH.getDefaultState(), AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .mapColor(MapColor.RED)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.SLIME)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cube_of_flesh_stairs")))));

    public static final Block CUBE_OF_FLESH_SLAB = registerBlock("cube_of_flesh_slab",
            new FluidloggableSlabBlock(AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .mapColor(MapColor.RED)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.SLIME)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cube_of_flesh_slab")))));

    public static final Block CUBE_OF_FLESH_VERTICAL_SLAB = registerBlock("cube_of_flesh_vertical_slab",
            new VerticalSlabBlock(AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .mapColor(MapColor.RED)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.SLIME)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cube_of_flesh_vertical_slab")))));

    public static final Block CUBE_OF_FLESH_COLUMN = registerBlock("cube_of_flesh_column",
            new column_init(AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .mapColor(MapColor.RED)
                    .nonOpaque()
                    .sounds(BlockSoundGroup.SLIME)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cube_of_flesh_column")))));

    public static final Block VEINS = registerBlock("veins",
            new FluidloggableGlowLichenBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.RED)
                    .noCollision()
                    .strength(0.2F)
                    .sounds(BlockSoundGroup.SLIME)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "veins")))));

    public static final Block PILE_OF_MEAT = registerBlock("pile_of_meat",
            new SmallDecorativeBlock(AbstractBlock.Settings.create()
                    .strength(1F, 6.0F)
                    .mapColor(MapColor.RED)
                    .sounds(BlockSoundGroup.SLIME)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "pile_of_meat"))),
                    Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 4.0, 15.0)));

    public static final Block STITCHED_FLESH = registerBlock("stitched_flesh",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .mapColor(MapColor.RED)
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

    public static final Block FILTH = registerBlock("filth",
            new Block(AbstractBlock.Settings.create()
                    .strength(0.8F, 6.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "filth")))));

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
                    .replaceable()
                    .nonOpaque()
                    .emissiveLighting(Blocks::always)
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
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "blue_bell")))));

    public static final Block BLUE_FOXGLOVE = registerBlock("blue_foxglove", //!*texture
            new ExtraTallFlower(AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .noCollision()
                    .breakInstantly()
                    .nonOpaque()
                    .ticksRandomly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "blue_foxglove")))));

    public static final Block CERULEAN_WILDGRASS = registerBlock("cerulean_wildgrass",
            new TallPlantBlock(AbstractBlock.Settings.create()
        			.mapColor(MapColor.CYAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .replaceable()
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cerulean_wildgrass")))));

    public static final Block SUNKISSED_SHORTGRASS = registerBlock("sunkissed_shortgrass", //!*texture
            new ShortPlantBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .replaceable()
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "sunkissed_shortgrass")))));

    public static final Block CERULEAN_SHORTGRASS = registerBlock("cerulean_shortgrass", //!*texture
            new ShortPlantBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .noCollision()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .offset(AbstractBlock.OffsetType.XZ)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .replaceable()
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cerulean_shortgrass")))));

    public static final Block STARLIGHT_LICHEN = registerBlock("starlight_lichen", //!*texture
            new FluidloggableGlowLichenBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.CYAN)
                    .noCollision()
                    .strength(0.2F)
                    .sounds(BlockSoundGroup.GLOW_LICHEN)
                    .luminance(GlowLichenBlock.getLuminanceSupplier(7))
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "starlight_lichen")))));

    public static final Block GABRIELS_TRUMPETS_BUSH = registerBlock("gabriels_trumpets_bush", //!*texture
            new TwintailBerryBushBlock(AbstractBlock.Settings.create()
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
            new SmallDecorativeBlock(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.GRASS)
                    .strength(0.5F, 1.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "loaf"))),
                    Block.createCuboidShape(5.0, 0.0, 2.0, 11.0, 7.0, 14.0),
                    Block.createCuboidShape(5.0, 0.0, 2.0, 11.0, 7.0, 14.0),
                    Block.createCuboidShape(2.0, 0.0, 5.0, 14.0, 7.0, 11.0),
                    Block.createCuboidShape(2.0, 0.0, 5.0, 14.0, 7.0, 11.0)));

    public static final Block INFESTED_ROCK = registerBlock("infested_rock",
            new CustomDirt(AbstractBlock.Settings.create()
                    .strength(2.5F, 2.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "infested_rock")))));

    public static final Block OLD_SOIL = registerBlock("old_soil",
            new CustomDirt(AbstractBlock.Settings.create()
                    .strength(1.5F, 1.5F)
                    .sounds(BlockSoundGroup.ROOTED_DIRT)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "old_soil")))));

    public static final Block OLD_MEADOW = registerBlock("old_meadow",
            new CustomGrass(AbstractBlock.Settings.create()
                    .strength(1.5F, 1.5F)
                    .sounds(BlockSoundGroup.GRASS)
                    .ticksRandomly()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "old_meadow")))));

    public static final Block DRAGONS_EYE_ORE = registerBlock("dragons_eye_ore",
            new CustomDirt(AbstractBlock.Settings.create()
                    .strength(1.5F, 3.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "dragons_eye_ore")))));

    public static final Block PETRIFIED_STONE = registerBlock("petrified_stone",
            new Block(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "petrified_stone")))));

    public static final Block CRACKED_PETRIFIED_STONE = registerBlock("cracked_petrified_stone",
            new PillarBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cracked_petrified_stone")))));

    public static final Block EMBOSSED_PETRIFIED_STONE = registerBlock("embossed_petrified_stone",
            new PillarBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "embossed_petrified_stone")))));

    public static final Block CRACKED_EMBOSSED_PETRIFIED_STONE = registerBlock("cracked_embossed_petrified_stone",
            new PillarBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cracked_embossed_petrified_stone")))));

    public static final Block TILED_PETRIFIED_STONE = registerBlock("tiled_petrified_stone",
            new PillarBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "tiled_petrified_stone")))));

    public static final Block CRACKED_TILED_PETRIFIED_STONE = registerBlock("cracked_tiled_petrified_stone",
            new PillarBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cracked_tiled_petrified_stone")))));

    public static final Block ADORNED_PETRIFIED_STONE = registerBlock("adorned_petrified_stone",
            new PillarBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "adorned_petrified_stone")))));

    public static final Block PETRIFIED_STONE_STAIRS = registerBlock("petrified_stone_stairs",
            new FluidloggableStairsBlock(PETRIFIED_STONE.getDefaultState(), AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "petrified_stone_stairs")))));

    public static final Block PETRIFIED_STONE_WALL = registerBlock("petrified_stone_wall",
            new FluidloggableWallBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "petrified_stone_wall")))));

    public static final Block PETRIFIED_STONE_COLUMN = registerBlock("petrified_stone_column",
            new column_init(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "petrified_stone_column")))));

    public static final Block PETRIFIED_STONE_SLAB = registerBlock("petrified_stone_slab",
            new FluidloggableSlabBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "petrified_stone_slab")))));

    public static final Block PETRIFIED_STONE_VERTICAL_SLAB = registerBlock("petrified_stone_vertical_slab",
            new VerticalSlabBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "petrified_stone_vertical_slab")))));

    public static final Block EMBOSSED_PETRIFIED_STONE_STAIRS = registerBlock("embossed_petrified_stone_stairs",
            new FluidloggableStairsBlock(EMBOSSED_PETRIFIED_STONE.getDefaultState(), AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "embossed_petrified_stone_stairs")))));

    public static final Block EMBOSSED_PETRIFIED_STONE_WALL = registerBlock("embossed_petrified_stone_wall",
            new FluidloggableWallBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "embossed_petrified_stone_wall")))));

    public static final Block EMBOSSED_PETRIFIED_STONE_COLUMN = registerBlock("embossed_petrified_stone_column",
            new column_init(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "embossed_petrified_stone_column")))));

    public static final Block EMBOSSED_PETRIFIED_STONE_SLAB = registerBlock("embossed_petrified_stone_slab",
            new FluidloggableSlabBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "embossed_petrified_stone_slab")))));

    public static final Block EMBOSSED_PETRIFIED_STONE_VERTICAL_SLAB = registerBlock("embossed_petrified_stone_vertical_slab",
            new VerticalSlabBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "embossed_petrified_stone_vertical_slab")))));

    public static final Block TILED_PETRIFIED_STONE_STAIRS = registerBlock("tiled_petrified_stone_stairs",
            new FluidloggableStairsBlock(TILED_PETRIFIED_STONE.getDefaultState(), AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "tiled_petrified_stone_stairs")))));

    public static final Block TILED_PETRIFIED_STONE_WALL = registerBlock("tiled_petrified_stone_wall",
            new FluidloggableWallBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "tiled_petrified_stone_wall")))));

    public static final Block TILED_PETRIFIED_STONE_COLUMN = registerBlock("tiled_petrified_stone_column",
            new column_init(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "tiled_petrified_stone_column")))));

    public static final Block TILED_PETRIFIED_STONE_SLAB = registerBlock("tiled_petrified_stone_slab",
            new FluidloggableSlabBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "tiled_petrified_stone_slab")))));

    public static final Block TILED_PETRIFIED_STONE_VERTICAL_SLAB = registerBlock("tiled_petrified_stone_vertical_slab",
            new VerticalSlabBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "tiled_petrified_stone_vertical_slab")))));

    public static final Block ADORNED_PETRIFIED_STONE_STAIRS = registerBlock("adorned_petrified_stone_stairs",
            new FluidloggableStairsBlock(ADORNED_PETRIFIED_STONE.getDefaultState(), AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "adorned_petrified_stone_stairs")))));

    public static final Block ADORNED_PETRIFIED_STONE_WALL = registerBlock("adorned_petrified_stone_wall",
            new FluidloggableWallBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "adorned_petrified_stone_wall")))));

    public static final Block ADORNED_PETRIFIED_STONE_COLUMN = registerBlock("adorned_petrified_stone_column",
            new column_init(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "adorned_petrified_stone_column")))));

    public static final Block ADORNED_PETRIFIED_STONE_SLAB = registerBlock("adorned_petrified_stone_slab",
            new FluidloggableSlabBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "adorned_petrified_stone_slab")))));

    public static final Block ADORNED_PETRIFIED_STONE_VERTICAL_SLAB = registerBlock("adorned_petrified_stone_vertical_slab",
            new VerticalSlabBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "adorned_petrified_stone_vertical_slab")))));

    public static final Block GROTESQUE = registerBlock("grotesque",
            new CustomHorisontalFacingBlock(AbstractBlock.Settings.create()
                    .strength(3.3F, 20.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "grotesque")))));

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

    public static final FlowableFluid STILL_BLOOD = fluid_init.STILL_BLOOD;
    public static final FlowableFluid FLOWING_BLOOD = fluid_init.FLOWING_BLOOD;
    public static final Block BLOOD = fluid_init.BLOOD;
    public static final FlowableFluid STILL_GUNK = fluid_init.STILL_GUNK;
    public static final FlowableFluid FLOWING_GUNK = fluid_init.FLOWING_GUNK;
    public static final Block GUNK = fluid_init.GUNK;

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
            new ThuribleBlock(CustomParticles.GOLD_INCENSE, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .solid()
                    .strength(3.5F)
                    .sounds(BlockSoundGroup.LANTERN)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "golden_thurible")))));

    public static final Block GOLD_WALL = registerBlock("gold_wall",
            new FluidloggableWallBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_wall")))));

    public static final Block GOLD_STAIRS = registerBlock("gold_stairs",
            new FluidloggableStairsBlock(Blocks.ANDESITE_WALL.getDefaultState(), AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_stairs")))));

    public static final Block GOLD_SLAB = registerBlock("gold_slab",
            new FluidloggableSlabBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_slab")))));

    public static final Block GOLD_VERTICAL_SLAB = registerBlock("gold_vertical_slab",
            new VerticalSlabBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_vertical_slab")))));

    public static final Block GOLD_COLUMN = registerBlock("gold_column",
            new column_init(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_column")))));

    public static final Block GOLD_TRAPDOOR = registerBlock("gold_trapdoor",
            new FluidloggableTrapdoorBlock(BlockSetType.COPPER, AbstractBlock.Settings.create() //set to copper because this is mainly just a decorative block and not supposed to have any unique redstone functionality
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_trapdoor")))));

    public static final Block GOLD_BUTTON = registerBlock("gold_button",
            new ButtonBlock(BlockSetType.COPPER, 10, AbstractBlock.Settings.create() //set to copper because this is mainly just a decorative block and not supposed to have any unique redstone functionality
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_button")))));

    public static final Block GOLD_BARS = registerBlock("gold_bars",
            new PaneBlock(AbstractBlock.Settings.create()
                    .requiresTool()
                    .nonOpaque()
                    .sounds(BlockSoundGroup.METAL)
                    .mapColor(MapColor.GOLD)
                    .strength(5F, 6.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_bars")))));

    public static final Block GOLD_LADDER = registerBlock("gold_ladder",
            new FluidloggableLadderBlock(AbstractBlock.Settings.create()
                    .requiresTool()
                    .nonOpaque()
                    .noCollision()
                    .sounds(BlockSoundGroup.METAL)
                    .mapColor(MapColor.GOLD)
                    .strength(3F, 6.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gold_ladder")))));


    public static final Block GINKGO_LOG = registerBlock("ginkgo_log",
            new PillarBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .burnable()
                    .strength(2f)
                    .sounds(BlockSoundGroup.WOOD)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "ginkgo_log")))));

    public static final Block STRIPPED_GINKGO_LOG = registerBlock("stripped_ginkgo_log",
            new PillarBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .burnable()
                    .strength(2f)
                    .sounds(BlockSoundGroup.WOOD)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "stripped_ginkgo_log")))));

    public static final Block GINKGO_PLANKS = registerBlock("ginkgo_planks",
            new PillarBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .burnable()
                    .strength(2f)
                    .sounds(BlockSoundGroup.WOOD)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "ginkgo_planks")))));

    public static final Block GINKGO_STAIRS = registerBlock("ginkgo_stairs",
            new FluidloggableStairsBlock(Blocks.ANDESITE_WALL.getDefaultState(), AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "ginkgo_stairs")))));

    public static final Block GINKGO_SLAB = registerBlock("ginkgo_slab",
            new FluidloggableSlabBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "ginkgo_slab")))));

    public static final Block GINKGO_VERTICAL_SLAB = registerBlock("ginkgo_vertical_slab",
            new VerticalSlabBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "ginkgo_vertical_slab")))));

    public static final Block GINKGO_TRAPDOOR = registerBlock("ginkgo_trapdoor",
            new FluidloggableTrapdoorBlock(BlockSetType.OAK, AbstractBlock.Settings.create() //set to copper because this is mainly just a decorative block and not supposed to have any unique redstone functionality
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "ginkgo_trapdoor")))));

    public static final Block GINKGO_BUTTON = registerBlock("ginkgo_button",
            new ButtonBlock(BlockSetType.OAK, 10, AbstractBlock.Settings.create() //set to copper because this is mainly just a decorative block and not supposed to have any unique redstone functionality
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.GOLD)
                    .strength(3.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "ginkgo_button")))));

    public static final Block GINKGO_LEAVES = registerBlock("ginkgo_leaves",
            new NonDecayingLeavesBlock(5, CustomParticles.GOLD_INCENSE, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .sounds(BlockSoundGroup.GRASS)
                    .burnable()
                    .strength(0.2F)
                    .nonOpaque()
                    .allowsSpawning(Blocks::canSpawnOnLeaves)
                    .suffocates(Blocks::never)
                    .blockVision(Blocks::never)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .solidBlock(Blocks::never)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "ginkgo_leaves")))));

    public static final Block GINKGO_SAPLING = registerBlock("ginkgo_sapling",
            new SaplingBlock(ModSaplingGenerators.GINKGO, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GOLD)
                    .sounds(BlockSoundGroup.GRASS)
                    .burnable()
                    .nonOpaque()
                    .noCollision()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "ginkgo_sapling")))));

    public static final Block CHANTERELLE = registerBlock("chanterelle",
            new MushroomPlantBlock(ModConfiguredFeatures.GINKGO_KEY, AbstractBlock.Settings.create() //make large mushroom variant!
                    .mapColor(MapColor.ORANGE)
                    .noCollision()
                    .ticksRandomly()
                    .breakInstantly()
                    .sounds(BlockSoundGroup.GRASS)
                    .postProcess(Blocks::always)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "chanterelle")))));


    public static final Block VAULTS_STANCHION_SAPLING = registerBlock("vaults_stanchion_sapling",
            new SaplingBlock(ModSaplingGenerators.VAULTS_STANCHION, AbstractBlock.Settings.create()
                    .mapColor(MapColor.TEAL)
                    .sounds(BlockSoundGroup.GRASS)
                    .burnable()
                    .nonOpaque()
                    .noCollision()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "vaults_stanchion_sapling")))));

    public static final Block AFHD = registerBlock("afhd",
            new Block(AbstractBlock.Settings.create()
                    .requiresTool()
                    .mapColor(MapColor.LAPIS_BLUE)
                    .strength(3F, 12.0F)
                    .sounds(BlockSoundGroup.STONE)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "afhd")))));

    public static final Block SLATE = registerBlock("slate",
            new PillarBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.BLACK)
                    .strength(1F, 3.0F)
                    .sounds(BlockSoundGroup.DEEPSLATE_TILES)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "slate")))));

    public static final Block ITORE = registerBlock("itore",
            new PillarBlock(AbstractBlock.Settings.create()
                    .requiresTool()
                    .mapColor(MapColor.BLACK)
                    .strength(1F, 3.0F)
                    .sounds(BlockSoundGroup.STONE)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "itore")))));

    public static final Block TANKARD = registerBlock("tankard",
            new SmallDecorativeBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.BROWN)
                    .strength(0.3F, 1.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "tankard"))),
                    Block.createCuboidShape(4.0, 0.0, 5.0, 11.0, 7.0, 10.0),
                    Block.createCuboidShape(5.0, 0.0, 6.0, 12.0, 7.0, 11.0),
                    Block.createCuboidShape(6.0, 0.0, 4.0, 11.0, 7.0, 11.0),
                    Block.createCuboidShape(5.0, 0.0, 5.0, 10.0, 7.0, 12.0)));

    public static final Block SPIRITS = registerBlock("spirits",
            new SmallDecorativeBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.WHITE)
                    .strength(0.3F, 1.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "spirits"))),
                    Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 13.0, 10.0)));

    public static final Block SAUERKRAUT = registerBlock("sauerkraut",
            new SmallDecorativeBlock(AbstractBlock.Settings.create()
                    .strength(0.3F, 1.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "sauerkraut"))),
                    Block.createCuboidShape(6.0, 0.0, 6.0, 10.0, 6.5, 10.0)));

    public static final Block WHITE_COFFER = registerBlock("white_coffer",
            new ColouredCofferBlock(DyeColor.WHITE, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "white_coffer")))));

    public static final Block ORANGE_COFFER = registerBlock("orange_coffer",
            new ColouredCofferBlock(DyeColor.ORANGE, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "orange_coffer")))));

    public static final Block MAGENTA_COFFER = registerBlock("magenta_coffer",
            new ColouredCofferBlock(DyeColor.MAGENTA, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "magenta_coffer")))));

    public static final Block LIGHT_BLUE_COFFER = registerBlock("light_blue_coffer",
            new ColouredCofferBlock(DyeColor.LIGHT_BLUE, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "light_blue_coffer")))));

    public static final Block YELLOW_COFFER = registerBlock("yellow_coffer",
            new ColouredCofferBlock(DyeColor.YELLOW, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "yellow_coffer")))));

    public static final Block LIME_COFFER = registerBlock("lime_coffer",
            new ColouredCofferBlock(DyeColor.LIME, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "lime_coffer")))));

    public static final Block PINK_COFFER = registerBlock("pink_coffer",
            new ColouredCofferBlock(DyeColor.PINK, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "pink_coffer")))));

    public static final Block GREY_COFFER = registerBlock("grey_coffer",
            new ColouredCofferBlock(DyeColor.GRAY, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "grey_coffer")))));

    public static final Block LIGHT_GREY_COFFER = registerBlock("light_grey_coffer",
            new ColouredCofferBlock(DyeColor.LIGHT_GRAY, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "light_grey_coffer")))));

    public static final Block CYAN_COFFER = registerBlock("cyan_coffer",
            new ColouredCofferBlock(DyeColor.CYAN, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cyan_coffer")))));

    public static final Block PURPLE_COFFER = registerBlock("purple_coffer",
            new ColouredCofferBlock(DyeColor.PURPLE, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "purple_coffer")))));

    public static final Block BLUE_COFFER = registerBlock("blue_coffer",
            new ColouredCofferBlock(DyeColor.BLUE, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "blue_coffer")))));

    public static final Block BROWN_COFFER = registerBlock("brown_coffer",
            new ColouredCofferBlock(DyeColor.BROWN, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "brown_coffer")))));

    public static final Block GREEN_COFFER = registerBlock("green_coffer",
            new ColouredCofferBlock(DyeColor.GREEN, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "green_coffer")))));

    public static final Block RED_COFFER = registerBlock("red_coffer",
            new ColouredCofferBlock(DyeColor.RED, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "red_coffer")))));

    public static final Block BLACK_COFFER = registerBlock("black_coffer",
            new ColouredCofferBlock(DyeColor.BLACK, AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "black_coffer")))));

    public static final Block SIMPLE_COFFER = registerBlock("simple_coffer",
            new CustomHorisontalFacingBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1F, 10.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "simple_coffer")))));



    public static final Block SILVER_ORE = registerBlock("silver_ore",
            new PillarBlock(AbstractBlock.Settings.create()
                    .requiresTool()
                    .mapColor(MapColor.GRAY)
                    .strength(2.6F, 4.0F)
                    .sounds(BlockSoundGroup.STONE)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "silver_ore")))));

    public static final Block SILVER_VEIN = registerBlock("silver_vein",
            new PillarBlock(AbstractBlock.Settings.create()
                    .requiresTool()
                    .mapColor(MapColor.GRAY)
                    .strength(2.6F, 4.0F)
                    .sounds(BlockSoundGroup.STONE)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "silver_vein")))));

    public static final Block RAW_SILVER_BLOCK = registerBlock("raw_silver_block",
            new PillarBlock(AbstractBlock.Settings.create()
                    .requiresTool()
                    .mapColor(MapColor.GRAY)
                    .strength(2.6F, 4.0F)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "raw_silver_block")))));

    public static final Block SILVER_BLOCK = registerBlock("silver_block",
            new PillarBlock(AbstractBlock.Settings.create()
                    .requiresTool()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .strength(2.8F, 4.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "silver_block")))));

    public static final Block SILVER_WALL = registerBlock("silver_wall",
            new FluidloggableWallBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .strength(2.8F, 4.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "silver_wall")))));

    public static final Block SILVER_STAIRS = registerBlock("silver_stairs",
            new FluidloggableStairsBlock(Blocks.ANDESITE_WALL.getDefaultState(), AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .strength(2.8F, 4.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "silver_stairs")))));

    public static final Block SILVER_SLAB = registerBlock("silver_slab",
            new FluidloggableSlabBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .strength(2.8F, 4.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "silver_slab")))));

    public static final Block SILVER_VERTICAL_SLAB = registerBlock("silver_vertical_slab",
            new VerticalSlabBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .strength(2.8F, 4.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "silver_vertical_slab")))));

    public static final Block SILVER_COLUMN = registerBlock("silver_column",
            new column_init(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .strength(2.8F, 4.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "silver_column")))));

    public static final Block SILVER_LANTERN = registerBlock("silver_lantern",
            new FluidloggableLanternBlock(AbstractBlock.Settings.create()
                    .requiresTool()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .strength(2.8F, 4.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> 15)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "silver_lantern")))));

    public static final Block SILVER_THURIBLE = registerBlock("silver_thurible",
            new ThuribleBlock(CustomParticles.WHITE_INCENSE, AbstractBlock.Settings.create()
                    .mapColor(MapColor.LIGHT_GRAY)
                    .solid()
                    .strength(2.8F)
                    .sounds(BlockSoundGroup.LANTERN)
                    .nonOpaque()
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "silver_thurible")))));

    public static final Block GRAVESTONE = registerBlock("gravestone",
            new StorageTemplateBlock(AbstractBlock.Settings.create()
                    .mapColor(MapColor.GRAY)
                    .strength(1.3F, 50.0F)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gravestone")))));

    public static final Block REDSTONE_LANTERN = registerBlock("redstone_lantern",
            new FluidloggableLanternBlock(AbstractBlock.Settings.create()
                    .requiresTool()
                    .mapColor(MapColor.DARK_RED)
                    .strength(2.8F, 4.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .luminance(state -> 15)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "redstone_lantern")))));

    public static final Block REDSTONE_CHAIN = registerBlock("redstone_chain",
            new ChainBlock(AbstractBlock.Settings.create()
                    .solid()
                    .requiresTool()
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.CHAIN)
                    .nonOpaque()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "redstone_chain")))));

    public static final Block EMERALD_WALL = registerBlock("emerald_wall",
            new FluidloggableWallBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.EMERALD_GREEN)
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "emerald_wall")))));

    public static final Block EMERALD_STAIRS = registerBlock("emerald_stairs",
            new FluidloggableStairsBlock(Blocks.ANDESITE_WALL.getDefaultState(), AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.EMERALD_GREEN)
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "emerald_stairs")))));

    public static final Block EMERALD_SLAB = registerBlock("emerald_slab",
            new FluidloggableSlabBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.EMERALD_GREEN)
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "emerald_slab")))));

    public static final Block EMERALD_VERTICAL_SLAB = registerBlock("emerald_vertical_slab",
            new VerticalSlabBlock(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.EMERALD_GREEN)
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "emerald_vertical_slab")))));

    public static final Block EMERALD_COLUMN = registerBlock("emerald_column",
            new column_init(AbstractBlock.Settings.create()
                    .nonOpaque()
                    .requiresTool()
                    .mapColor(MapColor.EMERALD_GREEN)
                    .strength(5.0F, 6.0F)
                    .sounds(BlockSoundGroup.METAL)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "emerald_column")))));

    public static final Block KAOLIN = registerBlock("kaolin",
            new Block(AbstractBlock.Settings.create()
                    .mapColor(MapColor.OFF_WHITE)
                    .strength(1.1F, 1.0F)
                    .sounds(BlockSoundGroup.MUD_BRICKS)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "kaolin")))));

    private static AbstractBlock.Settings jadeSettings(String name) {
        return AbstractBlock.Settings.create()
                .mapColor(MapColor.EMERALD_GREEN)
                .strength(4F, 10.0F)
                .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", name)));
    }

    private static AbstractBlock.Settings jadeVariantSettings(String name) {
        return jadeSettings(name).nonOpaque();
    }

    private static AbstractBlock.Settings emissiveJadeSettings(String name) {
        return jadeSettings(name).emissiveLighting(Blocks::always);
    }

    private static AbstractBlock.Settings emissiveJadeVariantSettings(String name) {
        return emissiveJadeSettings(name).nonOpaque();
    }

    public static final Block IMPERIAL_JADE_BLOCK = registerBlock("imperial_jade_block",
            new Block(jadeVariantSettings("imperial_jade_block")));
    public static final Block IMPERIAL_JADE_WALL = registerBlock("imperial_jade_wall",
            new FluidloggableWallBlock(jadeVariantSettings("imperial_jade_wall")));
    public static final Block IMPERIAL_JADE_STAIRS = registerBlock("imperial_jade_stairs",
            new FluidloggableStairsBlock(IMPERIAL_JADE_BLOCK.getDefaultState(), jadeVariantSettings("imperial_jade_stairs")));
    public static final Block IMPERIAL_JADE_SLAB = registerBlock("imperial_jade_slab",
            new FluidloggableSlabBlock(jadeVariantSettings("imperial_jade_slab")));
    public static final Block IMPERIAL_JADE_VERTICAL_SLAB = registerBlock("imperial_jade_vertical_slab",
            new VerticalSlabBlock(jadeVariantSettings("imperial_jade_vertical_slab")));
    public static final Block IMPERIAL_JADE_COLUMN = registerBlock("imperial_jade_column",
            new column_init(jadeVariantSettings("imperial_jade_column")));

    public static final Block BLOOD_JADE_BLOCK = registerBlock("blood_jade_block",
            new Block(jadeSettings("blood_jade_block")));
    public static final Block BLOOD_JADE_WALL = registerBlock("blood_jade_wall",
            new FluidloggableWallBlock(jadeVariantSettings("blood_jade_wall")));
    public static final Block BLOOD_JADE_STAIRS = registerBlock("blood_jade_stairs",
            new FluidloggableStairsBlock(BLOOD_JADE_BLOCK.getDefaultState(), jadeVariantSettings("blood_jade_stairs")));
    public static final Block BLOOD_JADE_SLAB = registerBlock("blood_jade_slab",
            new FluidloggableSlabBlock(jadeVariantSettings("blood_jade_slab")));
    public static final Block BLOOD_JADE_VERTICAL_SLAB = registerBlock("blood_jade_vertical_slab",
            new VerticalSlabBlock(jadeVariantSettings("blood_jade_vertical_slab")));
    public static final Block BLOOD_JADE_COLUMN = registerBlock("blood_jade_column",
            new column_init(jadeVariantSettings("blood_jade_column")));

    public static final Block JADEITE_BLOCK = registerBlock("jadeite_block",
            new Block(jadeSettings("jadeite_block")));
    public static final Block UNCUT_JADEITE_STONE = registerBlock("uncut_jadeite_stone",
            new Block(jadeSettings("uncut_jadeite_stone")));
    public static final Block JADEITE_WALL = registerBlock("jadeite_wall",
            new FluidloggableWallBlock(jadeVariantSettings("jadeite_wall")));
    public static final Block JADEITE_STAIRS = registerBlock("jadeite_stairs",
            new FluidloggableStairsBlock(JADEITE_BLOCK.getDefaultState(), jadeVariantSettings("jadeite_stairs")));
    public static final Block JADEITE_SLAB = registerBlock("jadeite_slab",
            new FluidloggableSlabBlock(jadeVariantSettings("jadeite_slab")));
    public static final Block JADEITE_VERTICAL_SLAB = registerBlock("jadeite_vertical_slab",
            new VerticalSlabBlock(jadeVariantSettings("jadeite_vertical_slab")));
    public static final Block JADEITE_COLUMN = registerBlock("jadeite_column",
            new column_init(jadeVariantSettings("jadeite_column")));

    public static final Block MUTTON_FAT_JADE_BLOCK = registerBlock("mutton_fat_jade_block",
            new Block(jadeSettings("mutton_fat_jade_block")));
    public static final Block MUTTON_FAT_JADE_WALL = registerBlock("mutton_fat_jade_wall",
            new FluidloggableWallBlock(jadeVariantSettings("mutton_fat_jade_wall")));
    public static final Block MUTTON_FAT_JADE_STAIRS = registerBlock("mutton_fat_jade_stairs",
            new FluidloggableStairsBlock(MUTTON_FAT_JADE_BLOCK.getDefaultState(), jadeVariantSettings("mutton_fat_jade_stairs")));
    public static final Block MUTTON_FAT_JADE_SLAB = registerBlock("mutton_fat_jade_slab",
            new FluidloggableSlabBlock(jadeVariantSettings("mutton_fat_jade_slab")));
    public static final Block MUTTON_FAT_JADE_VERTICAL_SLAB = registerBlock("mutton_fat_jade_vertical_slab",
            new VerticalSlabBlock(jadeVariantSettings("mutton_fat_jade_vertical_slab")));
    public static final Block MUTTON_FAT_JADE_COLUMN = registerBlock("mutton_fat_jade_column",
            new column_init(jadeVariantSettings("mutton_fat_jade_column")));

    public static final Block LAVENDER_JADE_BLOCK = registerBlock("lavender_jade_block",
            new Block(jadeSettings("lavender_jade_block")));
    public static final Block LAVENDER_JADE_WALL = registerBlock("lavender_jade_wall",
            new FluidloggableWallBlock(jadeVariantSettings("lavender_jade_wall")));
    public static final Block LAVENDER_JADE_STAIRS = registerBlock("lavender_jade_stairs",
            new FluidloggableStairsBlock(LAVENDER_JADE_BLOCK.getDefaultState(), jadeVariantSettings("lavender_jade_stairs")));
    public static final Block LAVENDER_JADE_SLAB = registerBlock("lavender_jade_slab",
            new FluidloggableSlabBlock(jadeVariantSettings("lavender_jade_slab")));
    public static final Block LAVENDER_JADE_VERTICAL_SLAB = registerBlock("lavender_jade_vertical_slab",
            new VerticalSlabBlock(jadeVariantSettings("lavender_jade_vertical_slab")));
    public static final Block LAVENDER_JADE_COLUMN = registerBlock("lavender_jade_column",
            new column_init(jadeVariantSettings("lavender_jade_column")));

    public static final Block LOTUS_JADE_BLOCK = registerBlock("lotus_jade_block",
            new Block(emissiveJadeVariantSettings("lotus_jade_block")));
    public static final Block LOTUS_JADE_WALL = registerBlock("lotus_jade_wall",
            new FluidloggableWallBlock(emissiveJadeVariantSettings("lotus_jade_wall")));
    public static final Block LOTUS_JADE_STAIRS = registerBlock("lotus_jade_stairs",
            new FluidloggableStairsBlock(LOTUS_JADE_BLOCK.getDefaultState(), emissiveJadeVariantSettings("lotus_jade_stairs")));
    public static final Block LOTUS_JADE_SLAB = registerBlock("lotus_jade_slab",
            new FluidloggableSlabBlock(emissiveJadeVariantSettings("lotus_jade_slab")));
    public static final Block LOTUS_JADE_VERTICAL_SLAB = registerBlock("lotus_jade_vertical_slab",
            new VerticalSlabBlock(emissiveJadeVariantSettings("lotus_jade_vertical_slab")));
    public static final Block LOTUS_JADE_COLUMN = registerBlock("lotus_jade_column",
            new column_init(emissiveJadeVariantSettings("lotus_jade_column")));

    public static final Block PHOENIX_BLOOD_JADE_BLOCK = registerBlock("phoenix_blood_jade_block",
            new Block(emissiveJadeSettings("phoenix_blood_jade_block")));
    public static final Block PHOENIX_BLOOD_JADE_WALL = registerBlock("phoenix_blood_jade_wall",
            new FluidloggableWallBlock(emissiveJadeVariantSettings("phoenix_blood_jade_wall")));
    public static final Block PHOENIX_BLOOD_JADE_STAIRS = registerBlock("phoenix_blood_jade_stairs",
            new FluidloggableStairsBlock(PHOENIX_BLOOD_JADE_BLOCK.getDefaultState(), emissiveJadeVariantSettings("phoenix_blood_jade_stairs")));
    public static final Block PHOENIX_BLOOD_JADE_SLAB = registerBlock("phoenix_blood_jade_slab",
            new FluidloggableSlabBlock(emissiveJadeVariantSettings("phoenix_blood_jade_slab")));
    public static final Block PHOENIX_BLOOD_JADE_VERTICAL_SLAB = registerBlock("phoenix_blood_jade_vertical_slab",
            new VerticalSlabBlock(emissiveJadeVariantSettings("phoenix_blood_jade_vertical_slab")));
    public static final Block PHOENIX_BLOOD_JADE_COLUMN = registerBlock("phoenix_blood_jade_column",
            new column_init(emissiveJadeVariantSettings("phoenix_blood_jade_column")));

    public static final Block HEAVENLY_DEW_JADE_BLOCK = registerBlock("heavenly_dew_jade_block",
            new Block(jadeVariantSettings("heavenly_dew_jade_block")));
    public static final Block HEAVENLY_DEW_JADE_WALL = registerBlock("heavenly_dew_jade_wall",
            new FluidloggableWallBlock(jadeVariantSettings("heavenly_dew_jade_wall")));
    public static final Block HEAVENLY_DEW_JADE_STAIRS = registerBlock("heavenly_dew_jade_stairs",
            new FluidloggableStairsBlock(HEAVENLY_DEW_JADE_BLOCK.getDefaultState(), jadeVariantSettings("heavenly_dew_jade_stairs")));
    public static final Block HEAVENLY_DEW_JADE_SLAB = registerBlock("heavenly_dew_jade_slab",
            new FluidloggableSlabBlock(jadeVariantSettings("heavenly_dew_jade_slab")));
    public static final Block HEAVENLY_DEW_JADE_VERTICAL_SLAB = registerBlock("heavenly_dew_jade_vertical_slab",
            new VerticalSlabBlock(jadeVariantSettings("heavenly_dew_jade_vertical_slab")));
    public static final Block HEAVENLY_DEW_JADE_COLUMN = registerBlock("heavenly_dew_jade_column",
            new column_init(jadeVariantSettings("heavenly_dew_jade_column")));

    public static final Block GOLDEN_THREAD_JADE_BLOCK = registerBlock("golden_thread_jade_block",
            new Block(jadeSettings("golden_thread_jade_block")));
    public static final Block GOLDEN_THREAD_JADE_WALL = registerBlock("golden_thread_jade_wall",
            new FluidloggableWallBlock(jadeVariantSettings("golden_thread_jade_wall")));
    public static final Block GOLDEN_THREAD_JADE_STAIRS = registerBlock("golden_thread_jade_stairs",
            new FluidloggableStairsBlock(GOLDEN_THREAD_JADE_BLOCK.getDefaultState(), jadeVariantSettings("golden_thread_jade_stairs")));
    public static final Block GOLDEN_THREAD_JADE_SLAB = registerBlock("golden_thread_jade_slab",
            new FluidloggableSlabBlock(jadeVariantSettings("golden_thread_jade_slab")));
    public static final Block GOLDEN_THREAD_JADE_VERTICAL_SLAB = registerBlock("golden_thread_jade_vertical_slab",
            new VerticalSlabBlock(jadeVariantSettings("golden_thread_jade_vertical_slab")));
    public static final Block GOLDEN_THREAD_JADE_COLUMN = registerBlock("golden_thread_jade_column",
            new column_init(jadeVariantSettings("golden_thread_jade_column")));

    public static final Block IMPERIAL_JADE_LANTERN = registerBlock("imperial_jade_lantern",
            new FluidloggableLanternBlock(AbstractBlock.Settings.create()
                    .requiresTool()
                    .mapColor(MapColor.EMERALD_GREEN)
                    .strength(5F, 10.0F)
                    .luminance(state -> 15)
                    .pistonBehavior(PistonBehavior.DESTROY)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "imperial_jade_lantern")))));


    public static Block registerBlock(String name, Block block) { //this is the method to load a new (non-item) block
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
        registerBlockItem("cube_of_flesh_stairs", CUBE_OF_FLESH_STAIRS);
        registerBlockItem("cube_of_flesh_slab", CUBE_OF_FLESH_SLAB);
        registerBlockItem("cube_of_flesh_vertical_slab", CUBE_OF_FLESH_VERTICAL_SLAB);
        registerBlockItem("cube_of_flesh_wall", CUBE_OF_FLESH_WALL);
        registerBlockItem("cube_of_flesh_column", CUBE_OF_FLESH_COLUMN);
        registerBlockItem("cube_of_perceiving", CUBE_OF_PERCEIVING); //*add texture
        registerBlockItem("pulled_teeth", PULLED_TEETH);
        registerBlockItem("skin", SKIN);
        registerBlockItem("filth", FILTH);
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
        registerBlockItem("dragons_eye_ore", DRAGONS_EYE_ORE);
        registerBlockItem("petrified_stone", PETRIFIED_STONE);
        registerBlockItem("cracked_petrified_stone", CRACKED_PETRIFIED_STONE);
        registerBlockItem("embossed_petrified_stone", EMBOSSED_PETRIFIED_STONE);
        registerBlockItem("cracked_embossed_petrified_stone", CRACKED_EMBOSSED_PETRIFIED_STONE);
        registerBlockItem("adorned_petrified_stone", ADORNED_PETRIFIED_STONE);
        registerBlockItem("tiled_petrified_stone", TILED_PETRIFIED_STONE);
        registerBlockItem("cracked_tiled_petrified_stone", CRACKED_TILED_PETRIFIED_STONE);
        registerBlockItem("petrified_stone_stairs", PETRIFIED_STONE_STAIRS);
        registerBlockItem("petrified_stone_wall", PETRIFIED_STONE_WALL);
        registerBlockItem("petrified_stone_column", PETRIFIED_STONE_COLUMN);
        registerBlockItem("petrified_stone_slab", PETRIFIED_STONE_SLAB);
        registerBlockItem("petrified_stone_vertical_slab", PETRIFIED_STONE_VERTICAL_SLAB);
        registerBlockItem("embossed_petrified_stone_stairs", EMBOSSED_PETRIFIED_STONE_STAIRS);
        registerBlockItem("embossed_petrified_stone_wall", EMBOSSED_PETRIFIED_STONE_WALL);
        registerBlockItem("embossed_petrified_stone_column", EMBOSSED_PETRIFIED_STONE_COLUMN);
        registerBlockItem("embossed_petrified_stone_slab", EMBOSSED_PETRIFIED_STONE_SLAB);
        registerBlockItem("embossed_petrified_stone_vertical_slab", EMBOSSED_PETRIFIED_STONE_VERTICAL_SLAB);
        registerBlockItem("tiled_petrified_stone_stairs", TILED_PETRIFIED_STONE_STAIRS);
        registerBlockItem("tiled_petrified_stone_wall", TILED_PETRIFIED_STONE_WALL);
        registerBlockItem("tiled_petrified_stone_column", TILED_PETRIFIED_STONE_COLUMN);
        registerBlockItem("tiled_petrified_stone_slab", TILED_PETRIFIED_STONE_SLAB);
        registerBlockItem("tiled_petrified_stone_vertical_slab", TILED_PETRIFIED_STONE_VERTICAL_SLAB);
        registerBlockItem("adorned_petrified_stone_stairs", ADORNED_PETRIFIED_STONE_STAIRS);
        registerBlockItem("adorned_petrified_stone_wall", ADORNED_PETRIFIED_STONE_WALL);
        registerBlockItem("adorned_petrified_stone_column", ADORNED_PETRIFIED_STONE_COLUMN);
        registerBlockItem("adorned_petrified_stone_slab", ADORNED_PETRIFIED_STONE_SLAB);
        registerBlockItem("adorned_petrified_stone_vertical_slab", ADORNED_PETRIFIED_STONE_VERTICAL_SLAB);
        registerBlockItem("cerulean_shortgrass", CERULEAN_SHORTGRASS);
        registerBlockItem("rod_of_luminescence", ROD_OF_LUMINESCENCE);
        registerBlockItem("can_of_retardation", CAN_OF_RETARDATION);
        registerBlockItem("dragons_leap", DRAGONS_LEAP);
        registerBlockItem("jesters_hat", JESTERS_HAT);
        registerBlockItem("loaf", LOAF);
        registerBlockItem("gravestone", GRAVESTONE);
        registerBlockItem("shadow", SHADOW);
        registerBlockItem("blue_hostas", BLUE_HOSTAS);
        registerBlockItem("salt", SALT);
        registerBlockItem("golden_thurible", GOLDEN_THURIBLE);
        registerBlockItem("gold_wall", GOLD_WALL);
        registerBlockItem("gold_stairs", GOLD_STAIRS);
        registerBlockItem("gold_slab", GOLD_SLAB);
        registerBlockItem("gold_vertical_slab", GOLD_VERTICAL_SLAB);
        registerBlockItem("gold_column", GOLD_COLUMN);
        registerBlockItem("afhd", AFHD);
        registerBlockItem("slate", SLATE);
        registerBlockItem("itore", ITORE);
        registerBlockItem("tankard", TANKARD);
        registerBlockItem("spirits", SPIRITS);
        registerBlockItem("blaj_ore", BLAJ_ORE);
        registerBlockItem("blaj_lesser_ore", BLAJ_LESSER_ORE);
        registerBlockItem("gold_bars", GOLD_BARS);
        registerBlockItem("grotesque", GROTESQUE);
        registerBlockItem("gold_trapdoor", GOLD_TRAPDOOR);
        registerBlockItem("gold_ladder", GOLD_LADDER);
        registerBlockItem("sauerkraut", SAUERKRAUT);
        registerBlockItem("white_coffer", WHITE_COFFER);
        registerBlockItem("orange_coffer", ORANGE_COFFER);
        registerBlockItem("magenta_coffer", MAGENTA_COFFER);
        registerBlockItem("light_blue_coffer", LIGHT_BLUE_COFFER);
        registerBlockItem("yellow_coffer", YELLOW_COFFER);
        registerBlockItem("lime_coffer", LIME_COFFER);
        registerBlockItem("pink_coffer", PINK_COFFER);
        registerBlockItem("grey_coffer", GREY_COFFER);
        registerBlockItem("light_grey_coffer", LIGHT_GREY_COFFER);
        registerBlockItem("cyan_coffer", CYAN_COFFER);
        registerBlockItem("purple_coffer", PURPLE_COFFER);
        registerBlockItem("blue_coffer", BLUE_COFFER);
        registerBlockItem("brown_coffer", BROWN_COFFER);
        registerBlockItem("green_coffer", GREEN_COFFER);
        registerBlockItem("red_coffer", RED_COFFER);
        registerBlockItem("black_coffer", BLACK_COFFER);
        registerBlockItem("simple_coffer", SIMPLE_COFFER);
        registerBlockItem("ginkgo_log", GINKGO_LOG);
        registerBlockItem("ginkgo_leaves", GINKGO_LEAVES);
        registerBlockItem("silver_ore", SILVER_ORE);
        registerBlockItem("silver_vein", SILVER_VEIN);
        registerBlockItem("gold_button", GOLD_BUTTON);
        registerBlockItem("silver_lantern", SILVER_LANTERN);
        registerBlockItem("silver_block", SILVER_BLOCK);
        registerBlockItem("old_soil", OLD_SOIL);
        registerBlockItem("old_meadow", OLD_MEADOW);
        registerBlockItem("silver_thurible", SILVER_THURIBLE);
        registerBlockItem("raw_silver_block", RAW_SILVER_BLOCK);
        registerBlockItem("emerald_wall", EMERALD_WALL);
        registerBlockItem("emerald_column", EMERALD_COLUMN);
        registerBlockItem("emerald_slab", EMERALD_SLAB);
        registerBlockItem("emerald_vertical_slab", EMERALD_VERTICAL_SLAB);
        registerBlockItem("emerald_stairs", EMERALD_STAIRS);
        registerBlockItem("kaolin", KAOLIN);
        registerBlockItem("silver_wall", SILVER_WALL);
        registerBlockItem("silver_column", SILVER_COLUMN);
        registerBlockItem("silver_slab", SILVER_SLAB);
        registerBlockItem("silver_vertical_slab", SILVER_VERTICAL_SLAB);
        registerBlockItem("silver_stairs", SILVER_STAIRS);
        registerBlockItem("ginkgo_planks", GINKGO_PLANKS);
        registerBlockItem("ginkgo_stairs", GINKGO_STAIRS);
        registerBlockItem("ginkgo_slab", GINKGO_SLAB);
        registerBlockItem("ginkgo_vertical_slab", GINKGO_VERTICAL_SLAB);
        registerBlockItem("ginkgo_trapdoor", GINKGO_TRAPDOOR);
        registerBlockItem("ginkgo_button", GINKGO_BUTTON);
        registerBlockItem("stripped_ginkgo_log", STRIPPED_GINKGO_LOG);
        registerBlockItem("vaults_stanchion", VAULTS_STANCHION_SAPLING);
        registerBlockItem("imperial_jade_block", IMPERIAL_JADE_BLOCK);
        registerBlockItem("imperial_jade_wall", IMPERIAL_JADE_WALL);
        registerBlockItem("imperial_jade_stairs", IMPERIAL_JADE_STAIRS);
        registerBlockItem("imperial_jade_slab", IMPERIAL_JADE_SLAB);
        registerBlockItem("imperial_jade_vertical_slab", IMPERIAL_JADE_VERTICAL_SLAB);
        registerBlockItem("imperial_jade_column", IMPERIAL_JADE_COLUMN);
        registerBlockItem("blood_jade_block", BLOOD_JADE_BLOCK);
        registerBlockItem("blood_jade_wall", BLOOD_JADE_WALL);
        registerBlockItem("blood_jade_stairs", BLOOD_JADE_STAIRS);
        registerBlockItem("blood_jade_slab", BLOOD_JADE_SLAB);
        registerBlockItem("blood_jade_vertical_slab", BLOOD_JADE_VERTICAL_SLAB);
        registerBlockItem("blood_jade_column", BLOOD_JADE_COLUMN);
        registerBlockItem("uncut_jadeite_stone", UNCUT_JADEITE_STONE);
        registerBlockItem("jadeite_block", JADEITE_BLOCK);
        registerBlockItem("jadeite_wall", JADEITE_WALL);
        registerBlockItem("jadeite_stairs", JADEITE_STAIRS);
        registerBlockItem("jadeite_slab", JADEITE_SLAB);
        registerBlockItem("jadeite_vertical_slab", JADEITE_VERTICAL_SLAB);
        registerBlockItem("jadeite_column", JADEITE_COLUMN);
        registerBlockItem("mutton_fat_jade_block", MUTTON_FAT_JADE_BLOCK);
        registerBlockItem("mutton_fat_jade_wall", MUTTON_FAT_JADE_WALL);
        registerBlockItem("mutton_fat_jade_stairs", MUTTON_FAT_JADE_STAIRS);
        registerBlockItem("mutton_fat_jade_slab", MUTTON_FAT_JADE_SLAB);
        registerBlockItem("mutton_fat_jade_vertical_slab", MUTTON_FAT_JADE_VERTICAL_SLAB);
        registerBlockItem("mutton_fat_jade_column", MUTTON_FAT_JADE_COLUMN);
        registerBlockItem("lavender_jade_block", LAVENDER_JADE_BLOCK);
        registerBlockItem("lavender_jade_wall", LAVENDER_JADE_WALL);
        registerBlockItem("lavender_jade_stairs", LAVENDER_JADE_STAIRS);
        registerBlockItem("lavender_jade_slab", LAVENDER_JADE_SLAB);
        registerBlockItem("lavender_jade_vertical_slab", LAVENDER_JADE_VERTICAL_SLAB);
        registerBlockItem("lavender_jade_column", LAVENDER_JADE_COLUMN);
        registerBlockItem("lotus_jade_block", LOTUS_JADE_BLOCK);
        registerBlockItem("lotus_jade_wall", LOTUS_JADE_WALL);
        registerBlockItem("lotus_jade_stairs", LOTUS_JADE_STAIRS);
        registerBlockItem("lotus_jade_slab", LOTUS_JADE_SLAB);
        registerBlockItem("lotus_jade_vertical_slab", LOTUS_JADE_VERTICAL_SLAB);
        registerBlockItem("lotus_jade_column", LOTUS_JADE_COLUMN);
        registerBlockItem("phoenix_blood_jade_block", PHOENIX_BLOOD_JADE_BLOCK);
        registerBlockItem("phoenix_blood_jade_wall", PHOENIX_BLOOD_JADE_WALL);
        registerBlockItem("phoenix_blood_jade_stairs", PHOENIX_BLOOD_JADE_STAIRS);
        registerBlockItem("phoenix_blood_jade_slab", PHOENIX_BLOOD_JADE_SLAB);
        registerBlockItem("phoenix_blood_jade_vertical_slab", PHOENIX_BLOOD_JADE_VERTICAL_SLAB);
        registerBlockItem("phoenix_blood_jade_column", PHOENIX_BLOOD_JADE_COLUMN);
        registerBlockItem("heavenly_dew_jade_block", HEAVENLY_DEW_JADE_BLOCK);
        registerBlockItem("heavenly_dew_jade_wall", HEAVENLY_DEW_JADE_WALL);
        registerBlockItem("heavenly_dew_jade_stairs", HEAVENLY_DEW_JADE_STAIRS);
        registerBlockItem("heavenly_dew_jade_slab", HEAVENLY_DEW_JADE_SLAB);
        registerBlockItem("heavenly_dew_jade_vertical_slab", HEAVENLY_DEW_JADE_VERTICAL_SLAB);
        registerBlockItem("heavenly_dew_jade_column", HEAVENLY_DEW_JADE_COLUMN);
        registerBlockItem("golden_thread_jade_block", GOLDEN_THREAD_JADE_BLOCK);
        registerBlockItem("golden_thread_jade_wall", GOLDEN_THREAD_JADE_WALL);
        registerBlockItem("golden_thread_jade_stairs", GOLDEN_THREAD_JADE_STAIRS);
        registerBlockItem("golden_thread_jade_slab", GOLDEN_THREAD_JADE_SLAB);
        registerBlockItem("golden_thread_jade_vertical_slab", GOLDEN_THREAD_JADE_VERTICAL_SLAB);
        registerBlockItem("golden_thread_jade_column", GOLDEN_THREAD_JADE_COLUMN);
        registerBlockItem("imperial_jade_lantern", IMPERIAL_JADE_LANTERN);
        registerBlockItem("veins", VEINS);
        registerBlockItem("chanterelle", CHANTERELLE);
        registerBlockItem("redstone_lantern", REDSTONE_LANTERN);
        registerBlockItem("redstone_chain", REDSTONE_CHAIN);
        registerBlockItem("pile_of_meat", PILE_OF_MEAT);
        registerBlockItem("sunkissed_shortgrass", SUNKISSED_SHORTGRASS);
    } //to load into game

}
