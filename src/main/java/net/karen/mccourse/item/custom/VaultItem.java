package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
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
import static net.karen.mccourse.util.ChatUtil.componentLiteral;
import static net.karen.mccourse.util.ChatUtil.tooltipLine;

public class VaultItem extends Item {
    public VaultItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand); // Player has Vault item on main hand
        if (!level.isClientSide() && stack.hasTag() && stack.getTag() != null && stack.getTag().contains("VaultItems")) {
            ListTag itemListTag = stack.getTag().getList("VaultItems", Tag.TAG_COMPOUND);
            itemListTag.forEach(tag -> {
                if (tag instanceof CompoundTag compound) {
                    ItemStack restored = ItemStack.of(compound); // Drops on the ground if inventory is full
                    if (!player.getInventory().add(restored)) { player.spawnAtLocation(restored, 0.5f); }
                }
            });
            stack.shrink(1); // Remove VaultItem after use
            return InteractionResultHolder.success(stack);
        }
        return InteractionResultHolder.pass(stack);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        if (stack.getTag() != null && stack.getTag().contains("DisplayName")) {
            return componentLiteral(stack.getTag().getString("DisplayName")); // Name item
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip,
                                @NotNull TooltipFlag tooltipFlag) {
        tooltipLine(tooltip, "Restored all items from inventory!", Utils.darkBlue); // Description item
    }
}