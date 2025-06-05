package net.karen.mccourse.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.*;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.entity.KaupenFurnaceBlockEntity;
import net.karen.mccourse.recipe.KaupenFurnaceRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class KaupenFurnaceRecipeCategory implements IRecipeCategory<KaupenFurnaceRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MCCourseMod.MOD_ID, "kaupen_furnace");
    public static final ResourceLocation TEXTURE = new ResourceLocation(MCCourseMod.MOD_ID,
            "textures/gui/kaupen_furnace.png");

    public static final RecipeType<KaupenFurnaceRecipe> KAUPEN_FURNACE_TYPE =
            new RecipeType<>(UID, KaupenFurnaceRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public KaupenFurnaceRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.KAUPEN_FURNACE_BLOCK.get()));
    }

    @Override
    public @NotNull RecipeType<KaupenFurnaceRecipe> getRecipeType() { return KAUPEN_FURNACE_TYPE; }

    @Override
    public @NotNull Component getTitle() { return Component.translatable("block.mccourse.kaupen_furnace"); }

    @Override
    public @NotNull IDrawable getBackground() { return background; }

    @Override
    public IDrawable getIcon() { return icon; }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, KaupenFurnaceRecipe recipe, @NotNull IFocusGroup focuses) {
        // Input 0 -> Item | Input 1 - Fuel item | Output 2 -> Result item
        builder.addSlot(RecipeIngredientRole.INPUT, 56, 17).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 56, 53).addItemStacks(KaupenFurnaceBlockEntity.getValidFuels());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 116, 35).addItemStack(recipe.getResultItem(null));
    }

    @Override
    public void draw(@NotNull KaupenFurnaceRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView,
                     @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        float seconds = recipe.getCookingTime(); // Cooking time
        float experience = recipe.getExperience(); // Experience
        if (seconds > 0 || experience > 0) {
            showInfo(fontRenderer, guiGraphics, "gui.jei.category.smelting.time.seconds", seconds, 110,60); // Time
            showInfo(fontRenderer, guiGraphics, "gui.jei.category.smelting.experience", experience, 110, 20); // Experience
        }
    }

    // CUSTOM METHOD - Show info of cooking time, experience, etc. for each Kaupen Furnace recipe
    private void showInfo(Font font, GuiGraphics gui, String key, float value, int x, int y) {
        Component info = Component.translatable(key, value);
        gui.drawString(font, info, x, y, 0xFF808080, false);
    }
}