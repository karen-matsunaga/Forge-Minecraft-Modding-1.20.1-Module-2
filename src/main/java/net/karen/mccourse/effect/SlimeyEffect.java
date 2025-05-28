package net.karen.mccourse.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

// Climbing Effect by SameDifferent: https://github.com/samedifferent/TrickOrTreat/blob/master/LICENSE - Distributed under MIT
public class SlimeyEffect extends MobEffect {
    public SlimeyEffect(MobEffectCategory category, int color) { super(category, color); }

    // Slimey effect applied
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.horizontalCollision) {
            Vec3 initialVec = entity.getDeltaMovement();
            Vec3 climbVec = new Vec3(initialVec.x, 0.2D, initialVec.z);
            entity.setDeltaMovement(climbVec.x * 0.91D, climbVec.y * 0.98D, climbVec.z * 0.91D);
        }
        super.applyEffectTick(entity, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) { return true; } // Slimey effect duration
}