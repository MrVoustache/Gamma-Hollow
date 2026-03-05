package com.gammahollow.spawn.mobs.reef_animals;

import com.gammahollow.spawn.mobs.types.CoralSpawningRules;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.axolotl.Axolotl;

public class AxolotlSpawningRule extends CoralSpawningRules<Axolotl> {

    public AxolotlSpawningRule() {
        super(EntityType.AXOLOTL, 10, 5, 1, 2);
    }
    
}
