package mia.the_tower.initialisation.world.structure;

import com.mojang.serialization.MapCodec;
import mia.the_tower.The_Tower;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureType;

public class ModStructures {
    public static final StructureType<LargeGinkgoTreeStructure> LARGE_GINKGO_TREE =
            registerStructureType("large_ginkgo_tree", LargeGinkgoTreeStructure.CODEC);

    public static final StructurePieceType LARGE_GINKGO_TREE_PIECE =
            Registry.register(
                    Registries.STRUCTURE_PIECE,
                    The_Tower.id("large_ginkgo_tree_piece"),
                    (StructurePieceType.Simple) LargeGinkgoTreePiece::new
            );

    public static void init() {}

    private static <S extends Structure> StructureType<S> registerStructureType(String name, MapCodec<S> codec) {
        return Registry.register(Registries.STRUCTURE_TYPE, The_Tower.id(name), () -> codec);
    }
}
