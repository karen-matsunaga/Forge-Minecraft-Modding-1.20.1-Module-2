package net.karen.mccourse.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LightstringEnchantment extends Enchantment {
    protected LightstringEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
        super(rarity, category, equipmentSlots);
    }

    @Override
    public int getMinLevel() { return 2; }

    @Override
    public int getMaxLevel() { return 10; }
}