package net.karen.mccourse.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MccourseItem extends DiggerItem implements Vanishable {
    public MccourseItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, TagKey<Block> blockTags,
                      Properties pProperties) {
        super(pAttackDamageModifier, pAttackSpeedModifier, pTier, blockTags, pProperties);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack pStack, Level pLevel, @NotNull BlockState pState,
                             @NotNull BlockPos pPos, @NotNull LivingEntity pEntityLiving) {
        if (!pLevel.isClientSide) {
            double x = pPos.getX();
            double y = pPos.getY();
            double z = pPos.getZ();
            hammerMode(pLevel, x, y, z, pEntityLiving);
        }
        return super.mineBlock(pStack, pLevel, pState, pPos, pEntityLiving);
    }

    // Hammer 2x1
    public static void hammerMode(LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null) return;
        if ((entity.getDirection()) == Direction.NORTH) {
            {
                BlockPos pos = BlockPos.containing(x, y, z);
                Block.dropResources(world.getBlockState(pos), world, BlockPos.containing(x, y, z), null);
                world.destroyBlock(pos, false);
            }
            {
                BlockPos pos = BlockPos.containing(x, y - 1, z);
                Block.dropResources(world.getBlockState(pos), world, BlockPos.containing(x, y, z), null);
                world.destroyBlock(pos, false);
            }
        }
        if ((entity.getDirection()) == Direction.SOUTH) {
            {
                BlockPos pos = BlockPos.containing(x, y, z);
                Block.dropResources(world.getBlockState(pos), world, BlockPos.containing(x, y, z), null);
                world.destroyBlock(pos, false);
            }
            {
                BlockPos pos = BlockPos.containing(x, y - 1, z);
                Block.dropResources(world.getBlockState(pos), world, BlockPos.containing(x, y, z), null);
                world.destroyBlock(pos, false);
            }
        }
        if ((entity.getDirection()) == Direction.WEST) {
            {
                BlockPos pos = BlockPos.containing(x, y, z);
                Block.dropResources(world.getBlockState(pos), world, BlockPos.containing(x, y, z), null);
                world.destroyBlock(pos, false);
            }
            {
                BlockPos pos = BlockPos.containing(x, y - 1, z);
                Block.dropResources(world.getBlockState(pos), world, BlockPos.containing(x, y, z), null);
                world.destroyBlock(pos, false);
            }
        }
        if ((entity.getDirection()) == Direction.EAST) {
            {
                BlockPos _pos = BlockPos.containing(x, y, z);
                Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
                world.destroyBlock(_pos, false);
            }
            {
                BlockPos _pos = BlockPos.containing(x, y - 1, z);
                Block.dropResources(world.getBlockState(_pos), world, BlockPos.containing(x, y, z), null);
                world.destroyBlock(_pos, false);
            }
        }
    }

    // Appears tooltip on screen of Hammer, Paxel, etc.
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.literal("Hammer breaks: 2x1"));
    }
}