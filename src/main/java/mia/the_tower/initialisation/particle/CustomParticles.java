package mia.the_tower.initialisation.particle;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class CustomParticles {

    //to make a new particle, create a new class in the client portion of the mod, under mia.the_tower, defining the behaviour
    //then load it here
    //then put it in the client initialiser
    //then give it a json file
    //then add a texture

    public static final SimpleParticleType EMBERS_OF_THE_FRAGE =
            registerParticle("embers_of_the_frage", FabricParticleTypes.simple());

    public static final SimpleParticleType UNDERBLOOD =
            registerParticle("underblood", FabricParticleTypes.simple());

    public static final SimpleParticleType GOLD_INCENSE =
            registerParticle("gold_incense", FabricParticleTypes.simple());

    public static final SimpleParticleType GLUH_PARTICLE =
            registerParticle("gluh_particle", FabricParticleTypes.simple());

    public static final SimpleParticleType STAVE_PARTICLE =
            registerParticle("stave_particle", FabricParticleTypes.simple());

    public static final SimpleParticleType WHITE_INCENSE =
            registerParticle("white_incense", FabricParticleTypes.simple());

    public static final SimpleParticleType GOLD_ORB =
            registerParticle("gold_orb_particle", FabricParticleTypes.simple());

    private static SimpleParticleType registerParticle(String name, SimpleParticleType particleType) {
        return Registry.register(Registries.PARTICLE_TYPE, Identifier.of("the_tower", name), particleType);
    }

    public static void load() {}
}
