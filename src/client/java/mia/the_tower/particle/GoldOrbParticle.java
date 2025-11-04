package mia.the_tower.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;


public class GoldOrbParticle extends SpriteBillboardParticle {
    private final SpriteProvider sprites;


    public GoldOrbParticle(ClientWorld world, double x, double y, double z,
                           double vx, double vy, double vz, SpriteProvider sprites) {
        super(world, x, y, z, vx, vy, vz);
        this.sprites = sprites;
        this.scale *= this.random.nextFloat() * 0.6F + 1.5F;
        this.maxAge = (int)((double)160.0F / (Math.random() * 0.8 + 0.2));
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

            GoldOrbParticle particle = new GoldOrbParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
            particle.setSprite(spriteProvider.getSprite(world.random));
            return particle;
        }
    }



}
