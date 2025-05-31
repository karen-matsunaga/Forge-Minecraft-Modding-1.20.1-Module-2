package net.karen.mccourse.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Supplier;

public class GlowingBlocksNetworkMessage {
    // SyncedSavedData base subclass for synchronize data
    public static abstract class SyncedSavedData extends SavedData {
        public abstract String getDataName();
        public abstract void read(CompoundTag tag);
        public abstract boolean isWorldScoped();

        @Override
        public abstract @NotNull CompoundTag save(@NotNull CompoundTag tag);

        public void syncData(LevelAccessor world) {
            setDirty(); // Saved data on DISK
            if (world instanceof Level level && !level.isClientSide()) {
                var target = isWorldScoped() ? PacketDistributor.DIMENSION.with(level::dimension) : PacketDistributor.ALL.noArg();
                ModNetworks.PACKET_HANDLER.send(target, new SavedDataSyncMessage(this));
            }
        }
    }

    // World subclass
    public static class World extends SyncedSavedData { // World variable data
        public static final String DATA_NAME = "mccourse_world";
        public boolean xray = false;
        public static World clientSide = new World();

        public static World get(LevelAccessor world) {
            if (world instanceof ServerLevel level) {
                return level.getDataStorage().computeIfAbsent(World::load, World::new, DATA_NAME);
            }
            return clientSide;
        }

        public static World load(CompoundTag tag) { var data = new World(); data.read(tag); return data; }

        @Override
        public void read(CompoundTag tag) { xray = tag.getBoolean("xray"); } // TRUE/FALSE xray tag

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag tag) { tag.putBoolean("xray", xray); return tag; }

        @Override
        public String getDataName() { return DATA_NAME; } // Data name: mccourse_world

        @Override
        public boolean isWorldScoped() { return true; }
    }

    // Map subclass
    public static class Map extends SyncedSavedData { // Map variable data
        public static final String DATA_NAME = "mccourse_map";
        public static Map clientSide = new Map();

        public static Map get(LevelAccessor world) {
            if (world instanceof ServerLevelAccessor accessor) {
                return Objects.requireNonNull(accessor.getLevel().getServer().getLevel(Level.OVERWORLD))
                        .getDataStorage().computeIfAbsent(Map::load, Map::new, DATA_NAME);
            }
            return clientSide;
        }

        public static Map load(CompoundTag tag) { var data = new Map(); data.read(tag); return data; }

        @Override
        public void read(CompoundTag tag) {}

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag tag) { return tag; }

        @Override
        public String getDataName() { return DATA_NAME; } // Data name: mccourse_map

        @Override
        public boolean isWorldScoped() { return false; }
    }

    // SavedDataSyncMessage class for save synchronize data (ACTIVE/DISABLE xray)
    public static class SavedDataSyncMessage { // Saved data
        private final SyncedSavedData data;

        public SavedDataSyncMessage(FriendlyByteBuf buffer) {
            String id = buffer.readUtf();
            CompoundTag tag = buffer.readNbt();
            if (Map.DATA_NAME.equals(id)) { data = Map.load(tag); }
            else { data = World.load(tag); }
        }

        public SavedDataSyncMessage(SyncedSavedData data) { this.data = data; }

        public static void buffer(SavedDataSyncMessage msg, FriendlyByteBuf buf) {
            buf.writeUtf(msg.data.getDataName());
            buf.writeNbt(msg.data.save(new CompoundTag()));
        }

        public static void handler(SavedDataSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> {
                if (!context.getDirection().getReceptionSide().isServer()) {
                    if (msg.data instanceof Map map) { Map.clientSide = map; } // Map Saved Data
                    else if (msg.data instanceof World world) { World.clientSide = world; } // World Saved Data
                }
            });
            context.setPacketHandled(true);
        }
    }
}