package net.karen.mccourse.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import org.jetbrains.annotations.NotNull;

public class ElytraBoostEnchantment extends Enchantment {
    protected ElytraBoostEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
        super(rarity, category, equipmentSlots);
    }

    @Override
    public int getMaxLevel() { return 5; }

    @Override
    public boolean canEnchant(ItemStack stack) { return stack.is(Items.ELYTRA); }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) { return stack.is(Items.ELYTRA); }

    @Override
    protected boolean checkCompatibility(@NotNull Enchantment enchant) {
        return super.checkCompatibility(enchant) && enchant != Enchantments.BINDING_CURSE && enchant != Enchantments.VANISHING_CURSE;
    }
}