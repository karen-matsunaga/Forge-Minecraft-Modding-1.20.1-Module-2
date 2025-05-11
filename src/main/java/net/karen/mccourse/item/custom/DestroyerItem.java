package net.karen.mccourse.item.custom;

import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DestroyerItem extends Item {
    public DestroyerItem(Properties pProperties) { super(pProperties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        // Player has Destroyer item on Main hand
        if (level.isClientSide()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        // Block below the player
        BlockState state = level.getBlockState(player.blockPosition().below());

        // Items received according to the block clicked
        ItemStack targetItem = new ItemStack(state.getBlock().asItem());

        // All recipes from Crafting Recipe
        List<Recipe<?>> matchingRecipes = level.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getResultItem(level.registryAccess()).getItem()
                        == targetItem.getItem()).toList();

        // If not has recipe display on screen message
        if (matchingRecipes.isEmpty()) {
            player.displayClientMessage(Component.literal("No recipes found for this item."), true);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        // For each recipe found
        for (Recipe<?> recipe : matchingRecipes) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();

            // Drop all input ingredients also variants to Player's inventory
            for (Ingredient ingredient : ingredients) {
                for (ItemStack possibleMatch : ingredient.getItems()) {
                    ItemStack stackToGive = possibleMatch.copy();
                    player.getInventory().add(stackToGive);
                }
            }
        }

        // Display on screen message when has recipe and Player received items on inventory
        player.displayClientMessage(Component.literal("Ingredients returned from " +
                matchingRecipes.size() + " recipes."), true);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}