package net.karen.mccourse.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import java.util.List;

import static net.karen.mccourse.util.Utils.*;

public class ChatUtil {
    // GENERAL METHODS
    public static void player(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(Component.literal(message).withStyle(color), true);
    }

    public static void style(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(Component.literal("Glowing " + message)
              .setStyle(Style.EMPTY.applyFormats(color, ChatFormatting.BOLD)), true);
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

    // DISPLAY
    public static void glow(Player player, String name, ChatFormatting color) {
        style(player, name, color);
    }

    // CUSTOM METHOD - [X, Y, Z] Coordinates
    public static Component literal(double x, double y, double z) {
        return Component.literal("X: ").append(Component.literal(String.format("%.3f", x)).withStyle(aqua))
        .append(Component.literal("  Y: ")).append(Component.literal(String.format("%.5f", y)).withStyle(purple))
        .append(Component.literal("  Z: ")).append(Component.literal(String.format("%.3f", z)).withStyle(gold));
    }

    // CUSTOM METHOD - Light numbers
    public static Component numbers(int totalLight, int skyLight, int blockLight) {
        return Component.literal("Light: ").append(Component.literal(String.valueOf(totalLight))
                        .setStyle(Style.EMPTY.withColor(totalLight > 6 ? 0x32FC76 : 0xFF1818)))
                .append(Component.literal("  Sky: ")).append(Component.literal(String.valueOf(skyLight))
                        .setStyle(Style.EMPTY.withColor(skyLight > 6 ? 0x32FC76 : 0xFF1818)))
                .append(Component.literal("  Block: ")).append(Component.literal(String.valueOf(blockLight))
                        .setStyle(Style.EMPTY.withColor(blockLight > 6 ? 0x32FC76 : 0xFF1818)));

    }
}