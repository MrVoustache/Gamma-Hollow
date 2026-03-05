package com.gammahollow.spawn.mobs.monsters;

import com.gammahollow.spawn.mobs.types.GroundMonsterSpawningRules;
import com.gammahollow.util.ModBiomes;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.biome.Biome;

public class WitherSkeletonSpawningRule extends GroundMonsterSpawningRules<WitherSkeleton> {

    public WitherSkeletonSpawningRule() {
        super(EntityType.WITHER_SKELETON, 50, 15, 1, 4);
    }

    public boolean canSpawnInBiome(Holder<Biome> biome) {
        return biome.is(ModBiomes.GAMMA_DRIPSTONE_CAVES);
    }
    
    public boolean canSpawnAtPosition(ServerLevel level, BlockPos pos) {
        return super.canSpawnAtPosition(level, pos) && level.getBlockState(pos.below()).is(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
    }
}
