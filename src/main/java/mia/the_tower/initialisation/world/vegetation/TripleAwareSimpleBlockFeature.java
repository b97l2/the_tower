package mia.the_tower.initialisation.world.vegetation;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import mia.the_tower.initialisation.block.TripleBlockPart;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class TripleAwareSimpleBlockFeature extends Feature<SimpleBlockFeatureConfig> {
    public TripleAwareSimpleBlockFeature(Codec<SimpleBlockFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<SimpleBlockFeatureConfig> ctx) {
        ServerWorldAccess world = ctx.getWorld();
        BlockPos pos = ctx.getOrigin();
        Random random = ctx.getRandom();
        SimpleBlockFeatureConfig cfg = ctx.getConfig();

        BlockState state = cfg.toPlace().get(random, pos);

        EnumProperty<TripleBlockPart> third = findThirdProperty(state);
        if (third != null) {
            // bottom/middle/top; fail if we can't fit
            if (pos.getY() + 2 > world.getTopYInclusive()) return false;
            if (!world.getBlockState(pos).isAir()
                    || !world.getBlockState(pos.up()).isAir()
                    || !world.getBlockState(pos.up(2)).isAir()) return false;

            BlockState lower  = state.with(third, TripleBlockPart.LOWER);
            BlockState middle = state.with(third, TripleBlockPart.MIDDLE);
            BlockState upper  = state.with(third, TripleBlockPart.UPPER);

            world.setBlockState(pos,        lower,  Block.NOTIFY_ALL);
            world.setBlockState(pos.up(),   middle, Block.NOTIFY_ALL);
            world.setBlockState(pos.up(2),  upper,  Block.NOTIFY_ALL);
            return true;
        }

        // Fallback: behave like SIMPLE_BLOCK
        if (!state.canPlaceAt(world, pos)) return false;
        return world.setBlockState(pos, state, Block.NOTIFY_ALL);
    }

    @SuppressWarnings("unchecked")
    private static EnumProperty<TripleBlockPart> findThirdProperty(BlockState state) {
        for (Property<?> p : state.getBlock().getStateManager().getProperties()) {
            if (p instanceof EnumProperty<?> && "third".equals(p.getName())) {
                try { return (EnumProperty<TripleBlockPart>) p; } catch (ClassCastException ignored) {}
            }
        }
        return null;
    }

    public static final Feature<SimpleBlockFeatureConfig> TRIPLE_AWARE_SIMPLE_BLOCK =
            Registry.register(Registries.FEATURE,
                    Identifier.of("the_tower", "triple_aware_simple_block"),
                    new TripleAwareSimpleBlockFeature(SimpleBlockFeatureConfig.CODEC));

    public static void init() {}

}
