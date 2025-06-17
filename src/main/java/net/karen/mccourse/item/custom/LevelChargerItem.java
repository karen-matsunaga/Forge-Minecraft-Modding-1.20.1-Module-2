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
    private final Enchantment enchantment; // Increase or decrease enchantment level (Ex: Fortune +1 or -1)

    public LevelChargerItem(Properties properties, int changeAmount, Enchantment enchantment) {
        super(properties);
        this.changeAmount = changeAmount;
        this.enchantment = enchantment;
    }

    // DEFAULT METHOD - Level Charger item used on Main hand + RIGHT CLICK
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack changerStack = player.getItemInHand(hand); // Player Main hand
        InteractionHand otherHand = hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
        ItemStack targetStack = player.getItemInHand(otherHand); // Player Offhand
        if (applyTo(player, targetStack, changerStack)) {
            itemPlus(changerStack, ModItems.LEVEL_CHARGER_PLUS.get(), player); // General
            itemMinus(changerStack, ModItems.LEVEL_CHARGER_MINUS.get(), player);
            itemPlus(changerStack, ModItems.LEVEL_CHARGER_PLUS_FORTUNE.get(), player); // Fortune
            itemMinus(changerStack, ModItems.LEVEL_CHARGER_MINUS_FORTUNE.get(), player);
            changerStack.shrink(1);
            return InteractionResultHolder.success(changerStack);
        }
        else { return InteractionResultHolder.fail(changerStack); }
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
        else if (stack.is(ModItems.LEVEL_CHARGER_PLUS_FORTUNE.get())) { return baseName.copy().withStyle(ChatFormatting.GREEN); }
        else if (stack.is(ModItems.LEVEL_CHARGER_MINUS_FORTUNE.get())) { return baseName.copy().withStyle(ChatFormatting.RED); }
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

    // CUSTOM METHOD - Apply Level Charger Plus, Level Charger Minus, etc. on enchanted book, armor or tool
    public static boolean applyTo(Player player, ItemStack targetStack, ItemStack changerStack) {
        if (!(changerStack.getItem() instanceof LevelChargerItem self)) { return false; }
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(targetStack); // Enchanted books, armors or tools
        if (enchants.isEmpty()) { // Empty enchants
            screen(player, "The item has no enchantments!", ChatFormatting.DARK_RED);
            return false;
        }
        if (self.enchantment != null && !enchants.containsKey(self.enchantment)) { // General or specific enchantment types
            screen(player, "This item doesn't have the required enchantment!", ChatFormatting.GRAY);
            return false;
        }
        if (self.changeAmount < 0) { // Check enchantment levels
            boolean allMin = enchants.entrySet().stream().filter(e -> self.enchantment == null || e.getKey() == self.enchantment)
                    .allMatch(e -> e.getValue() <= 1);
            if (allMin) { // All enchantment are with min level is 1
                screen(player, "All enchantments are already at level 1!", ChatFormatting.AQUA);
                return false;
            }
        }
        if (!player.level().isClientSide()) { // Applies new enchantment levels on Enchanted books, armors or tools
            Map<Enchantment, Integer> newEnchants = new HashMap<>(enchants);
            enchants.forEach((enc, lvl) -> {
                if (self.enchantment == null || enc == self.enchantment) { // Armors or tools
                    int newLevel = Math.max(1, lvl + self.changeAmount);
                    newEnchants.put(enc, newLevel);
                }
            });
            if (targetStack.getItem() instanceof EnchantedBookItem) { // Enchanted books
                ItemStack newBook = new ItemStack(Items.ENCHANTED_BOOK);
                newEnchants.forEach((enc, lvl) -> EnchantedBookItem.addEnchantment(newBook, new EnchantmentInstance(enc, lvl)));
                player.setItemInHand(InteractionHand.OFF_HAND, newBook);
            }
            else { EnchantmentHelper.setEnchantments(newEnchants, targetStack); }
        }
        return true;
    }

    // CUSTOM METHOD - Level Charger Plus message
    private void itemPlus(ItemStack changerStack, Item item, Player player) {
        itemHurt(changerStack, item, player, "Increased +", ChatFormatting.GREEN);
    }

    // CUSTOM METHOD - Level Charger Minus message
    private void itemMinus(ItemStack changerStack, Item item, Player player) {
        itemHurt(changerStack, item, player, "Decreased ", ChatFormatting.RED);
    }
}