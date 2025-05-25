package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ModTags;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class RestoreItem extends Item {
    public RestoreItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide()) { return InteractionResultHolder.pass(player.getItemInHand(hand)); }

        // Main hand -> Restore item || Offhand -> Target item (Block, item, tools or armors to uncraft)
        ItemStack targetItem = player.getItemInHand((hand == InteractionHand.MAIN_HAND) ?
                InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
        ItemStack restoreItem = player.getItemInHand(InteractionHand.MAIN_HAND);

        // If Offhand empty
        if (targetItem.isEmpty() || targetItem.is(ModTags.Items.RESTORE_BLACKLIST_ITEMS)) {
            message(player, "Hold the item you wish to uncraft in your other hand.");
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        // Items restored on Player's inventory
        int itemsGiven = 0;

        // If the item is enchanted, converts the enchantments to enchanted books and returns the
        // base item WITHOUT enchantment
        if (targetItem.isEnchanted()) {
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(targetItem);
            ItemStack toolsArmorsBook = new ItemStack(Items.ENCHANTED_BOOK); // Tools or Armors
            enchantments.forEach((key, value) -> EnchantmentHelper.setEnchantments(Map.of(key, value), toolsArmorsBook));
            if (player.getInventory().add(toolsArmorsBook)) { itemsGiven++; }
        }

        // Filters recipes that create the same base item (ignores NBT)
        List<Recipe<?>> matchingRecipes = level.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getType() == RecipeType.CRAFTING)
                .filter(recipe -> recipe.getResultItem(level.registryAccess()).getItem() == targetItem.getItem()).toList();

        if (matchingRecipes.isEmpty()) {
            message(player, "No recipes found for this item.");
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        Recipe<?> recipe = matchingRecipes.get(0);
        NonNullList<Ingredient> ingredients = recipe.getIngredients();

        // First, return the ingredients
        for (Ingredient ingredient : ingredients) {
            if (ingredient.isEmpty()) { continue; }
            ItemStack[] possibleItems = ingredient.getItems();
            if (possibleItems.length > 0) {
                ItemStack stackToGive = possibleItems[0].copy();
                stackToGive.setCount(1);
                if (player.getInventory().add(stackToGive)) { itemsGiven++; }
            }
        }

        // Target item and Restore item are removed on Player's inventory
        targetItem.shrink(1);
        restoreItem.shrink(1);
        message(player, "Descraft accomplished! Items recovered: " + itemsGiven);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }

    // Custom method - Message on SCREEN
    private void message(Player player, String message) {
        player.displayClientMessage(Component.literal(message), true);
    }
}