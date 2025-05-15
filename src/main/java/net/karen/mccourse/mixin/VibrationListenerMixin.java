package net.karen.mccourse.mixin;

import net.karen.mccourse.effect.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(VibrationSystem.Listener.class)
public class VibrationListenerMixin {
    @Inject(method = "handleGameEvent", at = @At("HEAD"), cancellable = true)
    private void mccourse$handleGameEvent(ServerLevel level, GameEvent gameEvent, GameEvent.Context context,
                                          Vec3 pos, CallbackInfoReturnable<Boolean> ci) {
        Entity source = context.sourceEntity();
        if (source instanceof Player player) {
            if (player.hasEffect(ModEffects.NOTHING_EFFECT.get())) {
                ci.setReturnValue(true); // Cancel vibration of Sculk Sensor block
            }
        }
    }
}