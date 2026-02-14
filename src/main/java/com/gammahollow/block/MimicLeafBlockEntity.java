package com.gammahollow.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gammahollow.init.ModBlockEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;

public class MimicLeafBlockEntity extends BlockEntity {
    public static final ModelProperty<BlockState> MIMIC_PROPERTY = new ModelProperty<>();
    private BlockState mimicState = Blocks.OAK_LEAVES.defaultBlockState();

    public MimicLeafBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MIMIC_LEAF.get(), pos, state);
    }

    public void scanAndMimic(WorldGenLevel level, BlockPos pos) {
        Map<Block, Integer> counts = new HashMap<>();
        // 3x3x3 scan
        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (level.hasChunk(SectionPos.blockToSectionCoord(p.getX()), SectionPos.blockToSectionCoord(p.getZ()))) {
                BlockState s = level.getBlockState(p);
                if (s.is(BlockTags.LEAVES) && !(s.getBlock() instanceof MimicLeafBlock)) {
                    counts.put(s.getBlock(), counts.getOrDefault(s.getBlock(), 0) + 1);
                }
            }
        }

        List<Block> priority = List.of(Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.AZALEA_LEAVES);
        Block winner = counts.entrySet().stream()
                .max((a, b) -> a.getValue().equals(b.getValue()) 
                        ? Integer.compare(priority.indexOf(b.getKey()), priority.indexOf(a.getKey())) 
                        : a.getValue().compareTo(b.getValue()))
                .map(Map.Entry::getKey)
                .orElse(Blocks.OAK_LEAVES);

        this.setMimicState(winner.defaultBlockState());
    }

    public void scanAndMimic(Level level, BlockPos pos) {
        Map<Block, Integer> counts = new HashMap<>();
        // 3x3x3 scan
        for (BlockPos p : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (level.hasChunk(SectionPos.blockToSectionCoord(p.getX()), SectionPos.blockToSectionCoord(p.getZ()))) {
                BlockState s = level.getBlockState(p);
                if (s.is(BlockTags.LEAVES) && !(s.getBlock() instanceof MimicLeafBlock)) {
                    counts.put(s.getBlock(), counts.getOrDefault(s.getBlock(), 0) + 1);
                }
            }
        }

        List<Block> priority = List.of(Blocks.JUNGLE_LEAVES, Blocks.OAK_LEAVES, Blocks.AZALEA_LEAVES);
        Block winner = counts.entrySet().stream()
                .max((a, b) -> a.getValue().equals(b.getValue()) 
                        ? Integer.compare(priority.indexOf(b.getKey()), priority.indexOf(a.getKey())) 
                        : a.getValue().compareTo(b.getValue()))
                .map(Map.Entry::getKey)
                .orElse(Blocks.OAK_LEAVES);

        this.setMimicState(winner.defaultBlockState());
    }

    public void setMimicState(BlockState state) {
        this.mimicState = state;
        setChanged();
        if (level != null) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            level.getChunkAt(worldPosition).setUnsaved(true); 
        }
    }

    public BlockState getMimicState() {
        return mimicState;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("mimic", NbtUtils.writeBlockState(mimicState));
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.mimicState = NbtUtils.readBlockState(registries.lookupOrThrow(Registries.BLOCK), tag.getCompound("mimic"));
    }
    
    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return saveWithoutMetadata(registries);
    }

    @Override
    public ModelData getModelData() {
        // This passes the mimicState to the BakedModel
        return ModelData.builder().with(MIMIC_PROPERTY, mimicState).build();
    }
}