package mia.the_tower.mixin.client;


import mia.the_tower.initialisation.status_effects.SanguineStatusEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow @Final private MinecraftClient client;

    //used by GameRenderer to ease FOV changes frame-to-frame
    @Shadow private float fovMultiplier;
    @Shadow private float lastFovMultiplier;

    @Inject(method = "updateFovMultiplier", at = @At("HEAD"), cancellable = true)
    private void modid$freezeSpeedFovOnOurEffect(CallbackInfo ci) {
        Entity cam = this.client.getCameraEntity();
        if (!(cam instanceof LivingEntity living)) return;

        boolean hasOurSpeedEffect = living.hasStatusEffect(SanguineStatusEffect.SANGUINE);

        if (hasOurSpeedEffect) {
            // Smoothly converge FOV multiplier to 1.0 (neutral: no speed-based FOV change)
            this.lastFovMultiplier = this.fovMultiplier;
            this.fovMultiplier += (1.0F - this.fovMultiplier) * 0.5F;
            ci.cancel(); // Skip vanilla’s computation (which reads movement speed)
        }
    }
}