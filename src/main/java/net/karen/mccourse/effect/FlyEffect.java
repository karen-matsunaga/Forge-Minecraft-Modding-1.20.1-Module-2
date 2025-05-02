package net.karen.mccourse.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class FlyEffect extends MobEffect {
    protected FlyEffect(MobEffectCategory pCategory, int pColor) { super(pCategory, pColor); }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity instanceof Player player) {
            player.getAbilities().mayfly = true; // Allow flight
            player.onUpdateAbilities(); // Update the player abilities
        }
        super.applyEffectTick(pLivingEntity, pAmplifier);
    } // Fly effect applied

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        if (pLivingEntity instanceof Player player) {
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.onUpdateAbilities(); // Update the player abilities
        }
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
    } // Fly effect ended

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        int k = 50 >> pAmplifier;
        if (k > 0) { return pDuration % k == 0; }
        else { return true; }
    } // Fly effect duration
}