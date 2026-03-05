package com.gammahollow.spawn.mobs.lush_animals;

import com.gammahollow.spawn.mobs.types.GroundAnimalSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.level.biome.Biome;

public class FrogSpawningRule extends GroundAnimalSpawningRules<Frog> {

    public FrogSpawningRule() {
        super(EntityType.FROG, 10, 10, 1, 3);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES);
    }
    
}
