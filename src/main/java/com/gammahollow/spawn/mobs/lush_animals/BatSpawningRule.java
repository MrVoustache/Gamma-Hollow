package com.gammahollow.spawn.mobs.lush_animals;

import com.gammahollow.spawn.mobs.types.FlyingSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;

public class BatSpawningRule extends FlyingSpawningRules<Bat> {

    public BatSpawningRule() {
        super(EntityType.BAT, 30, 5, 1, 2);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_LUSH_CAVES) || biome.is(ModBiomes.GAMMA_DRIPSTONE_CAVES);
    }

    public boolean canSpawnAtPosition(ServerLevel level, BlockPos pos) {
        // Bats can spawn in air blocks with a solid block below and low light level
        return super.canSpawnAtPosition(level, pos) && level.getBrightness(LightLayer.BLOCK, pos) <= 8;
    }
    
}
