package net.karen.mccourse.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class NothingEffect extends MobEffect {
    protected NothingEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.isInWater()) {
            pLivingEntity.setSpeed(100);
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }
}