package net.karen.mccourse.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VaultItem extends Item {
    public VaultItem(Properties pProperties) { super(pProperties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!level.isClientSide() && stack.hasTag() && Objects.requireNonNull(stack.getTag()).contains("VaultItems")) {
            ListTag itemListTag = stack.getTag().getList("VaultItems", Tag.TAG_COMPOUND);
            for (Tag tag : itemListTag) {
                if (tag instanceof CompoundTag compound) {
                    ItemStack restored = ItemStack.of(compound);
                    boolean added = player.getInventory().add(restored);
                    if (!added) { player.spawnAtLocation(restored, 0.5f); } // Drops on the ground if inventory is full
                }
            }
            // Remove VaultItem after use
            stack.shrink(1);
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }
}