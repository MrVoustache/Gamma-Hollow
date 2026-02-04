package com.gammahollow.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LavaSurfaceFeature extends Feature<NoneFeatureConfiguration> {
    public LavaSurfaceFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        // Iterate through the 16x16 area of the chunk
        for (int x = 0; x < 16; ++x) {
            for (int z = 0; z < 16; ++z) {
                // Target your specific Y range: [650, 750]
                for (int y = 650; y <= 750; ++y) {
                    mutablePos.set(origin.getX() + x, y, origin.getZ() + z);
                    
                    // Only replace air. This keeps your ravine 'walls' intact!
                    if (level.getBlockState(mutablePos).isAir()) {
                        // Use flag 2 to avoid unnecessary physics updates during generation
                        level.setBlock(mutablePos, Blocks.LAVA.defaultBlockState(), 2);
                    }
                }
            }
        }
        return true;
    }
}