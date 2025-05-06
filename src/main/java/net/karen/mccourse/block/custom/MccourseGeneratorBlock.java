package net.karen.mccourse.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

import java.util.List;

public class MccourseGeneratorBlock extends Block {
    List<Item> items;
    public MccourseGeneratorBlock(Properties pProperties, List<Item> itemList) {
        super(pProperties);
        this.items = itemList;
    }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos,
                                       Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide) {
            if (player.isCreative()) { return true; }
            else {
                for (Item item : items) { // Items generated
                    ItemStack stack = new ItemStack(item);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
                level.sendBlockUpdated(pos, state, state, 3);
                return false;
            }
        }
        return player.isCreative(); // Prevents the block from being destroyed
    }
}