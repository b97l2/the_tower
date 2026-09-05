package mia.the_tower.initialisation.world.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

import java.util.Optional;

public class LargeGinkgoTreeStructure extends Structure {
    public static final MapCodec<LargeGinkgoTreeStructure> CODEC = createCodec(LargeGinkgoTreeStructure::new);

    public LargeGinkgoTreeStructure(Config config) {
        super(config);
    }

    @Override
    protected Optional<StructurePosition> getStructurePosition(Context context) {
        ChunkPos chunkPos = context.chunkPos();
        int x = chunkPos.getCenterX();
        int z = chunkPos.getCenterZ();
        int y = context.chunkGenerator().getHeightOnGround(
                x,
                z,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                context.world(),
                context.noiseConfig()
        );

        if (y < context.world().getBottomY() + LargeGinkgoTreePiece.VERTICAL_RADIUS_DOWN
                || y + LargeGinkgoTreePiece.VERTICAL_RADIUS_UP > context.world().getTopYInclusive()) {
            return Optional.empty();
        }

        BlockPos origin = new BlockPos(x, y, z);
        long treeSeed = context.random().nextLong();
        return Optional.of(new StructurePosition(origin, collector ->
                collector.addPiece(new LargeGinkgoTreePiece(origin, treeSeed))
        ));
    }

    @Override
    public StructureType<?> getType() {
        return ModStructures.LARGE_GINKGO_TREE;
    }
}
