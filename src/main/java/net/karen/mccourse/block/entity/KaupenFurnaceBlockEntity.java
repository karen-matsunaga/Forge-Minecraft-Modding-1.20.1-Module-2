package net.karen.mccourse.block.entity;

import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.recipe.KaupenFurnaceRecipe;
import net.karen.mccourse.screen.KaupenFurnaceMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class KaupenFurnaceBlockEntity extends AbstractFurnaceBlockEntity {
    // List of items that burned on Kaupen custom furnace using ticks
    private static final Map<Item, Integer> BURN_DURATION_MAP = Map.of(ModItems.PEAT_BRICK.get(), 100,
            ModItems.KOHLRABI.get(), 200, Items.BLAZE_POWDER, 800);

    public KaupenFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.KAUPEN_FURNACE_BLOCK_ENTITY.get(), pos, state, KaupenFurnaceRecipe.Type.INSTANCE);
    }

    @Override
    protected @NotNull Component getDefaultName() { return Component.translatable("block.mccourse.kaupen_furnace"); }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int container, @NotNull Inventory inventory) {
        return new KaupenFurnaceMenu(container, inventory, this, dataAccess);
    }

    @Override
    protected int getBurnDuration(ItemStack stack) { return BURN_DURATION_MAP.getOrDefault(stack.getItem(), 0); }

    // CUSTOM METHOD - List of items that burned on Kaupen custom furnace
    public static List<ItemStack> getValidFuels() {
        List<ItemStack> fuels = new ArrayList<>();
        ForgeRegistries.ITEMS.forEach(item -> { if (BURN_DURATION_MAP.containsKey(item)) { fuels.add(new ItemStack(item)); } });
        return fuels;
    }
}