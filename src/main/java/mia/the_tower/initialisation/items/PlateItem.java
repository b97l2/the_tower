package mia.the_tower.initialisation.items;

import mia.the_tower.initialisation.entity.custom.PlateEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class PlateItem extends Item {
    public PlateItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        Direction direction = context.getSide();
        if (direction != Direction.UP) {
            return ActionResult.FAIL;
        }

        PlayerEntity player = context.getPlayer();
        ItemStack stack = context.getStack();
        BlockPos platePos = context.getBlockPos().offset(direction);
        World world = context.getWorld();
        if (world.isOutOfHeightLimit(platePos) || player != null && !player.canPlaceOn(platePos, direction, stack)) {
            return ActionResult.FAIL;
        }

        PlateEntity plate = new PlateEntity(world, platePos, Direction.UP);
        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.ENTITY_DATA, NbtComponent.DEFAULT);
        if (!nbtComponent.isEmpty()) {
            EntityType.loadFromEntityNbt(world, player, plate, nbtComponent);
        }

        if (!plate.canStayAttached()) {
            return ActionResult.CONSUME;
        }

        if (!world.isClient) {
            plate.onPlace();
            world.emitGameEvent(player, GameEvent.ENTITY_PLACE, plate.getPos());
            world.spawnEntity(plate);
        }

        stack.decrement(1);
        return ActionResult.SUCCESS;
    }
}
