package mia.the_tower.initialisation.world.tree;

import mia.the_tower.The_Tower;
import mia.the_tower.initialisation.world.ModConfiguredFeatures;
import net.minecraft.block.SaplingGenerator;

import java.util.Optional;

public class ModSaplingGenerators {
    public static final SaplingGenerator GINKGO = new SaplingGenerator(The_Tower.MOD_ID + ":ginkgo",
            Optional.empty(), Optional.of(ModConfiguredFeatures.GINKGO_KEY), Optional.empty());

    public static final SaplingGenerator VAULTS_STANCHION = new SaplingGenerator(The_Tower.MOD_ID + ":vaults_stanchion",
            Optional.empty(), Optional.of(ModConfiguredFeatures.VAULTS_STANCHION_KEY), Optional.empty());
}
