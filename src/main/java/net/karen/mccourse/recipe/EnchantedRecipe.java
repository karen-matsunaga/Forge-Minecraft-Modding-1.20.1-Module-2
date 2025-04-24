package net.karen.mccourse.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.karen.mccourse.MCCourseMod;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class EnchantedRecipe implements Recipe<CraftingContainer> {
    final NonNullList<Ingredient> inputSlot;
    final ItemStack outputSlot;
    private final ResourceLocation id;

    public EnchantedRecipe(ResourceLocation id, ItemStack outputSlot, NonNullList<Ingredient> inputSlot) {
        this.inputSlot = inputSlot;
        this.outputSlot = outputSlot;
        this.id = id;
    }

    @Override
    public boolean matches(CraftingContainer pContainer, Level pLevel) {
        if (pLevel.isClientSide()) { return false; }
        return inputSlot.get(0).test(pContainer.getItem(0)); // Verify if the item is equals on input slot
    }

    @Override
    public ItemStack assemble(CraftingContainer pContainer, RegistryAccess pRegistryAccess) { return outputSlot.copy(); }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) { return true; }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) { return outputSlot.copy(); }

    @Override
    public NonNullList<Ingredient> getIngredients() { return this.inputSlot; }

    @Override
    public ResourceLocation getId() { return this.id; }

    @Override
    public RecipeSerializer<?> getSerializer() { return Serializer.INSTANCE; }

    @Override
    public RecipeType<?> getType() { return Type.INSTANCE; }

    public static class Type implements RecipeType<EnchantedRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "enchanted";
    }

    public static class Serializer implements RecipeSerializer<EnchantedRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(MCCourseMod.MOD_ID,"enchanted");

        @Override
        public EnchantedRecipe fromJson(ResourceLocation id, JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output")); // Crafting type
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
            NonNullList<Ingredient> inputs = NonNullList.withSize(1, Ingredient.EMPTY); // Input ingredient
            for (int i = 0; i < inputs.size(); i++) { inputs.set(i, Ingredient.fromJson(ingredients.get(i))); }
            return new EnchantedRecipe(id, output, inputs);
        }

        // Read one ingredient and one item stack on each JSON file
        // CLIENT and SERVER is always connected
        @Override
        public EnchantedRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY);
            for (int i = 0; i < inputs.size(); i++) { inputs.set(i, Ingredient.fromNetwork(buf)); }
            ItemStack output = buf.readItem(); // Read Output from Network
            return new EnchantedRecipe(id, output, inputs);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, EnchantedRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size());
            for (Ingredient ing : recipe.getIngredients()) { ing.toNetwork(buf); }
            buf.writeItemStack(recipe.getResultItem(null), false);
        }
    }
}
