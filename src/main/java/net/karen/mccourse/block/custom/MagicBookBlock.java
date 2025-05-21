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
import java.util.function.Predicate;

public class MagicBookBlock extends Block {
    public MagicBookBlock(Properties pProperties) { super(pProperties); }

    @Override
    public void stepOn(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!level.isClientSide() && entity instanceof ItemEntity) {
            AABB area = new AABB(pos).inflate(0.5);
            // Collect enchanted books and enchanted items
            List<ItemEntity> enchantedBooks = items(level, area, books -> books.getItem().getItem() == Items.ENCHANTED_BOOK);
            List<ItemEntity> enchantedItems = items(level, area, items -> items.getItem().isEnchantable());

            // Combine books if there are MULTIPLE
            if (enchantedBooks.size() > 1) {
                Map<Enchantment, Integer> enchantSumLevels = new HashMap<>(); // SUM enchanted book with SAME enchantment
                // Get enchantments to EACH enchanted book
                enchantedBooks.stream().map(bookEntity -> EnchantmentHelper.getEnchantments(bookEntity.getItem()))
                        .forEachOrdered(enchants -> enchants.forEach((key, value)
                                -> enchantSumLevels.merge(key, value, Integer::sum)));

                Map<Enchantment, Integer> finalEnchants = new HashMap<>(enchantSumLevels); // Enchanted Book with NEW value
                enchantedBooks.forEach(Entity::discard); // Remove original books

                // Create and drop the NEW combined book
                ItemStack combinedBook = new ItemStack(Items.ENCHANTED_BOOK);
                EnchantmentHelper.setEnchantments(finalEnchants, combinedBook);

                level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5,
                        pos.getY() + 1, pos.getZ() + 0.5, combinedBook));

                sound(level, pos);
            }
            // Enchant ITEM with enchanted book
            else if (enchantedBooks.size() == 1 && !enchantedItems.isEmpty()) {
                // Get the first item of ITEM and ENCHANTED BOOK
                ItemEntity toolItem = enchantedItems.get(0);
                ItemEntity enchantedBookItem = enchantedBooks.get(0);

                ItemStack toolStack = toolItem.getItem(); // TOOL, ARMOR, etc.
                ItemStack bookStack = enchantedBookItem.getItem(); // ENCHANTED BOOK with enchantment

                // Transfer all enchantments of enchanted book to TOOL, ARMOR, etc.
                Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(bookStack);
                enchantments.forEach(toolStack::enchant);

                toolItem.setItem(toolStack); // Receive item with updated enchantment levels
                bookStack.shrink(1); // Consumes 1 book
                if (bookStack.isEmpty()) { enchantedBookItem.discard(); } // Remove original item
                else { enchantedBookItem.setItem(bookStack); } // Return original item
                sound(level, pos);
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    // CUSTOM METHOD - Enchanted Book and Item quantities list
    private static List<ItemEntity> items(Level level, AABB area, Predicate<ItemEntity> filter) {
        return level.getEntitiesOfClass(ItemEntity.class, area, filter);
    }

    // CUSTOM METHOD - Sound when player uses Magic Book block
    private static void sound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}