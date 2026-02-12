package com.gammahollow.worldgen.feature;

import java.util.Set;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseCoralFanBlock;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public abstract class AbstractCoralTreeFeature extends Feature<NoneFeatureConfiguration> {
    public AbstractCoralTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    // --- Subclass Material Implementation ---
    protected abstract BlockState getTrunkBlock(FeaturePlaceContext<NoneFeatureConfiguration> context);
    protected abstract BlockState getTopBlock(FeaturePlaceContext<NoneFeatureConfiguration> context);
    protected abstract BlockState getFanBlock(FeaturePlaceContext<NoneFeatureConfiguration> context); // Floor-based fan
    protected abstract BlockState getWallFanBlock(FeaturePlaceContext<NoneFeatureConfiguration> context); // Wall-based fan
    protected abstract boolean supportsPickle(FeaturePlaceContext<NoneFeatureConfiguration> context);

    // --- Subclass Shape Implementation ---
    /**
     * @return A set of absolute BlockPos where the trunk blocks should be placed.
     */
    protected abstract Set<BlockPos> getTrunkPositions(FeaturePlaceContext<NoneFeatureConfiguration> context);

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        RandomSource random = context.random();
        
        // 1. Get the shape from the subclass
        Set<BlockPos> trunkPositions = getTrunkPositions(context);
        if (trunkPositions.isEmpty()) return false;

        // 2. Place the Trunk Blocks
        for (BlockPos pos : trunkPositions) {
            if (level.getBlockState(pos).is(Blocks.WATER)) {
                level.setBlock(pos, getTrunkBlock(context), 3);
            }
        }

        // 3. Decoration Phase (Iterate through the trunk we just built)
        for (BlockPos pos : trunkPositions) {
            // Failed to place the block
            if (!level.getBlockState(pos).is(getTrunkBlock(context).getBlock())) continue;
            
            // A. Wall Fans (Check 4 horizontal sides)
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos neighbor = pos.relative(dir);
                // If neighbor is water and NOT part of the trunk itself
                if (level.getBlockState(neighbor).is(Blocks.WATER)) {
                    if (random.nextFloat() < 0.25f) {
                        placeWallFan(context, level, neighbor, dir);
                    }
                }
            }

            // B. Tops, Pickles and top fans
            BlockPos above = pos.above();
            if (level.getBlockState(above).is(Blocks.WATER)) {
                if (random.nextFloat() < 0.4f) {
                    level.setBlock(above, getTopBlock(context).setValue(BaseCoralFanBlock.WATERLOGGED, true), 3);
                } else if (supportsPickle(context) && random.nextFloat() < 0.3f){
                    BlockState pickleState = Blocks.SEA_PICKLE.defaultBlockState()
                        .setValue(SeaPickleBlock.PICKLES, random.nextInt(3) + 1)
                        .setValue(SeaPickleBlock.WATERLOGGED, true);
                    level.setBlock(above, pickleState, 3);
                } else if (random.nextFloat() < 0.2f) {
                    level.setBlock(above, getFanBlock(context).setValue(BaseCoralFanBlock.WATERLOGGED, true), 3);
                }
            }
        }

        return true;
    }

    private void placeWallFan(FeaturePlaceContext<NoneFeatureConfiguration> context, WorldGenLevel level, BlockPos pos, Direction dir) {
        BlockState state = getWallFanBlock(context);
        if (state.hasProperty(BaseCoralWallFanBlock.FACING)) {
            level.setBlock(pos, state
                .setValue(BaseCoralWallFanBlock.FACING, dir)
                .setValue(BaseCoralWallFanBlock.WATERLOGGED, true), 3);
        }
    }
}