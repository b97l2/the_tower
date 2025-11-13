package mia.the_tower.initialisation.world.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

/**
 * Organic, drooping foliage for very large trees.
 * Shape:
 *  - Rounded canopy whose radius shrinks with depth (vertical taper) + per-layer fuzz.
 *  - From lower layers' perimeter, spawn hanging strands that can drift sideways.
 * Config fields (see CODEC below):
 *  - radius, offset                (standard fields inherited from FoliagePlacer)
 *  - max_droop                     maximum length of a drooping strand
 *  - strand_chance                 chance to start a strand at a perimeter leaf (0..1)
 *  - strand_continue_chance        chance to extend a strand by another block (0..1)
 *  - drift_chance                  chance that a strand step drifts one block horizontally (0..1)
 *  - vertical_taper                how fast the canopy radius shrinks per layer downward (blocks/layer)
 *  - layer_fuzz                    +/- random radius wobble per layer (integer)
 *  - y_ellipticity                 weights vertical distance in the roundness test (higher = flatter dome)
 */
public class GiantFoliagePlacer extends FoliagePlacer {

    // --- Custom knobs ---
    private final IntProvider maxDroop;
    private final float topRoundness;
    private final float strandContinueChance;
    private final float driftChance;
    private final float verticalTaper;
    private final int layerFuzz;
    private static final float DEFAULT_Y_ELLIPTICITY = 0.6f;

    public static final MapCodec<GiantFoliagePlacer> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    fillFoliagePlacerFields(instance)
                            .and(IntProvider.createValidatingCodec(0, 128)
                                    .fieldOf("max_droop")
                                    .forGetter(p -> p.maxDroop))
                            .and(Codec.floatRange(0.0F, 1.0F)
                                    .fieldOf("strand_chance").orElse(0.35F)
                                    .forGetter(p -> p.topRoundness))
                            .and(Codec.floatRange(0.0F, 1.0F)
                                    .fieldOf("strand_continue_chance").orElse(0.85F)
                                    .forGetter(p -> p.strandContinueChance))
                            .and(Codec.floatRange(0.0F, 1.0F)
                                    .fieldOf("drift_chance").orElse(0.25F)
                                    .forGetter(p -> p.driftChance))
                            .and(Codec.floatRange(0.0F, 3.0F)
                                    .fieldOf("vertical_taper").orElse(0.6F)
                                    .forGetter(p -> p.verticalTaper))
                            .and(Codec.intRange(0, 8)
                                    .fieldOf("layer_fuzz").orElse(1)
                                    .forGetter(p -> p.layerFuzz))
                            .apply(instance, GiantFoliagePlacer::new)
            );


    public GiantFoliagePlacer(
            IntProvider radius,                // vanilla: base radius seed
            IntProvider offset,                // vanilla: vertical offset seed
            IntProvider maxDroop,
            float topRoundness,
            float strandContinueChance,
            float driftChance,
            float verticalTaper,
            int layerFuzz
    ) {
        super(radius, offset);
        this.maxDroop = maxDroop;
        this.topRoundness = topRoundness;
        this.strandContinueChance = strandContinueChance;
        this.driftChance = driftChance;
        this.verticalTaper = verticalTaper;
        this.layerFuzz = layerFuzz;
    }

    public static final ThreadLocal<ChunkPos> CURRENT_TREE_ORIGIN = new ThreadLocal<>(); //for chunk window

    @Override
    protected FoliagePlacerType<?> getType() {
        return ORGANIC_DROOP; // defined below
    }

    public static final FoliagePlacerType<GiantFoliagePlacer> ORGANIC_DROOP =
            Registry.register(
                    Registries.FOLIAGE_PLACER_TYPE,
                    Identifier.of("the_tower", "organic_droop"),
                    new FoliagePlacerType<>(GiantFoliagePlacer.CODEC)
            );

    public static void init(){}

    /**
     * Choose a foliage height given trunk height. For giant trees you usually want a deep canopy.
     * Adjust if you prefer; this just ensures we get enough vertical space to show the droop.
     */
    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        // ~ upper third of trunk, clamped:
        int h = Math.max(8, trunkHeight / 3);
        return Math.min(h, 48); // safety clamp for huge specimens
    }

    @Override
    protected void generate(
            TestableWorld world,
            BlockPlacer placer,
            Random random,
            TreeFeatureConfig config,
            int trunkHeight,
            FoliagePlacer.TreeNode node,
            int foliageHeight,
            int baseRadius,
            int offset
    ) {
        final BlockPos center = node.getCenter();
        final boolean giantTrunk = node.isGiantTrunk();

        // How many layers ABOVE the top to include inside the main blob
        // Scales with topRoundness and baseRadius, but clamped for perf.
        final int capMax = Math.min(16, Math.max(1, (int)Math.floor(baseRadius * 0.5)));
        final int capLayers = (topRoundness <= 0f) ? 0 : Math.max(1, (int)Math.round(topRoundness * capMax));

        // Cache a droop length for this tree (or keep per-layer if you like)
        final int maxDroopLen = this.maxDroop.get(random);

        //this is to place only in feature chunk
        final ChunkPos originChunk =
                java.util.Optional.ofNullable(CURRENT_TREE_ORIGIN.get())
                        .orElseGet(() -> new ChunkPos(center));
        final FoliagePlacer.BlockPlacer gatedPlacer = new FoliagePlacer.BlockPlacer() {
            @Override
            public void placeBlock(BlockPos pos, BlockState state) {
                if (withinFeatureRegion(originChunk, pos)) {
                    placer.placeBlock(pos, state);
                }
            }
            @Override
            public boolean hasPlacedBlock(BlockPos pos) {
                return placer.hasPlacedBlock(pos); // preserve leaf-layer checks
            }
        };


        // IMPORTANT: one vertical shift path (yOff), no pre-shifted BlockPos
        for (int dy = -capLayers; dy < foliageHeight; dy++) {
            final int yOff = offset - dy;

            int layerRadius;
            if (dy < 0) {
                // --- Rounded top (spherical cap) ---
                // normalized height above top: 1..capLayers
                final int h = -dy;
                final double t = (double) h / (double) (capLayers + 1); // 0..~1
                // r = R * sqrt(1 - t^2)
                layerRadius = (int)Math.round(baseRadius * Math.sqrt(Math.max(0.0, 1.0 - t * t)));

                // smaller fuzz on the cap to keep it smooth
                if (layerFuzz > 0) {
                    int fuzz = Math.max(1, layerFuzz / 2);
                    layerRadius += random.nextBetween(-fuzz, fuzz);
                }
            } else {
                // --- Main blob below the top ---
                layerRadius = Math.max(1, (int)Math.round(baseRadius - verticalTaper * dy));
                if (layerFuzz > 0) {
                    layerRadius += random.nextBetween(-layerFuzz, layerFuzz);
                }
            }

            if (layerRadius < 1) continue;  // nothing to place on this layer

            // Place the layer (single application of vertical offset).
            generateSquare(world, gatedPlacer, random, config, center, layerRadius, yOff, giantTrunk);

            // Start a strand from EVERY leaf cell in this layer, threading through leaves
            // (as you already implemented in spawnDroopingStrandsFromAllLeafCellsInLayer).
            BlockPos layerCenter = center.up(yOff);
            spawnDroopingStrandsFromAllLeafCellsInLayer(world, gatedPlacer, random, config, layerCenter, layerRadius, maxDroopLen);

        }
    }

    /**
     * Organic acceptance test for layer cells. We treat y (depth) as part of a squashed ellipsoid to avoid boxy crowns.
     * y passed in here is negative going downward (vanilla convention in calls).
     */
    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        // Ellipsoidal contour with slight random softening near the rim:
        // Treat vertical as a weighted component to keep top broader, bottom narrower.
        int ay = Math.abs(y);
        // "distance squared" in an ellipsoid sense:
        double d2 = dx * dx + dz * dz + (ay * ay) * DEFAULT_Y_ELLIPTICITY;
        double r2 = radius * radius;

        if (d2 > r2) return true;

        // Introduce tiny holes near the rim to break up perfect edges:
        double rim = Math.max(0.0, r2 - d2);
        if (rim < Math.max(1.0, 0.15 * r2)) {
            // Near the edge: randomly skip a few spots for organic look
            if (random.nextFloat() < 0.03f) return true;
        }
        return false;
    }


    private static boolean isLeafAt(TestableWorld world, BlockPos pos) {
        return world.testBlockState(pos, s -> s.isIn(BlockTags.LEAVES));
    }

    /**
     * Iterate the whole layer's bounding box and fire a strand from every cell
     * where we placed a leaf this pass.
     */
    private void spawnDroopingStrandsFromAllLeafCellsInLayer(
            TestableWorld world,
            BlockPlacer placer,
            Random random,
            TreeFeatureConfig config,
            BlockPos layerCenter,
            int r,
            int maxDroopLen
    ) {
        if (r <= 0) return;
        final BlockPos.Mutable p = new BlockPos.Mutable();

        for (int x = -r; x <= r; x++) {
            for (int z = -r; z <= r; z++) {
                p.set(layerCenter, x, 0, z);
                if (placer.hasPlacedBlock(p)) {
                    // Start a strand at this exact leaf cell
                    startStrandThroughLeaves(world, placer, random, config, p, maxDroopLen);
                }
            }
        }
    }

    /**
     * Strand walker that *threads through* leaves (doesn't stop on them)
     * and only places when in replaceable space. Stops on non-replaceable.
     */
    private void startStrandThroughLeaves(
            TestableWorld world,
            BlockPlacer placer,
            Random random,
            TreeFeatureConfig config,
            BlockPos sourceLeafPos,
            int maxDroopLen
    ) {
        final BlockPos.Mutable cur = new BlockPos.Mutable().set(sourceLeafPos).move(Direction.DOWN);
        int steps = 0;
        Direction preferred = Direction.Type.HORIZONTAL.random(random);

        while (steps < maxDroopLen) {
            if (!isLeafAt(world, cur)) {
                // Not inside leaves → try to place; fails on solid → stop
                if (!FoliagePlacer.placeFoliageBlock(world, placer, random, config, cur)) {
                    break;
                }
            }
            steps++;

            // Continue?
            if (random.nextFloat() > strandContinueChance) break;

            // Drift sideways sometimes
            if (random.nextFloat() < driftChance) {
                Direction d = (random.nextFloat() < 0.6f) ? preferred : Direction.Type.HORIZONTAL.random(random);
                cur.move(d);
            }

            // Move down for next segment
            cur.move(Direction.DOWN);
        }
    }


    private static boolean withinFeatureRegion(ChunkPos origin, BlockPos p) { //this is to make leaves only generate in the feature chunk
        int pcx = p.getX() >> 4;   // chunk X of placement
        int pcz = p.getZ() >> 4;   // chunk Z of placement
        return Math.abs(pcx - origin.x) <= 1 && Math.abs(pcz - origin.z) <= 1;
    }


//    // Reuse the base helper for placing leaves (handles persistence/waterlogging via TreeFeature.canReplace)
//    private static boolean placeFoliageBlock(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, BlockPos pos) {
//        // Same semantics as the protected helper in FoliagePlacer:
//        return FoliagePlacer.placeFoliageBlock(world, placer, random, config, pos);
//    }
}
