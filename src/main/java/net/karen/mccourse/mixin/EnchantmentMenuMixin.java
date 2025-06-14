package net.karen.mccourse.mixin;

import net.karen.mccourse.util.Utils;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin {
    // Ensures the Lapis Lazuli is in slot 1 when opening
    @Inject(method = "<init>*", at = @At("TAIL"))
    private void onConstruct(int syncId, Inventory playerInventory, ContainerLevelAccess access, CallbackInfo ci) {
        EnchantmentMenu self = (EnchantmentMenu) (Object) this;
        self.slots.get(1).set(new ItemStack(Items.LAPIS_LAZULI, 64));
    }

    // Prevents consumption of Lapis Lazuli during enchantment
    @Inject(method = "clickMenuButton", at = @At("HEAD"))
    private void beforeClick(Player player, int id, CallbackInfoReturnable<Boolean> cir) { Utils.IGNORE_LAPIS = true; }

    // Restores the Lapis Lazuli after an enchantment is performed
    @Inject(method = "clickMenuButton", at = @At("TAIL"))
    private void restoreLapisAfterEnchant(Player pPlayer, int pId, CallbackInfoReturnable<Boolean> ci) {
        Utils.IGNORE_LAPIS = false;
        if (ci.getReturnValue()) {
            EnchantmentMenu self = (EnchantmentMenu)(Object)this;
            ItemStack lapis = self.slots.get(1).getItem();
            if (lapis.is(Items.LAPIS_LAZULI)) { lapis.setCount(64); }
            else { self.slots.get(1).set(new ItemStack(Items.LAPIS_LAZULI, 64)); }
        }
    }

    // Clear the Lapis Lazuli when the GUI closes
    @Inject(method = "removed", at = @At("HEAD"))
    private void containerClosed(Player pPlayer, CallbackInfo ci) {
        EnchantmentMenu self = (EnchantmentMenu) (Object) this;
        self.slots.get(1).set(ItemStack.EMPTY);
    }

    // Prevents the player from removing or moving the Lapis Lazuli
    @Inject(method = "quickMoveStack", at = @At("HEAD"), cancellable = true)
    private void preventLapisClick(Player player, int index, CallbackInfoReturnable<ItemStack> ci) {
        if (index == 1) { ci.setReturnValue(ItemStack.EMPTY); } // Prevents moving the Lapis Lazuli with shift
    }
}