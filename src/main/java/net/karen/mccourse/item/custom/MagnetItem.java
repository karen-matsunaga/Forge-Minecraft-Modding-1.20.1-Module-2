package net.karen.mccourse.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class MagnetItem extends Item {
    private final int radius; // Radius area to take items on Player's inventory

    public MagnetItem(Properties properties, int radius) {
        super(properties);
        this.radius = radius;
    }

    // CUSTOM METHOD - Call active CUSTOM NBT TAG to detected stage of Magnet item
    private boolean activeTag(ItemStack item) {
        return item.getOrCreateTag().contains("active") && item.getOrCreateTag().getBoolean("active");
    }

    @Override
    public boolean isFoil(@NotNull ItemStack item) { return activeTag(item); } // Enchanted only Magnet is active

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand); // Magnet item has on HAND
        if (!player.level().isClientSide() && item.getItem() instanceof MagnetItem) { // Player on SERVER side and has Magnet item
            // Sound when Player change STAGE - Magnet can Activate or Disable only right-clicking
            soundBlock(player, activeTag(item) ? SoundEvents.BEACON_DEACTIVATE : SoundEvents.BEACON_ACTIVATE, 1.0f, 2.0f);
            item.getOrCreateTag().putBoolean("active", !activeTag(item)); // Active CUSTOM TAG stage
            // Player has call active CUSTOM TAG stage on SCREEN - Active | Disable message
            playerStyle(player, activeTag(item) ? "Magnet is ACTIVE" : "Magnet is DISABLE", activeTag(item) ? darkGreen : darkRed);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, item);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, Entity entity,
                              int slot, boolean selected) {
        if (entity.isSpectator()) { return; } // Mode Spectator is nothing
        if (activeTag(stack)) { // Call active tag
            int minus = -radius, plus = radius;
            Vec3 entityMinus = entity.position().add(minus, minus, minus), entityPlus = entity.position().add(plus, plus, plus);
            AABB radiusArea = new AABB(entityMinus, entityPlus); // Radius area that collect items
            List<ItemEntity> items = level.getEntities(EntityType.ITEM, radiusArea, item -> // Items collect from radius area
                             item.isAlive() && (!level.isClientSide() || item.tickCount > 1) &&
                             (item.getOwner() == null || !item.hasPickUpDelay()) && !item.getItem().isEmpty());
            items.forEach(item -> { // Items return on Player inventory
                double x = entity.getX() - item.getX(), y = entity.getY() - item.getY(), z = entity.getZ() - item.getZ();
                Vec3 vec3 = new Vec3(x, y, z);
                double lenght = vec3.lengthSqr(), math = Math.sqrt(lenght), scale = (1.0 - math / 8.0) * (1.0 - math / 8.0) * 0.1;
                if (lenght < 64.0) { item.setDeltaMovement(item.getDeltaMovement().add(vec3.normalize().scale(scale))); }
            });
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull
                                List<Component> list, @NotNull TooltipFlag context) {
        boolean compoundTag = stack.getOrCreateTag().getBoolean("active"); // Call ACTIVE tag
        // Magnet item description depends of active CUSTOM NBT TAG - Active | Disable message on TOOLTIP
        tooltipLineBold(list, compoundTag ? "Activated Magnet" : "Inactivated Magnet", compoundTag ? darkGreen : darkRed);
    }
}