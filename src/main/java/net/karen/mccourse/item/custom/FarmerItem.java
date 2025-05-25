package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

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
        BlockState contains = block.defaultBlockState();
        if (!level.isClientSide() && player != null) {
            @Nullable ITagManager<Block> blockTag = ForgeRegistries.BLOCKS.tags(); // Checks if the block is in the tag
            if (isBlock(blockTag, ModTags.Blocks.FARMER_INSTANT_GROWABLES, block)) {
                // Standard Bonemealable
                if (block instanceof BonemealableBlock growable && growable.isValidBonemealTarget(level, pos, state, false)) {
                    growable.performBonemeal((ServerLevel) level, level.random, pos, state);
                    BlockState newState = level.getBlockState(pos);
                    for (Property<?> property : newState.getProperties()) {
                        if (property.getName().equals("age") && property instanceof IntegerProperty age) {
                            grow(level, pos, newState.setValue(age, Collections.max(age.getPossibleValues())), 2);
                            break;
                        }
                    }
                    consumeItem(stack, player);
                    return InteractionResult.SUCCESS;
                }
                // Vertical growth (if tagged)
                if (isBlock(blockTag, ModTags.Blocks.FARMER_BLOCK_GROWABLES, block)) {
                    // Used Farmer on grow vertically
                    int maxHeight = 5;
                    BlockPos.MutableBlockPos current = pos.mutable();

                    while (level.getBlockState(current.above()).is(block) && maxHeight-- > 0) { current.move(Direction.UP); }

                    for (int i = 0; i < 3; i++) {
                        BlockPos above = current.above();
                        if (level.isEmptyBlock(above) && contains.canSurvive(level, above)) {
                            grow(level, above, contains, 3);
                            current = above.mutable();
                        }
                        else { break; }
                    }
                    // Used Farmer on Nether wart
                    if (contains.is(Blocks.NETHER_WART)) {
                        if (state.hasProperty(BlockStateProperties.AGE_3)) {
                            grow(level, pos, state.setValue(BlockStateProperties.AGE_3, 3), 2);
                        }
                    }
                    consumeItem(stack, player);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    // Every time it is used it is consumed
    private void consumeItem(ItemStack stack, Player player) {
        if (!player.getAbilities().instabuild) { stack.shrink(1); }
    }

    private void grow(Level level, BlockPos pos, BlockState state, int flag) {
        level.setBlock(pos, state, flag);
    }

    private boolean isBlock(ITagManager<Block> registry, TagKey<Block> blocks, Block block) {
        return registry != null && registry.getTag(blocks).contains(block);
    }
}