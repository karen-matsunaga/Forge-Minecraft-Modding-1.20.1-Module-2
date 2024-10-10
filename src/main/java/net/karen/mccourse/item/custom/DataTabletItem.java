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
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DataTabletItem extends Item {
    public DataTabletItem(Properties pProperties) {
        super(pProperties);
    }

    // If player clicking without looking a block
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        // Delete the tag in a hand to player
        if(pPlayer.getItemInHand(pUsedHand).hasTag()) {
            pPlayer.getItemInHand(pUsedHand).setTag(new CompoundTag());
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }

    // Enchantment item shine - Saved on particular item
    @Override
    public boolean isFoil(ItemStack pStack) {
        return pStack.hasTag();
    }

    // If found some ore automatically return to data with coordinates (X, Y, Z)
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel,
                                List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {

        // If an item is enchantment
        if(pStack.hasTag()) {
            String currentFoundOre = pStack.getTag().getString("mccourse.found_ore");
            // Item received more information with coordinates
            pTooltipComponents.add(Component.literal(currentFoundOre));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

}
