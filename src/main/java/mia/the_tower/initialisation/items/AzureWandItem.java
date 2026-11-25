package mia.the_tower.initialisation.items;


import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class AzureWandItem extends Item {

    private final ParticleEffect customParticle;

    private static final double MAX_DISTANCE = 64.0;
    private static final float DAMAGE = 5.0F;
    private static final double KNOCKBACK_STRENGTH = 1.6;
    private static final double KNOCKBACK_LIFT = 0.25;
    private static final double BOLT_STEP = 0.45;
    private static final int BOLT_SEGMENTS = 12;
    private static final double BOLT_JITTER = 0.65;
    private static final int BOLT_TRAVEL_TICKS = 4;
    private static final Map<ServerWorld, List<ScheduledParticle>> SCHEDULED_PARTICLES = new WeakHashMap<>();

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(AzureWandItem::spawnScheduledParticles);
    }

    public AzureWandItem(ParticleEffect customParticle,
                         Settings settings) {
        super(settings);
        this.customParticle = customParticle;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.SUCCESS;
        }

        Vec3d start = user.getEyePos();
        Vec3d direction = user.getRotationVec(1.0F);
        Vec3d end = start.add(direction.multiply(MAX_DISTANCE));

        BlockHitResult blockHit = serverWorld.raycast(new RaycastContext(
                start,
                end,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                user
        ));

        Vec3d blockTarget = blockHit.getType() == HitResult.Type.BLOCK ? blockHit.getPos() : end;
        EntityHitResult entityHit = findEntityHit(user, start, blockTarget);
        Vec3d target = entityHit != null ? entityHit.getPos() : blockTarget;

        spawnBolt(serverWorld, start, target);

        if (entityHit != null) {
            Entity targetEntity = entityHit.getEntity();
            targetEntity.damage(serverWorld, user.getDamageSources().playerAttack(user), DAMAGE);
            targetEntity.addVelocity(direction.multiply(KNOCKBACK_STRENGTH).add(0.0, KNOCKBACK_LIFT, 0.0));
            targetEntity.velocityModified = true;
        }

        EquipmentSlot equipmentSlot = stack.equals(user.getEquippedStack(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
        stack.damage(1, user, equipmentSlot);

        return ActionResult.SUCCESS;
    }

    private static EntityHitResult findEntityHit(PlayerEntity user, Vec3d start, Vec3d end) {
        Vec3d ray = end.subtract(start);
        Box searchBox = user.getBoundingBox().stretch(ray).expand(1.0);
        double maxDistanceSquared = start.squaredDistanceTo(end);

        return ProjectileUtil.raycast(
                user,
                start,
                end,
                searchBox,
                entity -> entity != user && !entity.isSpectator() && entity.isAlive() && entity.canHit(),
                maxDistanceSquared
        );
    }

    private void spawnBolt(ServerWorld world, Vec3d start, Vec3d end) {
        Vec3d ray = end.subtract(start);
        double length = ray.length();
        if (length < 1.0e-6) {
            return;
        }

        Vec3d direction = ray.normalize();
        Vec3d reference = Math.abs(direction.y) > 0.9 ? new Vec3d(1.0, 0.0, 0.0) : new Vec3d(0.0, 1.0, 0.0);
        Vec3d right = direction.crossProduct(reference).normalize();
        Vec3d up = right.crossProduct(direction).normalize();

        Random random = world.getRandom();
        Vec3d previous = start;
        for (int i = 1; i <= BOLT_SEGMENTS; i++) {
            double progress = (double) i / BOLT_SEGMENTS;
            Vec3d point = start.add(ray.multiply(progress));

            if (i < BOLT_SEGMENTS) {
                double taper = Math.sin(Math.PI * progress);
                double rightOffset = random.nextTriangular(0.0, BOLT_JITTER) * taper;
                double upOffset = random.nextTriangular(0.0, BOLT_JITTER) * taper;
                point = point.add(right.multiply(rightOffset)).add(up.multiply(upOffset));
            }

            spawnBoltSegment(world, previous, point, i - 1);
            previous = point;
        }
    }

    private void spawnBoltSegment(ServerWorld world, Vec3d start, Vec3d end, int segmentIndex) {
        Vec3d segment = end.subtract(start);
        double length = segment.length();
        int particleCount = Math.max(1, (int) Math.ceil(length / BOLT_STEP));

        for (int i = 0; i <= particleCount; i++) {
            Vec3d pos = start.add(segment.multiply((double) i / particleCount));
            double pathProgress = (segmentIndex + (double) i / particleCount) / BOLT_SEGMENTS;
            int delay = (int) Math.round(pathProgress * BOLT_TRAVEL_TICKS);

            if (delay == 0) {
                spawnParticle(world, this.customParticle, pos);
            } else {
                SCHEDULED_PARTICLES.computeIfAbsent(world, ignored -> new ArrayList<>())
                        .add(new ScheduledParticle(world.getTime() + delay, this.customParticle, pos));
            }
        }
    }

    private static void spawnScheduledParticles(ServerWorld world) {
        List<ScheduledParticle> particles = SCHEDULED_PARTICLES.get(world);
        if (particles == null) {
            return;
        }

        Iterator<ScheduledParticle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            ScheduledParticle particle = iterator.next();
            if (particle.spawnTick() <= world.getTime()) {
                spawnParticle(world, particle.effect(), particle.position());
                iterator.remove();
            }
        }

        if (particles.isEmpty()) {
            SCHEDULED_PARTICLES.remove(world);
        }
    }

    private static void spawnParticle(ServerWorld world, ParticleEffect effect, Vec3d pos) {
        world.spawnParticles(
                effect,
                pos.x, pos.y, pos.z,
                1,
                0.0, 0.0, 0.0,
                0.0
        );
    }

    private record ScheduledParticle(long spawnTick, ParticleEffect effect, Vec3d position) {
    }
}
