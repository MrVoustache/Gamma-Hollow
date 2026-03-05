package com.gammahollow.spawn.mobs.types;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

public abstract class GroundAnimalSpawningRules<T extends Mob> extends GroundSpawningRules<T> {


    public GroundAnimalSpawningRules(EntityType<T> entityType, int spawnWeight, int spawnCap, int minGroupSize, int maxGroupSize) {
        super(entityType, spawnWeight, spawnCap, minGroupSize, maxGroupSize);
    }
    
    public boolean canSpawnAtPosition(ServerLevel level, BlockPos pos) {
        // Also check solid ground
        return super.canSpawnAtPosition(level, pos) && (!level.getBlockState(pos.below()).is(BlockTags.LEAVES) && !level.getBlockState(pos.below()).is(BlockTags.LOGS));
    }

    @Override
    public boolean spawnGroup(ServerLevel level, BlockPos pos, RandomSource random, int groupSize) {
        return spawnGroup(level, pos, random, groupSize, false);
    }
}
