package net.karen.mccourse.item.custom;

import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
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
import static net.karen.mccourse.util.Utils.*;

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
            if (isBlockTag(blockTag, FARMER_TREE_GROWABLES, block)) {
                if (block instanceof BonemealableBlock growable) { // Standard Bonemealable
                    if (growable.isValidBonemealTarget(level, pos, state, false)) {
                        if (block instanceof SaplingBlock sapling) { // Vanilla sapling -> Oak, Acacia, etc.
                            sapling.advanceTree((ServerLevel) level, pos, state, level.random);
                        }
                        else if (block instanceof MangrovePropaguleBlock mangrove) { // Dark and Jungle saplings
                            mangrove.advanceTree((ServerLevel) level, pos, state, level.random);
                        }
                        else { growable.performBonemeal((ServerLevel) level, level.random, pos, state); }
                        return consumeAndSucceed(stack, player); // Used Farmer on TREES
                    }
                    else { return InteractionResult.SUCCESS; }
                }
            }
            if (isBlockTag(blockTag, FARMER_CROPS_GROWABLES, block)) { // Potato, Carrot, Beetroot, etc.
                for (Property<?> property : state.getProperties()) {
                    if (property.getName().equals("age") && property instanceof IntegerProperty age) {
                        int currentAge = state.getValue(age), maxAge = Collections.max(age.getPossibleValues());
                        if (currentAge < maxAge) {
                            grow(level, pos, state.setValue(age, maxAge), 2);
                            return consumeAndSucceed(stack, player); // Used Farmer on CROPS
                        }
                        else { return InteractionResult.SUCCESS; } // Crops are max age level
                    }
                }
            }
            if (isBlockTag(blockTag, FARMER_VERTICAL_GROWABLES, block)) { // Bamboo, Sugar cane and Cactus
                int height = 0;
                BlockState contains = block.defaultBlockState();
                BlockPos.MutableBlockPos current = pos.mutable();
                while (level.getBlockState(current.above()).is(block) && height++ < 16) { current.move(Direction.UP); }
                boolean grew = false; // Vertical grow not max level
                for (int i = 0; i < 3; i++) {
                    BlockPos above = current.above();
                    if (level.isEmptyBlock(above) && contains.canSurvive(level, above)) {
                        grow(level, above, contains, 3);
                        current = above.mutable();
                        grew = true; // Vertical grow max age level
                    }
                    else { break; }
                }
                // Vertical growth (if tagged) - Used Farmer on GROW VERTICALLY
                return grew ? consumeAndSucceed(stack, player) : InteractionResult.SUCCESS;
            }
            if (isBlockTag(blockTag, FARMER_AGE_GROWABLES, block) && state.hasProperty(BlockStateProperties.AGE_3)) {
                if (state.getValue(BlockStateProperties.AGE_3) < 3) {
                    grow(level, pos, state.setValue(BlockStateProperties.AGE_3, 3), 2);
                    return consumeAndSucceed(stack, player); // Used Farmer on NETHER WART
                }
                else { return InteractionResult.SUCCESS; } // Nether Wart is max age level
            }
        }
        return InteractionResult.PASS;
    }

    // CUSTOM METHOD - Consumed item
    private InteractionResult consumeAndSucceed(ItemStack stack, Player player) {
        if (!player.getAbilities().instabuild) { stack.shrink(1); } // Every time it is used it is consumed
        return InteractionResult.SUCCESS;
    }
}