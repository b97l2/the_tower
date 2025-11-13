package mia.the_tower.initialisation.entity.custom;

import mia.the_tower.initialisation.entity.goal.DreamyArcFlightGoal;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class LampFlyEntity extends AmbientEntity implements Flutterer{
    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public final AnimationState flyAnimationState = new AnimationState();

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
        return true; // typical ambient behavior
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
        }
        if (!this.isOnGround()) {
            var v = this.getVelocity();
            this.setVelocity(v.x * 0.96, v.y * 0.90, v.z * 0.96);
        }
    }

}
