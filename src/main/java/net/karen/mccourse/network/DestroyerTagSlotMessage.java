package net.karen.mccourse.network;

import net.karen.mccourse.item.custom.DestroyerTagItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class DestroyerTagSlotMessage {
    private final int slotIndex;

    public DestroyerTagSlotMessage(int slotIndex) { this.slotIndex = slotIndex; }

    public DestroyerTagSlotMessage(FriendlyByteBuf buf) { this.slotIndex = buf.readVarInt(); }

    public static void buffer(DestroyerTagSlotMessage msg, FriendlyByteBuf buf) {
        buf.writeVarInt(msg.slotIndex);
    }

    public void handler(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) { return; }
            if (slotIndex < 0 || slotIndex >= player.containerMenu.slots.size()) { return; }
            Slot slot = player.containerMenu.getSlot(slotIndex);
            if (!slot.hasItem()) { return; }
            ItemStack stack = slot.getItem(), carried = player.containerMenu.getCarried(); // Used item on MAIN HAND inventory
            if (carried.isEmpty() || !(carried.getItem() instanceof DestroyerTagItem destroyer)) { return; }
            CompoundTag getTag = stack.getTag(); // Item has Lucky Bomb tag or Unbreakable tag
            String nameTag = destroyer.getNameTag(), word = splitWord(nameTag), upper = upperString(word);
            boolean notNull = getTag != null, notTag = notNull && !getTag.getBoolean(nameTag);
            if (notTag) { player(player, "Item without " + upper + " tag!", red); } // Item WITHOUT tag
            else if (notNull && getTag.getBoolean(nameTag)) { // Remove the Unbreakable tag or Lucky Bomb tag
                stack.removeTagKey(nameTag); // Remove tag of tool or armor on MAIN HAND
                player(player, "Removed " + upper + " tag!", darkRed); // Remove message on screen
                consumeInfinite(player, carried); // Consumes OFFHAND item Destroyer Unbreakable Tag or Destroyer Lucky Bomb Tag
            }
        });
        ctx.get().setPacketHandled(true);
    }
}