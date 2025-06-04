package net.karen.mccourse.potion;

import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.brewing.*;

public class ModPotionsRecipes {
    // CUSTOM METHOD - Added all CUSTOM POTIONS RECIPES
    public static void addRecipe(Potion type, ItemLike item, Potion result) {
        BrewingRecipeRegistry.addRecipe(new BrewingRecipe(potion(out(Items.POTION, type)), item(item), out(Items.POTION, result)));
    }

    // CUSTOM METHOD - Input -> POTION TYPE
    public static Ingredient potion(ItemStack item) { return Ingredient.of(item); }

    // CUSTOM METHOD - Input -> ITEM
    public static Ingredient item(ItemLike item) { return Ingredient.of(item); }

    // CUSTOM METHOD - Output -> MOD POTION
    public static ItemStack out(ItemLike item, Potion result) {
        return PotionUtils.setPotion(new ItemStack(item), result);
    }
}