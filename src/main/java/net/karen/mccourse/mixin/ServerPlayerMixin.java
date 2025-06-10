package net.karen.mccourse.mixin;

import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Unit;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "startSleepInBed", at = @At("HEAD"), cancellable = true)
    private void forceSleep(BlockPos pos, CallbackInfoReturnable<Either<Player.BedSleepingProblem, Unit>> cir) {
        ServerPlayer player = (ServerPlayer) (Object) this;
        ServerLevel level = player.serverLevel();
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof BedBlock) { // Checks if the block is a bed
            if (player.isSleeping() || !player.isAlive()) { // It allows you to sleep always, even during the day.
                cir.setReturnValue(Either.left(Player.BedSleepingProblem.OTHER_PROBLEM));
                return;
            }
            player.startSleeping(pos); // Starts sleep
            long time = level.getDayTime() % 24000L; // Alternates the world's time
            if (time >= 0 && time < 12000) { level.setDayTime(level.getDayTime() + (13000 - time)); } // Day → turns into night
            else { level.setDayTime(level.getDayTime() + (24000 - time)); } // Night → turns into day
            player.stopSleeping(); // Stops sleep
            cir.setReturnValue(Either.right(Unit.INSTANCE)); // Prevent the original logic
        }
    }
}