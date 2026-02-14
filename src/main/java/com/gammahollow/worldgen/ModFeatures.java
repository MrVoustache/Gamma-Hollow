package com.gammahollow.worldgen;

import com.gammahollow.GammaHollow;
import com.gammahollow.worldgen.feature.BubbleCoralTreeFeature;
import com.gammahollow.worldgen.feature.HybridCoralTreeFeature;
import com.gammahollow.worldgen.feature.JungleVineLightFeature;
import com.gammahollow.worldgen.feature.LavaSurfaceFeature;
import com.gammahollow.worldgen.feature.TubeCoralTreeFeature;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModFeatures {

    public static final DeferredRegister<Feature<?>> FEATURES = 
            DeferredRegister.create(Registries.FEATURE, GammaHollow.MODID);

    // Register the different features
    public static final DeferredHolder<Feature<?>, LavaSurfaceFeature> LAVA_SURFACE = 
        FEATURES.register("lava_surface", () -> new LavaSurfaceFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, TubeCoralTreeFeature> TUBE_CORAL_TREE = 
        FEATURES.register("tube_coral_tree", () -> new TubeCoralTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, BubbleCoralTreeFeature> BUBBLE_CORAL_TREE = 
        FEATURES.register("bubble_coral_tree", () -> new BubbleCoralTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, HybridCoralTreeFeature> HYBRID_CORAL_TREE = 
        FEATURES.register("hybrid_coral_tree", () -> new HybridCoralTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final DeferredHolder<Feature<?>, JungleVineLightFeature> JUNGLE_VINE_LIGHT = 
        FEATURES.register("jungle_lights", () -> new JungleVineLightFeature(NoneFeatureConfiguration.CODEC));

    public static void register(IEventBus eventBus) {
        FEATURES.register(eventBus);
    }
}