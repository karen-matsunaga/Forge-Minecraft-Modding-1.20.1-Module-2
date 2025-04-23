package net.karen.mccourse.datagen;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.datagen.custom.GemEmpoweringRecipeBuilder;
import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.ModItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fluids.FluidStack;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    // All items smelting and blasting
    private static final List<ItemLike> ALEXANDRITE_SMELTABLES = List.of(ModItems.RAW_ALEXANDRITE.get(),
            ModBlocks.ALEXANDRITE_ORE.get(), ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(), ModBlocks.END_STONE_ALEXANDRITE_ORE.get(),
            ModBlocks.NETHER_ALEXANDRITE_ORE.get());

    private static final List<ItemLike> PINK_SMELTABLES = List.of(ModItems.PINK.get(),
            ModBlocks.PINK_ORE.get(), ModBlocks.DEEPSLATE_PINK_ORE.get(), ModBlocks.END_STONE_PINK_ORE.get(), ModBlocks.NETHER_PINK_ORE.get());

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
        // Hammer
        pickaxeHammerItem(ModItems.ALEXANDRITE_HAMMER.get(), ModBlocks.ALEXANDRITE_BLOCK.get(), pWriter); // Hammer
        pickaxeHammerItem(ModItems.COPPER_HAMMER.get(), Items.COPPER_BLOCK, pWriter);
        pickaxeHammerItem(ModItems.DIAMOND_HAMMER.get(), Items.DIAMOND_BLOCK, pWriter);
        pickaxeHammerItem(ModItems.GOLD_HAMMER.get(), Items.GOLD_BLOCK, pWriter);
        pickaxeHammerItem(ModItems.IRON_HAMMER.get(), Items.IRON_BLOCK, pWriter);
        pickaxeHammerItem(ModItems.NETHERITE_HAMMER.get(), Items.NETHERITE_BLOCK, pWriter);
        pickaxeHammerItem(ModItems.PINK_HAMMER.get(), ModBlocks.PINK_BLOCK.get(), pWriter);
        pickaxeHammerItem(ModItems.WOODEN_HAMMER.get(), Items.OAK_LOG, pWriter);
        pickaxeHammerItem(ModItems.STONE_HAMMER.get(), Items.STONE, pWriter);

        // Pickaxe
        pickaxeHammerItem(ModItems.ALEXANDRITE_PICKAXE.get(), ModItems.ALEXANDRITE.get(), pWriter);
        pickaxeHammerItem(ModItems.PINK_PICKAXE.get(), ModItems.PINK.get(), pWriter);
        pickaxeHammerItem(ModItems.COPPER_PICKAXE.get(), Items.COPPER_INGOT, pWriter);

        // Sword
        swordItem(ModItems.ALEXANDRITE_SWORD.get(), ModItems.ALEXANDRITE.get(), pWriter);
        swordItem(ModItems.PINK_SWORD.get(), ModItems.PINK.get(), pWriter);
        swordItem(ModItems.COPPER_SWORD.get(), Items.COPPER_INGOT, pWriter);

        // Axe
        axeItem(ModItems.ALEXANDRITE_AXE.get(), ModItems.ALEXANDRITE.get(), pWriter);
        axeItem(ModItems.PINK_AXE.get(), ModItems.PINK.get(), pWriter);
        axeItem(ModItems.COPPER_AXE.get(), Items.COPPER_INGOT, pWriter);

        // Shovel
        shovelItem(ModItems.ALEXANDRITE_SHOVEL.get(), ModItems.ALEXANDRITE.get(), pWriter);
        shovelItem(ModItems.PINK_SHOVEL.get(), ModItems.PINK.get(), pWriter);
        shovelItem(ModItems.COPPER_SHOVEL.get(), Items.COPPER_INGOT, pWriter);

        // Paxel
        paxelItem(ModItems.ALEXANDRITE_PAXEL.get(), ModItems.ALEXANDRITE_PICKAXE.get(),
                ModItems.ALEXANDRITE_AXE.get(), ModItems.ALEXANDRITE_SHOVEL.get(), pWriter);
        paxelItem(ModItems.PINK_PAXEL.get(), ModItems.PINK_PICKAXE.get(),
                ModItems.PINK_AXE.get(), ModItems.PINK_SHOVEL.get(), pWriter);
        paxelItem(ModItems.COPPER_PAXEL.get(), ModItems.COPPER_PICKAXE.get(),
                ModItems.COPPER_AXE.get(), ModItems.COPPER_SHOVEL.get(), pWriter);
        paxelItem(ModItems.DIAMOND_PAXEL.get(), Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL, pWriter);
        paxelItem(ModItems.GOLD_PAXEL.get(), Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL, pWriter);
        paxelItem(ModItems.IRON_PAXEL.get(), Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL, pWriter);
        paxelItem(ModItems.STONE_PAXEL.get(), Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_SHOVEL, pWriter);
        paxelItem(ModItems.WOODEN_PAXEL.get(), Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_SHOVEL, pWriter);
        paxelItem(ModItems.NETHERITE_PAXEL.get(), Items.NETHERITE_PICKAXE, Items.NETHERITE_AXE, Items.NETHERITE_SHOVEL, pWriter);

        // Hoe
        hoeItem(ModItems.ALEXANDRITE_HOE.get(), ModItems.ALEXANDRITE.get(), pWriter);
        hoeItem(ModItems.PINK_HOE.get(), ModItems.PINK.get(), pWriter);
        hoeItem(ModItems.COPPER_HOE.get(), Items.COPPER_INGOT, pWriter);

        // Armor
        helmetArmor(ModItems.ALEXANDRITE_HELMET.get(), ModItems.ALEXANDRITE.get(), pWriter); // Helmet
        chestplateArmor(ModItems.ALEXANDRITE_CHESTPLATE.get(), ModItems.ALEXANDRITE.get(), pWriter); // Chestplate
        leggingsArmor(ModItems.ALEXANDRITE_LEGGINGS.get(), ModItems.ALEXANDRITE.get(), pWriter); // Leggings
        bootsArmor(ModItems.ALEXANDRITE_BOOTS.get(), ModItems.ALEXANDRITE.get(), pWriter); // Boots

        helmetArmor(ModItems.PINK_HELMET.get(), ModItems.PINK.get(), pWriter); // Helmet
        chestplateArmor(ModItems.PINK_CHESTPLATE.get(), ModItems.PINK.get(), pWriter); // Chestplate
        leggingsArmor(ModItems.PINK_LEGGINGS.get(), ModItems.PINK.get(), pWriter); // Leggings
        bootsArmor(ModItems.PINK_BOOTS.get(), ModItems.PINK.get(), pWriter); // Boots

        helmetArmor(ModItems.COPPER_HELMET.get(), Items.COPPER_INGOT, pWriter); // Helmet
        chestplateArmor(ModItems.COPPER_CHESTPLATE.get(), Items.COPPER_INGOT, pWriter); // Chestplate
        leggingsArmor(ModItems.COPPER_LEGGINGS.get(), Items.COPPER_INGOT, pWriter); // Leggings
        bootsArmor(ModItems.COPPER_BOOTS.get(), Items.COPPER_INGOT, pWriter); // Boots

        // Block -> Item and Item -> Block
        itemTransformBlock(ModBlocks.ENDER_PEARL_BLOCK.get(), Items.ENDER_PEARL, pWriter);
        blockTransformItem(Items.ENDER_PEARL, ModBlocks.ENDER_PEARL_BLOCK.get(), pWriter);

        itemTransformBlock(ModBlocks.NETHER_STAR_BLOCK.get(), Items.NETHER_STAR, pWriter);
        blockTransformItem(Items.NETHER_STAR, ModBlocks.NETHER_STAR_BLOCK.get(), pWriter);

        itemTransformBlock(ModBlocks.ROTTEN_FLESH_BLOCK.get(), Items.ROTTEN_FLESH, pWriter);
        blockTransformItem(Items.ROTTEN_FLESH, ModBlocks.ROTTEN_FLESH_BLOCK.get(), pWriter);

        itemTransformBlock(ModBlocks.GUNPOWDER_BLOCK.get(), Items.GUNPOWDER, pWriter);
        blockTransformItem(Items.GUNPOWDER, ModBlocks.GUNPOWDER_BLOCK.get(), pWriter);

        itemTransformBlock(ModBlocks.BLAZE_ROD_BLOCK.get(), Items.BLAZE_ROD, pWriter);
        blockTransformItem(Items.BLAZE_ROD, ModBlocks.BLAZE_ROD_BLOCK.get(), pWriter);

        // Ore
        itemTransformBlock(ModBlocks.PINK_BLOCK.get(), ModItems.PINK.get(), pWriter);
        blockTransformItem(ModItems.PINK.get(), ModBlocks.PINK_BLOCK.get(), pWriter);

        // My custom ore
        // Items Smelting
        oreSmelting(pWriter, PINK_SMELTABLES, RecipeCategory.MISC, ModItems.PINK.get(), 0.25f, 200, "pink");

        // Items Blasting
        oreBlasting(pWriter, PINK_SMELTABLES, RecipeCategory.MISC, ModItems.PINK.get(), 0.25f, 200, "pink");

        oreSmelting(pWriter, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 1.00f, 100, "rotten_flesh");
        oreBlasting(pWriter, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 1.00f, 100, "rotten_flesh");

        // My custom enchanted book and item enchanted
        generateEnchantedRecipe(new ResourceLocation("mccourse",
                        Enchantments.UNBREAKING.getDescriptionId().replace("enchantment.minecraft.", "") + "_book"),
                Items.ENCHANTED_BOOK,
                Map.of(Enchantments.UNBREAKING, 10), Items.BOOK, Items.OBSIDIAN, true, pWriter);

        generateEnchantedRecipe(new ResourceLocation("mccourse",
                        ModEnchantments.MORE_ORES.get().getDescriptionId().replace("enchantment.mccourse.", "")+ "_book"),
                Items.ENCHANTED_BOOK,
                Map.of(ModEnchantments.MORE_ORES.get(), 5,
                        Enchantments.BLOCK_EFFICIENCY, 10), Items.BOOK, Items.NETHER_STAR, true, pWriter);

        generateEnchantedRecipe(new ResourceLocation("mccourse",
                        Items.DIAMOND_PICKAXE.getDescriptionId().replace("item.minecraft.", "") + "_item"),
                Items.DIAMOND_PICKAXE,
                Map.of(ModEnchantments.MORE_ORES.get(), 5, Enchantments.UNBREAKING, 10,
                        Enchantments.BLOCK_EFFICIENCY, 10, Enchantments.MENDING, 1),
                Items.DIAMOND_PICKAXE,
                Items.COPPER_INGOT,
                false,
                pWriter);

        generateEnchantedRecipe(new ResourceLocation("mccourse",
                        ModItems.PINK_MODES.get().getDescriptionId().replace("item.mccourse.", "") + "_item"),
                ModItems.PINK_MODES.get(),
                Map.of(ModEnchantments.MORE_ORES.get(), 5, Enchantments.UNBREAKING, 10,
                        Enchantments.BLOCK_EFFICIENCY, 10, Enchantments.MENDING, 1),
                ModItems.PINK_MODES.get(),
                Items.GLOWSTONE,
                false,
                pWriter);

        // My custom tool enchantment
        toolEnchantment(ModItems.BLUE_MODES.get(), Items.DIAMOND, pWriter);
        toolEnchanted(ModItems.GREEN_MODES.get(), ModItems.PINK.get(), ModItems.BLUE_MODES.get(), pWriter);
        toolEnchanted(ModItems.PURPLE_MODES.get(), Items.NETHERITE_INGOT, ModItems.GREEN_MODES.get(), pWriter);

        // Colored blocks
        coloredBlocks(ModBlocks.GREEN_ENDER_PEARL_BLOCK.get(), Items.GREEN_DYE, pWriter);
        coloredBlocks(ModBlocks.LIME_GREEN_ENDER_PEARL_BLOCK.get(), Items.LIME_DYE, pWriter);
        coloredBlocks(ModBlocks.MAGENTA_ENDER_PEARL_BLOCK.get(), Items.MAGENTA_DYE, pWriter);
        coloredBlocks(ModBlocks.PINK_ENDER_PEARL_BLOCK.get(), Items.PINK_DYE, pWriter);
        coloredBlocks(ModBlocks.PURPLE_ENDER_PEARL_BLOCK.get(), Items.PURPLE_DYE, pWriter);
        coloredBlocks(ModBlocks.BLACK_ENDER_PEARL_BLOCK.get(), Items.BLACK_DYE, pWriter);
        coloredBlocks(ModBlocks.BLUE_ENDER_PEARL_BLOCK.get(), Items.BLUE_DYE, pWriter);
        coloredBlocks(ModBlocks.CYAN_ENDER_PEARL_BLOCK.get(), Items.CYAN_DYE, pWriter);
        coloredBlocks(ModBlocks.GRAY_ENDER_PEARL_BLOCK.get(), Items.GRAY_DYE, pWriter);
        coloredBlocks(ModBlocks.BROWN_ENDER_PEARL_BLOCK.get(), Items.BROWN_DYE, pWriter);
        coloredBlocks(ModBlocks.YELLOW_ENDER_PEARL_BLOCK.get(), Items.YELLOW_DYE, pWriter);
        coloredBlocks(ModBlocks.WHITE_ENDER_PEARL_BLOCK.get(), Items.WHITE_DYE, pWriter);
        coloredBlocks(ModBlocks.ORANGE_ENDER_PEARL_BLOCK.get(), Items.ORANGE_DYE, pWriter);
        coloredBlocks(ModBlocks.RED_ENDER_PEARL_BLOCK.get(), Items.RED_DYE, pWriter);

        // My Disenchanted custom block
        itemTransformBlock(ModBlocks.DISENCHANTED_BLOCK.get(), Blocks.OBSIDIAN, pWriter);
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

    // Cooking -> Custom method to oreSmelting and oreBlasting
    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer,
                                     List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime,
                            pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, MCCourseMod.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }

    // My custom Recipe methods
    // Item transform on custom block
    protected static void itemTransformBlock(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pResult)
                .pattern("AAA")
                .pattern("AAA")
                .pattern("AAA")
                .define('A', item)
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item().
                        of(item).build()))
                .save(pWriter);
    }

    // Custom block transform on item
    protected static void blockTransformItem(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pResult, 9)
                .requires(item)
                .unlockedBy("has_item", inventoryTrigger(ItemPredicate.Builder.item().
                        of(item).build()))
                .save(pWriter);
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

    // Custom tools enchanted
    public static void toolEnchantment(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pResult, 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', item)
                .define('B', Items.BOOK)
                .unlockedBy("has_item", has(item))
                .save(pWriter);
    }

    public static void toolEnchanted(ItemLike pResult, ItemLike item, ItemLike item2, Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pResult, 1)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', item)
                .define('B', item2)
                .unlockedBy("has_item", has(item))
                .save(pWriter);
    }

    // Custom color blocks
    public static void coloredBlocks(ItemLike pResult, ItemLike item, Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pResult, 1)
                .pattern("   ")
                .pattern(" AB")
                .pattern("   ")
                .define('A', ModBlocks.ENDER_PEARL_BLOCK.get())
                .define('B', item)
                .unlockedBy("has_item", has(item))
                .save(pWriter);
    }

    // Custom enchanted item or enchanted book
    public static void generateEnchantedRecipe(ResourceLocation id, ItemLike resultItem, Map<Enchantment, Integer> enchantments,
                                               ItemLike centerItem, ItemLike borderMaterial, boolean isBook,
                                               Consumer<FinishedRecipe> writer) {
        // Registry item
        JsonObject resultJson = new JsonObject();
        resultJson.addProperty("item", BuiltInRegistries.ITEM.getKey(resultItem.asItem()).toString());
        resultJson.addProperty("count", 1);

        // Registry enchantments
        JsonObject nbt = new JsonObject();
        JsonArray enchantmentArray = new JsonArray();

        // Sorts enchantments by level and then by ID
        enchantments.entrySet().stream()
                .sorted(Comparator
                        .comparingInt(Map.Entry<Enchantment, Integer>::getValue) // Enchantment level
                        .thenComparing(e -> BuiltInRegistries.ENCHANTMENT.getKey(e.getKey()).toString())) // Enchantment name
                .forEach(entry -> {
                    JsonObject enchantmentTag = new JsonObject();
                    enchantmentTag.addProperty("id", BuiltInRegistries.ENCHANTMENT.getKey(entry.getKey()).toString());
                    enchantmentTag.addProperty("lvl", entry.getValue());
                    enchantmentArray.add(enchantmentTag);
                });

        nbt.add(isBook ? "StoredEnchantments" : "Enchantments", enchantmentArray);
        resultJson.add("nbt", nbt);

        // Registry recipe
        JsonObject recipeJson = new JsonObject();
        recipeJson.addProperty("type", "minecraft:crafting_shaped");

        JsonArray pattern = new JsonArray();
        pattern.add("AAA");
        pattern.add("ABA");
        pattern.add("AAA");
        recipeJson.add("pattern", pattern);

        JsonObject key = new JsonObject();

        JsonObject aKey = new JsonObject();
        aKey.addProperty("item", BuiltInRegistries.ITEM.getKey(borderMaterial.asItem()).toString());
        key.add("A", aKey);

        JsonObject bKey = new JsonObject();
        bKey.addProperty("item", BuiltInRegistries.ITEM.getKey(centerItem.asItem()).toString());
        key.add("B", bKey);

        recipeJson.add("key", key);
        recipeJson.add("result", resultJson);

        // Registry JSON file
        writer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(JsonObject jsonObject) {
                jsonObject.add("type", recipeJson.get("type"));
                jsonObject.add("pattern", recipeJson.get("pattern"));
                jsonObject.add("key", recipeJson.get("key"));
                jsonObject.add("result", recipeJson.get("result"));
            }

            @Override
            public ResourceLocation getId() { return id; }

            @Override
            public RecipeSerializer<?> getType() { return RecipeSerializer.SHAPED_RECIPE; }

            @Override
            public JsonObject serializeAdvancement() { return null; }

            @Override
            public ResourceLocation getAdvancementId() { return new ResourceLocation(""); }
        });
    }
}