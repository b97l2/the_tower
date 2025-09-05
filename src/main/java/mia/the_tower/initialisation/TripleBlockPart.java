package mia.the_tower.initialisation;

import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;


import net.minecraft.util.StringIdentifiable;

public enum TripleBlockPart implements StringIdentifiable {
    LOWER("lower"),
    MIDDLE("middle"),
    UPPER("upper");

    private final String name;

    TripleBlockPart(String name) {
        this.name = name;
    }

    @Override
    public String asString() {
        return this.name;
    }
}