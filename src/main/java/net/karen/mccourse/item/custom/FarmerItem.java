package net.karen.mccourse.item.custom;

import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.*;
import java.util.Collections;
import static net.karen.mccourse.util.ModTags.Blocks.*;

public class FarmerItem extends Item {
    public FarmerItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();
        ITagManager<Block> blockTag = ForgeRegistries.BLOCKS.tags(); // Checks if the block is in the tag
        if (!level.isClientSide() && player != null && blockTag != null) {
            if (isBlock(blockTag, FARMER_TREE_GROWABLES, block) && block instanceof BonemealableBlock grow &&
                grow.isValidBonemealTarget(level, pos, state, false)) {
                grow.performBonemeal((ServerLevel) level, level.random, pos, state); // Standard Bonemealable
                return consumeAndSucceed(stack, player); // Used Farmer on TREES
            }
            if (isBlock(blockTag, FARMER_CROPS_GROWABLES, block)) {
                for (Property<?> property : state.getProperties()) {
                    if (property.getName().equals("age") && property instanceof IntegerProperty age) {
                        grow(level, pos, state.setValue(age, Collections.max(age.getPossibleValues())), 2);
                        break;
                    }
                }
                return consumeAndSucceed(stack, player); // Used Farmer on CROPS
            }
            if (isBlock(blockTag, FARMER_VERTICAL_GROWABLES, block)) {
                int height = 0;
                BlockState contains = block.defaultBlockState();
                BlockPos.MutableBlockPos current = pos.mutable();
                while (level.getBlockState(current.above()).is(block) && height++ < 16) { current.move(Direction.UP); }
                for (int i = 0; i < 3; i++) {
                    BlockPos above = current.above();
                    if (level.isEmptyBlock(above) && contains.canSurvive(level, above)) {
                        grow(level, above, contains, 3);
                        current = above.mutable();
                    }
                    else { break; }
                }
                return consumeAndSucceed(stack, player); // Vertical growth (if tagged) - Used Farmer on GROW VERTICALLY
            }
            if (isBlock(blockTag, FARMER_AGE_GROWABLES, block) && state.hasProperty(BlockStateProperties.AGE_3)) {
                grow(level, pos, state.setValue(BlockStateProperties.AGE_3, 3), 2);
                return consumeAndSucceed(stack, player); // Used Farmer on NETHER WART
            }
        }
        return InteractionResult.PASS;
    }

    private InteractionResult consumeAndSucceed(ItemStack stack, Player player) {
        if (!player.getAbilities().instabuild) { stack.shrink(1); } // Every time it is used it is consumed
        return InteractionResult.SUCCESS;
    }

    private void grow(Level level, BlockPos pos, BlockState state, int flag) {
        level.setBlock(pos, state, flag);
    }

    private boolean isBlock(ITagManager<Block> registry, TagKey<Block> blocks, Block block) {
        return registry.getTag(blocks).contains(block);
    }
}