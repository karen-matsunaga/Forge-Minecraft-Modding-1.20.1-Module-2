package net.karen.mccourse.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.function.Predicate;

public class MagicBookBlock extends Block {
    public MagicBookBlock(Properties properties) { super(properties); }

    @Override
    public void stepOn(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!level.isClientSide() && entity instanceof ItemEntity) {
            List<ItemEntity> enchantedBooks = findItems(level, pos, book -> book.getItem().getItem() == Items.ENCHANTED_BOOK),
            enchantedItems = findItems(level, pos, item -> item.getItem().isEnchantable() || item.getItem().isEnchanted());
            if (enchantedBooks.size() > 1) { combineBooks(level, pos, enchantedBooks); } // Combine enchanted books
            else if (enchantedBooks.size() == 1 && !enchantedItems.isEmpty()) {
                applyBookToItem(level, pos, enchantedBooks.get(0), enchantedItems.get(0)); // Combine book and item
            }
            else if (enchantedItems.size() > 1) { combineItems(level, pos, enchantedItems); } // Combine items
        }
        super.stepOn(level, pos, state, entity);
    }

    // CUSTOM METHOD - Enchanted book or items on Magic Book area
    private static List<ItemEntity> findItems(Level level, BlockPos pos, Predicate<ItemEntity> filter) {
        return level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(0.5), filter);
    }

    // CUSTOM METHOD - Grouped enchantments on enchanted book
    private static void groupEnchant(List<ItemEntity> list, Map<Enchantment, Integer> combine,
                                     Item drop, Level level, BlockPos pos) {
        list.forEach(item -> {
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(item.getItem()); // Get item enchantment
            enchants.forEach((ench, lvl) -> combine.merge(ench, lvl, Integer::sum)); // Added and sum enchantment level
            item.discard(); // Remove enchanted book or item
        });
        ItemStack newValue = new ItemStack(drop); // Enchanted book or item with new enchantment
        EnchantmentHelper.setEnchantments(combine, newValue); // Set item enchantments
        dropItem(level, pos, newValue); // Drop item on ground
    }

    // CUSTOM METHOD - COMBINE vanilla or custom enchantments on enchanted book
    private static void combineBooks(Level level, BlockPos pos, List<ItemEntity> books) {
        Map<Enchantment, Integer> combined = new HashMap<>();
        groupEnchant(books, combined, Items.ENCHANTED_BOOK, level, pos);
        playSound(level, pos);
    }

    // CUSTOM METHOD - Transfer enchanted book with enchantments to vanilla or custom item
    private static void applyBookToItem(Level level, BlockPos pos,
                                        ItemEntity bookEntity, ItemEntity toolEntity) {
        ItemStack bookStack = bookEntity.getItem(), toolStack = toolEntity.getItem();
        Map<Enchantment, Integer> bookEnchantments = EnchantmentHelper.getEnchantments(bookStack);
        bookEnchantments.forEach(toolStack::enchant);
        ItemStack enchantedTool = toolStack.copy();
        EnchantmentHelper.setEnchantments(EnchantmentHelper.getEnchantments(toolStack), enchantedTool);
        dropItem(level, pos, enchantedTool); // If you can't deliver, drop it in the world
        toolEntity.discard();
        bookStack.shrink(1);
        if (bookStack.isEmpty()) { bookEntity.discard(); }
        else { bookEntity.setItem(bookStack); }
        playSound(level, pos);
    }

    // CUSTOM METHOD - COMBINE vanilla or custom enchanted items
    private static void combineItems(Level level, BlockPos pos, List<ItemEntity> items) {
        Item firstType = items.get(0).getItem().getItem();
        boolean allSame = items.stream().allMatch(e -> e.getItem().getItem() == firstType);
        if (!allSame) { return; }
        Map<Enchantment, Integer> combined = new HashMap<>();
        groupEnchant(items, combined, firstType, level, pos); // Drop tool WITH all sum enchantments
        for (int i = 0; i < items.size() - 1; i++) { // Drop tools WITHOUT enchantment
            ItemStack clean = new ItemStack(firstType);
            dropItem(level, pos, clean);
        }
        playSound(level, pos);
    }

    // CUSTOM METHOD - Enchanted Book and Item quantities list
    private static void dropItem(Level level, BlockPos pos, ItemStack stack) {
        ItemEntity item = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, stack);
        item.setDeltaMovement(Vec3.ZERO);
        level.addFreshEntity(item);
    }

    // CUSTOM METHOD - Sound when player uses Magic Book block
    private static void playSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.BLOCKS, 1.0F, 1.0F);
    }
}