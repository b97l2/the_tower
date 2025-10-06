package mia.the_tower.initialisation.blockentity;

import com.mojang.serialization.MapCodec;
import mia.the_tower.initialisation.blockentity.util.ColouredInventory;
import mia.the_tower.initialisation.items.KeyItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;

public class ColouredCofferBlock extends BlockWithEntity {

    private final MapCodec<ColouredCofferBlock> codec;
    public static final EnumProperty<Direction> FACING = Properties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = Properties.OPEN;

    public ColouredCofferBlock(DyeColor color, Settings settings) {
        super(settings);
        this.color = color;
        this.codec = Block.createCodec(s -> new ColouredCofferBlock(color, s));
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(OPEN, false));
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return this.codec;
    }

    private final DyeColor color;

    public DyeColor getColor() {
        return color;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ColouredCofferBlockEntity(pos, state);
    }


    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onPlaced(world, pos, state, placer, stack);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        // If placing on a vertical face, face the player; otherwise use the clicked horizontal face.
        Direction dir = ctx.getSide().getAxis().isHorizontal()
                ? ctx.getSide()
                : ctx.getHorizontalPlayerFacing().getOpposite();
        return this.getDefaultState().with(FACING, dir);
    }

    @Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (!world.isClient && be instanceof ColouredCofferBlockEntity cofferBe) {
                ServerWorld sw = (ServerWorld) world;
                List<ItemStack> overflow = ColouredInventory.get(sw, color).onContainerRemoved();
                for (ItemStack s : overflow) {
                    if (!s.isEmpty()) ItemScatterer.spawn(world, pos.getX(), pos.getY(), pos.getZ(), s);
                }
                world.updateComparators(pos, this);
            }
            super.onStateReplaced(state, world, pos, newState, moved);
        } else {
            super.onStateReplaced(state, world, pos, newState, moved);
        }
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        if (world.isClient) return null;
        return tickerIf(type, blockentity_init.COLORED_COFFER);
    }

    /** Local replacement for BlockWithEntity.checkType */
    @SuppressWarnings("unchecked")
    private static <T extends BlockEntity> BlockEntityTicker<T> tickerIf(
            BlockEntityType<T> given,
            BlockEntityType<? extends ColouredCofferBlockEntity> expected
    ) {
        if (given == expected) {
            // Cast the serverTick method reference to the expected generic type.
            return (BlockEntityTicker<T>) (BlockEntityTicker<ColouredCofferBlockEntity>) ColouredCofferBlockEntity::serverTick;
        }
        return null;
    }

//for the screen handler below

    @Override
    public net.minecraft.util.ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        if (player.getMainHandStack().getItem() instanceof KeyItem
                || player.getOffHandStack().getItem() instanceof KeyItem) {
            return ActionResult.PASS; // let the item handle the interaction
        }

        if (world.isClient) return net.minecraft.util.ActionResult.SUCCESS;
        BlockEntity be = world.getBlockEntity(pos);
        if (be instanceof NamedScreenHandlerFactory factory) {
            player.openHandledScreen(factory); // server → client sync
            return net.minecraft.util.ActionResult.CONSUME;
        }
        return net.minecraft.util.ActionResult.PASS;
    }

}