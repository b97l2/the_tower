package mia.the_tower.initialisation.fluid;

import mia.the_tower.initialisation.status_effects.DecayStatusEffect;
import net.minecraft.block.FluidBlock;
import net.minecraft.block.BlockState;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.HashMap;
import java.util.Map;

public class GunkFluidBlock extends FluidBlock {

    // Map to track time spent inside the fluid by each player
    public static final Map<PlayerEntity, Integer> playerTimeInside = new HashMap<>();
    public static final Map<PlayerEntity, Integer> playerTimeOutside = new HashMap<>();

    public GunkFluidBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, net.minecraft.entity.Entity entity) {
        if (entity instanceof PlayerEntity player) {

            playerTimeOutside.put(player, 0);

            // Check if the player is inside the fluid
            if (world instanceof ServerWorld) {
                ServerWorld serverWorld = (ServerWorld) world;

                // Track time spent inside the fluid
                int timeInside = playerTimeInside.getOrDefault(player, 0) + 1;
                playerTimeInside.put(player, timeInside);

                // Apply status effect every 80 ticks
                if (timeInside % 400 == 0) {
                    int level = Math.min(timeInside / 400, 5); // Max level 5
                    int duration = level * 200;

                    // Apply or update the status effect
                    player.addStatusEffect(new StatusEffectInstance(DecayStatusEffect.DECAY, duration, level - 1, true, true));

                }
            }
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        this.spawnBreakParticles(world, player, pos, state);

        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
        playerTimeInside.clear();
        return state;
    }


}
