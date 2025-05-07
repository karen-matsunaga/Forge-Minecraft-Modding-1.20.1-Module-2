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
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                       boolean willHarvest, FluidState fluid) {
        ItemStack heldItem = player.getMainHandItem();
        if (!level.isClientSide) {
            if (player.isCreative() || heldItem.is(ModItems.PINK_PICKAXE.get())) {
                // Destroyed block, but not received drop block
                return level.destroyBlock(pos, false); // Enables destruction in the creative
            }
            else {
                for (Item item : items) { // Drop custom items only in survival
                    ItemStack stack = new ItemStack(item);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack); // Item generated
                }
                level.sendBlockUpdated(pos, state, state, 3); // Reseat the block - Prevents destruction outside of creative
                return false;
            }
        }
        return player.isCreative() || heldItem.is(ModItems.PINK_PICKAXE.get()); // On the client side, it allows only if you are creative
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        // Checks if the player is holding the specific item
        ItemStack heldItem = player.getMainHandItem();
        return heldItem.is(ModItems.PINK_PICKAXE.get()); // Change to your item
    }
}