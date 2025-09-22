package mia.the_tower.initialisation.items;

import mia.the_tower.initialisation.block.TwoBlockTallRod;
import mia.the_tower.initialisation.block_init; // <-- swap/remove if you use a different marker block
import mia.the_tower.initialisation.particle.CustomParticles;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class StaveItem extends Item {

    // the_tower:the_frage world key
    private static final RegistryKey<World> FRAGE_KEY =
            RegistryKey.of(RegistryKeys.WORLD, Identifier.of("the_tower", "the_frage"));
    private static final RegistryKey<World> OVERWORLD_KEY = World.OVERWORLD;

    public StaveItem(Settings settings) {
        super(settings);
    }

    // Show bow draw animation
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    // Charge time (ticks)
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 65; //35 for full draw
    }

    // Start charging and block further "use block" handling
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);

        if (!world.isClient) {
            world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_METAL_BREAK, SoundCategory.PLAYERS, 0.1f, 2.0f);
            //change to a stabbing sound effect
        }
        return ActionResult.SUCCESS;
    }

    // Optional: visual/sound effects each tick while charging (correct 1.21.4 signature)
    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        Random random = world.getRandom();
        if (!world.isClient && world instanceof ServerWorld sw) {
            CircleSpawn((ServerWorld) user.getWorld(), user, 2.5, 100, random); // 24 points, radius 1.5
            FlowerSpawn((ServerWorld) user.getWorld(), user, 0.45, 100, random);
            CrossSpawn((ServerWorld) user.getWorld(), user, 0.45, 50, random);
            SphereSpawn((ServerWorld) user.getWorld(), user, 5.5, 25, random);
            sw.spawnParticles(
                    ParticleTypes.ASH,
                    user.getX(),
                    user.getBodyY(0.5),
                    user.getZ(),
                    4,           // count
                    1.65, 2.35, 1.65, // position spread (x,y,z)
                    0.0          // speed
            );
        }
    }

    // If released early, do nothing (correct 1.21.4 signature)
    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        return false; // no remainder/cooldown changes; we only act at full charge
    }

    // Called automatically when full charge time elapses
    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (world.isClient || !(user instanceof ServerPlayerEntity player)) return stack;

        final ServerWorld currentWorld = player.getServerWorld();
        final RegistryKey<World> currentDim = currentWorld.getRegistryKey();
        final BlockPos here = player.getBlockPos();

        // --- Optional: place a marker block at the origin (guarded) ---
        try {
            // Replace with your own marker block or remove this whole section.
            world.setBlockState(here, block_init.FRAGE_MARKER.getDefaultState().with(TwoBlockTallRod.HALF, DoubleBlockHalf.LOWER));
            world.setBlockState(here.up(), block_init.FRAGE_MARKER.getDefaultState().with(TwoBlockTallRod.HALF, DoubleBlockHalf.UPPER));
        } catch (Throwable ignored) {
            // If placement fails (non-replaceable, protection, etc.), ignore.
        }
        // ----------------------------------------------------------------

        // Lore layout:
        // line 0 -> last Overworld position (as "x y z")
        // line 1 -> last the_frage position (as "x y z")
        String overworldStr = null;
        String frageStr = null;

        LoreComponent lore = stack.get(DataComponentTypes.LORE);
        if (lore != null) {
            if (lore.lines().size() > 0) overworldStr = lore.lines().get(0).getString();
            if (lore.lines().size() > 1) frageStr     = lore.lines().get(1).getString();
        }

        if (currentDim.equals(FRAGE_KEY)) {
            // We are in the_frage: save its marker (line 1) and go to Overworld
            frageStr = posToStr(here);
            saveLore(stack, overworldStr, frageStr);

            BlockPos target = (overworldStr != null && !overworldStr.isBlank())
                    ? strToPos(overworldStr)
                    : new BlockPos(0, 100, 0); // fallback
            teleportAbsolute(player, OVERWORLD_KEY, target);
        } else {
            // We are in Overworld/other: save overworld marker (line 0) and go to the_frage
            overworldStr = posToStr(here);
            saveLore(stack, overworldStr, frageStr);

            BlockPos target = (frageStr != null && !frageStr.isBlank())
                    ? strToPos(frageStr)
                    : new BlockPos(0, 120, 0); // fallback
            teleportAbsolute(player, FRAGE_KEY, target);
        }

        // -8 HP (4 hearts)
        player.damage(currentWorld, player.getDamageSources().magic(), 10.0f);

        return stack;
    }

    private void teleportAbsolute(ServerPlayerEntity player, RegistryKey<World> targetKey, BlockPos pos) {
        ServerWorld targetWorld = player.getServer().getWorld(targetKey);
        if (targetWorld == null) return;

        targetWorld.setBlockState(pos, Blocks.AIR.getDefaultState());

        Set<PositionFlag> flags = EnumSet.noneOf(PositionFlag.class); // absolute teleport
        player.teleport(
                targetWorld,
                pos.getX() + 0.5,
                pos.getY(),
                pos.getZ() + 0.5,
                flags,
                player.getYaw(),
                player.getPitch(),
                true // reset camera
        );

        targetWorld.playSound(null, pos, SoundEvents.ENTITY_ENDERMAN_TELEPORT, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }

    private void saveLore(ItemStack stack, String overworldStr, String frageStr) {
        Text line0 = Text.literal(overworldStr != null ? overworldStr : "");
        Text line1 = Text.literal(frageStr     != null ? frageStr     : "");
        stack.set(DataComponentTypes.LORE, new LoreComponent(List.of(line0, line1)));
    }

    private static String posToStr(BlockPos p) {
        return p.getX() + " " + p.getY() + " " + p.getZ();
    }

    private static BlockPos strToPos(String s) {
        if (s == null) return new BlockPos(0, 100, 0);
        String[] a = s.trim().split("\\s+");
        if (a.length != 3) return new BlockPos(0, 100, 0);
        try {
            return new BlockPos(Integer.parseInt(a[0]), Integer.parseInt(a[1]), Integer.parseInt(a[2]));
        } catch (NumberFormatException e) {
            return new BlockPos(0, 100, 0);
        }
    }

// Call this on the SERVER side (ServerWorld), not client.
// points = how many particles around the ring
// radius = circle radius in blocks

    public static void CircleSpawn(ServerWorld sw, LivingEntity user, double radius, int points, Random random) {
        // Foot height; bump a bit so it doesn't z-fight with the ground
        double y = user.getBodyY(0.0) + 0.15;


        for (int i = 0; i < points; i++) {
            double angle = ((2.0 + random.nextFloat()*2) * Math.PI * i ) + 0.001 / points;
            double x = user.getX() + radius * Math.cos(angle); //* random.nextTriangular(1, 1.3f);
            double z = user.getZ() + radius * Math.sin(angle); //* random.nextTriangular(1, 1.3f);

            // count=1, no spread, no speed => exact placement
            sw.spawnParticles(
                    CustomParticles.EMBERS_OF_THE_FRAGE, // your particle type
                    x, y, z,
                    1,          // count
                    0.0, 0.0, 0.0, // position spread (x,y,z)
                    0.0         // speed
            );
        }
    }

    public static void FlowerSpawn(ServerWorld sw, LivingEntity user, double radius, int points, Random random) {
        // Foot height; bump a bit so it doesn't z-fight with the ground
        double y = user.getBodyY(0.0) + 0.15;


        for (int i = 0; i < points; i++) {
            double angle = ((2.0 + random.nextFloat()*2) * Math.PI * i ) + 0.001 / points;
            double x = user.getX() + radius * 1/Math.cos(angle); //* random.nextTriangular(1, 1.3f);
            double z = user.getZ() + radius * 1/Math.sin(angle); //* random.nextTriangular(1, 1.3f);

            sw.spawnParticles(
                    CustomParticles.EMBERS_OF_THE_FRAGE, // my particle type
                    x, y, z,
                    1,          // count
                    0.0, 0.0, 0.0, // position spread (x,y,z)
                    0.0         // speed
            );
        }
    }

    public static void CrossSpawn(ServerWorld sw, LivingEntity user, double radius, int points, Random random) {
        // Foot height; bump a bit so it doesn't z-fight with the ground
        double y = user.getBodyY(0.0) + 0.15;


        for (int i = 0; i < points; i++) {
            double angle = ((45 * (Math.PI/180) * i ));
            double x = user.getX() + radius * Math.cos(angle); //* random.nextTriangular(1, 1.3f);
            double z = user.getZ() + radius * Math.sin(angle); //* random.nextTriangular(1, 1.3f);
            radius = radius + random.nextTriangular(0, 1.2f); //random.nextFloat()/10;

            sw.spawnParticles(
                    CustomParticles.EMBERS_OF_THE_FRAGE, // my particle type
                    x, y, z,
                    1,          // count
                    0.0, 0.0, 0.0, // position spread (x,y,z)
                    0.0         // speed
            );
        }
    }

    public static void SphereSpawn(ServerWorld sw, LivingEntity user, double radius, int points, Random random) {
        // Foot height; bump a bit so it doesn't z-fight with the ground
        //double y = user.getBodyY(0.0) + 0.15;


        for (int i = 0; i < points; i++) {
            double angle = ((2.0 + random.nextFloat()*2) * Math.PI * i ) + 0.001 / points;
            double y = user.getY() + radius * Math.tan(angle) + 3; //* random.nextTriangular(1, 1.3f);
            double x = user.getX() - radius * Math.cos(angle)*Math.sin(angle); //* random.nextTriangular(1, 1.3f);
            double z = user.getZ() - radius * Math.sin(angle)*Math.sin(angle); //* random.nextTriangular(1, 1.3f);

            // count=1, no spread, no speed => exact placement
            sw.spawnParticles(
                    CustomParticles.STAVE_PARTICLE, // your particle type
                    x, y, z,
                    1,          // count
                    0.0, 0.0, 0.0, // position spread (x,y,z)
                    0.0         // speed
            );

            z = user.getZ() - radius * Math.cos(angle)*Math.sin(angle); //* random.nextTriangular(1, 1.3f);
            x = user.getX() - radius * Math.sin(angle)*Math.sin(angle); //* random.nextTriangular(1, 1.3f);
            sw.spawnParticles(
                    CustomParticles.STAVE_PARTICLE,
                    x, y, z,
                    1,          // count
                    0.0, 0.0, 0.0, // position spread (x,y,z)
                    0.0         // speed
            );

            x = (user.getX() + radius * Math.cos(angle)*Math.sin(angle)); //* random.nextTriangular(1, 1.3f);
            z = user.getZ() + radius * Math.sin(angle)*Math.sin(angle); //* random.nextTriangular(1, 1.3f);
            sw.spawnParticles(
                    CustomParticles.STAVE_PARTICLE,
                    x, y, z,
                    1,          // count
                    0.0, 0.0, 0.0, // position spread (x,y,z)
                    0.0         // speed
            );

            z = (user.getZ() + radius * Math.cos(angle)*Math.sin(angle)); //* random.nextTriangular(1, 1.3f);
            x = user.getX() + radius * Math.sin(angle)*Math.sin(angle); //* random.nextTriangular(1, 1.3f);
            sw.spawnParticles(
                    CustomParticles.STAVE_PARTICLE,
                    x, y, z,
                    1,          // count
                    0.0, 0.0, 0.0, // position spread (x,y,z)
                    0.0         // speed
            );
        }
    }

}
