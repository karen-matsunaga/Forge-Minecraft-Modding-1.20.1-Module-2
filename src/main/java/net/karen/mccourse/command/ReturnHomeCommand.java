package net.karen.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ReturnHomeCommand {
    public ReturnHomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Command to type on chat -> /home return
        dispatcher.register(Commands.literal("home").then(Commands.literal("return").executes(this::execute)));
    }

    // When player to type this command it is returned on home
    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        // Initialize value with 0
        int value = 0;
        if (player != null) {
            boolean hasHomepos = player.getPersistentData().getIntArray("mccourse.homepos").length != 0;
            // If there is no Set Home
            if (!hasHomepos) {
                // Display the message in the chat (FAIL)
                context.getSource().sendFailure(Component.literal("No Home Position has been set!"));
                value = -1; // Player's position not save (FALSE)
            }
            // The Player will return to the saved position
            else {
                // Get the Player's saved [X, Y, Z] coordinates from the /home set COMMAND
                int[] playerPos = player.getPersistentData().getIntArray("mccourse.homepos");
                player.teleportTo(playerPos[0], playerPos[1], playerPos[2]);

                // Display the message in the chat (SUCCESS)
                context.getSource().sendSuccess(() -> Component.literal("Player returned Home!"), false);
                value = 1; // Player's position save with success (TRUE)
            }
        }
        return value; // Depends on result (1 - TRUE) or (-1 - FALSE)
    }
}