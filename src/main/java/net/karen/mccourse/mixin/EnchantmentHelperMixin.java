package net.karen.mccourse.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getEnchantmentLevel(Lnet/minecraft/nbt/CompoundTag;)I", cancellable = true)
    private static void mccourse$getEnchantmentLevel(CompoundTag nbt, CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Mth.clamp(nbt.getInt("lvl"), 0, Short.MAX_VALUE));
    }
}