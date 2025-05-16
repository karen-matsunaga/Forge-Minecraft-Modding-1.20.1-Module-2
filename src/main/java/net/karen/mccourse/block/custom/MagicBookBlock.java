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
        if (!level.isClientSide() && entity instanceof ItemEntity itemEntity) {
            // Checks if item is enchanted book
            ItemStack stack = itemEntity.getItem();

            if (stack.getItem() == Items.ENCHANTED_BOOK) {
                // Gets all ItemEntity entities from enchanted books within the block
                List<ItemEntity> nearbyBooks = level.getEntitiesOfClass(ItemEntity.class,
                        new AABB(pos).inflate(0.5), e -> e.getItem().getItem() == Items.ENCHANTED_BOOK);

                if (nearbyBooks.size() > 1) {
                    // Map to count how many times each enchantment appears
                    Map<Enchantment, Integer> enchantCount = new HashMap<>();

                    // Counts the enchantments of all the books
                    for (ItemEntity bookEntity : nearbyBooks) {
                        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(bookEntity.getItem());
                        for (Enchantment enchant : enchants.keySet()) {
                            enchantCount.merge(enchant, 1, Integer::sum);
                        }
                    }

                    // Checks if any enchantment appears in more than one book
                    boolean hasCommonEnchants = enchantCount.values().stream().anyMatch(count -> count > 1);

                    if (!hasCommonEnchants) {
                        // If there are no repeated enchantments between the books, it matches
                        Map<Enchantment, Integer> combined = new HashMap<>();

                        // Combines the enchantments of all found books
                        for (ItemEntity bookEntity : nearbyBooks) {
                            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(bookEntity.getItem());
                            for (Map.Entry<Enchantment, Integer> e : enchants.entrySet()) {
                                combined.merge(e.getKey(), e.getValue(), Math::max);
                            }
                        }

                        // Remove the original books from the world
                        for (ItemEntity bookEntity : nearbyBooks) {
                            bookEntity.discard();
                        }

                        // Create the combined book
                        ItemStack combinedBook = new ItemStack(Items.ENCHANTED_BOOK);
                        EnchantmentHelper.setEnchantments(combined, combinedBook);

                        // Spawn the combined book in the center of the block
                        level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5,
                                pos.getY() + 1, pos.getZ() + 0.5, combinedBook));

                        // Sound for feedback
                        level.playSound(null, pos, SoundEvents.ENCHANTMENT_TABLE_USE,
                                SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                }
            }
        }
        super.stepOn(level, pos, state, entity);
    }
}