package net.karen.mccourse.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class GlowingMobsEnchantment extends Enchantment {
    protected GlowingMobsEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
        super(rarity, category, equipmentSlots);
    }
}