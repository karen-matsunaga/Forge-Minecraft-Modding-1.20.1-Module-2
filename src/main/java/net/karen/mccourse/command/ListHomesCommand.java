package net.karen.mccourse.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Set;

public class ListHomesCommand {
    public ListHomesCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        // Command to type on chat -> /homes
        dispatcher.register(Commands.literal("homes").executes(this::execute));
    }

    private int execute(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player != null) {
            CompoundTag homes = player.getPersistentData().getCompound("mccourse.homes");
            Set<String> homeNames = homes.getAllKeys();
            if (homeNames.isEmpty()) { // Player hasn't set home
                context.getSource().sendFailure(Component.literal("You have no homes set!"));
                return -1; // Appears FAIL message
            }
            String homesList = String.join(", ", homeNames); // Player has set homes
            context.getSource().sendSuccess(() -> Component.literal("Your homes: " + homesList), false);
            return 1; // Appears SUCCESS message
        }
        return 0;
    }
}