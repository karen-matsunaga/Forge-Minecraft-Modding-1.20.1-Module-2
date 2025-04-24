package net.karen.mccourse.network;

import net.karen.mccourse.screen.EnchantedMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.ClickType;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class EnchantedApplyEnchantmentMessage {
    private final int slotID, x, y, z, dragType;
    private final ClickType clickType;

    public EnchantedApplyEnchantmentMessage(int slotID, int x, int y, int z, int dragType, ClickType clickType) {
        this.slotID = slotID;
        this.x = x;
        this.y = y;
        this.z = z;
        this.dragType = dragType;
        this.clickType = clickType;
    } // Constructor for sending the package

    public EnchantedApplyEnchantmentMessage(FriendlyByteBuf buffer) {
        this.slotID = buffer.readInt();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
        this.dragType = buffer.readInt();
        this.clickType = ClickType.values()[buffer.readInt()];
    } // Constructor that rebuilds the buffer package

    public void buffer(FriendlyByteBuf buffer) {
        buffer.writeInt(slotID);
        buffer.writeInt(x);
        buffer.writeInt(y);
        buffer.writeInt(z);
        buffer.writeInt(dragType);
        buffer.writeInt(clickType.ordinal());
    }

    public void handler(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player == null) return;
            EnchantedMenu.handleSlotAction(player, slotID, dragType, clickType, x, y, z);
        });
        context.setPacketHandled(true);
    }
}