package mia.the_tower.initialisation.items;

import mia.the_tower.initialisation.TwoBlockTallRod;
import mia.the_tower.initialisation.block_init;
import mia.the_tower.initialisation.particle.CustomParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class ShepherdsStaffItem extends Item {

    public ShepherdsStaffItem(Settings settings) {
        super(settings);
    }

    // Charge time (ticks)
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 100;
    }

    /*
    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (world.isClient) return;                        // spawn on server for multiplayer visibility
        if (!(entity instanceof PlayerEntity player)) return;

        // Figure out if THIS stack is in a hand, and which one.
        Hand hand = null;

        // Main hand: also check 'selected' so we don't fire when it's in the hotbar but not active.
        if (player.getMainHandStack() == stack && selected) {
            hand = Hand.MAIN_HAND;
        }

        // Off-hand has no 'selected' flag; it's either held or not.
        if (player.getOffHandStack() == stack) {
            hand = Hand.OFF_HAND;
        }

        if (hand == null) return;                          // not held → no hand to use

        // Simple rate limit so we don't spawn every tick (tweak 3–5 as you like)
        if (((world.getTime() + entity.getId()) % 4) != 0) return;

        spawnHeldTipParticles(player, hand, (ServerWorld) world);
    }

     */



    /** Hand-relative particle placement. */
    /*
    private static void spawnHeldTipParticles(PlayerEntity player, Hand hand, ServerWorld world) {
        // Base at eye height minus a bit so the tip sits near the held item
        Vec3d base = player.getPos().add(0, player.getStandingEyeHeight() - 0.2, 0);

        // Build a forward/right/up basis from the player's yaw/pitch
        float yawRad = (float) Math.toRadians(player.getYaw());
        float pitchRad = (float) Math.toRadians(player.getPitch());

        Vec3d forward = new Vec3d(
                -Math.sin(yawRad) * Math.cos(pitchRad),
                Math.sin(pitchRad),
                Math.cos(yawRad) * Math.cos(pitchRad)
        );
        Vec3d right = new Vec3d(forward.z, 0, -forward.x).normalize();
        Vec3d up = right.crossProduct(forward).normalize();

        // Left/right hand swap: "is this physical right hand?"
        boolean physicalRightHand = (hand == Hand.MAIN_HAND) == (player.getMainArm() == Arm.RIGHT);

        // Tune these three numbers for your 3D model so the tip lines up.
        double lateral = physicalRightHand ? 0.28 : -0.28; // left/right from center
        double forwardOff = 0.55;                           // in front of the player
        double upOff = -0.20;                               // up/down

        // Small pose tweaks
        if (player.isSneaking()) upOff -= 0.07;
        if (player.isSprinting()) forwardOff += 0.05;

        Vec3d tip = base
                .add(right.multiply(lateral))
                .add(forward.multiply(forwardOff))
                .add(up.multiply(upOff));

        world.spawnParticles(
                CustomParticles.GLUH_PARTICLE,     // replace with your particle type
                tip.x, tip.y, tip.z,
                2,                              // count
                0.0, 0.0, 0.0,                  // spread
                0.0                             // speed
        );
    }
    */

    // Start charging and block further "use block" handling
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);

        if (!world.isClient) {
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_METAL_BREAK, SoundCategory.PLAYERS, 0.1f, 2.0f);
            //change to a tinkling sound effect
        }
        return ActionResult.SUCCESS;
    }

    // Optional: visual/sound effects each tick while charging (correct 1.21.4 signature)
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        Random random = world.getRandom();
        if (!world.isClient && world instanceof ServerWorld sw) {
            CircleSpawn((ServerWorld) user.getWorld(), user, 6.5, 35, random); // 24 points, radius 1.5
        }
    }

    private void saveLore(ItemStack stack, String overworldStr, String frageStr) {
        Text line0 = Text.literal(overworldStr != null ? overworldStr : "");
        Text line1 = Text.literal(frageStr     != null ? frageStr     : "");
        stack.set(DataComponentTypes.LORE, new LoreComponent(List.of(line0, line1)));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack,
                              TooltipContext context,
                              List<Text> tooltip,
                              TooltipType type) {
        // Simple literal line
        tooltip.add(Text.literal("Happiness can be found even in the ").formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.literal("darkest of times, if one only remembers ").formatted(Formatting.GRAY, Formatting.ITALIC));
        tooltip.add(Text.literal("to turn on the light.").formatted(Formatting.GRAY, Formatting.ITALIC));

        // Or use a translatable key from your lang file:
        // tooltip.add(Text.translatable("item.the_tower.your_item.lore").formatted(Formatting.GRAY));
    }

// Call this on the SERVER side (ServerWorld), not client.
// points = how many particles around the ring
// radius = circle radius in blocks

    public static void CircleSpawn(ServerWorld sw, LivingEntity user, double radius, int points, Random random) {
        // Foot height; bump a bit so it doesn't z-fight with the ground
        double y = user.getBodyY(0.0) + 2.85 + Math.abs(random.nextTriangular(0, 1.7f));


        for (int i = 0; i < points; i++) {
            double radius2 = radius * random.nextDouble();
            double angle = ((2.0 + random.nextDouble()*2) * Math.PI * i ) / points;
            double x = user.getX() + radius2 * Math.cos(angle) * random.nextTriangular(1, 1.3f);
            double z = user.getZ() + radius2 * Math.sin(angle) * random.nextTriangular(1, 1.3f);

            // count=1, no spread, no speed => exact placement
            sw.spawnParticles(
                    CustomParticles.WHITE_INCENSE, // your particle type
                    x, y, z,
                    1,          // count
                    0.0, 0.0, 0.0, // position spread (x,y,z)
                    0.0         // speed
            );
        }
    }
}
