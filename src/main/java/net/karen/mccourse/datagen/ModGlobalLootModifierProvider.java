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
import java.util.Map;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output) { super(output, MCCourseMod.MOD_ID); }

    private final Map<String, String> locations = Map.ofEntries(
            // CHESTS
            // Abandoned mine
            Map.entry("_from_abandoned_mineshaft", "chests/abandoned_mineshaft"),

            // Ancient City
            Map.entry("_from_ancient_city", "chests/ancient_city"),
            Map.entry("_from_ancient_city_ice_box", "chests/ancient_city_ice_box"),

            // Bastion
            Map.entry("_from_bastion_bridge", "chests/bastion_bridge"),
            Map.entry("_from_bastion_hoglin_stable", "chests/bastion_hoglin_stable"),
            Map.entry("_from_bastion_other", "chests/bastion_other"),
            Map.entry("_from_bastion_treasure", "chests/bastion_treasure"),

            // Nether
            Map.entry("_from_nether_bridge", "chests/nether_bridge"),

            // Beach
            Map.entry("_from_buried_treasure", "chests/buried_treasure"),

            // Desert
            Map.entry("_from_desert_pyramid", "chests/desert_pyramid"),

            // End
            Map.entry("_from_end_city_treasure", "chests/end_city_treasure"),
            Map.entry("_from_igloo_chest", "chests/igloo_chest"),

            // Jungle
            Map.entry("_from_jungle_temple", "chests/jungle_temple"),
            Map.entry("_from_jungle_temple_dispenser", "chests/jungle_temple_dispenser"),

            // Pillager mansion
            Map.entry("_from_pillager_outpost", "chests/pillager_outpost"),
            Map.entry("_from_woodland_mansion", "chests/woodland_mansion"),

            // Ruined portal
            Map.entry("_from_ruined_portal", "chests/ruined_portal"),

            // Shipwreck
            Map.entry("_from_shipwreck_map", "chests/shipwreck_map"),
            Map.entry("_from_shipwreck_supply", "chests/shipwreck_supply"),
            Map.entry("_from_shipwreck_treasure", "chests/shipwreck_treasure"),

            // Dungeon
            Map.entry("_from_simple_dungeon", "chests/simple_dungeon"),

            // Chest option
            Map.entry("_from_spawn_bonus_chest", "chests/spawn_bonus_chest"),

            // Stronghold
            Map.entry("_from_stronghold_corridor", "chests/stronghold_corridor"),
            Map.entry("_from_stronghold_crossing", "chests/stronghold_crossing"),
            Map.entry("_from_stronghold_library", "chests/stronghold_library"),

            // Underwater
            Map.entry("_from_underwater_ruin_big", "chests/underwater_ruin_big"),
            Map.entry("_from_underwater_ruin_small", "chests/underwater_ruin_small"),

            // CHESTS/VILLAGE
            Map.entry("_from_village_armorer", "chests/village/village_armorer"),
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
            Map.entry("_from_village_weaponsmith", "chests/village/village_weaponsmith")
    );

    // Registry all blocks or items with custom loot modifiers
    @Override
    protected void start() {
        // Added vanilla or custom loot modifiers on items or blocks

        // JSON file name, BLOCK type, Random chance to receive item, List of items
        addDropWithBlock("kohlrabi_seeds_from_grass", Blocks.GRASS, 0.35f,
                List.of(ModItems.KOHLRABI_SEEDS.get())); // Grass block
        addDropWithBlock("kohlrabi_seeds_from_fern", Blocks.FERN, 0.35f,
                List.of(ModItems.KOHLRABI_SEEDS.get())); // Fern block

        // Added item or block on structure
        addDropOnStructure("metal_detector_from_jungle_temple",
                0.25f, List.of(ModItems.METAL_DETECTOR.get()));

        // Added custom ores loot modifier
        addDropWithBlock("coal_from_coal_ore", Blocks.COAL_ORE, 1.00f,
                List.of(Items.COAL));

        addDropWithBlock("coal_from_deepslate_coal_ore", Blocks.DEEPSLATE_COAL_ORE, 1.00f,
                List.of(Items.COAL));

        // Special Metal Detector
        addDropOnStructure("special_metal_detector_from_jungle_temple",
                0.25f, List.of(ModItems.SPECIAL_METAL_DETECTOR.get())); // Jungle Temple

        // Orange Modes
        addDropOnStructure("orange_modes_from_ancient_city",
                0.50f, List.of(ModItems.ORANGE_MODES.get())); // Ancient City

        addDropOnStructure("orange_modes_from_jungle_temple",
                0.25f, List.of(ModItems.ORANGE_MODES.get())); // Jungle Temple

        addDropOnStructure("orange_modes_from_simple_dungeon",
                0.40f, List.of(ModItems.ORANGE_MODES.get())); // Simple Dungeon

        addDropOnStructure("orange_modes_from_abandoned_mineshaft",
                0.60f, List.of(ModItems.ORANGE_MODES.get())); // Abandoned Mineshaft

        addDropOnStructure("orange_modes_from_bastion_bridge",
                0.20f, List.of(ModItems.ORANGE_MODES.get())); // Bastion Bridge

        addDropOnStructure("orange_modes_from_bastion_hoglin_stable",
                0.40f, List.of(ModItems.ORANGE_MODES.get())); // Bastion Hoglin Stable

        addDropOnStructure("orange_modes_from_bastion_other",
                0.30f, List.of(ModItems.ORANGE_MODES.get())); // Bastion Other

        addDropOnStructure("orange_modes_from_bastion_treasure",
                0.10f, List.of(ModItems.ORANGE_MODES.get())); // Bastion Treasure

        // Added custom loot table on SUSPICIOUS SAND
        add("metal_detector_from_suspicious_sand", new AddSusSandItemModifier(new LootItemCondition[] {
                new LootTableIdCondition.Builder(
                        new ResourceLocation("archaeology/desert_pyramid")).build() },
                ModItems.METAL_DETECTOR.get()));
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
    private void addDropOnStructure(String name, float chance, List<Item> drops) {
        for (Map.Entry<String, String> local : locations.entrySet()) {
            if (name.contains(local.getKey())) {
                LootItemCondition[] conditions = new LootItemCondition[] {
                        new LootTableIdCondition.Builder(new ResourceLocation(local.getValue())).build(),
                        LootItemRandomChanceCondition.randomChance(chance).build()
                };
                add(name, new AddItemModifier(conditions, drops));
            }
        }
    }
}