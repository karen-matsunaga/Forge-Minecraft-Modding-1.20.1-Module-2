package net.karen.mccourse.mixin;

import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(AnvilScreen.class)
public class AnvilScreenMixin {
    // Remove Too Expensive! message only CLIENT side -> i >= 40
    @ModifyConstant(method = "renderLabels", constant = @Constant(intValue = 40))
    private int renderLabelsLimitInt(int value) {
        return Integer.MAX_VALUE;
    }
}