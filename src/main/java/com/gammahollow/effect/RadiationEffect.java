package com.gammahollow.effect;

import com.gammahollow.util.RadiationUtils;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class RadiationEffect extends MobEffect {

    static int DURATION = 6000; // 5 minutes in ticks
    static int INCREASE_INTERVAL = 1200; // 1 minute in ticks
    static int INCREASE_CKECK_DELAY = 100; // 5 seconds in ticks

    public RadiationEffect() {
        // Reddish-green color for the particles
        super(MobEffectCategory.HARMFUL, 0x91DB69); 
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // Logic for damage intervals based on level (amplifier)
        return (duration % 20 == 0) || (duration == 1);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        MobEffectInstance instance = entity.getEffect(ModEffects.RADIATION);
        if (instance == null) return true;

        int duration = instance.getDuration();
        Level level = entity.level();

        // --- EFFECT INCREASE LOGIC ---
        // If we have spend more than the INCREASE_INTERVAL at this level, that we are in a checking tick and that the entity is exposed, increase it or reset at max
        if (DURATION - duration >= INCREASE_INTERVAL && duration % INCREASE_CKECK_DELAY == 0 && RadiationUtils.isExposed(level, entity.blockPosition())) {
            if (amplifier < 3) {
                // Increase level
                int nextAmp = amplifier + 1;
                entity.addEffect(new MobEffectInstance(ModEffects.RADIATION, DURATION, nextAmp));
            } else {
                // Reset duration at max level
                entity.addEffect(new MobEffectInstance(ModEffects.RADIATION, DURATION, amplifier));
            }
            return true; 
        }

        // --- DEGRADATION LOGIC ---
        // When there is only 1 tick left, we apply the lower level
        else if (duration <= 1 && amplifier > 0) {
            int nextAmp = amplifier - 1;
            
            // We add it here. Since the current one expires this tick, 
            // the new one will take over immediately.
            entity.addEffect(new MobEffectInstance(ModEffects.RADIATION, DURATION, nextAmp));
            return true; 
        }

        // --- DAMAGE LOGIC --- (Intervals)
        boolean shouldDamage = switch (amplifier) {
            case 1 -> duration % 200 == 0; 
            case 2 -> duration % 100 == 0; 
            case 3 -> duration % 20 == 0;  
            default -> false;
        };
        if (shouldDamage) entity.hurt(entity.damageSources().magic(), 1.0F);

        // --- SLOWNESS LOGIC ---
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 39, amplifier, true, false, false));
        // --- NAUSEA LOGIC ---
        if (amplifier == 3) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 39, 0, true, false, false));

        return true;
    }
}