package net.karen.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import static net.karen.mccourse.util.ChatUtil.componentLiteralStyle;
import static net.karen.mccourse.util.Utils.*;

public class ReturnHomeCommand {
    public ReturnHomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("home") // Command to type on chat -> /home name
                .then(Commands.argument("name", StringArgumentType.word()).executes(this::execute)));
    }

    // CUSTOM METHOD - When player to type this command it is returned on home
    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String name = StringArgumentType.getString(context, "name");
        if (player != null) {
            CompoundTag homes = PlayerHomesData.get(player.serverLevel()).getHomes(player.getUUID());
            if (!homes.contains(name)) { // If there is no Set Home - Display the message in the chat (FAIL)
                context.getSource().sendFailure(componentLiteralStyle("No home named §6§l" + name + "§4§l found!", darkRed));
                return -1; // Player's position not save (FALSE)
            }
            int[] pos = homes.getIntArray(name);
            if (pos.length != 3) { // Display the message in the chat (FAIL)
                context.getSource().sendFailure(componentLiteralStyle("Invalid home position for §6§l" + name + "§4§l!", darkRed));
                return -1; // Player's position not save (FALSE)
            }
            // The Player will return to the saved [X, Y, Z] position - Player's saved position from /sethome name COMMAND
            player.teleportTo(pos[0] + 0.5, pos[1], pos[2] + 0.5);
            context.getSource().sendSuccess(() -> // Display the message in the chat (SUCCESS)
                    componentLiteralStyle("Teleported to home! §6§l" + name, green), false);
            return 1; // Player's position save with success (TRUE)
        }
        return 0; // Depends on result (1 - TRUE) or (-1 - FALSE)
    }
}