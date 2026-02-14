package com.gammahollow.client.renderer;

import java.util.List;

import javax.annotation.Nullable;

import com.gammahollow.block.MimicLeafBlockEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;

public class MimicBakedModel implements IDynamicBakedModel {
    private final BlockRenderDispatcher dispatcher;

    public MimicBakedModel(BlockRenderDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, RenderType renderType) {
        BlockState mimic = data.get(MimicLeafBlockEntity.MIMIC_PROPERTY);
        if (mimic == null) mimic = Blocks.OAK_LEAVES.defaultBlockState();

        // We steal the quads (the 3D shape) from the actual leaf model
        BakedModel model = dispatcher.getBlockModel(mimic);
        return model.getQuads(mimic, side, rand, ModelData.EMPTY, renderType);
    }

    // Boilerplate requirements
    @Override public boolean useAmbientOcclusion() { return true; }
    @Override public boolean isGui3d() { return false; }
    @Override public boolean usesBlockLight() { return true; }
    @Override public boolean isCustomRenderer() { return false; }

    @Override
    public ItemOverrides getOverrides() {
        // We aren't doing any special item-stack logic (especially since there's no BlockItem)
        return ItemOverrides.EMPTY;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return dispatcher.getBlockModel(Blocks.OAK_LEAVES.defaultBlockState()).getParticleIcon(ModelData.EMPTY);
    }

    // NOTE: Ensure you still have the version that takes ModelData for the actual mimicry:
    @Override
    public TextureAtlasSprite getParticleIcon(ModelData data) {
        BlockState mimic = data.get(MimicLeafBlockEntity.MIMIC_PROPERTY);
        if (mimic == null) mimic = Blocks.OAK_LEAVES.defaultBlockState();
        
        return dispatcher.getBlockModel(mimic).getParticleIcon(ModelData.EMPTY);
    }
}