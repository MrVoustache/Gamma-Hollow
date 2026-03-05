package com.gammahollow; // Ensure this matches your folder structure

import org.slf4j.Logger;

import com.gammahollow.block.MimicLeafBlockEntity;
import com.gammahollow.client.renderer.MimicBakedModel;
import com.gammahollow.effect.ModEffects;
import com.gammahollow.init.ModBlockEntities;
import com.gammahollow.init.ModBlocks;
import com.gammahollow.worldgen.ModFeatures;
import com.gammahollow.worldgen.ModPlacementModifiers;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.CustomPortalRegistrationEvent;

@Mod(GammaHollow.MODID)
public class GammaHollow {
    public static final String MODID = "gammahollow";
    public static final Logger LOGGER = LogUtils.getLogger();

    public GammaHollow(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerPortals);
        ModEffects.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        ModFeatures.register(modEventBus);
        ModPlacementModifiers.PLACEMENT_MODIFIERS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        modEventBus.addListener(this::onModelBake);
        modEventBus.addListener(this::registerBlockColors);
    }

    private void registerPortals(CustomPortalRegistrationEvent event) {
        CustomPortalBuilder.beginPortal()
            .frameBlock(Blocks.DEEPSLATE_BRICKS)
            .lightWithItem(Items.DIAMOND_PICKAXE)
            .destDimID(ResourceLocation.fromNamespaceAndPath(MODID, "gamma"))
            .onlyLightInOverworld()
            .tintColor(100, 50, 200)
            .registerPortal();
        LOGGER.info("Gamma Hollow Portals Registered during Common Setup!");
    }

    private void onModelBake(ModelEvent.ModifyBakingResult event) {
        BlockRenderDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        MimicBakedModel customModel = new MimicBakedModel(dispatcher);

        // Instead of guessing the "normal" string, we replace EVERY variant 
        // associated with our block to be safe.
        for (BlockState state : ModBlocks.MIMIC_LEAF.get().getStateDefinition().getPossibleStates()) {
            ModelResourceLocation mrl = BlockModelShaper.stateToModelLocation(state);
            event.getModels().put(mrl, customModel);
        }
    }

    private void registerBlockColors(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> {
            if (level != null && pos != null) {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof MimicLeafBlockEntity mimic) {
                    // Get the color of the leaf we are mimicking at this specific spot
                    return event.getBlockColors().getColor(mimic.getMimicState(), level, pos, tintIndex);
                }
            }
            // Fallback to a standard green if something is wrong
            return FoliageColor.getDefaultColor(); 
        }, ModBlocks.MIMIC_LEAF.get());
    }
}