package net.karen.mccourse.enchantment;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class RainbowEnchantment extends Enchantment {
    protected RainbowEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) { super(pRarity, pCategory, pApplicableSlots); }

    @Override
    public int getMaxLevel() { return 1; } // Rainbow enchantment max level

    @Override
    public @NotNull Component getFullname(int pLevel) {
        if (pLevel > 0) {
            return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.BOLD, ChatFormatting.GREEN)
                    .append(CommonComponents.SPACE)
                    .append(Component.translatable(String.valueOf(pLevel)));
        }
        return super.getFullname(pLevel);
    }

    @Override
    public boolean isTreasureOnly() { return true; } // Rainbow custom enchantment exclusive on chest

    // Auto Smelt and Rainbow Enchantment doesn't work together
    public boolean checkCompatibility(@NotNull Enchantment pEnch) {
        return super.checkCompatibility(pEnch) && pEnch != ModEnchantments.AUTO_SMELT.get();
    }
}
