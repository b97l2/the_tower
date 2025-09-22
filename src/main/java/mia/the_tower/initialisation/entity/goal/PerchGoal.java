package mia.the_tower.initialisation.entity.goal;

import mia.the_tower.initialisation.entity.custom.VoidMothEntity;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.EnumSet;

public class PerchGoal extends Goal {
    private final VoidMothEntity mob;
    private BlockPos targetPos;
    private Direction targetFace;
    private int minPerchTicks;
    private int perchTicks;

    public PerchGoal(VoidMothEntity mob, int minPerchSeconds) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.minPerchTicks = minPerchSeconds * 20;
    }

    @Override
    public boolean canStart() {
        if (mob.isPerched()) return false;
        if (mob.getRandom().nextFloat() > 0.02f) return false; // 2% chance each tick ~ idle decision

        // Find a nearby face (ceiling preferred, else walls)
        BlockPos base = mob.getBlockPos();
        for (int tries = 0; tries < 16; tries++) {
            BlockPos p = base.add(
                    mob.getRandom().nextBetween(-6, 6),
                    mob.getRandom().nextBetween(-2, 4),
                    mob.getRandom().nextBetween(-6, 6)
            );

            // Try ceiling first
            if (isFaceSolid(p, Direction.DOWN)) { // block at p has a solid bottom -> we can hang from its underside
                this.targetPos = p;
                this.targetFace = Direction.UP;  // attach to the ceiling (we “face” UP)
                return true;
            }

            // Try walls
            for (Direction dir : Direction.Type.HORIZONTAL) {
                if (isFaceSolid(p, dir.getOpposite())) {
                    this.targetPos = p;
                    this.targetFace = dir; // attach to that wall
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        if (mob.isPerched()) {
            // Remain perched while support is valid and time not elapsed and not disturbed
            if (!hasSupport(mob.getBlockPos(), mob.getPerchFace())) return false;
            if (perchTicks < minPerchTicks) return true;

            // Optional disturbances:
            if (mob.getTarget() != null) return false;
            if (mob.getWorld().isDay()) {
                // if you want “night moths” — unperch by day; or invert if you want the opposite
                return false;
            }
            // Keep perching longer with a chance
            return mob.getRandom().nextFloat() < 0.95f;
        } else {
            // If we haven’t reached target yet, continue while path exists
            return targetPos != null && !mob.getNavigation().isIdle();
        }
    }

    @Override
    public void start() {
        perchTicks = 0;
        if (targetPos != null && !mob.isPerched()) {
            // Fly to an approach point slightly off the face
            Vec3i n = targetFace.getVector();
            BlockPos approach = targetPos.offset(targetFace.getOpposite()); // just in front of the surface
            mob.getNavigation().startMovingTo(approach.getX() + 0.5, approach.getY() + 0.5, approach.getZ() + 0.5, 1.0);
        }
    }

    @Override
    public void tick() {
        if (!mob.isPerched()) {
            // Close enough to snap and perch
            if (targetPos != null && mob.squaredDistanceTo(Vec3d.ofCenter(targetPos)) < 1.0) {
                mob.perchAt(targetPos, targetFace);
            }
        } else {
            perchTicks++;
            mob.getLookControl().lookAt(
                    mob.getX() + 0.1, mob.getY(), mob.getZ() + 0.1); // subtle look change; optional
        }
    }

    @Override
    public void stop() {
        targetPos = null;
        targetFace = null;
        if (mob.isPerched()) {
            mob.unperch();
        }
    }

    // --- helpers ---

    private boolean isFaceSolid(BlockPos pos, Direction face) {
        // For ceiling: face = DOWN (we check the block's bottom face); for wall: opposite of our attach direction.
        BlockState s = mob.getWorld().getBlockState(pos);
        // Yarn name can be one of these; pick what your mappings have:
        // return s.isSideSolid(mob.getWorld(), pos, face, SideShapeType.FULL);
        // or:
        return s.isOpaqueFullCube(); // conservative fallback
    }

    private boolean hasSupport(BlockPos entityPos, Direction attachFace) {
        BlockPos anchor = switch (attachFace) {
            case UP -> entityPos.up();            // ceiling is above
            case DOWN -> entityPos.down();
            default -> entityPos.offset(attachFace); // wall is to the side we attached to
        };
        return isFaceSolid(anchor, attachFace.getOpposite());
    }
}
