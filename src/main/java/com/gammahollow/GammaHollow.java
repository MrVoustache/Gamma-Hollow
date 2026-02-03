package com.gammahollow; // Ensure this matches your folder structure

import org.slf4j.Logger;

import com.gammahollow.effect.ModEffects;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
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
}