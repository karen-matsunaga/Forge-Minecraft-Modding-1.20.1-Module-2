package net.karen.mccourse.datagen;

import com.google.gson.*;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.datagen.custom.GemEmpoweringRecipeBuilder;
import net.karen.mccourse.datagen.custom.KaupenFurnaceRecipeBuilder;
import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.util.ModTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
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
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    // All items smelting and blasting
    private static final List<ItemLike> ALEXANDRITE_SMELTABLES =
            List.of(ModItems.RAW_ALEXANDRITE.get(), ModBlocks.ALEXANDRITE_ORE.get(), ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(),
                    ModBlocks.END_STONE_ALEXANDRITE_ORE.get(), ModBlocks.NETHER_ALEXANDRITE_ORE.get());

    private static final List<ItemLike> PINK_SMELTABLES =
            List.of(ModItems.PINK.get(), ModBlocks.PINK_ORE.get(), ModBlocks.DEEPSLATE_PINK_ORE.get(),
                    ModBlocks.END_STONE_PINK_ORE.get(), ModBlocks.NETHER_PINK_ORE.get());

    public ModRecipeProvider(PackOutput output) { super(output); }

    // DEFAULT METHOD - Create ALL custom recipes
    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> writer) {
        // Walnut log
        logBlocks(List.of(ModBlocks.WALNUT_PLANKS.get(), ModBlocks.WALNUT_WOOD.get(), ModBlocks.WALNUT_LOG.get(),
                          ModItems.WALNUT_BOAT.get(), ModItems.WALNUT_CHEST_BOAT.get(), ModItems.WALNUT_SIGN.get(),
                          ModItems.WALNUT_HANGING_SIGN.get(), ModBlocks.STRIPPED_WALNUT_LOG.get(),
                          ModBlocks.STRIPPED_WALNUT_WOOD.get()),
                  ModTags.Items.WALNUT_LOGS, writer);

        // Mccourse glass
        glassBlocks(List.of(ModBlocks.MCCOURSE_GLASS_BLOCK.get(), ModBlocks.MCCOURSE_GLASS_PANE_BLOCK.get(), Items.LIME_DYE), writer);

        // Gem Empowering Station custom recipes
        gemEmpoweringStation(ModItems.RAW_ALEXANDRITE.get(), ModItems.ALEXANDRITE.get(), 3, 160, 50,
                             new FluidStack(Fluids.WATER, 2000), writer);
        gemEmpoweringStation(Items.COAL, Items.DIAMOND, 7, 40, 150,
                             new FluidStack(Fluids.LAVA, 500), writer);
        gemEmpoweringStation(Items.COBBLESTONE, Items.OBSIDIAN, 1, 100, 200,
                             new FluidStack(Fluids.LAVA, 1000), writer);

        // Kaupen Furnace custom recipes
        kaupenFurnace(Items.IRON_INGOT, Items.RAW_IRON, 0.5f, 50, writer);
        kaupenFurnace(Items.COAL, Items.DIAMOND, 0.5f, 50, writer);
        kaupenFurnace(Items.BONE_MEAL, Items.PHANTOM_MEMBRANE, 10.0f, 100, writer);

        // Hammer and Pickaxe custom recipes
        pickaxeHammerItem(List.of(ModItems.ALEXANDRITE_HAMMER.get(), ModBlocks.ALEXANDRITE_BLOCK.get()), writer); // Hammer
        pickaxeHammerItem(List.of(ModItems.COPPER_HAMMER.get(), Items.COPPER_BLOCK), writer);
        pickaxeHammerItem(List.of(ModItems.DIAMOND_HAMMER.get(), Items.DIAMOND_BLOCK), writer);
        pickaxeHammerItem(List.of(ModItems.GOLD_HAMMER.get(), Items.GOLD_BLOCK), writer);
        pickaxeHammerItem(List.of(ModItems.IRON_HAMMER.get(), Items.IRON_BLOCK), writer);
        pickaxeHammerItem(List.of(ModItems.NETHERITE_HAMMER.get(), Items.NETHERITE_BLOCK), writer);
        pickaxeHammerItem(List.of(ModItems.PINK_HAMMER.get(), ModBlocks.PINK_BLOCK.get()), writer);
        pickaxeHammerItem(List.of(ModItems.WOODEN_HAMMER.get(), Items.OAK_LOG), writer);
        pickaxeHammerItem(List.of(ModItems.STONE_HAMMER.get(), Items.STONE), writer);
        pickaxeHammerItem(List.of(ModItems.ALEXANDRITE_PICKAXE.get(), ModItems.ALEXANDRITE.get()), writer); // Pickaxes
        pickaxeHammerItem(List.of(ModItems.PINK_PICKAXE.get(), ModItems.PINK.get()), writer);
        pickaxeHammerItem(List.of(ModItems.COPPER_PICKAXE.get(), Items.COPPER_INGOT), writer);

        // Sword custom recipes
        swordItem(List.of(ModItems.ALEXANDRITE_SWORD.get(), ModItems.ALEXANDRITE.get()), writer);
        swordItem(List.of(ModItems.PINK_SWORD.get(), ModItems.PINK.get()), writer);
        swordItem(List.of(ModItems.COPPER_SWORD.get(), Items.COPPER_INGOT), writer);

        // Axe custom recipes
        axeItem(List.of(ModItems.ALEXANDRITE_AXE.get(), ModItems.ALEXANDRITE.get()), writer);
        axeItem(List.of(ModItems.PINK_AXE.get(), ModItems.PINK.get()), writer);
        axeItem(List.of(ModItems.COPPER_AXE.get(), Items.COPPER_INGOT), writer);

        // Shovel custom recipes
        shovelItem(List.of(ModItems.ALEXANDRITE_SHOVEL.get(), ModItems.ALEXANDRITE.get()), writer);
        shovelItem(List.of(ModItems.PINK_SHOVEL.get(), ModItems.PINK.get()), writer);
        shovelItem(List.of(ModItems.COPPER_SHOVEL.get(), Items.COPPER_INGOT), writer);

        // Paxel custom recipes
        paxelItem(List.of(ModItems.ALEXANDRITE_PAXEL.get(), ModItems.ALEXANDRITE_PICKAXE.get(),
                  ModItems.ALEXANDRITE_AXE.get(), ModItems.ALEXANDRITE_SHOVEL.get()), writer);
        paxelItem(List.of(ModItems.PINK_PAXEL.get(), ModItems.PINK_PICKAXE.get(),
                  ModItems.PINK_AXE.get(), ModItems.PINK_SHOVEL.get()), writer);
        paxelItem(List.of(ModItems.COPPER_PAXEL.get(), ModItems.COPPER_PICKAXE.get(),
                  ModItems.COPPER_AXE.get(), ModItems.COPPER_SHOVEL.get()), writer);
        paxelItem(List.of(ModItems.DIAMOND_PAXEL.get(), Items.DIAMOND_PICKAXE, Items.DIAMOND_AXE, Items.DIAMOND_SHOVEL), writer);
        paxelItem(List.of(ModItems.GOLD_PAXEL.get(), Items.GOLDEN_PICKAXE, Items.GOLDEN_AXE, Items.GOLDEN_SHOVEL), writer);
        paxelItem(List.of(ModItems.IRON_PAXEL.get(), Items.IRON_PICKAXE, Items.IRON_AXE, Items.IRON_SHOVEL), writer);
        paxelItem(List.of(ModItems.STONE_PAXEL.get(), Items.STONE_PICKAXE, Items.STONE_AXE, Items.STONE_SHOVEL), writer);
        paxelItem(List.of(ModItems.WOODEN_PAXEL.get(), Items.WOODEN_PICKAXE, Items.WOODEN_AXE, Items.WOODEN_SHOVEL), writer);
        paxelItem(List.of(ModItems.NETHERITE_PAXEL.get(), Items.NETHERITE_PICKAXE,
                  Items.NETHERITE_AXE, Items.NETHERITE_SHOVEL), writer);

        // Hoe custom recipes
        hoeItem(List.of(ModItems.ALEXANDRITE_HOE.get(), ModItems.ALEXANDRITE.get()), writer);
        hoeItem(List.of(ModItems.PINK_HOE.get(), ModItems.PINK.get()), writer);
        hoeItem(List.of(ModItems.COPPER_HOE.get(), Items.COPPER_INGOT), writer);

        // Armor custom recipes
        helmetArmor(List.of(ModItems.ALEXANDRITE_HELMET.get(), ModItems.ALEXANDRITE.get()), writer); // Alexandrite helmet
        helmetArmor(List.of(ModItems.PINK_HELMET.get(), ModItems.PINK.get()), writer); // Pink helmet
        helmetArmor(List.of(ModItems.COPPER_HELMET.get(), Items.COPPER_INGOT), writer); // Copper helmet
        chestplateArmor(List.of(ModItems.ALEXANDRITE_CHESTPLATE.get(), ModItems.ALEXANDRITE.get()), writer); // Alexandrite chestplate
        chestplateArmor(List.of(ModItems.PINK_CHESTPLATE.get(), ModItems.PINK.get()), writer); // Pink chestplate
        chestplateArmor(List.of(ModItems.COPPER_CHESTPLATE.get(), Items.COPPER_INGOT), writer); // Copper chestplate
        leggingsArmor(List.of(ModItems.ALEXANDRITE_LEGGINGS.get(), ModItems.ALEXANDRITE.get()), writer); // Alexandrite leggings
        leggingsArmor(List.of(ModItems.PINK_LEGGINGS.get(), ModItems.PINK.get()), writer); // Pink leggings
        leggingsArmor(List.of(ModItems.COPPER_LEGGINGS.get(), Items.COPPER_INGOT), writer); // Copper leggings
        bootsArmor(List.of(ModItems.ALEXANDRITE_BOOTS.get(), ModItems.ALEXANDRITE.get()), writer); // Alexandrite boots
        bootsArmor(List.of(ModItems.PINK_BOOTS.get(), ModItems.PINK.get()), writer); // Pink boots
        bootsArmor(List.of(ModItems.COPPER_BOOTS.get(), Items.COPPER_INGOT), writer); // Copper boots

        // One block TRANSFORM nine items and Nine items TRANSFORM one Block -> 0 = Block | 1 = Item
        itemTransformBlock(List.of(ModBlocks.ALEXANDRITE_BLOCK.get(), ModItems.ALEXANDRITE.get()), writer);
        itemTransformBlock(List.of(ModBlocks.RAW_ALEXANDRITE_BLOCK.get(), ModItems.RAW_ALEXANDRITE.get()), writer);
        itemTransformBlock(List.of(ModBlocks.ENDER_PEARL_BLOCK.get(), Items.ENDER_PEARL), writer);
        itemTransformBlock(List.of(ModBlocks.NETHER_STAR_BLOCK.get(), Items.NETHER_STAR), writer);
        itemTransformBlock(List.of(ModBlocks.ROTTEN_FLESH_BLOCK.get(), Items.ROTTEN_FLESH), writer);
        itemTransformBlock(List.of(ModBlocks.GUNPOWDER_BLOCK.get(), Items.GUNPOWDER), writer);
        itemTransformBlock(List.of(ModBlocks.BLAZE_ROD_BLOCK.get(), Items.BLAZE_ROD), writer);
        itemTransformBlock(List.of(ModBlocks.PHANTOM_MEMBRANE_BLOCK.get(), Items.PHANTOM_MEMBRANE), writer);
        itemTransformBlock(List.of(ModBlocks.STRING_BLOCK.get(), Items.STRING), writer);
        itemTransformBlock(List.of(ModBlocks.SPIDER_EYE_BLOCK.get(), Items.SPIDER_EYE), writer);
        itemTransformBlock(List.of(ModBlocks.FERMENTED_SPIDER_EYE_BLOCK.get(), Items.FERMENTED_SPIDER_EYE), writer);
        itemTransformBlock(List.of(ModBlocks.SUGAR_BLOCK.get(), Items.SUGAR), writer);
        itemTransformBlock(List.of(ModBlocks.SUGAR_CANE_BLOCK.get(), Items.SUGAR_CANE), writer);
        itemTransformBlock(List.of(ModBlocks.PINK_BLOCK.get(), ModItems.PINK.get()), writer); // Pink ore
        itemTransformBlock(List.of(ModBlocks.CRAFT_CRAFTING_TABLE.get(), Items.CRAFTING_TABLE), writer); // Craft Crafting Table
        itemTransformBlock(List.of(ModItems.GROWTH.get(), Items.CARROT), writer); // Growth
        itemTransformBlock(List.of(ModBlocks.DISENCHANTED_BLOCK.get(), Blocks.OBSIDIAN), writer); // Disenchanted

        // Smelting + Blasting items custom recipes
        oreSmeltingBlasting(writer, ALEXANDRITE_SMELTABLES, RecipeCategory.MISC, ModItems.ALEXANDRITE.get(),
                  0.25f, 200, "alexandrite");
        oreSmeltingBlasting(writer, PINK_SMELTABLES, RecipeCategory.MISC, ModItems.PINK.get(),
                  0.25f, 200, "pink");
        oreSmeltingBlasting(writer, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER,
                  1.00f, 100, "rotten_flesh");

        // Enchanted book and item enchanted custom recipes
        enchantItem(List.of(Items.ENCHANTED_BOOK, Items.OBSIDIAN, Items.BOOK),
                    Map.of(Enchantments.UNBREAKING, 10), List.of("AAA", "ABA", "AAA"), List.of("A", "B"),
                    true, false, 1, writer);
        enchantItem(List.of(Items.ENCHANTED_BOOK, Items.NETHER_STAR, Items.BOOK),
                    Map.of(ModEnchantments.MORE_ORES.get(), 5, Enchantments.BLOCK_EFFICIENCY, 10),
                    List.of("ABA", "AAA", "AAA"), List.of("A", "B"), true, false, 2, writer);
        enchantItem(List.of(Items.DIAMOND_PICKAXE, Items.COPPER_INGOT, Items.DIAMOND_PICKAXE),
                    Map.of(ModEnchantments.MORE_ORES.get(), 5, Enchantments.UNBREAKING, 10,
                           Enchantments.BLOCK_EFFICIENCY, 10, Enchantments.MENDING, 1),
                    List.of("AAA", "AAA", "ABA"), List.of("A", "B"), false, true, 3, writer);
        enchantItem( // Result + Secondary ingredient + Primary ingredient
                    List.of(ModItems.PINK_MODES.get(), Items.GLOWSTONE, ModItems.PINK_MODES.get()),
                    Map.of(ModEnchantments.MORE_ORES.get(), 5, Enchantments.UNBREAKING, 10,
                           Enchantments.BLOCK_EFFICIENCY, 10, Enchantments.MENDING, 1), // Enchantments
                    // 3x3 crafting recipe + letter ingredients
                    List.of("A A", " B ", "A A"), List.of("A", "B"), false, true, 4, writer);
        enchantItem(List.of(Items.IRON_PICKAXE, Items.IRON_INGOT, Items.IRON_PICKAXE),
                    Map.of(Enchantments.UNBREAKING, 3, Enchantments.BLOCK_EFFICIENCY, 7,
                           Enchantments.BLOCK_FORTUNE, 5),
                    List.of("A A", " B ", "A A"), List.of("A", "B"), false, true, 5, writer);
        // Modes Pickaxes custom recipes
        enchantItem(List.of(ModItems.BLUE_MODES.get(), Items.LAPIS_BLOCK, Items.NETHERITE_PICKAXE),
                    Map.of(Enchantments.BLOCK_EFFICIENCY, 1, Enchantments.UNBREAKING, 1,
                           Enchantments.BLOCK_FORTUNE, 1, Enchantments.MENDING, 1),
                    List.of("AAA", "ABA", "AAA"), List.of("A", "B"), false, true, 6, writer);
        enchantItem(List.of(ModItems.PINK_MODES.get(), ModBlocks.PINK_BLOCK.get(), ModItems.BLUE_MODES.get()),
                    Map.of(Enchantments.BLOCK_EFFICIENCY, 3, Enchantments.UNBREAKING, 3,
                           Enchantments.BLOCK_FORTUNE, 3, Enchantments.MENDING, 1),
                    List.of("AAA", "ABA", "AAA"), List.of("A", "B"), false, true, 7, writer);
        enchantItem(List.of(ModItems.GREEN_MODES.get(), Items.DIAMOND_BLOCK, ModItems.PINK_MODES.get()),
                    Map.of(Enchantments.BLOCK_EFFICIENCY, 5, Enchantments.UNBREAKING, 5,
                           Enchantments.BLOCK_FORTUNE, 5, Enchantments.MENDING, 1),
                    List.of("AAA", "ABA", "AAA"), List.of("A", "B"), false, true, 8, writer);
        enchantItem(List.of(ModItems.PURPLE_MODES.get(), Items.NETHERITE_BLOCK, ModItems.GREEN_MODES.get()),
                    Map.of(Enchantments.BLOCK_EFFICIENCY, 7, Enchantments.UNBREAKING, 7,
                           Enchantments.BLOCK_FORTUNE, 7, Enchantments.MENDING, 1),
                    List.of("AAA", "ABA", "AAA"), List.of("A", "B"), false, true, 9, writer);
        // Miner helmet
        enchantItem(List.of(ModItems.MINER_HELMET.get(), Items.TORCH, Items.COPPER_BLOCK),
                    Map.of(ModEnchantments.GLOWING_MOBS.get(), 1), List.of("AAA", "ABA", "AAA"), List.of("A", "B"),
                    false, true, 10, writer);

        // Colored blocks
        coloredBlocks(List.of(ModBlocks.GREEN_ENDER_PEARL_BLOCK.get(), Items.GREEN_DYE), writer);
        coloredBlocks(List.of(ModBlocks.LIME_GREEN_ENDER_PEARL_BLOCK.get(), Items.LIME_DYE), writer);
        coloredBlocks(List.of(ModBlocks.MAGENTA_ENDER_PEARL_BLOCK.get(), Items.MAGENTA_DYE), writer);
        coloredBlocks(List.of(ModBlocks.PINK_ENDER_PEARL_BLOCK.get(), Items.PINK_DYE), writer);
        coloredBlocks(List.of(ModBlocks.PURPLE_ENDER_PEARL_BLOCK.get(), Items.PURPLE_DYE), writer);
        coloredBlocks(List.of(ModBlocks.BLACK_ENDER_PEARL_BLOCK.get(), Items.BLACK_DYE), writer);
        coloredBlocks(List.of(ModBlocks.BLUE_ENDER_PEARL_BLOCK.get(), Items.BLUE_DYE), writer);
        coloredBlocks(List.of(ModBlocks.CYAN_ENDER_PEARL_BLOCK.get(), Items.CYAN_DYE), writer);
        coloredBlocks(List.of(ModBlocks.GRAY_ENDER_PEARL_BLOCK.get(), Items.GRAY_DYE), writer);
        coloredBlocks(List.of(ModBlocks.BROWN_ENDER_PEARL_BLOCK.get(), Items.BROWN_DYE), writer);
        coloredBlocks(List.of(ModBlocks.YELLOW_ENDER_PEARL_BLOCK.get(), Items.YELLOW_DYE), writer);
        coloredBlocks(List.of(ModBlocks.WHITE_ENDER_PEARL_BLOCK.get(), Items.WHITE_DYE), writer);
        coloredBlocks(List.of(ModBlocks.ORANGE_ENDER_PEARL_BLOCK.get(), Items.ORANGE_DYE), writer);
        coloredBlocks(List.of(ModBlocks.RED_ENDER_PEARL_BLOCK.get(), Items.RED_DYE), writer);

        // Luck custom generator enchanted book
        luckItem(List.of(ModItems.LUCK.get(), Items.LAPIS_LAZULI, Items.COPPER_INGOT, Items.BOOK), writer);
        luckItem(List.of(ModItems.PICKAXE_LUCK.get(), Items.LAPIS_LAZULI, Items.DIAMOND, ModItems.LUCK.get()), writer);
        luckItem(List.of(ModItems.WEAPON_LUCK.get(), Items.LAPIS_LAZULI, Items.REDSTONE, ModItems.LUCK.get()), writer);

        // Craft Crafting Table 7x7 - (One item)
        craftSeven(List.of(ModBlocks.KAUPEN_FURNACE_BLOCK.get(), Items.FURNACE), writer); // Kaupen Furnace
        craftSeven(List.of(ModBlocks.MCCOURSE_ELEVATOR.get(), Items.WHITE_WOOL), writer);
        craftSeven(List.of(ModItems.FARMER.get(), Items.BONE_MEAL), writer);
        craftSeven(List.of(ModItems.RESTORE.get(), Items.BOOK), writer); // Restore item
        craftSeven(List.of(ModBlocks.MAGIC_ENCHANTED_BLOCK.get(), Items.ENCHANTING_TABLE), writer);
        craftSeven(List.of(ModBlocks.MAGIC_DISENCHANTED_BLOCK.get(), ModBlocks.MAGIC_DISENCHANTED_BOOK_BLOCK.get()), writer);
        craftSeven(List.of(ModBlocks.MAGIC_DISENCHANTED_BOOK_BLOCK.get(), Items.ANVIL), writer);
        // Craft Crafting Table 7x7 - (Three items)
        craftSevenItems(List.of(ModBlocks.MCCOURSE_GENERATOR.get(), ModBlocks.CRAFT_CRAFTING_TABLE.get(),
                                Items.NETHER_STAR, Items.ENCHANTED_GOLDEN_APPLE), writer);
        craftSevenItems(List.of(ModBlocks.GEM_EMPOWERING_STATION.get(), Items.FURNACE, ModItems.ALEXANDRITE.get(),
                                Items.BOOK), writer); // Gem Empowering Station

        // Custom trims
        trimSmithing(writer, ModItems.KAUPEN_SMITHING_TEMPLATE.get(), new ResourceLocation(MCCourseMod.MOD_ID, "kaupen"));
    }

    // CUSTOM METHOD - Gem Empowering Station custom recipes
    protected static void gemEmpoweringStation(ItemLike ingredient, ItemLike result, int count,
                                               int craftTime, int energyAmount, FluidStack fluidStack,
                                               Consumer<FinishedRecipe> writer) {
        new GemEmpoweringRecipeBuilder(ingredient, result, count, craftTime, energyAmount, fluidStack)
                                      .unlockedBy("has_item", has(result)).save(writer);
    }

    // CUSTOM METHOD - Kaupen Furnace custom recipes
    protected static void kaupenFurnace(ItemLike ingredient, ItemLike result, float experience,
                                        int cookingTime, Consumer<FinishedRecipe> writer) {
        new KaupenFurnaceRecipeBuilder(ingredient, result, experience, cookingTime)
                                      .unlockedBy("has_item", has(result)).save(writer);
    }

    // CUSTOM METHOD - Smelting + Blasting custom recipes
    protected static void oreSmeltingBlasting(@NotNull Consumer<FinishedRecipe> writer,
                                              List<ItemLike> item, @NotNull RecipeCategory category, @NotNull ItemLike result,
                                              float experience, int cookingTime, @NotNull String group) {
        // SMELTING
        oreCooking(writer, RecipeSerializer.SMELTING_RECIPE, item, category, result, experience,
                   cookingTime, group, "_from_smelting");
        // BLASTING
        oreCooking(writer, RecipeSerializer.BLASTING_RECIPE, item, category, result, experience,
                   cookingTime, group, "_from_blasting");
    }

    // CUSTOM METHOD - oreSmelting and oreBlasting CUSTOM METHODS
    protected static void oreCooking(@NotNull Consumer<FinishedRecipe> writer,
                                     @NotNull RecipeSerializer<? extends AbstractCookingRecipe> serializer,
                                     List<ItemLike> items, @NotNull RecipeCategory category, @NotNull ItemLike result,
                                     float experience, int cookingTime, @NotNull String group, String recipeName) {
        items.forEach(item ->
             SimpleCookingRecipeBuilder.generic(Ingredient.of(item), category, result, experience, cookingTime, serializer)
                                       .group(group).unlockedBy(getHasName(item), has(item))
                                       .save(writer, MCCourseMod.MOD_ID + ":" + getItemName(result) + recipeName + "_" +
                                             getItemName(item)));
    }

    // CUSTOM METHOD - Craft Crafting Table 7x7 (One item)
    protected static void craftSeven(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item.get(0))
                           .pattern("AAAAAAA").pattern("AAAAAAA").pattern("AAAAAAA")
                           .pattern("AAAAAAA").pattern("AAAAAAA").pattern("AAAAAAA")
                           .pattern("AAAAAAA")
                           .define('A', item.get(1))
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - Craft Crafting Table 7x7 (Three items)
    protected static void craftSevenItems(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item.get(0))
                           .pattern("AAAAAAA").pattern("ABBBBBA").pattern("ABBBBBA")
                           .pattern("ABBCBBA").pattern("ABBBBBA").pattern("ABBBBBA")
                           .pattern("AAAAAAA")
                           .define('A', item.get(1))
                           .define('B', item.get(2))
                           .define('C', item.get(3))
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - ITEM transform on custom block || BLOCK transform on item
    protected static void itemTransformBlock(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        // BLOCK
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item.get(0)) // BLOCK result
                           .pattern("AAA").pattern("AAA").pattern("AAA")
                           .define('A', item.get(1)) // ITEM input
                           .unlockedBy("has_item", has(item.get(1))) // Has ITEM
                           .save(writer);
        // ITEM
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, item.get(1), 9) // ITEM result
                              .requires(item.get(0)) // BLOCK input
                              .unlockedBy("has_item", has(item.get(0))) // Has BLOCK
                              .save(writer);
    }

    // CUSTOM METHOD - Hammer and Pickaxe tools custom recipes
    protected static void pickaxeHammerItem(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                           .pattern("AAA").pattern(" B ").pattern(" B ")
                           .define('A', item.get(1)).define('B', Items.STICK)
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - Helmet custom recipes
    protected static void helmetArmor(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                           .pattern("AAA").pattern("A A").pattern("   ")
                           .define('A', item.get(1))
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - Chestplate custom recipes
    protected static void chestplateArmor(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                           .pattern("A A").pattern("AAA").pattern("AAA")
                           .define('A', item.get(1))
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - Leggings custom recipes
    protected static void leggingsArmor(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                           .pattern("AAA").pattern("A A").pattern("A A")
                           .define('A', item.get(1))
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - Boots custom recipes
    protected static void bootsArmor(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                           .pattern("   ").pattern("A A").pattern("A A")
                           .define('A', item.get(1))
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - Shovel custom recipes
    protected static void shovelItem(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                           .pattern(" A ").pattern(" B ").pattern(" B ")
                           .define('A', item.get(1)).define('B', Items.STICK)
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - Axe custom recipes
    protected static void axeItem(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                           .pattern("AA ").pattern("AB ").pattern(" B ")
                           .define('A', item.get(1)).define('B', Items.STICK)
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - Paxel custom recipes
    protected static void paxelItem(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, item.get(0), 1)
                              .requires(item.get(1)).requires(item.get(2)).requires(item.get(3))
                              .unlockedBy("has_item", has(item.get(1))).save(writer);
    }

    // CUSTOM METHOD - Sword custom recipes
    protected static void swordItem(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                           .pattern(" A ").pattern(" A ").pattern(" B ")
                           .define('A', item.get(1)).define('B', Items.STICK)
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - Hoe custom recipes
    protected static void hoeItem(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, item.get(0), 1)
                           .pattern("AA ").pattern(" B ").pattern(" B ")
                           .define('A', item.get(1)).define('B', Items.STICK)
                           .unlockedBy("has_item", has(item.get(1)))
                           .save(writer);
    }

    // CUSTOM METHOD - Color blocks custom recipes
    protected static void coloredBlocks(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item.get(0), 1)
                              .requires(item.get(1)).requires(ModBlocks.ENDER_PEARL_BLOCK.get())
                              .unlockedBy("has_item", has(item.get(1))).save(writer);
    }

    // CUSTOM METHOD - Log blocks custom recipes
    protected static void logBlocks(List<ItemLike> items, TagKey<Item> itemTag,
                                    Consumer<FinishedRecipe> writer) {
        // 0 -> PLANKS; 1 -> WOOD; 2 -> LOG; 3 -> BOAT; 4 -> CHEST BOAT; 5 -> SIGN;
        // 6 -> HANGING SIGN; 7 -> STRIPPED OAK; 8 -> STRIPPED WOOD.
        planksFromLog(writer, items.get(0), itemTag, 4); // Planks block
        woodFromLogs(writer, items.get(1), items.get(2)); // Wood block
        woodFromLogs(writer, items.get(8), items.get(7)); // Wood block
        woodenBoat(writer, items.get(3), items.get(0)); // Boat block
        chestBoat(writer, items.get(4), items.get(3)); // Chest boat block
        signBuilder(items.get(5), Ingredient.of(items.get(0)))
                         .unlockedBy("has_item", has(items.get(3))).save(writer); // Sign block
        hangingSign(writer, items.get(6), items.get(7)); // Hanging sing block
    }

    // CUSTOM METHOD - Glass blocks custom recipes
    protected static void glassBlocks(List<ItemLike> items, Consumer<FinishedRecipe> writer) {
        // 0 -> GLASS; 1 -> GLASS PANE; 2 -> DYE COLOR.
        stainedGlassFromGlassAndDye(writer, items.get(0), items.get(2));
        stainedGlassPaneFromGlassPaneAndDye(writer, items.get(1), items.get(2));
        stainedGlassPaneFromStainedGlass(writer, items.get(1), items.get(0));
    }

    // CUSTOM METHOD - Luck items custom recipes
    protected static void luckItem(List<ItemLike> item, Consumer<FinishedRecipe> writer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, item.get(0), 1)
                           .pattern("ABA").pattern("BCB").pattern("ABA")
                           .define('A', item.get(1)).define('B', item.get(2)).define('C', item.get(3))
                           .unlockedBy("has_item", has(item.get(3))).save(writer);
    }

    // CUSTOM METHOD - Enchanted items or enchanted books
    protected static void enchantItem(List<ItemLike> result, Map<Enchantment, Integer> map,
                                      List<String> format, List<String> letters, boolean isBook, boolean unbreakable,
                                      int number, Consumer<FinishedRecipe> writer) {
        String MOD_ID = MCCourseMod.MOD_ID, location = number + "_enchanted";
        IForgeRegistry<Item> item = ForgeRegistries.ITEMS;
        ResourceLocation result0 = item.getKey(result.get(0).asItem()), result2 = item.getKey(result.get(2).asItem());
        IForgeRegistry<Enchantment> enchant = ForgeRegistries.ENCHANTMENTS;
        JsonObject resultJson = new JsonObject(); // Registry item = Result Index 0
        if (result0 != null) { resultJson.addProperty("item", result0.toString()); }
        resultJson.addProperty("count", 1);
        JsonObject nbt = new JsonObject(); // Registry enchantments
        JsonArray enchantmentArray = new JsonArray();
        map.entrySet().stream() // Sorts enchantments by level and then by ID
                    .sorted(Comparator.comparingInt(Map.Entry<Enchantment, Integer>::getValue) // Enchantment level
                    .thenComparing(e -> { ResourceLocation key = enchant.getKey(e.getKey());
                                          return key != null ? key.toString() : null; })) // Enchantment name
                    .forEach(entry -> { JsonObject enchantmentTag = new JsonObject();
                                        ResourceLocation key = enchant.getKey(entry.getKey());
                                        enchantmentTag.addProperty("id", key != null ? key.toString() : null);
                                        enchantmentTag.addProperty("lvl", entry.getValue());
                                        enchantmentArray.add(enchantmentTag); });
        nbt.add(isBook ? "StoredEnchantments" : "Enchantments", enchantmentArray);
        if (unbreakable) { nbt.addProperty("Unbreakable", 1); } // Unbreakable tag
        resultJson.add("nbt", nbt);
        JsonObject recipeJson = new JsonObject(); // Registry recipe
        recipeJson.addProperty("type", "minecraft:crafting_shaped");
        JsonArray pattern = new JsonArray();
        for (String s : format) { pattern.add(s); }
        recipeJson.add("pattern", pattern);
        // Registry ingredients = Result Index 1 and 2
        JsonObject key = new JsonObject(), aKey = new JsonObject(), bKey = new JsonObject();
        List<JsonObject> jsonObjectList = List.of(aKey, bKey);
        for (int i = 0; i < letters.size(); i++) {
            jsonObjectList.get(i).addProperty("item", Objects.requireNonNull(item.getKey(result.get(i+1).asItem())).toString());
            key.add(letters.get(i), jsonObjectList.get(i));
        }
        recipeJson.add("key", key);
        recipeJson.add("result", resultJson);
        writer.accept(new FinishedRecipe() { // Registry JSON file
            @Override
            public void serializeRecipeData(@NotNull JsonObject jsonObject) {
                List<String> recipe = List.of("type", "pattern", "key", "result");
                for (String rec : recipe) { jsonObject.add(rec, recipeJson.get(rec)); }
            }

            @Override
            public @NotNull ResourceLocation getId() { return new ResourceLocation(MOD_ID, location); }

            @Override
            public @NotNull RecipeSerializer<?> getType() { return RecipeSerializer.SHAPED_RECIPE; }

            @Override
            public JsonObject serializeAdvancement() {
                JsonObject advancement = new JsonObject();
                advancement.addProperty("parent", "minecraft:recipes/root");
                JsonObject criteria = new JsonObject(), trigger = new JsonObject();
                trigger.addProperty("trigger", "minecraft:inventory_changed");
                JsonObject conditions = new JsonObject();
                JsonArray items = new JsonArray();
                JsonObject itemObject = new JsonObject();
                itemObject.addProperty("item", result2 != null ? result2.toString() : null); // Item to unlock on Recipe Book
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
            public ResourceLocation getAdvancementId() { return new ResourceLocation(MOD_ID, "recipes/custom/" + location); }
        });
    }
}