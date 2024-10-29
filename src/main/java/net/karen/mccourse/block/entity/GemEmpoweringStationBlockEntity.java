package net.karen.mccourse.block.entity;

import net.karen.mccourse.block.custom.GemEmpoweringStationBlock;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.recipe.GemEmpoweringRecipe;
import net.karen.mccourse.screen.GemEmpoweringStationMenu;
import net.karen.mccourse.util.InventoryDirectionEntry;
import net.karen.mccourse.util.InventoryDirectionWrapper;
import net.karen.mccourse.util.ModEnergyStorage;
import net.karen.mccourse.util.WrappedHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class GemEmpoweringStationBlockEntity extends BlockEntity implements MenuProvider {
    // Custom block entity GUI
    private final ItemStackHandler itemHandler = new ItemStackHandler(4) {

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        // An item inserted it is valid
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0, 1 -> true; // Input a fluid or an item = Fluid input slot and Input slot
                case 2 -> false; // Output an item = Output slot
                case 3 -> stack.getItem() == ModItems.KOHLRABI.get(); // Transform an item on energy = Energy item slot
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    // Constants are the items inserted on custom block entity
    private static final int INPUT_SLOT = 0;
    private static final int FLUID_INPUT_SLOT = 1;
    private static final int OUTPUT_SLOT = 2;
    private static final int ENERGY_ITEM_SLOT = 3;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    // Hopper item inserted on Gem Empowering Station
    // DOWN, SOUTH, and, EAST directions doesn't worked, but NORTH, WEST, and, UP directions is worked
    private final Map<Direction, LazyOptional<WrappedHandler>> directionWrappedHandlerMap =
            new InventoryDirectionWrapper(itemHandler,
                    new InventoryDirectionEntry(Direction.DOWN, OUTPUT_SLOT, false),
                    new InventoryDirectionEntry(Direction.NORTH, INPUT_SLOT, true),
                    new InventoryDirectionEntry(Direction.SOUTH, OUTPUT_SLOT, false),
                    new InventoryDirectionEntry(Direction.EAST, OUTPUT_SLOT, false),
                    new InventoryDirectionEntry(Direction.WEST, INPUT_SLOT, true),
                    new InventoryDirectionEntry(Direction.UP, INPUT_SLOT, true)).directionsMap;

    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    // Progress bar when an item transform on other
    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    // Progress bar when an item transform on custom energy storage - CLIENT and SERVER is synchronized
    private final ModEnergyStorage ENERGY_STORAGE = createEnergyStorage();

    private ModEnergyStorage createEnergyStorage() {
        return new ModEnergyStorage(64000, 200) {
            @Override
            public void onEnergyChanged() {
                setChanged();
                getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        };
    }

    // Variables progress and maxProgress synchronization
    public GemEmpoweringStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.GEM_EMPOWERING_STATION_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> GemEmpoweringStationBlockEntity.this.progress;
                    case 1 -> GemEmpoweringStationBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> GemEmpoweringStationBlockEntity.this.progress = pValue;
                    case 1 -> GemEmpoweringStationBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2; // Two integers to synchronize = progress and maxProgress int variables
            }
        };
    }

    // Restore energy storage saved
    public IEnergyStorage getEnergyStorage() {
        return this.ENERGY_STORAGE;
    }

    // Drops all items on inventory
    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    // Name that to show in screen
    @Override
    public Component getDisplayName() {
        return Component.literal("Gem Empowering Station");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new GemEmpoweringStationMenu(pContainerId, pPlayerInventory, this, this.data);
    }

    // Avoid losing items when inserted on custom block entity
    // Adding side inventory on custom block entity as example a hopper
    // Detected hopper's placed direction
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyHandler.cast();
        }

        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) {
                return lazyItemHandler.cast();
            }

            if(directionWrappedHandlerMap.containsKey(side)) {
                Direction localDir = this.getBlockState().getValue(GemEmpoweringStationBlock.FACING);

                if(side == Direction.DOWN ||side == Direction.UP) {
                    return directionWrappedHandlerMap.get(side).cast();
                }

                return switch (localDir) {
                    default -> directionWrappedHandlerMap.get(side.getOpposite()).cast();
                    case EAST -> directionWrappedHandlerMap.get(side.getClockWise()).cast();
                    case SOUTH -> directionWrappedHandlerMap.get(side).cast();
                    case WEST -> directionWrappedHandlerMap.get(side.getCounterClockWise()).cast();
                };
            }
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyHandler.invalidate();
    }

    // Save all items of inventory
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("gem_empowering_station.progress", progress);
        pTag.putInt("energy", ENERGY_STORAGE.getEnergyStored());
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("gem_empowering_station.progress");
        ENERGY_STORAGE.setEnergy(pTag.getInt("energy"));
    }

    // Custom block entity class to work on Server side
    public void tick(Level level, BlockPos pPos, BlockState pState) {
        fillUpOnEnergy(); // This is a "placeholder" for getting energy through wires or similar

        if (isOutputSlotEmptyOrReceivable() && hasRecipe()) {
            increaseCraftingProcess();
            extractEnergy();
            setChanged(level, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    private void extractEnergy() {
        this.ENERGY_STORAGE.extractEnergy(100, false);
    }


    private void fillUpOnEnergy() {
        if(hasEnergyItemInSlot(ENERGY_ITEM_SLOT)) {

            if(this.itemHandler.getStackInSlot(ENERGY_ITEM_SLOT).getCount() > 0) {

                int energyPlus1k = ENERGY_STORAGE.getEnergyStored() + 1000;

                while(ENERGY_STORAGE.getEnergyStored() != energyPlus1k) {
                    this.ENERGY_STORAGE.receiveEnergy(1000, false);
                }
                itemHandler.extractItem(ENERGY_ITEM_SLOT, 1, false);
            }
        }
    }

    private boolean hasEnergyItemInSlot(int energyItemSlot) {
        return !this.itemHandler.getStackInSlot(energyItemSlot).isEmpty() &&
                this.itemHandler.getStackInSlot(energyItemSlot).getItem() == ModItems.KOHLRABI.get();
    }

    private void craftItem() {
        Optional<GemEmpoweringRecipe> recipe = getCurrentRecipe();
        ItemStack resultItem = recipe.get().getResultItem(getLevel().registryAccess());

        this.itemHandler.extractItem(INPUT_SLOT, 1, false); // Input slot

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(resultItem.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + resultItem.getCount())); // Output slot
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private boolean hasProgressFinished() {
        return this.progress >= this.maxProgress;
    }

    private void increaseCraftingProcess() {
        this.progress++;
    }

    private boolean hasRecipe() {
        Optional<GemEmpoweringRecipe> recipe = getCurrentRecipe();

        if (recipe.isEmpty()) { return false; }
        ItemStack resultItem = recipe.get().getResultItem(getLevel().registryAccess());
        return canInsertAmountIntoOutputSlot(resultItem.getCount())
                && canInsertItemIntoOutputSlot(resultItem.getItem()) && hasEnoughEnergyToCraft();
    }

    private boolean hasEnoughEnergyToCraft() {
        return this.ENERGY_STORAGE.getEnergyStored() >= 100 * maxProgress;
    }

    // Verify all custom recipes if are added or not inserted
    private Optional<GemEmpoweringRecipe> getCurrentRecipe() {
        SimpleContainer inventory = new SimpleContainer(this.itemHandler.getSlots());
        for (int i = 0; i < this.itemHandler.getSlots(); i++) {
            inventory.setItem(i, this.itemHandler.getStackInSlot(i));
        }

        return this.level.getRecipeManager().getRecipeFor(GemEmpoweringRecipe.Type.INSTANCE, inventory, level);
    }

    private boolean canInsertItemIntoOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    private boolean canInsertAmountIntoOutputSlot(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize() >=
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count;
    }

    private boolean isOutputSlotEmptyOrReceivable() {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() ||
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() < this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    // Save and restore on disk the energy storage
    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        super.onDataPacket(net, pkt);
    }
}