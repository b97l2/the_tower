package mia.the_tower.mixin;


import mia.the_tower.initialisation.block_init;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    public abstract World getWorld();
    @Shadow public abstract Box getBoundingBox();
    @Shadow public abstract void setVelocity(Vec3d velocity);
    @Shadow public abstract Vec3d getVelocity();
    @Shadow protected abstract boolean updateMovementInFluid(TagKey<Fluid> tag, double speed);

    @Inject(method = "baseTick", at = @At("HEAD"))
    private void onBaseTick(CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        if (!self.getWorld().isClient) {
            FluidState fluidState = self.getWorld().getFluidState(self.getBlockPos());
            if (fluidState.isOf(block_init.STILL_GUNK)) {
                // Example: Resistance
                Vec3d velocity = self.getVelocity();
                self.setVelocity(velocity.multiply(0.5, 0.5, 0.5));
            }
        }
    }
}