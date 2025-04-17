package net.karen.mccourse.mixin;

import net.minecraft.world.item.enchantment.LootBonusEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(LootBonusEnchantment.class)
public class LootBonusEnchantmentMixin {
    /**
     * @author
     * @reason
     */
    @Overwrite
    public int getMaxLevel() {
        return 10;
    }
}
