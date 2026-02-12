package com.gammahollow.worldgen.feature;

import java.util.HashSet;
import java.util.Set;

import com.jcraft.jorbis.Block;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class HybridCoralTreeFeature extends AbstractCoralTreeFeature {

    static private int MAX_STEPS = 100;
    static private float HORIZONTAL_AFFINITY = 0.8f;

    public HybridCoralTreeFeature(Codec<NoneFeatureConfiguration> codec) { super(codec); }

    @Override
    protected BlockState getTrunkBlock(FeaturePlaceContext<NoneFeatureConfiguration> context) { return Blocks.BRAIN_CORAL_BLOCK.defaultBlockState(); }

    @Override
    protected BlockState getTopBlock(FeaturePlaceContext<NoneFeatureConfiguration> context) { return context.random().nextFloat() < 0.5 ? Blocks.TUBE_CORAL.defaultBlockState() : Blocks.BUBBLE_CORAL.defaultBlockState(); }

    @Override
    protected BlockState getFanBlock(FeaturePlaceContext<NoneFeatureConfiguration> context) { return context.random().nextFloat() < 0.5 ? Blocks.TUBE_CORAL_FAN.defaultBlockState() : Blocks.BUBBLE_CORAL_FAN.defaultBlockState(); }

    @Override
    protected BlockState getWallFanBlock(FeaturePlaceContext<NoneFeatureConfiguration> context) { return context.random().nextFloat() < 0.5 ? Blocks.TUBE_CORAL_WALL_FAN.defaultBlockState() : Blocks.BUBBLE_CORAL_WALL_FAN.defaultBlockState(); }

    @Override
    protected boolean supportsPickle(FeaturePlaceContext<NoneFeatureConfiguration> context) { return true; }

    @Override
    protected Set<BlockPos> getTrunkPositions(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        Set<BlockPos> positions = new HashSet<>();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        
        int size = random.nextInt(5);
        
        BlockPos junction = origin.above(2);
        positions.add(origin);
        positions.add(origin.above(1));
        positions.add(origin.above(2));
        
        // Add size random branches
        int done = 0;
        int steps = 0;
        while (done < size) {
            // Add a failsafe: if a new branch collides with another, don't add and try again!
            BlockPos current = junction;
            Set<BlockPos> to_add = new HashSet<>();
            int dz = 0;
            int dh = 0;
            float px = random.nextFloat() * HORIZONTAL_AFFINITY;
            float pz = random.nextFloat() * HORIZONTAL_AFFINITY;
            int ox = random.nextFloat() < 0.5 ? 1 : -1;
            int oz = random.nextFloat() < 0.5 ? 1 : -1;
            boolean valid = true;
            while (dz <= size * 1.5) {
                steps++;
                if (steps > MAX_STEPS) return new HashSet<>();
                if (random.nextFloat() < 0.05) break;
                if (dh > size * 2 && level.getBlockState(current.above()).is(Blocks.WATER)) {
                    current = current.above();
                    dz++;
                } else if (random.nextFloat() < px && level.getBlockState(new BlockPos(current.getX() + ox, current.getY(), current.getZ())).is(Blocks.WATER)) {
                    current = new BlockPos(current.getX() + ox, current.getY(), current.getZ());
                    dh++;
                } else if (random.nextFloat() < pz && level.getBlockState(new BlockPos(current.getX(), current.getY(), current.getZ() + oz)).is(Blocks.WATER)) {
                    current = new BlockPos(current.getX(), current.getY(), current.getZ() + oz);
                    dh++;
                } else if (level.getBlockState(current.above()).is(Blocks.WATER)) {
                    current = current.above();
                    dz++;
                }
                if (positions.contains(current)) {
                    valid = false;
                    break;
                }
                to_add.add(current);
            }
            if (valid) {
                for (BlockPos blockPos : to_add) {
                    positions.add(blockPos);
                }
                done++;
            }
        }
        
        return positions;
    }
    
}