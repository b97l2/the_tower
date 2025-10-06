package mia.the_tower.initialisation.items;

import mia.the_tower.initialisation.block_init;
import mia.the_tower.initialisation.item_init;
import mia.the_tower.initialisation.items.util.CofferToKey;
import mia.the_tower.initialisation.sounds.CustomSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Set;

public class KeyItem extends Item {
    private final DyeColor color;

    public KeyItem(DyeColor color, Item.Settings settings) { //you need to specify what colour the key is, like a dye item
        super(settings);
        this.color = color;
    }

    private static final Set<Block> COFFER_SET =
            Set.of(
                    block_init.SIMPLE_COFFER,//set of coffers
                    block_init.WHITE_COFFER,
                    block_init.ORANGE_COFFER,
                    block_init.MAGENTA_COFFER,
                    block_init.LIGHT_BLUE_COFFER,
                    block_init.YELLOW_COFFER,
                    block_init.LIME_COFFER,
                    block_init.PINK_COFFER,
                    block_init.GREY_COFFER,
                    block_init.LIGHT_GREY_COFFER,
                    block_init.CYAN_COFFER,
                    block_init.PURPLE_COFFER,
                    block_init.BLUE_COFFER,
                    block_init.BROWN_COFFER,
                    block_init.GREEN_COFFER,  //coloured_key -> coloured_coffer
                    block_init.RED_COFFER,
                    block_init.BLACK_COFFER
            );

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        if (world.isClient) return ActionResult.SUCCESS;
        BlockPos position = context.getBlockPos();
        BlockState clickedBlockState = world.getBlockState(position);
        Block clickedBlock = clickedBlockState.getBlock();
        PlayerEntity player = context.getPlayer();

        Block newCoffer = switch (color) { //choses the new coffer colour based on the colour of the key
                    case WHITE -> block_init.WHITE_COFFER;
                    case ORANGE -> block_init.ORANGE_COFFER;
                    case MAGENTA -> block_init.MAGENTA_COFFER;
                    case LIGHT_BLUE -> block_init.LIGHT_BLUE_COFFER;
                    case YELLOW -> block_init.YELLOW_COFFER;
                    case LIME -> block_init.LIME_COFFER;
                    case PINK -> block_init.PINK_COFFER;
                    case GRAY -> block_init.GREY_COFFER;
                    case LIGHT_GRAY -> block_init.LIGHT_GREY_COFFER;
                    case CYAN -> block_init.CYAN_COFFER;
                    case PURPLE -> block_init.PURPLE_COFFER;
                    case BLUE -> block_init.BLUE_COFFER;
                    case BROWN -> block_init.BROWN_COFFER;
                    case GREEN -> block_init.GREEN_COFFER;  //coloured_key -> coloured_coffer
                    case RED -> block_init.RED_COFFER;
                    case BLACK -> block_init.BLACK_COFFER;
            default   -> null;
        };

        if(COFFER_SET.contains(clickedBlock)) { //checks whether the block clicked on is a coffer
            if(!world.isClient()) {

                BlockState newState = copyFacing(clickedBlockState, newCoffer.getDefaultState()); //copy facing direction of old coffer

                world.setBlockState(position, newState, Block.NOTIFY_ALL);
                world.playSound(null, context.getBlockPos(), SoundEvents.BLOCK_GRINDSTONE_USE, SoundCategory.BLOCKS);

                if (player == null || !player.isCreative()) {
                    context.getStack().decrement(1); // <- this is the held stack (correct hand)
                }

                ItemConvertible drop = CofferToKey.keyForCoffer(clickedBlock);
                ItemStack stack = new ItemStack(drop, 1);
                ItemScatterer.spawn(world, position.getX() + 0.5, position.getY() + 0.5, position.getZ() + 0.5, stack);
                    // Alternatively: Block.dropStack(world, pos, stack);

            }
        }

        return ActionResult.CONSUME;
    }


    private static BlockState copyFacing(BlockState from, BlockState to) {
        // Full 6-direction facing
        if (from.contains(Properties.HORIZONTAL_FACING) && to.contains(Properties.HORIZONTAL_FACING)) {
            return to.with(Properties.HORIZONTAL_FACING, from.get(Properties.HORIZONTAL_FACING));
        }
        // If the target doesn’t support a compatible facing property, leave defaults
        return to;
    }

}


