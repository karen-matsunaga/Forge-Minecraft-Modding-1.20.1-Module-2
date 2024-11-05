package net.karen.mccourse.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.karen.mccourse.MCCourseMod;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

public class GemEmpoweringRecipe implements Recipe<SimpleContainer> {
    // Adding specify items on Gem Empowering Station
    private final NonNullList<Ingredient> inputItems;
    private final ItemStack output;
    private final ResourceLocation id;
    private final int craftTime;
    private final int energyAmount;
    private final FluidStack fluidStack;

    public GemEmpoweringRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> inputItems,
                               int craftTime, int energyAmount, FluidStack fluidStack) {
        this.inputItems = inputItems;
        this.output = output;
        this.id = id;
        this.craftTime = craftTime;
        this.energyAmount = energyAmount;
        this.fluidStack = fluidStack;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) { return false; }
        return inputItems.get(0).test(pContainer.getItem(0)); // Verify if the item is equals on input slot
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) { return output.copy(); }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) { return true; }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) { return output.copy(); }

    @Override
    public NonNullList<Ingredient> getIngredients() { return this.inputItems; }

    public int getCraftTime() { return craftTime; } // Craft Time

    public int getEnergyAmount() { return energyAmount; } // Energy Amount

    public FluidStack getFluidStack() { return fluidStack; } // Fluid Stack

    @Override
    public ResourceLocation getId() { return id; }

    @Override
    public RecipeSerializer<?> getSerializer() { return null; }

    @Override
    public RecipeType<?> getType() { return Type.INSTANCE; }

    public static class Type implements RecipeType<GemEmpoweringRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "gem_empowering";
    }

    // Create JSON custom recipe files
    public static class Serializer implements RecipeSerializer<GemEmpoweringRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(MCCourseMod.MOD_ID,"gem_empowering");

        @Override
        public GemEmpoweringRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output")); // Crafting type
            FluidStack fluidStack = new FluidStack(ForgeRegistries.FLUIDS.getValue(new ResourceLocation(json.get("fluidType").getAsString())),
                    json.get("fluidAmount").getAsInt()); // Read Fluid Stack on JSON file

            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY); // Input ingredient

            for (int i = 0; i < inputs.size(); i++) { inputs.set(i, Ingredient.fromJson(ingredients.get(i))); }

            int craftTime = json.get("craftTime").getAsInt(); // Read Craft Time on JSON file
            int energyAmount = json.get("energyAmount").getAsInt(); // Read Energy Amount on JSON file
            return new GemEmpoweringRecipe(id, output, inputs, craftTime, energyAmount, fluidStack);
        }

        // Read one ingredient and one item stack on each JSON file
        // CLIENT and SERVER is always connected
        @Override
        public GemEmpoweringRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            FluidStack fluidStack = buf.readFluidStack(); // Read Fluid Stack from Network

            for (int i = 0; i < inputs.size(); i++) { inputs.set(i, Ingredient.fromNetwork(buf)); }

            int craftTime = buf.readInt(); // Read Craft Time from Network
            int energyAmount = buf.readInt(); // Read Energy Amount from Network
            ItemStack output = buf.readItem(); // Read Output from Network
            return new GemEmpoweringRecipe(id, output, inputs, craftTime, energyAmount, fluidStack);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, GemEmpoweringRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            buf.writeFluidStack(recipe.fluidStack); // Read Fluid Stack to Network = Read integer

            for (Ingredient ing : recipe.getIngredients()) { ing.toNetwork(buf); }

            buf.writeInt(recipe.craftTime); // Read Craft Time to Network = Read integer
            buf.writeInt(recipe.energyAmount); // Read Energy Amount to Network = Read integer
            buf.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}