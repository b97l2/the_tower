package mia.the_tower.initialisation.fluid;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;

public abstract class BaseFluid extends FlowableFluid {
        @Override public boolean matchesType(Fluid fluid) { return fluid == getStill() || fluid == getFlowing(); }
        @Override protected boolean isInfinite(ServerWorld world) { return false; }
        @Override protected void beforeBreakingBlock(WorldAccess world, BlockPos pos, BlockState state) { /* drop stacks */ }
        @Override protected boolean canBeReplacedWith(FluidState state, BlockView view, BlockPos pos, Fluid fluid, Direction dir) { return true; }
        //@Override protected int getFlowSpeed(WorldView world) { return 4; }
        @Override protected float getBlastResistance() { return 100.0F; }
    }
