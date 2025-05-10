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
import net.minecraft.world.item.crafting.ShapedRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    public void setRecipe(IRecipeLayoutBuilder builder, CraftingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 6).addIngredients(recipe.getIngredients().get(0));
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 6).addIngredients(recipe.getIngredients().get(1));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 6).addIngredients(recipe.getIngredients().get(2));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 6).addIngredients(recipe.getIngredients().get(3));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 6).addIngredients(recipe.getIngredients().get(4));
        builder.addSlot(RecipeIngredientRole.INPUT, 98, 6).addIngredients(recipe.getIngredients().get(5));
        builder.addSlot(RecipeIngredientRole.INPUT, 116, 6).addIngredients(recipe.getIngredients().get(6)); // 7x1

        builder.addSlot(RecipeIngredientRole.INPUT, 8, 24).addIngredients(recipe.getIngredients().get(7));
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 24).addIngredients(recipe.getIngredients().get(8));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 24).addIngredients(recipe.getIngredients().get(9));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 24).addIngredients(recipe.getIngredients().get(10));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 24).addIngredients(recipe.getIngredients().get(11));
        builder.addSlot(RecipeIngredientRole.INPUT, 98, 24).addIngredients(recipe.getIngredients().get(12));
        builder.addSlot(RecipeIngredientRole.INPUT, 116, 24).addIngredients(recipe.getIngredients().get(13)); // 7x2

        builder.addSlot(RecipeIngredientRole.INPUT, 8, 42).addIngredients(recipe.getIngredients().get(14));
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 42).addIngredients(recipe.getIngredients().get(15));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 42).addIngredients(recipe.getIngredients().get(16));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 42).addIngredients(recipe.getIngredients().get(17));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 42).addIngredients(recipe.getIngredients().get(18));
        builder.addSlot(RecipeIngredientRole.INPUT, 98, 42).addIngredients(recipe.getIngredients().get(19));
        builder.addSlot(RecipeIngredientRole.INPUT, 116, 42).addIngredients(recipe.getIngredients().get(20)); // 7x3

        builder.addSlot(RecipeIngredientRole.INPUT, 8, 60).addIngredients(recipe.getIngredients().get(21));
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 60).addIngredients(recipe.getIngredients().get(22));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 60).addIngredients(recipe.getIngredients().get(23));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 60).addIngredients(recipe.getIngredients().get(24));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 60).addIngredients(recipe.getIngredients().get(25));
        builder.addSlot(RecipeIngredientRole.INPUT, 98, 60).addIngredients(recipe.getIngredients().get(26));
        builder.addSlot(RecipeIngredientRole.INPUT, 116, 60).addIngredients(recipe.getIngredients().get(27)); // 7x4

        builder.addSlot(RecipeIngredientRole.INPUT, 8, 78).addIngredients(recipe.getIngredients().get(28));
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 78).addIngredients(recipe.getIngredients().get(29));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 78).addIngredients(recipe.getIngredients().get(30));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 78).addIngredients(recipe.getIngredients().get(31));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 78).addIngredients(recipe.getIngredients().get(32));
        builder.addSlot(RecipeIngredientRole.INPUT, 98, 78).addIngredients(recipe.getIngredients().get(33));
        builder.addSlot(RecipeIngredientRole.INPUT, 116, 78).addIngredients(recipe.getIngredients().get(34)); // 7x5

        builder.addSlot(RecipeIngredientRole.INPUT, 8, 96).addIngredients(recipe.getIngredients().get(35));
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 96).addIngredients(recipe.getIngredients().get(36));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 96).addIngredients(recipe.getIngredients().get(37));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 96).addIngredients(recipe.getIngredients().get(38));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 96).addIngredients(recipe.getIngredients().get(39));
        builder.addSlot(RecipeIngredientRole.INPUT, 98, 96).addIngredients(recipe.getIngredients().get(40));
        builder.addSlot(RecipeIngredientRole.INPUT, 116, 96).addIngredients(recipe.getIngredients().get(41)); // 7x6

        builder.addSlot(RecipeIngredientRole.INPUT, 8, 114).addIngredients(recipe.getIngredients().get(42));
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 114).addIngredients(recipe.getIngredients().get(43));
        builder.addSlot(RecipeIngredientRole.INPUT, 44, 114).addIngredients(recipe.getIngredients().get(44));
        builder.addSlot(RecipeIngredientRole.INPUT, 62, 114).addIngredients(recipe.getIngredients().get(45));
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 114).addIngredients(recipe.getIngredients().get(46));
        builder.addSlot(RecipeIngredientRole.INPUT, 98, 114).addIngredients(recipe.getIngredients().get(47));
        builder.addSlot(RecipeIngredientRole.INPUT, 116, 114).addIngredients(recipe.getIngredients().get(48)); // 7x7

        builder.addSlot(RecipeIngredientRole.OUTPUT, 148, 35).addItemStack(recipe.getResultItem(null));
    }
}