package net.karen.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.*;
import net.minecraft.server.level.ServerPlayer;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class DeleteHomeCommand {
    public DeleteHomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("delhome") // Command to type on chat -> /delhome name
                  .then(Commands.argument("name", StringArgumentType.word()).executes(this::execute)));
    }

    // CUSTOM METHOD - Delete home executes when type command
    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String name = StringArgumentType.getString(context, "name"); // Home name -> Ex: /delhome tree
        if (player != null) {
            PlayerHomesData data = PlayerHomesData.get(player.serverLevel()); // Update Home list
            boolean removed = data.removeHome(player.getUUID(), name); // Remove exist Home's name from list
            if (removed) { // Home exist
                context.getSource().sendSuccess(() ->
                        componentLiteralStyle("Deleted home! §6§l" + name, green), false);
                return 1; // Appears SUCCESS message (TRUE)
            }
            else { // Home not exist
                context.getSource().sendFailure(componentLiteralStyle("No home named §6§l" + name + "§4§l exists!", darkRed));
                return -1; // Appears FAIL message (FALSE)
            }
        }
        return 0;
    }
}