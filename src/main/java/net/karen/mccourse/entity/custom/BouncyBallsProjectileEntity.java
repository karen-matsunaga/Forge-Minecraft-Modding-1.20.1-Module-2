package net.karen.mccourse.entity.custom;

import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.particle.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.entity.EntityTeleportEvent.EnderPearl;
import org.jetbrains.annotations.NotNull;
import javax.annotation.Nullable;
import static net.minecraftforge.event.ForgeEventFactory.onEnderPearlLand;

public class BouncyBallsProjectileEntity extends ThrownEnderpearl {
    private final Entity owner = this.getOwner();

    public BouncyBallsProjectileEntity(EntityType<? extends ThrownEnderpearl> entityType, Level level) {
        super(entityType, level);
    }

    public BouncyBallsProjectileEntity(Level level, LivingEntity shooter) { super(level, shooter); }

    // CUSTOM METHOD - Default item
    protected @NotNull Item getDefaultItem() { return ModItems.BOUNCY_BALLS.get(); }

    // CUSTOM METHOD - Called when the arrow hits an entity
    protected void onHitEntity(@NotNull EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, owner), 0.0F);
    }

    // CUSTOM METHOD - Called when this EntityFireball hits a block or entity.
    protected void onHit(@NotNull HitResult result) {
        super.onHit(result);
        RandomSource source = this.random;
        double x = this.getX(), y = this.getY(), z = this.getZ(), gaus = source.nextGaussian(), dou = source.nextDouble();
        for (int i = 0; i < 32; ++i) { // Particles when used Bouncy Balls item
            this.level().addParticle(ModParticles.BOUNCY_BALLS_PARTICLES.get(), x, y + dou * 2.0D, z, gaus, 0.0D, gaus);
        }
        if (!this.level().isClientSide && !this.isRemoved()) {
            if (owner instanceof ServerPlayer server) {
                if (server.connection.isAcceptingMessages() && server.level() == this.level() && !server.isSleeping()) {
                    EnderPearl event = onEnderPearlLand(server, x, y, z, this, 5.0F, result);
                    if (!event.isCanceled()) { // Don't indent to lower patch size
                        if (owner.isPassenger()) { server.dismountTo(x, y, z); }
                        else { owner.teleportTo(x, y, z); }
                        owner.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        owner.resetFallDistance();
                        owner.hurt(this.damageSources().fall(), event.getAttackDamage());
                    } // Forge: End
                }
            }
            else if (owner != null) { // Player teleport
                owner.teleportTo(x, y, z);
                owner.resetFallDistance();
            }
            this.discard();
        }
    }

    // CUSTOM METHOD - Called to update the entity's position/logic
    public void tick() {
        if (owner instanceof Player && !owner.isAlive()) { this.discard(); }
        else { super.tick(); }
    }

    @Nullable
    public Entity changeDimension(@NotNull ServerLevel level, @NotNull ITeleporter teleporter) {
        if (owner != null && owner.level().dimension() != level.dimension()) { this.setOwner(null); }
        return super.changeDimension(level, teleporter);
    }
}