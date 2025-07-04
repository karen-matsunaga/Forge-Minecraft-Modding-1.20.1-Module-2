package net.karen.mccourse.command;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class PlayerHomesData extends SavedData {
    private final Map<UUID, CompoundTag> playerHomes = new HashMap<>();
    private static final String DATA_NAME = "homes_data";

    public static PlayerHomesData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(PlayerHomesData::load, PlayerHomesData::new, DATA_NAME);
    }

    public static PlayerHomesData load(CompoundTag tag) {
        PlayerHomesData data = new PlayerHomesData();
        for (String uuidStr : tag.getAllKeys()) {
            UUID uuid = UUID.fromString(uuidStr);
            data.playerHomes.put(uuid, tag.getCompound(uuidStr));
        }
        return data;
    }

    private PlayerHomesData() {}

    @Override
    public @NotNull CompoundTag save(@NotNull CompoundTag tag) {
        for (Map.Entry<UUID, CompoundTag> entry : playerHomes.entrySet()) {
            tag.put(entry.getKey().toString(), entry.getValue());
        }
        return tag;
    }

    // CUSTOM METHOD - LIST homes command
    public CompoundTag getHomes(UUID uuid) {
        return playerHomes.computeIfAbsent(uuid, u -> new CompoundTag());
    }

    // CUSTOM METHOD - SET home command
    public void setHome(UUID uuid, String name, BlockPos pos) {
        CompoundTag homes = getHomes(uuid);
        homes.putIntArray(name, new int[]{pos.getX(), pos.getY(), pos.getZ()});
        playerHomes.put(uuid, homes);
        setDirty();
    }

    // CUSTOM METHOD - DELETE home command
    public boolean removeHome(UUID uuid, String name) {
        CompoundTag homes = getHomes(uuid);
        if (homes.contains(name)) {
            homes.remove(name);
            setDirty();
            return true;
        }
        return false;
    }
}