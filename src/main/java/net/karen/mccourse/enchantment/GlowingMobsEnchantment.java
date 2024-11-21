package net.karen.mccourse.enchantment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class GlowingMobsEnchantment extends Enchantment {
    protected GlowingMobsEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) { super(pRarity, pCategory, pApplicableSlots); }

    // Glowing Mobs's max level enchantment
    @Override
    public int getMaxLevel() { return 1; }

    @Override
    public @NotNull Component getFullname(int pLevel) {
        if (pLevel > 0) {
            return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.BOLD, ChatFormatting.YELLOW) // Colors used on name enchantment
                    .append(CommonComponents.SPACE) // Separate name and level
                    .append(Component.translatable(String.valueOf(pLevel)));  // Level enchantment on item, chat, and enchanted book
        }
        return super.getFullname(pLevel);
    }
}