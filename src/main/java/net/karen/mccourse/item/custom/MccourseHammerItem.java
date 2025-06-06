package net.karen.mccourse.item.custom;

import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.*;
import java.util.List;

public class MccourseHammerItem extends DiggerItem implements Vanishable {
    public MccourseHammerItem(Tier tier, float attackDamageModifier, float attackSpeedModifier,
                              TagKey<Block> blockTags, Properties properties) {
        super(attackDamageModifier, attackSpeedModifier, tier, blockTags, properties);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, Level level, @NotNull BlockState state,
                             @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        if (!level.isClientSide()) { hammerMode(level, pos, entity); }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    public void hammerMode(LevelAccessor world, BlockPos origin, Entity entity) {
        Direction direction = getFacingDirection(entity);
        switch (direction) { // If looking UP or DOWN: break 2 vertical blocks (y and y+1)
            case UP, DOWN -> { blocks(origin.above(), world); blocks(origin.below(), world); }
            // If looking NORTH, SOUTH, WEST or EAST: break 2 vertical blocks (y and y-1)
            case NORTH, SOUTH, WEST, EAST -> blocks(origin.below(), world);
        }
    } // CUSTOM METHOD - Hammer breaks 2x1

    private Direction getFacingDirection(Entity entity) {
        return Direction.getNearest(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z);
    } // CUSTOM METHOD - Gets the direction the player is looking

    private void blocks(BlockPos position, LevelAccessor world) {
        if (!world.isEmptyBlock(position)) { // CUSTOM METHOD - Break the block in the world
            Block.dropResources(world.getBlockState(position), world, position, null);
            world.destroyBlock(position, false);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        components.add(Component.literal("Hammer breaks: 2x1")); // Appears TOOLTIP on screen of Hammer, Paxel, etc.
        super.appendHoverText(stack, level, components, tooltipFlag);
    }
}