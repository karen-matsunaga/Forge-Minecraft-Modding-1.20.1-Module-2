package net.karen.mccourse.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class InfiniteInventorySlotMessage {
    private final int slotIndex;

    public InfiniteInventorySlotMessage(int slotIndex) { this.slotIndex = slotIndex; }

    public InfiniteInventorySlotMessage(FriendlyByteBuf buf) { this.slotIndex = buf.readVarInt(); }

    public static void buffer(InfiniteInventorySlotMessage msg, FriendlyByteBuf buf) { buf.writeVarInt(msg.slotIndex); }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) { return; }
            if (slotIndex < 0 || slotIndex >= player.containerMenu.slots.size()) { return; }
            Slot slot = player.containerMenu.getSlot(slotIndex);
            if (!slot.hasItem()) { return; }
            ItemStack stack = slot.getItem();
            if (!stack.isDamageableItem()) {
                player.displayClientMessage(Component.literal("This item has no durability!"), true);
                return;
            }
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.getBoolean("Unbreakable")) {
                player.displayClientMessage(Component.literal("This item is already unbreakable!"), true);
                return;
            }
            tag.putBoolean("Unbreakable", true);
            player.displayClientMessage(Component.literal("Item is now unbreakable!"), true);
        });
        ctx.get().setPacketHandled(true);
    }
}