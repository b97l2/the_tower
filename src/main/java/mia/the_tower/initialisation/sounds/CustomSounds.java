package mia.the_tower.initialisation.sounds;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class CustomSounds {
    //a line here for each sound
    //a line below for each sound group
    //an entry in the sounds.json for each group and individual sound
    //all sounds in ogg format in the sounds folder
    //then give the chosen thing your sound group
    public static final SoundEvent SCREAMING_BLOCK_BREAK = registerSoundEvent("screaming_block_break");
    public static final SoundEvent SCREAMING_BLOCK_STEP = registerSoundEvent("screaming_block_step");
    public static final SoundEvent SCREAMING_BLOCK_PLACE = registerSoundEvent("screaming_block_place");
    public static final SoundEvent SCREAMING_BLOCK_HIT = registerSoundEvent("screaming_block_hit");
    public static final SoundEvent SCREAMING_BLOCK_FALL = registerSoundEvent("screaming_block_fall");
//coresponding sound group
    public static final BlockSoundGroup SCREAMING_BLOCK = new BlockSoundGroup(1f, 1f,
            SCREAMING_BLOCK_BREAK, SCREAMING_BLOCK_STEP, SCREAMING_BLOCK_PLACE, SCREAMING_BLOCK_HIT, SCREAMING_BLOCK_FALL);

    public static final SoundEvent BLAJ_USE = registerSoundEvent("blaj_use");

    public static final SoundEvent BLAJ_BLOCK_BREAK = registerSoundEvent("blaj_block_break");
    public static final SoundEvent BLAJ_BLOCK_STEP = registerSoundEvent("blaj_block_step");
    public static final SoundEvent BLAJ_BLOCK_PLACE = registerSoundEvent("blaj_block_place");
    public static final SoundEvent BLAJ_BLOCK_HIT = registerSoundEvent("blaj_block_hit");
    public static final SoundEvent BLAJ_BLOCK_FALL = registerSoundEvent("blaj_block_fall");
    //coresponding sound group
    public static final BlockSoundGroup BLAJ_BLOCK = new BlockSoundGroup(1f, 1f,
            BLAJ_BLOCK_BREAK, BLAJ_BLOCK_STEP, BLAJ_BLOCK_PLACE, BLAJ_BLOCK_HIT, BLAJ_BLOCK_FALL);

    public static final SoundEvent SHEPHERDS_STAFF_USE = registerSoundEvent("shepherds_staff_use");




    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of("the_tower", name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void load() {}

}
