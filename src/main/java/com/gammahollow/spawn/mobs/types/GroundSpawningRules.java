package com.gammahollow.spawn.mobs.types;

import java.util.ArrayList;
import java.util.List;

import com.gammahollow.spawn.AbstractMobSpawningRules;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public abstract class GroundSpawningRules<T extends Mob> extends AbstractMobSpawningRules<T> {

    public static int GROUND_ABOVE_OFFSET = 5; // How many blocks above the starting point to check for valid spawn positions
    public static int GROUND_BELOW_OFFSET = 15; // How many blocks below the starting point to check for valid spawn positions
    public static int GROUP_SPAWN_RADIUS = 3; // Radius around the center point to search horizontally for valid spawn positions (radius of the group)
    public static int MAX_SPAWN_ATTEMPTS = 10; // Max attempts to find valid spawn positions for members ofthe group

    public GroundSpawningRules(EntityType<T> entityType, int spawnWeight, int spawnCap, int minGroupSize, int maxGroupSize) {
        super(entityType, spawnWeight, spawnCap, minGroupSize, maxGroupSize);
    }
    
    public BlockPos findValidGround(ServerLevel level, BlockPos start, RandomSource random) {
        for (int yOffset = -GROUND_BELOW_OFFSET; yOffset <= GROUND_ABOVE_OFFSET; yOffset++) {
            BlockPos checkPos = start.offset(0, yOffset, 0);
            if (canSpawnAtPosition(level, checkPos)) {
                return checkPos;
            }
        }
        return null;
    }

    public boolean canSpawnAtPosition(ServerLevel level, BlockPos pos) {
        // Check if the block below is solid and the current block is air
        return level.getBlockState(pos.below()).isSolid() && level.getBlockState(pos).isAir();
    }

    public List<BlockPos> getSpawnPositions(ServerLevel level, BlockPos center, RandomSource random, int groupSize) {
        List<BlockPos> spawnPositions = new ArrayList<>();
        for (int a = 0; a < MAX_SPAWN_ATTEMPTS && spawnPositions.size() < groupSize; a++) {
            BlockPos randomOffset = new BlockPos(random.nextInt(GROUP_SPAWN_RADIUS * 2 + 1) - GROUP_SPAWN_RADIUS, 0, random.nextInt(GROUP_SPAWN_RADIUS * 2 + 1) - GROUP_SPAWN_RADIUS);
            BlockPos checkPos = center.offset(randomOffset);
            BlockPos validGround = findValidGround(level, checkPos, random);
            if (validGround != null) {
                spawnPositions.add(validGround);
            }
        }
        return spawnPositions.size() == groupSize ? spawnPositions : new ArrayList<>();
    }
}
