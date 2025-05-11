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
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EnchantedRecipe implements Recipe<SimpleContainer> {
    private final Enchantment enchantment;
    private final Integer enchantLevel;
    private final NonNullList<Ingredient> inputItems; // Ingredients = Slots
    private final List<Integer> inputCounts; // Ingredients counts
    private final ItemStack output; // Output
    private final ResourceLocation id; // Item id

    public EnchantedRecipe(Enchantment enchantment, Integer enchantLevel, ResourceLocation id, ItemStack output,
                           NonNullList<Ingredient> inputItems, List<Integer> inputCounts) {
        this.enchantment = enchantment;
        this.enchantLevel = enchantLevel;
        this.inputItems = inputItems;
        this.inputCounts = inputCounts;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(@NotNull SimpleContainer container, @NotNull Level world) {
        for (int i = 0; i < inputItems.size(); i++) {
            ItemStack stack = container.getItem(i);
            if (!inputItems.get(i).test(stack) || stack.getCount() < inputCounts.get(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public @NotNull ItemStack assemble(@NotNull SimpleContainer pContainer, @NotNull RegistryAccess pRegistryAccess) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) { return true; }

    @Override
    public @NotNull ItemStack getResultItem(RegistryAccess pRegistryAccess) { return output.copy(); }

    @Override
    public @NotNull NonNullList<Ingredient> getIngredients() { return this.inputItems; } // Ingredients

    public List<Integer> getIngredientsCounts() { return this.inputCounts; } // Ingredients counts

    @Override
    public @NotNull ResourceLocation getId() { return id; }

    @Override
    public RecipeSerializer<?> getSerializer() { return null; }

    @Override
    public @NotNull RecipeType<?> getType() { return Type.INSTANCE; }

    public static class Type implements RecipeType<EnchantedRecipe> {
        private Type() {}
        public static final Type INSTANCE = new Type();
        public static final String ID = "enchanted";
    }

    public static class Serializer implements RecipeSerializer<EnchantedRecipe> {
        public static final Serializer INSTANCE = new Serializer();
        public static final ResourceLocation ID =
                new ResourceLocation(MCCourseMod.MOD_ID, "enchanted");

        @Override
        public @NotNull EnchantedRecipe fromJson(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            ItemStack output = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "output")); // Output item
            NonNullList<Ingredient> inputs = NonNullList.withSize(2, Ingredient.EMPTY); // Slot size -> Input ingredient
            List<Integer> inputsCounts = new ArrayList<>(2); // Slot size -> Ingredient counts
            JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");

            // Added each ingredient with your count
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromJson(ingredients.get(i))); // Ingredient
                inputsCounts.add(GsonHelper.getAsInt(ingredients.get(i).getAsJsonObject(), "count", 1)); // Ingredient count
            }

            var enchantment = ForgeRegistries.ENCHANTMENTS.getValue(new ResourceLocation(GsonHelper.getAsString(json, "enchantment")));
            int enchantLevel = json.get("lvl").getAsInt(); // Read Enchantment Level on JSON file

            return new EnchantedRecipe(enchantment, enchantLevel, id, output, inputs, inputsCounts);
        }

        @Override
        public @Nullable EnchantedRecipe fromNetwork(@NotNull ResourceLocation id, FriendlyByteBuf buf) {
            NonNullList<Ingredient> inputs = NonNullList.withSize(buf.readInt(), Ingredient.EMPTY); // Read Ingredients from Network
            List<Integer> inputsCounts = new ArrayList<>(buf.readInt()); // Read Ingredient counts from Network

            // Added each ingredient with your count
            for (int i = 0; i < inputs.size(); i++) {
                inputs.set(i, Ingredient.fromNetwork(buf)); // Read Ingredients
                inputsCounts.add(buf.readInt()); // Read Ingredient counts
            }

            var enchantment = ForgeRegistries.ENCHANTMENTS.getValue(buf.readResourceLocation());
            int enchantLevel = buf.readInt(); // Read Enchantment Level from Network

            ItemStack output = buf.readItem(); // Read Output from Network
            return new EnchantedRecipe(enchantment, enchantLevel, id, output, inputs, inputsCounts);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buf, EnchantedRecipe recipe) {
            buf.writeInt(recipe.getIngredients().size()); // Write Ingredients to Network

            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                recipe.getIngredients().get(i).toNetwork(buf); // Write Ingredients
                buf.writeInt(i < recipe.getIngredientsCounts().size() ? recipe.getIngredientsCounts().get(i) : 1); // Write Ingredient counts
            }

            var enchantment = Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(recipe.enchantment));
            buf.writeResourceLocation(enchantment);

            buf.writeInt(recipe.enchantLevel); // Read Enchantment Level to Network = Read integer

            buf.writeItemStack(recipe.getResultItem(null), false); // Write Output to Network
        }
    }
}