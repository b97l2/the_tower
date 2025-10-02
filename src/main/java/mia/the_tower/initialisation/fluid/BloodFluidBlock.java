package mia.the_tower.initialisation.fluid;

import mia.the_tower.initialisation.status_effects.DecayStatusEffect;
import mia.the_tower.initialisation.status_effects.SanguineStatusEffect;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.HashMap;
import java.util.Map;

public class BloodFluidBlock extends FluidBlock {


    public BloodFluidBlock(FlowableFluid fluid, Settings settings) {
        super(fluid, settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, net.minecraft.entity.Entity entity) {
        if (entity instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(SanguineStatusEffect.SANGUINE, 60*20, 0, true, true));
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        this.spawnBreakParticles(world, player, pos, state);
        world.emitGameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Emitter.of(player, state));
        return state;
    }


}
