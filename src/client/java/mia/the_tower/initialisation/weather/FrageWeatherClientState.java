package mia.the_tower.initialisation.weather;

import net.minecraft.util.math.MathHelper;

public final class FrageWeatherClientState {
    private static final float TRANSITION_STEP = 0.04F;

    private static FrageWeatherPattern pattern = FrageWeatherPattern.OVERCAST;
    private static float overcastStrength = 1.0F;
    private static float previousOvercastStrength = 1.0F;
    private static float targetOvercastStrength = 1.0F;

    private FrageWeatherClientState() {
    }

    public static void apply(FrageWeatherSyncPayload payload) {
        pattern = payload.pattern();
        targetOvercastStrength = MathHelper.clamp(payload.overcastStrength(), 0.0F, 1.0F);
    }

    public static void tick() {
        previousOvercastStrength = overcastStrength;
        overcastStrength = MathHelper.clamp(
                overcastStrength + MathHelper.clamp(targetOvercastStrength - overcastStrength, -TRANSITION_STEP, TRANSITION_STEP),
                0.0F,
                1.0F
        );
    }

    public static void reset() {
        pattern = FrageWeatherPattern.OVERCAST;
        overcastStrength = 1.0F;
        previousOvercastStrength = 1.0F;
        targetOvercastStrength = 1.0F;
    }

    public static float getOvercastStrength(float tickDelta) {
        if (pattern != FrageWeatherPattern.OVERCAST) {
            return 0.0F;
        }

        return MathHelper.lerp(tickDelta, previousOvercastStrength, overcastStrength);
    }
}
