package com.gammahollow.effect;

import com.gammahollow.GammaHollow;
import com.gammahollow.util.RadiationUtils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class RadiationEffect extends MobEffect {

    static int MAX_DURATION = 6000; // 5 minutes in ticks
    static int INCREASE_CKECK_DELAY = 20; // 1 second in ticks
    static float INCREASE_SPEED_RATIO = 6.0f; // How fast the effect increases when exposed compared to normal duration decrease
    static int DAMAGE_DELAY_LEVEL_2 = 200; // 10 seconds
    static int DAMAGE_DELAY_LEVEL_3 = 100; // 5 seconds
    static int DAMAGE_DELAY_LEVEL_4 = 20;  // 1 second

    public RadiationEffect() {
        // Reddish-green color for the particles
        super(MobEffectCategory.HARMFUL, 0x91DB69);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }

    private DamageSource getRadiationSource(LivingEntity entity) {
        // We fetch the custom damage type from the registry using its ResourceLocation
        var registry = entity.level().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE);
        var holder = registry.getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, ResourceLocation.fromNamespaceAndPath(GammaHollow.MODID, "radiation")));
        
        return new DamageSource(holder);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        MobEffectInstance instance = entity.getEffect(ModEffects.RADIATION);
        if (instance == null) return true;
        int time = entity.tickCount;

        // --- DAMAGE LOGIC --- (Intervals)
        int interval = switch (amplifier) {
            case 1 -> DAMAGE_DELAY_LEVEL_2; // Level II
            case 2 -> DAMAGE_DELAY_LEVEL_3; // Level III
            case 3 -> DAMAGE_DELAY_LEVEL_4; // Level IV
            default -> 0; // No damage at level I
        };

        if (interval != 0 && time % interval == 0) {
            entity.hurt(getRadiationSource(entity), 1.0F);
        }

        int duration = instance.getDuration();
        Level level = entity.level();

        // --- SLOWNESS LOGIC ---
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 39, amplifier, true, false, false));

        // --- EFFECT INCREASE LOGIC ---
        // If we have spend more than the INCREASE_INTERVAL at this level, that we are in a checking tick and that the entity is exposed, increase it or reset at max
        if (time % INCREASE_CKECK_DELAY == 0 && RadiationUtils.isExposed(level, entity.blockPosition())) {
            int newAmp = amplifier;
            int newDuration = duration + (int) (INCREASE_SPEED_RATIO * INCREASE_CKECK_DELAY);
            if (newDuration > MAX_DURATION) {
                if (amplifier < 3) {
                    // Increase level
                    newAmp = amplifier + 1;
                    newDuration = 2 * INCREASE_CKECK_DELAY; // Start fresh at the new level
                } else {
                    // Reset duration at max level
                    newAmp = amplifier;
                }
            }
            entity.addEffect(new MobEffectInstance(ModEffects.RADIATION, newDuration, newAmp));
            return true; 
        }

        // --- DEGRADATION LOGIC ---
        // When there is only 1 tick left, we apply the lower level
        else if (duration <= 1 && amplifier > 0) {
            // We add it here. Since the current one expires this tick, 
            // the new one will take over immediately.
            entity.addEffect(new MobEffectInstance(ModEffects.RADIATION, MAX_DURATION, amplifier - 1));
            return true; 
        }

        return true;
    }
}