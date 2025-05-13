package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
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
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FarmerItem extends Item {
    public FarmerItem(Properties pProperties) { super(pProperties); }

    List<Block> farmerBlocks =  List.of(Blocks.SUGAR_CANE, Blocks.CACTUS, Blocks.NETHER_WART,
            Blocks.TWISTING_VINES, Blocks.WEEPING_VINES, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        ItemStack stack = context.getItemInHand();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

        if (level.isClientSide()) { return InteractionResult.SUCCESS; }

        // Checks if the block is in the tag
        if (!Objects.requireNonNull(ForgeRegistries.BLOCKS.tags())
                .getTag(ModTags.Blocks.FARMER_INSTANT_GROWABLES).contains(block)) {
            return InteractionResult.PASS;
        }

        // Standard Bonemealable
        if (block instanceof BonemealableBlock growable && growable.isValidBonemealTarget(level, pos, state, false)) {
            growable.performBonemeal((ServerLevel) level, level.random, pos, state);

            BlockState newState = level.getBlockState(pos);
            for (Property<?> property : newState.getProperties()) {
                if (property.getName().equals("age") && property instanceof IntegerProperty ageProp) {
                    int max = Collections.max(ageProp.getPossibleValues());
                    level.setBlock(pos, newState.setValue(ageProp, max), 2);
                    break;
                }
            }
            consumeItem(stack, player);
            return InteractionResult.SUCCESS;
        }

        // Vertical growth (if tagged)
        if (farmerBlocks.contains(block)) {
            growVertically(level, pos, block);
            consumeItem(stack, player);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    // Every time it is used it is consumed
    private void consumeItem(ItemStack stack, Player player) {
        if (player != null && !player.getAbilities().instabuild) {
            stack.shrink(1);
        }
    }

    // Used Farmer on grow vertically
    private void growVertically(Level level, BlockPos pos, Block block) {
        int maxHeight = 5;
        BlockPos.MutableBlockPos current = pos.mutable();

        while (level.getBlockState(current.above()).is(block) && maxHeight-- > 0) {
            current.move(Direction.UP);
        }

        for (int i = 0; i < 3; i++) {
            BlockPos above = current.above();
            if (level.isEmptyBlock(above) && block.defaultBlockState().canSurvive(level, above)) {
                level.setBlock(above, block.defaultBlockState(), 3);
                current = above.mutable();
            } else break;
        }

        // Used Farmer on Nether wart
        if (farmerBlocks.contains(Blocks.NETHER_WART)) {
            BlockState state = level.getBlockState(pos);
            if (state.hasProperty(BlockStateProperties.AGE_3)) {
                level.setBlock(pos, state.setValue(BlockStateProperties.AGE_3, 3), 2);
            }
        }
    }
}