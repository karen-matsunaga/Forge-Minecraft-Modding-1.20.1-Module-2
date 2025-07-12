package net.karen.mccourse.entity.custom;

import net.karen.mccourse.entity.ModEntities;
import net.karen.mccourse.item.ModItems;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

public class TorchBallProjectileEntity extends ThrowableItemProjectile {
    public TorchBallProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public TorchBallProjectileEntity(Level level) {
        this(ModEntities.TORCH_BALL_PROJECTILE.get(), level);
    }

    public TorchBallProjectileEntity(Level level, LivingEntity livingEntity) {
        super(ModEntities.TORCH_BALL_PROJECTILE.get(), livingEntity, level);
    }

    @Override
    protected @NotNull Item getDefaultItem() { return ModItems.TORCH_BALL.get(); }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (level().isClientSide()) { return; }
        BlockPos hitPos = result.getBlockPos().relative(result.getDirection());
        Direction hitDirection = result.getDirection();
        ItemStack thrownStack = this.getItem();
        CompoundTag tag = thrownStack.getTag();
        Block blockToPlace = Blocks.TORCH;
        if (tag != null && tag.contains("PlaceBlock", Tag.TAG_STRING)) {
            ResourceLocation id = ResourceLocation.tryParse(tag.getString("PlaceBlock"));
            if (id != null && ForgeRegistries.BLOCKS.containsKey(id)) { blockToPlace = ForgeRegistries.BLOCKS.getValue(id); }
        }
        BlockState placeState;
        if (blockToPlace == Blocks.TORCH || blockToPlace == Blocks.WALL_TORCH) {
            if (hitDirection == Direction.UP) { placeState = Blocks.TORCH.defaultBlockState(); }
            else { placeState = Blocks.WALL_TORCH.defaultBlockState().setValue(WallTorchBlock.FACING, hitDirection.getOpposite()); }
        }
        else { placeState = blockToPlace != null ? blockToPlace.defaultBlockState() : null; }
        if (level().getBlockState(hitPos).isAir()) {
            if (placeState != null && placeState.canSurvive(level(), hitPos)) { level().setBlockAndUpdate(hitPos, placeState); }
        }
        this.discard();
    }
}