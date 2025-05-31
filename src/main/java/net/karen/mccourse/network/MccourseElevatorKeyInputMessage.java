package net.karen.mccourse.network;

import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.custom.MccourseElevatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MccourseElevatorKeyInputMessage {
    private final boolean goUp;

    public MccourseElevatorKeyInputMessage(boolean goUp) { this.goUp = goUp; }

    public MccourseElevatorKeyInputMessage(FriendlyByteBuf buf) { this.goUp = buf.readBoolean(); }

    public static void buffer(MccourseElevatorKeyInputMessage msg, FriendlyByteBuf buf) { buf.writeBoolean(msg.goUp); }

    public static void handler(MccourseElevatorKeyInputMessage msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                BlockPos pos = BlockPos.containing(player.getX(), player.getY() - 1, player.getZ());
                if (player.level().getBlockState(pos).getBlock() == ModBlocks.MCCOURSE_ELEVATOR.get()) {
                    if (msg.goUp) { MccourseElevatorBlock.blockUp(player); } // Player up
                    else { MccourseElevatorBlock.blockDown(player); } // Player down
                }
            }
        });
        context.setPacketHandled(true);
    }
}