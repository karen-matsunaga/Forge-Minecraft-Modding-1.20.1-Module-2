package net.karen.mccourse.util;

import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;

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
}