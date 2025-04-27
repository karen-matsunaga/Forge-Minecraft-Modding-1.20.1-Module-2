package net.karen.mccourse.mixin;

import net.minecraft.world.item.enchantment.WaterWalkerEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WaterWalkerEnchantment.class)
public class WaterWalkerEnchantmentMixin {
    @Inject(at = @At("HEAD"), method = "getMaxLevel", cancellable = true)
    public void getMaxLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(10);
    }
}