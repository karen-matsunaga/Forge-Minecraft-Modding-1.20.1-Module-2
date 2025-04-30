package net.karen.mccourse.block.entity;

import io.netty.buffer.Unpooled;
import net.karen.mccourse.screen.CraftCraftingTableMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CraftCraftingTableBlockEntity extends RandomizableContainerBlockEntity {
    protected CraftCraftingTableBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CRAFT_CRAFTING_TABLE_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    protected NonNullList<ItemStack> getItems() { return this.getItems(); }

    @Override
    protected void setItems(NonNullList<ItemStack> pItemStacks) { this.getItems(); }

    @Override
    protected Component getDefaultName() { return Component.literal("craft"); }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new CraftCraftingTableMenu(id, inventory, (ContainerLevelAccess)
                new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
    }

    @Override
    public int getContainerSize() { return 1000; }
}
