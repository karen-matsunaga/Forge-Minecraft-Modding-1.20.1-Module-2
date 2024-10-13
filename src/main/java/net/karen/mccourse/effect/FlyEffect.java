package net.karen.mccourse.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class FlyEffect extends MobEffect {
    protected FlyEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor); }


    // When the effect it is using
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player player) {
            player.getAbilities().mayfly = true; // Allow flight
            player.onUpdateAbilities(); // Update the player abilities
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    // When the effect it is ending
    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {

        if (pLivingEntity instanceof Player player) {
            player.getAbilities().flying = false;
            player.getAbilities().mayfly = false;
            player.onUpdateAbilities(); // Update the player abilities
        }

        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    // Duration of effect
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }
}
