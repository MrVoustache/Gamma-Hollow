package com.gammahollow.worldgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LavaSurfaceFeature extends Feature<NoneFeatureConfiguration> {

    static int LAVA_MIN = 900;
    static int LAVA_MAX = 1000;
    
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
                for (int y = LAVA_MIN; y <= LAVA_MAX; ++y) {
                    mutablePos.set(origin.getX() + x, y, origin.getZ() + z);
                    if (level.getBlockState(mutablePos).isAir()) {
                        level.setBlock(mutablePos, Blocks.LAVA.defaultBlockState(), 2);
                    }
                }
            }
        }
        return true;
    }
}