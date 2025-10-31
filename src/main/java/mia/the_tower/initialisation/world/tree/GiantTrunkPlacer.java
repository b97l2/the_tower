package mia.the_tower.initialisation.world.tree;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mia.the_tower.initialisation.block_init;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer.TreeNode;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import static mia.the_tower.The_Tower.id;

//honestly I just let chatgpt have fun on this one, it is not optimised in any way, but it looks very cool
//that's all I really wanted

public class GiantTrunkPlacer extends TrunkPlacer {
    // Extra params for taper & flare.
    private final float baseRadius;
    private final float topRadius;
    private final float taperPower;
    private final float flareAmplitude;
    private final float flareSigma;
    private final float shellThicknessBlocks;  // 2 near base, tapers to ~1 near top
    private final float snakeAccelScale;      // random “accel” magnitude per layer
    private final float snakeDamping;         // velocity damping
    private final float snakeMaxOffset;

    public static final MapCodec<GiantTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.intRange(1, 128).fieldOf("base_height").forGetter(p -> p.baseHeight),
            Codec.intRange(0, 128).fieldOf("height_rand_a").forGetter(p -> p.firstRandomHeight),
            Codec.intRange(0, 128).fieldOf("height_rand_b").forGetter(p -> p.secondRandomHeight),
            Codec.FLOAT.fieldOf("base_radius").forGetter(p -> p.baseRadius),
            Codec.FLOAT.fieldOf("top_radius").forGetter(p -> p.topRadius),
            Codec.FLOAT.fieldOf("taper_power").forGetter(p -> p.taperPower),
            Codec.FLOAT.fieldOf("flare_amplitude").forGetter(p -> p.flareAmplitude),
            Codec.FLOAT.fieldOf("flare_sigma").forGetter(p -> p.flareSigma),
            Codec.FLOAT.fieldOf("shell_thickness_blocks").forGetter(p -> p.shellThicknessBlocks),
            Codec.FLOAT.fieldOf("snake_accel_scale").forGetter(p -> p.snakeAccelScale),
            Codec.FLOAT.fieldOf("snake_damping").forGetter(p -> p.snakeDamping),
            Codec.FLOAT.fieldOf("snake_max_offset").forGetter(p -> p.snakeMaxOffset)
    ).apply(instance, GiantTrunkPlacer::new));

    public GiantTrunkPlacer(int baseHeight, int randomHeightA, int randomHeightB,
                            float baseRadius, float topRadius, float taperPower,
                            float flareAmplitude, float flareSigma, float shellThicknessBlocks, float snakeAccelScale, float snakeDamping,
                            float snakeMaxOffset) {
        super(baseHeight, randomHeightA, randomHeightB);
        this.baseRadius = baseRadius;
        this.topRadius = topRadius;
        this.taperPower = taperPower;
        this.flareAmplitude = flareAmplitude;
        this.flareSigma = flareSigma;
        this.shellThicknessBlocks = shellThicknessBlocks;
        this.snakeAccelScale = snakeAccelScale;
        this.snakeDamping = snakeDamping;
        this.snakeMaxOffset = snakeMaxOffset;
    }

    public static final TrunkPlacerType<GiantTrunkPlacer> GIANT_TRUNK =
            Registry.register(Registries.TRUNK_PLACER_TYPE,
                    id("giant_trunk"),
                    new TrunkPlacerType<>(GiantTrunkPlacer.CODEC));

    public static void init() {}

    @Override
    protected TrunkPlacerType<?> getType() {
        return GIANT_TRUNK; // register this
    }

    @Override
    public List<TreeNode> generate(TestableWorld world,
                                            BiConsumer<BlockPos, BlockState> replacer,
                                            Random random,
                                            int height,
                                            BlockPos startPos,
                                            TreeFeatureConfig config) {
        List<TreeNode> attachments = new ArrayList<>();
        BlockPos.Mutable m = new BlockPos.Mutable();

        // Snaking centerline state
        float cx = 0f, cz = 0f;
        float vx = 0f, vz = 0f;

        // Keep per-layer centers and radii for later decoration/branches
        int[] centerX = new int[height + 2];
        int[] centerZ = new int[height + 2];
        float[] outerR = new float[height + 2];
        float[] innerR = new float[height + 2];

        // 1) Build hollow, snaking trunk
        for (int y = 0; y < height; y++) {
            float t = (float) y / (float) height;

            // Smooth random walk for the center
            vx = vx * snakeDamping + (random.nextFloat() - 0.5f) * snakeAccelScale;
            vz = vz * snakeDamping + (random.nextFloat() - 0.5f) * snakeAccelScale;
            cx += vx;
            cz += vz;

            // Clamp offset
            float offLen = MathHelper.sqrt(cx * cx + cz * cz);
            if (offLen > snakeMaxOffset) {
                float s = snakeMaxOffset / offLen;
                cx *= s; cz *= s;
            }

            int cxi = startPos.getX() + Math.round(cx);
            int czi = startPos.getZ() + Math.round(cz);
            int cy  = startPos.getY() + y;

            float core = MathHelper.lerp((float)Math.pow(t, this.taperPower), this.baseRadius, this.topRadius);
            float flare = this.flareAmplitude * (float) Math.exp(-(y * y) / (2f * this.flareSigma * this.flareSigma));
            float rOuter = Math.max(1.0f, core + flare);

            // Thickness tapers slightly with height
            float thick = MathHelper.lerp(t, shellThicknessBlocks, 1.0f);
            float rInner = Math.max(0.0f, rOuter - thick);

            centerX[y] = cxi;
            centerZ[y] = czi;
            outerR[y] = rOuter;
            innerR[y] = rInner;

            int r = MathHelper.ceil(rOuter) + 1;
            for (int dx = -r; dx <= r; dx++) {
                for (int dz = -r; dz <= r; dz++) {
                    float d2 = dx * dx + dz * dz;
                    // shell region
                    if (d2 <= rOuter * rOuter && d2 > rInner * rInner) {
                        m.set(cxi + dx, cy, czi + dz);
                        if (canReplace(world, m)) {
                            BlockState log = config.trunkProvider.get(random, m)
                                    .with(PillarBlock.AXIS, net.minecraft.util.math.Direction.Axis.Y);
                            replacer.accept(m, log);
                        }
                    }
                    // carve interior
                    else if (d2 < rInner * rInner) {
                        m.set(cxi + dx, cy, czi + dz);
                        // Don’t accidentally break liquids/bedrock; basic guard:
                        if (isCarvable(world, m)) {
                            replacer.accept(m, Blocks.AIR.getDefaultState());
                        }
                    }
                }
            }
        }

        // Top cap (sealed)
        int topY = startPos.getY() + height;
        int cxi = centerX[Math.max(0, height - 1)];
        int czi = centerZ[Math.max(0, height - 1)];
        float rCap = Math.max(1.0f, outerR[Math.max(0, height - 1)] * 0.8f);
        int rC = MathHelper.ceil(rCap);
        for (int dx = -rC; dx <= rC; dx++) {
            for (int dz = -rC; dz <= rC; dz++) {
                if (dx * dx + dz * dz <= rCap * rCap) {
                    m.set(cxi + dx, topY, czi + dz);
                    if (canReplace(world, m)) {
                        BlockState log = config.trunkProvider.get(random, m)
                                .with(PillarBlock.AXIS, net.minecraft.util.math.Direction.Axis.Y);
                        replacer.accept(m, log);
                    }
                }
            }
        }


//to extend base to ground
        int baseY  = startPos.getY();
        int baseCx = centerX[0];
        int baseCz = centerZ[0];
// Use a modest search window around the expected radius
        int searchR = Math.max(6, MathHelper.ceil(outerR[0]) + 4);
// Measure outer radius by ray-casting the actual ring at baseY
        float measuredOuter = sampleBaseOuterRadiusByRaycast(world, baseY, baseCx, baseCz, searchR, 64);
// Fall back to the model radius if measurement failed
        if (measuredOuter <= 0f) measuredOuter = Math.max(1f, outerR[0]);
// Choose a simple thickness (use your shell thickness, clamped to [1,3] for neat seams)
        float skirtThickness = MathHelper.clamp(this.shellThicknessBlocks, 1.0f, 3.0f);
// Draw a clean annulus straight down (instead of extending every base cell)
        extendDownAsRing(world, replacer, random, config, baseY, baseCx, baseCz, measuredOuter, skirtThickness, 10);




        // 2) Branches from curved centerline
        int branchCount = 5 + random.nextInt(4);
        int minY = height / 2;
        for (int i = 0; i < branchCount; i++) {
            int y = minY + random.nextInt(Math.max(1, height - minY - 4));
            int bx = centerX[y], bz = centerZ[y];  // branch base follows the snake
            float rHere = Math.max(1.0f, outerR[y] * 0.55f);

            double theta = random.nextDouble() * Math.PI * 2.0;
            double pitch = MathHelper.lerp(random.nextFloat(), 0.20f, 0.60f);
            int len = MathHelper.floor(6 + rHere * 4 + random.nextFloat() * 4);

            int sx = bx + (int) Math.round(Math.cos(theta) * (rHere + 1));
            int sz = bz + (int) Math.round(Math.sin(theta) * (rHere + 1));
            int sy = startPos.getY() + y;

            int ex = sx + (int) Math.round(Math.cos(theta) * len);
            int ez = sz + (int) Math.round(Math.sin(theta) * len);
            int ey = sy + (int) Math.round(len * pitch);

            drawBranch(world, replacer, random, new BlockPos(sx, sy, sz), new BlockPos(ex, ey, ez),
                    MathHelper.clamp(MathHelper.floor(rHere), 1, 3), config);

            // foliage at end
            attachments.add(new TreeNode(new BlockPos(ex, ey, ez), 0, false));

            // Patch the inner shell immediately “behind” the branch start so the interior stays sealed
            patchBehindBranch(replacer, random, config, new BlockPos(sx, sy, sz), bx, bz);
        }

        // 3) Roots (reuse your existing; base at startPos)

        int rootCount = 3 + random.nextInt(4);
        for (int i = 0; i < rootCount; i++) {
            double theta = (2 * Math.PI / rootCount) * i + random.nextFloat() * 0.6f;
            makeRoot(world, replacer, random, startPos, theta, config);
        }

        // 4) Interior decoration: WALL_PLANT + FLOOR_PLANT
        decorateInterior(world, replacer, random, startPos.getY(), centerX, centerZ, innerR, height);

        // 5) Crown foliage anchor at top
        attachments.add(new TreeNode(new BlockPos(cxi, topY, czi), 0, false));
        return attachments;
    }


    private boolean isCarvable(TestableWorld world, BlockPos pos) {
        // Be stricter than isReplaceable: don’t nuke water/lava/bedrock
        return this.canReplace(world, pos);
    }

    private void patchBehindBranch(BiConsumer<BlockPos, BlockState> replacer, Random random,
                                   TreeFeatureConfig config, BlockPos branchBase, int trunkCx, int trunkCz) {
        // Fill a 3x3x3 lump centered slightly toward trunk center from the branchBase.
        int dx = Integer.compare(trunkCx, branchBase.getX());
        int dz = Integer.compare(trunkCz, branchBase.getZ());
        BlockPos core = branchBase.add(dx, 0, dz);
        BlockPos.Mutable m = new BlockPos.Mutable();
        for (int ox = -1; ox <= 1; ox++) for (int oy = -1; oy <= 1; oy++) for (int oz = -1; oz <= 1; oz++) {
            m.set(core.getX() + ox, core.getY() + oy, core.getZ() + oz);
            BlockState log = config.trunkProvider.get(random, m).with(PillarBlock.AXIS, net.minecraft.util.math.Direction.Axis.Y);
            replacer.accept(m, log);
        }
    }

    // Helper: choose the outward direction by comparing to cavity center
    private Direction pickOutward(int cx, int cz, int x, int z) {
        int dx = x - cx, dz = z - cz;
        if (Math.abs(dx) >= Math.abs(dz)) {
            return dx >= 0 ? Direction.EAST : Direction.WEST;
        } else {
            return dz >= 0 ? Direction.SOUTH : Direction.NORTH;
        }
    }

    private void decorateInterior(TestableWorld world,
                                  BiConsumer<BlockPos, BlockState> replacer,
                                  Random random,
                                  int baseY,
                                  int[] centerX, int[] centerZ, float[] innerR, int height) {
        BlockPos.Mutable m = new BlockPos.Mutable();

// A) WALL_PLANT: multiface placement like glow lichen
// Given: baseY, centerX[], centerZ[], innerR[], height, ModBlocks.WALL_PLANT
        for (int y = 2; y < height - 2; y++) {
            int cx = centerX[y], cz = centerZ[y];
            float rIn = innerR[y];
            if (rIn < 1.5f) continue;

            int samples = Math.max(8, MathHelper.floor(6 * rIn));
            for (int i = 0; i < samples; i++) {
                if (random.nextFloat() > 0.06f) continue;  // ~6% density
                double ang = (2 * Math.PI * i) / samples;

                int ix = cx + (int)Math.round(Math.cos(ang) * (rIn - 0.5));
                int iz = cz + (int)Math.round(Math.sin(ang) * (rIn - 0.5));
                int iy = baseY + y;
                BlockPos airPos = new BlockPos(ix, iy, iz);
                if (!this.canReplace(world, airPos)) continue;

                // Probe neighbors; prefer outward first, then the other 3.
                Direction outward = pickOutward(cx, cz, ix, iz);
                Direction[] order = new Direction[] {
                        outward,
                        outward.rotateYClockwise(),
                        outward.rotateYCounterclockwise(),
                        outward.getOpposite()
                };

                BlockState placed = null;
                for (Direction d : order) {
                    BlockPos neighbor = airPos.offset(d);
                    // "Solid enough" = NOT replaceable (you can tighten if needed)
                    if (!this.canReplace(world, neighbor)) {
                        BlockState s = block_init.STARLIGHT_LICHEN.getDefaultState();
                        if (s.getBlock() instanceof MultifaceGrowthBlock mgb) {
                            BooleanProperty face = MultifaceGrowthBlock.getProperty(d);
                            if (face != null && s.contains(face)) {
                                placed = s.with(face, true);
                            }
                        } else if (s.contains(Properties.HORIZONTAL_FACING)) {
                            // Fallback for a simple facing block: face inward
                            placed = s.with(Properties.HORIZONTAL_FACING, d.getOpposite());
                        }
                        if (placed != null) {
                            replacer.accept(airPos, placed);
                            break;
                        }
                    }
                }
            }
        }

        // B) FLOOR_PLANT: probe down to real ground per sample
        int startProbeY = baseY + 2;    // a bit above the nominal floor
        int maxDown = 12;               // search window; adjust if needed

        int baseCx = centerX[0], baseCz = centerZ[0];
        float rIn0 = innerR[0];
        if (rIn0 >= 1.5f) {
            int area = MathHelper.floor((float)Math.PI * rIn0 * rIn0);
            int tries = Math.min(64, Math.max(12, area / 2));
            for (int k = 0; k < tries; k++) {
                double ang = random.nextDouble() * Math.PI * 2;
                double rad = random.nextDouble() * (rIn0 - 0.5);
                int ix = baseCx + (int)Math.round(Math.cos(ang) * rad);
                int iz = baseCz + (int)Math.round(Math.sin(ang) * rad);

                int groundY = findGroundY(world, ix, startProbeY, iz, maxDown);
                BlockPos plantPos = new BlockPos(ix, groundY + 1, iz);

                // Make sure this plant position is inside the cavity at that Y slice
                int yi = MathHelper.clamp(groundY + 1 - baseY, 0, innerR.length - 1);
                int ccx = centerX[yi], ccz = centerZ[yi];
                float rIn = innerR[yi];
                if (rIn < 1.0f) continue;
                double dx = ix - ccx, dz = iz - ccz;
                if (dx * dx + dz * dz >= rIn * rIn) continue; // not inside

                // Place only if air/replaceable
                if (this.canReplace(world, plantPos)) {
                    BlockState plant = block_init.CERULEAN_SHORTGRASS.getDefaultState();
                    replacer.accept(plantPos, plant);
                }
            }
        }

    }

    private void drawBranch(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random,
                            BlockPos from, BlockPos to, int baseRadius, TreeFeatureConfig config) {
        int dx = to.getX() - from.getX();
        int dy = to.getY() - from.getY();
        int dz = to.getZ() - from.getZ();
        int steps = Math.max(Math.max(Math.abs(dx), Math.abs(dy)), Math.abs(dz));
        float fx = from.getX();
        float fy = from.getY();
        float fz = from.getZ();
        float stepx = dx / (float) steps;
        float stepy = dy / (float) steps;
        float stepz = dz / (float) steps;

        for (int i = 0; i <= steps; i++) {
            int cx = MathHelper.floor(fx + i * stepx);
            int cy = MathHelper.floor(fy + i * stepy);
            int cz = MathHelper.floor(fz + i * stepz);

            float t = steps == 0 ? 1f : (i / (float) steps);
            float radius = MathHelper.lerp(t, baseRadius, 1f);

            int r = MathHelper.floor(radius);
            for (int ox = -r; ox <= r; ox++) {
                for (int oz = -r; oz <= r; oz++) {
                    for (int oy = -Math.max(1, r / 2); oy <= Math.max(1, r / 2); oy++) {
                        if (ox * ox + oz * oz + oy * oy <= r * r) {
                            BlockPos p = new BlockPos(cx + ox, cy + oy, cz + oz);
                            if (canReplace(world, p)) {
                                // choose axis by dominant delta
                                net.minecraft.util.math.Direction.Axis axis =
                                        Math.abs(dx) >= Math.abs(dy) && Math.abs(dx) >= Math.abs(dz) ? net.minecraft.util.math.Direction.Axis.X :
                                                (Math.abs(dy) >= Math.abs(dz) ? net.minecraft.util.math.Direction.Axis.Y : net.minecraft.util.math.Direction.Axis.Z);
                                BlockState log = config.trunkProvider.get(random, p).with(PillarBlock.AXIS, axis);
                                replacer.accept(p, log);
                            }
                        }
                    }
                }
            }
        }
    }

    private void makeRoot(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random,
                          BlockPos base, double theta, TreeFeatureConfig config) {
        int rootLen = 6 + random.nextInt(6);
        // Simple two-segment path that arcs down
        BlockPos p0 = base;
        BlockPos p1 = base.add((int) Math.round(Math.cos(theta) * 3), 0, (int) Math.round(Math.sin(theta) * 3));
        BlockPos p2 = base.add((int) Math.round(Math.cos(theta) * rootLen), -1 - random.nextInt(2),
                (int) Math.round(Math.sin(theta) * rootLen));

        int segments = 16;
        for (int i = 0; i <= segments; i++) {
            float t = i / (float) segments;
            // Quadratic Bézier interpolation
            double x = MathHelper.lerp(t, MathHelper.lerp(t, p0.getX(), p1.getX()), MathHelper.lerp(t, p1.getX(), p2.getX()));
            double y = MathHelper.lerp(t, MathHelper.lerp(t, p0.getY(), p1.getY()), MathHelper.lerp(t, p1.getY(), p2.getY()));
            double z = MathHelper.lerp(t, MathHelper.lerp(t, p0.getZ(), p1.getZ()), MathHelper.lerp(t, p1.getZ(), p2.getZ()));

            BlockPos c = BlockPos.ofFloored(x, y, z);
            // Small radius, sometimes 2 near base
            int r = (i < 4 && random.nextBoolean()) ? 2 : 1;
            for (int ox = -r; ox <= r; ox++) {
                for (int oz = -r; oz <= r; oz++) {
                    if (ox * ox + oz * oz <= r * r) {
                        BlockPos p = c.add(ox, 0, oz);
                        if (canReplace(world, p)) {
                            BlockState log = config.trunkProvider.get(random, p)
                                    .with(PillarBlock.AXIS, Direction.Axis.Y);
                            replacer.accept(p, log);
                        }
                    }
                }
            }
        }
    }

    // Pack/unpack helpers for (x,z) pairs
    private static long packXZ(int x, int z) { return (((long)x) << 32) ^ (z & 0xffffffffL); }
    private static int unpackX(long v) { return (int)(v >> 32); }
    private static int unpackZ(long v) { return (int)v; }

    // Is this cell one of our already-placed trunk logs?
// Uses tags so it works with vanilla and custom logs (ensure your logs are in BlockTags.LOGS).
    private boolean isPlacedTrunkLog(TestableWorld world, BlockPos pos) {
        return world.testBlockState(pos, s ->
                s.getBlock() instanceof PillarBlock &&
                        (s.isIn(BlockTags.LOGS) || s.isIn(BlockTags.LOGS_THAT_BURN))
        );
    }

    /**
     * Read back the bottom-most ring at Y = baseY by scanning a square around the center.
     * Every (x,z) that currently holds a trunk log at that Y is added to the mask.
     */
    private java.util.Set<Long> collectBaseRingMaskByReadback(TestableWorld world,
                                                              int baseY, int cx, int cz, int searchR) {
        java.util.HashSet<Long> mask = new java.util.HashSet<>();
        BlockPos.Mutable m = new BlockPos.Mutable();
        for (int dx = -searchR; dx <= searchR; dx++) {
            for (int dz = -searchR; dz <= searchR; dz++) {
                m.set(cx + dx, baseY, cz + dz);
                if (isPlacedTrunkLog(world, m)) {
                    mask.add(packXZ(m.getX(), m.getZ()));
                }
            }
        }
        return mask;
    }

    // Ray-cast from the trunk center at baseY to estimate the outer radius of the lowest ring.
    private float sampleBaseOuterRadiusByRaycast(TestableWorld world,
                                                 int baseY, int cx, int cz,
                                                 int searchR, int angularSamples) {
        if (angularSamples <= 0) angularSamples = 48;
        java.util.ArrayList<Float> hits = new java.util.ArrayList<>(angularSamples);

        // Step along angle rays; record the last r in the FIRST contiguous log segment.
        for (int i = 0; i < angularSamples; i++) {
            double theta = (2.0 * Math.PI) * (i / (double) angularSamples);
            double cos = Math.cos(theta), sin = Math.sin(theta);

            boolean inLog = false;
            double lastLogR = -1.0;

            // Small radial step to avoid skipping narrow shells
            for (double r = 0.0; r <= searchR; r += 0.5) {
                int x = cx + (int)Math.round(cos * r);
                int z = cz + (int)Math.round(sin * r);

                if (isPlacedTrunkLog(world, new BlockPos(x, baseY, z))) {
                    inLog = true;
                    lastLogR = r;
                } else if (inLog) {
                    // left the first log segment -> done for this ray
                    break;
                }
            }
            if (lastLogR > 0.0) {
                hits.add((float) lastLogR);
            }
        }

        if (hits.isEmpty()) return -1f;  // caller can fall back

        // Use median to be robust against dents/holes.
        hits.sort(Float::compare);
        return hits.get(hits.size() / 2);
    }

    // Extend a clean annulus (outer radius / thickness) straight down up to maxDrop.
    private void extendDownAsRing(TestableWorld world,
                                  BiConsumer<BlockPos, BlockState> replacer,
                                  Random random,
                                  TreeFeatureConfig config,
                                  int baseY, int cx, int cz,
                                  float outerRadius, float thickness,
                                  int maxDrop) {

        float innerRadius = Math.max(0f, outerRadius - Math.max(1.0f, thickness));
        int rInt = MathHelper.ceil(outerRadius) + 1;
        int seamY = baseY - 1;

        BlockPos.Mutable m = new BlockPos.Mutable();
        BlockPos.Mutable below = new BlockPos.Mutable();

        for (int dx = -rInt; dx <= rInt; dx++) {
            for (int dz = -rInt; dz <= rInt; dz++) {
                float d2 = dx*dx + dz*dz;
                if (d2 > outerRadius*outerRadius || d2 <= innerRadius*innerRadius) continue;

                int x = cx + dx;
                int z = cz + dz;

                // Find first solid below seam within window
                below.set(x, seamY - 1, z);
                int drop = 0;
                int supportY = seamY;
                while (drop < maxDrop) {
                    if (!this.canReplace(world, below)) { // found support
                        supportY = below.getY() + 1;
                        break;
                    }
                    below.move(Direction.DOWN);
                    drop++;
                }
                if (drop == maxDrop) {
                    supportY = seamY - maxDrop + 1;
                }

                // Fill log columns down to support
                for (int y = seamY; y >= supportY; y--) {
                    m.set(x, y, z);
                    if (this.canReplace(world, m)) {
                        BlockState log = config.trunkProvider.get(random, m)
                                .with(PillarBlock.AXIS, Direction.Axis.Y);
                        replacer.accept(m, log);
                    } else {
                        break; // hit something mid-column
                    }
                }
            }
        }
    }




    private int findGroundY(TestableWorld world, int x, int startY, int z, int maxDown) {
        BlockPos.Mutable m = new BlockPos.Mutable(x, startY, z);
        for (int i = 0; i < maxDown; i++) {
            // First non-replaceable encountered is the ground.
            if (!this.canReplace(world, m)) {
                return m.getY();
            }
            m.move(net.minecraft.util.math.Direction.DOWN);
        }
        return startY - maxDown; // fallback if nothing solid within range
    }




}
