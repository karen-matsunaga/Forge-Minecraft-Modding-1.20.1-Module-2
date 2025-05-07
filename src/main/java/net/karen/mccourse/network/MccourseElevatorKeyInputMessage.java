package net.karen.mccourse.network;

import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.custom.MccourseElevatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MccourseElevatorKeyInputMessage {
    private final boolean goUp;

    public MccourseElevatorKeyInputMessage(boolean goUp) { this.goUp = goUp; }

    public MccourseElevatorKeyInputMessage(FriendlyByteBuf buf) { this.goUp = buf.readBoolean(); }

    public static void buffer(MccourseElevatorKeyInputMessage msg, FriendlyByteBuf buf) { buf.writeBoolean(msg.goUp); }

    public static void handler(MccourseElevatorKeyInputMessage msg, Supplier<NetworkEvent.Context> ctxSupplier) {
        NetworkEvent.Context ctx = ctxSupplier.get();
        ctx.enqueueWork(() -> {
            ServerPlayer player = ctx.getSender();
            if (player == null) return;

            Level level = player.level();
            BlockPos pos = BlockPos.containing(player.getX(), player.getY() - 1, player.getZ());
            BlockState state = level.getBlockState(pos);

            if (state.getBlock() == ModBlocks.MCCOURSE_ELEVATOR.get()) {
                if (msg.goUp) {
                    MccourseElevatorBlock.blockUp(player);
                } else {
                    MccourseElevatorBlock.blockDown(player);
                }
            }
        });
        ctx.setPacketHandled(true);
    }
}