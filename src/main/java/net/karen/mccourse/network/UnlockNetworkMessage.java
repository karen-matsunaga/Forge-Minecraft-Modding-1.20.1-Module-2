package net.karen.mccourse.network;

import net.karen.mccourse.enchantment.ModEnchantments;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class UnlockNetworkMessage {
    private final boolean locked;

    // MESSAGE TYPE
    public UnlockNetworkMessage(boolean locked) { this.locked = locked; }

    // DECODE
    public UnlockNetworkMessage(FriendlyByteBuf buf) { this.locked = buf.readBoolean(); }

    // ENCODE
    public static void buffer(UnlockNetworkMessage msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.locked);
    }

    // CONSUMER
    public static void handler(UnlockNetworkMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack stack = player.getMainHandItem(); // Player has item on main hand
                if (!stack.isEmpty() && stack.getEnchantmentLevel(ModEnchantments.UNLOCK.get()) > 0) { // Item with Unlock enchantment
                    stack.getOrCreateTag().putBoolean("Locked", msg.locked); // Changed stage
                    player.displayClientMessage(Component.literal(msg.locked ? "§c\uD83D\uDD12 Item locked!"
                                                                             : "§a\uD83D\uDD13 Item unlocked!"), true);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}