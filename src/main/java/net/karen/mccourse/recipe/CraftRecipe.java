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

import java.util.ArrayList;
import java.util.List;

public class CraftRecipe implements Recipe<SimpleContainer> {
    private final NonNullList<Ingredient> inputItems; // Ingredients = Slots
    private final List<Integer> inputCounts; // Ingredients counts
    private final ItemStack output; // Output
    private final ResourceLocation id; // Item id

    public CraftRecipe(ResourceLocation id, ItemStack output, NonNullList<Ingredient> inputItems, List<Integer> inputCounts) {
        this.inputItems = inputItems;
        this.inputCounts = inputCounts;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(SimpleContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) { return false; }
        return inputItems.get(0).test(pContainer.getItem(0)); // Verify if the item is equals on input slot
    }

    @Override
    public ItemStack assemble(SimpleContainer pContainer, RegistryAccess pRegistryAccess) { return output.copy(); }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) { return true; } // Shaped

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) { return output.copy(); } // Output

    @Override
    public NonNullList<Ingredient> getIngredients() { return this.inputItems; } // Ingredients

    public List<Integer> getIngredientsCounts() { return this.inputCounts; } // Ingredients counts

    @Override
    public ResourceLocation getId() { return id; } // Item id

    @Override
    public RecipeSerializer<?> getSerializer() { return null; }

    @Override
    public RecipeType<?> getType() { return Type.INSTANCE; }

    public static class Type implements RecipeType<CraftRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "craft";
    }

    // Create JSON custom recipe files
    public static class Serializer implements RecipeSerializer<CraftRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(MCCourseMod.MOD_ID,"craft");

        @Override
        public CraftRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output")); // Output item

            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY); // Input ingredient
            List<Integer> inputsCounts = new ArrayList<>(2); // Read Ingredient counts on JSON file
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");

            // Added each ingredient with your count
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i))); // Ingredient
                inputsCounts.add(GsonHelper.getAsInt(ingredients.get(i).getAsJsonObject(), "count", 1)); // Ingredient count
            }

            return new CraftRecipe(id, output, inputs, inputsCounts);
        }

        // Read one ingredient and one item stack on each JSON file
        // CLIENT and SERVER is always connected
        @Override
        public CraftRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY); // Read Ingredients from Network
            List<Integer> inputsCounts = new ArrayList<>(buf.readInt()); // Read Ingredient counts from Network

            // Added each ingredient with your count
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf)); // Ingredients
                inputsCounts.add(buf.readInt()); // Ingredient counts
            }

            ItemStack output = buf.readItem(); // Read Output from Network
            return new CraftRecipe(id, output, inputs, inputsCounts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, CraftRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size()); // Write Ingredients to Network = Read integer

            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                recipe.getIngredients().get(i).toNetwork(buf);
                buf.writeInt(i < recipe.getIngredientsCounts().size() ? recipe.getIngredientsCounts().get(i) : 1);
            }

            buf.writeItemStack(recipe.getResultItem(null), false); // Output
        }
    }
}