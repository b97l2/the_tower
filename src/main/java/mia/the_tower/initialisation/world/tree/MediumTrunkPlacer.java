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
import net.minecraft.util.math.ChunkPos;
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

public class MediumTrunkPlacer extends TrunkPlacer {
    // Extra params for taper & flare.
    private final float baseRadius;
    private final float topRadius;
    private final float taperPower;
    private final float flareSigma;
    private final float shellThicknessBlocks;  // 2 near base, tapers to ~1 near top
    private final float snakeAccelScale;      // random “accel” magnitude per layer
    private final float snakeDamping;         // velocity damping
    private final float snakeMaxOffset;

    public static final MapCodec<MediumTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.intRange(1, 128).fieldOf("base_height").forGetter(p -> p.baseHeight),
            Codec.intRange(0, 128).fieldOf("height_rand_a").forGetter(p -> p.firstRandomHeight),
            Codec.intRange(0, 128).fieldOf("height_rand_b").forGetter(p -> p.secondRandomHeight),
            Codec.FLOAT.fieldOf("base_radius").forGetter(p -> p.baseRadius),
            Codec.FLOAT.fieldOf("top_radius").forGetter(p -> p.topRadius),
            Codec.FLOAT.fieldOf("taper_power").forGetter(p -> p.taperPower),
            Codec.FLOAT.fieldOf("flare_sigma").forGetter(p -> p.flareSigma),
            Codec.FLOAT.fieldOf("shell_thickness_blocks").forGetter(p -> p.shellThicknessBlocks),
            Codec.FLOAT.fieldOf("snake_accel_scale").forGetter(p -> p.snakeAccelScale),
            Codec.FLOAT.fieldOf("snake_damping").forGetter(p -> p.snakeDamping),
            Codec.FLOAT.fieldOf("snake_max_offset").forGetter(p -> p.snakeMaxOffset)
    ).apply(instance, MediumTrunkPlacer::new));

    public MediumTrunkPlacer(int baseHeight, int randomHeightA, int randomHeightB,
                             float baseRadius, float topRadius, float taperPower,
                             float flareSigma, float shellThicknessBlocks, float snakeAccelScale, float snakeDamping,
                             float snakeMaxOffset) {
        super(baseHeight, randomHeightA, randomHeightB);
        this.baseRadius = baseRadius;
        this.topRadius = topRadius;
        this.taperPower = taperPower;
        this.flareSigma = flareSigma;
        this.shellThicknessBlocks = shellThicknessBlocks;
        this.snakeAccelScale = snakeAccelScale;
        this.snakeDamping = snakeDamping;
        this.snakeMaxOffset = snakeMaxOffset;
    }

    public static final TrunkPlacerType<MediumTrunkPlacer> MEDIUM_TRUNK =
            Registry.register(Registries.TRUNK_PLACER_TYPE,
                    id("medium_trunk"),
                    new TrunkPlacerType<>(MediumTrunkPlacer.CODEC));

    public static void init() {}

    @Override
    protected TrunkPlacerType<?> getType() {
        return MEDIUM_TRUNK; // register this
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

        //for chunk window
        ChunkPos origin = new ChunkPos(startPos);
        GiantFoliagePlacer.CURRENT_TREE_ORIGIN.set(origin);

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
            //the variable of the devil, I am ignoring it because it causes more trouble than what it is worth
            float flareAmplitude = 0;
            float flare = flareAmplitude * (float) Math.exp(-(y * y) / (2f * this.flareSigma * this.flareSigma));
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
                                    .with(PillarBlock.AXIS, Direction.Axis.Y);
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
                                .with(PillarBlock.AXIS, Direction.Axis.Y);
                        replacer.accept(m, log);
                    }
                }
            }
        }

        // 2) Branches from curved centerline
        int branchCount = 5 + random.nextInt(4);
        int minY = height / 2;
        for (int i = 0; i < branchCount; i++) {
            int y = minY + random.nextInt(Math.max(1, height - minY - 4));
            int bx = centerX[y], bz = centerZ[y];  // branch base follows the snake
            float rHere = Math.max(1.0f, outerR[y] * 0.55f);

            double theta = random.nextDouble() * Math.PI * 2.0;
            double pitch = MathHelper.lerp(random.nextFloat(), 0.70f, 0.90f); //gouverns the angle the branch makes
            int len = MathHelper.floor(6 + rHere * 2 + random.nextFloat() * 3); //gouverns the length of the branches, I changed the constants (2 and 3)

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

        }

        // 5) Crown foliage anchor at top
        attachments.add(new TreeNode(new BlockPos(cxi, topY, czi), 0, false));
        return attachments;

    }


    private boolean isCarvable(TestableWorld world, BlockPos pos) {
        // Be stricter than isReplaceable: don’t nuke water/lava/bedrock
        return this.canReplace(world, pos);
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
                                Direction.Axis axis =
                                        Math.abs(dx) >= Math.abs(dy) && Math.abs(dx) >= Math.abs(dz) ? Direction.Axis.X :
                                                (Math.abs(dy) >= Math.abs(dz) ? Direction.Axis.Y : Direction.Axis.Z);
                                BlockState log = config.trunkProvider.get(random, p).with(PillarBlock.AXIS, axis);
                                replacer.accept(p, log);
                            }
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


    // Ray-cast from the trunk center at baseY to estimate the outer radius of the lowest ring.
    private float sampleBaseOuterRadiusByRaycast(TestableWorld world,
                                                 int baseY, int cx, int cz,
                                                 int searchR, int angularSamples) {
        if (angularSamples <= 0) angularSamples = 48;
        ArrayList<Float> hits = new ArrayList<>(angularSamples);

        //baseY = baseY + 1; this stops root generation

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
        return hits.get(hits.size() / 2); //changing this does nothing
    }

}
