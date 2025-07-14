package net.karen.mccourse.entity.custom;

import net.karen.mccourse.entity.ModEntities;
import net.karen.mccourse.item.ModItems;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.NotNull;

public class TomahawkProjectileEntity extends AbstractArrow {
    private float rotation;
    public Vec2 groundedOffset;

    public TomahawkProjectileEntity(EntityType<? extends AbstractArrow> entityType, Level level) {
        super(entityType, level);
    }

    public TomahawkProjectileEntity(LivingEntity shooter, Level level) {
        super(ModEntities.TOMAHAWK_PROJECTILE.get(), shooter, level);
    }

    protected TomahawkProjectileEntity(EntityType<? extends AbstractArrow> entityType,
                                       double x, double y, double z, Level level) {
        this(entityType, level);
        this.setPos(x, y, z);
    }

    protected TomahawkProjectileEntity(EntityType<? extends AbstractArrow> entityType,
                                       LivingEntity shooter, Level level) {
        this(entityType, shooter.getX(), shooter.getEyeY() - (double)0.1F, shooter.getZ(), level);
        this.setOwner(shooter);
        if (shooter instanceof Player) {
            this.pickup = AbstractArrow.Pickup.ALLOWED;
        }
    }

    public float getRotation() {
        rotation += 0.5f;
        if (rotation >= 360) { rotation = 0; }
        return rotation;
    }

    @Override
    public boolean onGround() { return inGround; }

    @Override
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), 4);
        if (!this.level().isClientSide()) {
            this.level().broadcastEntityEvent(this, (byte)3);
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (result.getDirection() == Direction.SOUTH) { groundedOffset = new Vec2(215f,180f); }
        if (result.getDirection() == Direction.NORTH) { groundedOffset = new Vec2(215f, 0f); }
        if (result.getDirection() == Direction.EAST) { groundedOffset = new Vec2(215f,-90f); }
        if (result.getDirection() == Direction.WEST) { groundedOffset = new Vec2(215f,90f); }
        if (result.getDirection() == Direction.DOWN) { groundedOffset = new Vec2(115f,180f); }
        if (result.getDirection() == Direction.UP) { groundedOffset = new Vec2(285f,180f); }
    }

    @Override
    protected @NotNull ItemStack getPickupItem() { return new ItemStack(ModItems.TOMAHAWK.get()); }
}