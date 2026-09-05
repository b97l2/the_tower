package mia.the_tower.initialisation.world.vegetation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class SurfaceDecorativeBlockFeature extends Feature<SurfaceDecorativeBlockFeature.Config> {
    public static record Config(
            BlockStateProvider toPlace,
            int minY
    ) implements FeatureConfig {
        public static final Codec<Config> CODEC = RecordCodecBuilder.create(instance ->
                instance.group(
                        BlockStateProvider.TYPE_CODEC
                                .fieldOf("to_place")
                                .forGetter(Config::toPlace),
                        Codec.INT
                                .fieldOf("min_y")
                                .forGetter(Config::minY)
                ).apply(instance, Config::new)
        );
    }

    public SurfaceDecorativeBlockFeature() {
        super(Config.CODEC);
    }

    public static final Feature<Config> SURFACE_DECORATIVE_BLOCK =
            Registry.register(
                    Registries.FEATURE,
                    Identifier.of("the_tower", "surface_decorative_block"),
                    new SurfaceDecorativeBlockFeature()
            );

    public static void init() {}

    @Override
    public boolean generate(FeatureContext<Config> context) {
        Config config = context.getConfig();
        StructureWorldAccess world = context.getWorld();
        BlockPos pos = context.getOrigin();

        if (pos.getY() < config.minY()) {
            return false;
        }

        if (!world.getBlockState(pos).isAir()) {
            return false;
        }

        BlockPos surfacePos = pos.down();
        BlockState surfaceState = world.getBlockState(surfacePos);
        if (!surfaceState.isSideSolid(world, surfacePos, Direction.UP, SideShapeType.FULL)) {
            return false;
        }

        BlockState state = config.toPlace().get(context.getRandom(), pos);
        if (!state.canPlaceAt(world, pos)) {
            return false;
        }

        return world.setBlockState(pos, state, Block.NOTIFY_ALL);
    }
}
