package com.gammahollow.init;

import com.gammahollow.block.MimicLeafBlock;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = 
        DeferredRegister.create(Registries.BLOCK, "gammahollow");

    public static final DeferredHolder<Block, MimicLeafBlock> MIMIC_LEAF = 
        BLOCKS.register("mimic_leaf", () -> new MimicLeafBlock(
            BlockBehaviour.Properties.of()
                .mapColor(MapColor.PLANT)
                .strength(0.2F)
                .sound(SoundType.GRASS)
                .noOcclusion()
                .isViewBlocking((s, l, p) -> false)
        ));
}