package net.karen.mccourse.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class FlyEffect extends MobEffect {
    protected FlyEffect(MobEffectCategory pCategory, int pColor) { super(pCategory, pColor); }

    // Fly effect applied
    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player player) {
            player.getAbilities().mayfly = true; // Allow flight
            player.onUpdateAbilities(); // Update the player abilities
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    // Fly effect ended
    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        if (pLivingEntity instanceof Player player) {
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.onUpdateAbilities(); // Update the player abilities
        }
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) { return true; } // Fly effect duration
}