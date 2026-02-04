package com.gammahollow.worldgen;

import com.gammahollow.GammaHollow;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {
    // 1. Create the Register
    public static final DeferredRegister<Feature<?>> FEATURES = 
            DeferredRegister.create(Registries.FEATURE, GammaHollow.MODID);

    // 2. Register your specific lava feature logic
    public static final DeferredHolder<Feature<?>, LavaSurfaceFeature> LAVA_SURFACE = 
            FEATURES.register("lava_surface", () -> new LavaSurfaceFeature(NoneFeatureConfiguration.CODEC));

    // 3. This method is called by your main GammaHollow class
    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}