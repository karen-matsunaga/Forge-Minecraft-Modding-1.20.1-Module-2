package net.karen.mccourse.network;

import net.karen.mccourse.item.custom.DestroyerTagItem;
import net.karen.mccourse.util.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.function.Supplier;
import static net.karen.mccourse.util.ChatUtil.itemLines;
import static net.karen.mccourse.util.ChatUtil.player;
import static net.karen.mccourse.util.Utils.*;
import static net.karen.mccourse.util.Utils.consumeInfinite;

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
            if (player != null) {
                if (slotIndex < 0 || slotIndex >= player.containerMenu.slots.size()) { return; }
                Slot slot = player.containerMenu.getSlot(slotIndex);
                if (slot.hasItem()) {
                    ItemStack stack = slot.getItem(), carried = player.containerMenu.getCarried();
                    if (carried.getItem() instanceof DestroyerTagItem destroyer) {
                        CompoundTag getTag = stack.getTag();
                        String hasTag = destroyer.getNameTag(), word = hasTag.replace("_", " ");
                        if (stack.hasTag() && getTag != null && getTag.getBoolean(hasTag)) { // Item has Lucky Bomb tag
                            stack.removeTagKey(hasTag); // Remove the Unbreakable | Lucky Bomb tags
                            Collection<Item> items = ForgeRegistries.ITEMS.getValues();
                            @Nullable ITagManager<Item> tagItem = ForgeRegistries.ITEMS.tags();
                            items.forEach(item -> {
                                if (tagItem != null && tagItem.getTag(ModTags.Items.DESTROYER_TAG_ITEMS).contains(item)) {
                                    if (item.toString().contains(hasTag)) {
                                        dropFish(player.level(), player.getX(), player.getY(), player.getZ(), new ItemStack(item));
                                    }
                                }
                            });
                            player(player, "Item is now " + itemLines(word) + "!", green);
                            consumeInfinite(player, carried); // Consumes the Infinite or Lucky Bomb items
                        }
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}