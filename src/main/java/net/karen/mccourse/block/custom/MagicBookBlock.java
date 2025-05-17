package net.karen.mccourse.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class MagicBookBlock extends Block {
    public MagicBookBlock(Properties pProperties) { super(pProperties); }

    @Override
    public void stepOn(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!level.isClientSide() && entity instanceof ItemEntity) {
            AABB area = new AABB(pos).inflate(0.5);
            // Collect enchanted books and enchanted items
            List<ItemEntity> enchantedBooks = level.getEntitiesOfClass(ItemEntity.class, area,
                    books -> books.getItem().getItem() == Items.ENCHANTED_BOOK);

            List<ItemEntity> enchantedItems = level.getEntitiesOfClass(ItemEntity.class, area,
                    items -> items.getItem().isEnchantable());

            // Combine books if there are multiple
            if (enchantedBooks.size() > 1) {
                // Sum enchanted book with same enchantment
                Map<Enchantment, Integer> enchantSumLevels = new HashMap<>();
                for (ItemEntity bookEntity : enchantedBooks) {
                    // Get enchantments to each enchanted book
                    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(bookEntity.getItem());
                    for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                        enchantSumLevels.merge(entry.getKey(), entry.getValue(), Integer::sum);
                    }
                }

                // Enchanted Book with new value
                Map<Enchantment, Integer> finalEnchants = new HashMap<>();
                for (Map.Entry<Enchantment, Integer> entry : enchantSumLevels.entrySet()) {
                    Enchantment ench = entry.getKey();
                    int sumLevel = entry.getValue();
                    finalEnchants.put(ench, sumLevel);
                }

                // Remove original books
                enchantedBooks.forEach(Entity::discard);

                // Create and drop the new combined book
                ItemStack combinedBook = new ItemStack(Items.ENCHANTED_BOOK);
                EnchantmentHelper.setEnchantments(finalEnchants, combinedBook);

                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5,
                        pos.getY() + 1, pos.getZ() + 0.5, combinedBook));

                sound(level, pos);
            }
            // Enchant item with book
            else if (enchantedBooks.size() == 1 && !enchantedItems.isEmpty()) {
                // Get the first item of toolItem and enchantedBookItem
                ItemEntity toolItem = enchantedItems.get(0);
                ItemEntity enchantedBookItem = enchantedBooks.get(0);

                // Tool, armor, etc.
                ItemStack toolStack = toolItem.getItem();
                // Enchanted book with enchantment
                ItemStack bookStack = enchantedBookItem.getItem();

                // Transfer all enchantments of enchanted book to Tool, armor, etc.
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(bookStack);
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    toolStack.enchant(entry.getKey(), entry.getValue());
                }

                // Receive item with updated enchantment levels
                toolItem.setItem(toolStack);

                // Consumes 1 book
                bookStack.shrink(1);
                if (bookStack.isEmpty()) {
                    // Remove original item
                    enchantedBookItem.discard();
                }
                else {
                    // Return original item
                    enchantedBookItem.setItem(bookStack);
                }
                sound(level, pos);
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    // CUSTOM METHOD - Sound
    private static void sound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE,
                SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}