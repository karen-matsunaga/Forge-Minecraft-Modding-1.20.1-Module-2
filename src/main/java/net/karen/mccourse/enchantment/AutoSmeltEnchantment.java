package net.karen.mccourse.enchantment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

public class AutoSmeltEnchantment extends Enchantment {
    protected AutoSmeltEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    @Override
    public int getMaxLevel() { return 1; }

    @Override
    public @NotNull Component getFullname(int pLevel) {
        if (pLevel > 0) {
            return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN) // Name enchantment
                    .append(CommonComponents.SPACE) // Separate words and numbers
                    .append(Component.translatable("enchantment.level." + pLevel));  // Level enchantment
        }
        return super.getFullname(pLevel);
    }

    // Magnetic, Auto Smelt, and More Ores doesn't work together
    public boolean checkCompatibility(@NotNull Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != ModEnchantments.MORE_ORES.get() &&
                pEnch != ModEnchantments.MAGNETIC.get();
    }
}