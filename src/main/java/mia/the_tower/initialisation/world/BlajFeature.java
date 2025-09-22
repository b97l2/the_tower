package mia.the_tower.initialisation.world;

import mia.the_tower.initialisation.block_init;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class BlajFeature extends Feature<DefaultFeatureConfig> {
    public BlajFeature() {
        super(DefaultFeatureConfig.CODEC);
    }

    // this method is what is called when the game tries to generate the feature. it is where the actual blocks get placed into the world.
    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();
        // the origin is the place where the game starts trying to place the feature
        BlockPos origin = context.getOrigin();
        Random random = context.getRandom();
        DefaultFeatureConfig config = context.getConfig();

        int height = random.nextBetween(8, 18);
        int x = origin.getX();
        int z = origin.getZ();
        int y = origin.getY() + height;

        var pos = new net.minecraft.util.math.BlockPos.Mutable();

        //sets the edges and the faces here
        for (int i = height; i >= 0; i--) {
            int dif = height - i;
            set(world, pos.set(x - dif, y - dif, z), pick(random, EDGES, 5));
            for (int j = dif; j >= 1; j--) {
                set(world, pos.set((x - dif) + j, y - dif, z + j), pick(random, FACES, 8));
            }
            set(world, pos.set(x + dif, y - dif, z), pick(random, EDGES, 5));
            for (int j = dif; j >= 1; j--) {
                set(world, pos.set((x + dif) - j, y - dif, z - j), pick(random, FACES, 8));
            }
            set(world, pos.set(x, y - dif, z + dif), pick(random, EDGES, 5));
            for (int j = dif; j >= 1; j--) {
                set(world, pos.set(x + j, y - dif, (z + dif) - j), pick(random, FACES, 8));
            }
            set(world, pos.set(x, y - dif, z - dif), pick(random, EDGES, 5));
            for (int j = dif; j >= 1; j--) {
                set(world, pos.set(x - j, y - dif, (z + dif) + j), pick(random, FACES, 8));
            }
        }

        return true;
    }

    private static void set(net.minecraft.world.StructureWorldAccess world,
                            net.minecraft.util.math.BlockPos pos,
                            net.minecraft.block.BlockState state) {
        var current = world.getBlockState(pos);
        if (current.isAir()) {
            world.setBlockState(pos, state, net.minecraft.block.Block.NOTIFY_ALL);
        }
    }

    //my picker
    private static final net.minecraft.block.BlockState[] EDGES = new net.minecraft.block.BlockState[] {
            block_init.BLAJ_LESSER_ORE.getDefaultState(),
            block_init.BLAJ_ORE.getDefaultState()
    };

    private static final net.minecraft.block.BlockState[] FACES = new net.minecraft.block.BlockState[] {
            block_init.DARKNESS.getDefaultState(),
            block_init.BLAJ_LESSER_ORE.getDefaultState()
    };

    private static net.minecraft.block.BlockState pick(net.minecraft.util.math.random.Random rand, net.minecraft.block.BlockState[] State, int primary_weight) {
        int r = rand.nextInt(10);
        if (r < primary_weight) return State[0];
        return State[1];
    }
}