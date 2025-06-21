package net.karen.mccourse.network;

import net.karen.mccourse.item.custom.HammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import java.util.List;
import java.util.function.Supplier;

public class HammerBlockRenderServerMessage {
    private final BlockPos pos;

    public HammerBlockRenderServerMessage(BlockPos pos) { this.pos = pos; }

    public HammerBlockRenderServerMessage(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void buffer(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public void handler(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            ServerPlayer player = context.get().getSender();
            if (player != null) {
                ItemStack held = player.getMainHandItem();
                if (!(held.getItem() instanceof HammerItem hammer)) { return; }
                List<BlockPos> toHighlight = HammerItem.getBlocksToBeDestroyed(0, hammer.getRadius(), pos, player);
                ModNetworks.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> player),
                        new HammerBlockRenderClientMessage(toHighlight));
            }
        });
        context.get().setPacketHandled(true);
    }
}