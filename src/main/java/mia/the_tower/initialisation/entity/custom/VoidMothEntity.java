package mia.the_tower.initialisation.entity.custom;

import mia.the_tower.initialisation.entity.ModEntities;
import mia.the_tower.initialisation.entity.goal.PerchGoal;
import mia.the_tower.initialisation.item_init;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class VoidMothEntity extends AnimalEntity implements Flutterer {
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final AnimationState flyAnimationState = new AnimationState();

    public VoidMothEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new FlightMoveControl(this, 12, true);
        this.setNoGravity(true);
    }

    // fields (VoidMothEntity.java)
    private static final TrackedData<Boolean> PERCHED =
            DataTracker.registerData(VoidMothEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Direction> PERCH_FACE =
            DataTracker.registerData(VoidMothEntity.class, TrackedDataHandlerRegistry.FACING);

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(PERCHED, false);
        builder.add(PERCH_FACE, Direction.UP); // default to ceiling
    }

    public boolean isPerched() { return this.dataTracker.get(PERCHED); }
    public Direction getPerchFace() { return this.dataTracker.get(PERCH_FACE); }

    public void perchAt(BlockPos anchor, Direction face) {
        // Anchor is the block you’re attaching to, 'face' is the face you cling to (UP=ceiling, NORTH/EAST/SOUTH/WEST=wall)
        this.dataTracker.set(PERCHED, true);
        this.dataTracker.set(PERCH_FACE, face);
        this.navigation.stop();         // stop pathing
        this.setVelocity(Vec3d.ZERO);   // freeze motion
        this.setNoGravity(true);

        // Snap position flush against face so it looks attached
        Vec3i n = face.getVector(); // offset of the face
        double off = 0.48; // how “deep” to press into the face (tweak)
        double x = anchor.getX() + 0.5 - n.getX() * off;
        double y = anchor.getY() + 0.5 - n.getY() * off;
        double z = anchor.getZ() + 0.5 - n.getZ() * off;
        this.refreshPositionAndAngles(x, y, z, this.getYaw(), 0);
    }

    public void unperch() {
        if (!this.isPerched()) return;
        this.dataTracker.set(PERCHED, false);
        this.setNoGravity(false);
    }

    @Override
    public boolean damage(ServerWorld world, DamageSource source, float amount) {
        boolean res = super.damage(world, source, amount);
        this.unperch();
        return res;
    }

    @Override
    public void pushAway(Entity entity) {
        super.pushAway(entity);
        this.unperch();
    }


    @Override
    protected EntityNavigation createNavigation(World world) {
        BirdNavigation nav = new BirdNavigation(this, world);
        nav.setCanSwim(false);
        nav.setCanPathThroughDoors(true);
        return nav;
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
    protected void initGoals() {
        //this.goalSelector.add(0, new PerchGoal(this, 20));
        this.goalSelector.add(1, new AvoidSunlightGoal(this));

        this.goalSelector.add(1, new AnimalMateGoal(this, 1.15D));

        this.goalSelector.add(2, new TemptGoal(this, 1.25D, Ingredient.ofItems(item_init.SHEPHERDS_STAFF), false));
        this.goalSelector.add(5, new FlyGoal(this, 1.15D));
        this.goalSelector.add(6, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 4.0F));

    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes()
                .add(EntityAttributes.MAX_HEALTH, 2)
                .add(EntityAttributes.MOVEMENT_SPEED, 0.7)
                .add(EntityAttributes.FLYING_SPEED, 0.8)
                .add(EntityAttributes.FOLLOW_RANGE, 40)
                .add(EntityAttributes.TEMPT_RANGE, 10)
                ;

    }

    private void setupAnimationStates() {
        if (this.idleAnimationTimeout <= 0) {
            this.idleAnimationTimeout = 40;
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
        }
    }

    @Override
    public void tickMovement() {
        if (this.isPerched()) {
            // Stay frozen and clear unwanted velocity
            this.setVelocity(Vec3d.ZERO);
            this.fallDistance = 0.0f;
            super.tickMovement(); // still do status effects etc.
            return;
        }
        super.tickMovement();
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.isOf(item_init.GLOOP);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return ModEntities.VOID_MOTH.create(world, SpawnReason.BREEDING);
    }
}
