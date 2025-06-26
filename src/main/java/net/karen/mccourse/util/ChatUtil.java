package net.karen.mccourse.util;

import com.mojang.datafixers.util.Either;
import net.karen.mccourse.MCCourseMod;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.enchantment.Enchantment;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static net.karen.mccourse.util.Utils.*;

public class ChatUtil {
    // GENERAL METHODS
    public static Component standardLiteral(String message) {
        return Component.literal(message);
    }

    public static Component standardTranslatable(String message) {
        return Component.translatable(message);
    }

    public static Component componentLiteral(String message, ChatFormatting color) {
        return Component.literal(message).withStyle(color);
    }

    public static Component componentLiteralStyle(String message, ChatFormatting color) {
        return Component.literal(message).setStyle(Style.EMPTY.applyFormats(color, ChatFormatting.BOLD));
    }

    public static Component customStyle(String number, int light) {
        return Component.literal(String.valueOf(number)).setStyle(Style.EMPTY.withColor(light > 6 ? 0x32FC76 : 0xFF1818));
    }

    public static Component componentTranslatable(String message, ChatFormatting color) {
        return Component.translatable(message).withStyle(color);
    }

    public static Component componentTranslatableStyle(String message, ChatFormatting color) {
        return Component.translatable(message).setStyle(Style.EMPTY.applyFormats(color, ChatFormatting.BOLD));
    }

    public static void player(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(componentLiteral(message, color), true);
    }

    // CUSTOM METHOD - Using RGB colors and BOLD format
    public static void style(Player player, String message, ChatFormatting color) {
        player.displayClientMessage(componentLiteralStyle("Glowing " + message, color), true);
    }

    // CUSTOM METHOD - Using RGB colors and BOLD format
    public static MutableComponent description(String tooltip, ChatFormatting color,
                                               List<Boolean> curse) {
        return Component.translatable(tooltip).withStyle(Style.EMPTY.withColor(color).withBold(curse.get(0)).withItalic(curse.get(1)));
    }

    public static void line(List<Component> tooltip, String message) {
        tooltip.add(Component.literal(message));
    }

    public static void tooltipLine(List<Component> tooltip, String message, ChatFormatting color) {
        tooltip.add(Component.literal(message).withStyle(color));
    }

    // UNIQUE message
    public static void normalMessage(Player player, String message, ChatFormatting color) {
        player(player, message, color);
    }

    // TOOLTIP MESSAGE
    public static void image(List<Either<FormattedText, TooltipComponent>> element,
                                    String path, int width, int height, String text, Boolean bool) {
        if (bool) {
            element.add(Either.right(new ImageTooltipComponent(new ResourceLocation(MCCourseMod.MOD_ID, path),
                                     width, height, Component.literal(text))));
        }
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
        return Component.literal("X: ").append(componentLiteral(String.format("%.3f", x), aqua))
        .append(standardLiteral("  Y: ")).append(componentLiteral(String.format("%.5f", y), purple))
        .append(standardLiteral("  Z: ")).append(componentLiteral(String.format("%.3f", z), gold));
    }

    // CUSTOM METHOD - Light numbers
    public static Component numbers(int totalLight, int skyLight, int blockLight) {
        String light = String.valueOf(totalLight), sky = String.valueOf(skyLight), block = String.valueOf(blockLight);
        return Component.literal("Light: ").append(customStyle(light, totalLight))
               .append(standardLiteral("  Sky: ")).append(customStyle(sky, skyLight))
               .append(standardLiteral("  Block: ")).append(customStyle(block, blockLight));
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
}