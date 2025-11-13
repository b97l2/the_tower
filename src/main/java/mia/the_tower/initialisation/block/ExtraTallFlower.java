package mia.the_tower.initialisation.block;

import com.mojang.serialization.MapCodec;
import mia.the_tower.initialisation.entity.ModEntities;
import mia.the_tower.initialisation.entity.custom.LampFlyEntity;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import org.jetbrains.annotations.Nullable;

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
        if (!world.isClient) {
            if (player.isCreative()) {
                // Prevent drops and cleanly remove the whole stack like vanilla
                onBreakInCreative(world, pos, state, player);
            } else {
                // Your existing survival behavior (break the other parts with drops)
                TripleBlockPart third = state.get(THIRD);
                if (third == TripleBlockPart.UPPER) {
                    world.breakBlock(pos.down(), true);
                    world.breakBlock(pos.down(2), true);
                } else if (third == TripleBlockPart.MIDDLE) {
                    world.breakBlock(pos.down(), true);
                    world.breakBlock(pos.up(),   true);
                } else {
                    world.breakBlock(pos.up(),   true);
                    world.breakBlock(pos.up(2),  true);
                }
            }
        }
        return super.onBreak(world, pos, state, player);
    }


    private static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        TripleBlockPart part = state.get(THIRD);

        // Resolve the full stack origin (lower/middle/upper -> lower)
        BlockPos lower = switch (part) {
            case LOWER -> pos;
            case MIDDLE -> pos.down();
            case UPPER -> pos.down(2);
        };
        BlockPos middle = lower.up();
        BlockPos upper  = lower.up(2);

        // Clear each section without drops and with a break animation
        clearNoDrop(world, lower,  player);
        clearNoDrop(world, middle, player);
        clearNoDrop(world, upper,  player);
    }

    private static void clearNoDrop(World world, BlockPos p, PlayerEntity player) {
        BlockState s = world.getBlockState(p);
        if (s.getBlock() instanceof ExtraTallFlower) {
            world.setBlockState(p, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
            world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, p, Block.getRawIdFromState(s));
        }
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

    @Override //this is for when the block is placed via features, it places the top two parts itself
    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);
        if (world.isClient) return;

        // Only bootstrap from the LOWER part
        if (!state.isOf(this) || state.get(THIRD) != TripleBlockPart.LOWER) return;

        // Y-bounds & simple replaceability checks (air is enough for vegetation patches)
        if (pos.getY() + 2 > world.getTopYInclusive()) return;
        BlockPos mid = pos.up();
        BlockPos top = pos.up(2);
        if (!world.getBlockState(mid).isAir() || !world.getBlockState(top).isAir()) return;

        world.setBlockState(mid, this.getDefaultState().with(THIRD, TripleBlockPart.MIDDLE), Block.NOTIFY_ALL);
        world.setBlockState(top, this.getDefaultState().with(THIRD, TripleBlockPart.UPPER),  Block.NOTIFY_ALL);
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

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        // 0) (ServerWorld is already server-side; no extra check needed)

        // 1) Time-slice gate: each plant tries at most once per ~12.8s (256 ticks) on average
        //    but staggered by position so they don't all align.
        if ( ((world.getTime() + pos.asLong()) & 0xFFL) != 0L ) return;

        // 2) Extra probabilistic gate: only 1 in 4 time-slices proceeds (~ every ~51s per plant average)
        if (random.nextInt(4) != 0) return;

        // 3) Only if a player is around (64 blocks)
        if (world.getClosestPlayer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 64.0, false) == null)
            return;

        // 4) Local density cap: if enough LAMP_FLY are already near, skip
        //    Keep the box modest (<=32) to avoid scanning lots of entities.
        final double radius = 32.0;
        int nearby = world.getEntitiesByType(ModEntities.LAMP_FLY,
                new Box(pos).expand(radius),
                e -> true).size();
        if (nearby >= 6) return; // tune this cap

        // 5) Try a few candidate spots slightly above/around the plant
        for (int tries = 0; tries < 3; tries++) {
            int dx = random.nextBetween(-3, 3);
            int dz = random.nextBetween(-3, 3);
            int dy = 1 + random.nextBetween(0, 3);

            BlockPos p = pos.add(dx, dy, dz);

            // Make sure we’re in air with headroom
            if (!world.isAir(p) || !world.isAir(p.up())) continue;

            // Optional: keep it above the local ground a bit
            int groundY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, p.getX(), p.getZ());
            if (p.getY() < groundY + 2) continue;

            // 6) Create and place the entity
            LampFlyEntity fly = ModEntities.LAMP_FLY.create(world, SpawnReason.NATURAL);
            if (fly == null) return;

            fly.refreshPositionAndAngles(p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5, random.nextFloat() * 360f, 0f);
            // Small upward nudge so FlightMoveControl takes over smoothly (if you use it)
            fly.setVelocity(fly.getVelocity().add(0.0, 0.08, 0.0));

            // Don’t spawn if colliding (leaves/caves etc.)
            if (!world.isSpaceEmpty(fly)) continue;

            world.spawnEntity(fly);
            break; // only one per tick per plant
        }
    }

}