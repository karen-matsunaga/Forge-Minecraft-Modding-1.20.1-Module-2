package net.karen.mccourse.entity.custom;

import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.custom.DiceBlock;
import net.karen.mccourse.entity.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class DiceProjectileEntity extends ThrowableItemProjectile {
    public DiceProjectileEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) { super(pEntityType, pLevel); }

    public DiceProjectileEntity(Level pLevel) { this(ModEntities.DICE_PROJECTILE.get(), pLevel); }

    public @Nullable DiceProjectileEntity(Level pLevel, LivingEntity livingEntity) { super(ModEntities.DICE_PROJECTILE.get(), livingEntity, pLevel); }

    @Override
    protected Item getDefaultItem() { return null; }

    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        if (!this.level().isClientSide()) { // Not is CLIENT side
            this.level().broadcastEntityEvent(this, ((byte) 3));
            this.level().setBlock(blockPosition(), ((DiceBlock) ModBlocks.DICE_BLOCK.get()).getRandomBlockState(), 3);
        }
        this.discard();
        super.onHitBlock(pResult);
    }
}