package mia.the_tower.initialisation.items;

import mia.the_tower.initialisation.entity.ModEntities;
import mia.the_tower.initialisation.entity.custom.LampFlyEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class LampFlyInABottleItem extends Item {
    public static final int LIGHT_LEVEL = 12;

    public LampFlyInABottleItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getBlockPos().offset(context.getSide());
        PlayerEntity player = context.getPlayer();

        if (world.isOutOfHeightLimit(pos) || !world.isAir(pos)) {
            return ActionResult.FAIL;
        }

        if (world instanceof ServerWorld serverWorld) {
            LampFlyEntity lampFly = ModEntities.LAMP_FLY.create(serverWorld, SpawnReason.BUCKET);
            if (lampFly == null) {
                return ActionResult.FAIL;
            }

            ItemStack stack = context.getStack();
            NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.BUCKET_ENTITY_DATA, NbtComponent.DEFAULT);
            if (!nbtComponent.isEmpty()) {
                lampFly.copyDataFromNbt(nbtComponent.copyNbt());
            }
            var customName = stack.get(DataComponentTypes.CUSTOM_NAME);
            if (customName != null) {
                lampFly.setCustomName(customName);
            }
            lampFly.setFromBucket(true);
            lampFly.refreshPositionAndAngles(pos, context.getPlayerYaw(), 0.0F);
            lampFly.setVelocity(0.0, 0.0, 0.0);

            serverWorld.spawnEntity(lampFly);
            serverWorld.playSound(null, pos, SoundEvents.ITEM_BOTTLE_EMPTY, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            serverWorld.emitGameEvent(player, GameEvent.ENTITY_PLACE, pos);

            if (player != null && !player.isInCreativeMode()) {
                player.setStackInHand(context.getHand(), ItemUsage.exchangeStack(stack, player, new ItemStack(Items.GLASS_BOTTLE), false));
            }
        }

        return ActionResult.SUCCESS;
    }
}
