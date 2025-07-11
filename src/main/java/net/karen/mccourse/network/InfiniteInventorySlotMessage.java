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
            ItemStack stack = slot.getItem(), carried = player.containerMenu.getCarried(); // Get MAIN HAND item on inventory
            if (carried.isEmpty() || !(carried.getItem() instanceof InfiniteItem infinite)) { return; }
            CompoundTag getTag = stack.getTag(), createTag = stack.getOrCreateTag(); // Get MAIN HAND item on inventory
            String hasTag = infinite.getNameTag(), word = splitWord(hasTag), upper = upperString(word);
            if (getTag != null && getTag.getBoolean(hasTag)) { // Item has Lucky Bomb tag or Unbreakable tag
                player(player, "This item is already " + upper + " tag!", yellow);
            }
            else if (getTag != null && !getTag.getBoolean(hasTag)) { // Apply the Unbreakable tag or Lucky Bomb tag
                createTag.putBoolean(hasTag, true);
                player(player, "Added " + upper + " tag!", green);
                consumeInfinite(player, carried); // Consumes the Infinite or Lucky Bomb items
            }
            else { player(player, "Hold the tool in your main hand!", red); } // Tool or armor on MAIN HAND
        });
        ctx.get().setPacketHandled(true);
    }
}