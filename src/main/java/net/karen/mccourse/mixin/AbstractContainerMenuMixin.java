package net.karen.mccourse.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin {
    // Blocks only if it is EnchantmentMenu and slot 1
    @Inject(method = "doClick", at = @At("HEAD"), cancellable = true)
    private void blockSlot1Click(int slot, int button, ClickType clickType, Player player, CallbackInfo ci) {
        if ((Object) this instanceof EnchantmentMenu self) {
            if (slot == 1 && self.slots.get(1) != null) { ci.cancel(); }
        }
    }
}