package com.gammahollow.event;

import com.gammahollow.GammaHollow;
import com.gammahollow.effect.ModEffects;
import com.gammahollow.util.RadiationUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

@EventBusSubscriber(modid = GammaHollow.MODID)
public class ExposureEventHandler {

    @SubscribeEvent
    public static void onEntityTick(EntityTickEvent.Pre event) {
        // We only care about LivingEntities (Players, Mobs, etc.)
        if (!(event.getEntity() instanceof LivingEntity entity)) return;

        // Ensure we're on the server side
        if (entity.level().isClientSide) return;

        // Every 200 ticks, check the dimension and apply the "Seed" effect
        if ((entity.tickCount + entity.getId()) % 200 == 0) {
            if (entity.level().dimension().location().getPath().equals("gamma")) {
                
                // If they don't have it yet, give them the base level
                if (!entity.hasEffect(ModEffects.RADIATION)) {
                    // Check exposure before giving the "Seed" effect
                    if (RadiationUtils.isExposed(entity.level(), entity.blockPosition())) {
                        entity.addEffect(new MobEffectInstance(ModEffects.RADIATION, 20, 0));
                    }
                }
            }
        }
    }
}