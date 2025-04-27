package net.karen.mccourse.datagen.custom;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.recipe.CraftRecipe;
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
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class CraftRecipeBuilder implements RecipeBuilder {
    private final List<Ingredient> ingredient;
    private final List<Integer> count;
    private final Item result;
    private final Advancement.Builder advancement = Advancement.Builder.advancement();

    public CraftRecipeBuilder(List<Ingredient> ingredient, List<Integer> count, ItemLike result) {
        this.ingredient = new ArrayList<>(ingredient);
        this.count = new ArrayList<>(count);
        this.result = result.asItem();
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) { return this; }

    @Override
    public Item getResult() { return result; }

    // Save all Craft recipe custom recipes
    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);

        pFinishedRecipeConsumer.accept(new Result(pRecipeId, this.result, this.count, this.ingredient,
                this.advancement, new ResourceLocation(pRecipeId.getNamespace(), "recipes/"
                + pRecipeId.getPath())));
    }

    // WRITE Gem Empowering Station JSON custom recipes
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Item result;
        private final List<Ingredient> ingredient;
        private final List<Integer> count;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation pId, Item pResult, List<Integer> pCount, List<Ingredient> ingredient, Advancement.Builder pAdvancement,
                      ResourceLocation pAdvancementId) {
            this.id = pId;
            this.result = pResult;
            this.count = pCount;
            this.ingredient = ingredient;
            this.advancement = pAdvancement;
            this.advancementId = pAdvancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            JsonArray jsonarray = new JsonArray();

            for (int i = 0; i < ingredient.size(); i++) {
                JsonObject ingredientObject = new JsonObject();

                int size = i < count.size() ? count.get(i) : 1; // Ingredient count -> If not define is default 1
                ingredientObject.addProperty("count", size);

                ItemStack[] stack = ingredient.get(i).getItems(); // Added each Ingredient
                ingredientObject.addProperty("item", Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(stack[0].getItem())).toString());

                jsonarray.add(ingredientObject);
            }

            pJson.add("ingredients", jsonarray); // Ingredients: []

            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("item", ForgeRegistries.ITEMS.getKey(this.result).toString());

            pJson.add("output", jsonobject); // Output: {}
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(MCCourseMod.MOD_ID,
                    ForgeRegistries.ITEMS.getKey(this.result).getPath() + "_from_craft");
        }

        @Override
        public RecipeSerializer<?> getType() { return CraftRecipe.Serializer.INSTANCE; }

        @Nullable
        public JsonObject serializeAdvancement() { return this.advancement.serializeToJson(); }

        @Nullable
        public ResourceLocation getAdvancementId() { return this.advancementId; }
    }
}