package net.karen.mccourse.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

public class XpBoostEnchantment extends Enchantment {
    protected XpBoostEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
        super(rarity, category, equipmentSlots);
    }

    @Override
    public int getMaxLevel() { return 10; }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment enchant) {
        return super.checkCompatibility(enchant) && enchant != Enchantments.BINDING_CURSE && enchant != Enchantments.VANISHING_CURSE;
    }
}