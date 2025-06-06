package net.karen.mccourse.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class PeacefulMobsEnchantment extends Enchantment {
    protected PeacefulMobsEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
        super(rarity, category, equipmentSlots);
    }

    @Override
    public boolean isTreasureOnly() { return true; }

    @Override
    public boolean isTradeable() { return false; }

    @Override
    public boolean isAllowedOnBooks() { return false; }
}