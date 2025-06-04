package net.karen.mccourse.effect;

import net.karen.mccourse.event.ModEvents;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;

public class FlyEffect extends MobEffect {
    protected FlyEffect(MobEffectCategory category, int color) { super(category, color); }

    @Override // Fly effect APPLIED
    public void addAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap map, int amplifier) {
        event(entity, 0.05f + (0.02f * amplifier));
        super.addAttributeModifiers(entity, map, amplifier);
    }

    @Override // Fly effect ENDED
    public void removeAttributeModifiers(@NotNull LivingEntity entity, @NotNull AttributeMap map, int amplifier) {
        event(entity, 0.05f);
        super.removeAttributeModifiers(entity, map, amplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) { return duration % 20 == 0; } // Fly effect DURATION

    private void event(LivingEntity entity, float fly) {
        if (entity instanceof Player player) {
            ModEvents.flyEffect(new TickEvent.PlayerTickEvent(TickEvent.Phase.END, player));
            player.getAbilities().setFlyingSpeed(fly); // Flying speed
        }
    }
}