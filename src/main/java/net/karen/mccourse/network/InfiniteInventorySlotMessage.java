package net.karen.mccourse.network;

import net.karen.mccourse.item.custom.InfiniteItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class InfiniteInventorySlotMessage {
    private final int slotIndex;

    public InfiniteInventorySlotMessage(int slotIndex) { this.slotIndex = slotIndex; }

    public InfiniteInventorySlotMessage(FriendlyByteBuf buf) { this.slotIndex = buf.readVarInt(); }

    public static void buffer(InfiniteInventorySlotMessage msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.slotIndex);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) { return; }
            if (slotIndex < 0 || slotIndex >= player.containerMenu.slots.size()) { return; }
            Slot slot = player.containerMenu.getSlot(slotIndex);
            if (!slot.hasItem()) { return; }
            ItemStack stack = slot.getItem(), carried = player.containerMenu.getCarried();
            if (carried.isEmpty() || !(carried.getItem() instanceof InfiniteItem infinite)) { return; }
            CompoundTag getTag = stack.getTag(), createTag = stack.getOrCreateTag();
            String hasTag = infinite.getNameTag(), word = hasTag.replace("_", " ");
            if (getTag != null && getTag.getBoolean(hasTag)) { // Item has Lucky Bomb tag or Unbreakable tag
                player(player, "This item is already " + itemLines(word) + "!", yellow);
                return;
            }
            createTag.putBoolean(hasTag, true); // Apply the Unbreakable tag or Lucky Bomb tag
            player(player, "Item is now " + itemLines(word) + "!", green);
            consumeInfinite(player, carried); // Consumes the Infinite or Lucky Bomb items
        });
        ctx.get().setPacketHandled(true);
    }
}