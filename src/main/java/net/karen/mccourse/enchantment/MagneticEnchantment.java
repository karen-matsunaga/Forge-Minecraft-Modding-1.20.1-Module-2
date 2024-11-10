package net.karen.mccourse.enchantment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class MagneticEnchantment extends Enchantment {
    protected MagneticEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) { super(pRarity, pCategory, pApplicableSlots); }

    @Override
    public int getMaxLevel() { return 1; } // Magnetic Ores enchantment max level

    @Override
    public @NotNull Component getFullname(int pLevel) {
        if (pLevel > 0) {
            return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.BOLD, ChatFormatting.GRAY) // Colors used on description name
                    .append(CommonComponents.SPACE) // Separate words and numbers
                    .append(Component.translatable("enchantment.level." + pLevel));  // Name and level enchantment on item, and chat
        }
        return super.getFullname(pLevel);
    }
}