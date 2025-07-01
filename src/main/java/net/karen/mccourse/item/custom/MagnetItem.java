package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ChatUtil;
import net.karen.mccourse.util.Utils;
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

public class MagnetItem extends Item {
    private final int radius; // Radius area to take items on Player's inventory

    public MagnetItem(Properties properties, int radius) {
        super(properties);
        this.radius = radius;
    }

    // CUSTOM METHOD - Call active CUSTOM NBT TAG to detected stage of Magnet item
    private boolean activeMagnet(ItemStack stack) {
        return stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) { return activeMagnet(stack); } // Enchanted only Magnet is active

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand); // Magnet item has on HAND
        if (!player.level().isClientSide() && stack.getItem() instanceof MagnetItem) { // Player on SERVER side and has Magnet item
            // Sound when Player change STAGE - Magnet can Activate or Disable only right-clicking
            Utils.soundBlock(player, activeMagnet(stack) ? SoundEvents.BEACON_DEACTIVATE : SoundEvents.BEACON_ACTIVATE,
                      1.0f, 2.0f);
            stack.getOrCreateTag().putBoolean("active", !activeMagnet(stack)); // Active CUSTOM TAG stage
            // Player has call active CUSTOM TAG stage on SCREEN - Active | Disable message
            ChatUtil.playerStyleBool(player, "Magnet is ACTIVE", activeMagnet(stack), Utils.darkGreen);
            ChatUtil.playerStyleBool(player, "Magnet is DISABLE", !activeMagnet(stack), Utils.darkRed);
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, Entity entity,
                              int slot, boolean selected) {
        if (entity.isSpectator()) { return; } // Mode Spectator is nothing
        if (activeMagnet(stack)) { // Call active tag
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
                                List<Component> tooltip, @NotNull TooltipFlag context) {
        boolean compoundTag = stack.getOrCreateTag().getBoolean("active"); // Call ACTIVE tag
        // Magnet item description depends of active CUSTOM NBT TAG - Active | Disable message on TOOLTIP
        ChatUtil.tooltipLineBool(tooltip, "Activated Magnet", compoundTag, Utils.darkGreen);
        ChatUtil.tooltipLineBool(tooltip, "Activated Inactivated", !compoundTag, Utils.darkRed);
    }
}