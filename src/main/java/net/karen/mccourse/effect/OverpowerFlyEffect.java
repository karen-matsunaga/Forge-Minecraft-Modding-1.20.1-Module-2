package net.karen.mccourse.effect;

import net.karen.mccourse.event.ModEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;

public class OverpowerFlyEffect extends MobEffect {
    protected OverpowerFlyEffect(MobEffectCategory pCategory, int pColor) { super(pCategory, pColor); }

    // Overpower Fly effect to apply call flyEffect custom event (hasFlyEffect == true)
    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity instanceof Player) {
            ModEvents.flyEffect(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, (Player) entity));
        }
        super.applyEffectTick(entity, amplifier);
    }

    // Overpower Fly effect to end call flyEffect custom event (hasFlyEffect == false)
    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap map, int amplifier) {
        if (entity instanceof Player) {
            ModEvents.flyEffect(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, (Player) entity));
        }
        super.removeAttributeModifiers(entity, map, amplifier);
    }

    // Overpower Fly effect duration
    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) { return pDuration % 20 == 0; }
}