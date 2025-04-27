package net.karen.mccourse.mixin;

import net.minecraft.world.item.enchantment.FireAspectEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FireAspectEnchantment.class)
public class FireAspectEnchantmentMixin {
    @Inject(at = @At("HEAD"), method = "getMaxLevel", cancellable = true)
    public void getMaxLevel(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(10);
    }
}