package com.gammahollow.spawn.mobs.lush_animals;

import com.gammahollow.spawn.mobs.types.GroundAnimalSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.level.biome.Biome;

public class FoxSpawningRule extends GroundAnimalSpawningRules<Fox> {

    public FoxSpawningRule() {
        super(EntityType.FOX, 15, 10, 1, 2);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES);
    }
    
}
