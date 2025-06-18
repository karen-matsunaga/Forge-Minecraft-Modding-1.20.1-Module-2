package net.karen.mccourse.network;

import net.karen.mccourse.item.MccourseBottleActionItem;
import net.karen.mccourse.item.custom.MccourseBottleItem;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import java.util.function.Supplier;

public class MccourseBottleKeyInputMessage {
    private final MccourseBottleActionItem action;
    private final int amount;

    public MccourseBottleKeyInputMessage(MccourseBottleActionItem action, int amount) {
        this.action = action;
        this.amount = amount;
    }

    public MccourseBottleKeyInputMessage(FriendlyByteBuf buf) {
        this.action = buf.readEnum(MccourseBottleActionItem.class);
        this.amount = buf.readInt();
    }

    public static void buffer(MccourseBottleKeyInputMessage msg, FriendlyByteBuf buf) {
        buf.writeEnum(msg.action);
        buf.writeInt(msg.amount);
    }

    public static void handler(MccourseBottleKeyInputMessage msg, Supplier<NetworkEvent.Context> ctx) {
        NetworkEvent.Context context = ctx.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender(); // Message of Mod Events
            if (player != null) {
                ItemStack stack = player.getMainHandItem(); // Has Mccourse Bottle item on main hand
                if (!(stack.getItem() instanceof MccourseBottleItem)) { return; }
                // NBT tag to store and to restore XP, Store Levels on Mccourse Bottle, Player XP
                CompoundTag tag = stack.getOrCreateTag();
                int storedLevels = tag.getInt("StoredLevels"), maxLevels = MccourseBottleItem.storeXp,
                    availableLevels = player.experienceLevel;
                if (player.getCooldowns().isOnCooldown(stack.getItem())) { // Check if it is already on cooldown
                    MccourseBottleItem.screen(player, "Wait before using again!", ChatFormatting.YELLOW);
                    return;
                }
                switch (msg.action) {
                    case STORE -> {
                        if (availableLevels <= 0) { // Player store XP only has 1+ levels
                            MccourseBottleItem.screen(player, "You have no XP to store!", ChatFormatting.RED);
                            return;
                        }
                        int canStore = Math.min(msg.amount, Math.min(maxLevels - storedLevels, availableLevels));
                        if (canStore > 0) {
                            tag.putInt("StoredLevels", storedLevels + canStore);
                            player.giveExperienceLevels(-canStore);
                            MccourseBottleItem.screen(player, "Stored " + canStore + " levels!", ChatFormatting.GREEN);
                        }
                        else { MccourseBottleItem.screen(player, "XP full or insufficient!", ChatFormatting.RED); }
                    }
                    case RESTORED -> {
                        if (storedLevels <= 0) { // Player store XP only has 1+ levels
                            MccourseBottleItem.screen(player, "No XP to restore!", ChatFormatting.RED);
                            return;
                        }
                        int toRestore = Math.min(msg.amount, storedLevels);
                        tag.putInt("StoredLevels", storedLevels - toRestore);
                        player.giveExperienceLevels(toRestore);
                        MccourseBottleItem.screen(player, "Restored " + toRestore + " levels!", ChatFormatting.GREEN);
                    }
                }
                player.getCooldowns().addCooldown(stack.getItem(), 20); // Applies 1 second cooldown (20 ticks)
            }
        });
    }
}