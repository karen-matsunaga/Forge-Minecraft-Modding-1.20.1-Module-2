package net.karen.mccourse.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CraftCraftingTableRecipeCategory implements IRecipeCategory<CraftingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MCCourseMod.MOD_ID, "craft_crafting_table");
    public static final ResourceLocation TEXTURE = new ResourceLocation(MCCourseMod.MOD_ID,
            "textures/gui/craft_crafting_table_gui.png");

    public static final RecipeType<CraftingRecipe> CRAFT_CRAFTING_TABLE_TYPE =
            new RecipeType<>(UID, ShapedRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public CraftCraftingTableRecipeCategory(IGuiHelper helper) {
        // Background and Icon screen
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 133);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CRAFT_CRAFTING_TABLE.get()));
    }

    @Override
    public @NotNull RecipeType<CraftingRecipe> getRecipeType() { return CRAFT_CRAFTING_TABLE_TYPE; }

    @Override
    public @NotNull Component getTitle() { return Component.translatable("block.mccourse.craft_crafting_table"); }

    @Override
    public @NotNull IDrawable getBackground() { return background; }

    @Override
    public @Nullable IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(@NotNull IRecipeLayoutBuilder builder, CraftingRecipe recipe, @NotNull IFocusGroup focuses) {
        List<Ingredient> ingredients = recipe.getIngredients();
        for (int row = 0; row < 7; row++) {
            for (int col = 0; col < 7; col++) {
                int index = row * 7 + col;
                if (index < ingredients.size()) {
                    int x = 8 + col * 18;
                    int y = 6 + row * 18;
                    builder.addSlot(RecipeIngredientRole.INPUT, x, y).addIngredients(ingredients.get(index));
                }
            }
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 148, 35).addItemStack(recipe.getResultItem(null));
    }
}