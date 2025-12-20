package mia.the_tower.initialisation.world.misc;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mia.the_tower.initialisation.world.tree.GiantFoliagePlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.PillarBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.foliage.FoliagePlacer.TreeNode;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static mia.the_tower.The_Tower.id;


public class DecoratedPitPlacer extends Feature<DecoratedPitPlacer.Config> {

    public static record Config(
            IntProvider radius,              // radius at rim
            IntProvider depth,               // max depth at center
            BlockStateProvider surfaceProvider  // weighted list of 3 blocks, etc.
    ) implements FeatureConfig {

        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        IntProvider.NON_NEGATIVE_CODEC
                                .fieldOf("radius")
                                .forGetter(Config::radius),
                        IntProvider.POSITIVE_CODEC
                                .fieldOf("depth")
                                .forGetter(Config::depth),
                        BlockStateProvider.TYPE_CODEC
                                .fieldOf("surface_provider")
                                .forGetter(Config::surfaceProvider)
                ).apply(instance, Config::new)
        );
    }

    public DecoratedPitPlacer() {
        super(Config.CODEC);
    }

    public static final Feature<DecoratedPitPlacer.Config> DECORATED_PIT =
            Registry.register(Registries.FEATURE, id("decorated_pit"), new DecoratedPitPlacer());

    public static void init() {}

    @Override
    public boolean generate(FeatureContext<Config> context) {
        Config config = context.getConfig();
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos origin = context.getOrigin();


        int radius = config.radius().get(random);
        int depth = config.depth().get(random);

        if (radius <= 0 || depth <= 0) {
            return false;
        }

        int bottomY = world.getBottomY();
        int topY = world.getTopYInclusive();
        int originY = origin.getY();

        if (originY <= bottomY || originY >= topY) {
            return false;
        }

        boolean changed = false;

        int originX = origin.getX();
        int originZ = origin.getZ();
        double radiusSq = (double) radius * (double) radius;

        BlockPos.Mutable mutable = new BlockPos.Mutable();

        // -------------------------------------------------
        // 1. Carve a bowl-shaped paraboloid of air
        //    z = depth * (1 - (r^2 / R^2)), r^2 = dx^2 + dz^2
        // -------------------------------------------------
        for (int dx = -radius; dx <= radius; dx++) {
            int x = originX + dx;

            for (int dz = -radius; dz <= radius; dz++) {
                int z = originZ + dz;

                double distSq = (double) dx * dx + (double) dz * dz;
                if (distSq > radiusSq) {
                    continue;
                }

                // 0 at center, 1 at rim
                double normalizedRadial = distSq / radiusSq;
                double columnDepthDouble = (double) depth * (1.0 - normalizedRadial);
                int columnDepth = MathHelper.floor(columnDepthDouble);

                if (columnDepth <= 0) {
                    continue;
                }

                // clamp so we don't go below world bottom
                int maxSteps = Math.min(columnDepth, originY - bottomY - 1);
                if (maxSteps <= 0) {
                    continue;
                }

                for (int dy = 0; dy < maxSteps; dy++) {
                    int y = originY - dy;
                    mutable.set(x, y, z);

                    BlockState state = world.getBlockState(mutable);

                    // don't replace obsidian
                    if (state.isOf(Blocks.OBSIDIAN)) {
                        continue;
                    }

                    if (!state.isAir()) {
                        changed = true;
                    }

                    world.setBlockState(mutable, Blocks.AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
                }
            }
        }

        // -------------------------------------------------
        // 2. Decorate inside floor surface
        //    "Inside surface" = blocks with air directly above them,
        //    within the paraboloid footprint.
        // -------------------------------------------------
        BlockStateProvider surfaceProvider = config.surfaceProvider();
        int minY = Math.max(bottomY + 1, originY - depth - 1);

        for (int dx = -radius; dx <= radius; dx++) {
            int x = originX + dx;

            for (int dz = -radius; dz <= radius; dz++) {
                int z = originZ + dz;

                double distSq = (double) dx * dx + (double) dz * dz;
                if (distSq > radiusSq) {
                    continue;
                }

                // Only columns that actually had some depth carved
                double normalizedRadial = distSq / radiusSq;
                double columnDepthDouble = (double) depth * (1.0 - normalizedRadial);
                int columnDepth = MathHelper.floor(columnDepthDouble);
                if (columnDepth <= 0) {
                    continue;
                }

                int y = originY;
                mutable.set(x, y, z);

                // walk down through the air we carved until we hit solid
                while (y > minY && world.getBlockState(mutable).isAir()) {
                    y--;
                    mutable.setY(y);
                }

                BlockState floorState = world.getBlockState(mutable);
                if (y <= minY) {
                    continue;
                }

                // skip if we didn't hit solid, or it's obsidian,
                // or it no longer has air above (not an interior floor).
                if (floorState.isAir() || floorState.isOf(Blocks.OBSIDIAN)) {
                    continue;
                }

                if (!world.getBlockState(mutable.up()).isAir()) {
                    continue;
                }

                BlockState decoration = surfaceProvider.get(random, mutable);
                world.setBlockState(mutable, decoration, Block.NOTIFY_LISTENERS);
                changed = true;
            }
        }

        return changed;
    }

}
