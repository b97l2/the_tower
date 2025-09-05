package mia.the_tower.initialisation;

import mia.the_tower.The_Tower;
import net.minecraft.block.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class crop_init {
    // This is where we create new crop blocks
    public static final Block GLUH_VINE = registerBlock("gluh_vine",
                new crop_init.CustomCropBlock(AbstractBlock.Settings.copy(Blocks.WHEAT)
                        .nonOpaque()
                        .noCollision()
                        .ticksRandomly()
                        .breakInstantly()
                        .sounds(BlockSoundGroup.CROP)
                        .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "gluh_vine")))
                )
        );

    // Define your custom crop block class
    public static class CustomCropBlock extends CropBlock {
        private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
                Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
                Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 3.0D, 16.0D),
                Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
                Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 5.0D, 16.0D),
                Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
                Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 7.0D, 16.0D),
                Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
                Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 9.0D, 16.0D)
        };

        public CustomCropBlock(AbstractBlock.Settings settings) {
            super(settings);
        }

        @Override
        protected ItemConvertible getSeedsItem() {
            return item_init.GLUH;
        }

        @Override
        protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
            return AGE_TO_SHAPE[this.getAge(state)];
        }

    }

    public static Block registerBlock(String name, Block block) { //this is the method to register a new (non item) block
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
    block_init.registerBlockItem("gluh_vine",GLUH_VINE);
    } //to load into game


}
