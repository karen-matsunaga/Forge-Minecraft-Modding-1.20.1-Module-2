package net.karen.mccourse.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.*;
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

    private final IDrawable background, icon;
    private final IDrawableAnimated arrow, flame;

    public KaupenFurnaceRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.KAUPEN_FURNACE_BLOCK.get()));
        // flame and arrow area texture (x, y, width, height)
        this.flame = drawAnimated(helper, 0, 14, 14, 300, IDrawableAnimated.StartDirection.TOP, true);
        this.arrow = drawAnimated(helper, 14, 24, 17, 200, IDrawableAnimated.StartDirection.LEFT, false);
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
            showInfo(fontRenderer, guiGraphics, "gui.jei.category.smelting.time.seconds", seconds, 60); // Time
            showInfo(fontRenderer, guiGraphics, "gui.jei.category.smelting.experience", experience, 20); // Experience
        }
        flame.draw(guiGraphics, 56, 36); // flame animated - x = 56, y = 36
        arrow.draw(guiGraphics, 79, 34); // arrow animated - x = 79, y = 34
    }

    // CUSTOM METHOD - Show info of cooking time, experience, etc. for each Kaupen Furnace recipe
    private void showInfo(Font font, GuiGraphics gui, String key, float value, int y) {
        Component info = Component.translatable(key, value);
        gui.drawString(font, info, 110, y, 0xFF808080, false);
    }

    private IDrawableAnimated drawAnimated(IGuiHelper helper, int v, int width, int height, int i,
                                           IDrawableAnimated.StartDirection direction, boolean has) {
        return helper.createAnimatedDrawable(helper.createDrawable(TEXTURE, 176, v, width, height), i, direction, has);
    }
}