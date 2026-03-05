package com.gammahollow.spawn.mobs.reef_animals;

import com.gammahollow.spawn.mobs.types.CoralSpawningRules;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Turtle;

public class TurtleSpawningRule extends CoralSpawningRules<Turtle> {

    public TurtleSpawningRule() {
        super(EntityType.TURTLE, 10, 5, 1, 2);
    }
    
}
