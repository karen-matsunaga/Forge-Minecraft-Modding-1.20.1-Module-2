package net.karen.mccourse.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class MagicBlock extends Block {
    public MagicBlock(Properties pProperties) { super(pProperties); }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState pState, Level pLevel, @NotNull BlockPos pPos,
                                          @NotNull Player pPlayer, @NotNull InteractionHand pHand,
                                          @NotNull BlockHitResult pHit) {
        pLevel.playSound(pPlayer, pPos, SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.BLOCKS, 1f, 1f);
        return InteractionResult.SUCCESS;
    }

    @Override
    public void stepOn(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull Entity pEntity) {
        if (pEntity instanceof ItemEntity itemEntity) {
            // Get real item
            ItemStack item = itemEntity.getItem();

            // Get all enchantments of the item
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);

            // Skip if item has no enchantments
            if (enchantments.isEmpty()) return;

            // Only process if it's not a previously split book (to avoid infinite loop)
            if (item.is(Items.ENCHANTED_BOOK) && enchantments.size() == 1) return;

            // Drop enchanted books with the enchantments
            if (!item.is(Items.ENCHANTED_BOOK)) {
                // It's a tool/armor/etc.
                ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    EnchantedBookItem.addEnchantment(enchantedBook,
                            new EnchantmentInstance(entry.getKey(), entry.getValue()));
                }
                pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX() + 0.5,
                        pPos.getY() + 1, pPos.getZ() + 0.5, enchantedBook));

                // Drop the base item without enchantments
                ItemStack baseItem = item.copy();
                baseItem.removeTagKey("Enchantments");
                baseItem.removeTagKey("StoredEnchantments");

                // Clean up tag if empty
                if (baseItem.hasTag() && Objects.requireNonNull(baseItem.getTag()).isEmpty()) {
                    baseItem.setTag(null);
                }

                pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX() + 0.5,
                        pPos.getY() + 1, pPos.getZ() + 0.5, baseItem));
            }
            // Split each enchantment into individual books
            else {
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    ItemStack singleBook = new ItemStack(Items.ENCHANTED_BOOK);
                    EnchantedBookItem.addEnchantment(singleBook,
                            new EnchantmentInstance(entry.getKey(), entry.getValue()));

                    pLevel.addFreshEntity(new ItemEntity(pLevel, pPos.getX() + 0.5,
                            pPos.getY() + 1, pPos.getZ() + 0.5, singleBook));
                }
            }

            // Remove the original item (to avoid reprocessing)
            itemEntity.discard();
        }
        super.stepOn(pLevel, pPos, pState, pEntity);
    }
}