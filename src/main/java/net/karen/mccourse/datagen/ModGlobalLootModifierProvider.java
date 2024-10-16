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

        // Added custom loot modifier
        add("diamond_ore_from_diamond_ore", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.DIAMOND_ORE).build(),
                LootItemRandomChanceCondition.randomChance(1.00f).build() },
                List.of(Blocks.DIAMOND_ORE.asItem(), Items.DIAMOND)));

        // Added custom loot modifier
        add("ancient_debris_from_ancient_debris", new AddItemModifier(new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.ANCIENT_DEBRIS).build(), // Adding the block mined of player
                LootItemRandomChanceCondition.randomChance(1.00f).build() }, // Adding random chance of item drop
                List.of(Blocks.ANCIENT_DEBRIS.asItem(), Items.NETHERITE_SCRAP))); // Adding new items blocks in list

    }
}
