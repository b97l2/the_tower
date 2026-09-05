package mia.the_tower.initialisation.entity.custom;

import mia.the_tower.initialisation.entity.goal.DreamyArcFlightGoal;
import mia.the_tower.initialisation.item_init;
import mia.the_tower.initialisation.items.LampFlyInABottleItem;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LightBlock;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsage;
import net.minecraft.item.Items;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class LampFlyEntity extends AmbientEntity implements Flutterer, Bucketable{
    private static final String FROM_BOTTLE_KEY = "FromBottle";
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final AnimationState flyAnimationState = new AnimationState();
    private boolean fromBottle = false;
    private BlockPos lightPos = null;

    public LampFlyEntity(EntityType<? extends AmbientEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 12, true);
        this.setNoGravity(true);
    }


    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation nav = new BirdNavigation(this, world);
        nav.setCanSwim(false);
        nav.setCanPathThroughDoors(false);
        return nav;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean isInAir() {
        return !this.isOnGround();
    }

    @Override
    public boolean handleFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        return false;
    }

    @Override
    protected ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (stack.isOf(Items.GLASS_BOTTLE) && this.isAlive()) {
            ItemStack bottleStack = this.getBucketItem();
            this.copyDataToStack(bottleStack);
            player.setStackInHand(hand, ItemUsage.exchangeStack(stack, player, bottleStack, false));
            this.playSound(this.getBucketFillSound(), 1.0F, 1.0F);
            this.getWorld().emitGameEvent(player, GameEvent.ENTITY_INTERACT, this.getBlockPos());

            if (!this.getWorld().isClient()) {
                this.clearBottleLight();
                this.discard();
            }

            return ActionResult.SUCCESS;
        }

        return super.interactMob(player, hand);
    }

    @Override
    protected void initGoals() {
        //this.goalSelector.add(0, new PerchGoal(this, 20));
        this.goalSelector.add(0, new DreamyArcFlightGoal(this,
                /*baseSpeed*/ 0.65,  // movement speed passed to moveControl
                /*minRadius*/ 5.0,    // horizontal orbit radius range
                /*maxRadius*/ 10.0,
                /*vertAmp*/   1.8,    // vertical sine amplitude
                /*omega*/     0.035,  // angular speed (radians/tick) — smaller = slower arcs
                /*anchorDriftSec*/ 12 // seconds between slow anchor drifts
        ));
    }



    public static boolean canSpawn(EntityType<LampFlyEntity> type,
                                   ServerWorldAccess world,
                                   SpawnReason reason,
                                   BlockPos pos,
                                   net.minecraft.util.math.random.Random random) {
        if (!world.isAir(pos) || !world.isAir(pos.up())) return false;
        //if (world.getLightLevel(pos) > 3) return false;
        return true;
    }

    @Override
    public boolean canImmediatelyDespawn(double distanceSquared) {
        return !this.fromBottle; // typical ambient behavior unless released from a bottle
    }

    @Override
    public boolean cannotDespawn() {
        return this.fromBottle || super.cannotDespawn();
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 1)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.4)
                .add(EntityAttributes.FLYING_SPEED, 0.5)
                .add(EntityAttributes.FOLLOW_RANGE, 40)
                .add(EntityAttributes.TEMPT_RANGE, 30)
                ;
    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 20*2;
            this.idleAnimationState.start(this.age);
        } else {
            --this.idleAnimationTimeout;
        }

        if (isInAir()) {
            if (!this.flyAnimationState.isRunning()) {
                this.flyAnimationState.start(this.age);
            }
        } else {
            this.flyAnimationState.stop();
        }

    }

    @Override
    public void tick() {
        super.tick();

        if (this.getWorld().isClient()) {
            this.setupAnimationStates();
        } else {
            this.updateBottleLight();
        }
        if (!this.isOnGround()) {
            var v = this.getVelocity();
            this.setVelocity(v.x * 0.96, v.y * 0.90, v.z * 0.96);
        }
    }

    @Override
    public void onRemove(RemovalReason reason) {
        this.clearBottleLight();
        super.onRemove(reason);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean(FROM_BOTTLE_KEY, this.fromBottle);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.fromBottle = nbt.getBoolean(FROM_BOTTLE_KEY);
    }

    @Override
    public boolean isFromBucket() {
        return this.fromBottle;
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        this.fromBottle = fromBucket;
    }

    @Override
    public void copyDataToStack(ItemStack stack) {
        Bucketable.copyDataToStack(this, stack);
        NbtComponent.set(DataComponentTypes.BUCKET_ENTITY_DATA, stack, nbt -> nbt.putBoolean(FROM_BOTTLE_KEY, true));
    }

    @Override
    public void copyDataFromNbt(NbtCompound nbt) {
        Bucketable.copyDataFromNbt(this, nbt);
        this.fromBottle = nbt.getBoolean(FROM_BOTTLE_KEY);
    }

    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(item_init.LAMP_FLY_IN_A_BOTTLE);
    }

    @Override
    public SoundEvent getBucketFillSound() {
        return SoundEvents.ITEM_BOTTLE_FILL;
    }

    private void updateBottleLight() {
        if (!this.fromBottle) {
            this.clearBottleLight();
            return;
        }

        BlockPos currentPos = this.getBlockPos();
        if (currentPos.equals(this.lightPos)) {
            return;
        }

        this.clearBottleLight();
        if (this.getWorld().isAir(currentPos)) {
            this.getWorld().setBlockState(
                    currentPos,
                    Blocks.LIGHT.getDefaultState().with(LightBlock.LEVEL_15, LampFlyInABottleItem.LIGHT_LEVEL),
                    Block.NOTIFY_ALL
            );
            this.lightPos = currentPos.toImmutable();
        }
    }

    private void clearBottleLight() {
        if (this.lightPos == null || this.getWorld().isClient()) {
            return;
        }

        var state = this.getWorld().getBlockState(this.lightPos);
        if (state.isOf(Blocks.LIGHT) && state.get(LightBlock.LEVEL_15) == LampFlyInABottleItem.LIGHT_LEVEL) {
            this.getWorld().removeBlock(this.lightPos, false);
        }
        this.lightPos = null;
    }

}
