package mia.the_tower.initialisation.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class CustomSounds {

    public static final SoundEvent SCREAMING_BLOCK_BREAK = registerSoundEvent("screaming_block_break");
    public static final SoundEvent SCREAMING_BLOCK_STEP = registerSoundEvent("screaming_block_step");
    public static final SoundEvent SCREAMING_BLOCK_PLACE = registerSoundEvent("screaming_block_place");
    public static final SoundEvent SCREAMING_BLOCK_HIT = registerSoundEvent("screaming_block_hit");
    public static final SoundEvent SCREAMING_BLOCK_FALL = registerSoundEvent("screaming_block_fall");

    public static final BlockSoundGroup SCREAMING_BLOCK = new BlockSoundGroup(1f, 1f,
            SCREAMING_BLOCK_BREAK, SCREAMING_BLOCK_STEP, SCREAMING_BLOCK_PLACE, SCREAMING_BLOCK_HIT, SCREAMING_BLOCK_FALL);


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of("the_tower", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void load() {}

}
