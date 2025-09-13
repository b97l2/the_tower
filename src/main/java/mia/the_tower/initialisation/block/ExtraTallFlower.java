package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;
import mia.the_tower.initialisation.TripleBlockPart;

public class ExtraTallFlower extends PlantBlock {
    public static final EnumProperty<TripleBlockPart> THIRD = EnumProperty.of("third", TripleBlockPart.class);

    public static final MapCodec<ExtraTallFlower> CODEC = createCodec(ExtraTallFlower::new);

    @Override
    public MapCodec<? extends ExtraTallFlower> getCodec() {
        return CODEC;
    }

    public ExtraTallFlower(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(THIRD, TripleBlockPart.LOWER));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(THIRD);
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();

        if (pos.getY() + 2 > world.getTopYInclusive()) return null;

        if (world.getBlockState(pos.up()).canReplace(ctx) &&
                world.getBlockState(pos.up(2)).canReplace(ctx)) {
            return super.getPlacementState(ctx).with(THIRD, TripleBlockPart.LOWER);
        }

        return null;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        world.setBlockState(pos.up(), this.getDefaultState().with(THIRD, TripleBlockPart.MIDDLE), Block.NOTIFY_ALL);
        world.setBlockState(pos.up(2), this.getDefaultState().with(THIRD, TripleBlockPart.UPPER), Block.NOTIFY_ALL);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TripleBlockPart third = state.get(THIRD);
        if (!world.isClient) {
            if (third == TripleBlockPart.UPPER) {
                BlockPos middlePos = pos.down();
                BlockPos lowerPos = pos.down(2);
                world.breakBlock(middlePos, !player.isCreative());
                world.breakBlock(lowerPos, !player.isCreative());
            } else if (third == TripleBlockPart.MIDDLE) {
                BlockPos lowerPos = pos.down();
                BlockPos upperPos = pos.up();
                world.breakBlock(lowerPos, !player.isCreative());
                world.breakBlock(upperPos, !player.isCreative());
            } else {
                BlockPos middlePos = pos.up();
                BlockPos upperPos = pos.up(2);
                world.breakBlock(middlePos, !player.isCreative());
                world.breakBlock(upperPos, !player.isCreative());
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        TripleBlockPart third = state.get(THIRD);
        switch (third) {
            case LOWER:
                return super.canPlaceAt(state, world, pos);
            case MIDDLE:
                return world.getBlockState(pos.down()).isOf(this) &&
                        world.getBlockState(pos.down()).get(THIRD) == TripleBlockPart.LOWER;
            case UPPER:
                return world.getBlockState(pos.down()).isOf(this) &&
                        world.getBlockState(pos.down()).get(THIRD) == TripleBlockPart.MIDDLE;
            default:
                return false;
        }
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView,
                                                   BlockPos pos, Direction dir, BlockPos neighborPos,
                                                   BlockState neighborState, Random random) {
        TripleBlockPart third = state.get(THIRD);
        boolean valid = switch (third) {
            case LOWER -> world.getBlockState(pos.up()).isOf(this) &&
                    world.getBlockState(pos.up()).get(THIRD) == TripleBlockPart.MIDDLE;
            case MIDDLE -> world.getBlockState(pos.down()).isOf(this) &&
                    world.getBlockState(pos.down()).get(THIRD) == TripleBlockPart.LOWER &&
                    world.getBlockState(pos.up()).isOf(this) &&
                    world.getBlockState(pos.up()).get(THIRD) == TripleBlockPart.UPPER;
            case UPPER -> world.getBlockState(pos.down()).isOf(this) &&
                    world.getBlockState(pos.down()).get(THIRD) == TripleBlockPart.MIDDLE;
        };

        return valid ? state : Blocks.AIR.getDefaultState();
    }
}