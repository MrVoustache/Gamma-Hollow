package com.gammahollow.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class ModDimensions {
    public static final ResourceKey<Level> GAMMA_HOLLOW_KEY = ResourceKey.create(
        Registries.DIMENSION, 
        ResourceLocation.fromNamespaceAndPath("gammahollow", "gamma")
    );
}