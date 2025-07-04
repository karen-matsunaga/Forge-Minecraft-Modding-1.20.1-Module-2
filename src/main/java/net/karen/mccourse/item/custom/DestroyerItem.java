package net.karen.mccourse.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class DestroyerItem extends Item {
    public DestroyerItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand); // Player has Destroyer item on Main hand
        if (!level.isClientSide()) {
            BlockState state = level.getBlockState(player.blockPosition().below()); // Block below the player
            ItemStack targetItem = new ItemStack(state.getBlock().asItem()); // Items received according to the block clicked
            List<Recipe<?>> matchingRecipes = level.getRecipeManager().getRecipes().stream() // All recipes from Crafting Recipe
                            .filter(recipe -> recipe.getResultItem(level.registryAccess()).getItem() == targetItem.getItem()).toList();
            // If not has recipe display on screen message
            if (matchingRecipes.isEmpty()) { return messages(player, "No recipes found for this item.", red, item); }
            for (Recipe<?> recipe : matchingRecipes) { // For each recipe found
                NonNullList<Ingredient> ingredients = recipe.getIngredients();
                for (Ingredient ingredient : ingredients) {
                    for (ItemStack possibleMatch : ingredient.getItems()) {
                        ItemStack stackToGive = possibleMatch.copy();
                        player.getInventory().add(stackToGive); // Drop all input ingredients also variants to Player's inventory
                    }
                }
            }
            // Display on screen message when has recipe and Player received items on inventory
            return messages(player, "Ingredients returned from " + matchingRecipes.size() + " recipes.", green, item);
        }
        return InteractionResultHolder.pass(item);
    }

    // CUSTOM METHOD - Success messages
    private InteractionResultHolder<ItemStack> messages(Player player, String message,
                                                        ChatFormatting color, ItemStack item) {
        player(player, message, color);
        return InteractionResultHolder.success(item);
    }
}