package mia.the_tower.initialisation.items;

import mia.the_tower.initialisation.block_init;
import mia.the_tower.initialisation.sounds.CustomSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;

public class PillarOfSaltItem extends Item {

    public PillarOfSaltItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);


        if (world.isClient) {
            return ActionResult.SUCCESS;
        }

        ServerWorld server = (ServerWorld) world;

        // play to everyone from server
        world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_SAND_BREAK, SoundCategory.PLAYERS, 1.0f, 0.6f);

        // 64-block raycast; include fluids? true here
        var hit = user.raycast(64.0, 0.0F, true);
        if (!(hit instanceof net.minecraft.util.hit.BlockHitResult bhr )) {
            return ActionResult.FAIL;
        }
        if (hit.getType() != HitResult.Type.BLOCK) {
            return ActionResult.FAIL;
        }

        BlockPos spawnAt = bhr.getBlockPos().offset(bhr.getSide());
        boolean ok = buildAt(server, spawnAt);

        return ok ? ActionResult.SUCCESS : ActionResult.FAIL;
    }


    public static boolean buildAt(ServerWorld world, BlockPos origin) {
        Random random = world.getRandom();

        int height = random.nextBetween(3, 25);
        int x = origin.getX();
        int z = origin.getZ();
        int y = origin.getY();
        double divisor = random.nextTriangular(2, 0.3);
        int count = 0;
        double mode = 1.7; //this is to dictate how fast the pillar decreases in size. It should always be above 1, lest the salt expands infinitely
                            // 1.7 is a good value for the item
                            // 1.1 is a good value for the biome feature
        var pos = new BlockPos.Mutable();

        setAndBellow(world, pos.set(x, y+height, z), SALT); //sets primary column
        while(height >0){
            count++;
            setAndBellow(world, pos.set(x-count, (int) (height/divisor) + y, z), SALT); //sets secondary columns with new height division each time
            divisor = random.nextTriangular(mode, 0.3);

            setAndBellow(world, pos.set(x+count, (int) (height/divisor) + y, z), SALT);
            divisor = random.nextTriangular(mode, 0.3);

            setAndBellow(world, pos.set(x, (int) (height/divisor) + y, z-count), SALT);
            divisor = random.nextTriangular(mode, 0.3);

            setAndBellow(world, pos.set(x, (int) (height/divisor) + y, z+count), SALT);
            divisor = random.nextTriangular(mode, 0.3);

            height = (int) (height/divisor); //to update to a smaller height for the next tier

            //this is to generate salt "noise" around the base so it is not just a cross in shape
            for(int i = count-1; i > 0; i--){
                int dif = count - 1 - i;
                divisor = random.nextTriangular(mode+dif, 0.3); // (-, +) axis
                setAndBellow(world, pos.set(x-(count-1), (int) (height/divisor) + y - 1, z+i), SALT);
                setAndBellow(world, pos.set(x-i, (int) (height/divisor) + y - 1, z+count-1), SALT);


                divisor = random.nextTriangular(mode+dif, 0.3); // (-, -) axis
                setAndBellow(world, pos.set(x-(count-1), (int) (height/divisor) + y - 1, z-i), SALT);
                setAndBellow(world, pos.set(x-i, (int) (height/divisor) + y - 1, z-(count-1)), SALT);

                divisor = random.nextTriangular(mode+dif, 0.3); // (+, +) axis
                setAndBellow(world, pos.set(x+count-1, (int) (height/divisor) + y - 1, z+i), SALT);
                setAndBellow(world, pos.set(x+i, (int) (height/divisor) + y - 1, z+count-1), SALT);

                divisor = random.nextTriangular(mode+dif, 0.3); // (+, -) axis
                setAndBellow(world, pos.set(x+count-1, (int) (height/divisor) + y - 1, z-i), SALT);
                setAndBellow(world, pos.set(x+i, (int) (height/divisor) + y - 1, z-(count-1)), SALT);
            }

            divisor = random.nextTriangular(mode, 0.3); //ready for the next round
        }
        return true;
    }

    private static void setAndBellow(StructureWorldAccess world,
                            BlockPos pos,
                            BlockState state) {
        BlockPos.Mutable pos1 = pos.mutableCopy();
        var current = world.getBlockState(pos);

        while(current.isAir()) {
            world.setBlockState(pos1, state, Block.NOTIFY_ALL);
            pos1.move(Direction.DOWN);
            current = world.getBlockState(pos1);
        }
    }

    //my picker
    private static final BlockState SALT = block_init.SALT.getDefaultState();

}





