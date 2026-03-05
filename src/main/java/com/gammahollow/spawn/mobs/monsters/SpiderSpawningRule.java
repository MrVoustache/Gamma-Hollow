package com.gammahollow.spawn.mobs.monsters;

import com.gammahollow.spawn.mobs.types.GroundMonsterSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.level.biome.Biome;

public class SpiderSpawningRule extends GroundMonsterSpawningRules<Spider> {

    public SpiderSpawningRule() {
        super(EntityType.SPIDER, 30, 20, 1, 3, 7, 7);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES);
    }
    
}
