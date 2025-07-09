package net.karen.mccourse.item.custom;

import net.karen.mccourse.item.ModItems;
import net.minecraft.ChatFormatting;
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
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class InfiniteItem extends Item {
    private final String nameTag;
    public InfiniteItem(Properties properties, String nameTag) {
        super(properties);
        this.nameTag = nameTag;
    }

    public String getNameTag() { return this.nameTag; } // Used on Infinite Network

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack usedStack = player.getItemInHand(hand), mainHand = player.getMainHandItem();
        if (!level.isClientSide() && !mainHand.isEmpty() && mainHand != usedStack) {
            CompoundTag getTag =  mainHand.getTag(), createTag = mainHand.getOrCreateTag();
            boolean hasUnbTag = !mainHand.isDamageableItem() || (getTag != null && getTag.getBoolean("Unbreakable"));
            if (hasUnbTag) { // Item hasn't durability or has Unbreakable tag
                return fail(player, "This item has no durability!", red, usedStack);
            }
            if (getTag != null && getTag.getBoolean("LuckyBomb")) { // Item has Lucky Bomb tag
                return fail(player, "This item is already " + nameTag + "!", yellow, usedStack);
            }
            createTag.putBoolean(nameTag, true); // Apply the Unbreakable | Lucky Bomb tags
            player(player, "Item is now " + nameTag + "!", green);
            consumeInfinite(player, usedStack);
            return InteractionResultHolder.success(usedStack);
        }
        else { fail(player, "Hold the tool in your main hand!", red, usedStack); }
        return InteractionResultHolder.pass(usedStack);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) { return componentTranslatable(stack.getDescriptionId(), aqua); }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        String message = "Click on item to your tools or armors transform on ",
                  item = this.toString().replace("_", " ");
        if (stack.is(ModItems.INFINITE.get())) { tooltipLine(list, message + itemLines(item) + " durability!", purple); }
        if (stack.is(ModItems.LUCKY_BOMB.get())) { tooltipLine(list, message + itemLines(item) + " tag!", gold); }
        super.appendHoverText(stack, pLevel, list, flag);
    }

    // CUSTOM METHOD - Fail messages
    public InteractionResultHolder<ItemStack> fail(Player player, String message,
                                                   ChatFormatting color, ItemStack usedStack) {
        player(player, message, color);
        return InteractionResultHolder.fail(usedStack);
    }
}