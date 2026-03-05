package com.gammahollow.spawn.mobs.lush_animals;

import com.gammahollow.spawn.mobs.types.GroundAnimalSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.level.biome.Biome;

public class OcelotSpawningRule extends GroundAnimalSpawningRules<Ocelot> {

    public OcelotSpawningRule() {
        super(EntityType.OCELOT, 20, 5, 1, 2);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES);
    }
    
}
