package net.karen.mccourse.item.custom;

import net.karen.mccourse.entity.custom.*;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.function.Predicate;
import static net.karen.mccourse.util.Utils.*;

public class ModBoatItem extends Item {
    private static final Predicate<Entity> ENTITY_PREDICATE = EntitySelector.NO_SPECTATORS.and(Entity::isPickable);
    private final ModBoatEntity.Type type;
    private final boolean hasChest;

    public ModBoatItem(boolean pHasChest, ModBoatEntity.Type type, Item.Properties properties) {
        super(properties);
        this.hasChest = pHasChest;
        this.type = type;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        HitResult hitresult = getPlayerPOVHitResult(level, player, any);
        if (hitresult.getType() == hitMiss) { return InteractionResultHolder.pass(itemstack); }
        else {
            Vec3 vec3 = player.getViewVector(1.0F);
            List<Entity> list = level.getEntities(player, player.getBoundingBox().expandTowards(vec3.scale(5.0D))
                                                                .inflate(1.0D), ENTITY_PREDICATE);
            if (!list.isEmpty()) {
                for (Entity entity : list) {
                    AABB aabb = entity.getBoundingBox().inflate(entity.getPickRadius());
                    if (aabb.contains(player.getEyePosition())) { return InteractionResultHolder.pass(itemstack); }
                }
            }
            if (hitresult.getType() == hitBlock) {
                Boat boat = this.getBoat(level, hitresult);
                if (boat instanceof ModChestBoatEntity chestBoat) { chestBoat.setVariant(this.type); }
                else if (boat instanceof ModBoatEntity) { ((ModBoatEntity)boat).setVariant(this.type); }
                boat.setYRot(player.getYRot());
                if (!level.noCollision(boat, boat.getBoundingBox())) { return InteractionResultHolder.fail(itemstack); }
                else {
                    if (!level.isClientSide()) {
                        level.addFreshEntity(boat);
                        level.gameEvent(player, GameEvent.ENTITY_PLACE, hitresult.getLocation());
                        if (!player.getAbilities().instabuild) { itemstack.shrink(1); }
                    }
                    player.awardStat(Stats.ITEM_USED.get(this));
                    return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
                }
            }
            else { return InteractionResultHolder.pass(itemstack); }
        }
    }

    // CUSTOM METHOD - Boat type
    private Boat getBoat(Level level, HitResult hit) {
        double x = hit.getLocation().x, y = hit.getLocation().y, z = hit.getLocation().z;
        return (this.hasChest ? new ModChestBoatEntity(level, x, y, z) : new ModBoatEntity(level, x, y, z));
    }
}