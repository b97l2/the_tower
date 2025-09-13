package mia.the_tower.initialisation.dimentions;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.world.biome.Biome;

public final class CustomSkyRenderer {
    /*
    private static final Identifier SUN_TEX  = Identifier.of("the_tower", "textures/environment/sun.png");
    private static final Identifier MOON_TEX = Identifier.of("the_tower", "textures/environment/moon_custom.png");
    private static final Identifier STARS_TEX = Identifier.of("the_tower", "textures/environment/stars.png");

    // Your biome ids
    private static final Identifier BIOME_SPIKES = Identifier.of("the_tower", "spikes");
    private static final Identifier BIOME_GLOOM  = Identifier.of("the_tower", "gloom");

    public static void render(WorldRendererContext worldRenderer,
                              MatrixStack matrices,
                              float tickDelta,
                              double cameraX, double cameraY, double cameraZ) {

        MinecraftClient mc = MinecraftClient.getInstance();
        ClientWorld world = mc.world;
        if (world == null) return;

        // Figure out which biome the camera is in
        BlockPos camPos = world.getCamera().getBlockPos();
        RegistryEntry<Biome> biomeEntry = world.getBiome(camPos);
        Identifier biomeId = world.getRegistryManager()
                .get(RegistryKeys.BIOME)
                .getId(biomeEntry.value());

        // Clear/background sky dome (optional custom gradient)
        renderSkyGradient(matrices, world, tickDelta);

        // Stars (example)
        renderStars(matrices, world, tickDelta);

        // Sun/Moon per-biome
        if (BIOME_SPIKES.equals(biomeId)) {
            renderSun(matrices, world, tickDelta, 1.2f, SUN_TEX);   // Larger, harsher sun
            renderMoon(matrices, world, tickDelta, 0.8f, MOON_TEX); // Smaller moon
        } else if (BIOME_GLOOM.equals(biomeId)) {
            // No sun, faint moon only
            renderMoon(matrices, world, tickDelta, 0.5f, MOON_TEX);
        } else {
            // Default for the dimension
            renderSun(matrices, world, tickDelta, 1.0f, SUN_TEX);
            renderMoon(matrices, world, tickDelta, 1.0f, MOON_TEX);
        }

        // Optional extra: auroras, rings, cathedral arcs, etc., per-biome
        // if (BIOME_GLOOM.equals(biomeId)) renderAurora(...);
    }

    // ---- helpers (sketches; fill in with your BufferBuilder/Tessellator quads) ----

    private static void renderSkyGradient(MatrixStack matrices, ClientWorld world, float tickDelta) {
        // Draw a big dome or full-screen gradient based on world.getSkyAngle(tickDelta)
        // and biome/world fog/sky colors. You’ll use BufferBuilder to submit a triangle fan.
    }

    private static void renderStars(MatrixStack matrices, ClientWorld world, float tickDelta) {
        // Bind STARS_TEX and draw many tiny quads or a precomputed VBO;
        // scale brightness by world.getStarBrightness(tickDelta).
    }

    private static void renderSun(MatrixStack matrices, ClientWorld world, float tickDelta, float scale, Identifier tex) {
        float angle = world.getSkyAngle(tickDelta) * 360.0f;
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
        // Bind tex with RenderSystem.setShaderTexture(0, tex);
        // Draw a single camera-facing quad at far distance; size controlled by 'scale'.
        matrices.pop();
    }

    private static void renderMoon(MatrixStack matrices, ClientWorld world, float tickDelta, float scale, Identifier tex) {
        float angle = world.getSkyAngle(tickDelta) * 360.0f + 180.0f; // opposite the sun
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(angle));
        // Bind tex (your custom moon, or a moon_phases atlas you sample from)
        // Draw a quad; you can implement your own phase logic if you don't want vanilla phases.
        matrices.pop();
    }

     */
}
