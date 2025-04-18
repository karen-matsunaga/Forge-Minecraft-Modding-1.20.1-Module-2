package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.loot.AddItemModifier;
import net.karen.mccourse.loot.AddSusSandItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.util.List;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output) { super(output, MCCourseMod.MOD_ID); }

    // Registry all blocks or items with custom loot modifiers
    @Override
    protected void start() {
        // Added vanilla or custom loot modifiers on items or blocks
        add("kohlrabi_seeds_from_grass", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GRASS).build(),
                LootItemRandomChanceCondition.randomChance(0.35f).build() }, List.of(ModItems.KOHLRABI_SEEDS.get())));

        add("kohlrabi_seeds_from_fern", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.FERN).build(),
                LootItemRandomChanceCondition.randomChance(0.35f).build() }, List.of(ModItems.KOHLRABI_SEEDS.get())));

        // Added item or block on structure
        add("metal_detector_from_jungle_temple", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/jungle_temple")).build() },
                List.of(ModItems.METAL_DETECTOR.get())));

        // Added custom suspicious sand
        add("metal_detector_from_suspicious_sand", new AddSusSandItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/desert_pyramid")).build() },
                ModItems.METAL_DETECTOR.get()));

        // Added custom ores loot modifier
        add("iron_ingot_from_iron_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.IRON_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.IRON_INGOT, Items.RAW_IRON))); // Iron ore

        add("gold_ingot_from_gold_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GOLD_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.GOLD_INGOT, Items.RAW_GOLD))); // Gold ore

        add("lapis_lazuli_from_lapis_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.LAPIS_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.LAPIS_LAZULI))); // Lapis Lazuli ore

        add("coal_from_coal_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.COAL_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.COAL))); // Coal ore

        add("quartz_from_nether_quartz_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.NETHER_QUARTZ_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.QUARTZ))); // Nether Quartz Ore

        add("raw_gold_from_nether_gold_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.NETHER_GOLD_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.GOLD_NUGGET, Items.RAW_GOLD))); // Nether Gold Ore

        add("iron_ingot_from_deepslate_iron_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_IRON_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.IRON_INGOT, Items.RAW_IRON))); // Iron ore

        add("gold_ingot_from_deepslate_gold_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_GOLD_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.GOLD_INGOT, Items.RAW_GOLD))); // Gold ore

        add("lapis_lazuli_from_deepslate_lapis_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_LAPIS_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.LAPIS_LAZULI))); // Lapis Lazuli ore

        add("coal_from_deepslate_coal_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_COAL_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.COAL))); // Coal ore

        // My custom blocks - Ender Pearl's variants
        add("ender_pearl_and_green_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.GREEN_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.GREEN_DYE)));

        add("ender_pearl_and_lime_green_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.LIME_GREEN_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.LIME_DYE)));

        add("ender_pearl_and_magenta_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.MAGENTA_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.MAGENTA_DYE)));

        add("ender_pearl_and_pink_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.PINK_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.PINK_DYE)));

        add("ender_pearl_and_purple_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.PURPLE_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.PURPLE_DYE)));

        add("ender_pearl_and_black_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BLACK_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.BLACK_DYE)));

        add("ender_pearl_and_blue_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BLUE_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.BLUE_DYE)));

        add("ender_pearl_and_cyan_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.CYAN_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.CYAN_DYE)));

        add("ender_pearl_and_gray_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.GRAY_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.GRAY_DYE)));

        add("ender_pearl_and_brown_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.BROWN_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.BROWN_DYE)));

        add("ender_pearl_and_yellow_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.YELLOW_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.YELLOW_DYE)));

        add("ender_pearl_and_white_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.WHITE_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.WHITE_DYE)));

        add("ender_pearl_and_orange_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.ORANGE_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.ORANGE_DYE)));

        add("ender_pearl_and_red_dye_from_green_ender_pearl_block", new AddItemModifier(new LootItemCondition[]{
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.RED_ENDER_PEARL_BLOCK.get()).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(ModBlocks.ENDER_PEARL_BLOCK.get().asItem(), Items.RED_DYE)));

        // My custom items - Mining Modes Pickaxe
        add("orange_modes_from_jungle_temple", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/jungle_temple")).build() },
                List.of(ModItems.ORANGE_MODES.get()))); // Jungle Temple

        add("orange_modes_from_ancient_city", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/ancient_city")).build() },
                List.of(ModItems.ORANGE_MODES.get()))); // Ancient City

        add("orange_modes_from_bastion_bridge", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/bastion_bridge")).build() },
                List.of(ModItems.ORANGE_MODES.get()))); // Bastion Bridge

        add("orange_modes_from_bastion_hoglin_stable", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/bastion_hoglin_stable")).build() },
                List.of(ModItems.ORANGE_MODES.get()))); // Bastion Hoglin Stable

        add("orange_modes_from_bastion_bastion_other", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/bastion_bastion_other")).build() },
                List.of(ModItems.ORANGE_MODES.get()))); // Bastion Other

        add("orange_modes_from_bastion_bastion_treasure", new AddItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("chests/bastion_treasure")).build() },
                List.of(ModItems.ORANGE_MODES.get()))); // Bastion Treasure
    }
}