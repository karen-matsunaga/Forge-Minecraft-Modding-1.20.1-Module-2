package net.karen.mccourse.block.custom;

import net.minecraft.core.BlockPos;
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
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class MagicBlock extends Block {
    public MagicBlock(Properties pProperties) { super(pProperties); }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!level.isClientSide() && entity instanceof ItemEntity itemEntity) {
            ItemStack item = itemEntity.getItem(); // Get real item
            // Get all enchantments of the item
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
            boolean isBook = item.is(Items.ENCHANTED_BOOK);

            // Skip if item has no enchantments
            // Only process if it's not a previously split book (to avoid infinite loop)
            if (enchantments.isEmpty() || (isBook && enchantments.size() == 1)) { return; }

            // Drop enchanted books with the enchantments
            if (!isBook) {
                // It's a TOOL/ARMOR/etc.
                ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                enchantments.forEach((key, value) -> EnchantedBookItem.addEnchantment(enchantedBook,
                        new EnchantmentInstance(key, value)));

                // Drop the base item WITHOUT enchantments
                ItemStack baseItem = item.copy();
                baseItem.removeTagKey("Enchantments");
                baseItem.removeTagKey("StoredEnchantments");

                // Clean up tag if empty
                if (baseItem.getTag() != null && baseItem.hasTag() && baseItem.getTag().isEmpty()) { baseItem.setTag(null); }

                dropItem(level, pos, enchantedBook);
                dropItem(level, pos, baseItem);
            }
            // Split each enchantment into individual books
            else {
                enchantments.forEach((key, value) -> {
                    ItemStack singleBook = new ItemStack(Items.ENCHANTED_BOOK);
                    EnchantedBookItem.addEnchantment(singleBook, new EnchantmentInstance(key, value));
                    dropItem(level, pos, singleBook);
                });
            }
            itemEntity.discard(); // Remove the original item (to avoid reprocessing)
        }
        super.stepOn(level, pos, state, entity);
    }

    // CUSTOM METHOD - Drop ENCHANTED BOOK and BASE ITEM on ground
    private static void dropItem(Level world, BlockPos pos, ItemStack stack) {
        world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5,
                pos.getY() + 1, pos.getZ() + 0.5, stack));
    }
}