package net.karen.mccourse.compat;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.recipe.GemEmpoweringRecipe;
import net.karen.mccourse.screen.renderer.EnergyDisplayTooltipArea;
import net.karen.mccourse.util.ModEnergyStorage;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class GemEmpoweringRecipeCategory implements IRecipeCategory<GemEmpoweringRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(MCCourseMod.MOD_ID, "gem_empowering");
    public static final ResourceLocation TEXTURE = new ResourceLocation(MCCourseMod.MOD_ID,
            "textures/gui/gem_empowering_station_gui.png");

    public static final RecipeType<GemEmpoweringRecipe> GEM_EMPOWERING_TYPE =
            new RecipeType<>(UID, GemEmpoweringRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public GemEmpoweringRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 0, 0, 176, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.GEM_EMPOWERING_STATION.get()));
    }

    @Override
    public @NotNull RecipeType<GemEmpoweringRecipe> getRecipeType() { return GEM_EMPOWERING_TYPE; }

    @Override
    public @NotNull Component getTitle() { return Component.literal("Gem Infusing Station"); } // Title appears on screen

    @Override
    public @NotNull IDrawable getBackground() { return this.background; } // Background appears on screen

    @Override
    public IDrawable getIcon() { return this.icon; } // Box slot appears on screen

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GemEmpoweringRecipe recipe, @NotNull IFocusGroup focuses) {
        // Recipe INPUT slot on screen
        builder.addSlot(RecipeIngredientRole.INPUT, 80, 11).addIngredients(recipe.getIngredients().get(0));

        // Recipe FLUID renderer slot on screen
        builder.addSlot(RecipeIngredientRole.INPUT, 26, 11)
                .addIngredients(ForgeTypes.FLUID_STACK,
                        List.of(new FluidStack(recipe.getFluidStack(), recipe.getFluidStack().getAmount())))
                        .setFluidRenderer(64000, true, 16, 39);

        builder.addSlot(RecipeIngredientRole.INPUT, 26, 59)
                .addItemStack(new ItemStack(recipe.getFluidStack().getFluid().getBucket()));

        // Recipe ENERGY renderer slot on screen
        builder.addSlot(RecipeIngredientRole.INPUT, 134, 59).addItemStack(new ItemStack(ModItems.KOHLRABI.get()));

        // Recipe OUTPUT slot on screen
        builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 59).addItemStack(recipe.getResultItem(null));
    }

    // Energy Renderer on screen
    @Override
    public void draw(@NotNull GemEmpoweringRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView,
                     @NotNull GuiGraphics guiGraphics, double mouseX, double mouseY) {
        ModEnergyStorage storage = energyStorage(recipe);
        EnergyDisplayTooltipArea energy = energyTooltip(storage);
        energy.render(guiGraphics); // Draws the power bar
    }

    // Energy Renderer on screen
    @Override
    public @NotNull List<Component> getTooltipStrings(@NotNull GemEmpoweringRecipe recipe,
                                                      @NotNull IRecipeSlotsView recipeSlotsView,
                                                      double mouseX, double mouseY) {
        int x = 156, y = 11, width = 8, height = 64;
        if (mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height) {
            ModEnergyStorage storage = energyStorage(recipe);
            return energyTooltip(storage).getTooltips();
        }

        return List.of(); // No tooltip outside the bar
    }

    private ModEnergyStorage energyStorage(GemEmpoweringRecipe recipe) {
        IEnergyStorage iEnergy = new ModEnergyStorage(64000, 64000) {
            @Override
            public void onEnergyChanged() {}
        };
        ModEnergyStorage energy = new ModEnergyStorage(iEnergy.getMaxEnergyStored(), iEnergy.getEnergyStored()) {
            @Override
            public void onEnergyChanged() {}
        };
        energy.setEnergy(recipe.getEnergyAmount());
        return energy;
    }

    private EnergyDisplayTooltipArea energyTooltip(ModEnergyStorage stored) {
        return new EnergyDisplayTooltipArea(156, 11, stored);
    }
}