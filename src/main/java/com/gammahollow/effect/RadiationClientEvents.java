package com.gammahollow.effect;

import java.util.Random;

import com.gammahollow.Config;
import com.gammahollow.GammaHollow;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = GammaHollow.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class RadiationClientEvents {
    private static float flashAlpha = 0.0f;
    private static final Random RANDOM = new Random();

    @SubscribeEvent
    public static void onClientTick(PlayerTickEvent.Post event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        // 1. Decay the current flash
        if (flashAlpha > 0) flashAlpha -= 0.1f;

        if (!Config.ENABLE_RADIATION_FLASHES.get()) return;

        // 2. Chance to trigger a new flash based on Radiation Level
        MobEffectInstance effect = player.getEffect(ModEffects.RADIATION);
        if (effect != null) {
            float chance = 0.005f;
            
            if (RANDOM.nextFloat() < chance && flashAlpha <= 0) {
                flashAlpha = RANDOM.nextFloat() * 0.4f; // Random brightness
            }
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiLayerEvent.Pre event) {
        if (flashAlpha <= 0) return;

        // 3. Draw the flash overlay
        int width = event.getGuiGraphics().guiWidth();
        int height = event.getGuiGraphics().guiHeight();
        
        // Color: Pure white or slightly yellowish-green (0xAAFFFFFF)
        int color = ((int)(flashAlpha * 255) << 24) | 0xFFFFFF;
        
        event.getGuiGraphics().fill(0, 0, width, height, color);
    }
}