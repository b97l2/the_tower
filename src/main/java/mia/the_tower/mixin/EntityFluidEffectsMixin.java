package mia.the_tower.mixin;

import mia.the_tower.initialisation.fluid.ModFluidEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityFluidEffectsMixin {
    @Shadow
    @Final
    protected Random random;

    @Shadow
    @Nullable
    public abstract LivingEntity getControllingPassenger();

    @Inject(method = "onSwimmingStart", at = @At("HEAD"), cancellable = true)
    private void theTower$replaceModFluidSplashEffects(CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        ModFluidEffects.Type fluidType = ModFluidEffects.getType(self);
        if (fluidType == null) {
            return;
        }

        Entity soundSource = Objects.requireNonNullElse(this.getControllingPassenger(), self);
        Vec3d velocity = soundSource.getVelocity();
        float sourceScale = soundSource == self ? 0.2F : 0.9F;
        float volume = Math.min(1.0F, (float)Math.sqrt(velocity.x * velocity.x * 0.2F + velocity.y * velocity.y + velocity.z * velocity.z * 0.2F) * sourceScale);
        SoundEvent sound = volume < 0.25F ? ModFluidEffects.getSplashSound(fluidType) : ModFluidEffects.getHighSpeedSplashSound(fluidType);
        self.playSound(sound, volume, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);

        float splashY = MathHelper.floor(self.getY());
        int particleCount = (int)(1.0F + self.getWidth() * 20.0F);
        for (int i = 0; i < particleCount; i++) {
            double xOffset = (this.random.nextDouble() * 2.0 - 1.0) * self.getWidth();
            double zOffset = (this.random.nextDouble() * 2.0 - 1.0) * self.getWidth();
            self.getWorld().addParticle(
                    ModFluidEffects.getSuspendedParticle(fluidType),
                    self.getX() + xOffset,
                    splashY + 1.0F,
                    self.getZ() + zOffset,
                    velocity.x,
                    velocity.y,
                    velocity.z
            );
        }

        self.emitGameEvent(GameEvent.SPLASH);
        ci.cancel();
    }

    @Inject(method = "playSwimSound(F)V", at = @At("HEAD"), cancellable = true)
    private void theTower$replaceModFluidSwimSound(float volume, CallbackInfo ci) {
        Entity self = (Entity)(Object)this;
        ModFluidEffects.Type fluidType = ModFluidEffects.getType(self);
        if (fluidType == null) {
            return;
        }

        self.playSound(ModFluidEffects.getSwimSound(fluidType), volume, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
        ci.cancel();
    }
}
