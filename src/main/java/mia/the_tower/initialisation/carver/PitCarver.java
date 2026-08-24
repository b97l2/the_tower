package mia.the_tower.initialisation.carver;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.carver.Carver;
import net.minecraft.world.gen.carver.CarverContext;
import net.minecraft.world.gen.carver.CarvingMask;
import net.minecraft.world.gen.chunk.AquiferSampler;
import net.minecraft.registry.entry.RegistryEntry;

import java.util.function.Function;

public class PitCarver extends Carver<PitCarverConfig> {

    public PitCarver() {
        super(PitCarverConfig.DIRECT_CODEC); // Carver wants a Codec<C>
    }

    @Override
    public boolean shouldCarve(PitCarverConfig cfg, Random random) {
        return random.nextFloat() < cfg.probability; // from ProbabilityConfig
    }

    @Override
    public boolean carve(
            CarverContext context,
            PitCarverConfig cfg,
            Chunk chunk,
            Function<BlockPos, RegistryEntry<Biome>> posToBiome,
            Random random,
            AquiferSampler aquiferSampler,
            ChunkPos chunkPos,
            CarvingMask mask
    ) {
        // Choose a center somewhere inside the source chunk.
        int xCenter = chunkPos.getStartX() + random.nextInt(16);
        int zCenter = chunkPos.getStartZ() + random.nextInt(16);
        ChunkPos carvingChunkPos = chunk.getPos();

        int requestedDepth = Math.max(1, cfg.depth.get(random));
        int pitRadius = Math.max(1, cfg.radius.get(random));
        int minimumBottomY = context.getMinY() + 5;
        int depth = Math.max(20, requestedDepth);
        int flareHeight = MathHelper.clamp(cfg.taperHeight.get(random), 1, Math.max(1, depth - 1));
        float flareRadius = pitRadius * 2.5f;
        float bottomRadius = Math.max(2.0f, pitRadius * 0.12f);

        boolean carvedAny = false;

        BlockPos.Mutable pos = new BlockPos.Mutable();

        int rInt = MathHelper.ceil(flareRadius);
        float flareRadiusSquared = flareRadius * flareRadius;

        int minX = Math.max(xCenter - rInt, carvingChunkPos.getStartX());
        int maxX = Math.min(xCenter + rInt, carvingChunkPos.getEndX());
        int minZ = Math.max(zCenter - rInt, carvingChunkPos.getStartZ());
        int maxZ = Math.min(zCenter + rInt, carvingChunkPos.getEndZ());

        for (int xx = minX; xx <= maxX; xx++) {
            int dx = xx - xCenter;
            int localX = xx - carvingChunkPos.getStartX();

            for (int zz = minZ; zz <= maxZ; zz++) {
                int dz = zz - zCenter;
                float distanceSquared = dx * dx + dz * dz;
                if (distanceSquared > flareRadiusSquared) continue;

                int localZ = zz - carvingChunkPos.getStartZ();
                int surfaceY = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG).get(localX, localZ);
                int availableDepth = surfaceY - minimumBottomY;
                if (availableDepth < 1) continue;

                float distance = MathHelper.sqrt(distanceSquared);
                int columnDepth = Math.min(getColumnDepth(distance, pitRadius, flareRadius, bottomRadius, flareHeight, depth), availableDepth);
                if (columnDepth <= 0) continue;

                int bottomY = surfaceY - columnDepth;
                for (int y = surfaceY; y >= bottomY; y--) {
                    carvedAny |= carveGroundBlock(cfg, chunk, mask, pos, xx, y, zz, localX, localZ);
                }
            }
        }

        return carvedAny;
    }

    private boolean carveGroundBlock(
            PitCarverConfig cfg,
            Chunk chunk,
            CarvingMask mask,
            BlockPos.Mutable pos,
            int x,
            int y,
            int z,
            int localX,
            int localZ
    ) {
        pos.set(x, y, z);
        BlockState state = chunk.getBlockState(pos);
        if (state.isAir() || !this.canAlwaysCarveBlock(cfg, state) && !(state.getBlock() instanceof FluidBlock)) return false;

        mask.set(localX, y, localZ);
        chunk.setBlockState(pos, Blocks.CAVE_AIR.getDefaultState(), false);
        return true;
    }

    private static int getColumnDepth(float distance, int pitRadius, float flareRadius, float bottomRadius, int flareHeight, int depth) {
        if (distance <= bottomRadius) {
            return depth;
        }

        if (distance <= pitRadius) {
            float progress = (pitRadius - distance) / Math.max(1.0f, pitRadius - bottomRadius);
            return flareHeight + MathHelper.floor(smoothStep(progress) * (depth - flareHeight));
        }

        float progress = (flareRadius - distance) / Math.max(1.0f, flareRadius - pitRadius);
        return 1 + MathHelper.floor(smoothStep(progress) * (flareHeight - 1));
    }

    private static float smoothStep(float value) {
        float clamped = MathHelper.clamp(value, 0.0f, 1.0f);
        return clamped * clamped * (3f - 2f * clamped);
    }

}
