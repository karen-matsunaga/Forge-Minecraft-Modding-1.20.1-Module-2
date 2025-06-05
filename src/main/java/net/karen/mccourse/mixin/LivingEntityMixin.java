package net.karen.mccourse.mixin;

import net.karen.mccourse.enchantment.ModEnchantments;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Inject(method = "canAttack*", at = @At("HEAD"), cancellable = true)
    private void livingPlayerAttack(LivingEntity living, CallbackInfoReturnable<Boolean> cir) {
        if (living instanceof Player player) { // Monsters etc. not attack Player
            if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.PEACEFUL_MOBS.get(), player) > 0) { cir.setReturnValue(false); }
        }
    }
}