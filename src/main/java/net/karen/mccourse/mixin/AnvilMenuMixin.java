package net.karen.mccourse.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    // Level limit -> i >= 40
    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 40))
    private int createResultLimitInt(int value) {
        return Integer.MAX_VALUE;
    }

    // Level max -> i <= 39
    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 39))
    private int createResultMaxInt(int value) {
        return Integer.MAX_VALUE - 1;
    }

    // You will always lose 1 XP
    @Redirect(method = "onTake(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;giveExperienceLevels(I)V"))
    private void onTakeRedirectXpCost(Player player, int originalCost) {
        player.giveExperienceLevels(-1);
    }

    // Remove penalty accumulate
    @Inject(method = "onTake(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/ItemStack;)V",
            at = @At("HEAD"))
    private void onTakeResetRepairCost(Player player, ItemStack output, CallbackInfo ci) {
        output.setRepairCost(0);
    }

    // It will always cost 1 XP
    @Inject(method = "getCost", at = @At("HEAD"), cancellable = true)
    private void getCostAlwaysOneCost(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(1);
    }
}