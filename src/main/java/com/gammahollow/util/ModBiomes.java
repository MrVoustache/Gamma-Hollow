package com.gammahollow.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class ModBiomes {

    public static final ResourceKey<Biome> GAMMA_LUSH_CAVES = register("lush_caves");
    public static final ResourceKey<Biome> GAMMA_SURFACE = register("gamma_surface");
    public static final ResourceKey<Biome> GAMMA_DRIPSTONE_CAVES = register("dripstone_caves");
    public static final ResourceKey<Biome> GAMMA_DEEP_DARK = register("deep_dark");

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("gammahollow", name));
    }
}