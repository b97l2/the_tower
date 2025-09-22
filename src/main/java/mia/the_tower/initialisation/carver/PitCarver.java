package mia.the_tower.initialisation.carver;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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

import org.apache.commons.lang3.mutable.MutableBoolean;

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
        if (!shouldCarve(cfg, random)) return false;

        // Choose a center somewhere inside this chunk
        int xCenter = chunkPos.getStartX() + random.nextInt(16);
        int zCenter = chunkPos.getStartZ() + random.nextInt(16);
        int lx = xCenter & 15;
        int lz = zCenter & 15;

        // Anchor the rim near the surface at this column
        int surfaceY = chunk.getHeightmap(Heightmap.Type.WORLD_SURFACE_WG).get(lx, lz);

        // Sample a start Y from the config, then clamp near the surface so the opening is visible
        int startY = MathHelper.clamp(cfg.y.get(random, context), surfaceY - 7, surfaceY + 4);

        int depth = Math.max(1, cfg.depth.get(random));
        int baseRadius = Math.max(1, cfg.radius.get(random));
        int taper = Math.max(1, cfg.taperHeight.get(random));
        int bottomY = Math.max(context.getMinY() + 1, startY - depth);

        boolean carvedAny = false;

        // Small horizontal wobble so the shaft isn't perfectly straight
        float driftX = 0f, driftZ = 0f;

        BlockPos.Mutable pos = new BlockPos.Mutable();
        BlockPos.Mutable tmp = new BlockPos.Mutable();
        MutableBoolean replacedGrassy = new MutableBoolean(false);

        for (int y = startY; y >= bottomY; --y) {
            int dy = startY - y;

            // Smoothly grow radius over the first `taper` layers (sloped rim)
            float t = MathHelper.clamp(dy / (float) taper, 0f, 1f);
            float smooth = t * t * (3f - 2f * t); // smoothstep

            int layerJitter = cfg.radiusJitter.get(random);
            float radiusAtY = Math.max(0f, smooth * (baseRadius + layerJitter));
            int rInt = MathHelper.floor(radiusAtY);

            driftX += (random.nextFloat() - 0.5f) * cfg.horizontalJitterPerY.get(random);
            driftZ += (random.nextFloat() - 0.5f) * cfg.horizontalJitterPerY.get(random);

            int x0 = MathHelper.floor(xCenter + driftX);
            int z0 = MathHelper.floor(zCenter + driftZ);
            int r2 = rInt * rInt;

            for (int dx = -rInt; dx <= rInt; dx++) {
                int xx = x0 + dx;
                int llx = xx & 15;
                for (int dz = -rInt; dz <= rInt; dz++) {
                    if (dx*dx + dz*dz > r2) continue;

                    int zz = z0 + dz;
                    int llz = zz & 15;
                    pos.set(xx, y, zz);

                    // skip if this local voxel was carved already
                    if (mask.get(llx, y, llz)) continue;

                    // Hand off to base logic: handles replaceables
                    if (this.carveAtPoint(context, cfg, chunk, posToBiome, mask, pos, tmp, aquiferSampler, replacedGrassy)) {
                        chunk.setBlockState(pos, Blocks.CAVE_AIR.getDefaultState(), false);
                        carvedAny = true;
                    }
                }
            }
        }

        return carvedAny;
    }


}
