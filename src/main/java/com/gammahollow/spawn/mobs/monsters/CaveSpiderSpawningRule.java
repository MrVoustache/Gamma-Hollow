package com.gammahollow.spawn.mobs.monsters;

import com.gammahollow.spawn.mobs.types.GroundMonsterSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.CaveSpider;
import net.minecraft.world.level.biome.Biome;

public class CaveSpiderSpawningRule extends GroundMonsterSpawningRules<CaveSpider> {

    public CaveSpiderSpawningRule() {
        super(EntityType.CAVE_SPIDER, 10, 10, 2, 5);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_DRIPSTONE_CAVES);
    }
    
}
