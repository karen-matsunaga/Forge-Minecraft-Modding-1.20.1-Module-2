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
import org.jetbrains.annotations.NotNull;

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
        if (!level.isClientSide) {
            if (player.isCreative()) {
                level.destroyBlock(pos, false); // Destroyed block
                return true;
            } // Enables destruction in the creative
            else {
                for (Item item : items) { // Drop custom items only in survival
                    ItemStack stack = new ItemStack(item);
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                }
                level.sendBlockUpdated(pos, state, state, 3); // Reseat the block - Prevents destruction outside of creative
                return false;
            }
        }
        return player.isCreative(); // On the client side, it allows only if you are creative
    }

    @Override
    public void playerWillDestroy(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull Player player) {
        super.playerWillDestroy(level, pos, state, player);
    }
}