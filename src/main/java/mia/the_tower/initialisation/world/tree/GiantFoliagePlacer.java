package mia.the_tower.initialisation.world.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

import net.minecraft.util.math.BlockPos;
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
 *
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
    private final float strandChance;
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
                                    .forGetter(p -> p.strandChance))
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
            float strandChance,
            float strandContinueChance,
            float driftChance,
            float verticalTaper,
            int layerFuzz
    ) {
        super(radius, offset);
        this.maxDroop = maxDroop;
        this.strandChance = strandChance;
        this.strandContinueChance = strandContinueChance;
        this.driftChance = driftChance;
        this.verticalTaper = verticalTaper;
        this.layerFuzz = layerFuzz;
    }

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
        final BlockPos center = node.getCenter();   // base stays fixed
        final boolean giantTrunk = node.isGiantTrunk();

        for (int dy = 0; dy < foliageHeight; dy++) {
            final int yOff = offset - dy;

            int layerRadius = Math.max(1, (int)Math.round(baseRadius - verticalTaper * dy));
            if (layerFuzz > 0) {
                layerRadius += random.nextBetween(-layerFuzz, layerFuzz);
                if (layerRadius < 1) layerRadius = 1;
            }

            // place leaves for this layer (single application of vertical offset)
            generateSquare(world, placer, random, config, center, layerRadius, yOff, giantTrunk);

            // spawn strands from THIS layer too
            BlockPos layerCenter = center.up(yOff);
            spawnDroopingStrandsFromPerimeter(
                    world, placer, random, config,
                    layerCenter, layerRadius, giantTrunk,
                    maxDroop.get(random) // or keep a cached maxDroopLen outside the loop
            );
        }

        // Add a small cap to avoid a pancake-flat top
        int capLayers = 3; // try 1 or 2
        for (int up = 1; up <= capLayers; up++) {
            int capRadius = Math.max(1, baseRadius - up - (layerFuzz > 0 ? random.nextBetween(0, layerFuzz) : 0));
            generateSquare(world, placer, random, config, center, capRadius, offset + up, giantTrunk);
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

    /**
     * Scan perimeter cells of a layer and grow hanging strands downward.
     */
    private void spawnDroopingStrandsFromPerimeter(
            TestableWorld world,
            BlockPlacer placer,
            Random random,
            TreeFeatureConfig config,
            BlockPos layerCenter,
            int r,
            boolean giantTrunk,
            int maxDroopLen
    ) {
        if (r <= 0) return;
        BlockPos.Mutable p = new BlockPos.Mutable();

        // We iterate the square border and only start strands below placed foliage.
        // Two passes: top/bottom edges, then left/right edges, to avoid duplicates.

        // Z-edges (north/south)
        for (int x = -r; x <= r; x++) {
            for (int zEdge : new int[]{-r, r + (giantTrunk ? 1 : 0)}) {
                tryStartStrand(world, placer, random, config, layerCenter, p.set(layerCenter, x, 0, zEdge), maxDroopLen);
            }
        }
        // X-edges (west/east), skip corners already handled:
        for (int z = -r + 1; z <= r - 1 + (giantTrunk ? 1 : 0); z++) {
            for (int xEdge : new int[]{-r, r + (giantTrunk ? 1 : 0)}) {
                tryStartStrand(world, placer, random, config, layerCenter, p.set(layerCenter, xEdge, 0, z), maxDroopLen);
            }
        }
    }

    private static boolean isLeafAt(TestableWorld world, BlockPos pos) {
        // TestableWorld gives us a predicate-based peek
        return world.testBlockState(pos, state -> state.isIn(BlockTags.LEAVES));
    }

    private void tryStartStrand(
            TestableWorld world,
            BlockPlacer placer,
            Random random,
            TreeFeatureConfig config,
            BlockPos layerCenter,
            BlockPos.Mutable leafPosAtLayer,
            int maxDroopLen
    ) {
        // Only droop below a perimeter leaf we actually placed for this layer:
        if (!placer.hasPlacedBlock(leafPosAtLayer)) return;
        if (random.nextFloat() > strandChance) return;

        final BlockPos.Mutable cur = new BlockPos.Mutable().set(leafPosAtLayer).move(Direction.DOWN);
        int steps = 0;

        // Pick a preferred sideways direction; can occasionally change
        Direction preferred = Direction.Type.HORIZONTAL.random(random);

        while (steps < maxDroopLen) {
            // If we're inside existing leaves, just pass through (do not try to place here)
            if (!isLeafAt(world, cur)) {
                // Not a leaf → try to place. This will succeed in air/water and fail on solid.
                if (!placeFoliageBlock(world, placer, random, config, cur)) {
                    // Hit non-replaceable (e.g., log/stone) → stop the strand.
                    break;
                }
            }
            // Count this vertical step whether we placed or just threaded through
            steps++;

            // Decide if the strand continues
            if (random.nextFloat() > strandContinueChance) break;

            // Optional sideways drift to create a vine-like curve
            if (random.nextFloat() < driftChance) {
                Direction d = (random.nextFloat() < 0.6f) ? preferred : Direction.Type.HORIZONTAL.random(random);
                cur.move(d);
            }

            // Move down for next step
            cur.move(Direction.DOWN);
        }
    }

//    // Reuse the base helper for placing leaves (handles persistence/waterlogging via TreeFeature.canReplace)
//    private static boolean placeFoliageBlock(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, BlockPos pos) {
//        // Same semantics as the protected helper in FoliagePlacer:
//        return FoliagePlacer.placeFoliageBlock(world, placer, random, config, pos);
//    }
}
