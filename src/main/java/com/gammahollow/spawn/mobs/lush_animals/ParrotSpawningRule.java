package com.gammahollow.spawn.mobs.lush_animals;

import com.gammahollow.spawn.mobs.types.TreeAnimalSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.level.biome.Biome;

public class ParrotSpawningRule extends TreeAnimalSpawningRules<Parrot> {

    public ParrotSpawningRule() {
        super(EntityType.PARROT, 30, 10, 1, 3);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES);
    }
    
}
