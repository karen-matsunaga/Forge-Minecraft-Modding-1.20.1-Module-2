package net.karen.mccourse.util;

import com.mojang.datafixers.util.Either;
import net.karen.mccourse.MCCourseMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static net.karen.mccourse.util.Utils.*;

public class ChatUtil { // GENERAL METHODS
    public static String mod = "enchantment.mccourse.", vanilla = "enchantment.minecraft.";

    // CUSTOM METHOD - COMPONENT LITERAL without color
    public static Component standardLiteral(String message) {
        return Component.literal(message);
    }

    // CUSTOM METHOD - COMPONENT TRANSLATABLE without color
    public static Component standardTranslatable(String message) {
        return Component.translatable(message);
    }

    // CUSTOM METHOD - COMPONENT LITERAL with one color
    public static Component componentLiteral(String message, ChatFormatting color) {
        return Component.literal(message).withStyle(color);
    }

    // CUSTOM METHOD - COMPONENT LITERAL with one color
    public static Component componentTranslatable(String message, ChatFormatting color) {
        return Component.translatable(message).withStyle(color);
    }

    // CUSTOM METHOD - COMPONENT LITERAL with BOLD format
    public static Component componentLiteralStyle(String message, ChatFormatting color) {
        return Component.literal(message).setStyle(Style.EMPTY.applyFormats(color, ChatFormatting.BOLD));
    }

    // CUSTOM METHOD - Message appears on screen with COMPONENT LITERAL without color (BOOL)
    public static void playerBool(Player player, String message) {
        player.displayClientMessage(standardLiteral(message), true);
    }

    // CUSTOM METHOD - Message appears on screen without BOLD format
    public static void player(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(componentLiteral(message, color), true);
    }

    public static void playerTranslatable(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(componentTranslatable(message, color), true);
    }

    // CUSTOM METHOD - Message appears on screen with BOLD format
    public static void playerStyle(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(componentLiteralStyle(message, color), true);
    }

    // CUSTOM METHOD - Message appears on screen with STAGE change
    public static void playerStyleBool(Player player, boolean bool, boolean bool2,
                                       String message, String message2, ChatFormatting color, ChatFormatting color2) {
        if (bool) { player.displayClientMessage(componentLiteral(message, color), true); }
        if (bool2) { player.displayClientMessage(componentLiteral(message2, color2), true); }
    }

    // CUSTOM METHOD - Using RGB colors and BOLD format
    public static void glow(Player player, boolean test, String message, String message1) {
        player.displayClientMessage(componentLiteralStyle("Glowing " + (test ? message : message1),
              (test ? green : red)), true);
    }

    // CUSTOM METHOD - Using RGB colors and BOLD format
    public static MutableComponent description(String tooltip, ChatFormatting color,
                                               List<Boolean> curse) {
        boolean curseFalse = curse.get(0), curseTrue = curse.get(1);
        return Component.translatable(tooltip).withStyle(Style.EMPTY.withColor(color).withBold(curseFalse).withItalic(curseTrue));
    }

    // CUSTOM METHOD - Text appears on TOOLTIP item
    public static void tooltipLine(List<Component> tooltip, String message, ChatFormatting color) {
        tooltip.add(componentLiteral(message, color));
    }

    // CUSTOM METHOD - Text appears on TOOLTIP item with BOLD format
    public static void tooltipLineBold(List<Component> tooltip, String message, ChatFormatting color) {
        tooltip.add(componentLiteralStyle(message, color));
    }

    // CUSTOM METHOD - Text appears on TOOLTIP item with STAGE change
    public static void tooltipLines(List<Component> tooltip, ItemStack item,
                                    String message, ChatFormatting color) {
        tooltip.add(Component.translatable(item.getDescriptionId()).withStyle(color)
                             .append(componentLiteral(message, color)));
    }

    // CUSTOM METHOD - UNIQUE message
    public static void normalMessage(Player player, String message, ChatFormatting color) {
        player(player, message, color);
    }

    // CUSTOM METHOD - Icon message TOOLTIP
    public static void image(List<Either<FormattedText, TooltipComponent>> element,
                                    String path, int width, int height, String text, Boolean bool) {
        if (bool) {
            element.add(Either.right(new ImageTooltipComponent(new ResourceLocation(MCCourseMod.MOD_ID, path),
                                     width, height, Component.literal(text))));
        }
    }

    // CUSTOM METHOD - INVALID message with BOLD format
    public static void invalidMessage(Player player, String invalid) {
        playerStyle(player, invalid, Utils.darkRed);
    }

    // CUSTOM METHOD - TRADE message with BOLD format
    public static void tradeMessage(Player player, String trade) {
        player(player, trade, Utils.red);
    }

    // CUSTOM METHOD - [X, Y, Z] Coordinates
    public static Component literal(double x, double y, double z) {
        return Component.literal("X: ").append(componentLiteral(String.format("%.3f", x), aqua))
        .append(standardLiteral("  Y: ")).append(componentLiteral(String.format("%.5f", y), purple))
        .append(standardLiteral("  Z: ")).append(componentLiteral(String.format("%.3f", z), gold));
    }

    // CUSTOM METHOD - Light numbers
    public static Component numbers(int totalLight, int skyLight, int blockLight) {
        return Component.literal("Light:").append(customStyle(" " + totalLight, totalLight))
               .append(standardLiteral("  Sky:")).append(customStyle(" " + skyLight, skyLight))
               .append(standardLiteral("  Block:")).append(customStyle(" " + blockLight, blockLight));
    }

    // CUSTOM METHOD - Light colors numbers
    public static Component customStyle(String number, int light) {
        return Component.literal(String.valueOf(number)).setStyle(Style.EMPTY.withColor(light > 6 ? 0x32FC76 : 0xFF1818));
    }

    // CUSTOM METHOD - Chat message on prompt
    public static void chat(String item, Player player) {
        MCCourseMod.LOGGER.info("Sheep was hit with {} by {}", item, player.getName().getString());
    }

    // CUSTOM METHOD - Enchantment Icon compatibility
    public static MutableComponent icon(boolean isCurse, Enchantment enchantment) {
        String armor = "§6⭐", pick = "§5⛏", bow = "§a\uD83C\uDFF9", sword = "§4\uD83D\uDDE1", trident = "§b\uD83D\uDD31",
               fish = "§e\uD83C\uDFA3", axe = "§5\uD83E\uDE93", hammer = "§3🔨", shield = "§1🛡",
               icon = isCurse ? "§c🔥" :
                        switch (enchantment.category) { // Replace this line with custom styled version
                            case ARMOR, ARMOR_HEAD, ARMOR_CHEST, ARMOR_LEGS, ARMOR_FEET -> armor; case DIGGER -> pick + " " + axe;
                            case BOW, CROSSBOW -> bow; case WEAPON -> sword; case TRIDENT -> trident; case FISHING_ROD -> fish;
                            case BREAKABLE -> axe + " " + fish + " " + pick + " " + armor + " " + sword + " " + bow + " " + trident +
                                              " " + hammer + " " + shield; default -> ""; };
        return Component.literal(icon + " ");
    }

    // CUSTOM METHOD - Vault item display
    public static Component itemChatMessage(Player player, BlockPos pos, ChatFormatting color) {
        int x = pos.getX(), y = pos.getY(), z = pos.getZ();
        return Component.literal(player.getGameProfile().getName() + " died at [X: " + x + ", Y: " + y + ", Z: " + z + "] " +
               LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                        .withStyle(Style.EMPTY.withColor(color).withItalic(false));
    }

    // CUSTOM METHOD - Interaction Result Holder FAIL messages
    public static boolean fail(Player player, String message, ChatFormatting color) {
        player(player, message, color);
        return false;
    }

    // CUSTOM METHOD - Renamed string on TOOLTIP -> Ex: Fortune etc. (Only one word) -> Capitalize First Letters
    public static String itemLine(String var, String old, String value,
                                  String old2, String value2) {
        String name = var.replace(old, value).replace(old2, value2), firstIndex = name.substring(0, 1).toUpperCase();
        return firstIndex + name.substring(1);
    }

    // CUSTOM METHOD - Renamed string on TOOLTIP -> Ex: Mccourse Generator etc. (Two+ words) -> Capitalize First Letters
    public static String itemLines(String input) {
        String[] words = input.split(" "); // Separated words
        StringJoiner capitalized = new StringJoiner(" "); // Spaced words
        for (String word : words) { // For each word
            if (!word.isEmpty()) { capitalized.add(word.substring(0, 1).toUpperCase() + word.substring(1).toLowerCase()); }
        }
        return capitalized.toString(); // Joined words with capitalize words
    }
}