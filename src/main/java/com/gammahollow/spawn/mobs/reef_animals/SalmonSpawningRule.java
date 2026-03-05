package com.gammahollow.spawn.mobs.reef_animals;

import com.gammahollow.spawn.mobs.types.WaterSpawningRules;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Salmon;

public class SalmonSpawningRule extends WaterSpawningRules<Salmon> {

    public SalmonSpawningRule() {
        super(EntityType.SALMON, 30, 10, 2, 4);
    }
    
}
