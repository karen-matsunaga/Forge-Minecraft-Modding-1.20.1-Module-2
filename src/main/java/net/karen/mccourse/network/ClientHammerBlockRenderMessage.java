package net.karen.mccourse.network;

import net.karen.mccourse.item.custom.HammerItem;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.List;
import java.util.function.Supplier;

public class ClientHammerBlockRenderMessage {
    private final List<BlockPos> positions;

    public ClientHammerBlockRenderMessage(List<BlockPos> positions) { this.positions = positions; }

    public ClientHammerBlockRenderMessage(FriendlyByteBuf buf) { this.positions = buf.readList(FriendlyByteBuf::readBlockPos); }

    public void buffer(FriendlyByteBuf buf) { buf.writeCollection(positions, FriendlyByteBuf::writeBlockPos); }

    public void handler(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> HammerItem.setHighlightedBlocks(positions));
        context.get().setPacketHandled(true);
    }
}