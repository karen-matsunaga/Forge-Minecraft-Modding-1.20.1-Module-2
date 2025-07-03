package net.karen.mccourse.entity.custom;

import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.entity.ModEntities;
import net.karen.mccourse.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.*;
import net.minecraft.util.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;
import java.util.function.IntFunction;

public class ModBoatEntity extends Boat {
    private static final EntityDataAccessor<Integer> DATA_ID_TYPE =
            SynchedEntityData.defineId(Boat.class, EntityDataSerializers.INT);

    public ModBoatEntity(EntityType<? extends Boat> entityType, Level level) { super(entityType, level); }

    public ModBoatEntity(Level pLevel, double x, double y, double z) {
        this(ModEntities.MOD_BOAT.get(), pLevel);
        this.setPos(x, y, z);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    public @NotNull Item getDropItem() {
        switch (getModVariant()) { case WALNUT -> { return ModItems.WALNUT_BOAT.get(); } }
        return super.getDropItem();
    }

    public void setVariant(ModBoatEntity.Type type) { this.entityData.set(DATA_ID_TYPE, type.ordinal()); }

    public ModBoatEntity.Type getModVariant() {
        return ModBoatEntity.Type.byId(this.entityData.get(DATA_ID_TYPE));
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ID_TYPE, Type.WALNUT.ordinal());
    }

    protected void addAdditionalSaveData(CompoundTag tag) {
        tag.putString("Type", this.getModVariant().getSerializedName());
    }

    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("Type", 8)) { this.setVariant(ModBoatEntity.Type.byName(tag.getString("Type"))); }
    }

    public static enum Type implements StringRepresentable {
        WALNUT(ModBlocks.WALNUT_PLANKS.get(), "walnut");
        private final String name;
        private final Block planks;
        public static final StringRepresentable.EnumCodec<ModBoatEntity.Type> CODEC =
                StringRepresentable.fromEnum(ModBoatEntity.Type::values);
        private static final IntFunction<Type> BY_ID =
                ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.ZERO);

        private Type(Block pPlanks, String pName) {
            this.name = pName;
            this.planks = pPlanks;
        }

        @Override
        public @NotNull String getSerializedName() { return this.name; }

        public String getName() { return this.name; }

        public Block getPlanks() { return this.planks; }

        public String toString() { return this.name; }

        /** Get a boat type by its enum ordinal **/
        public static ModBoatEntity.Type byId(int id) { return BY_ID.apply(id); }

        public static ModBoatEntity.Type byName(String name) { return CODEC.byName(name, WALNUT); }
    }
}