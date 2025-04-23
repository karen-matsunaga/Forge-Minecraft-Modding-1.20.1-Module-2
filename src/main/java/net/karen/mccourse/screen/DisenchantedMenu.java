package net.karen.mccourse.screen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.entity.DisenchantedBlockEntity;
import net.karen.mccourse.network.DisenchantedGuiSlotMessage;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.Entity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.Map;
import java.util.HashMap;

public class DisenchantedMenu extends AbstractContainerMenu implements Supplier<Map<Integer, Slot>> {
    public Level world;
    public Player entity;
    public int x, y, z;
    private ContainerLevelAccess access = ContainerLevelAccess.NULL;
    private IItemHandler internal = new ItemStackHandler(3);
    private final Map<Integer, Slot> customSlots = new HashMap<>();
    private boolean bound = false;
    private Supplier<Boolean> boundItemMatcher = null;
    private Entity boundEntity = null;
    private BlockEntity boundBlockEntity = null;

    public DisenchantedMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        super(ModMenuTypes.DISENCHANTED_MENU.get(), id);
        this.entity = inv.player;
        this.world = inv.player.level();

        if (extraData != null) {
            BlockPos pos = extraData.readBlockPos();
            this.x = pos.getX();
            this.y = pos.getY();
            this.z = pos.getZ();
            this.access = ContainerLevelAccess.create(world, pos);

            if (extraData.readableBytes() == 1) {
                byte hand = extraData.readByte();
                ItemStack itemstack = hand == 0 ? entity.getMainHandItem() : entity.getOffhandItem();
                this.boundItemMatcher = () -> itemstack == (hand == 0 ? entity.getMainHandItem() : entity.getOffhandItem());
                bindCapability(itemstack);
            } else if (extraData.readableBytes() > 1) {
                extraData.readByte(); // Drop padding
                boundEntity = world.getEntity(extraData.readVarInt());
                if (boundEntity != null) bindCapability(boundEntity);
            } else {
                boundBlockEntity = world.getBlockEntity(pos);
                if (boundBlockEntity != null) bindCapability(boundBlockEntity);
            }
        }

        addCustomSlots();
        addPlayerInventorySlots(inv);
    }

    private void bindCapability(Object target) {
        if (target instanceof ItemStack item)
            item.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(cap -> { internal = cap; bound = true; });
        else if (target instanceof Entity entity)
            entity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(cap -> { internal = cap; bound = true; });
        else if (target instanceof BlockEntity blockEntity)
            blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).ifPresent(cap -> { internal = cap; bound = true; });
    }

    // Added custom slots
    private void addCustomSlots() {
        customSlots.put(0, addSlot(new SlotItemHandler(internal, 0, 26, 47) {
            @Override public boolean mayPlace(@NotNull ItemStack stack) {
                return !stack.is(Items.ENCHANTED_BOOK) && !stack.isStackable();
            } // Place only enchanted items expect Enchanted Book
            @Override public int getMaxStackSize() { return 1; } // Accepts only 1 item
        }));
        customSlots.put(1, addSlot(new SlotItemHandler(internal, 1, 79, 47) {
            @Override public boolean mayPlace(@NotNull ItemStack stack) { return stack.is(Items.BOOK); } // Place only book
            @Override public int getMaxStackSize() { return 1; } // Accepts only 1 book
        }));
        customSlots.put(2, addSlot(new SlotItemHandler(internal, 2, 138, 47) {
            @Override public boolean mayPlace(@NotNull ItemStack stack) { return false; } // Nothing is placed
            @Override public void setChanged() {
                super.setChanged();
                slotChanged(2, 0, ClickType.QUICK_MOVE);
            } // If player clicked on item with LEFT click or SHIFT + LEFT click
            @Override public int getMaxStackSize() { return 1; } // Accepts an output contained only 1 enchanted book
        }));
    }

    private void addPlayerInventorySlots(Inventory inv) {
        for (int row = 0; row < 3; ++row)
            for (int col = 0; col < 9; ++col) this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
        for (int col = 0; col < 9; ++col) this.addSlot(new Slot(inv, col, 8 + col * 18, 142));
    }

    @Override
    public boolean stillValid(Player player) {
        if (bound) {
            if (boundItemMatcher != null) return boundItemMatcher.get();
            if (boundBlockEntity != null) return stillValid(access, player, boundBlockEntity.getBlockState().getBlock());
            if (boundEntity != null) return boundEntity.isAlive();
        }
        return true;
    }

    // Player's GUI is closed
    @Override
    public void removed(Player playerIn) {
        super.removed(playerIn);
        if (!(playerIn instanceof ServerPlayer)) return;
        for (int i = 0; i < internal.getSlots(); ++i) {
            if (i == 2) continue; // Don't drop result slot
            ItemStack stack = internal.extractItem(i, internal.getStackInSlot(i).getCount(), false);
            if (!stack.isEmpty())
                playerIn.getInventory().placeItemBackInInventory(stack);
        }
    }

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must be the number of slots you have!

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;

        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        boolean moved;
        if (index < TE_INVENTORY_FIRST_SLOT_INDEX) {
            moved = moveItemStackTo(sourceStack,
                    TE_INVENTORY_FIRST_SLOT_INDEX,
                    TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT,
                    false); // Player inventory -> Block inventory
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            moved = moveItemStackTo(sourceStack,
                    VANILLA_FIRST_SLOT_INDEX,
                    VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT,
                    false); // Block inventory -> Player inventory
        } else {
            System.out.println("Invalid slotIndex:" + index); // Invalid index
            return ItemStack.EMPTY;
        }

        if (!moved) return ItemStack.EMPTY;

        // Upgrades the slot
        if (sourceStack.isEmpty()) { sourceSlot.set(ItemStack.EMPTY); }
        else { sourceSlot.setChanged(); }

        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    // When player clicked on OUTPUT slot call slotChanged custom method
    @Override
    public void clicked(int slotId, int dragType, ClickType clickType, Player player) {
        super.clicked(slotId, dragType, clickType, player);
        if (player.level().isClientSide()) { slotChanged(slotId, dragType, clickType); }
    }

    // Active if player CLICKED on item -> Send to network packet event message on DisenchantedGuiSlotMessage custom class
    private void slotChanged(int slotId, int dragType, ClickType clickType) {
        if (boundBlockEntity != null && this.world != null && world.isClientSide()) {
            BlockPos pos = boundBlockEntity.getBlockPos();
            MCCourseMod.PACKET_HANDLER.sendToServer(new DisenchantedGuiSlotMessage(slotId, pos.getX(), pos.getY(), pos.getZ(), dragType, clickType));
        }
    }

    // Active after checked function to generate output slot contained an ENCHANTED BOOK with all enchantments
    public static void handleSlotAction(Player entity, int slot, int dragType, ClickType clickType, int x, int y, int z) {
        Level world = entity.level();
        if (!world.isLoaded(new BlockPos(x, y, z))) return;

        // Slot 2 clicked with PICKUP or QUICK MOVE types and clicked with left click
        if (slot == 2 && clickType == ClickType.PICKUP || clickType == ClickType.QUICK_MOVE) { execute(world, x, y, z); }
    }

    public Map<Integer, Slot> get() { return customSlots; } // Return slots (0, 1, 2) -> 0 + 1 [Input Slot] = 2 [Output Slot]

    // When player clicked on slot 2 (Output slot) generates Enchanted book with all enchantments of item disenchanted
    public static void execute(Level world, int x, int y, int z) {
        if (world.isClientSide()) return; // Ensures that it only runs on the server

        BlockEntity be = world.getBlockEntity(new BlockPos(x, y, z));
        if (!(be instanceof DisenchantedBlockEntity blockEntity)) return;

        ItemStack inputItem = blockEntity.getItem(0);
        ItemStack inputBook = blockEntity.getItem(1);

        // Disenchanted only if slot 0 is enchanted and has a book on slot 1
        if (!inputItem.isEmpty() && inputItem.isEnchanted() && !inputBook.isEmpty() && inputBook.getItem() == Items.BOOK) {

            // Created an enchanted book with enchantments of item
            ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(inputItem);
            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                EnchantedBookItem.addEnchantment(enchantedBook, new EnchantmentInstance(entry.getKey(), entry.getValue()));
            }

            // Removed enchantments of original item
            if (inputItem.getTag() != null && inputItem.getTag().contains("Enchantments")) {
                inputItem.getTag().remove("Enchantments");
                if (inputItem.getTag().isEmpty()) { inputItem.setTag(null); }
            }

            // Updated slots
            blockEntity.setItem(0, inputItem.copy()); // Item without enchantments
            blockEntity.setItem(1, ItemStack.EMPTY);  // Book is consumed
            blockEntity.setItem(2, enchantedBook);    // Put the result

            blockEntity.setChanged();
            world.sendBlockUpdated(blockEntity.getBlockPos(), blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
        }
    }
}