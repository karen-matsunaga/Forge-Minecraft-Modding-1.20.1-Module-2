package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ChatUtil;
import net.karen.mccourse.util.Utils;
import net.minecraft.ChatFormatting;
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
import static net.karen.mccourse.util.Utils.consumeInfinite;

public class InfiniteItem extends Item {
    public InfiniteItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack usedStack = player.getItemInHand(hand), mainHand = player.getMainHandItem();
        if (!level.isClientSide() && !mainHand.isEmpty() && mainHand != usedStack) {
            // Item hasn't durability
            if (!mainHand.isDamageableItem()) { return fail(player, "This item has no durability!", Utils.red, usedStack); }
            if (mainHand.getOrCreateTag().getBoolean("Unbreakable")) { // Item has Unbreakable tag
                return fail(player, "This item is already unbreakable!", Utils.yellow, usedStack);
            }
            mainHand.getOrCreateTag().putBoolean("Unbreakable", true); // Apply the Unbreakable tag
            ChatUtil.player(player, "Item is now unbreakable!", Utils.green);
            consumeInfinite(player, usedStack);
            return InteractionResultHolder.success(usedStack);
        }
        else { fail(player, "Hold the tool in your main hand!", Utils.red, usedStack); }
        return InteractionResultHolder.pass(usedStack);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return ChatUtil.componentTranslatable(stack.getDescriptionId(), Utils.aqua);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        ChatUtil.tooltipLine(list, "Click on item to your tools or armors transform on infinite durability!", Utils.purple);
        super.appendHoverText(stack, pLevel, list, flag);
    }

    // CUSTOM METHOD - Fail messages
    public InteractionResultHolder<ItemStack> fail(Player player, String message,
                                                   ChatFormatting color, ItemStack usedStack) {
        ChatUtil.player(player, message, color);
        return InteractionResultHolder.fail(usedStack);
    }
}