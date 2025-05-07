package net.karen.mccourse.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.recipe.GemEmpoweringRecipe;
import net.karen.mccourse.recipe.KaupenFurnaceRecipe;
import net.karen.mccourse.screen.CraftCraftingTableScreen;
import net.karen.mccourse.screen.GemEmpoweringStationScreen;
import net.karen.mccourse.screen.KaupenFurnaceScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

@JeiPlugin
public class JEIMCCoursePlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() { return new ResourceLocation(MCCourseMod.MOD_ID, "jei_plugin"); }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        // Register all custom categories
        registration.addRecipeCategories(new GemEmpoweringRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new KaupenFurnaceRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));

        registration.addRecipeCategories(new CraftCraftingTableRecipeCategory(
                registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().level).getRecipeManager();

        // Register all custom recipes
        List<GemEmpoweringRecipe> empoweringRecipes = recipeManager.getAllRecipesFor(GemEmpoweringRecipe.Type.INSTANCE);
        registration.addRecipes(GemEmpoweringRecipeCategory.GEM_EMPOWERING_TYPE, empoweringRecipes);

        List<KaupenFurnaceRecipe> kaupenFurnaceRecipes = recipeManager.getAllRecipesFor(KaupenFurnaceRecipe.Type.INSTANCE);
        registration.addRecipes(KaupenFurnaceRecipeCategory.KAUPEN_FURNACE_TYPE, kaupenFurnaceRecipes);

        List<CraftingRecipe> craftCraftingTableRecipes = recipeManager.getAllRecipesFor(RecipeType.CRAFTING);
        registration.addRecipes(CraftCraftingTableRecipeCategory.CRAFT_CRAFTING_TABLE_TYPE, craftCraftingTableRecipes);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        // Register all custom gui handlers
        registration.addRecipeClickArea(GemEmpoweringStationScreen.class, 60, 30, 20, 30,
                GemEmpoweringRecipeCategory.GEM_EMPOWERING_TYPE);

        registration.addRecipeClickArea(KaupenFurnaceScreen.class, 60, 30, 20, 30,
                KaupenFurnaceRecipeCategory.KAUPEN_FURNACE_TYPE);

        registration.addRecipeClickArea(CraftCraftingTableScreen.class, 140, 18, 18, 18,
                CraftCraftingTableRecipeCategory.CRAFT_CRAFTING_TABLE_TYPE);
    }
}