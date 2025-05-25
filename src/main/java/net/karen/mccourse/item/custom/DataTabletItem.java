package net.karen.mccourse.item.custom;

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

    // If player clicking without looking a block
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        // Delete the tag in a hand to player
        if (player.getItemInHand(hand).hasTag()) { player.getItemInHand(hand).setTag(new CompoundTag()); }
        return super.use(level, player, hand);
    }

    // Enchantment item shine - Saved on particular item
    @Override
    public boolean isFoil(ItemStack stack) { return stack.hasTag(); }

    // If found some ore automatically return to data with coordinates (X, Y, Z)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull List<Component> components,
                                @NotNull TooltipFlag tooltipFlag) {
        // If an item is enchantment
        if (stack.getTag() != null && stack.hasTag()) {
            String currentFoundOre = stack.getTag().getString("mccourse.found_ore");
            components.add(Component.literal(currentFoundOre)); // Item received more information with coordinates
        }
        super.appendHoverText(stack, level, components, tooltipFlag);
    }
}