package com.gammahollow.spawn.mobs.reef_animals;

import com.gammahollow.spawn.mobs.types.WaterSpawningRules;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.TropicalFish;

public class TropicalFishSpawningRule extends WaterSpawningRules<TropicalFish> {

    public TropicalFishSpawningRule() {
        super(EntityType.TROPICAL_FISH, 25, 15, 2, 5);
    }

}
