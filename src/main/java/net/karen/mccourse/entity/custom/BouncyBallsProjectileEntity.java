package net.karen.mccourse.entity.custom;

import net.karen.mccourse.particle.ModParticles;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityTeleportEvent.EnderPearl;

import javax.annotation.Nullable;

public class BouncyBallsProjectileEntity extends ThrownEnderpearl {
    public BouncyBallsProjectileEntity(EntityType<? extends ThrownEnderpearl> pEntityType, Level pLevel) { super(pEntityType, pLevel); }

    public BouncyBallsProjectileEntity(Level pLevel, LivingEntity pShooter) { super(pLevel, pShooter); }

    protected Item getDefaultItem() { return null; }

    // Called when the arrow hits an entity
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        pResult.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    // Called when this EntityFireball hits a block or entity.
    protected void onHit(HitResult pResult) {
        super.onHit(pResult);

        // Particles when used Bouncy Balls item
        for(int i = 0; i < 32; ++i) {
            this.level().addParticle(ModParticles.BOUNCY_BALLS_PARTICLES.get(),
                    this.getX(), this.getY() + this.random.nextDouble() * 2.0D, this.getZ(),
                    this.random.nextGaussian(), 0.0D, this.random.nextGaussian());
        }

        if (!this.level().isClientSide && !this.isRemoved()) {
            Entity entity = this.getOwner();
            if (entity instanceof ServerPlayer) {
                ServerPlayer serverplayer = (ServerPlayer)entity;
                if (serverplayer.connection.isAcceptingMessages() && serverplayer.level() == this.level() && !serverplayer.isSleeping()) {
                    EnderPearl event = ForgeEventFactory.onEnderPearlLand(serverplayer, this.getX(), this.getY(), this.getZ(), this, 5.0F, pResult);

                    if (!event.isCanceled()) {
                        // Don't indent to lower patch size
                        if (entity.isPassenger()) { serverplayer.dismountTo(this.getX(), this.getY(), this.getZ()); }
                        else { entity.teleportTo(this.getX(), this.getY(), this.getZ()); }

                        entity.teleportTo(event.getTargetX(), event.getTargetY(), event.getTargetZ());
                        entity.resetFallDistance();
                        entity.hurt(this.damageSources().fall(), event.getAttackDamage());
                    } // Forge: End
                }
            }
            else if (entity != null) { entity.teleportTo(this.getX(), this.getY(), this.getZ()); entity.resetFallDistance(); }
            this.discard();
        }
    }

    // Called to update the entity's position/logic.
    public void tick() {
        Entity entity = this.getOwner();
        if (entity instanceof Player && !entity.isAlive()) { this.discard(); }
        else { super.tick(); }
    }

    @Nullable
    public Entity changeDimension(ServerLevel pServer, ITeleporter teleporter) {
        Entity entity = this.getOwner();
        if (entity != null && entity.level().dimension() != pServer.dimension()) { this.setOwner(null); }
        return super.changeDimension(pServer, teleporter);
    }
}