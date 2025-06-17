package net.karen.mccourse.network;

import net.karen.mccourse.item.custom.LevelChargerItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class LevelChargerInventorySlotMessage {
    private final int slotIndex;

    public LevelChargerInventorySlotMessage(int slotIndex) { this.slotIndex = slotIndex; }

    public LevelChargerInventorySlotMessage(FriendlyByteBuf buf) { this.slotIndex = buf.readVarInt(); }

    public static void buffer(LevelChargerInventorySlotMessage msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.slotIndex);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) { return; }
            if (slotIndex < 0 || slotIndex >= player.containerMenu.slots.size()) { return; }
            Slot slot = player.containerMenu.getSlot(slotIndex);
            if (!slot.hasItem()) { return; }
            ItemStack targetStack = slot.getItem(), changerStack = player.containerMenu.getCarried();
            if (changerStack.isEmpty() || !(changerStack.getItem() instanceof LevelChargerItem)) return;
            boolean applied = LevelChargerItem.applyTo(player, targetStack, changerStack);
            if (applied && !player.getAbilities().instabuild) {
                changerStack.shrink(1);
                player.containerMenu.broadcastChanges();
            }
        });
        ctx.get().setPacketHandled(true);
    }
}