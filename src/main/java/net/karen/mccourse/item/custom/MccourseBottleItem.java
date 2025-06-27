package net.karen.mccourse.item.custom;

import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.util.Utils;
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
import static net.karen.mccourse.util.ChatUtil.*;

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
        return componentTranslatable(stack.getDescriptionId(), Utils.darkGreen);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        int xp = stack.getOrCreateTag().getInt("StoredLevels");
        tooltipLine(tooltip, "Stored XP: " + xp + " / " + storeXp, Utils.yellow);
        tooltipLine(tooltip, "Stored XP: Left click: 1 level; N: 10 levels;", Utils.red);
        tooltipLine(tooltip, "Shift + N: 100 levels; Shift + Left click: All levels.", Utils.red);
        tooltipLine(tooltip, "Restored XP: Right click: 1 level; B: 10 levels;", Utils.green);
        tooltipLine(tooltip, "Shift + B: 100 levels; Shift + Right click: All levels.", Utils.green);
        super.appendHoverText(stack, level, tooltip, flag);
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
            player(player, "Wait before using again!", Utils.yellow);
            return;
        }
        if (storedLevels > 0) { // Restore levels
            serverPlayer.giveExperienceLevels(amount);
            tag.putInt("StoredLevels", store);
            player(player, "Restored " + message, Utils.green);
            player.getCooldowns().addCooldown(heldItem.getItem(), 20); // Applies 1 second cooldown (20 ticks)
        }
        else { tradeMessage(player, "Bottle is empty."); }
    }
}