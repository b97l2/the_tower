package mia.the_tower.initialisation.items;

import mia.the_tower.initialisation.fluid.FluidLogging;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class FluidloggingBucketItem extends BucketItem {
    private final Fluid fluid;

    public FluidloggingBucketItem(Fluid fluid, Settings settings) {
        super(fluid, settings);
        this.fluid = fluid;
    }

    @Override
    public boolean placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult) {
        if (this.fluid instanceof FlowableFluid flowableFluid) {
            if (this.tryFluidlog(player, world, pos, flowableFluid)) {
                return true;
            }

            if (hitResult != null) {
                BlockPos clickedPos = hitResult.getBlockPos();
                if (!clickedPos.equals(pos) && this.tryFluidlog(player, world, clickedPos, flowableFluid)) {
                    return true;
                }

                if (clickedPos.equals(pos) && FluidLogging.getSlot(this.fluid) != FluidLogging.Slot.NONE) {
                    BlockState clickedState = world.getBlockState(clickedPos);
                    if (clickedState.getBlock() instanceof FluidFillable) {
                        return false;
                    }
                }
            }
        }

        return super.placeFluid(player, world, pos, hitResult);
    }

    private boolean tryFluidlog(@Nullable PlayerEntity player, World world, BlockPos pos, FlowableFluid flowableFluid) {
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() instanceof FluidFillable fluidFillable
                && fluidFillable.canFillWithFluid(player, world, pos, state, this.fluid)
                && fluidFillable.tryFillWithFluid(world, pos, state, flowableFluid.getStill(false))) {
            this.playEmptyingSound(player, world, pos);
            return true;
        }

        return false;
    }
}
