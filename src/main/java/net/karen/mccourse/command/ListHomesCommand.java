package net.karen.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import java.util.Set;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class ListHomesCommand {
    public ListHomesCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("homes").executes(this::execute)); // Command to type on chat -> /homes
    }

    // CUSTOM METHOD - List homes executes when type command
    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            CompoundTag homes = PlayerHomesData.get(player.serverLevel()).getHomes(player.getUUID());
            Set<String> homeNames = homes.getAllKeys();
            if (homeNames.isEmpty()) { // Player hasn't set home
                context.getSource().sendFailure(componentLiteralStyle("You have no homes set!", darkRed));
                return -1; // Appears FAIL message (FALSE)
            }
            String homesList = String.join("; ", homeNames); // Player has set homes
            context.getSource().sendSuccess(() -> componentLiteralStyle("Your homes: §6§l" + homesList + ".", aqua), false);
            return 1; // Appears SUCCESS message (TRUE)
        }
        return 0;
    }
}