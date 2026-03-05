package com.gammahollow.spawn.mobs.monsters;

import com.gammahollow.spawn.mobs.types.GroundMonsterSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Bogged;
import net.minecraft.world.level.biome.Biome;

public class BoggedSpawningRule extends GroundMonsterSpawningRules<Bogged> {

    public BoggedSpawningRule() {
        super(EntityType.BOGGED, 40, 10, 1, 2);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES);
    }
    
}
