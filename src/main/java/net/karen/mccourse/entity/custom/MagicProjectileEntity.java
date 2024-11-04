package net.karen.mccourse.entity.custom;

import net.karen.mccourse.entity.ModEntities;
import net.karen.mccourse.particle.ModParticles;
import net.karen.mccourse.sound.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;

public class MagicProjectileEntity extends Projectile {
    // Player hit with RADIATION STAFF item
    private static final EntityDataAccessor<Boolean> HIT =
            SynchedEntityData.defineId(MagicProjectileEntity.class, EntityDataSerializers.BOOLEAN);
    private int counter = 0;

    public MagicProjectileEntity(EntityType<? extends Projectile> pEntityType, Level pLevel) { super(pEntityType, pLevel); }

    public MagicProjectileEntity(Level pLevel, Player player) {
        super(ModEntities.MAGIC_PROJECTILE.get(), pLevel);
        setOwner(player);
        BlockPos blockpos = player.blockPosition(); // Player position
        double d0 = (double)blockpos.getX() + 0.5D;
        double d1 = (double)blockpos.getY() + 1.75D;
        double d2 = (double)blockpos.getZ() + 0.5D;
        this.moveTo(d0, d1, d2, this.getYRot(), this.getXRot()); // Player hit with RADIATION STAFF item
    }

    @Override
    public void tick() {
        super.tick();
        if(this.entityData.get(HIT)) { if(this.tickCount >= counter) { this.discard(); } }

        if (this.tickCount >= 300) { this.remove(RemovalReason.DISCARDED); }

        Vec3 vec3 = this.getDeltaMovement();
        HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity); // Player hit on particular entity
        if (hitresult.getType() != HitResult.Type.MISS && !ForgeEventFactory.onProjectileImpact(this, hitresult))
            this.onHit(hitresult); // Player hit on a block or an entity

        double d0 = this.getX() + vec3.x;
        double d1 = this.getY() + vec3.y;
        double d2 = this.getZ() + vec3.z;
        this.updateRotation();

        double d5 = vec3.x;
        double d6 = vec3.y;
        double d7 = vec3.z;

        for(int i = 1; i < 5; ++i) { // Added custom particles
            this.level().addParticle(ModParticles.ALEXANDRITE_PARTICLES.get(), d0-(d5*2), d1-(d6*2), d2-(d7*2),
                    -d5, -d6 - 0.1D, -d7);
        }

        if (this.level().getBlockStates(this.getBoundingBox()).noneMatch(BlockBehaviour.BlockStateBase::isAir)) { this.discard(); }
        else if (this.isInWaterOrBubble()) { this.discard(); }
        else {
            this.setDeltaMovement(vec3.scale(0.99F)); // Scale is 99%
            this.setPos(d0, d1, d2); // Calculate new position
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult hitResult) {
        super.onHitEntity(hitResult);
        Entity hitEntity = hitResult.getEntity(); // Who receive hit on RADIATION STAFF item
        Entity owner = this.getOwner(); // Player is an owner

        if(hitEntity == owner && this.level().isClientSide()) { return; }

        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), ModSounds.METAL_DETECTOR_FOUND_ORE.get(), SoundSource.NEUTRAL,
                2F, 1F);

        LivingEntity livingentity = owner instanceof LivingEntity ? (LivingEntity)owner : null;
        float damage = 2f;
        boolean hurt = hitEntity.hurt(this.damageSources().mobProjectile(this, livingentity), damage);
        if (hurt) {
            if(hitEntity instanceof LivingEntity livingHitEntity) { // If hit an entity added POISON effect
                livingHitEntity.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 1), owner);
            }
        }
    }

    @Override
    protected void onHit(HitResult hitResult) {
        super.onHit(hitResult);
        for(int x = 0; x < 18; ++x) {
            for(int y = 0; y < 18; ++y) {
                this.level().addParticle(ModParticles.ALEXANDRITE_PARTICLES.get(), this.getX(), this.getY(), this.getZ(),
                        Math.cos(x*20) * 0.15d, Math.cos(y*20) * 0.15d, Math.sin(x*20) * 0.15d);
            }
        } // Spawn custom particle

        if(this.level().isClientSide()) { return; }

        if(hitResult.getType() == HitResult.Type.ENTITY && hitResult instanceof EntityHitResult entityHitResult) {
            Entity hit = entityHitResult.getEntity();
            Entity owner = this.getOwner();
            if(owner != hit) {
                this.entityData.set(HIT, true);
                counter = this.tickCount + 5;
            }
        } else {
            this.entityData.set(HIT, true);
            counter = this.tickCount + 5;
        }
    }

    @Override
    protected void defineSynchedData() { this.entityData.define(HIT, false); }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() { return NetworkHooks.getEntitySpawningPacket(this); }
}