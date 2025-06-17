package net.karen.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class ReturnHomeCommand {
    public ReturnHomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Command to type on chat -> /home name
        dispatcher.register(Commands.literal("home")
                .then(Commands.argument("name", StringArgumentType.word()).executes(this::execute)));
    }

    // When player to type this command it is returned on home
    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String name = StringArgumentType.getString(context, "name");
        if (player != null) {
            CompoundTag homes = player.getPersistentData().getCompound("mccourse.homes");
            if (!homes.contains(name)) { // If there is no Set Home - Display the message in the chat (FAIL)
                context.getSource().sendFailure(Component.literal("No home named " + name + " found!"));
                return -1; // Player's position not save (FALSE)
            }
            int[] pos = homes.getIntArray(name);
            if (pos.length != 3) { // Display the message in the chat (FAIL)
                context.getSource().sendFailure(Component.literal("Invalid home position for " + name + "!"));
                return -1; // Player's position not save (FALSE)
            }
            // The Player will return to the saved [X, Y, Z] position
            player.teleportTo(pos[0] + 0.5, pos[1], pos[2] + 0.5); // Player's saved position from /sethome name COMMAND
            // Display the message in the chat (SUCCESS)
            context.getSource().sendSuccess(() -> Component.literal("Teleported to home " + name + "!"), false);
            return 1; // Player's position save with success (TRUE)
        }
        return 0; // Depends on result (1 - TRUE) or (-1 - FALSE)
    }
}