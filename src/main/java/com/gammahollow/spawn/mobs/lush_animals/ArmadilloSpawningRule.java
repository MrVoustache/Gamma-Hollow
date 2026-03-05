package com.gammahollow.spawn.mobs.lush_animals;

import com.gammahollow.spawn.mobs.types.GroundAnimalSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.armadillo.Armadillo;
import net.minecraft.world.level.biome.Biome;

public class ArmadilloSpawningRule extends GroundAnimalSpawningRules<Armadillo> {

    public ArmadilloSpawningRule() {
        super(EntityType.ARMADILLO, 20, 5, 1, 2);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES);
    }
    
}
