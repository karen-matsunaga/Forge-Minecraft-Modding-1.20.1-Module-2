package net.karen.mccourse.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class MultiplierEnchantment extends Enchantment {
    protected MultiplierEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... equipmentSlots) {
        super(rarity, category, equipmentSlots);
    }

    @Override
    public int getMinLevel() { return 2; }

    @Override
    public int getMaxLevel() { return 10; }

    @Override
    public boolean canEnchant(ItemStack stack) {
        Item item = stack.getItem();
        return item instanceof SwordItem || item instanceof PickaxeItem || item instanceof AxeItem ||
               item instanceof ShovelItem || item instanceof EnchantedBookItem;
    }
}