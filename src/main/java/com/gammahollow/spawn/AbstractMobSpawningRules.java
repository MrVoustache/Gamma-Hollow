package com.gammahollow.spawn;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.EventHooks;

public abstract class AbstractMobSpawningRules<T extends Mob> {

    protected final EntityType<T> entityType;
    public final int spawnWeight;
    public final int spawnCap;
    public final int minGroupSize;
    public final int maxGroupSize;

    public AbstractMobSpawningRules(EntityType<T> entityType, int spawnWeight, int spawnCap, int minGroupSize, int maxGroupSize) {
        this.entityType = entityType;
        this.spawnWeight = spawnWeight;
        this.spawnCap = spawnCap;
        this.minGroupSize = minGroupSize;
        this.maxGroupSize = maxGroupSize;
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return true;
    }

    public abstract List<BlockPos> getSpawnPositions(ServerLevel level, BlockPos center, RandomSource random, int groupSize);

    public abstract boolean canSpawnAtPosition(ServerLevel level, BlockPos pos);

    public T initializeMob(ServerLevel level, BlockPos pos, RandomSource random) {
        return entityType.create(level);
    }

    public SpawnGroupData spawnMobAtPosition(ServerLevel level, BlockPos pos, RandomSource random, T mob, SpawnGroupData spawnData) {
        if (mob != null) {
            mob.moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, random.nextFloat() * 360.0F, 0.0F);
            SpawnGroupData updatedSpawnData = EventHooks.finalizeMobSpawn(mob, level, level.getCurrentDifficultyAt(pos), MobSpawnType.NATURAL, spawnData);
            level.addFreshEntity(mob);
            return updatedSpawnData;
        }
        return null;
    }

    public boolean spawnGroup(ServerLevel level, BlockPos pos, RandomSource random, int groupSize, boolean checkLoaded) {
        List<BlockPos> spawnPositions = getSpawnPositions(level, pos, random, groupSize);
        int validSpawnPositions = 0;

        for (BlockPos spawnPos : spawnPositions) {
            if (canSpawnAtPosition(level, spawnPos) && (!checkLoaded || level.isLoaded(spawnPos))) {
                validSpawnPositions++;
            }
        }

        if (validSpawnPositions == groupSize) {
            SpawnGroupData spawnData = null;
            for (BlockPos spawnPos : spawnPositions) {
                T mob = initializeMob(level, spawnPos, random);
                spawnData = spawnMobAtPosition(level, spawnPos, random, mob, spawnData);
            }
            return true;
        }
        return false;
    }

    public boolean spawnGroup(ServerLevel level, BlockPos pos, RandomSource random, int groupSize) {
        return spawnGroup(level, pos, random, groupSize, true);
    }

    public int getGroupSizeForSpawn(RandomSource random) {
        return minGroupSize + random.nextInt(maxGroupSize - minGroupSize + 1);
    }

    public int getMobCountInArea(ServerLevel level, AABB area) {
        int count = 0;
        for (Entity mob : level.getEntitiesOfClass(entityType.getBaseClass(), area)) {
            if (entityType.equals(mob.getType())) {
                count++;
            }
        }
        return count;
    }

    public int getMobCapacityInArea(ServerLevel level, AABB area) {
        return spawnCap - getMobCountInArea(level, area);
    }
}