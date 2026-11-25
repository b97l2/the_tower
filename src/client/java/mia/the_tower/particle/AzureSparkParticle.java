package mia.the_tower.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;


public class AzureSparkParticle extends SpriteBillboardParticle {
    private static final int LIFETIME_TICKS = 4; // 0.2 seconds at 20 ticks per second
    private final SpriteProvider sprites;


    public AzureSparkParticle(ClientWorld world, double x, double y, double z,
                              double vx, double vy, double vz, SpriteProvider sprites) {
        super(world, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.scale *= this.random.nextFloat() * 0.6F;
        this.maxAge = LIFETIME_TICKS;
        this.velocityMultiplier = 0.1f;
        this.gravityStrength = 0.0f;
        this.collidesWithWorld = false;
        this.velocityX = vx;
        this.velocityY = vy;
        this.velocityZ = vz;
        //this.setSpriteForAge(sprites);
    }

@Override
    public ParticleTextureSheet getType(){
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
}

/*
    @Override
    public void tick() {
        super.tick();                  // increments age
        this.setSpriteForAge(sprites); // updates frame based on age/maxAge
    }

 */

    @Override
    public void tick() {
        // DO NOT call super.tick(); it applies friction/gravity.
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;

        if (this.age++ >= this.maxAge) {
            this.markDead();
            return;
        }

        // Move exactly by the current velocity (no damping)
        this.move(this.velocityX, this.velocityY, this.velocityZ);
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

            AzureSparkParticle particle = new AzureSparkParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
            particle.setSprite(spriteProvider.getSprite(world.random));
            return particle;
        }
    }



}
