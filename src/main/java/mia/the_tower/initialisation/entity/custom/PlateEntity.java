package mia.the_tower.initialisation.entity.custom;

import mia.the_tower.initialisation.item_init;
import mia.the_tower.initialisation.entity.ModEntities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.item.ItemStack;
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

    @Override
    protected void setFacing(Direction facing) {
        super.setFacing(Direction.UP);
    }

    @Override
    public boolean canStayAttached() {
        return this.getHorizontalFacing() == Direction.UP && super.canStayAttached();
    }

    @Override
    protected ItemStack getAsItemStack() {
        return new ItemStack(item_init.PLATE);
    }
}
