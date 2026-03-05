package com.gammahollow.spawn.mobs.types;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.LightLayer;

public abstract class GroundMonsterSpawningRules<T extends Mob> extends GroundSpawningRules<T> {

    public final int MAX_BLOCK_LIGHT_LEVEL; // Maximum light level for monsters to spawn from blocks
    public final int MAX_SKY_LIGHT_LEVEL; // Maximum light level for monsters to spawn from sky light

    public GroundMonsterSpawningRules(EntityType<T> entityType, int spawnWeight, int spawnCap, int minGroupSize, int maxGroupSize) {
        super(entityType, spawnWeight, spawnCap, minGroupSize, maxGroupSize);
        this.MAX_BLOCK_LIGHT_LEVEL = 2; // Default max block light level for monsters
        this.MAX_SKY_LIGHT_LEVEL = 7; // Default max sky light level for monsters
    }

    public GroundMonsterSpawningRules(EntityType<T> entityType, int spawnWeight, int spawnCap, int minGroupSize, int maxGroupSize, int maxBlockLightLevel, int maxSkyLightLevel) {
        super(entityType, spawnWeight, spawnCap, minGroupSize, maxGroupSize);
        this.MAX_BLOCK_LIGHT_LEVEL = maxBlockLightLevel;
        this.MAX_SKY_LIGHT_LEVEL = maxSkyLightLevel;
    }
    
    public boolean canSpawnAtPosition(ServerLevel level, BlockPos pos) {
        // Also check solid ground and light level for monsters
        return super.canSpawnAtPosition(level, pos) && (!level.getBlockState(pos.below()).is(BlockTags.LEAVES) && !level.getBlockState(pos.below()).is(BlockTags.LOGS)) && level.getBrightness(LightLayer.BLOCK, pos) <= MAX_BLOCK_LIGHT_LEVEL && level.getBrightness(LightLayer.SKY, pos) <= MAX_SKY_LIGHT_LEVEL;
    }
}
