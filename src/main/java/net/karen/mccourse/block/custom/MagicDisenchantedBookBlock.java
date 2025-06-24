package net.karen.mccourse.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.Map;

public class MagicDisenchantedBookBlock extends Block {
    public MagicDisenchantedBookBlock(Properties properties) { super(properties); }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!level.isClientSide() && entity instanceof ItemEntity itemEntity) {
            ItemStack item = itemEntity.getItem(); // Get real item
            Map<Enchantment, Integer> enchanted = EnchantmentHelper.getEnchantments(item); // Get all enchantments of the item
            // Skip if item has no enchantments - Only process if it's not a previously split book (to avoid infinite loop)
            if (enchanted.isEmpty() || (isBook(item) && enchanted.size() == 1)) { return; }
            if (!isBook(item)) { // 1. Drop enchanted books with the enchantments
                groupedBooks(enchanted, true, level, pos); // It's a TOOL/ARMOR/etc. (Grouped books)
                ItemStack baseItem = item.copy(); // Drop the base item WITHOUT enchantments
                removeTag(List.of("Enchantments", "StoredEnchantments"), baseItem);
                CompoundTag tag = baseItem.getTag();
                if (tag != null && baseItem.hasTag() && tag.isEmpty()) { baseItem.setTag(null); } // Clean up tag if empty
                dropItem(level, pos, baseItem);
            }
            else { groupedBooks(enchanted, false, level, pos); } // 2. Split each enchantment into INDIVIDUAL books
            itemEntity.discard(); // Remove the original item (to avoid reprocessing)
        }
        super.stepOn(level, pos, state, entity);
    }

    // CUSTOM METHOD - Drop ENCHANTED BOOK and BASE ITEM on ground
    private static void dropItem(Level world, BlockPos pos, ItemStack item) {
        ItemEntity drop = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, item);
        drop.setDeltaMovement(Vec3.ZERO);
        world.addFreshEntity(drop);
    }

    private static void groupedBooks(Map<Enchantment, Integer> item, boolean grouped,
                                     Level level, BlockPos pos) {
        if (grouped) { // GROUPED book -> Two or more enchantments
            ItemStack group = new ItemStack(Items.ENCHANTED_BOOK);
            item.forEach((enc, lvl) -> { if (lvl > 0) { EnchantedBookItem.addEnchantment(group, new EnchantmentInstance(enc, lvl)); }});
            dropItem(level, pos, group);
        }
        else { // INDIVIDUAL book -> One enchantment
            item.forEach((enc, lvl) -> {
                ItemStack single = new ItemStack(Items.ENCHANTED_BOOK);
                if (lvl > 0) { EnchantedBookItem.addEnchantment(single, new EnchantmentInstance(enc, lvl)); }
                dropItem(level, pos, single);
            });
        }
    }

    private boolean isBook(ItemStack item) { return item.is(Items.ENCHANTED_BOOK); }

    private static void removeTag(List<String> tags, ItemStack item) {
        tags.forEach(item::removeTagKey);
    }
}