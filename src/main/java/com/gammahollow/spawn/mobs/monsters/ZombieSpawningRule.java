package com.gammahollow.spawn.mobs.monsters;

import com.gammahollow.spawn.mobs.types.GroundMonsterSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.biome.Biome;

public class ZombieSpawningRule extends GroundMonsterSpawningRules<Zombie> {

    public ZombieSpawningRule() {
        super(EntityType.ZOMBIE, 40, 10, 1, 5);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES) || biome.is(ModBiomes.GAMMA_DRIPSTONE_CAVES);
    }
    
}
