package net.karen.mccourse.screen;

import net.karen.mccourse.block.ModBlocks;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Optional;

public class CraftCraftingTableMenu extends RecipeBookMenu<CraftingContainer> {
    private final CraftingContainer craftSlots = new TransientCraftingContainer(this, 7, 7);
    private final ResultContainer resultSlots = new ResultContainer();
    private final ContainerLevelAccess access;
    private final Player player;

    public CraftCraftingTableMenu(int pContainerId, Inventory pPlayerInventory, FriendlyByteBuf friendlyByteBuf) {
        this(pContainerId, pPlayerInventory, ContainerLevelAccess.NULL);
    }

    public CraftCraftingTableMenu(int pContainerId, Inventory pPlayerInventory, ContainerLevelAccess pAccess) {
        super(ModMenuTypes.CRAFT_CRAFTING_TABLE_MENU.get(), pContainerId);
        this.access = pAccess;
        this.player = pPlayerInventory.player;
        this.addSlot(new ResultSlot(pPlayerInventory.player, this.craftSlots, this.resultSlots, 0, 148, 35));

        for(int i = 0; i < craftSlots.getHeight(); ++i) {
            for(int j = 0; j < craftSlots.getWidth(); ++j) {
                this.addSlot(new Slot(this.craftSlots, j + i * 7, 8 + j * 18, 6 + i * 18));
            }
        }

        for(int k = 0; k < 3; ++k) {
            for(int i1 = 0; i1 < 9; ++i1) {
                this.addSlot(new Slot(pPlayerInventory, i1 + k * 9 + 9, 8 + i1 * 18, 135 + k * 18));
            }
        }

        for(int l = 0; l < 9; ++l) {
            this.addSlot(new Slot(pPlayerInventory, l, 8 + l * 18, 193));
        }

    }

    protected static void slotChangedCraftingGrid(AbstractContainerMenu pMenu, Level pLevel, Player pPlayer, CraftingContainer pContainer, ResultContainer pResult) {
        if (!pLevel.isClientSide) {
            ServerPlayer serverplayer = (ServerPlayer)pPlayer;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<CraftingRecipe> optional = pLevel.getServer().getRecipeManager().getRecipeFor(RecipeType.CRAFTING, pContainer, pLevel);
            if (optional.isPresent()) {
                CraftingRecipe craftingrecipe = optional.get();
                if (pResult.setRecipeUsed(pLevel, serverplayer, craftingrecipe)) {
                    ItemStack itemstack1 = craftingrecipe.assemble(pContainer, pLevel.registryAccess());
                    if (itemstack1.isItemEnabled(pLevel.enabledFeatures())) {
                        itemstack = itemstack1;
                    }
                }
            }
            pResult.setItem(0, itemstack);
            pMenu.setRemoteSlot(0, itemstack);
            serverplayer.connection.send(new ClientboundContainerSetSlotPacket(pMenu.containerId, pMenu.incrementStateId(), 0, itemstack));
        }
    }

    public void slotsChanged(Container pInventory) {
        this.access.execute((p_39386_, p_39387_) -> {
            slotChangedCraftingGrid(this, p_39386_, this.player, this.craftSlots, this.resultSlots);
        });
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents pItemHelper) {
        this.craftSlots.fillStackedContents(pItemHelper);
    }

    @Override
    public void clearCraftingContent() {
        this.craftSlots.clearContent();
        this.resultSlots.clearContent();
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> pRecipe) {
        return pRecipe.matches(this.craftSlots, this.player.level());
    }

    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.access.execute((p_39371_, p_39372_) -> {
            this.clearContainer(pPlayer, this.craftSlots);
        });
    }

    @Override
    public int getResultSlotIndex() { return 0; }

    @Override
    public int getGridWidth() { return this.craftSlots.getWidth(); }

    @Override
    public int getGridHeight() { return this.craftSlots.getHeight(); }

    @Override
    public int getSize() { return this.craftSlots.getWidth() * this.craftSlots.getHeight() + 1; }

    @Override
    public RecipeBookType getRecipeBookType() { return RecipeBookType.CRAFTING; }

    @Override
    public boolean shouldMoveToInventory(int pSlotIndex) { return pSlotIndex != this.getResultSlotIndex(); }

    private static final int RESULT_SLOT = 0;
    private static final int CRAFTING_FIRST_SLOT = 1;
    private static final int CRAFTING_LAST_SLOT = 49; // inclusive
    private static final int PLAYER_INV_FIRST_SLOT = 50;
    private static final int PLAYER_INV_LAST_SLOT = 85; // inclusive

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (pIndex == RESULT_SLOT) {
                this.access.execute((level, pos) -> {
                    itemstack1.getItem().onCraftedBy(itemstack1, level, pPlayer);
                });
                // Resultado -> Inventário do jogador
                if (!this.moveItemStackTo(itemstack1, PLAYER_INV_FIRST_SLOT, PLAYER_INV_LAST_SLOT + 1, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(itemstack1, itemstack);
            } else if (pIndex >= PLAYER_INV_FIRST_SLOT && pIndex <= PLAYER_INV_LAST_SLOT) {
                // Inventário do jogador -> crafting grid
                if (!this.moveItemStackTo(itemstack1, CRAFTING_FIRST_SLOT, CRAFTING_LAST_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (pIndex >= CRAFTING_FIRST_SLOT && pIndex <= CRAFTING_LAST_SLOT) {
                // Crafting grid -> inventário do jogador
                if (!this.moveItemStackTo(itemstack1, PLAYER_INV_FIRST_SLOT, PLAYER_INV_LAST_SLOT + 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else {
                return ItemStack.EMPTY; // índice inválido
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, itemstack1);

            if (pIndex == RESULT_SLOT) {
                pPlayer.drop(itemstack1, false);
            }
        }

        return itemstack;
    }

    @Override
    public boolean stillValid(Player pPlayer) { return stillValid(this.access, pPlayer, ModBlocks.CRAFT_CRAFTING_TABLE.get()); }

    public boolean canTakeItemForPickAll(ItemStack pStack, Slot pSlot) {
        return pSlot.container != this.resultSlots && super.canTakeItemForPickAll(pStack, pSlot);
    }
}
