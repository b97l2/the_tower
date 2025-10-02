package mia.the_tower.initialisation.items;

import mia.the_tower.initialisation.block_init;
import mia.the_tower.initialisation.particle.CustomParticles;
import mia.the_tower.initialisation.sounds.CustomSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;

public class BlajItem extends Item {

    public BlajItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);


        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        // SERVER: safe to cast now
        ServerWorld server = (ServerWorld) world;

        // play to everyone from server
        world.playSound(null, user.getBlockPos(), CustomSounds.BLAJ_USE, SoundCategory.PLAYERS, 1.0f, 1.0f);

        // 64-block raycast; include fluids? true here
        var hit = user.raycast(64.0, 0.0F, true);
        if (!(hit instanceof net.minecraft.util.hit.BlockHitResult bhr)) {
            return ActionResult.FAIL;
        }

        BlockPos spawnAt = bhr.getBlockPos().offset(bhr.getSide());
        boolean ok = buildAt(server, spawnAt);

        if (ok && !user.isCreative()) {
            user.getStackInHand(hand).decrement(1);
        }
        return ok ? ActionResult.CONSUME : ActionResult.FAIL;
    }


    public static boolean buildAt(ServerWorld world, BlockPos origin) {
        Random random = world.getRandom();

        int height = random.nextBetween(8, 18);
        int x = origin.getX();
        int z = origin.getZ();
        int y = origin.getY() + height;

        var pos = new net.minecraft.util.math.BlockPos.Mutable();

        //sets the edges and the faces here
        for (int i = height; i >= 0; i--) {
            int dif = height - i;
            //for creating the edges
            set(world, pos.set(x - dif, y - dif, z), pick(random, EDGES, 5));
            set(world, pos.set(x + dif, y - dif, z), pick(random, EDGES, 5));
            set(world, pos.set(x, y - dif, z + dif), pick(random, EDGES, 5));
            set(world, pos.set(x, y - dif, z - dif), pick(random, EDGES, 5));
            //for creating the faces
            for (int j = dif; j >= 1; j--) {
                if(i != 0) {
                    set(world, pos.set((x - dif) + j, y - dif, z + j), pick(random, FACES, 8));
                }
                else set(world, pos.set((x - dif) + j, y - dif, z + j), pick(random, EDGES, 5));
            }
            for (int j = dif; j >= 1; j--) {
                if(i != 0) {
                set(world, pos.set((x + dif) - j, y - dif, z - j), pick(random, FACES, 8));
                }
                else set(world, pos.set((x + dif) - j, y - dif, z - j), pick(random, EDGES, 5));
            }
            for (int j = dif; j >= 1; j--) {
                if(i != 0) {
                set(world, pos.set(x + j, y - dif, (z + dif) - j), pick(random, FACES, 8));
                }
                set(world, pos.set(x + j, y - dif, (z + dif) - j), pick(random, EDGES, 5));
            }
            for (int j = dif; j >= 1; j--) {
                if(i != 0) {
                set(world, pos.set(x - j, y - dif, (z - dif) + j), pick(random, FACES, 8));
                }
                set(world, pos.set(x - j, y - dif, (z - dif) + j), pick(random, EDGES, 5));
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


