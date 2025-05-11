package net.karen.mccourse.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.recipe.EnchantedRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;

public class EnchantedRecipeBuilder implements RecipeBuilder {
    private final Enchantment enchantment;
    private final int enchantLevel;
    private final List<Ingredient> ingredient;
    private final List<Integer> count;
    private final Item result;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public EnchantedRecipeBuilder(Enchantment enchantment, int enchantLevel, List<Ingredient> ingredient, List<Integer> count, ItemLike result) {
        this.enchantment = enchantment;
        this.enchantLevel = enchantLevel;
        this.ingredient = new ArrayList<>(ingredient);
        this.count = new ArrayList<>(count);
        this.result = result.asItem();
    }

    @Override
    public @NotNull RecipeBuilder unlockedBy(@NotNull String pCriterionName,
                                             @NotNull CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public @NotNull RecipeBuilder group(@Nullable String pGroupName) { return this; }

    @Override
    public @NotNull Item getResult() { return result; }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, @NotNull ResourceLocation pRecipeId) {
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);

        pFinishedRecipeConsumer.accept(new Result(this.enchantment, this.enchantLevel, pRecipeId, this.result, this.count, this.ingredient,
                this.advancement, new ResourceLocation(pRecipeId.getNamespace(), "recipes/"
                + pRecipeId.getPath())));
    }

    public static class Result implements FinishedRecipe {
        private final Enchantment enchantment;
        private final int enchantLevel;
        private final ResourceLocation id;
        private final Item result;
        private final List<Ingredient> ingredient;
        private final List<Integer> count;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(Enchantment enchantment, int enchantLevel, ResourceLocation pId, Item pResult, List<Integer> pCount, List<Ingredient> ingredient,
                      Advancement.Builder pAdvancement, ResourceLocation pAdvancementId) {
            this.enchantment = enchantment;
            this.enchantLevel = enchantLevel;
            this.id = pId;
            this.result = pResult;
            this.count = pCount;
            this.ingredient = ingredient;
            this.advancement = pAdvancement;
            this.advancementId = pAdvancementId;
        }

        @Override
        public void serializeRecipeData(@NotNull JsonObject pJson) {
            JsonArray jsonarray = new JsonArray();
            for (int i = 0; i < ingredient.size(); i++) {
                JsonObject ingredientObject = new JsonObject();

                int size = i < count.size() ? count.get(i) : 1; // Ingredient count -> If not define is default 1
                ingredientObject.addProperty("count", size);

                ItemStack[] stack = ingredient.get(i).getItems(); // Added each Ingredient
                ingredientObject.addProperty("item",
                        Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack[0].getItem())).toString());

                jsonarray.add(ingredientObject);
            }

            pJson.add("ingredients", jsonarray); // Ingredients: []

            // Enchantment
            JsonObject outputJson = new JsonObject();
            outputJson.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(this.result)).toString());
            JsonArray enchantments = new JsonArray();

            JsonObject enchJson = new JsonObject();
            enchJson.addProperty("id", Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(this.enchantment)).toString());
            enchJson.addProperty("lvl", enchantLevel);
            enchantments.add(enchJson);

            JsonObject nbtJson = new JsonObject();
            nbtJson.add("StoredEnchantments", enchantments);

            outputJson.add("nbt", nbtJson);
            pJson.add("output", outputJson); // Output: {}
        }

        // File with numbers
        private static final Map<ResourceLocation, Integer> counters = new HashMap<>();

        @Override
        public @NotNull ResourceLocation getId() {
            ResourceLocation baseId = this.id;
            int count = counters.getOrDefault(baseId, 0) + 1;
            counters.put(baseId, count);

            return new ResourceLocation(MCCourseMod.MOD_ID,
                    baseId.getPath() + "_from_enchanted_" + count);
        }

        @Override
        public @NotNull RecipeSerializer<?> getType() { return EnchantedRecipe.Serializer.INSTANCE; }

        @Nullable
        public JsonObject serializeAdvancement() { return this.advancement.serializeToJson(); }

        @Nullable
        public ResourceLocation getAdvancementId() { return this.advancementId; }
    }
}