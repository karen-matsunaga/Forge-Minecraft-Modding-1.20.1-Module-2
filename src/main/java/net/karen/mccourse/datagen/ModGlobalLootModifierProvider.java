package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.loot.AddItemModifier;
import net.karen.mccourse.loot.AddSusSandItemModifier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
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

        // JSON file name, BLOCK type, Random chance to receive item, List of items
        addDropWithBlock("kohlrabi_seeds_from_grass", Blocks.GRASS, 0.35f,
                List.of(ModItems.KOHLRABI_SEEDS.get()));
        addDropWithBlock("kohlrabi_seeds_from_fern", Blocks.FERN, 0.35f,
                List.of(ModItems.KOHLRABI_SEEDS.get()));

        // Added item or block on structure
        addDropOnStructure("metal_detector_from_jungle_temple", "chests/jungle_temple",
                List.of(ModItems.METAL_DETECTOR.get()));

        // Added custom suspicious sand
        add("metal_detector_from_suspicious_sand", new AddSusSandItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/desert_pyramid")).build() },
                ModItems.METAL_DETECTOR.get()));

        // Added custom ores loot modifier
        addDropWithBlock("coal_from_coal_ore", Blocks.COAL_ORE, 1.00f,
                List.of(Items.COAL));

        addDropWithBlock("coal_from_deepslate_coal_ore", Blocks.DEEPSLATE_COAL_ORE, 1.00f,
                List.of(Items.COAL));

        // Special Metal Detector
        addDropOnStructure("special_metal_detector_from_jungle_temple", "chests/jungle_temple",
                List.of(ModItems.SPECIAL_METAL_DETECTOR.get())); // Jungle Temple
    }

    // CUSTOM METHOD - Global Loot Modifiers

    // Item on block
    private void addDropWithBlock(String name, Block block, float chance, List<Item> drops) {
        LootItemCondition[] conditions = new LootItemCondition[] {
                LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build(),
                LootItemRandomChanceCondition.randomChance(chance).build()
        };
        add(name, new AddItemModifier(conditions, drops));
    }

    // Item on structure
    private void addDropOnStructure(String name, String location, List<Item> drops) {
        LootItemCondition[] conditions = new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation(location)).build()
        };
        add(name, new AddItemModifier(conditions, drops));
    }
}