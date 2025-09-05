package mia.the_tower.initialisation.dimentions;

import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.DimensionTypes;

import java.util.OptionalLong;

public class TheFrage {
    public static final RegistryKey<DimensionOptions> THE_FRAGE_KEY = RegistryKey.of(RegistryKeys.DIMENSION,
            Identifier.of("the_tower", "the_frage"));
    public static final RegistryKey<World> THE_FRAGE_LEVEL_KEY = RegistryKey.of(RegistryKeys.WORLD,
            Identifier.of("the_tower", "the_frage"));
    public static final RegistryKey<DimensionType> THE_FRAGE_TYPE = RegistryKey.of(RegistryKeys.DIMENSION_TYPE,
            Identifier.of("the_tower", "the_frage_type"));

    public static void bootstrapType(Registerable<DimensionType> context) {
        context.register(THE_FRAGE_TYPE, new DimensionType(
                OptionalLong.of(12000), // fixedTime
                false, // hasSkylight
                false, // hasCeiling
                false, // ultraWarm
                true, // natural
                1.0, // coordinateScale
                true, // bedWorks
                true, // respawnAnchorWorks
                -160, // minY
                640, // height
                640, // logicalHeight
                BlockTags.INFINIBURN_OVERWORLD, // infiniburn
                DimensionTypes.OVERWORLD_ID, // effectsLocation
                2.0f, // ambientLight
                new DimensionType.MonsterSettings(false, false, UniformIntProvider.create(0, 0), 0)));
    }

    public static void load(){}

}
