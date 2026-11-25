package mia.the_tower.particle;

import mia.the_tower.initialisation.particle.CustomParticles;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

/**
 * Vanilla-style block leak particle used by all three ichor phases.
 */
public class DrippingIchor extends SpriteBillboardParticle {
    private DrippingIchor(ClientWorld world, double x, double y, double z) {
        super(world, x, y, z);
        this.setBoundingBoxSpacing(0.01F, 0.01F);
        this.gravityStrength = 0.06F;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        this.updateAge();

        if (!this.dead) {
            this.velocityY -= this.gravityStrength;
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.updateVelocity();

            if (!this.dead) {
                this.velocityX *= 0.98F;
                this.velocityY *= 0.98F;
                this.velocityZ *= 0.98F;
            }
        }
    }

    protected void updateAge() {
        if (this.maxAge-- <= 0) {
            this.markDead();
        }
    }

    protected void updateVelocity() {
    }

    private static class Dripping extends DrippingIchor {
        private final ParticleEffect nextParticle;

        private Dripping(ClientWorld world, double x, double y, double z, ParticleEffect nextParticle) {
            super(world, x, y, z);
            this.nextParticle = nextParticle;
            this.gravityStrength *= 0.02F;
            this.maxAge = 40;
        }

        @Override
        protected void updateAge() {
            if (this.maxAge-- <= 0) {
                this.markDead();
                this.world.addParticle(
                        this.nextParticle,
                        this.x,
                        this.y,
                        this.z,
                        this.velocityX,
                        this.velocityY,
                        this.velocityZ
                );
            }
        }

        @Override
        protected void updateVelocity() {
            this.velocityX *= 0.02;
            this.velocityY *= 0.02;
            this.velocityZ *= 0.02;
        }
    }

    private static class Falling extends DrippingIchor {
        private final ParticleEffect nextParticle;

        private Falling(ClientWorld world, double x, double y, double z, ParticleEffect nextParticle) {
            super(world, x, y, z);
            this.nextParticle = nextParticle;
            this.maxAge = (int) (64.0 / (Math.random() * 0.8 + 0.2));
        }

        @Override
        protected void updateVelocity() {
            if (this.onGround) {
                this.markDead();
                this.world.addParticle(this.nextParticle, this.x, this.y, this.z, 0.0, 0.0, 0.0);
            }
        }
    }

    private static class Landing extends DrippingIchor {
        private Landing(ClientWorld world, double x, double y, double z) {
            super(world, x, y, z);
            this.maxAge = (int) (16.0 / (Math.random() * 0.8 + 0.2));
        }
    }

    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            DrippingIchor particle = new Dripping(world, x, y, z, CustomParticles.FALLING_ICHOR);
            particle.setSprite(this.spriteProvider.getSprite(world.random));
            return particle;
        }
    }

    public static class FallingFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public FallingFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            DrippingIchor particle = new Falling(world, x, y, z, CustomParticles.LANDING_ICHOR);
            particle.setSprite(this.spriteProvider.getSprite(world.random));
            return particle;
        }
    }

    public static class LandingFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public LandingFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            DrippingIchor particle = new Landing(world, x, y, z);
            particle.setSprite(this.spriteProvider.getSprite(world.random));
            return particle;
        }
    }
}
