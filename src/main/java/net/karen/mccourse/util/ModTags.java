package net.karen.mccourse.util;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    // Items
    public static class Items {
        // Created Item's tags HERE
        // Ores item
        public static final TagKey<Item> MCCOURSE_ITEMS = tag("mccourse_items");

        // Created Mccourse ores tags
        public static final TagKey<Item> PINK_ORES_ITEMS = tag("pink_ores_items");
        public static final TagKey<Item> ALEXANDRITE_ORES_ITEMS = tag("alexandrite_ores_items");
        public static final TagKey<Item> MCCOURSE_ORES_ITEMS = tag("mccourse_ores_items");

        // Created Fly effect item tag
        public static final TagKey<Item> HELMET_FLY = tag("helmet_fly");
        public static final TagKey<Item> CHESTPLATE_FLY = tag("chestplate_fly");
        public static final TagKey<Item> LEGGINGS_FLY = tag("leggings_fly");
        public static final TagKey<Item> BOOTS_FLY = tag("boots_fly");

        // Restore item blacklist
        public static final TagKey<Item> RESTORE_BLACKLIST_ITEMS = tag("restore_blacklist_items");

        // Custom armors
        public static final TagKey<Item> ALEXANDRITE_ARMOR = tag("alexandrite_armor");
        public static final TagKey<Item> COPPER_ARMOR = tag("copper_armor");
        public static final TagKey<Item> PINK_ARMOR = tag("pink_armor");

        // Ores
        public static final TagKey<Item> MULTIPLIER_ORES = tag("multiplier_ores");

        // Ore block items
        public static final TagKey<Item> ORE_BLOCK_ITEMS = tag("ore_block_items");

        // Teleport item
        public static final TagKey<Item> TELEPORT_ITEMS = tag("teleport_items");

        // Ultra Compactor item
        public static final TagKey<Item> ULTRA_COMPACTOR_ITEMS = tag("ultra_compactor_items");
        public static final TagKey<Item> ULTRA_COMPACTOR_RESULT = tag("ultra_compactor_result");

        // Pink Ultra Compactor item
        public static final TagKey<Item> PINK_ULTRA_COMPACTOR_ITEMS = tag("pink_ultra_compactor_items");
        public static final TagKey<Item> PINK_ULTRA_COMPACTOR_RESULT = tag("pink_ultra_compactor_result");

        // Level Charger items
        public static final TagKey<Item> LEVEL_CHARGER_GENERAL = tag("level_charger_general");
        public static final TagKey<Item> LEVEL_CHARGER_SPECIFIC = tag("level_charger_specific");
        public static final TagKey<Item> LEVEL_CHARGER_PLUS_ENCHANT = tag("level_charger_plus_enchant");
        public static final TagKey<Item> LEVEL_CHARGER_MINUS_ENCHANT = tag("level_charger_minus_enchant");
        public static final TagKey<Item> LEVEL_CHARGER_ALL = tag("level_charger_all");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(MCCourseMod.MOD_ID, name)); // Pass item id
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name)); // Pass item id in Forge
        }
    }

    // Blocks
    public static class Blocks {
        // Created Block's tags HERE
        // Created METAL DETECTOR's tag
        public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");
        public static final TagKey<Block> SPECIAL_METAL_DETECTOR_VALUABLES =
                tag("special_metal_detector_valuables");
        public static final TagKey<Block> METAL_DETECTOR_COLORS = tag("metal_detector_colors");

        // Created Alexandrite Tool Level's tag
        public static final TagKey<Block> NEEDS_ALEXANDRITE_TOOL = tag("needs_alexandrite_tool");
        public static final TagKey<Block> NEEDS_PINK_TOOL = tag("needs_pink_tool");
        public static final TagKey<Block> NEEDS_COPPER_TOOL = tag("needs_copper_tool");
        public static final TagKey<Block> NEEDS_LAPIS_LAZULI_TOOL = tag("needs_lapis_lazuli_tool");
        public static final TagKey<Block> NEEDS_REDSTONE_TOOL = tag("needs_redstone_tool");

        // Created Alexandrite Paxel's tag
        public static final TagKey<Block> PAXEL_MINEABLE = tag("mineable/paxel");

        // Created More Ores's drops - More Ores I, II, III, IV, V, VI, Max Level
        public static final TagKey<Block> MORE_ORES_ONE_DROPS = tag("more_ores_one_drops");
        public static final TagKey<Block> MORE_ORES_TWO_DROPS = tag("more_ores_two_drops");
        public static final TagKey<Block> MORE_ORES_THREE_DROPS = tag("more_ores_three_drops");
        public static final TagKey<Block> MORE_ORES_FOUR_DROPS = tag("more_ores_four_drops");
        public static final TagKey<Block> MORE_ORES_FIVE_DROPS = tag("more_ores_five_drops");
        public static final TagKey<Block> MORE_ORES_SIX_DROPS = tag("more_ores_six_drops");
        public static final TagKey<Block> MORE_ORES_ALL_DROPS = tag("more_ores_all_drops");

        // Created More Ores's alternative drops - Modes Pickaxe's More Ores mode
        public static final TagKey<Block> MORE_ORES_MODES_PICKAXE_DROPS = tag("more_ores_modes_pickaxe_drops");

        // Created Mobs blocks tags
        public static final TagKey<Block> MOBS_BLOCKS_DROPS = tag("mobs_blocks_drops");

        // Created Mccourse ores tags
        public static final TagKey<Block> PINK_ORES = tag("pink_ores");
        public static final TagKey<Block> ALEXANDRITE_ORES = tag("alexandrite_ores");
        public static final TagKey<Block> MCCOURSE_ORES_BLOCK = tag("mccourse_ores_block");
        public static final TagKey<Block> MCCOURSE_ORES = tag("mccourse_ores");

        // Created Farmer tag
        public static final TagKey<Block> FARMER_BONEMEAL_GROWABLES = tag("farmer_bonemeal_growables");
        public static final TagKey<Block> FARMER_CROPS_GROWABLES = tag("farmer_crops_growables");
        public static final TagKey<Block> FARMER_TREE_GROWABLES = tag("farmer_tree_growables");
        public static final TagKey<Block> FARMER_VERTICAL_GROWABLES = tag("farmer_vertical_growables");
        public static final TagKey<Block> FARMER_AGE_GROWABLES = tag("farmer_age_growables");

        // Crop replant tag
        public static final TagKey<Block> MUSHROOM_BLOCKS = tag("mushroom_blocks");
        public static final TagKey<Block> VERTICAL_BLOCKS = tag("vertical_blocks");
        public static final TagKey<Block> VERTICAL_GROW_BLOCKS = tag("vertical_grow_blocks");

        // Ores
        public static final TagKey<Block> ALL_ORES = tag("all_ores");

        // Block Fly enchantment
        public static final TagKey<Block> BLOCK_FLY_BLOCK_SPEED = tag("block_fly_block_speed");

        // Accumulator enchantment
        public static final TagKey<Block> ACCUMULATOR_EXPERIENCE = tag("accumulator_experience");

        // Rainbow enchantment
        public static final TagKey<Block> RAINBOW_DROPS = tag("rainbow_drops");

        // Miner Bow
        public static final TagKey<Block> MINER_BOW_BLACKLIST = tag("miner_bow_blacklist");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(MCCourseMod.MOD_ID, name)); // Pass block id
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name)); // Pass block id in Forge
        }
    }

    // Entities
    public static class Entities {
        // Created Entities's tags HERE
        public static final TagKey<EntityType<?>> MCCOURSE_ENTITIES = tag("mccourse_entities");
        public static final TagKey<EntityType<?>> BOSSES = tag("bosses");
        public static final TagKey<EntityType<?>> NETHER = tag("nether");
        public static final TagKey<EntityType<?>> END = tag("end");
        public static final TagKey<EntityType<?>> OVERWORLD = tag("overworld");
        public static final TagKey<EntityType<?>> MONSTERS = tag("monsters");
        public static final TagKey<EntityType<?>> ANIMALS = tag("animals");
        public static final TagKey<EntityType<?>> VILLAGER = tag("villager");
        public static final TagKey<EntityType<?>> WATER_ANIMALS = tag("water_animals");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MCCourseMod.MOD_ID, name)); // Pass entities id
        }

        // Pass entities id in Forge
        private static TagKey<EntityType<?>> forgeTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge", name));
        }
    }
}