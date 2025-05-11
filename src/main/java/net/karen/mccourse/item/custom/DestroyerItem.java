package net.karen.mccourse.item.custom;

import net.minecraft.core.BlockPos;
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
        if (level.isClientSide()) {
            return InteractionResultHolder.pass(player.getItemInHand(hand));
        }

        BlockPos pos = player.blockPosition();
        BlockState state = level.getBlockState(pos.below()); // Block below the player
        ItemStack targetItem = new ItemStack(state.getBlock().asItem());

        List<Recipe<?>> matchingRecipes = level.getRecipeManager().getRecipes().stream()
                .filter(recipe -> recipe.getResultItem(level.registryAccess()).getItem()
                        == targetItem.getItem()).toList();

        if (matchingRecipes.isEmpty()) {
            player.displayClientMessage(Component.literal("No recipes found for this item."), true);
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }

        // For each recipe found
        for (Recipe<?> recipe : matchingRecipes) {
            NonNullList<Ingredient> ingredients = recipe.getIngredients();

            for (Ingredient ingredient : ingredients) {
                ItemStack[] matchingStacks = ingredient.getItems();

                if (matchingStacks.length > 0) {
                    ItemStack ingredientStack = matchingStacks[0].copy(); // Only the first matching item

                    // Give 1 of the item to the player (you can upgrade this to support larger quantities)
                    player.getInventory().add(ingredientStack);
                }
            }
        }

        player.displayClientMessage(Component.literal("Ingredients returned from " +
                matchingRecipes.size() + " recipes."), true);
        return InteractionResultHolder.success(player.getItemInHand(hand));
    }
}