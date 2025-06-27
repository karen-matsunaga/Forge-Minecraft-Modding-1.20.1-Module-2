package net.karen.mccourse.network;

import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.UnlockEnchantmentAction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;
import static net.karen.mccourse.util.ChatUtil.*;

public class UnlockNetworkMessage {
    private final boolean locked; // Item is LOCKED | UNLOCKED
    private final UnlockEnchantmentAction type; // ITEMS, ARMOR, OFFHAND Player's inventory
    private final int index; // ITEMS, ARMOR, OFFHAND SLOTS

    // MESSAGE TYPE
    public UnlockNetworkMessage(boolean locked, UnlockEnchantmentAction type, int index) {
        this.locked = locked;
        this.type = type;
        this.index = index;
    }

    // DECODE
    public UnlockNetworkMessage(FriendlyByteBuf buf) {
        this.locked = buf.readBoolean();
        this.type = buf.readEnum(UnlockEnchantmentAction.class);
        this.index = buf.readInt();
    }

    // ENCODE
    public static void buffer(UnlockNetworkMessage msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.locked);
        buf.writeEnum(msg.type);
        buf.writeInt(msg.index);
    }

    // CONSUMER
    public static void handler(UnlockNetworkMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                ItemStack target = switch (msg.type) {
                    case MAIN -> player.getInventory().items.get(msg.index);
                    case ARMOR -> player.getInventory().armor.get(msg.index);
                    case OFFHAND -> player.getInventory().offhand.get(msg.index);
                };
                if (!target.isEmpty() && target.getEnchantmentLevel(ModEnchantments.UNLOCK.get()) > 0) { // Item with Unlock enchantment
                    target.getOrCreateTag().putBoolean("Locked", msg.locked); // Changed stage
                    playerBool(player, msg.locked ? "§c\uD83D\uDD12 Item locked!" : "§a\uD83D\uDD13 Item unlocked!");
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}