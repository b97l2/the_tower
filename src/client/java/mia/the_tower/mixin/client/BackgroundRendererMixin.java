package mia.the_tower.mixin.client;

import mia.the_tower.initialisation.fluid.ModFluidEffects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.math.BlockPos;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(BackgroundRenderer.class)
public abstract class BackgroundRendererMixin {
    @Inject(method = "getFogColor", at = @At("RETURN"), cancellable = true)
    private static void theTower$replaceCustomFluidFogColor(Camera camera, float tickDelta, ClientWorld world,
                                                            int clampedViewDistance, float skyDarkness,
                                                            CallbackInfoReturnable<Vector4f> cir) {
        if (camera.getSubmersionType() != CameraSubmersionType.WATER) {
            return;
        }

        FluidState fluidState = world.getFluidState(BlockPos.ofFloored(camera.getPos()));
        ModFluidEffects.Type fluidType = ModFluidEffects.getType(fluidState);
        if (fluidType == null) {
            return;
        }

        float darkness = Math.max(0.0F, 1.0F - skyDarkness * 0.35F);
        Vector4f fogColor = switch (fluidType) {
            case BLOOD -> new Vector4f(0.46F * darkness, 0.024F * darkness, 0.020F * darkness, 1.0F);
            case GUNK -> new Vector4f(0.020F * darkness, 0.018F * darkness, 0.016F * darkness, 1.0F);
        };

        cir.setReturnValue(fogColor);
    }
}
