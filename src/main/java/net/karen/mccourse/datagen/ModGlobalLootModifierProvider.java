package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.loot.*;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;
import java.util.*;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output) { super(output, MCCourseMod.MOD_ID); }

    private final Map<String, String> locations = Map.ofEntries( // CHESTS
            Map.entry("_from_abandoned_mineshaft", "chests/abandoned_mineshaft"), // Abandoned mine
            Map.entry("_from_ancient_city", "chests/ancient_city"),
            Map.entry("_from_ancient_city_ice_box", "chests/ancient_city_ice_box"), // Ancient City
            Map.entry("_from_bastion_bridge", "chests/bastion_bridge"),
            Map.entry("_from_bastion_hoglin_stable", "chests/bastion_hoglin_stable"),
            Map.entry("_from_bastion_other", "chests/bastion_other"),
            Map.entry("_from_bastion_treasure", "chests/bastion_treasure"), // Bastion
            Map.entry("_from_nether_bridge", "chests/nether_bridge"), // Nether
            Map.entry("_from_buried_treasure", "chests/buried_treasure"), // Beach
            Map.entry("_from_desert_pyramid", "chests/desert_pyramid"), // Desert
            Map.entry("_from_end_city_treasure", "chests/end_city_treasure"),
            Map.entry("_from_igloo_chest", "chests/igloo_chest"), // End
            Map.entry("_from_jungle_temple", "chests/jungle_temple"),
            Map.entry("_from_jungle_temple_dispenser", "chests/jungle_temple_dispenser"), // Jungle
            Map.entry("_from_pillager_outpost", "chests/pillager_outpost"),
            Map.entry("_from_woodland_mansion", "chests/woodland_mansion"), // Pillager mansion
            Map.entry("_from_ruined_portal", "chests/ruined_portal"), // Ruined portal
            Map.entry("_from_shipwreck_map", "chests/shipwreck_map"),
            Map.entry("_from_shipwreck_supply", "chests/shipwreck_supply"),
            Map.entry("_from_shipwreck_treasure", "chests/shipwreck_treasure"), // Shipwreck
            Map.entry("_from_simple_dungeon", "chests/simple_dungeon"), // Dungeon
            Map.entry("_from_spawn_bonus_chest", "chests/spawn_bonus_chest"), // Chest option
            Map.entry("_from_stronghold_corridor", "chests/stronghold_corridor"),
            Map.entry("_from_stronghold_crossing", "chests/stronghold_crossing"),
            Map.entry("_from_stronghold_library", "chests/stronghold_library"), // Stronghold
            Map.entry("_from_underwater_ruin_big", "chests/underwater_ruin_big"),
            Map.entry("_from_underwater_ruin_small", "chests/underwater_ruin_small"), // Underwater
            Map.entry("_from_village_armorer", "chests/village/village_armorer"), // VILLAGE
            Map.entry("_from_village_butcher", "chests/village/village_butcher"),
            Map.entry("_from_village_cartographer", "chests/village/village_cartographer"),
            Map.entry("_from_village_desert_house", "chests/village/village_desert_house"),
            Map.entry("_from_village_fisher", "chests/village/village_fisher"),
            Map.entry("_from_village_fletcher", "chests/village/village_fletcher"),
            Map.entry("_from_village_mason", "chests/village/village_mason"),
            Map.entry("_from_village_plains_house", "chests/village/village_plains_house"),
            Map.entry("_from_village_savanna_house", "chests/village/village_savanna_house"),
            Map.entry("_from_village_shepherd", "chests/village/village_shepherd"),
            Map.entry("_from_village_snowy_house", "chests/village/village_snowy_house"),
            Map.entry("_from_village_taiga_house", "chests/village/village_taiga_house"),
            Map.entry("_from_village_tannery", "chests/village/village_tannery"),
            Map.entry("_from_village_temple", "chests/village/village_temple"),
            Map.entry("_from_village_toolsmith", "chests/village/village_toolsmith"),
            Map.entry("_from_village_weaponsmith", "chests/village/village_weaponsmith"));

    private final List<Item> pick = List.of(ModItems.MCCOURSE_HAMMER.get()), seeds = List.of(ModItems.KOHLRABI_SEEDS.get()),
                             coal = List.of(Items.COAL), metal = List.of(ModItems.METAL_DETECTOR.get()),
                             special = List.of(ModItems.SPECIAL_METAL_DETECTOR.get());

    // Registry all blocks or items with custom loot modifiers
    @Override
    protected void start() { // Added vanilla or custom loot modifiers on items or blocks
        // JSON file name, BLOCK type, Random chance to receive item, List of items
        addDropWithBlock("kohlrabi_seeds_from_grass", Blocks.GRASS, 0.35f, seeds); // Grass block
        addDropWithBlock("kohlrabi_seeds_from_fern", Blocks.FERN, 0.35f, seeds); // Fern block
        addDropOnStructure("metal_detector_from_jungle_temple", 0.25f, metal); // Added item or block on structure
        // Added custom ores loot modifier
        addDropWithBlock("coal_from_coal_ore", Blocks.COAL_ORE, 1.00f, coal);
        addDropWithBlock("coal_from_deepslate_coal_ore", Blocks.DEEPSLATE_COAL_ORE, 1.00f, coal);
        // Special Metal Detector on Jungle Temple
        addDropOnStructure("special_metal_detector_from_jungle_temple", 0.25f, special);
        // Mccourse Hammer
        addDropOnStructure("mccourse_hammer_from_ancient_city", 0.50f, pick); // Ancient City
        addDropOnStructure("mccourse_hammer_from_jungle_temple", 0.25f, pick); // Jungle Temple
        addDropOnStructure("mccourse_hammer_from_simple_dungeon", 0.40f, pick); // Simple Dungeon
        addDropOnStructure("mccourse_hammer_from_abandoned_mineshaft", 0.60f, pick); // Abandoned Mineshaft
        addDropOnStructure("mccourse_hammer_from_bastion_bridge", 0.20f, pick); // Bastion Bridge
        addDropOnStructure("mccourse_hammer_from_bastion_hoglin_stable", 0.40f, pick); // Bastion Hoglin Stable
        addDropOnStructure("mccourse_hammer_from_bastion_other", 0.30f, pick); // Bastion Other
        addDropOnStructure("mccourse_hammer_from_bastion_treasure", 0.10f, pick); // Bastion Treasure
        // Added custom loot table on SUSPICIOUS SAND
        add("metal_detector_from_suspicious_sand", new AddSusSandItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(new ResourceLocation("archaeology/desert_pyramid")).build() },
                ModItems.METAL_DETECTOR.get()));
    }

    // CUSTOM METHODS - Global Loot Modifiers

    // CUSTOM METHOD - Item on broken BLOCK
    private void addDropWithBlock(String name, Block block, float chance, List<Item> drops) {
        LootItemCondition[] conditions = new LootItemCondition[] {
                                         LootItemBlockStatePropertyCondition.hasBlockStateProperties(block).build(),
                                         LootItemRandomChanceCondition.randomChance(chance).build() };
        add(name, new AddItemModifier(conditions, drops));
    }

    // CUSTOM METHOD - Item on STRUCTURE
    private void addDropOnStructure(String name, float chance, List<Item> drops) {
        for (Map.Entry<String, String> local : locations.entrySet()) {
            if (name.contains(local.getKey())) {
                LootItemCondition[] conditions = new LootItemCondition[] {
                                                 new LootTableIdCondition.Builder(new ResourceLocation(local.getValue())).build(),
                                                 LootItemRandomChanceCondition.randomChance(chance).build() };
                add(name, new AddItemModifier(conditions, drops));
            }
        }
    }
}