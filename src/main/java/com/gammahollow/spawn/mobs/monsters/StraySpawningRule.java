package com.gammahollow.spawn.mobs.monsters;

import com.gammahollow.spawn.mobs.types.GroundMonsterSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.level.biome.Biome;

public class StraySpawningRule extends GroundMonsterSpawningRules<Stray> {

    public StraySpawningRule() {
        super(EntityType.STRAY, 40, 10, 1, 2);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_DRIPSTONE_CAVES);
    }
    
}
