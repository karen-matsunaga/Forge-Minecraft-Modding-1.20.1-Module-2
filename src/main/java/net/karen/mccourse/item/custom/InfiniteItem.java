package net.karen.mccourse.item.custom;

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

public class InfiniteItem extends Item {
    public InfiniteItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack usedStack = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            ItemStack mainHand = player.getMainHandItem();
            if (!mainHand.isEmpty() && mainHand != usedStack) {
                if (!mainHand.isDamageableItem()) { // Item hasn't durability
                    screen(player, "This item has no durability!", ChatFormatting.RED);
                    return InteractionResultHolder.fail(usedStack);
                }
                if (mainHand.getOrCreateTag().getBoolean("Unbreakable")) { // Item has Unbreakable tag
                    screen(player, "This item is already unbreakable!", ChatFormatting.YELLOW);
                    return InteractionResultHolder.fail(usedStack);
                }
                mainHand.getOrCreateTag().putBoolean("Unbreakable", true); // Apply the Unbreakable tag
                screen(player, "Item is now unbreakable!", ChatFormatting.GREEN);
                if (!player.getAbilities().instabuild) {
                    usedStack.shrink(1);
                    player.containerMenu.broadcastChanges();
                }
                return InteractionResultHolder.success(usedStack);
            }
            else {
                screen(player, "Hold the tool in your main hand!", ChatFormatting.RED);
                return InteractionResultHolder.fail(usedStack);
            }
        }
        return InteractionResultHolder.pass(usedStack);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(stack.getDescriptionId()).withStyle(ChatFormatting.AQUA);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel,
                                @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        components.add(Component.literal("Click on item to your tools or armors transform on infinite durability!")
                .withStyle(ChatFormatting.LIGHT_PURPLE));
        super.appendHoverText(stack, pLevel, components, flag);
    }

    // CUSTOM METHOD - Message appears on screen
    public static void screen(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(Component.literal(message).withStyle(color), true);
    }
}