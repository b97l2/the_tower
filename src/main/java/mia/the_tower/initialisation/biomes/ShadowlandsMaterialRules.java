package mia.the_tower.initialisation.biomes;

import mia.the_tower.initialisation.block_init;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.surfacebuilder.MaterialRules;

public class ShadowlandsMaterialRules {
    private static final MaterialRules.MaterialRule DIRT = makeStateRule(Blocks.DIRT);
    private static final MaterialRules.MaterialRule GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
    private static final MaterialRules.MaterialRule INFESTED_ROCK = makeStateRule(block_init.INFESTED_ROCK);
    private static final MaterialRules.MaterialRule CUBE_OF_FLESH = makeStateRule(block_init.CUBE_OF_FLESH);

    public static MaterialRules.MaterialRule makeRules() {
        MaterialRules.MaterialCondition isAtOrAboveWaterLevel = MaterialRules.water(-1, 0);

        MaterialRules.MaterialRule grassSurface = MaterialRules.sequence(MaterialRules.condition(isAtOrAboveWaterLevel, GRASS_BLOCK), DIRT);

        return MaterialRules.sequence(
                MaterialRules.sequence(MaterialRules.condition(MaterialRules.biome(Shadowlands.SHADOWLANDS),
                                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, INFESTED_ROCK)),
                        MaterialRules.condition(MaterialRules.STONE_DEPTH_CEILING, CUBE_OF_FLESH)),

                // Default to a grass and dirt surface
                MaterialRules.condition(MaterialRules.STONE_DEPTH_FLOOR, grassSurface)
        );
    }

    private static MaterialRules.MaterialRule makeStateRule(Block block) {
        return MaterialRules.block(block.getDefaultState());
    }
}