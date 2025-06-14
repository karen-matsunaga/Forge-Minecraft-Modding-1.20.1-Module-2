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
    private final int distance; // 2 x 1 x Z -> Break straight distance

    public MccourseHammerItem(Tier tier, float attackDamageModifier, float attackSpeedModifier,
                              Properties properties, TagKey<Block> blockTags, int distance) {
        super(attackDamageModifier, attackSpeedModifier, tier, blockTags, properties);
        this.distance = distance;
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, Level level, @NotNull BlockState state,
                             @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        if (!level.isClientSide()) { hammer(level, pos, entity); }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    // CUSTOM METHOD - Hammer Mode
    public void hammer(LevelAccessor world, BlockPos origin, Entity entity) {
        double x = entity.getLookAngle().x, y = entity.getLookAngle().y, z = entity.getLookAngle().z;
        Direction direction = Direction.getNearest(x, y, z);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < distance; i++) {
            pos.set(origin).move(direction, i); // Calculate the position of the block to be broken at step i in the look direction
            blocks(pos, world); // Breaks the block at the current height (y)
            pos.setY(pos.getY() + 1); // Break the block above (y + 1) to make height 2
            blocks(pos, world);
        }
    }

    // CUSTOM METHOD - Break the block in the world
    private void blocks(BlockPos position, LevelAccessor world) {
        if (!world.isEmptyBlock(position)) {
            Block.dropResources(world.getBlockState(position), world, position, null);
            world.destroyBlock(position, false);
        }
    }

    // Appears TOOLTIP on screen of Hammer, Paxel, etc.
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        components.add(Component.literal("Hammer breaks: 2x1x" + this.distance));
        super.appendHoverText(stack, level, components, tooltipFlag);
    }
}