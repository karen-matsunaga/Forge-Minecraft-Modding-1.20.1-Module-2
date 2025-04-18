package net.karen.mccourse.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class MagneticEnchantment extends Enchantment {
    protected MagneticEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) { super(pRarity, pCategory, pApplicableSlots); }

    // Magnetic, Auto Smelt, and More Ores doesn't work together
    public boolean checkCompatibility(@NotNull Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != ModEnchantments.AUTO_SMELT.get() && pEnch != ModEnchantments.MORE_ORES.get();
    }
}