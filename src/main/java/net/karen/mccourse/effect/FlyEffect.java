package net.karen.mccourse.effect;

import net.karen.mccourse.event.ModEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;

public class FlyEffect extends MobEffect {
    protected FlyEffect(MobEffectCategory pCategory, int pColor) { super(pCategory, pColor); }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if (entity instanceof Player) {
            ModEvents.flyItem(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, (Player) entity));
        }
        super.applyEffectTick(entity, amplifier);
    }  // Fly effect applied

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap map, int amplifier) {
        if (entity instanceof Player) {
            ModEvents.flyItem(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, (Player) entity));
        }
        super.removeAttributeModifiers(entity, map, amplifier);
    }  // Fly effect ended

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) { return pDuration % 20 == 0; } // Fly effect duration
}