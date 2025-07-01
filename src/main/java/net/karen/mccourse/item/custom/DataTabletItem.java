package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ChatUtil;
import net.karen.mccourse.util.Utils;
import net.minecraft.nbt.CompoundTag;
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

public class DataTabletItem extends Item {
    public DataTabletItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack mainHand = player.getItemInHand(hand); // If player clicking without looking a block delete the tag in hand
        if (mainHand.hasTag()) { mainHand.setTag(new CompoundTag()); }
        return super.use(level, player, hand);
    }

    @Override
    public boolean isFoil(ItemStack stack) { return stack.hasTag(); } // Enchantment item shine - Saved on particular item

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        // If found some ore automatically return to data with coordinates (X, Y, Z)
        if (stack.getTag() != null && stack.hasTag()) { // If an item is enchantment
            String currentFoundOre = stack.getTag().getString("mccourse.found_ore");
            ChatUtil.tooltipLine(list, currentFoundOre, Utils.gold); // Item received more information with coordinates
        }
        super.appendHoverText(stack, level, list, flag);
    }
}