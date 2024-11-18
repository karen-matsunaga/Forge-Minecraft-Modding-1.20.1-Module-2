package net.karen.mccourse.enchantment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class MoreOresEnchantment extends Enchantment {
    protected MoreOresEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) { super(pRarity, pCategory, pApplicableSlots); }

    @Override
    public int getMaxLevel() { return 5; } // More Ores enchantment max level

    @Override
    public @NotNull Component getFullname(int pLevel) {
        if (pLevel > 0) {
            return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN) // Colors used on description name
                    .append(CommonComponents.SPACE) // Separate words and numbers
                    .append(Component.translatable("enchantment.level." + pLevel));  // Name and level enchantment on item, and chat
        }
        return super.getFullname(pLevel);
    }

    // Auto Smelt and More Ores doesn't work together
    public boolean checkCompatibility(@NotNull Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != ModEnchantments.AUTO_SMELT.get();
    }
}