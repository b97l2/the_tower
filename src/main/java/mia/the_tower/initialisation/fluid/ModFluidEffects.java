package mia.the_tower.initialisation.fluid;

import mia.the_tower.initialisation.particle.CustomParticles;
import mia.the_tower.initialisation.sounds.CustomSounds;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class ModFluidEffects {
    public enum Type {
        BLOOD,
        GUNK
    }

    public static Type getType(Entity entity) {
        World world = entity.getWorld();
        Box box = entity.getBoundingBox().contract(0.001);
        int minX = MathHelper.floor(box.minX);
        int maxX = MathHelper.ceil(box.maxX);
        int minY = MathHelper.floor(box.minY);
        int maxY = MathHelper.ceil(box.maxY);
        int minZ = MathHelper.floor(box.minZ);
        int maxZ = MathHelper.ceil(box.maxZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for (int x = minX; x < maxX; x++) {
            for (int y = minY; y < maxY; y++) {
                for (int z = minZ; z < maxZ; z++) {
                    mutable.set(x, y, z);
                    FluidState fluidState = world.getFluidState(mutable);
                    Type type = getType(fluidState);
                    if (type != null && y + fluidState.getHeight(world, mutable) >= box.minY) {
                        return type;
                    }
                }
            }
        }

        return null;
    }

    public static Type getType(FluidState state) {
        if (fluid_init.STILL_BLOOD.matchesType(state.getFluid())) {
            return Type.BLOOD;
        }
        if (fluid_init.STILL_GUNK.matchesType(state.getFluid())) {
            return Type.GUNK;
        }
        return null;
    }

    public static ParticleEffect getSuspendedParticle(Type type) {
        return switch (type) {
            case BLOOD -> CustomParticles.UNDERBLOOD;
            case GUNK -> ParticleTypes.ASH;
        };
    }

    public static SoundEvent getSplashSound(Type type) {
        return switch (type) {
            case BLOOD -> CustomSounds.BLOOD_SPLASH;
            case GUNK -> CustomSounds.GUNK_SPLASH;
        };
    }

    public static SoundEvent getHighSpeedSplashSound(Type type) {
        return switch (type) {
            case BLOOD -> CustomSounds.BLOOD_SPLASH_HIGH_SPEED;
            case GUNK -> CustomSounds.GUNK_SPLASH_HIGH_SPEED;
        };
    }

    public static SoundEvent getSwimSound(Type type) {
        return switch (type) {
            case BLOOD -> CustomSounds.BLOOD_SWIM;
            case GUNK -> CustomSounds.GUNK_SWIM;
        };
    }
}
