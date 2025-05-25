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

public class MccourseHammerItem extends DiggerItem implements Vanishable {
    public MccourseHammerItem(Tier tier, float pAttackDamageModifier, float pAttackSpeedModifier,
                        TagKey<Block> blockTags, Properties properties) {
        super(pAttackDamageModifier, pAttackSpeedModifier, tier, blockTags, properties);
    }

    @Override
    public boolean mineBlock(@NotNull ItemStack stack, Level level, @NotNull BlockState state,
                             @NotNull BlockPos pos, @NotNull LivingEntity entity) {
        if (!level.isClientSide()) { hammerMode(level, pos.getX(), pos.getY(), pos.getZ(), entity); }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    // Hammer 2x1
    public void hammerMode(LevelAccessor world, double x, double y, double z, Entity entity) {
        positions(Direction.NORTH, x, y, z, entity, world);
        positions(Direction.SOUTH, x, y, z, entity, world);
        positions(Direction.WEST, x, y, z, entity, world);
        positions(Direction.EAST, x, y, z, entity, world);
    }

    // Appears tooltip on screen of Hammer, Paxel, etc.
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        components.add(Component.literal("Hammer breaks: 2x1"));
        super.appendHoverText(stack, level, components, tooltipFlag);
    }

    private void positions(Direction direction, double x, double y, double z,
                           Entity entity, LevelAccessor world) {
        if (entity.getDirection() == direction) {
            { blocks(x, y, z, world); }
            { blocks(x, y - 1, z, world); }
        }
    }

    private void blocks(double x, double y, double z, LevelAccessor world) {
        BlockPos position = BlockPos.containing(x, y, z);
        Block.dropResources(world.getBlockState(position), world, BlockPos.containing(x, y, z), null);
        world.destroyBlock(position, false);
    }
}