package mia.the_tower.initialisation.block;

import net.minecraft.block.*;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import static mia.the_tower.initialisation.block_init.registerBlock;
import static mia.the_tower.initialisation.block_init.registerBlockItem;

public class column_init extends PillarBlock{

    public column_init(Settings settings) {
        super(settings);
    }

    public static Block STONE_COLUMN = registerBlock("stone_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "stone_column")))));

    public static Block COBBLESTONE_COLUMN = registerBlock("cobblestone_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cobblestone_column")))));

    public static Block SMOOTH_STONE_COLUMN = registerBlock("smooth_stone_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "smooth_stone_column")))));

    public static Block STONE_BRICK_COLUMN = registerBlock("stone_brick_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "stone_brick_column")))));

    public static Block BLACKSTONE_COLUMN = registerBlock("blackstone_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "blackstone_column")))));

    public static Block POLISHED_BLACKSTONE_COLUMN = registerBlock("polished_blackstone_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "polished_blackstone_column")))));

    public static Block POLISHED_BLACKSTONE_BRICK_COLUMN = registerBlock("polished_blackstone_brick_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "polished_blackstone_brick_column")))));

    public static Block COBBLED_DEEPSLATE_COLUMN = registerBlock("cobbled_deepslate_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "cobbled_deepslate_column")))));

    public static Block POLISHED_DEEPSLATE_COLUMN = registerBlock("polished_deepslate_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "polished_deepslate_column")))));

    public static Block DEEPSLATE_BRICK_COLUMN = registerBlock("deepslate_brick_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "deepslate_brick_column")))));

    public static Block DEEPSLATE_TILE_COLUMN = registerBlock("deepslate_tile_column",
            new column_init(AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.STONE)
                    .strength(1.5F, 8.0F)
                    .requiresTool()
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "deepslate_tile_column")))));


    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        Direction.Axis axis = state.get(AXIS);
        switch (axis) {
            case X:
                return VoxelShapes.cuboid(0.0F, 0.25F, 0.25F, 1.0F, 0.75F, 0.75F);
            case Y:
                return VoxelShapes.cuboid(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
            case Z:
            default:
                return VoxelShapes.cuboid(0.25F, 0.25F, 0.0F, 0.75F, 0.75F, 1.0F);
        }

    }

    public static void load() {
        registerBlockItem("stone_column",STONE_COLUMN);
        registerBlockItem("cobblestone_column",COBBLESTONE_COLUMN);
        registerBlockItem("smooth_stone_column",SMOOTH_STONE_COLUMN);
        registerBlockItem("stone_brick_column",STONE_BRICK_COLUMN);
        registerBlockItem("blackstone_column",BLACKSTONE_COLUMN);
        registerBlockItem("polished_blackstone_column",POLISHED_BLACKSTONE_COLUMN);
        registerBlockItem("polished_blackstone_brick_column",POLISHED_BLACKSTONE_BRICK_COLUMN);
        registerBlockItem("cobbled_deepslate_column",COBBLED_DEEPSLATE_COLUMN);
        registerBlockItem("polished_deepslate_column",POLISHED_DEEPSLATE_COLUMN);
        registerBlockItem("deepslate_brick_column",DEEPSLATE_BRICK_COLUMN);
        registerBlockItem("deepslate_tile_column",DEEPSLATE_TILE_COLUMN);
    } //to load into game

}
