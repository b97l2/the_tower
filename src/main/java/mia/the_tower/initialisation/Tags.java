package mia.the_tower.initialisation;

import mia.the_tower.The_Tower;
import net.minecraft.fluid.Fluid;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class Tags {
        public static final TagKey<Fluid> IS_GUNK = TagKey.of(RegistryKeys.FLUID, The_Tower.id("is_gunk"));
    }

