package com.gammahollow.effect;

import com.gammahollow.util.RadiationUtils;

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
    static int DAMAGE_DELAY_LEVEL_1 = 200; // 10 seconds
    static int DAMAGE_DELAY_LEVEL_2 = 100; // 5 seconds
    static int DAMAGE_DELAY_LEVEL_3 = 20;  // 1 second
    private int lastDamageTick = 0;

    public RadiationEffect() {
        // Reddish-green color for the particles
        super(MobEffectCategory.HARMFUL, 0x91DB69);
        this.lastDamageTick = 0;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        // Logic for damage intervals based on level (amplifier)
        this.lastDamageTick++;
        return (duration % INCREASE_CKECK_DELAY == 0) || (duration == 1);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        MobEffectInstance instance = entity.getEffect(ModEffects.RADIATION);
        if (instance == null) return true;

        int duration = instance.getDuration();
        Level level = entity.level();

        // --- DAMAGE LOGIC --- (Intervals)
        boolean shouldDamage = switch (amplifier) {
            case 1 -> this.lastDamageTick >= DAMAGE_DELAY_LEVEL_1;
            case 2 -> this.lastDamageTick >= DAMAGE_DELAY_LEVEL_2;
            case 3 -> this.lastDamageTick >= DAMAGE_DELAY_LEVEL_3;
            default -> false;
        };
        if (shouldDamage) {
            entity.hurt(entity.damageSources().magic(), 1.0F);
            lastDamageTick = 0; // Reset damage tick counter after applying damage
        }

        // --- SLOWNESS LOGIC ---
        entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 39, amplifier, true, false, false));
        // --- NAUSEA LOGIC ---
        if (amplifier == 3) entity.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 59, 0, true, false, false));


        // --- EFFECT INCREASE LOGIC ---
        // If we have spend more than the INCREASE_INTERVAL at this level, that we are in a checking tick and that the entity is exposed, increase it or reset at max
        if (duration % INCREASE_CKECK_DELAY == 0 && RadiationUtils.isExposed(level, entity.blockPosition())) {
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