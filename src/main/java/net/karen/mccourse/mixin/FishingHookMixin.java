package net.karen.mccourse.mixin;

import net.karen.mccourse.item.ModItems;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import javax.annotation.Nullable;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Shadow private int timeUntilHooked, timeUntilLured, nibble;
    @Shadow @Nullable public abstract Player getPlayerOwner();

    // Player fishes faster
    @Inject(method = "tick", at = @At("HEAD"))
    private void reduceFishingWaitTime(CallbackInfo ci) {
        Player player = getPlayerOwner(); // Player Fishing Rod OWNER
        if (player != null) {
            ItemStack fishingRod = player.getMainHandItem(); // Player has Mccourse Fishing Rod on MAIN HAND
            if (fishingRod.is(ModItems.MCCOURSE_FISHING_ROD.get())) {
                this.timeUntilLured = Math.min(this.timeUntilLured, 10); // Waiting time until a fish starts to approach
                this.nibble = Math.min(this.nibble, 10); // Hook swing time (fish agitation phase)
                this.timeUntilHooked = Math.min(this.timeUntilHooked, 20); // Time remaining until the fish actually bites the hook
            }
        }
    }
}