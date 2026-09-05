package mia.the_tower.initialisation.world.structure;

import mia.the_tower.The_Tower;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.StructureSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.structure.Structure;

import java.util.Map;

public class ModStructureData {
    public static final RegistryKey<Structure> LARGE_GINKGO_TREE =
            RegistryKey.of(RegistryKeys.STRUCTURE, The_Tower.id("large_ginkgo_tree"));
    public static final RegistryKey<StructureSet> LARGE_GINKGO_TREES =
            RegistryKey.of(RegistryKeys.STRUCTURE_SET, The_Tower.id("large_ginkgo_trees"));
    public static final TagKey<Biome> HAS_LARGE_GINKGO_TREE =
            TagKey.of(RegistryKeys.BIOME, The_Tower.id("has_structure/large_ginkgo_tree"));

    public static void bootstrapStructures(Registerable<Structure> context) {
        var biomes = context.getRegistryLookup(RegistryKeys.BIOME);
        context.register(
                LARGE_GINKGO_TREE,
                new LargeGinkgoTreeStructure(new Structure.Config(
                        biomes.getOrThrow(HAS_LARGE_GINKGO_TREE),
                        Map.of(),
                        GenerationStep.Feature.SURFACE_STRUCTURES,
                        StructureTerrainAdaptation.NONE
                ))
        );
    }

    public static void bootstrapStructureSets(Registerable<StructureSet> context) {
        var structures = context.getRegistryLookup(RegistryKeys.STRUCTURE);
        context.register(
                LARGE_GINKGO_TREES,
                new StructureSet(
                        structures.getOrThrow(LARGE_GINKGO_TREE),
                        new RandomSpreadStructurePlacement(11, 9, SpreadType.LINEAR, 19402154)
                )
        );
    }
}
