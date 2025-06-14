package net.karen.mccourse.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
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

public class MccourseBottleItem extends Item {
    public static int storeXp;
    public final int amountXp;

    public MccourseBottleItem(Properties properties, int storeXp, int amountXp) {
        super(properties);
        MccourseBottleItem.storeXp = storeXp;
        this.amountXp = amountXp;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();
        int storedLevels = tag.getInt("StoredLevels");
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            if (player.isShiftKeyDown()) {
                if (storedLevels > 0) { // Restore levels
                    serverPlayer.giveExperienceLevels(storedLevels);
                    tag.putInt("StoredLevels", 0);
                    screen(player, "You restored " + storedLevels + " levels.", ChatFormatting.GREEN);
                }
                else { screen(player, "Bottle is empty.", ChatFormatting.RED); }
            }
            else { screen(player, "Shift + Right Click to restore levels.", ChatFormatting.YELLOW); } // There's nothing
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, @NotNull TooltipFlag flag) {
        int xp = stack.getOrCreateTag().getInt("StoredLevels");
        tooltip.add(Component.literal("Stored XP: " + xp + " / " + storeXp).withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("Left click: Store XP-").withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("Shift + Left click: Store all XP-").withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("Shift + Right click: Restore all XP+").withStyle(ChatFormatting.GREEN));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    public static void screen(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(Component.literal(message).withStyle(color), true);
    }
}