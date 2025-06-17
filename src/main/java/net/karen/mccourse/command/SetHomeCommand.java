package net.karen.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class SetHomeCommand {
    public SetHomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Command to type on chat -> /sethome name
        dispatcher.register(Commands.literal("sethome")
                  .then(Commands.argument("name", StringArgumentType.word()).executes(this::execute)));
    }

    // When player to type the command is done area's position
    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String name = StringArgumentType.getString(context, "name");
        if (player != null) {
            // Player's [X, Y, Z] block positions
            int x = player.blockPosition().getX(), y = player.blockPosition().getY(), z = player.blockPosition().getZ();
            // Save data of Player's position
            CompoundTag data = player.getPersistentData(), homes = data.getCompound("mccourse.homes");
            IntArrayTag pos = new IntArrayTag(new int[]{x, y, z});
            homes.put(name, pos);
            data.put("mccourse.homes", homes); // Added set home on list
            // Display the message in the chat
            context.getSource().sendSuccess(() ->
                    Component.literal("Set home at " + "[X:" + x + ", Y:" + y + ", Z:" + z + "]"), true);
        }
        return 1; // Player's position save with success
    }
}