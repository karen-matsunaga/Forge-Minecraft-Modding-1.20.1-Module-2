package net.karen.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetHomeCommand {
    public SetHomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Command to type on chat -> /home set
        dispatcher.register(Commands.literal("home").then(Commands.literal("set")
                .executes(this::execute)));
    }

    // When player to type the command is done area's position
    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            // Player's [X, Y, Z] block positions
            int x = player.blockPosition().getX();
            int y = player.blockPosition().getY();
            int z = player.blockPosition().getZ();

            // Save data of Player's position
            player.getPersistentData().putIntArray("mccourse.homepos",
                    new int[] { x, y, z });

            // Display the message in the chat
            context.getSource().sendSuccess(() -> Component.literal("Set home at " +
                    "[" + x + ", " + y + ", " + z + "]"), true);
        }
        return 1; // Player's position save with success
    }
}