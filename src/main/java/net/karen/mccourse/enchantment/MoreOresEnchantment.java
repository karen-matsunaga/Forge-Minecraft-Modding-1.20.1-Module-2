package net.karen.mccourse.enchantment;

import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class MoreOresEnchantment extends Enchantment {
    protected MoreOresEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) { super(pRarity, pCategory, pApplicableSlots); }

    @Override
    public int getMaxLevel() { return 5; } // More Ores enchantment max level

    // Magnetic, Auto Smelt, and More Ores doesn't work together
    public boolean checkCompatibility(@NotNull Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != ModEnchantments.AUTO_SMELT.get() && pEnch != ModEnchantments.MAGNETIC.get();
    }
}