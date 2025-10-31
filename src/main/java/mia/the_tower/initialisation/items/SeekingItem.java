package mia.the_tower.initialisation.items;


import net.minecraft.block.Block;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.VibrationParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.BlockPositionSource;
import net.minecraft.world.event.EntityPositionSource;
import net.minecraft.world.event.listener.Vibration;

import java.util.Optional;

/**
 * Right-click to emit a particle that travels toward the nearest instance of a given Block.
 * Designed for Fabric/Yarn 1.21.4.
 */
public class SeekingItem extends Item {

    private final Block targetBlock;
    private final ParticleEffect customParticle;

    private static final int H_RADIUS = 64;
    private static final int V_RADIUS = 64;
    private static final int COOLDOWN_TICKS = 20; // 1s

    // Tuning: how many ticks per block of distance the particle should take
    private static final double TICKS_PER_BLOCK = 3.5; // slower => more "floaty"

    public SeekingItem(Block targetBlock,
                                                ParticleEffect customParticle,
                                                Settings settings) {
        super(settings);
        this.targetBlock = targetBlock;
        this.customParticle = customParticle;
    }

    @Override
    public void postDamageEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(2, attacker, EquipmentSlot.MAINHAND);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack stack = user.getStackInHand(hand);

        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.SUCCESS;
        }

        BlockPos origin = user.getBlockPos();

        Optional<BlockPos> nearest = BlockPos.findClosest(
                origin,
                H_RADIUS,
                V_RADIUS,
                pos -> serverWorld.getBlockState(pos).isOf(this.targetBlock)
        );

        if (nearest.isEmpty()) {
            user.sendMessage(Text.literal("No target block within 64 blocks."), true);
            return ActionResult.SUCCESS;
        }

        BlockPos target = nearest.get();

        // Source: player eyes
        double sx = user.getX();
        double sy = user.getY() + user.getStandingEyeHeight();
        double sz = user.getZ();

        // Destination: center of the target block
        double tx = target.getX() + 0.5;
        double ty = target.getY() + 0.5;
        double tz = target.getZ() + 0.5;

        double dx = tx - sx;
        double dy = ty - sy;
        double dz = tz - sz;

        double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (dist < 1.0e-6) {
            // Edge case: player is inside/at target; nudge upwards
            dy = 0.05;
            dist = 0.05;
        }

        // Choose a travel time that scales with distance, with sane bounds
        int travelTicks = MathHelper.clamp((int) Math.ceil(dist * TICKS_PER_BLOCK), 10, 160);

        // Constant per-tick velocity to reach target in ~travelTicks (assuming low/no drag)
        double vx = dx / travelTicks;
        double vy = dy / travelTicks;
        double vz = dz / travelTicks;

        /*
         * Spawn ONE particle with an explicit velocity.
         * In the particles packet, when count == 0 the offset arguments are treated as velocity,
         * multiplied by the "extra/speed" parameter. We pass speed = 1.0 to keep vx/vy/vz exact.
         */
        serverWorld.spawnParticles(
                this.customParticle,
                sx, sy, sz,
                0,          // <-- single particle with explicit velocity semantics
                vx, vy, vz, // <-- velocity components
                1.0         // <-- speed/extra multiplier (keep at 1.0)
        );

        EquipmentSlot equipmentSlot = stack.equals(user.getEquippedStack(EquipmentSlot.OFFHAND)) ? EquipmentSlot.OFFHAND : EquipmentSlot.MAINHAND;
        stack.damage(6, user, equipmentSlot);

        return ActionResult.SUCCESS;
    }
}
