package net.karen.mccourse.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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

    // Call active CUSTOM TAG to detected stage of Magnet item
    private boolean activeMagnet(ItemStack stack) {
        return stack.getOrCreateTag().contains("active") && stack.getOrCreateTag().getBoolean("active");
    }

    // CUSTOM METHOD - Player has call active CUSTOM TAG stage on SCREEN
    private void messageScreen(Player player, String name, ChatFormatting color, boolean bool) {
        if (bool) {
            player.displayClientMessage(Component.literal("Magnet is " + name)
                    .setStyle(Style.EMPTY.applyFormats(color, ChatFormatting.BOLD)), true);
        }
    }

    // Enchanted only Magnet is active
    @Override
    public boolean isFoil(@NotNull ItemStack stack) { return activeMagnet(stack); }

    // Magnet can activate or disable only right-clicking
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
                                                           @NotNull InteractionHand hand) {
        // Magnet item has on HAND
        ItemStack stack = player.getItemInHand(hand);
        // Player on SERVER side and has Magnet item
        if (!player.level().isClientSide() && stack.getItem() instanceof MagnetItem) {
            // Sound when Player change to activate -> disable || disable -> activate
            level.playSound(null, player.blockPosition(), activeMagnet(stack) ? SoundEvents.BEACON_DEACTIVATE
                    : SoundEvents.BEACON_ACTIVATE, SoundSource.BLOCKS, 1.0f, 2.0f);
            // Active CUSTOM TAG stage
            stack.getOrCreateTag().putBoolean("active", !activeMagnet(stack));
            messageScreen(player, "ACTIVE", ChatFormatting.DARK_GREEN, activeMagnet(stack)); // Magnet active message on SCREEN
            messageScreen(player, "DISABLE", ChatFormatting.DARK_RED, !activeMagnet(stack)); // Magnet disable message on SCREEN
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, Entity entity,
                              int slot, boolean selected) {
        // Mode Spectator is nothing
        if (entity.isSpectator()) { return; }

        // Call active tag
        if (activeMagnet(stack)) {
            // Radius area that collect items
            AABB radiusArea = new AABB(entity.position().add(-radius, -radius, -radius),
                    entity.position().add(radius, radius, radius));

            // Items collect from radius area
            List<ItemEntity> items = level.getEntities(EntityType.ITEM, radiusArea,
                    item -> item.isAlive() && (!level.isClientSide() || item.tickCount > 1) &&
                            (item.getOwner() == null || !item.hasPickUpDelay()) && !item.getItem().isEmpty());

            // Items return on Player inventory
            items.forEach(item -> { Vec3 vec3 = new Vec3(entity.getX() - item.getX(), entity.getY() - item.getY(),
                        entity.getZ() - item.getZ());
                if (vec3.lengthSqr() < 64.0) {
                    item.setDeltaMovement(item.getDeltaMovement().add(vec3.normalize()
                            .scale((1.0 - Math.sqrt(vec3.lengthSqr()) / 8.0) *
                                    (1.0 - Math.sqrt(vec3.lengthSqr()) / 8.0) * 0.1)));
                }
            });
        }
    }

    // Magnet item description
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull
                                List<Component> tooltip, @NotNull TooltipFlag context) {
        boolean compoundTag = stack.getOrCreateTag().getBoolean("active"); // Call ACTIVE tag
        message(tooltip, "Activated", ChatFormatting.DARK_GREEN, compoundTag); // Magnet active message on TOOLTIP
        message(tooltip, "Inactivated", ChatFormatting.DARK_RED, !compoundTag); // Magnet disable message on TOOLTIP
    }

    // Magnet message depends of active CUSTOM TAG
    private void message(List<Component> tooltip, String name, ChatFormatting color, boolean bool) {
        if (bool) {
            tooltip.add(Component.literal(name + " Magnet").setStyle(Style.EMPTY.applyFormats(color, ChatFormatting.BOLD)));
        }
    }
}