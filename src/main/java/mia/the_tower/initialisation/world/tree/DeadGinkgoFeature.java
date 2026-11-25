package mia.the_tower.initialisation.world.tree;

import mia.the_tower.initialisation.block_init;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.block.PillarBlock;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.ArrayList;
import java.util.List;

public class DeadGinkgoFeature extends Feature<DefaultFeatureConfig> {
    public static final Feature<DefaultFeatureConfig> DEAD_GINKGO = Registry.register(
            Registries.FEATURE,
            Identifier.of("the_tower", "dead_ginkgo"),
            new DeadGinkgoFeature()
    );

    private DeadGinkgoFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    public static void init() {}

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        Random random = context.getRandom();
        BlockPos origin = context.getOrigin();

        if (!canReplace(world.getBlockState(origin)) || !world.getBlockState(origin.down()).isSolidBlock(world, origin.down())) {
            return false;
        }

        int height = MathHelper.nextInt(random, 5, 15);
        int breakHeight = Math.max(3, height - MathHelper.nextInt(random, 2, 4));
        int baseRadius = random.nextFloat() < 0.2F ? 3 : 2;
        List<BlockPos> trunkBlocks = new ArrayList<>();

        BlockPos topCenter = generateTrunk(world, random, origin, breakHeight, baseRadius, trunkBlocks);
        generateSplinters(world, random, topCenter, height - breakHeight, baseRadius, trunkBlocks);

        if (trunkBlocks.isEmpty()) {
            return false;
        }

        placeLichen(world, random, trunkBlocks);
        placeChanterelles(world, random, origin, baseRadius);
        return true;
    }

    private BlockPos generateTrunk(StructureWorldAccess world, Random random, BlockPos origin, int breakHeight,
                                   int baseRadius, List<BlockPos> trunkBlocks) {
        int centerX = origin.getX();
        int centerZ = origin.getZ();

        for (int y = 0; y <= breakHeight; y++) {
            if (y > 2 && random.nextFloat() < 0.18F) {
                centerX = MathHelper.clamp(centerX + random.nextInt(3) - 1, origin.getX() - 1, origin.getX() + 1);
                centerZ = MathHelper.clamp(centerZ + random.nextInt(3) - 1, origin.getZ() - 1, origin.getZ() + 1);
            }

            float progress = y / (float) Math.max(1, breakHeight);
            float radius = MathHelper.lerp(progress, baseRadius + 0.35F, 1.55F);
            int bounds = MathHelper.ceil(radius);

            for (int offsetX = -bounds; offsetX <= bounds; offsetX++) {
                for (int offsetZ = -bounds; offsetZ <= bounds; offsetZ++) {
                    float roughness = 0.82F + random.nextFloat() * 0.3F;
                    if (offsetX * offsetX + offsetZ * offsetZ > radius * radius * roughness) {
                        continue;
                    }

                    placeLog(world, new BlockPos(centerX + offsetX, origin.getY() + y, centerZ + offsetZ),
                            Direction.Axis.Y, trunkBlocks);
                }
            }
        }

        return new BlockPos(centerX, origin.getY() + breakHeight, centerZ);
    }

    private void generateSplinters(StructureWorldAccess world, Random random, BlockPos topCenter,
                                   int availableHeight, int baseRadius, List<BlockPos> trunkBlocks) {
        int splinterCount = MathHelper.nextInt(random, 3, 5);
        double angleOffset = random.nextDouble() * Math.PI * 2.0;

        for (int splinter = 0; splinter < splinterCount; splinter++) {
            double angle = angleOffset + Math.PI * 2.0 * splinter / splinterCount + (random.nextDouble() - 0.5) * 0.7;
            double radialDistance = 0.5 + random.nextDouble() * Math.max(0.75, baseRadius - 0.5);
            int startX = topCenter.getX() + (int) Math.round(Math.cos(angle) * radialDistance);
            int startZ = topCenter.getZ() + (int) Math.round(Math.sin(angle) * radialDistance);
            int splinterHeight = splinter == 0 ? availableHeight : MathHelper.nextInt(random, 1, availableHeight);

            for (int step = 1; step <= splinterHeight; step++) {
                float lean = step / (float) Math.max(1, splinterHeight);
                int x = startX + (int) Math.round(Math.cos(angle) * lean);
                int z = startZ + (int) Math.round(Math.sin(angle) * lean);
                BlockPos position = new BlockPos(x, topCenter.getY() + step, z);
                placeLog(world, position, Direction.Axis.Y, trunkBlocks);

                if (step <= 2 && random.nextBoolean()) {
                    Direction inward = Direction.getFacing(-Math.cos(angle), 0, -Math.sin(angle));
                    placeLog(world, position.offset(inward), Direction.Axis.Y, trunkBlocks);
                }
            }
        }
    }

    private void placeLichen(StructureWorldAccess world, Random random, List<BlockPos> trunkBlocks) {
        Direction[] sides = Direction.Type.HORIZONTAL.stream().toArray(Direction[]::new);
        int attempts = MathHelper.nextInt(random, 8, 14);

        for (int attempt = 0; attempt < attempts; attempt++) {
            Direction side = sides[random.nextInt(sides.length)];
            BlockPos logPos = trunkBlocks.get(random.nextInt(trunkBlocks.size()));
            BlockPos lichenPos = logPos.offset(side);

            if (!canReplace(world.getBlockState(lichenPos))) {
                continue;
            }

            Direction attachedFace = side.getOpposite();
            if (!world.getBlockState(logPos).isOf(block_init.GINKGO_LOG)) {
                continue;
            }

            BlockState lichen = block_init.STARLIGHT_LICHEN.getDefaultState();
            if (lichen.getBlock() instanceof MultifaceGrowthBlock) {
                BooleanProperty faceProperty = MultifaceGrowthBlock.getProperty(attachedFace);
                if (faceProperty != null && lichen.contains(faceProperty)) {
                    BlockState placedState = lichen.with(faceProperty, true);
                    if (placedState.canPlaceAt(world, lichenPos)) {
                        world.setBlockState(lichenPos, placedState, Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }
    }

    private void placeChanterelles(StructureWorldAccess world, Random random, BlockPos origin, int baseRadius) {
        int targetCount = MathHelper.nextInt(random, 5, 9);
        int placed = 0;

        for (int attempt = 0; attempt < targetCount * 5 && placed < targetCount; attempt++) {
            double angle = random.nextDouble() * Math.PI * 2.0;
            double distance = baseRadius + 1.0 + random.nextDouble() * 3.5;
            int x = origin.getX() + (int) Math.round(Math.cos(angle) * distance);
            int z = origin.getZ() + (int) Math.round(Math.sin(angle) * distance);
            int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
            BlockPos position = new BlockPos(x, y, z);
            BlockState chanterelle = block_init.CHANTERELLE.getDefaultState();

            if (canReplace(world.getBlockState(position)) && chanterelle.canPlaceAt(world, position)) {
                world.setBlockState(position, chanterelle, Block.NOTIFY_LISTENERS);
                placed++;
            }
        }
    }

    private void placeLog(StructureWorldAccess world, BlockPos position, Direction.Axis axis, List<BlockPos> trunkBlocks) {
        if (!canReplace(world.getBlockState(position))) {
            return;
        }

        world.setBlockState(position, block_init.GINKGO_LOG.getDefaultState().with(PillarBlock.AXIS, axis),
                Block.NOTIFY_LISTENERS);
        trunkBlocks.add(position.toImmutable());
    }

    private static boolean canReplace(BlockState state) {
        return state.isAir() || state.isIn(BlockTags.REPLACEABLE_BY_TREES);
    }
}
