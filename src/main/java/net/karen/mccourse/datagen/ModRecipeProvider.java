package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.datagen.custom.GemEmpoweringRecipeBuilder;
import net.karen.mccourse.item.ModItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    // All items smelting and blasting
    private static final List<ItemLike> ALEXANDRITE_SMELTABLES = List.of(ModItems.RAW_ALEXANDRITE.get(),
            ModBlocks.ALEXANDRITE_ORE.get(), ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(), ModBlocks.END_STONE_ALEXANDRITE_ORE.get(),
            ModBlocks.NETHER_ALEXANDRITE_ORE.get());

    public ModRecipeProvider(PackOutput pOutput) { super(pOutput); }

    // Create all recipes
    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        // Alexandrite transforms on Alexandrite Block recipe
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.ALEXANDRITE_BLOCK.get())
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', ModItems.ALEXANDRITE.get())
                .unlockedBy("has_alexandrite", inventoryTrigger(ItemPredicate.Builder.item().
                        of(ModItems.ALEXANDRITE.get()).build()))
                .save(pWriter);

        // Alexandrite Block converts to Alexandrite recipe
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.ALEXANDRITE.get(), 9)
                .requires(ModBlocks.ALEXANDRITE_BLOCK.get())
                .unlockedBy("has_alexandrite_block", inventoryTrigger(ItemPredicate.Builder.item().
                        of(ModBlocks.ALEXANDRITE_BLOCK.get()).build()))
                .save(pWriter);

        // Raw Alexandrite
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, ModItems.RAW_ALEXANDRITE.get(), RecipeCategory.MISC, ModBlocks.ALEXANDRITE_BLOCK.get(),
                "mccourse:raw_alexandrite", "alexandrite", "mccourse:raw_alexandrite_block", null);

        // Items Smelting
        oreSmelting(pWriter, ALEXANDRITE_SMELTABLES, RecipeCategory.MISC, ModItems.ALEXANDRITE.get(), 0.25f, 200, "alexandrite");

        // Items Blasting
        oreBlasting(pWriter, ALEXANDRITE_SMELTABLES, RecipeCategory.MISC, ModItems.ALEXANDRITE.get(), 0.25f, 200, "alexandrite");

        // Gem Empowering Station custom recipes
        new GemEmpoweringRecipeBuilder(ModItems.RAW_ALEXANDRITE.get(), ModItems.ALEXANDRITE.get(), 3, 160, 50,
                new FluidStack(Fluids.WATER, 2000))
                .unlockedBy("has_raw_alexandrite", has(ModItems.RAW_ALEXANDRITE.get())).save(pWriter);

        new GemEmpoweringRecipeBuilder(Items.COAL, Items.DIAMOND, 7, 40, 150,
                new FluidStack(Fluids.LAVA, 500))
                .unlockedBy("has_diamond", has(Items.DIAMOND)).save(pWriter);

        // My custom mod
        // Hammer and Pickaxe
        swordItem(ModItems.ALEXANDRITE_SWORD.get(), ModItems.ALEXANDRITE.get(), pWriter); // Sword

        pickaxeHammerItem(ModItems.ALEXANDRITE_HAMMER.get(), ModBlocks.ALEXANDRITE_BLOCK.get(), pWriter); // Hammer
        pickaxeHammerItem(ModItems.ALEXANDRITE_PICKAXE.get(), ModItems.ALEXANDRITE.get(), pWriter); // Pickaxe

        axeItem(ModItems.ALEXANDRITE_AXE.get(), ModItems.ALEXANDRITE.get(), pWriter); // Axe
        shovelItem(ModItems.ALEXANDRITE_SHOVEL.get(), ModItems.ALEXANDRITE.get(), pWriter); // Shovel

        paxelItem(ModItems.ALEXANDRITE_PAXEL.get(), ModItems.ALEXANDRITE_PICKAXE.get(),
                ModItems.ALEXANDRITE_AXE.get(), ModItems.ALEXANDRITE_SHOVEL.get(), pWriter); // Paxel

        hoeItem(ModItems.ALEXANDRITE_HOE.get(), ModItems.ALEXANDRITE.get(), pWriter); // Hoe

        // Armor
        helmetArmor(ModItems.ALEXANDRITE_HELMET.get(), ModItems.ALEXANDRITE.get(), pWriter); // Helmet
        chestplateArmor(ModItems.ALEXANDRITE_CHESTPLATE.get(), ModItems.ALEXANDRITE.get(), pWriter); // Chestplate
        leggingsArmor(ModItems.ALEXANDRITE_LEGGINGS.get(), ModItems.ALEXANDRITE.get(), pWriter); // Leggings
        bootsArmor(ModItems.ALEXANDRITE_BOOTS.get(), ModItems.ALEXANDRITE.get(), pWriter); // Boots
    }

    // Smelting
    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    // Blasting
    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    // Cooking
    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer,
                                     List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime,
                            pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, MCCourseMod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    // Custom recipe
    protected static void pickaxeHammerItem(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        // Hammer and Pickaxe tools
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pResult, 1)
                .pattern("AAA")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', item)
                .define('B', Items.STICK)
                .unlockedBy("has_item", has(pResult))
                .save(pWriter);
    }

    protected static void helmetArmor(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        // Helmet
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pResult, 1)
                .pattern("AAA")
                .pattern("A A")
                .pattern("   ")
                .define('A', item)
                .unlockedBy("has_item", has(pResult))
                .save(pWriter);
    }

    protected static void chestplateArmor(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        // Chestplate
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pResult, 1)
                .pattern("A A")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', item)
                .unlockedBy("has_item", has(pResult))
                .save(pWriter);
    }

    protected static void leggingsArmor(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        // Leggings
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pResult, 1)
                .pattern("AAA")
                .pattern("A A")
                .pattern("A A")
                .define('A', item)
                .unlockedBy("has_item", has(pResult))
                .save(pWriter);
    }

    protected static void bootsArmor(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        // Boots
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pResult, 1)
                .pattern("   ")
                .pattern("A A")
                .pattern("A A")
                .define('A', item)
                .unlockedBy("has_item", has(pResult))
                .save(pWriter);
    }

    protected static void shovelItem(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        // Shovel
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pResult, 1)
                .pattern(" A ")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', item)
                .define('B', Items.STICK)
                .unlockedBy("has_item", has(pResult))
                .save(pWriter);
    }

    protected static void axeItem(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        // Axe
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pResult, 1)
                .pattern("AA ")
                .pattern("AB ")
                .pattern(" B ")
                .define('A', item)
                .define('B', Items.STICK)
                .unlockedBy("has_item", has(pResult))
                .save(pWriter);
    }

    protected static void paxelItem(ItemLike pResult, ItemLike item, ItemLike item2, ItemLike item3, Consumer<FinishedRecipe> pWriter) {
        // Paxel
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pResult, 1)
                .pattern("   ")
                .pattern("ABC")
                .pattern("   ")
                .define('A', item)
                .define('B', item2)
                .define('C', item3)
                .unlockedBy("has_item", has(pResult))
                .save(pWriter);
    }

    protected static void swordItem(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        // Paxel
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pResult, 1)
                .pattern(" A ")
                .pattern(" A ")
                .pattern(" B ")
                .define('A', item)
                .define('B', Items.STICK)
                .unlockedBy("has_item", has(pResult))
                .save(pWriter);
    }

    protected static void hoeItem(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        // Paxel
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, pResult, 1)
                .pattern("AA ")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', item)
                .define('B', Items.STICK)
                .unlockedBy("has_item", has(pResult))
                .save(pWriter);
    }
}