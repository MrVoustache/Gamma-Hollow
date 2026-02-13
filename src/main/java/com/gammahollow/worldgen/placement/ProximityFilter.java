package com.gammahollow.worldgen.placement;

import com.gammahollow.worldgen.ModPlacementModifiers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.placement.PlacementContext;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;

import java.util.stream.Stream;

public class ProximityFilter extends PlacementModifier {
    // Define the JSON parameters: radius and what block to look for
    public static final MapCodec<ProximityFilter> CODEC = RecordCodecBuilder.mapCodec(instance -> 
        instance.group(
            com.mojang.serialization.Codec.INT.fieldOf("radius").forGetter(filter -> filter.radius),
            net.minecraft.core.registries.BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(filter -> filter.targetBlock)
        ).apply(instance, ProximityFilter::new)
    );

    private final int radius;
    private final Block targetBlock;

    public ProximityFilter(int radius, Block targetBlock) {
        this.radius = radius;
        this.targetBlock = targetBlock;
    }

    @Override
    public Stream<BlockPos> getPositions(PlacementContext context, RandomSource random, BlockPos pos) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    if (context.getBlockState(pos.offset(x, y, z)).is(targetBlock)) {
                        return Stream.empty();
                    }
                }
            }
        }
        return Stream.of(pos);
    }

    @Override
    public PlacementModifierType<?> type() {
        return ModPlacementModifiers.PROXIMITY_FILTER.get();
    }
}