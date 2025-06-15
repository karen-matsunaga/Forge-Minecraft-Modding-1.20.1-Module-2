package net.karen.mccourse.network;

import net.karen.mccourse.item.custom.InfiniteItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
import static net.karen.mccourse.item.custom.InfiniteItem.screen;

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
            ItemStack stack = slot.getItem();
            ItemStack carried = player.containerMenu.getCarried();
            if (carried.isEmpty() || !(carried.getItem() instanceof InfiniteItem)) return;
            if (!stack.isDamageableItem()) { // Item hasn't durability
                screen(player, "This item has no durability!", ChatFormatting.RED);
                return;
            }
            CompoundTag tag = stack.getOrCreateTag();
            if (tag.getBoolean("Unbreakable")) { // Item has Unbreakable tag
                screen(player, "This item is already unbreakable!", ChatFormatting.YELLOW);
                return;
            }
            tag.putBoolean("Unbreakable", true); // Apply the Unbreakable tag
            screen(player, "Item is now unbreakable!", ChatFormatting.GREEN);
            if (!player.getAbilities().instabuild) { // Consumes the InfiniteItem
                carried.shrink(1);
                player.containerMenu.broadcastChanges(); // Update the interface
            }
        });
        ctx.get().setPacketHandled(true);
    }
}