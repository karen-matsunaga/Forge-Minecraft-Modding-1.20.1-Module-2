package net.karen.mccourse.entity.custom;

import net.karen.mccourse.entity.ModEntities;
import net.minecraft.core.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class TorchBallProjectileEntity extends ThrowableItemProjectile {
    public TorchBallProjectileEntity(EntityType<? extends ThrowableItemProjectile> entityType,
                                     Level level) { super(entityType, level); }

    public TorchBallProjectileEntity(Level level) {
        this(ModEntities.TORCH_BALL_PROJECTILE.get(), level);
    }

    public TorchBallProjectileEntity(Level level, LivingEntity livingEntity) {
        super(ModEntities.TORCH_BALL_PROJECTILE.get(), livingEntity, level);
    }

    @Override
    protected @NotNull Item getDefaultItem() { return Blocks.TORCH.asItem(); }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        super.onHitBlock(result);
        if (level().isClientSide()) { return; }
        Direction hitDir = result.getDirection();
        BlockPos hitPos = result.getBlockPos(), placePos;
        BlockState placeState = null;
        Block torch = Blocks.TORCH, wallTorch = Blocks.WALL_TORCH,
              blockToPlace = getItem().getItem() instanceof BlockItem item ? item.getBlock() : torch; // Get the associated item
        if (blockToPlace == torch || blockToPlace == wallTorch) {
            if (hitDir == Direction.UP) {
                placePos = hitPos.above(); // Place a normal torch on top of the block
                placeState = torch.defaultBlockState();
            }
            else if (hitDir.getAxis().isHorizontal()) {
                placePos = hitPos.relative(hitDir); // Air block where the torch will be placed
                if (!level().getBlockState(hitPos).isSolidRender(level(), hitPos)) { return; } // Check if the wall is solid
                if (WallTorchBlock.FACING.getPossibleValues().contains(hitDir)) { // Check the direction is valid
                    placeState = wallTorch.defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, hitDir);
                    level().setBlock(placePos, placeState, Block.UPDATE_ALL_IMMEDIATE); // Place the torch in the air, glued to the wall
                }
            }
            else { return; } // Does not support placement on the ceiling
        }
        else { // Place normal blocks on any valid face
            placePos = hitPos.relative(hitDir);
            placeState = blockToPlace.defaultBlockState();
        }

        if (placeState != null && level().getBlockState(placePos).isAir() && placeState.canSurvive(level(), placePos)) {
            level().setBlockAndUpdate(placePos, placeState); // Place the block if the location is empty and can hold it
        }
        this.discard(); // Remove the entity after use
    }
}