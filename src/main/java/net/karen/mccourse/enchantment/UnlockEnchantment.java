package net.karen.mccourse.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class UnlockEnchantment extends Enchantment {
    protected UnlockEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... pApplicableSlots) {
        super(rarity, category, pApplicableSlots);
    }
}