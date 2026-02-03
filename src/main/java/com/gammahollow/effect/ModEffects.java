package com.gammahollow.effect;

import com.gammahollow.GammaHollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public class ModEffects {
    public static final DeferredRegister<MobEffect> EFFECTS = 
        DeferredRegister.create(Registries.MOB_EFFECT, GammaHollow.MODID);

    public static final DeferredHolder<MobEffect, RadiationEffect> RADIATION = 
        EFFECTS.register("radiation", RadiationEffect::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }
}