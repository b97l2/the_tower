package mia.the_tower.mixin.client;

import mia.the_tower.initialisation.dimentions.TheFrage;
import mia.the_tower.initialisation.weather.FrageWeatherClientState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(World.class)
public abstract class WorldMixin {
    @Inject(method = "getRainGradient", at = @At("RETURN"), cancellable = true)
    private void theTower$useFrageOvercastRainGradient(float delta, CallbackInfoReturnable<Float> cir) {
        World world = (World) (Object) this;
        if (!world.getRegistryKey().equals(TheFrage.THE_FRAGE_LEVEL_KEY)) {
            return;
        }

        cir.setReturnValue(Math.max(cir.getReturnValue(), FrageWeatherClientState.getOvercastStrength(delta)));
    }

    @Inject(method = "getThunderGradient", at = @At("RETURN"), cancellable = true)
    private void theTower$useFrageOvercastThunderGradient(float delta, CallbackInfoReturnable<Float> cir) {
        World world = (World) (Object) this;
        if (!world.getRegistryKey().equals(TheFrage.THE_FRAGE_LEVEL_KEY)) {
            return;
        }

        float overcastStrength = FrageWeatherClientState.getOvercastStrength(delta);
        cir.setReturnValue(MathHelper.clamp(Math.max(cir.getReturnValue(), overcastStrength), 0.0F, 1.0F));
    }
}
