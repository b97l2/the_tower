package mia.the_tower.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;


public class Underblood extends SpriteBillboardParticle {

    public Underblood(ClientWorld world, double x, double y, double z,
                      double vx, double vy, double vz, SpriteProvider sprites) {
        super(world, x, y - (double)0.125F, z);
        this.setBoundingBoxSpacing(0.01F, 0.01F);
        this.scale *= this.random.nextFloat() * 0.6F + 0.2F;
        this.maxAge = (int)((double)16.0F / (Math.random() * 0.8 + 0.2));
        this.collidesWithWorld = false;
        this.velocityMultiplier = 1.0F;
        this.gravityStrength = 0.0F;
        this.setSpriteForAge(sprites);
    }

@Override
    public ParticleTextureSheet getType(){
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
}

//below is for animating the sprite
/*
    @Override
    public void tick() {
        super.tick();                  // increments age
        this.setSpriteForAge(sprites); // updates frame based on age/maxAge
    }

 */


    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {
            return new Underblood(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
        }
    }



}
