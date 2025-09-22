package mia.the_tower.particle;

import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import org.jetbrains.annotations.Nullable;


public class GluhParticle extends SpriteBillboardParticle {
    private final SpriteProvider sprites;


    public GluhParticle(ClientWorld world, double x, double y, double z,
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



    public static class Factory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public Factory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z,
                                       double velocityX, double velocityY, double velocityZ) {

            GluhParticle particle = new GluhParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.spriteProvider);
            particle.setSprite(spriteProvider.getSprite(world.random));
            return particle;
        }
    }



}
