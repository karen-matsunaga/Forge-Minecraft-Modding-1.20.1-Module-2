package net.karen.mccourse.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class RecoverEnchantment extends Enchantment {
    protected RecoverEnchantment(Rarity rarity, EnchantmentCategory category,
                                          EquipmentSlot... equipmentSlots) {
        super(rarity, category, equipmentSlots);
    }
}