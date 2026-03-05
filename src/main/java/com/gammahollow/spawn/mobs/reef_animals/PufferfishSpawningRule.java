package com.gammahollow.spawn.mobs.reef_animals;

import com.gammahollow.spawn.mobs.types.WaterSpawningRules;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Pufferfish;

public class PufferfishSpawningRule extends WaterSpawningRules<Pufferfish> {

    public PufferfishSpawningRule() {
        super(EntityType.PUFFERFISH, 10, 5, 1, 1);
    }
    
}
