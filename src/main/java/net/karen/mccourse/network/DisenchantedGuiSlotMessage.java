package net.karen.mccourse.network;

import net.karen.mccourse.screen.DisenchantedMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class DisenchantedGuiSlotMessage {
    private final int slotID, x, y, z, changeType, meta;

    // DisenchantedMenu.java -> Client and Server packages communication
    public DisenchantedGuiSlotMessage(int slotID, int x, int y, int z, int changeType, int meta) {
        this.slotID = slotID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.changeType = changeType;
        this.meta = meta;
    }

    // MCCourse.java -> Added Network event message
    public DisenchantedGuiSlotMessage(FriendlyByteBuf buffer) {
        this.slotID = buffer.readInt();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.changeType = buffer.readInt();
        this.meta = buffer.readInt();
    }

    public void buffer(FriendlyByteBuf buffer) {
        buffer.writeInt(slotID);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(changeType);
        buffer.writeInt(meta);
    }

    public void handler(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            DisenchantedMenu.handleSlotAction(player, slotID, changeType, meta, x, y, z);
        });
        context.setPacketHandled(true);
    }
}