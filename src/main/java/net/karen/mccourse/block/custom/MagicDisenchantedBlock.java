package net.karen.mccourse.block.custom;

import net.minecraft.ChatFormatting;
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
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;

public class MagicDisenchantedBlock extends Block {
    public MagicDisenchantedBlock(Properties properties) { super(properties); }

    @Override
    public void stepOn(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Entity entity) {
        if (!level.isClientSide() && entity instanceof ItemEntity itemEntity) {
            ItemStack stack = itemEntity.getItem();
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
            if (enchantments.isEmpty()) { return; }
            if (stack.getItem() == Items.ENCHANTED_BOOK) { // Is enchanted book divided on INDIVIDUAL books
                List<ItemStack> separatedBooks = extractEnchantments(stack);
                separatedBooks.forEach(book -> dropItem(level, pos, book));
            }
            else { // Is tool, armor, etc. an enchanted book with all enchantments and base item
                ItemStack grouped = new ItemStack(Items.ENCHANTED_BOOK);
                enchantments.forEach((ench, lvl) -> EnchantedBookItem.addEnchantment(grouped, new EnchantmentInstance(ench, lvl)));
                dropItem(level, pos, grouped); // Drop grouped enchanted book WITH enchantment
                ItemStack baseItem = stack.copy(); // Remove enchantments of original item
                removeTag(List.of("Enchantments", "StoredEnchantments"), baseItem);
                CompoundTag tag = baseItem.getTag();
                if (tag != null && tag.isEmpty()) { baseItem.setTag(null); }
                dropItem(level, pos, baseItem); // Drop base item WITHOUT enchantment
            }
            itemEntity.discard(); // remover item original
        }
        super.stepOn(level, pos, state, entity);
    }

    // CUSTOM METHOD - Item drop on ground of world
    private static void dropItem(Level world, BlockPos pos, ItemStack item) {
        ItemEntity drop = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, item);
        drop.setDeltaMovement(Vec3.ZERO);
        world.addFreshEntity(drop);
    }

    // CUSTOM METHOD - Extract enchantments on separate enchanted books (Divided per level)
    private List<ItemStack> extractEnchantments(ItemStack enchantedBook) {
        List<ItemStack> result = new ArrayList<>();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(enchantedBook);
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            Enchantment enchantment = entry.getKey();
            int level = entry.getValue();
            // Enchantment level 10 or below (INDIVIDUAL books with enchantment level 1)
            if (level <= 10) { for (int i = 0; i < level; i++) { result.add(createBook(enchantment, 1)); } }
            else { // Enchantment level 11 or above (INDIVIDUAL books with enchantment level 10 + remaining enchantment level)
                int tens = level / 10, remainder = level % 10;
                for (int i = 0; i < tens; i++) result.add(createBook(enchantment, 10));
                if (remainder > 0) { result.add(createBook(enchantment, remainder)); }
            }
        }
        return result;
    }

    // CUSTOM METHOD - Create enchanted book
    private ItemStack createBook(Enchantment enchantment, int level) {
        ItemStack newBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(newBook, new EnchantmentInstance(enchantment, level));
        return newBook;
    }

    // CUSTOM METHOD - Remove enchantment tags
    private static void removeTag(List<String> tags, ItemStack item) {
        tags.forEach(item::removeTagKey);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter pLevel,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.literal("1. Enchanted Tool NEAR -> Grouped book + Tool without enchantments")
                .withStyle(ChatFormatting.AQUA));
        tooltip.add(Component.literal("2. Enchanted Tool FAR -> Separate book + Tool without enchantments")
                .withStyle(ChatFormatting.DARK_AQUA));
        super.appendHoverText(stack, pLevel, tooltip, flag);
    }
}