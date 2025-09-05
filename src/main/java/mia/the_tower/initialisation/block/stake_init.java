package mia.the_tower.initialisation.block;

import mia.the_tower.initialisation.sounds.CustomSounds;
import net.minecraft.block.*;
import net.minecraft.block.enums.BambooLeaves;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import static mia.the_tower.initialisation.block_init.registerBlock;

public class stake_init {

    // This is where we create new crop blocks
    public static final Block STAKE = registerBlock("stake",
            new StakeBlock(AbstractBlock.Settings.copy(Blocks.BAMBOO)
                    .nonOpaque()
                    .ticksRandomly()
                    .sounds(CustomSounds.SCREAMING_BLOCK)
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "stake")))
            ));
    public static final Block STAKE_BUD = registerBlock("stake_bud",
            new stake_init.CustomBambooShootBlock(AbstractBlock.Settings.copy(Blocks.BAMBOO_SAPLING)
                    .nonOpaque()
                    .noCollision()
                    .breakInstantly()
                    .ticksRandomly()
                    //.sounds(BlockSoundGroup.CROP) put in custom sound
                    .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of("the_tower", "stake_bud")))
            )
    );


    public static class CustomBambooShootBlock extends BambooShootBlock {
        public CustomBambooShootBlock(AbstractBlock.Settings settings) {
            super(settings);
        }

        @Override
        protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
            if (random.nextInt(9) == 0 && world.isAir(pos.up()) && world.getBaseLightLevel(pos.up(), 0) >= 1) {
                this.grow(world, pos);
            }
        }

        @Override
        protected void grow(World world, BlockPos pos) {
            world.setBlockState(pos, stake_init.STAKE.getDefaultState()
                    .with(BambooBlock.LEAVES, BambooLeaves.NONE)
                    .with(BambooBlock.STAGE, 0)
                    .with(BambooBlock.AGE, 0), Block.NOTIFY_ALL);
            world.setBlockState(pos.up(), stake_init.STAKE.getDefaultState().with(StakeBlock.LEAVES, BambooLeaves.SMALL), Block.NOTIFY_ALL);
        }
    }

    public static void load() {
    } //to load into game








}
