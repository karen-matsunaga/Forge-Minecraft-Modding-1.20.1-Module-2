package net.karen.mccourse.block.custom;

import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class MccourseElevatorBlock extends Block {
    public MccourseElevatorBlock(Properties pProperties) { super(pProperties); }

    // Player Up
    public static void blockUp(Player player) {
        Level world = player.level(); // Obtains the player's level (world)
        BlockPos pos = new BlockPos((int) Math.floor(player.getX()), (int) Math.floor(player.getY() - 1),
                (int) Math.floor(player.getZ()));
        BlockState state = world.getBlockState(pos);

        // Checks if the player is standing on an elevator block
        if (state.getBlock() == ModBlocks.MCCOURSE_ELEVATOR.get()) {
            teleportPlayerUp(player, pos); // Teleports upwards
        }
    }

    private static void teleportPlayerUp(Player player, BlockPos pos) {
        Level world = player.level(); // Obtains the player's level (world)
        BlockPos targetPos = pos.above(); // Sets the target position up
        boolean foundElevator = false;

        // Searches for the next elevator block in the desired direction
        while (!foundElevator && world.getBlockState(targetPos).getBlock() != ModBlocks.MCCOURSE_ELEVATOR.get()) {
            targetPos = targetPos.above(); // Adjusts target position upwards

            // Checks if the position is outside the bounds of the world
            if (targetPos.getY() > world.getMaxBuildHeight()) {
                return; // Exit the method if it doesn't find an elevator block
            }

            // Check to see if you have found an elevator block
            if (world.getBlockState(targetPos).getBlock() == ModBlocks.MCCOURSE_ELEVATOR.get()) {
                foundElevator = true;
            }
        }

        // If it found an elevator block, teleports the player
        if (foundElevator) {
            player.teleportTo(targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5);
            // Get the teleportation sound and play the teleportation sound in the new position
            world.playSound(null, targetPos, ModSounds.ALEXANDRITE_LAMP_HIT.get(), SoundSource.NEUTRAL, 1, 1);
        }
    }

    // Player Down
    public static void blockDown(Player player) {
        Level world = player.level(); // Obtains the player's level (world)
        BlockPos pos = new BlockPos((int) Math.floor(player.getX()), (int) Math.floor(player.getY() - 1),
                (int) Math.floor(player.getZ()));
        BlockState state = world.getBlockState(pos);

        // Checks if the player is standing on an elevator block
        if (state.getBlock() == ModBlocks.MCCOURSE_ELEVATOR.get()) {
            teleportPlayerDown(player, pos); // Teleports down
        }
    }

    private static void teleportPlayerDown(Player player, BlockPos pos) {
        Level world = player.level(); // Obtains the player's level (world)
        BlockPos targetPos = pos.below(); // Sets the target position down
        boolean foundElevator = false;

        // Searches for the next elevator block in the desired direction
        while (!foundElevator && world.getBlockState(targetPos).getBlock() != ModBlocks.MCCOURSE_ELEVATOR.get()) {
            targetPos = targetPos.below(); // Adjusts the target position down

            // Checks if the position is outside the bounds of the world
            if (targetPos.getY() < world.getMinBuildHeight()) {
                return; // Exit the method if it doesn't find an elevator block
            }

            // Check to see if you have found an elevator block
            if (world.getBlockState(targetPos).getBlock() == ModBlocks.MCCOURSE_ELEVATOR.get()) {
                foundElevator = true;
            }
        }

        // If it found an elevator block, teleports the player
        if (foundElevator) {
            player.teleportTo(targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5);
            // Get the teleportation sound and Play the teleportation sound in the new position
            world.playSound(null, targetPos, ModSounds.ALEXANDRITE_LAMP_HIT.get(), SoundSource.NEUTRAL, 1, 1);
        }
    }
}