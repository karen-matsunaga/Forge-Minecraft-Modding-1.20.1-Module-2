package net.karen.mccourse.entity.custom;

import net.karen.mccourse.entity.ModEntities;
import net.karen.mccourse.item.ModItems;
import net.minecraft.network.syncher.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ModChestBoatEntity extends ChestBoat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE =
            SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);

    public ModChestBoatEntity(EntityType<? extends ChestBoat> entityType, Level level) {
        super(entityType, level);
    }

    public ModChestBoatEntity(Level pLevel, double x, double y, double z) {
        this(ModEntities.MOD_CHEST_BOAT.get(), pLevel);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public @NotNull Item getDropItem() {
        switch (getModVariant()) { case WALNUT -> { return ModItems.WALNUT_CHEST_BOAT.get(); } }
        return super.getDropItem();
    }

    public void setVariant(ModBoatEntity.Type type) {
        this.entityData.set(DATA_ID_TYPE, type.ordinal());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, ModBoatEntity.Type.WALNUT.ordinal());
    }

    public ModBoatEntity.Type getModVariant() {
        return ModBoatEntity.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }
}