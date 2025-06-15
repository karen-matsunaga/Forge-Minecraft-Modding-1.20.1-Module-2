package net.karen.mccourse.network;

import net.karen.mccourse.item.custom.MccourseBottleItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class MccourseBottleKeyInputMessage {
    private final boolean storeAll;

    public MccourseBottleKeyInputMessage(boolean storeAll) { this.storeAll = storeAll; }

    public MccourseBottleKeyInputMessage(FriendlyByteBuf buf) { this.storeAll = buf.readBoolean(); }

    public static void buffer(MccourseBottleKeyInputMessage msg, FriendlyByteBuf buf) {
        buf.writeBoolean(msg.storeAll);
    }

    public static void handler(MccourseBottleKeyInputMessage msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender(); // Message of Mod Events
            if (player != null) {
                ItemStack stack = player.getMainHandItem(); // Has Mccourse Bottle item on main hand
                if (!(stack.getItem() instanceof MccourseBottleItem)) { return; }
                CompoundTag tag = stack.getOrCreateTag();
                int storedLevels = tag.getInt("StoredLevels"); // NBT tag to store and to restore XP
                int maxLevels = MccourseBottleItem.storeXp; // Store Levels
                int availableLevels = player.experienceLevel;
                if (availableLevels <= 0) { // Player store XP only has 1+ levels
                    MccourseBottleItem.screen(player, "You have no XP to store.", ChatFormatting.RED);
                    return;
                }
                int toStore = msg.storeAll ? availableLevels : 1;
                if (storedLevels < maxLevels) {
                    int canStore = Math.min(maxLevels - storedLevels, toStore);
                    tag.putInt("StoredLevels", storedLevels + canStore);
                    player.giveExperienceLevels(-canStore);
                    MccourseBottleItem.screen(player, "Stored " + canStore + " levels.", ChatFormatting.YELLOW);
                }
                else { MccourseBottleItem.screen(player, "XP full or insufficient.", ChatFormatting.RED); }
            }
        });
    }
}