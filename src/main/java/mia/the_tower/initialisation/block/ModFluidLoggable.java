package mia.the_tower.initialisation.block;

import mia.the_tower.initialisation.fluid.FluidLogging;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface ModFluidLoggable extends Waterloggable {
    @Override
    default boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
        return FluidLogging.canFillWithFluid(player, world, pos, state, fluid);
    }

    @Override
    default boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
        return FluidLogging.tryFillWithFluid(world, pos, state, fluidState);
    }

    @Override
    default ItemStack tryDrainFluid(@Nullable PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state) {
        return FluidLogging.tryDrainFluid(world, pos, state);
    }

    @Override
    default Optional<SoundEvent> getBucketFillSound() {
        return FluidLogging.getBucketFillSound();
    }
}
