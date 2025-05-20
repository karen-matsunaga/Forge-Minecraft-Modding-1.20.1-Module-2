package net.karen.mccourse.block.custom;

import net.karen.mccourse.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
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
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest,
                                       FluidState fluid) {
        ItemStack heldItem = player.getMainHandItem();
        boolean destroy = player.isCreative() || heldItem.is(ModItems.PINK_PICKAXE.get());
        if (!level.isClientSide()) {
            if (destroy) { // ENABLES destruction on CREATIVE mode or uses PINK PICKAXE, but not received DROP items
                return level.destroyBlock(pos, false); }
            else {
                for (Item item : items) { // Items generated only on SURVIVAL mode
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(item));
                }
                level.sendBlockUpdated(pos, state, state, 3); // PREVENTS destruction of block
                return false;
            }
        }
        return destroy; // On the SERVER side, it allows only if you are creative
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        // Checks if the player is holding the specific item is PINK PICKAXE
        return player.getMainHandItem().is(ModItems.PINK_PICKAXE.get());
    }
}