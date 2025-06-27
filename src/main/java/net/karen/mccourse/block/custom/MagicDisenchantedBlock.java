package net.karen.mccourse.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class MagicDisenchantedBlock extends Block {
    private final int type;

    public MagicDisenchantedBlock(Properties properties, int type) {
        super(properties);
        this.type = type;
    }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!level.isClientSide() && entity instanceof ItemEntity itemEntity) {
            ItemStack item = itemEntity.getItem(); // Get real item
            switch (type) {
                case 1 -> individualEnchantedBook(level, pos, item, itemEntity); // Decrement more enchantment level
                case 2 -> groupedEnchantedBook(level, pos, item, itemEntity); // Original enchantment level
            }
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter pLevel,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        if (type == 1) {
            tooltipLine(tooltip, "1. Enchanted Tool NEAR -> Grouped book + Tool without enchantments;", aqua);
            tooltipLine(tooltip, "2. Enchanted Tool FAR -> Separate book + Tool without enchantments.", darkAqua);
        }
        if (type == 2) { tooltipLine(tooltip, "The books separate but remain at the original level.", black); }
        super.appendHoverText(stack, pLevel, tooltip, flag);
    }

    // CUSTOM METHOD - TYPE 1
    private static void individualEnchantedBook(Level level, BlockPos pos,
                                               ItemStack item, ItemEntity itemEntity) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
        if (enchantments.isEmpty()) { return; }
        if (item.getItem().equals(Items.ENCHANTED_BOOK)) { // Is enchanted book divided on INDIVIDUAL books
            List<ItemStack> separatedBooks = extractEnchantments(item);
            separatedBooks.forEach(book -> dropEnchanted(level, pos, book));
        }
        else { // Is tool, armor, etc. an enchanted book with all enchantments and base item
            groupedEnch(enchantments, level, pos);
            ItemStack baseItem = item.copy(); // Remove enchantments of original item
            removeTag(List.of("Enchantments", "StoredEnchantments"), baseItem);
            CompoundTag tag = baseItem.getTag();
            if (tag != null && tag.isEmpty()) { baseItem.setTag(null); }
            dropEnchanted(level, pos, baseItem); // Drop base item WITHOUT enchantment
        }
        itemEntity.discard(); // remover item original
    }

    // CUSTOM METHOD - TYPE 2
    private static void groupedEnchantedBook(Level level, BlockPos pos,
                                            ItemStack item, ItemEntity itemEntity) {
        Map<Enchantment, Integer> enchanted = EnchantmentHelper.getEnchantments(item); // Get all enchantments of the item
        // Skip if item has no enchantments - Only process if it's not a previously split book (to avoid infinite loop)
        if (enchanted.isEmpty() || (isBook(item) && enchanted.size() == 1)) { return; }
        if (!isBook(item)) { // 1. Drop enchanted books with the enchantments
            groupedBooks(enchanted, true, level, pos); // It's a TOOL/ARMOR/etc. (Grouped books)
            ItemStack baseItem = item.copy(); // Drop the base item WITHOUT enchantments
            removeTag(List.of("Enchantments", "StoredEnchantments"), baseItem);
            CompoundTag tag = baseItem.getTag();
            if (tag != null && baseItem.hasTag() && tag.isEmpty()) { baseItem.setTag(null); } // Clean up tag if empty
            dropEnchanted(level, pos, baseItem);
        }
        // 2. Split each enchantment into INDIVIDUAL books -> ENCHANTED BOOK
        else { groupedBooks(enchanted, false, level, pos); }
        itemEntity.discard(); // Remove the original item (to avoid reprocessing)
    }

    // CUSTOM METHOD - Extract enchantments on separate enchanted books (Divided per level)
    private static List<ItemStack> extractEnchantments(ItemStack enchantedBook) {
        List<ItemStack> result = new ArrayList<>();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(enchantedBook);
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();
            // Enchantment level 10 or below (INDIVIDUAL books with enchantment level 1)
            if (level <= 10) { for (int i = 0; i < level; i++) { result.add(createEnchantedBook(enchantment, 1)); } }
            else { // Enchantment level 11 or above (INDIVIDUAL books with enchantment level 10 + remaining enchantment level)
                int tens = level / 10, remainder = level % 10;
                for (int i = 0; i < tens; i++) result.add(createEnchantedBook(enchantment, 10));
                if (remainder > 0) { result.add(createEnchantedBook(enchantment, remainder)); }
            }
        }
        return result;
    }

    // CUSTOM METHOD - Removed all enchantments of ENCHANTED tool, armor, etc. from Enchantments NBT
    private static void removeTag(List<String> tags, ItemStack item) {
        tags.forEach(item::removeTagKey);
    }

    // CUSTOM METHOD - GROUPED -> [Item -> Group Book] | NOT GROUPED -> [Group Book -> Single Book]
    private static void groupedBooks(Map<Enchantment, Integer> item, boolean grouped,
                                    Level level, BlockPos pos) {
        if (grouped) { groupedEnch(item, level, pos); } // GROUPED book -> Two or more enchantments
        if (!grouped) { individualEnch(item, level, pos); } // INDIVIDUAL book -> One enchantment
    }

    // CUSTOM METHOD - Item dropped is an ENCHANTED (tool, armor, etc.) or an ENCHANTED BOOK
    private static boolean isBook(ItemStack item) { return item.is(Items.ENCHANTED_BOOK); }
}