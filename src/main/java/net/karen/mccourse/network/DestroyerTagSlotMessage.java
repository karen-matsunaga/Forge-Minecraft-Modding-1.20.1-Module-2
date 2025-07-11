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
            String nameTag = destroyer.getNameTag(), word = nameTag.replace("_", " ");
            if (getTag == null) { player(player, "Item without " + itemLines(word) + "!", red); } // Item WITHOUT tag
            else if (getTag.getBoolean(nameTag)) { // Remove the Unbreakable tag or Lucky Bomb tag
                stack.removeTagKey(nameTag);
                player(player, "Removed " + itemLines(word) + "tag!", gold);
                consumeInfinite(player, carried); // Consumes OFFHAND item Destroyer Unbreakable Tag or Destroyer Lucky Bomb Tag
            }
            else { player(player, "Item without any matching tags!", red); } // Different tags
            player(player, "Hold the tool in your main hand!", red); // Tool or armor on MAIN HAND
        });
        ctx.get().setPacketHandled(true);
    }
}