package net.karen.mccourse.mixin;

import net.karen.mccourse.enchantment.ModEnchantments;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingHook.class)
public class FishingHookMixin {
    private int forge$waitTime;
    @Shadow private int life;
    @Final @Nullable private Entity forge$owner;

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectAgileFishingTick(CallbackInfo ci) {
        if (this.life == 1 && this.forge$owner instanceof Player player) {
            ItemStack fishingRod = player.getMainHandItem();
            if (fishingRod.getItem() instanceof FishingRodItem) {
                int level = fishingRod.getEnchantmentLevel(ModEnchantments.AGILE_FISHING.get());
                if (level > 0) {
                    int reduction = (int) (this.forge$waitTime * (0.1f * level)); // Reduces 10% per level
                    this.forge$waitTime = Math.max(5, this.forge$waitTime - reduction);
                }
            }
        }
    }
}