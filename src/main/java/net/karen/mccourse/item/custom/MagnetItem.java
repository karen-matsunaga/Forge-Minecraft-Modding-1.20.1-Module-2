package net.karen.mccourse.item.custom;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MagnetItem extends Item {
    private final int radius;
    public MagnetItem(Properties pProperties, int radius) {
        super(pProperties);
        this.radius = radius;
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) { return true; }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack item = player.getMainHandItem();
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, Entity entity,
                              int slot, boolean selected) {
        // Mode Spectator is nothing
        if (entity.isSpectator()) { return; }

        // Radius area that collect items
        AABB radiusArea = new AABB(entity.position().add(-radius, -radius, -radius),
                entity.position().add(radius, radius, radius));

        // Items collect from radius area
        List<ItemEntity> items = level.getEntities(EntityType.ITEM, radiusArea,
                item -> item.isAlive() && (!level.isClientSide() || item.tickCount > 1) &&
                        (item.getOwner() == null || !item.hasPickUpDelay()) &&
                        !item.getItem().isEmpty());

        // Items return on Player inventory
        items.forEach(item -> {
            Vec3 vec3 = new Vec3(entity.getX() - item.getX(), entity.getY() - item.getY(),
                    entity.getZ() - item.getZ());

            if (vec3.lengthSqr() < 64.0) {
                item.setDeltaMovement(item.getDeltaMovement().add(vec3.normalize()
                        .scale((1.0 - Math.sqrt(vec3.lengthSqr()) / 8.0) *
                                (1.0 - Math.sqrt(vec3.lengthSqr()) / 8.0) * 0.1)));
            }
        });
    }
}