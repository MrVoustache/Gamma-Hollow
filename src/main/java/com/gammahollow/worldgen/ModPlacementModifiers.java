package com.gammahollow.worldgen;

import com.gammahollow.worldgen.placement.ProximityFilter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModPlacementModifiers {
    public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIERS = 
        DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, "gammahollow");

    public static final DeferredHolder<PlacementModifierType<?>, PlacementModifierType<ProximityFilter>> PROXIMITY_FILTER = 
        PLACEMENT_MODIFIERS.register("proximity_filter", () -> () -> ProximityFilter.CODEC);
}