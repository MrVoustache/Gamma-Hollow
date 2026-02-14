package com.gammahollow.init;

import com.gammahollow.block.MimicLeafBlockEntity;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = 
        DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, "gammahollow");

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MimicLeafBlockEntity>> MIMIC_LEAF = 
        BLOCK_ENTITIES.register("mimic_leaf", () -> 
            BlockEntityType.Builder.of(MimicLeafBlockEntity::new, ModBlocks.MIMIC_LEAF.get()).build(null));
}