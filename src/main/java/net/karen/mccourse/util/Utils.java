package net.karen.mccourse.util;

import net.karen.mccourse.network.ModNetworks;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class Utils {
    public static boolean IGNORE_LAPIS = false; // Avoid consumption of LAPIS LAZULI

    // All vanilla colors
    public static ChatFormatting blue = ChatFormatting.BLUE, darkBlue = ChatFormatting.DARK_BLUE,
                   aqua = ChatFormatting.AQUA, darkAqua = ChatFormatting.DARK_AQUA,
                   purple = ChatFormatting.LIGHT_PURPLE, darkPurple = ChatFormatting.DARK_PURPLE,
                   green = ChatFormatting.GREEN, darkGreen = ChatFormatting.DARK_GREEN,
                   gray = ChatFormatting.GRAY, darkGray = ChatFormatting.DARK_GRAY,
                   yellow = ChatFormatting.YELLOW, gold = ChatFormatting.GOLD,
                   red = ChatFormatting.RED, darkRed = ChatFormatting.DARK_RED,
                   black = ChatFormatting.BLACK, white = ChatFormatting.WHITE;

    // CUSTOM METHOD - Block and item sounds
    public static void sound(Player player, SoundEvent sound, float volume, float pitch) {
        player.level().playSound(null, player.blockPosition(), sound, SoundSource.PLAYERS, volume, pitch);
    }

    // CUSTOM METHOD - Enchanted ITEM
    public static ItemStack createEnchantedItem(Item item, Enchantment enchantment, int level) {
        ItemStack stack = new ItemStack(item);
        stack.enchant(enchantment, level);
        return stack;
    }

    // CUSTOM METHOD - Enchanted BOOK item
    public static ItemStack createEnchantedBook(Enchantment enchantment, int level) {
        ItemStack book = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(book, new EnchantmentInstance(enchantment, level));
        return book;
    }

    // CUSTOM METHOD - NETWORK message (CLIENT -> SERVER)
    public static void network(Object message) {
        ModNetworks.PACKET_HANDLER.sendToServer(message);
    }

    // CUSTOM METHOD - Item used on MAIN HAND
    public static boolean item(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == item; // CUSTOM METHOD - Used item
    }
}