package net.karen.mccourse.mixin;

import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.util.Utils;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BowItem.class)
public abstract class BowItemMixin {
    @Inject(method = "getPowerForTime", at = @At("HEAD"), cancellable = true)
    private static void injectPower(int charge, CallbackInfoReturnable<Float> cir) {
        ItemStack stack = Utils.getLastBowUsed();
        if (stack != null) {
            int level = Utils.enchant(stack, ModEnchantments.LIGHTSTRING.get());
            if (level > 0) {
                float speedMultiplier = 1.0f + (0.25f * level); // 25% per level
                float power = charge / (20.0f / speedMultiplier); // Simulates faster loading
                cir.setReturnValue(Math.min(power, 1.0f));
            }
        }
    }
}