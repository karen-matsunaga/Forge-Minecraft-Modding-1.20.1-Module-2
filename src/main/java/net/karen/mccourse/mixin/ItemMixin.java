package net.karen.mccourse.mixin;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Final
    @Shadow
    private Rarity rarity;

    @Inject(method = "getEnchantmentValue", at = @At("HEAD"), cancellable = true)
    public void getEnchantmentValue(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(Math.max(5, rarity.ordinal() * 10));
    }
}