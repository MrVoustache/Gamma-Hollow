package com.gammahollow.spawn.mobs.types;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.gammahollow.spawn.AbstractMobSpawningRules;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.block.Blocks;

public abstract class FlyingSpawningRules<T extends Mob> extends AbstractMobSpawningRules<T> {

    public static int GROUP_SPAWN_RADIUS = 3; // Radius around the center point to search for valid spawn positions (radius of the group)
    public static int MAX_SPAWN_ATTEMPTS = 10; // Max attempts to find valid spawn positions for members ofthe group

    public FlyingSpawningRules(EntityType<T> entityType, int spawnWeight, int spawnCap, int minGroupSize, int maxGroupSize) {
        super(entityType, spawnWeight, spawnCap, minGroupSize, maxGroupSize);
    }

    public boolean canSpawnAtPosition(ServerLevel level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.AIR);
    }

    public List<BlockPos> getSpawnPositions(ServerLevel level, BlockPos center, RandomSource random, int groupSize) {
        Set<BlockPos> positions = new HashSet<>();
        for (int i = 0; i < MAX_SPAWN_ATTEMPTS && positions.size() < groupSize; i++) {
            int xOffset = random.nextInt(GROUP_SPAWN_RADIUS * 2 + 1) - GROUP_SPAWN_RADIUS;
            int yOffset = random.nextInt(GROUP_SPAWN_RADIUS * 2 + 1) - GROUP_SPAWN_RADIUS;
            int zOffset = random.nextInt(GROUP_SPAWN_RADIUS * 2 + 1) - GROUP_SPAWN_RADIUS;
            BlockPos spawnPos = center.offset(xOffset, yOffset, zOffset);
            if (canSpawnAtPosition(level, spawnPos)) {
                positions.add(spawnPos);
            }
        }
        return positions.size() == groupSize ? positions.stream().toList() : List.of();
    }
    
}
