package net.karen.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DeleteHomeCommand {
    public DeleteHomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Command to type on chat -> /delhome name
        dispatcher.register(Commands.literal("delhome")
                  .then(Commands.argument("name", StringArgumentType.word()).executes(this::execute)));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String name = StringArgumentType.getString(context, "name"); // Home name -> Ex: /delhome tree
        if (player != null) {
            CompoundTag data = player.getPersistentData(), homes = data.getCompound("mccourse.homes");
            if (!homes.contains(name)) { // Home not exist
                context.getSource().sendFailure(Component.literal("No home named " + name + " exists!"));
                return -1; // Appears FAIL message (FALSE)
            }
            homes.remove(name); // Home exist remove from list
            data.put("mccourse.homes", homes);
            context.getSource().sendSuccess(() -> Component.literal("Deleted home " + name + "!"), false);
            return 1; // Appears SUCCESS message (TRUE)
        }
        return 0;
    }
}