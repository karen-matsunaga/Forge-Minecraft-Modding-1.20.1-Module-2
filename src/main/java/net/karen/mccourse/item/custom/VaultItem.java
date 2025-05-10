package net.karen.mccourse.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

    // Name item
    @Override
    public @NotNull Component getName(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.contains("DisplayName")) {
            return Component.literal(stack.getTag().getString("DisplayName"))
                    .withStyle(Style.EMPTY.withColor(ChatFormatting.GOLD));
        }
        return super.getName(stack);
    }

    // Description item
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, @NotNull TooltipFlag tooltipFlag) {
        tooltip.add(Component.literal("Restored all items from inventory!"));
        super.appendHoverText(stack, level, tooltip, tooltipFlag);
    }
}