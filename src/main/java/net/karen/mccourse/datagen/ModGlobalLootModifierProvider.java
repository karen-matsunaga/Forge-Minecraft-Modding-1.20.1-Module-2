package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.loot.AddItemModifier;
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
    public ModGlobalLootModifierProvider(PackOutput output) {
        super(output, MCCourseMod.MOD_ID);
    }

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

        // Added custom ores loot modifier
        add("diamond_ore_from_diamond_ore", new AddItemModifier(new LootItemCondition[] { // Diamond ore
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DIAMOND_ORE).build(), // Adding the block mined of player
                LootItemRandomChanceCondition.randomChance(1.00f).build() }, // Adding random chance of item drop
                List.of(Items.DIAMOND))); // Adding new blocks or items drop in list

        add("emerald_ore_from_emerald_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.EMERALD_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.EMERALD))); // Emerald ore

        add("iron_ore_from_iron_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.IRON_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.IRON_INGOT, Items.RAW_IRON))); // Iron ore

        add("gold_ore_from_gold_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.GOLD_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.GOLD_INGOT, Items.RAW_GOLD))); // Gold ore

        add("lapis_ore_from_lapis_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.LAPIS_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.LAPIS_LAZULI))); // Lapis Lazuli ore

        add("redstone_ore_from_redstone_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.REDSTONE_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.REDSTONE))); // Redstone ore

        add("copper_ore_from_copper_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.COPPER_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.COPPER_INGOT, Items.RAW_COPPER))); // Copper ore

        add("coal_ore_from_coal_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.COAL_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.COAL))); // Coal ore

        add("ancient_debris_from_ancient_debris", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.ANCIENT_DEBRIS).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.NETHERITE_SCRAP))); // Ancient Debris

        add("nether_quartz_ore_from_nether_quartz_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.NETHER_QUARTZ_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.QUARTZ))); // Nether Quartz Ore

        add("nether_gold_ore_from_nether_gold_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.NETHER_GOLD_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.GOLD_NUGGET, Items.RAW_GOLD))); // Nether Gold Ore

        add("deepslate_diamond_ore_from_deepslate_diamond_ore", new AddItemModifier(new LootItemCondition[] { // Diamond ore
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_DIAMOND_ORE).build(), // Adding the block mined of player
                LootItemRandomChanceCondition.randomChance(1.00f).build() }, // Adding random chance of item drop
                List.of(Items.DIAMOND))); // Adding new blocks or items drop in list

        add("deepslate_emerald_ore_from_deepslate_emerald_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_EMERALD_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.EMERALD))); // Emerald ore

        add("deepslate_iron_ore_from_deepslate_iron_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_IRON_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.IRON_INGOT, Items.RAW_IRON))); // Iron ore

        add("deepslate_gold_ore_from_deepslate_gold_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_GOLD_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.GOLD_INGOT, Items.RAW_GOLD))); // Gold ore

        add("deepslate_lapis_ore_from_deepslate_lapis_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_LAPIS_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.LAPIS_LAZULI))); // Lapis Lazuli ore

        add("deepslate_redstone_ore_from_deepslate_redstone_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_REDSTONE_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.REDSTONE))); // Redstone ore

        add("deepslate_copper_ore_from_deepslate_copper_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_COPPER_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.COPPER_INGOT, Items.RAW_COPPER))); // Copper ore

        add("deepslate_coal_ore_from_deepslate_coal_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DEEPSLATE_COAL_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Items.COAL))); // Coal ore


    }
}
