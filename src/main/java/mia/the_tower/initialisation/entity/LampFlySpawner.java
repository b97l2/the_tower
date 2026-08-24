package mia.the_tower.initialisation.entity;

import mia.the_tower.initialisation.biomes.CeruleanCoast;
import mia.the_tower.initialisation.dimentions.TheFrage;
import mia.the_tower.initialisation.entity.custom.LampFlyEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.SpawnReason;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;

public class LampFlySpawner {
    private static final int CHECK_INTERVAL_TICKS = 20;
    private static final int TARGET_NEAR_PLAYER = 32;
    private static final int MAX_SPAWNS_PER_CHECK = 5;
    private static final double COUNT_RADIUS = 48.0;
    private static final int MIN_SPAWN_RADIUS = 8;
    private static final int MAX_SPAWN_RADIUS = 34;
    private static final int SPAWN_ATTEMPTS_PER_FLY = 24;

    public static void init() {
        ServerTickEvents.END_WORLD_TICK.register(LampFlySpawner::tickWorld);
    }

    private static void tickWorld(ServerWorld world) {
        if (!world.getRegistryKey().equals(TheFrage.THE_FRAGE_LEVEL_KEY)) return;
        if (world.getTime() % CHECK_INTERVAL_TICKS != 0L) return;

        for (ServerPlayerEntity player : world.getPlayers()) {
            if (player.isSpectator() || !isCeruleanCoast(world, player.getBlockPos())) continue;

            Box localArea = new Box(player.getBlockPos()).expand(COUNT_RADIUS);
            int nearby = world.getEntitiesByType(ModEntities.LAMP_FLY, localArea, LampFlyEntity::isAlive).size();
            int toSpawn = Math.min(MAX_SPAWNS_PER_CHECK, TARGET_NEAR_PLAYER - nearby);

            for (int i = 0; i < toSpawn; i++) {
                trySpawnNearPlayer(world, player);
            }
        }
    }

    private static boolean trySpawnNearPlayer(ServerWorld world, ServerPlayerEntity player) {
        Random random = world.getRandom();
        BlockPos playerPos = player.getBlockPos();

        for (int attempt = 0; attempt < SPAWN_ATTEMPTS_PER_FLY; attempt++) {
            BlockPos pos = pickCandidatePos(world, playerPos, random);
            if (!canPlaceLampFly(world, pos)) continue;

            LampFlyEntity fly = ModEntities.LAMP_FLY.create(world, SpawnReason.NATURAL);
            if (fly == null) return false;

            fly.refreshPositionAndAngles(
                    pos.getX() + 0.5,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5,
                    random.nextFloat() * 360.0f,
                    0.0f
            );
            fly.setVelocity(0.0, 0.08, 0.0);

            if (!world.isSpaceEmpty(fly)) continue;

            world.spawnEntity(fly);
            return true;
        }

        return false;
    }

    private static BlockPos pickCandidatePos(ServerWorld world, BlockPos playerPos, Random random) {
        int radius = random.nextBetween(MIN_SPAWN_RADIUS, MAX_SPAWN_RADIUS);
        double angle = random.nextDouble() * Math.PI * 2.0;
        int dx = Math.round((float) (Math.cos(angle) * radius));
        int dz = Math.round((float) (Math.sin(angle) * radius));
        int x = playerPos.getX() + dx;
        int z = playerPos.getZ() + dz;
        int y = playerPos.getY() + random.nextBetween(-3, 9);

        BlockPos nearPlayer = new BlockPos(x, y, z);
        if (canPlaceLampFly(world, nearPlayer)) return nearPlayer;

        int surfaceY = world.getTopY(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, x, z);
        return new BlockPos(x, surfaceY + 2 + random.nextInt(6), z);
    }

    private static boolean canPlaceLampFly(ServerWorld world, BlockPos pos) {
        return pos.getY() > world.getBottomY()
                && pos.getY() < world.getTopYInclusive()
                && isCeruleanCoast(world, pos)
                && LampFlyEntity.canSpawn(ModEntities.LAMP_FLY, world, SpawnReason.NATURAL, pos, world.getRandom());
    }

    private static boolean isCeruleanCoast(ServerWorld world, BlockPos pos) {
        return world.getBiome(pos).matchesKey(CeruleanCoast.CERULEAN_COAST);
    }

    public static void load() {}
}
