package com.gammahollow.spawn.mobs.lush_animals;

import com.gammahollow.spawn.mobs.types.GroundAnimalSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.level.biome.Biome;

public class ChickenSpawningRule extends GroundAnimalSpawningRules<Chicken> {

    public ChickenSpawningRule() {
        super(EntityType.CHICKEN, 50, 20, 2, 5);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES);
    }
    
}
