package mia.the_tower.initialisation.entity.custom;

import mia.the_tower.initialisation.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class PlateEntity extends ItemFrameEntity {
    public PlateEntity(EntityType<? extends ItemFrameEntity> type, World world) {
        super(type, world);
    }
    public PlateEntity(World world, BlockPos pos, Direction side) {
        super(ModEntities.PLATE, world, pos, side); // your registry constant
    }

    // Optional: change physical size in pixels if you want a different footprint
    // @Override public int getWidthPixels() { return 16; }
    // @Override public int getHeightPixels() { return 16; }
}