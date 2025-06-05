package net.karen.mccourse.datagen.custom;

import com.google.gson.JsonObject;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.recipe.KaupenFurnaceRecipe;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;
import java.util.function.Consumer;

public class KaupenFurnaceRecipeBuilder implements RecipeBuilder {
    private final Ingredient ingredient;
    private final Item result;
    private final float experience;
    private final int cookingTime;
    private final Advancement.Builder advancement = Advancement.Builder.recipeAdvancement();

    public KaupenFurnaceRecipeBuilder(ItemLike ingredient, ItemLike result, float experience,
                                      int cookingTime) {
        this.ingredient = Ingredient.of(ingredient);
        this.result = result.asItem();
        this.experience = experience;
        this.cookingTime = cookingTime;
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        this.advancement.addCriterion(pCriterionName, pCriterionTrigger);
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) { return this; }

    @Override
    public Item getResult() { return this.result; }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        this.advancement.parent(new ResourceLocation("recipes/root"))
                .addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(pRecipeId))
                .rewards(AdvancementRewards.Builder.recipe(pRecipeId)).requirements(RequirementsStrategy.OR);
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, this.result,
                this.ingredient, this.experience, this.cookingTime, this.advancement,
                new ResourceLocation(pRecipeId.getNamespace(), "recipes/" + pRecipeId.getPath())));
    }

    // WRITE Gem Empowering Station JSON custom recipes
    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final Item result;
        private final float experience;
        private final int cookingTime;
        private final Advancement.Builder advancement;
        private final ResourceLocation advancementId;

        public Result(ResourceLocation pId, Item pResult, Ingredient ingredient, float experience, int cookingTime,
                      Advancement.Builder advancement, ResourceLocation advancementId) {
            this.id = pId;
            this.ingredient = ingredient;
            this.result = pResult;
            this.experience = experience;
            this.cookingTime = cookingTime;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("category", RecipeCategory.MISC.toString().toLowerCase());
            json.add("ingredient", this.ingredient.toJson());
            json.addProperty("result", ForgeRegistries.ITEMS.getKey(this.result).toString());
            json.addProperty("experience", this.experience);
            json.addProperty("cookingtime", this.cookingTime);
        }

        @Override
        public ResourceLocation getId() {
            return new ResourceLocation(MCCourseMod.MOD_ID,
                    ForgeRegistries.ITEMS.getKey(this.result).getPath() + "_from_kaupen_furnace");
        }

        @Override
        public RecipeSerializer<?> getType() { return KaupenFurnaceRecipe.Serializer.INSTANCE; }

        @javax.annotation.Nullable
        public JsonObject serializeAdvancement() { return this.advancement.serializeToJson(); }

        @javax.annotation.Nullable
        public ResourceLocation getAdvancementId() { return this.advancementId; }
    }
}