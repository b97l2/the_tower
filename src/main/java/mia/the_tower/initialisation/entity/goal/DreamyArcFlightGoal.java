// DreamyArcFlightGoal.java
package mia.the_tower.initialisation.entity.goal;

import java.util.EnumSet;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class DreamyArcFlightGoal extends Goal {
    private final MobEntity mob;
    private final double speed;      // moveControl speed
    private final double minR, maxR; // horizontal orbit radius bounds
    private final double vertAmp;    // vertical amplitude
    private final double omega;      // angular velocity (radians/tick)

    private Vec3d anchor;            // center of the local arc
    private double radius;           // current orbit radius
    private double t;                // phase parameter for the curve
    private double phaseXZ;          // slight XZ phase skew for ellipse
    private double phaseY;           // vertical phase offset
    private int   reanchorTicks;     // countdown to drift/refresh anchor
    private final int reanchorPeriod;

    public DreamyArcFlightGoal(MobEntity mob,
                               double speed,
                               double minRadius, double maxRadius,
                               double vertAmp,
                               double omega,
                               int anchorDriftSec) {
        this.mob = mob;
        this.speed = speed;
        this.minR = minRadius;
        this.maxR = maxRadius;
        this.vertAmp = vertAmp;
        this.omega = omega;
        this.reanchorPeriod = Math.max(40, anchorDriftSec * 20);
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        return true;
    }

    @Override
    public boolean shouldContinue() {
        return true; // ambient, continuous idle flight
    }

    @Override
    public void start() {
        var r = mob.getRandom();
        this.anchor = mob.getPos();
        this.radius = MathHelper.lerp(r.nextFloat(), minR, maxR);
        this.t = r.nextDouble() * (Math.PI * 2);
        this.phaseXZ = r.nextDouble() * (Math.PI * 2);
        this.phaseY = r.nextDouble() * (Math.PI * 2);
        this.reanchorTicks = reanchorPeriod;
    }

    @Override
    public void tick() {
        var world = mob.getWorld();
        var r = mob.getRandom();

        // Occasionally drift the anchor a little to avoid tight circles feeling mechanical.
        if (--reanchorTicks <= 0) {
            reanchorTicks = reanchorPeriod + r.nextInt(60) - 30;
            // Drift anchor softly in a random horizontal direction and slight vertical change.
            double drift = 2.0 + r.nextDouble() * 2.0;
            double angle = r.nextDouble() * Math.PI * 2.0;
            anchor = anchor.add(Math.cos(angle) * drift,
                                r.nextDouble() * 0.6 - 0.3, // small up/down drift
                                Math.sin(angle) * drift);

            // Occasionally retune radius within bounds.
            if (r.nextFloat() < 0.3f) {
                radius = MathHelper.lerp(r.nextFloat(), minR, maxR);
            }
        }

        // Advance along an ellipse; skew the Y frequency for a lazy helix feel.
        t += omega;
        double ex = Math.cos(t);
        double ez = Math.sin(t + phaseXZ) * 0.75; // 0.75 → ellipse instead of perfect circle
        double ey = Math.sin(t * 0.5 + phaseY);   // slower vertical oscillation

        // Target point on the curve
        double tx = anchor.x + radius * ex;
        double ty = clampToFlyableY(anchor.y + vertAmp * ey, world.getBottomY() + 2, world.getTopYInclusive() - 2);
        double tz = anchor.z + radius * ez;

        // Nudge the anchor upward if we are colliding with terrain above.
        if (mob.horizontalCollision || mob.verticalCollision) {
            anchor = anchor.add(0, 0.6, 0);
        }

        // Steer toward the target with FlightMoveControl
        mob.getMoveControl().moveTo(tx, ty, tz, speed);

        // Look slightly ahead of the motion for smoother visuals
//        Vec3d vel = mob.getVelocity();
//        Vec3d look = new Vec3d(tx - mob.getX(), ty - mob.getEyeY(), tz - mob.getZ());
//        if (look.lengthSquared() > 1.0e-3) {
//            mob.getLookControl().lookAt(mob.getX() + vel.x * 4.0 + look.x * 0.5,
//                                        mob.getEyeY() + vel.y * 2.0 + look.y * 0.2,
//                                        mob.getZ() + vel.z * 4.0 + look.z * 0.5,
//                                        10.0f, 20.0f);
//        }

        // Small, random micro-perturbations to break perfect periodicity.
        if (r.nextInt(120) == 0) phaseXZ += (r.nextDouble() - 0.5) * 0.25;
        if (r.nextInt(160) == 0) phaseY  += (r.nextDouble() - 0.5) * 0.25;
    }

    private static double clampToFlyableY(double y, int minY, int maxY) {
        return Math.max(minY, Math.min(maxY, y));
    }
}
