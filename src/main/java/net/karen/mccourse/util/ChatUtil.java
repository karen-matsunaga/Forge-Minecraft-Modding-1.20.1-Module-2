package net.karen.mccourse.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class ChatUtil {
    // GENERAL METHODS
    public static void player(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(Component.literal(message).withStyle(color), true);
    }

    public static void line(List<Component> tooltip, String message) {
        tooltip.add(Component.literal(message));
    }

    // UNIQUE message
    public static void normalMessage(Player player, String message, ChatFormatting color) {
        player(player, message, color);
    }

    // TRUE/FALSE messages
    public static void booleanMessage(Player player, String yes, String no, Boolean bool) {
        if (bool) { player(player, yes, Utils.green); }
        if (!bool) { player(player, no, Utils.red); }
    }

    // INVALID message
    public static void invalidMessage(Player player, String invalid) {
        player(player, invalid, Utils.darkRed);
    }

    public static void tradeMessage(Player player, String trade) {
        player(player, trade, Utils.red);
    }

    // TOOLTIP true/false messages
    public static void booleanTooltip(List<Component> tooltip, String message, Boolean bool) {
        if (bool) {
            tooltip.add(CommonComponents.EMPTY);
            line(tooltip, message);
        }
    }
}