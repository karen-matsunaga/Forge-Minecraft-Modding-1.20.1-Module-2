package net.karen.mccourse.mixin;

import net.minecraft.world.inventory.AnvilMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilMenu.class)
public class AnvilMenuMixin {
    // Level limit
    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 40))
    private int mixinLimitInt(int value) {
        return Integer.MAX_VALUE;
    }

    // Level max
    @ModifyConstant(method = "createResult", constant = @Constant(intValue = 39))
    private int mixinMaxInt(int value) {
        return Integer.MAX_VALUE - 1;
    }
}