package com.gammahollow.worldgen.feature;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CaveVines;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class JungleVineLightFeature extends Feature<NoneFeatureConfiguration> {
    public JungleVineLightFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();

        // 1. Check the block above
        BlockPos supportPos = origin.above();
        BlockState blockAbove = level.getBlockState(supportPos);
        
        // If it's not something we can hang from, abort
        if (!blockAbove.is(BlockTags.LEAVES) && !blockAbove.is(BlockTags.LOGS)) {
            return false;
        }

        int length = random.nextInt(4) + 1;

        // 2. Proximity & Obstacle Check
        // We check if the space is empty and if neighbors are too close
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            for (int i = 0; i < length; i++) {
                BlockPos currentPos = origin.below(i);
                BlockPos neighbor = currentPos.relative(dir);
                BlockState neighborState = level.getBlockState(neighbor);
                
                // If there's already a vine next to us OR the path is blocked, abort
                if (neighborState.is(Blocks.CAVE_VINES) || neighborState.is(Blocks.CAVE_VINES_PLANT) || !level.isEmptyBlock(currentPos)) {
                    return false; 
                }
            }
        }

        // 3. Anchor Replacement (The "Cheat")
        // If the anchor is a leaf, we swap it for Mangrove Roots so the vine doesn't pop.
        // Logs are already sturdy, so we leave them alone.
        if (blockAbove.is(BlockTags.LEAVES)) {
            level.setBlock(supportPos, Blocks.MANGROVE_ROOTS.defaultBlockState(), 2 | 16);
        }

        // 4. Place the Vine Column
        for (int i = 0; i < length; i++) {
            BlockPos currentPos = origin.below(i);
            
            // Double check it's still air (safety first)
            if (level.isEmptyBlock(currentPos)) {
                boolean isTip = (i == length - 1);
                BlockState stateToPlace = isTip ? 
                    Blocks.CAVE_VINES.defaultBlockState() : 
                    Blocks.CAVE_VINES_PLANT.defaultBlockState();

                // 30% chance for berries to provide light
                if (random.nextFloat() < 0.3f) {
                    stateToPlace = stateToPlace.setValue(CaveVines.BERRIES, true);
                }

                // Use flag 2 (Update clients) and 16 (Prevent neighbor reactions)
                level.setBlock(currentPos, stateToPlace, 2 | 16);
            } else {
                break; 
            }
        }
        return true;
    }
}