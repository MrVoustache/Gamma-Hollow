package com.gammahollow.spawn.mobs.lush_animals;

import com.gammahollow.spawn.mobs.types.GroundAnimalSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Rabbit;
import net.minecraft.world.level.biome.Biome;

public class RabbitSpawningRule extends GroundAnimalSpawningRules<Rabbit> {

    public RabbitSpawningRule() {
        super(EntityType.RABBIT, 40, 15, 2, 4);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES);
    }
    
}
