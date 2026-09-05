package mia.the_tower.initialisation.entity.goal;

import mia.the_tower.initialisation.entity.custom.VoidMothEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SideShapeType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.EnumSet;

public class PerchGoal extends Goal {
    private static final Direction[] SUPPORT_SEARCH_ORDER = {
            Direction.UP,
            Direction.NORTH,
            Direction.SOUTH,
            Direction.EAST,
            Direction.WEST,
            Direction.DOWN
    };
    private static final int MAX_APPROACH_TICKS = 100;

    private final VoidMothEntity mob;
    private final int minPerchTicks;
    private BlockPos targetPos;
    private Direction targetFace;
    private int approachTicks;
    private int perchTicks;

    public PerchGoal(VoidMothEntity mob, int minPerchSeconds) {
        this.mob = mob;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        this.minPerchTicks = minPerchSeconds * 20;
    }

    @Override
    public boolean canStart() {
        if (mob.isPerched()) return false;
        if (mob.getRandom().nextFloat() > 0.02f) return false;

        BlockPos base = mob.getBlockPos();
        for (int tries = 0; tries < 16; tries++) {
            BlockPos pos = base.add(
                    mob.getRandom().nextBetween(-6, 6),
                    mob.getRandom().nextBetween(-2, 4),
                    mob.getRandom().nextBetween(-6, 6)
            );

            for (Direction supportDirection : SUPPORT_SEARCH_ORDER) {
                if (canPerchOn(pos, supportDirection)) {
                    this.targetPos = pos;
                    this.targetFace = supportDirection;
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean shouldContinue() {
        if (mob.isPerched()) {
            if (!hasCurrentPerch()) return false;
            if (perchTicks < minPerchTicks) return true;
            if (mob.getTarget() != null) return false;
            if (mob.getWorld().isDay()) return false;

            return mob.getRandom().nextFloat() < 0.95f;
        }

        return targetPos != null
                && targetFace != null
                && approachTicks < MAX_APPROACH_TICKS
                && canPerchOn(targetPos, targetFace);
    }

    @Override
    public void start() {
        approachTicks = 0;
        perchTicks = 0;

        if (targetPos != null && targetFace != null && !mob.isPerched()) {
            Vec3d perchPos = mob.getPerchPosition(targetPos, targetFace);
            mob.getNavigation().startMovingTo(perchPos.x, perchPos.y, perchPos.z, 1.0);
        }
    }

    @Override
    public void tick() {
        if (!mob.isPerched()) {
            approachTicks++;
            if (targetPos == null || targetFace == null || !canPerchOn(targetPos, targetFace)) {
                approachTicks = MAX_APPROACH_TICKS;
                return;
            }

            Vec3d perchPos = mob.getPerchPosition(targetPos, targetFace);
            if (mob.squaredDistanceTo(perchPos) < 1.0) {
                mob.perchAt(targetPos, targetFace);
            } else if (mob.getNavigation().isIdle()) {
                mob.getNavigation().startMovingTo(perchPos.x, perchPos.y, perchPos.z, 1.0);
            }
        } else {
            perchTicks++;
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

    private boolean canPerchOn(BlockPos pos, Direction supportDirection) {
        return isFaceSolid(pos, supportDirection.getOpposite())
                && mob.getWorld().isSpaceEmpty(mob, mob.getPerchBox(pos, supportDirection));
    }

    private boolean isFaceSolid(BlockPos pos, Direction face) {
        BlockState state = mob.getWorld().getBlockState(pos);
        return state.isSideSolid(mob.getWorld(), pos, face, SideShapeType.FULL);
    }

    private boolean hasCurrentPerch() {
        Direction supportDirection = mob.getPerchFace();
        BlockPos anchor = switch (supportDirection) {
            case UP -> mob.getBlockPos().up();
            case DOWN -> mob.getBlockPos().down();
            default -> mob.getBlockPos().offset(supportDirection);
        };

        return canPerchOn(anchor, supportDirection);
    }
}
