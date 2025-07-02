package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class RestoreItem extends Item {
    public RestoreItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level.isClientSide()) { return InteractionResultHolder.pass(player.getItemInHand(hand)); }
        // MAIN HAND -> Restore item || OFFHAND -> Target item (Block, item, tools or armors to UNCRAFT)
        InteractionHand offhand = InteractionHand.OFF_HAND, mainHand = InteractionHand.MAIN_HAND;
        ItemStack target = hasItem(player, (hand == mainHand) ? offhand : mainHand), restore = hasItem(player, mainHand);
        if (target.isEmpty() || target.is(ModTags.Items.RESTORE_BLACKLIST_ITEMS)) { // If OFFHAND empty
           return screen(player, "Hold the item you wish to uncraft in your other hand.", black, hand);
        }
        int itemsGiven = 0; // Items restored on Player's inventory
        if (target.isEnchanted()) { // If the item is enchanted
            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(target);
            ItemStack itemBook = new ItemStack(Items.ENCHANTED_BOOK); // Converts the enchantments to enchanted books
            enchantments.forEach((key, value) -> EnchantmentHelper.setEnchantments(Map.of(key, value), itemBook));
            // Returns the base item WITHOUT enchantment and an enchanted book WITH enchantment(s)
            if (player.getInventory().add(itemBook)) { itemsGiven++; }
        }

        // Filters recipes that create the same base item (ignores NBT)
        List<Recipe<?>> matchingRecipes = level.getRecipeManager().getRecipes().stream()
                        .filter(recipe -> recipe.getType() == RecipeType.CRAFTING) // Crafting recipe
                        .filter(recipe -> recipe.getResultItem(level.registryAccess()).getItem() == target.getItem()).toList();
        if (matchingRecipes.isEmpty()) { return screen(player, "No recipes found for this item.", darkRed, hand); }
        Recipe<?> recipe = matchingRecipes.get(0);
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (Ingredient ingredient : ingredients) { // First, return the ingredients
            if (ingredient.isEmpty()) { continue; }
            ItemStack[] possibleItems = ingredient.getItems();
            if (possibleItems.length > 0) {
                ItemStack stackToGive = possibleItems[0].copy();
                stackToGive.setCount(1);
                if (player.getInventory().add(stackToGive)) { itemsGiven++; }
            }
        }
        target.shrink(1); // Target item and Restore item are removed on Player's inventory
        restore.shrink(1);
        return screen(player, "Descraft accomplished! Items recovered: " + itemsGiven + "item(s)!", green, hand);
    }

    // CUSTOM METHOD - Message on SCREEN
    private InteractionResultHolder<ItemStack> screen(Player player, String message,
                                                      ChatFormatting color, InteractionHand hand) {
        player(player, message, color);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}