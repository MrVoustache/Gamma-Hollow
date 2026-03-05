package com.gammahollow.spawn.mobs.monsters;

import com.gammahollow.spawn.mobs.types.GroundMonsterSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.level.biome.Biome;

public class WitchSpawningRule extends GroundMonsterSpawningRules<Witch> {

    public WitchSpawningRule() {
        super(EntityType.WITCH, 20, 5, 1, 1, 5, 7);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES);
    }
    
}
