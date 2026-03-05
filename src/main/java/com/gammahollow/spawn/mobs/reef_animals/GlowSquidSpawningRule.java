package com.gammahollow.spawn.mobs.reef_animals;

import com.gammahollow.spawn.mobs.types.WaterSpawningRules;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;

public class GlowSquidSpawningRule extends WaterSpawningRules<GlowSquid> {

    public GlowSquidSpawningRule() {
        super(EntityType.GLOW_SQUID, 25, 15, 1, 4);
    }
    
}
