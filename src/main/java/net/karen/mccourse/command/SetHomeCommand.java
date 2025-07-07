package net.karen.mccourse.command;

import com.mojang.brigadier.*;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.*;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class SetHomeCommand {
    public SetHomeCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("sethome") // Command to type on chat -> /sethome name
                                    .then(Commands.argument("name", StringArgumentType.word()).executes(this::execute)));
    }

    // CUSTOM METHOD - When player to type the command is done area's position
    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        String name = StringArgumentType.getString(context, "name");
        if (player != null) {
            PlayerHomesData data = PlayerHomesData.get(player.serverLevel());
            CompoundTag homes = data.getHomes(player.getUUID());
            if (homes.contains(name)) {
                String hasHome = "A home named §6§l" + name + "§4§l already exists!";
                context.getSource().sendFailure(componentLiteralStyle(hasHome, darkRed));
                return -1; // Player's fail position save
            }
            BlockPos pos = player.blockPosition(); // Player's [X, Y, Z] block positions
            data.setHome(player.getUUID(), name, pos); // Save data of Player's position
            // Display the message in the chat
            String message = "Set home at §e§l[X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ() + "]";
            context.getSource().sendSuccess(() -> componentLiteralStyle(message, purple), false);
            return 1; // Player's position save with success
        }
        return 0;
    }
}