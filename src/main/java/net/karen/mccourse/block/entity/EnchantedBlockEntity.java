package net.karen.mccourse.block.entity;

import io.netty.buffer.Unpooled;
import net.karen.mccourse.screen.EnchantedMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import org.jetbrains.annotations.Nullable;

import java.util.stream.IntStream;

public class EnchantedBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer {
    private NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);
    private final LazyOptional<? extends IItemHandler>[] handlers = SidedInvWrapper.create(this, Direction.values());

    public EnchantedBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENCHANTED_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        if (!this.tryLoadLootTable(compound)) this.stacks = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, this.stacks);
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (!this.trySaveLootTable(compound)) { ContainerHelper.saveAllItems(compound, this.stacks); }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() { return ClientboundBlockEntityDataPacket.create(this); }

    @Override
    public CompoundTag getUpdateTag() { return this.saveWithFullMetadata(); }

    @Override
    public int getContainerSize() { return stacks.size(); }

    @Override
    public boolean isEmpty() { for (ItemStack itemstack : this.stacks) { if (!itemstack.isEmpty()) { return false; } } return true; }

    @Override
    public Component getDefaultName() { return Component.literal("enchanted"); }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new EnchantedMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(this.worldPosition));
    }

    @Override
    public int getMaxStackSize() { return 6400; }

    @Override
    public Component getDisplayName() { return Component.literal("Enchanted"); }

    @Override
    protected NonNullList<ItemStack> getItems() { return this.stacks; }

    @Override
    protected void setItems(NonNullList<ItemStack> stacks) { this.stacks = stacks; }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) { if (index == 2) { return false; } return true; }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        if (!this.remove && facing != null && capability == ForgeCapabilities.ITEM_HANDLER)
            return handlers[facing.ordinal()].cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void setRemoved() { super.setRemoved(); for (LazyOptional<? extends IItemHandler> handler : handlers) handler.invalidate(); }

    @Override
    public int[] getSlotsForFace(Direction side) { return IntStream.range(0, this.getContainerSize()).toArray(); }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, @Nullable Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) { return true; }
}