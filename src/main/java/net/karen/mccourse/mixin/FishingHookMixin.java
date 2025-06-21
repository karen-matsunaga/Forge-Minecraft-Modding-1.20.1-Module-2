package net.karen.mccourse.mixin;

import net.minecraft.world.entity.projectile.FishingHook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Shadow private int timeUntilHooked, timeUntilLured, nibble;

    // Player fish more easy
    @Inject(method = "tick", at = @At("HEAD"))
    private void reduceFishingWaitTime(CallbackInfo ci) {
        this.timeUntilLured = Math.min(this.timeUntilLured, 10); // Waiting time until a fish starts to approach
        this.nibble = Math.min(this.nibble, 10); // Hook swing time (fish agitation phase)
        this.timeUntilHooked = Math.min(this.timeUntilHooked, 20); // Time remaining until the fish actually bites the hook
    }
}