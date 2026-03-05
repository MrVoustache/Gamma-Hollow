package com.gammahollow.spawn.mobs.reef_animals;

import com.gammahollow.spawn.mobs.types.WaterSpawningRules;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Dolphin;

public class DolphinSpawningRule extends WaterSpawningRules<Dolphin> {

    public DolphinSpawningRule() {
        super(EntityType.DOLPHIN, 10, 5, 1, 3);
    }
    
}
