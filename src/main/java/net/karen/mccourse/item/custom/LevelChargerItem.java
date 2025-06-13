package net.karen.mccourse.item.custom;

import net.karen.mccourse.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LevelChargerItem extends Item {
    private final int changeAmount; // +1 (Increase) or -1 (Decrease) enchantment level

    public LevelChargerItem(Properties properties, int changeAmount) {
        super(properties);
        this.changeAmount = changeAmount;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack changerStack = player.getItemInHand(hand); // Player Main hand
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack targetStack = player.getItemInHand(otherHand); // Player Offhand
        // Player Offhand get enchantment levels
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(targetStack);
        if (enchants.isEmpty()) { // Enchanted book, armor or tools WITHOUT enchantments
            screen(player, "The item has no enchantments!", ChatFormatting.DARK_RED);
            return InteractionResultHolder.fail(changerStack);
        }
        // Check if all enchantments are already at level 1
        if (changeAmount < 0 && enchants.values().stream().allMatch(level -> level <= 1)) {
            screen(player, "All enchantments are now at level 1!", ChatFormatting.AQUA);
            return InteractionResultHolder.fail(changerStack);
        }
        if (!world.isClientSide()) { // Is server side
            Map<Enchantment, Integer> newEnchants = new HashMap<>();
            enchants.forEach((enc, lvl) -> {
                int newLevel = lvl + changeAmount; // Increase or decrease enchantment level
                newLevel = Math.max(newLevel, 1); // Min 1 enchantment level
                newEnchants.put(enc, newLevel); });
            if (targetStack.getItem() instanceof EnchantedBookItem) { // Is enchanted book
                ItemStack newBook = new ItemStack(Items.ENCHANTED_BOOK); // Create new book with updated enchantments
                newEnchants.forEach((enc, lvl) -> EnchantedBookItem.addEnchantment(newBook, new EnchantmentInstance(enc, lvl)));
                player.setItemInHand(otherHand, newBook);
            }
            else { // Is Armor or tools
                EnchantmentHelper.setEnchantments(newEnchants, targetStack);
                player.setItemInHand(otherHand, targetStack); // Force update
            }
        }
        itemHurt(changerStack, ModItems.LEVEL_CHARGER_PLUS.get(), player, "Increased +", ChatFormatting.GREEN);
        itemHurt(changerStack, ModItems.LEVEL_CHARGER_MINUS.get(), player, "Decreased ", ChatFormatting.RED);
        changerStack.shrink(1);
        return InteractionResultHolder.success(changerStack);
    }

    // DEFAULT METHOD - Added TOOLTIP on all Level Charger
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> components, @NotNull TooltipFlag flag) {
        if (changeAmount == 1) { item(components, stack, " increase +", ChatFormatting.GREEN); }
        else if (changeAmount == -1) { item(components, stack, " decrease ", ChatFormatting.RED); }
        super.appendHoverText(stack, level, components, flag);
    }

    // DEFAULT METHOD - Added NAME on all Level Charger -> Translatable en_us.json
    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Component baseName = super.getName(stack);
        if (stack.is(ModItems.LEVEL_CHARGER_PLUS.get())) { return baseName.copy().withStyle(ChatFormatting.GREEN); }
        else if (stack.is(ModItems.LEVEL_CHARGER_MINUS.get())) { return baseName.copy().withStyle(ChatFormatting.RED); }
        return baseName;
    }

    // CUSTOM METHOD - Tooltip of Level Charger Plus and Level Charger Minus items
    private void item(List<Component> components, ItemStack stack,
                      String message, ChatFormatting color) {
        components.add(Component.translatable(stack.getDescriptionId()).withStyle(color)
                  .append(Component.literal(message + changeAmount + " enchantment level").withStyle(color)));
    }

    // CUSTOM METHOD - Message on screen
    private static void screen(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(Component.literal(message).withStyle(color), true);
    }

    // CUSTOM METHOD - Message when consumed Level Charger (Plus / Minus) items
    private void itemHurt(ItemStack changerStack, Item item, Player player,
                          String message, ChatFormatting color) {
        if (changerStack.is(item)) { screen(player, message + changeAmount + " enchantment levels!", color); }
    }
}