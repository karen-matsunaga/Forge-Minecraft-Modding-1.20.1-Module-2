package net.karen.mccourse.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class BookDisenchantedBlock extends Block {
    public BookDisenchantedBlock(Properties properties) { super(properties); }

    @Override
    public void stepOn(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!level.isClientSide() && entity instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();
            if (stack.getItem() == Items.ENCHANTED_BOOK) {
                List<ItemStack> newBooks = extractEnchantments(stack);
                for (ItemStack book : newBooks) {
                    level.addFreshEntity(new ItemEntity(level,
                            pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, book));
                }
                itemEntity.discard(); // Original book is removed
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    private List<ItemStack> extractEnchantments(ItemStack enchantedBook) {
        List<ItemStack> result = new ArrayList<>();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(enchantedBook);
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();
            // Divide into level 1 books
            if (level <= 10) { for (int i = 0; i < level; i++) { result.add(createBook(enchantment, 1)); } }
            else { // Divide into level 10 books and the rest at level 1
                for (int i = 0; i < (level / 10); i++) { result.add(createBook(enchantment, 10)); }
                for (int i = 0; i < (level % 10); i++) { result.add(createBook(enchantment, 1)); }
            }
        }
        return result;
    }

    private ItemStack createBook(Enchantment enchantment, int level) {
        ItemStack newBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(newBook, new EnchantmentInstance(enchantment, level));
        return newBook;
    }
}