package com.gammahollow.spawn.mobs.monsters;

import com.gammahollow.spawn.mobs.types.GroundMonsterSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.level.biome.Biome;

public class SkeletonSpawningRule extends GroundMonsterSpawningRules<Skeleton> {

    public SkeletonSpawningRule() {
        super(EntityType.SKELETON, 60, 20, 1, 4);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES) || biome.is(ModBiomes.GAMMA_DRIPSTONE_CAVES);
    }
    
}
