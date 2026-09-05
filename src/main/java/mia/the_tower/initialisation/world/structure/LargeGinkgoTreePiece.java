package mia.the_tower.initialisation.world.structure;

import mia.the_tower.initialisation.block_init;
import mia.the_tower.initialisation.world.tree.GiantFoliagePlacer;
import mia.the_tower.initialisation.world.tree.GiantTrunkPlacer;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.structure.StructureContext;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.TwoLayersFeatureSize;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

public class LargeGinkgoTreePiece extends StructurePiece {
    public static final int HORIZONTAL_RADIUS = 96;
    public static final int VERTICAL_RADIUS_DOWN = 16;
    public static final int VERTICAL_RADIUS_UP = 250;
    private static final int TRUNK_EMBED_DEPTH = 10;

    private final BlockPos origin;
    private final long treeSeed;

    public LargeGinkgoTreePiece(BlockPos origin, long treeSeed) {
        super(ModStructures.LARGE_GINKGO_TREE_PIECE, 0, createBoundingBox(origin));
        this.origin = origin;
        this.treeSeed = treeSeed;
    }

    public LargeGinkgoTreePiece(NbtCompound nbt) {
        super(ModStructures.LARGE_GINKGO_TREE_PIECE, nbt);
        this.origin = new BlockPos(nbt.getInt("OriginX"), nbt.getInt("OriginY"), nbt.getInt("OriginZ"));
        this.treeSeed = nbt.getLong("TreeSeed");
    }

    private static BlockBox createBoundingBox(BlockPos origin) {
        return new BlockBox(
                origin.getX() - HORIZONTAL_RADIUS,
                origin.getY() - VERTICAL_RADIUS_DOWN,
                origin.getZ() - HORIZONTAL_RADIUS,
                origin.getX() + HORIZONTAL_RADIUS,
                origin.getY() + VERTICAL_RADIUS_UP,
                origin.getZ() + HORIZONTAL_RADIUS
        );
    }

    @Override
    protected void writeNbt(StructureContext context, NbtCompound nbt) {
        nbt.putInt("OriginX", this.origin.getX());
        nbt.putInt("OriginY", this.origin.getY());
        nbt.putInt("OriginZ", this.origin.getZ());
        nbt.putLong("TreeSeed", this.treeSeed);
    }

    @Override
    public void generate(
            StructureWorldAccess world,
            StructureAccessor structureAccessor,
            ChunkGenerator chunkGenerator,
            Random random,
            BlockBox chunkBox,
            ChunkPos chunkPos,
            BlockPos pivot
    ) {
        Map<Long, BlockState> states = this.generateTreeStates();
        for (Map.Entry<Long, BlockState> entry : states.entrySet()) {
            BlockPos pos = BlockPos.fromLong(entry.getKey());
            if (chunkBox.contains(pos)
                    && this.boundingBox.contains(pos)
                    && !world.isOutOfHeightLimit(pos)
                    && world.isValidForSetBlock(pos)) {
                world.setBlockState(pos, entry.getValue(), Block.NOTIFY_LISTENERS);
                FluidState fluidState = world.getFluidState(pos);
                if (!fluidState.isEmpty()) {
                    world.scheduleFluidTick(pos, fluidState.getFluid(), 0);
                }
            }
        }
    }

    private Map<Long, BlockState> generateTreeStates() {
        Map<Long, BlockState> states = new LinkedHashMap<>();
        Set<Long> foliagePositions = new HashSet<>();
        TreeFeatureConfig config = createTreeConfig();
        BlockPos treeStart = this.origin.down(TRUNK_EMBED_DEPTH);
        GeneratedTreeWorld treeWorld = new GeneratedTreeWorld(treeStart, states);
        Random random = Random.create(this.treeSeed);

        int trunkHeight = config.trunkPlacer.getHeight(random);
        int foliageHeight = config.foliagePlacer.getRandomHeight(random, trunkHeight, config);
        int foliageBaseHeight = trunkHeight - foliageHeight;
        int foliageRadius = config.foliagePlacer.getRandomRadius(random, foliageBaseHeight);

        BiConsumer<BlockPos, BlockState> trunkPlacer = (pos, state) ->
                states.put(pos.asLong(), state);
        FoliagePlacer.BlockPlacer foliagePlacer = new FoliagePlacer.BlockPlacer() {
            @Override
            public void placeBlock(BlockPos pos, BlockState state) {
                long packedPos = pos.asLong();
                foliagePositions.add(packedPos);
                states.put(packedPos, state);
            }

            @Override
            public boolean hasPlacedBlock(BlockPos pos) {
                return foliagePositions.contains(pos.asLong());
            }
        };

        GiantFoliagePlacer.DISABLE_FEATURE_REGION_LIMIT.set(true);
        try {
            List<FoliagePlacer.TreeNode> treeNodes =
                    config.trunkPlacer.generate(treeWorld, trunkPlacer, random, trunkHeight, treeStart, config);
            for (FoliagePlacer.TreeNode treeNode : treeNodes) {
                config.foliagePlacer.generate(
                        treeWorld,
                        foliagePlacer,
                        random,
                        config,
                        trunkHeight,
                        treeNode,
                        foliageHeight,
                        foliageRadius
                );
            }
        } finally {
            GiantFoliagePlacer.DISABLE_FEATURE_REGION_LIMIT.remove();
        }

        return states;
    }

    private static TreeFeatureConfig createTreeConfig() {
        return new TreeFeatureConfig.Builder(
                BlockStateProvider.of(block_init.GINKGO_LOG),
                new GiantTrunkPlacer(50, 88, 95, 15, 3, 0.5f, 0.3f, 3, 0.4f, 0.8f, 3.5f),
                BlockStateProvider.of(block_init.GINKGO_LEAVES),
                new GiantFoliagePlacer(
                        ConstantIntProvider.create(14),
                        ConstantIntProvider.create(0),
                        UniformIntProvider.create(18, 42),
                        1f,
                        0.90f,
                        0.30f,
                        0.70f,
                        2
                ),
                new TwoLayersFeatureSize(3, 0, 4)
        ).build();
    }

    private static class GeneratedTreeWorld implements TestableWorld {
        private static final BlockState AIR = Blocks.AIR.getDefaultState();
        private static final BlockState SOLID_GROUND = Blocks.STONE.getDefaultState();

        private final BlockPos origin;
        private final Map<Long, BlockState> generatedStates;

        private GeneratedTreeWorld(BlockPos origin, Map<Long, BlockState> generatedStates) {
            this.origin = origin;
            this.generatedStates = generatedStates;
        }

        @Override
        public boolean testBlockState(BlockPos pos, Predicate<BlockState> predicate) {
            return predicate.test(this.getState(pos));
        }

        @Override
        public boolean testFluidState(BlockPos pos, Predicate<FluidState> predicate) {
            return predicate.test(this.getState(pos).getFluidState());
        }

        @Override
        public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
            return Optional.empty();
        }

        @Override
        public BlockPos getTopPosition(Heightmap.Type heightmap, BlockPos pos) {
            return new BlockPos(pos.getX(), this.origin.getY(), pos.getZ());
        }

        private BlockState getState(BlockPos pos) {
            BlockState generatedState = this.generatedStates.get(pos.asLong());
            if (generatedState != null) {
                return generatedState;
            }
            return pos.getY() >= this.origin.getY() ? AIR : SOLID_GROUND;
        }
    }
}
