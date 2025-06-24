package net.karen.mccourse.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class UltraCompactorItem extends Item {
    private final boolean AUTOMATED; // Automated CRAFT items
    private final TagKey<Item> INPUT, OUTPUT; // INPUT and OUTPUT Crafting Recipe using ITEM TAGS

    public UltraCompactorItem(Properties properties, boolean automated,
                              TagKey<Item> input, TagKey<Item> output) {
        super(properties);
        this.AUTOMATED = automated;
        this.INPUT = input;
        this.OUTPUT = output;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide() && !this.AUTOMATED) {
            craftItem(level, player);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    // CUSTOM METHOD - Craft items
    private void craftItem(Level level, Player player) {
        RecipeManager recipeManager = level.getRecipeManager();
        int totalCrafted = 0;
        // Check crafting recipes 3x3
        for (Recipe<?> recipe : recipeManager.getRecipes()) {
            if (!(recipe instanceof CraftingRecipe)) { continue; }
            ItemStack output = recipe.getResultItem(level.registryAccess());
            if (output.isEmpty() || !output.is(this.OUTPUT)) { continue; } // Block OUTPUT
            NonNullList<Ingredient> ingredients = recipe.getIngredients();
            if (ingredients.isEmpty()) { continue; }
            // Check if all ingredients are equal and not empty
            ItemStack base = null;
            boolean valid = true;
            for (Ingredient ing : ingredients) {
                if (ing.isEmpty()) { continue; }
                ItemStack[] stacks = ing.getItems();
                if (stacks.length == 0) {
                    valid = false;
                    break;
                }
                if (base == null) { base = stacks[0]; }
                else {
                    boolean match = false;
                    for (ItemStack stack : stacks) {
                        if (ItemStack.isSameItemSameTags(stack, base)) {
                            match = true;
                            break;
                        }
                    }
                    if (!match) {
                        valid = false;
                        break;
                    }
                }
            }
            if (!valid || base == null) { continue; }
            if (!base.is(this.INPUT)) { continue; } // Item INPUT
            int countRequired = ingredients.size(), available = countItem(player, base);
            if (available >= countRequired) { // Has sufficient Item
                int maxCrafts = available / countRequired;
                removeItems(player, base, countRequired * maxCrafts);
                ItemStack result = output.copy();
                result.setCount(maxCrafts);
                player.getInventory().add(result);
                totalCrafted += maxCrafts;
            }
        }
        // Display on screen transformed items to blocks
        if (totalCrafted > 0) { normalMessage(player, "§aCompacted " + totalCrafted + " blocks!", green); }
        else { invalidMessage(player, "Nothing to compress."); }
    }

    // CUSTOM METHOD - Counts how many of the same items there are in the inventory
    private int countItem(Player player, ItemStack target) {
        int count = 0;
        for (ItemStack stack : player.getInventory().items) {
            if (ItemStack.isSameItemSameTags(stack, target)) { count += stack.getCount(); }
        }
        return count;
    }

    // CUSTOM METHOD - Removes a certain amount of an item from inventory
    private void removeItems(Player player, ItemStack target, int amountToRemove) {
        List<ItemStack> inv = player.getInventory().items;
        for (ItemStack stack : inv) { // Checks Player's inventory
            if (ItemStack.isSameItemSameTags(stack, target)) {
                int removed = Math.min(stack.getCount(), amountToRemove); // Is same item
                stack.shrink(removed);
                amountToRemove -= removed;
                if (amountToRemove <= 0) { return; }
            }
        }
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(stack.getDescriptionId()).withStyle(ChatFormatting.LIGHT_PURPLE);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltipLine(tooltip, "Transform all vanilla gems, raw's, ingots and mobs drops on blocks!", gold);
        tooltipLine(tooltip, "Compact type: 3x3 crafting recipes.", darkAqua);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity,
                              int slotId, boolean isSelected) {
        Player player = (Player) entity;
        // Automated craft
        if (this.AUTOMATED) { for (int i = 0; i < player.getInventory().getContainerSize(); i++) { craftItem(level, player); } }
    }
}