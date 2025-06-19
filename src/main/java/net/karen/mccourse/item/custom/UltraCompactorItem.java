package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
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

public class UltraCompactorItem extends Item {
    public UltraCompactorItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide()) { return InteractionResultHolder.pass(player.getItemInHand(hand)); }
        RecipeManager recipeManager = level.getRecipeManager();
        int totalCrafted = 0;
        // Check crafting recipes (2x2 or 3x3)
        for (Recipe<?> recipe : recipeManager.getRecipes()) {
            if (!(recipe instanceof CraftingRecipe)) { continue; }
            ItemStack output = recipe.getResultItem(level.registryAccess());
            if (output.isEmpty() || !output.is(ModTags.Items.ULTRA_COMPACTOR_RESULT)) { continue; } // Block OUTPUT
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
            if (!base.is(ModTags.Items.ULTRA_COMPACTOR_ITEMS)) { continue; } // Item INPUT
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
        if (totalCrafted > 0) { // Display on screen transformed items to blocks
            player.displayClientMessage(Component.literal("§aCompacted " + totalCrafted + " blocks!"), true);
        }
        else { player.displayClientMessage(Component.literal("§cNothing to compress."), true); }
        return InteractionResultHolder.success(player.getItemInHand(hand));
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
        for (ItemStack stack : inv) {
            if (ItemStack.isSameItemSameTags(stack, target)) {
                int removed = Math.min(stack.getCount(), amountToRemove);
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
                                List<Component> components, @NotNull TooltipFlag flag) {
        components.add(Component.literal("Transform all vanilla gems, raw's, ingots and mobs drops on blocks!")
                .withStyle(ChatFormatting.GOLD));
        components.add(Component.literal("Compact type: 3x3 crafting recipes.").withStyle(ChatFormatting.DARK_AQUA));
    }
}