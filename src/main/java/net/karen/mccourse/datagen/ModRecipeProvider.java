package net.karen.mccourse.datagen;

import com.google.gson.*;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.datagen.custom.GemEmpoweringRecipeBuilder;
import net.karen.mccourse.datagen.custom.KaupenFurnaceRecipeBuilder;
import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.ModItems;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    // All items smelting and blasting
    private static final List<ItemLike> ALEXANDRITE_SMELTABLES = List.of(ModItems.RAW_ALEXANDRITE.get(),
            ModBlocks.ALEXANDRITE_ORE.get(), ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(),
            ModBlocks.END_STONE_ALEXANDRITE_ORE.get(), ModBlocks.NETHER_ALEXANDRITE_ORE.get());

    private static final List<ItemLike> PINK_SMELTABLES = List.of(ModItems.PINK.get(),
            ModBlocks.PINK_ORE.get(), ModBlocks.DEEPSLATE_PINK_ORE.get(),
            ModBlocks.END_STONE_PINK_ORE.get(), ModBlocks.NETHER_PINK_ORE.get());

    public ModRecipeProvider(PackOutput pOutput) { super(pOutput); }

    // Create all recipes
    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
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
        nineBlockStorageRecipes(pWriter, RecipeCategory.MISC, ModItems.RAW_ALEXANDRITE.get(),
                RecipeCategory.MISC, ModBlocks.ALEXANDRITE_BLOCK.get(),
                "mccourse:raw_alexandrite", "alexandrite",
                "mccourse:raw_alexandrite_block", null);

        // Items Smelting
        oreSmelting(pWriter, ALEXANDRITE_SMELTABLES, RecipeCategory.MISC, ModItems.ALEXANDRITE.get(),
                0.25f, 200, "alexandrite");

        // Items Blasting
        oreBlasting(pWriter, ALEXANDRITE_SMELTABLES, RecipeCategory.MISC, ModItems.ALEXANDRITE.get(),
                0.25f, 200, "alexandrite");

        // Gem Empowering Station custom recipes
        new GemEmpoweringRecipeBuilder(ModItems.RAW_ALEXANDRITE.get(), ModItems.ALEXANDRITE.get(),
                3, 160, 50, new FluidStack(Fluids.WATER, 2000))
                .unlockedBy("has_raw_alexandrite", has(ModItems.RAW_ALEXANDRITE.get())).save(pWriter);

        new GemEmpoweringRecipeBuilder(Items.COAL, Items.DIAMOND, 7, 40, 150,
                new FluidStack(Fluids.LAVA, 500))
                .unlockedBy("has_diamond", has(Items.DIAMOND)).save(pWriter);

        // Kaupen Furnace custom recipes
        new KaupenFurnaceRecipeBuilder(Items.IRON_INGOT, Items.RAW_IRON, 0.5f, 50)
                .unlockedBy("has_raw_iron", has(Items.RAW_IRON)).save(pWriter);

        new KaupenFurnaceRecipeBuilder(Items.COAL, Items.DIAMOND, 0.5f, 50)
                .unlockedBy("has_diamond", has(Items.DIAMOND)).save(pWriter);

        // My custom mod
        // Hammer
        pickaxeHammerItem(List.of(ModItems.ALEXANDRITE_HAMMER.get(), ModBlocks.ALEXANDRITE_BLOCK.get()), pWriter);
        pickaxeHammerItem(List.of(ModItems.COPPER_HAMMER.get(), Items.COPPER_BLOCK), pWriter);
        pickaxeHammerItem(List.of(ModItems.DIAMOND_HAMMER.get(), Items.DIAMOND_BLOCK), pWriter);
        pickaxeHammerItem(List.of(ModItems.GOLD_HAMMER.get(), Items.GOLD_BLOCK), pWriter);
        pickaxeHammerItem(List.of(ModItems.IRON_HAMMER.get(), Items.IRON_BLOCK), pWriter);
        pickaxeHammerItem(List.of(ModItems.NETHERITE_HAMMER.get(), Items.NETHERITE_BLOCK), pWriter);
        pickaxeHammerItem(List.of(ModItems.PINK_HAMMER.get(), ModBlocks.PINK_BLOCK.get()), pWriter);
        pickaxeHammerItem(List.of(ModItems.WOODEN_HAMMER.get(), Items.OAK_LOG), pWriter);
        pickaxeHammerItem(List.of(ModItems.STONE_HAMMER.get(), Items.STONE), pWriter);

        // Pickaxe
        pickaxeHammerItem(List.of(ModItems.ALEXANDRITE_PICKAXE.get(), ModItems.ALEXANDRITE.get()), pWriter);
        pickaxeHammerItem(List.of(ModItems.PINK_PICKAXE.get(), ModItems.PINK.get()), pWriter);
        pickaxeHammerItem(List.of(ModItems.COPPER_PICKAXE.get(), Items.COPPER_INGOT), pWriter);

        // Sword
        swordItem(List.of(ModItems.ALEXANDRITE_SWORD.get(), ModItems.ALEXANDRITE.get()), pWriter);
        swordItem(List.of(ModItems.PINK_SWORD.get(), ModItems.PINK.get()), pWriter);
        swordItem(List.of(ModItems.COPPER_SWORD.get(), Items.COPPER_INGOT), pWriter);

        // Axe
        axeItem(List.of(ModItems.ALEXANDRITE_AXE.get(), ModItems.ALEXANDRITE.get()), pWriter);
        axeItem(List.of(ModItems.PINK_AXE.get(), ModItems.PINK.get()), pWriter);
        axeItem(List.of(ModItems.COPPER_AXE.get(), Items.COPPER_INGOT), pWriter);

        // Shovel
        shovelItem(List.of(ModItems.ALEXANDRITE_SHOVEL.get(), ModItems.ALEXANDRITE.get()), pWriter);
        shovelItem(List.of(ModItems.PINK_SHOVEL.get(), ModItems.PINK.get()), pWriter);
        shovelItem(List.of(ModItems.COPPER_SHOVEL.get(), Items.COPPER_INGOT), pWriter);

        // Paxel
        paxelItem(List.of(ModItems.ALEXANDRITE_PAXEL.get(), ModItems.ALEXANDRITE_PICKAXE.get(),
                ModItems.ALEXANDRITE_AXE.get(), ModItems.ALEXANDRITE_SHOVEL.get()), pWriter);
        paxelItem(List.of(ModItems.PINK_PAXEL.get(), ModItems.PINK_PICKAXE.get(),
                ModItems.PINK_AXE.get(), ModItems.PINK_SHOVEL.get()), pWriter);
        paxelItem(List.of(ModItems.COPPER_PAXEL.get(), ModItems.COPPER_PICKAXE.get(),
                ModItems.COPPER_AXE.get(), ModItems.COPPER_SHOVEL.get()), pWriter);
        paxelItem(List.of(ModItems.DIAMOND_PAXEL.get(), Items.DIAMOND_PICKAXE,
                Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL), pWriter);
        paxelItem(List.of(ModItems.GOLD_PAXEL.get(), Items.GOLDEN_PICKAXE,
                Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL), pWriter);
        paxelItem(List.of(ModItems.IRON_PAXEL.get(), Items.IRON_PICKAXE,
                Items.IRON_AXE, Items.IRON_SHOVEL), pWriter);
        paxelItem(List.of(ModItems.STONE_PAXEL.get(), Items.STONE_PICKAXE,
                Items.STONE_AXE, Items.STONE_SHOVEL), pWriter);
        paxelItem(List.of(ModItems.WOODEN_PAXEL.get(), Items.WOODEN_PICKAXE,
                Items.WOODEN_AXE, Items.WOODEN_SHOVEL), pWriter);
        paxelItem(List.of(ModItems.NETHERITE_PAXEL.get(), Items.NETHERITE_PICKAXE,
                Items.NETHERITE_AXE, Items.NETHERITE_SHOVEL), pWriter);

        // Hoe
        hoeItem(List.of(ModItems.ALEXANDRITE_HOE.get(), ModItems.ALEXANDRITE.get()), pWriter);
        hoeItem(List.of(ModItems.PINK_HOE.get(), ModItems.PINK.get()), pWriter);
        hoeItem(List.of(ModItems.COPPER_HOE.get(), Items.COPPER_INGOT), pWriter);

        // Armor
        helmetArmor(List.of(ModItems.ALEXANDRITE_HELMET.get(), ModItems.ALEXANDRITE.get()), pWriter); // Helmet
        chestplateArmor(List.of(ModItems.ALEXANDRITE_CHESTPLATE.get(), ModItems.ALEXANDRITE.get()), pWriter); // Chestplate
        leggingsArmor(List.of(ModItems.ALEXANDRITE_LEGGINGS.get(), ModItems.ALEXANDRITE.get()), pWriter); // Leggings
        bootsArmor(List.of(ModItems.ALEXANDRITE_BOOTS.get(), ModItems.ALEXANDRITE.get()), pWriter); // Boots

        helmetArmor(List.of(ModItems.PINK_HELMET.get(), ModItems.PINK.get()), pWriter); // Helmet
        chestplateArmor(List.of(ModItems.PINK_CHESTPLATE.get(), ModItems.PINK.get()), pWriter); // Chestplate
        leggingsArmor(List.of(ModItems.PINK_LEGGINGS.get(), ModItems.PINK.get()), pWriter); // Leggings
        bootsArmor(List.of(ModItems.PINK_BOOTS.get(), ModItems.PINK.get()), pWriter); // Boots

        helmetArmor(List.of(ModItems.COPPER_HELMET.get(), Items.COPPER_INGOT), pWriter); // Helmet
        chestplateArmor(List.of(ModItems.COPPER_CHESTPLATE.get(), Items.COPPER_INGOT), pWriter); // Chestplate
        leggingsArmor(List.of(ModItems.COPPER_LEGGINGS.get(), Items.COPPER_INGOT), pWriter); // Leggings
        bootsArmor(List.of(ModItems.COPPER_BOOTS.get(), Items.COPPER_INGOT), pWriter); // Boots

        // Block -> Item and Item -> Block
        // 0 -> Result / 1 -> Ingredient
        itemTransformBlock(List.of(ModBlocks.ENDER_PEARL_BLOCK.get(), Items.ENDER_PEARL), pWriter);
        blockTransformItem(List.of(Items.ENDER_PEARL, ModBlocks.ENDER_PEARL_BLOCK.get()), pWriter);

        itemTransformBlock(List.of(ModBlocks.NETHER_STAR_BLOCK.get(), Items.NETHER_STAR), pWriter);
        blockTransformItem(List.of(Items.NETHER_STAR, ModBlocks.NETHER_STAR_BLOCK.get()), pWriter);

        itemTransformBlock(List.of(ModBlocks.ROTTEN_FLESH_BLOCK.get(), Items.ROTTEN_FLESH), pWriter);
        blockTransformItem(List.of(Items.ROTTEN_FLESH, ModBlocks.ROTTEN_FLESH_BLOCK.get()), pWriter);

        itemTransformBlock(List.of(ModBlocks.GUNPOWDER_BLOCK.get(), Items.GUNPOWDER), pWriter);
        blockTransformItem(List.of(Items.GUNPOWDER, ModBlocks.GUNPOWDER_BLOCK.get()), pWriter);

        itemTransformBlock(List.of(ModBlocks.BLAZE_ROD_BLOCK.get(), Items.BLAZE_ROD), pWriter);
        blockTransformItem(List.of(Items.BLAZE_ROD, ModBlocks.BLAZE_ROD_BLOCK.get()), pWriter);

        itemTransformBlock(List.of(ModBlocks.PHANTOM_MEMBRANE_BLOCK.get(), Items.PHANTOM_MEMBRANE), pWriter);
        blockTransformItem(List.of(Items.PHANTOM_MEMBRANE, ModBlocks.PHANTOM_MEMBRANE_BLOCK.get()), pWriter);

        // Ore
        itemTransformBlock(List.of(ModBlocks.PINK_BLOCK.get(), ModItems.PINK.get()), pWriter);
        blockTransformItem(List.of(ModItems.PINK.get(), ModBlocks.PINK_BLOCK.get()), pWriter);

        // Block -> Craft Crafting Table
        itemTransformBlock(List.of(ModBlocks.CRAFT_CRAFTING_TABLE.get(), Items.CRAFTING_TABLE), pWriter);

        // My custom ore
        // Items Smelting
        oreSmelting(pWriter, PINK_SMELTABLES, RecipeCategory.MISC, ModItems.PINK.get(),
                0.25f, 200, "pink");

        // Items Blasting
        oreBlasting(pWriter, PINK_SMELTABLES, RecipeCategory.MISC, ModItems.PINK.get(),
                0.25f, 200, "pink");

        oreSmelting(pWriter, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER,
                1.00f, 100, "rotten_flesh");
        oreBlasting(pWriter, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER,
                1.00f, 100, "rotten_flesh");

        // My custom enchanted book and item enchanted
        enchantItem(List.of(Items.ENCHANTED_BOOK, Items.OBSIDIAN, Items.BOOK),
                Map.of(Enchantments.UNBREAKING, 10), List.of("AAA", "ABA", "AAA"), List.of("A", "B"),
                true, false, 1, pWriter);

        enchantItem(List.of(Items.ENCHANTED_BOOK, Items.NETHER_STAR, Items.BOOK),
                Map.of(ModEnchantments.MORE_ORES.get(), 5, Enchantments.BLOCK_EFFICIENCY, 10),
                List.of("ABA", "AAA", "AAA"), List.of("A", "B"), true, false, 2, pWriter);

        enchantItem(List.of(Items.DIAMOND_PICKAXE, Items.COPPER_INGOT, Items.DIAMOND_PICKAXE),
                Map.of(ModEnchantments.MORE_ORES.get(), 5, Enchantments.UNBREAKING, 10,
                        Enchantments.BLOCK_EFFICIENCY, 10, Enchantments.MENDING, 1),
                List.of("AAA", "AAA", "ABA"), List.of("A", "B"), false, true, 3, pWriter);

        // Result + Secondary ingredient + Primary ingredient
        enchantItem(List.of(ModItems.PINK_MODES.get(), Items.GLOWSTONE, ModItems.PINK_MODES.get()),
                // Enchantments
                Map.of(ModEnchantments.MORE_ORES.get(), 5, Enchantments.UNBREAKING, 10,
                        Enchantments.BLOCK_EFFICIENCY, 10, Enchantments.MENDING, 1),
                // 3x3 crafting recipe + letter ingredients
                List.of("A A", " B ", "A A"), List.of("A", "B"), false, true, 4, pWriter);

        enchantItem(List.of(Items.IRON_PICKAXE, Items.IRON_INGOT, Items.IRON_PICKAXE),
                Map.of(Enchantments.UNBREAKING, 3, Enchantments.BLOCK_EFFICIENCY, 7,
                        Enchantments.BLOCK_FORTUNE, 5),
                List.of("A A", " B ", "A A"), List.of("A", "B"), false, true, 5, pWriter);

        // Modes Pickaxes custom recipes
        enchantItem(List.of(ModItems.BLUE_MODES.get(), Items.LAPIS_BLOCK, Items.NETHERITE_PICKAXE),
                Map.of(Enchantments.BLOCK_EFFICIENCY, 1, Enchantments.UNBREAKING, 1,
                        Enchantments.BLOCK_FORTUNE, 1, Enchantments.MENDING, 1),
                List.of("AAA", "ABA", "AAA"), List.of("A", "B"), false, true, 6, pWriter);

        enchantItem(List.of(ModItems.PINK_MODES.get(), ModBlocks.PINK_BLOCK.get(), ModItems.BLUE_MODES.get()),
                Map.of(Enchantments.BLOCK_EFFICIENCY, 3, Enchantments.UNBREAKING, 3,
                        Enchantments.BLOCK_FORTUNE, 3, Enchantments.MENDING, 1),
                List.of("AAA", "ABA", "AAA"), List.of("A", "B"), false, true, 7, pWriter);

        enchantItem(List.of(ModItems.GREEN_MODES.get(), Items.DIAMOND_BLOCK, ModItems.PINK_MODES.get()),
                Map.of(Enchantments.BLOCK_EFFICIENCY, 5, Enchantments.UNBREAKING, 5,
                        Enchantments.BLOCK_FORTUNE, 5, Enchantments.MENDING, 1),
                List.of("AAA", "ABA", "AAA"), List.of("A", "B"), false, true, 8, pWriter);

        enchantItem(List.of(ModItems.PURPLE_MODES.get(), Items.NETHERITE_BLOCK, ModItems.GREEN_MODES.get()),
                Map.of(Enchantments.BLOCK_EFFICIENCY, 7, Enchantments.UNBREAKING, 7,
                        Enchantments.BLOCK_FORTUNE, 7, Enchantments.MENDING, 1),
                List.of("AAA", "ABA", "AAA"), List.of("A", "B"), false, true, 9, pWriter);

        // Colored blocks
        coloredBlocks(List.of(ModBlocks.GREEN_ENDER_PEARL_BLOCK.get(), Items.GREEN_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.LIME_GREEN_ENDER_PEARL_BLOCK.get(), Items.LIME_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.MAGENTA_ENDER_PEARL_BLOCK.get(), Items.MAGENTA_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.PINK_ENDER_PEARL_BLOCK.get(), Items.PINK_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.PURPLE_ENDER_PEARL_BLOCK.get(), Items.PURPLE_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.BLACK_ENDER_PEARL_BLOCK.get(), Items.BLACK_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.BLUE_ENDER_PEARL_BLOCK.get(), Items.BLUE_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.CYAN_ENDER_PEARL_BLOCK.get(), Items.CYAN_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.GRAY_ENDER_PEARL_BLOCK.get(), Items.GRAY_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.BROWN_ENDER_PEARL_BLOCK.get(), Items.BROWN_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.YELLOW_ENDER_PEARL_BLOCK.get(), Items.YELLOW_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.WHITE_ENDER_PEARL_BLOCK.get(), Items.WHITE_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.ORANGE_ENDER_PEARL_BLOCK.get(), Items.ORANGE_DYE), pWriter);
        coloredBlocks(List.of(ModBlocks.RED_ENDER_PEARL_BLOCK.get(), Items.RED_DYE), pWriter);

        // My Disenchanted custom block
        itemTransformBlock(List.of(ModBlocks.DISENCHANTED_BLOCK.get(), Blocks.OBSIDIAN), pWriter);

        // Luck custom generator enchanted book
        luckItem(List.of(ModItems.LUCK.get(), Items.LAPIS_LAZULI, Items.COPPER_INGOT, Items.BOOK), pWriter);
        luckItem(List.of(ModItems.PICKAXE_LUCK.get(), Items.LAPIS_LAZULI, Items.DIAMOND,
                ModItems.LUCK.get()), pWriter);
        luckItem(List.of(ModItems.WEAPON_LUCK.get(), Items.LAPIS_LAZULI, Items.REDSTONE,
                ModItems.LUCK.get()), pWriter);

        // Craft Crafting Table 7x7
        // One item
        craftSeven(List.of(ModBlocks.KAUPEN_FURNACE_BLOCK.get(), Items.FURNACE), pWriter); // Kaupen Furnace
        craftSeven(List.of(ModBlocks.MCCOURSE_ELEVATOR.get(), Items.WHITE_WOOL), pWriter);
        craftSeven(List.of(ModItems.FARMER.get(), Items.BONE_MEAL), pWriter);
        craftSeven(List.of(ModItems.RESTORE.get(), Items.BOOK), pWriter); // Restore item
        craftSeven(List.of(ModBlocks.MAGIC_BOOK_BLOCK.get(), ModBlocks.MAGIC_BLOCK.get()), pWriter);
        craftSeven(List.of(ModBlocks.BOOK_DISENCHANTED_BLOCK.get(), Items.ANVIL), pWriter);

        // Two items
        craftSevenItems(List.of(ModBlocks.MCCOURSE_GENERATOR.get(), ModBlocks.CRAFT_CRAFTING_TABLE.get(),
                Items.NETHER_STAR, Items.ENCHANTED_GOLDEN_APPLE), pWriter);
        craftSevenItems(List.of(ModBlocks.GEM_EMPOWERING_STATION.get(), Items.FURNACE, ModItems.ALEXANDRITE.get(),
                Items.BOOK), pWriter); // Gem Empowering Station
        craftSevenItems(List.of(ModBlocks.MAGIC_BLOCK.get(), Items.ENCHANTING_TABLE, Items.ANVIL,
                Items.BOOK), pWriter);

        // Custom trims
        trimSmithing(pWriter, ModItems.KAUPEN_SMITHING_TEMPLATE.get(), new ResourceLocation(MCCourseMod.MOD_ID, "kaupen"));
    }

    // Smelting
    protected static void oreSmelting(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer,
                                      List<ItemLike> pIngredients, @NotNull RecipeCategory pCategory,
                                      @NotNull ItemLike pResult, float pExperience,
                                      int pCookingTIme, @NotNull String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    // Blasting
    protected static void oreBlasting(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer,
                                      List<ItemLike> pIngredients, @NotNull RecipeCategory pCategory,
                                      @NotNull ItemLike pResult, float pExperience,
                                      int pCookingTime, @NotNull String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    // Cooking -> Custom method to oreSmelting and oreBlasting
    protected static void oreCooking(@NotNull Consumer<FinishedRecipe> pFinishedRecipeConsumer,
                                     @NotNull RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer,
                                     List<ItemLike> pIngredients, @NotNull RecipeCategory pCategory,
                                     @NotNull ItemLike pResult, float pExperience, int pCookingTime,
                                     @NotNull String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime,
                            pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(pFinishedRecipeConsumer, MCCourseMod.MOD_ID + ":" + getItemName(pResult) +
                            pRecipeName + "_" + getItemName(itemlike));
        }
    }

    // My custom Recipe methods

    // Craft Crafting Table 7x7
    protected static void craftSeven(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item.get(0))
                .pattern("AAAAAAA").pattern("AAAAAAA").pattern("AAAAAAA")
                .pattern("AAAAAAA").pattern("AAAAAAA").pattern("AAAAAAA")
                .pattern("AAAAAAA")
                .define('A', item.get(1))
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    protected static void craftSevenItems(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item.get(0))
                .pattern("AAAAAAA").pattern("ABBBBBA").pattern("ABBBBBA")
                .pattern("ABBCBBA").pattern("ABBBBBA").pattern("ABBBBBA")
                .pattern("AAAAAAA")
                .define('A', item.get(1))
                .define('B', item.get(2))
                .define('C', item.get(3))
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    // Item transform on custom block
    protected static void itemTransformBlock(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item.get(0))
                .pattern("AAA").pattern("AAA").pattern("AAA")
                .define('A', item.get(1))
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    // Custom block transform on item
    protected static void blockTransformItem(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item.get(0), 9)
                .requires(item.get(1))
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    // Custom recipe
    protected static void pickaxeHammerItem(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        // Hammer and Pickaxe tools
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                .pattern("AAA").pattern(" B ").pattern(" B ")
                .define('A', item.get(1)).define('B', Items.STICK)
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    protected static void helmetArmor(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        // Helmet
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                .pattern("AAA").pattern("A A").pattern("   ")
                .define('A', item.get(1))
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    protected static void chestplateArmor(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        // Chestplate
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                .pattern("A A").pattern("AAA").pattern("AAA")
                .define('A', item.get(1))
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    protected static void leggingsArmor(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        // Leggings
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                .pattern("AAA").pattern("A A").pattern("A A")
                .define('A', item.get(1))
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    protected static void bootsArmor(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        // Boots
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                .pattern("   ").pattern("A A").pattern("A A")
                .define('A', item.get(1))
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    protected static void shovelItem(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        // Shovel
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                .pattern(" A ").pattern(" B ").pattern(" B ")
                .define('A', item.get(1)).define('B', Items.STICK)
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    protected static void axeItem(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        // Axe
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                .pattern("AA ").pattern("AB ").pattern(" B ")
                .define('A', item.get(1)).define('B', Items.STICK)
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    protected static void paxelItem(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        // Paxel
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, item.get(0), 1)
                .requires(item.get(1)).requires(item.get(2)).requires(item.get(3))
                .unlockedBy("has_item", has(item.get(1))).save(pWriter);
    }

    protected static void swordItem(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        // Paxel
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                .pattern(" A ").pattern(" A ").pattern(" B ")
                .define('A', item.get(1)).define('B', Items.STICK)
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    protected static void hoeItem(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        // Paxel
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                .pattern("AA ").pattern(" B ").pattern(" B ")
                .define('A', item.get(1)).define('B', Items.STICK)
                .unlockedBy("has_item", has(item.get(1)))
                .save(pWriter);
    }

    // Custom color blocks
    public static void coloredBlocks(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item.get(0), 1)
                .requires(item.get(1)).requires(ModBlocks.ENDER_PEARL_BLOCK.get())
                .unlockedBy("has_item", has(item.get(1))).save(pWriter);
    }

    // Luck custom items
    public static void luckItem(List<ItemLike> item, Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item.get(0), 1)
                .pattern("ABA").pattern("BCB").pattern("ABA")
                .define('A', item.get(1)).define('B', item.get(2)).define('C', item.get(3))
                .unlockedBy("has_item", has(item.get(3))).save(pWriter);
    }

    // Custom enchanted item or enchanted book
    public static void enchantItem(List<ItemLike> result, Map<Enchantment, Integer> enchantments,
                                   List<String> format, List<String> letters, boolean isBook,
                                   boolean unbreakable, int number, Consumer<FinishedRecipe> writer) {
        // Registry item = Result Index 0
        JsonObject resultJson = new JsonObject();
        resultJson.addProperty("item",
                Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.get(0).asItem())).toString());
        resultJson.addProperty("count", 1);

        // Registry enchantments
        JsonObject nbt = new JsonObject();
        JsonArray enchantmentArray = new JsonArray();

        // Sorts enchantments by level and then by ID
        enchantments.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry<Enchantment, Integer>::getValue) // Enchantment level
                        // Enchantment name
                        .thenComparing(e ->
                                Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(e.getKey())).toString()))
                .forEach(entry -> {
                    JsonObject enchantmentTag = new JsonObject();
                    enchantmentTag.addProperty("id",
                            Objects.requireNonNull(ForgeRegistries.ENCHANTMENTS.getKey(entry.getKey())).toString());
                    enchantmentTag.addProperty("lvl", entry.getValue());
                    enchantmentArray.add(enchantmentTag);
                });

        nbt.add(isBook ? "StoredEnchantments" : "Enchantments", enchantmentArray);

        // Unbreakable tag
        if (unbreakable) {
            nbt.addProperty("Unbreakable", 1);
        }

        resultJson.add("nbt", nbt);

        // Registry recipe
        JsonObject recipeJson = new JsonObject();
        recipeJson.addProperty("type", "minecraft:crafting_shaped");

        JsonArray pattern = new JsonArray();
        for (String s : format) { pattern.add(s); }
        recipeJson.add("pattern", pattern);

        JsonObject key = new JsonObject();

        // Registry ingredients = Result Index 1 and 2
        JsonObject aKey = new JsonObject();
        JsonObject bKey = new JsonObject();

        List<JsonObject> jsonObjectList = List.of(aKey, bKey);

        for (int i = 0; i < letters.size(); i++) {
            jsonObjectList.get(i).addProperty("item",
                    Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.get(i+1).asItem())).toString());
            key.add(letters.get(i), jsonObjectList.get(i));
        }

        recipeJson.add("key", key);
        recipeJson.add("result", resultJson);

        // Registry JSON file
        writer.accept(new FinishedRecipe() {
            @Override
            public void serializeRecipeData(@NotNull JsonObject jsonObject) {
                List<String> recipe = List.of("type", "pattern", "key", "result");
                for (String rec : recipe) { jsonObject.add(rec, recipeJson.get(rec)); }
            }

            @Override
            public @NotNull ResourceLocation getId() {
                return new ResourceLocation(MCCourseMod.MOD_ID, number + "_enchanted");
            }

            @Override
            public @NotNull RecipeSerializer<?> getType() { return RecipeSerializer.SHAPED_RECIPE; }

            @Override
            public JsonObject serializeAdvancement() {
                JsonObject advancement = new JsonObject();
                advancement.addProperty("parent", "minecraft:recipes/root");
                JsonObject criteria = new JsonObject();
                JsonObject trigger = new JsonObject();
                trigger.addProperty("trigger", "minecraft:inventory_changed");
                JsonObject conditions = new JsonObject();
                JsonArray items = new JsonArray();
                JsonObject itemObject = new JsonObject();
                // Item to unlock on Recipe Book
                itemObject.addProperty("item",
                        Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(result.get(2).asItem())).toString());
                items.add(itemObject);
                conditions.add("items", items);
                trigger.add("conditions", conditions);
                criteria.add("has_item", trigger);
                advancement.add("criteria", criteria);
                JsonObject rewards = new JsonObject();
                JsonArray recipes = new JsonArray();
                recipes.add(getId().toString());
                rewards.add("recipes", recipes);
                advancement.add("rewards", rewards);
                return advancement;
            }

            @Override
            public ResourceLocation getAdvancementId() {
                return new ResourceLocation(MCCourseMod.MOD_ID, "recipes/custom/" + number + "_enchanted");
            }
        });
    }
}