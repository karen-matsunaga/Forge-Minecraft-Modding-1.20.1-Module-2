package net.karen.mccourse.compat;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.recipe.*;
import net.karen.mccourse.screen.*;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import org.jetbrains.annotations.NotNull;
import java.util.List;

@JeiPlugin
public class JEIMCCoursePlugin implements IModPlugin {
    @Override
    public @NotNull ResourceLocation getPluginUid() { return new ResourceLocation(MCCourseMod.MOD_ID, "jei_plugin"); }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        // Register all custom categories
        registration.addRecipeCategories(new GemEmpoweringRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new KaupenFurnaceRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new CraftCraftingTableRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            RecipeManager recipeManager = mc.level.getRecipeManager();
            // Register all custom recipes
            List<GemEmpoweringRecipe> empoweringRecipes = recipeManager.getAllRecipesFor(GemEmpoweringRecipe.Type.INSTANCE);
            registration.addRecipes(GemEmpoweringRecipeCategory.GEM_EMPOWERING_TYPE, empoweringRecipes);

            List<KaupenFurnaceRecipe> kaupenFurnaceRecipes = recipeManager.getAllRecipesFor(KaupenFurnaceRecipe.Type.INSTANCE);
            registration.addRecipes(KaupenFurnaceRecipeCategory.KAUPEN_FURNACE_TYPE, kaupenFurnaceRecipes);

            List<CraftingRecipe> craftCraftingTableRecipes = recipeManager.getAllRecipesFor(RecipeType.CRAFTING);
            registration.addRecipes(CraftCraftingTableRecipeCategory.CRAFT_CRAFTING_TABLE_TYPE, craftCraftingTableRecipes);
        }
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

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        // Register all custom recipe catalysts
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.CRAFT_CRAFTING_TABLE.get()),
                CraftCraftingTableRecipeCategory.CRAFT_CRAFTING_TABLE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.KAUPEN_FURNACE_BLOCK.get()),
                KaupenFurnaceRecipeCategory.KAUPEN_FURNACE_TYPE);

        registration.addRecipeCatalyst(new ItemStack(ModBlocks.GEM_EMPOWERING_STATION.get()),
                GemEmpoweringRecipeCategory.GEM_EMPOWERING_TYPE);
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        // Register all custom recipe transfer handlers
        // [CUSTOM ITEM] Input Start [i] - Input End [i1]
        // Player's Inventory Start [i2] (9 hotbar + 27 inventory slots) - Inventory End [i3]
        registration.addRecipeTransferHandler(CraftCraftingTableMenu.class, // Menu Class, Menu Type and Recipe Type
        ModMenuTypes.CRAFT_CRAFTING_TABLE_MENU.get(), CraftCraftingTableRecipeCategory.CRAFT_CRAFTING_TABLE_TYPE,
        1, 49, 50, 36);

        registration.addRecipeTransferHandler(KaupenFurnaceMenu.class, // Menu Class, Menu Type and Recipe Type
        ModMenuTypes.KAUPEN_FURNACE_MENU.get(), KaupenFurnaceRecipeCategory.KAUPEN_FURNACE_TYPE, 0, 1, 3, 36);

        // GEM EMPOWERING STATION
//        registration.addRecipeTransferHandler(GemEmpoweringStationMenu.class,
//                ModMenuTypes.GEM_EMPOWERING_MENU.get(), GemEmpoweringRecipeCategory.GEM_EMPOWERING_TYPE, 0, 3, 4, 36);
    }
}