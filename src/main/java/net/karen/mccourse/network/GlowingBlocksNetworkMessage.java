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
    // Base class for synchronize data
    public static abstract class SyncedSavedData extends SavedData {
        public abstract String getDataName();
        public abstract void read(CompoundTag tag);
        public abstract boolean isWorldScoped();

        @Override
        public abstract @NotNull CompoundTag save(@NotNull CompoundTag tag);

        public void syncData(LevelAccessor world) {
            setDirty();
            if (world instanceof Level level && !level.isClientSide()) {
                var target = isWorldScoped() ? PacketDistributor.DIMENSION.with(level::dimension) : PacketDistributor.ALL.noArg();
                ModNetworks.PACKET_HANDLER.send(target, new SavedDataSyncMessage(this));
            }
        }
    }

    // World Variables class
    public static class WorldVariables extends SyncedSavedData { // World variable data
        public static final String DATA_NAME = "mccourse_world_variables";
        public boolean xray = false;
        public static WorldVariables clientSide = new WorldVariables();

        public static WorldVariables get(LevelAccessor world) {
            if (world instanceof ServerLevel level) {
                return level.getDataStorage().computeIfAbsent(WorldVariables::load, WorldVariables::new, DATA_NAME);
            }
            return clientSide;
        }

        public static WorldVariables load(CompoundTag tag) { var data = new WorldVariables(); data.read(tag); return data; }

        @Override
        public void read(CompoundTag tag) { xray = tag.getBoolean("xray"); }

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag tag) { tag.putBoolean("xray", xray); return tag; }

        @Override
        public String getDataName() { return DATA_NAME; }

        @Override
        public boolean isWorldScoped() { return true; }
    }

    // Map Variables class
    public static class MapVariables extends SyncedSavedData { // Map variable data
        public static final String DATA_NAME = "mccourse_map_variables";
        public static MapVariables clientSide = new MapVariables();

        public static MapVariables get(LevelAccessor world) {
            if (world instanceof ServerLevelAccessor accessor) {
                return Objects.requireNonNull(accessor.getLevel().getServer().getLevel(Level.OVERWORLD))
                        .getDataStorage().computeIfAbsent(MapVariables::load, MapVariables::new, DATA_NAME);
            }
            return clientSide;
        }

        public static MapVariables load(CompoundTag tag) { var data = new MapVariables(); data.read(tag); return data; }

        @Override
        public void read(CompoundTag tag) {}

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag tag) { return tag; }

        @Override
        public String getDataName() { return DATA_NAME; }

        @Override
        public boolean isWorldScoped() { return false; }
    }

    // Base class for save synchronize data
    public static class SavedDataSyncMessage { // Saved data
        private final SyncedSavedData data;

        public SavedDataSyncMessage(FriendlyByteBuf buffer) {
            String id = buffer.readUtf();
            CompoundTag tag = buffer.readNbt();

            if (MapVariables.DATA_NAME.equals(id)) { data = MapVariables.load(tag); }
            else { data = WorldVariables.load(tag); }
        }

        public SavedDataSyncMessage(SyncedSavedData data) { this.data = data; }

        public static void buffer(SavedDataSyncMessage msg, FriendlyByteBuf buf) {
            buf.writeUtf(msg.data.getDataName());
            buf.writeNbt(msg.data.save(new CompoundTag()));
        }

        public static void handler(SavedDataSyncMessage msg, Supplier<NetworkEvent.Context> ctx) {
            ctx.get().enqueueWork(() -> {
                if (!ctx.get().getDirection().getReceptionSide().isServer()) {
                    if (msg.data instanceof MapVariables mapVars) MapVariables.clientSide = mapVars;
                    else if (msg.data instanceof WorldVariables worldVars) WorldVariables.clientSide = worldVars;
                }
            });
            ctx.get().setPacketHandled(true);
        }
    }
}