package com.gammahollow.spawn.mobs.reef_animals;

import com.gammahollow.spawn.mobs.types.WaterSpawningRules;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cod;

public class CodSpawningRule extends WaterSpawningRules<Cod> {

    public CodSpawningRule() {
        super(EntityType.COD, 50, 20, 2, 6);
    }
    
}
