package net.karen.mccourse.item.custom;

import net.karen.mccourse.item.ModItems;
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
        ItemStack stack = player.getItemInHand(hand); // Player has Mccourse Bottle on main hand
        CompoundTag tag = stack.getOrCreateTag();
        int storedLevels = tag.getInt("StoredLevels"); // StoreLevels NBT tag to save and to store XP
        if (!level.isClientSide() && player instanceof ServerPlayer serverPlayer) {
            if (player.isShiftKeyDown()) { // SHIFT + RIGHT click
                mccourseXp(serverPlayer, tag, player, storedLevels, storedLevels, 0, storedLevels + " levels!");
            }
            else { // RIGHT click
                mccourseXp(serverPlayer, tag, player, storedLevels, 1, storedLevels - 1, "1 level!");
            }
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        return Component.translatable(stack.getDescriptionId()).withStyle(ChatFormatting.DARK_GREEN);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                List<Component> tooltip, @NotNull TooltipFlag flag) {
        int xp = stack.getOrCreateTag().getInt("StoredLevels");
        tooltip.add(Component.literal("Stored XP: " + xp + " / " + storeXp).withStyle(ChatFormatting.YELLOW));
        tooltip.add(Component.literal("Left click: Store 1 XP- level").withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("Shift + Left click: Store all XP-").withStyle(ChatFormatting.RED));
        tooltip.add(Component.literal("Right click: Restore 1 XP+ level").withStyle(ChatFormatting.GREEN));
        tooltip.add(Component.literal("Shift + Right click: Restore all XP+").withStyle(ChatFormatting.GREEN));
        super.appendHoverText(stack, level, tooltip, flag);
    }

    // CUSTOM METHOD - Message appears on screen
    public static void screen(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(Component.literal(message).withStyle(color), true);
    }

    // CUSTOM METHOD - Mccourse Bottle with XP
    public static ItemStack createMccourseBottleWithXP(int levels) {
        ItemStack stack = new ItemStack(ModItems.MCCOURSE_BOTTLE.get());
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt("StoredLevels", levels); // Added levels on Mccourse Bottle item
        return stack;
    }

    // CUSTOM METHOD - Mccourse Bottle RESTORE system
    private void mccourseXp(ServerPlayer serverPlayer, CompoundTag tag, Player player,
                            int storedLevels, int amount, int store, String message) {
        ItemStack heldItem = player.getMainHandItem();
        if (player.getCooldowns().isOnCooldown(heldItem.getItem())) { // Check if it is already on cooldown
            screen(player, "Wait before using again!", ChatFormatting.YELLOW);
            return;
        }
        if (storedLevels > 0) { // Restore levels
            serverPlayer.giveExperienceLevels(amount);
            tag.putInt("StoredLevels", store);
            screen(player, "Restored " + message, ChatFormatting.GREEN);
            player.getCooldowns().addCooldown(heldItem.getItem(), 20); // Applies 1 second cooldown (20 ticks)
        }
        else { screen(player, "Bottle is empty.", ChatFormatting.RED); }
    }
}