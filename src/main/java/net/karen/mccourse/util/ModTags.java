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
        public static final TagKey<Item> MCCOURSE_ITEMS = tag("blocks/mccourse_items");

        // Created Mccourse ores tags
        public static final TagKey<Item> PINK_ORES_ITEMS = tag("blocks/pink_ores_items");
        public static final TagKey<Item> ALEXANDRITE_ORES_ITEMS = tag("blocks/alexandrite_ores_items");
        public static final TagKey<Item> MCCOURSE_ORES_ITEMS = tag("blocks/mccourse_ores_items");

        // Created Fly effect item tag
        public static final TagKey<Item> HELMET_FLY = tag("items/helmet_fly");
        public static final TagKey<Item> CHESTPLATE_FLY = tag("items/chestplate_fly");
        public static final TagKey<Item> LEGGINGS_FLY = tag("items/leggings_fly");
        public static final TagKey<Item> BOOTS_FLY = tag("items/boots_fly");

        // Restore item blacklist
        public static final TagKey<Item> RESTORE_BLACKLIST_ITEMS = tag("items/restore_blacklist_items");

        // Custom armors
        public static final TagKey<Item> ALEXANDRITE_ARMOR = tag("items/alexandrite_armor");
        public static final TagKey<Item> COPPER_ARMOR = tag("items/copper_armor");
        public static final TagKey<Item> PINK_ARMOR = tag("items/pink_armor");

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

        // Created Alexandrite Paxel's tag
        public static final TagKey<Block> PAXEL_MINEABLE = tag("mineable/paxel");

        // Created More Ores's drops - More Ores I, II, III, IV, V
        public static final TagKey<Block> MORE_ORES_ONE_DROPS = tag("ores/more_ores_one_drops");
        public static final TagKey<Block> MORE_ORES_TWO_DROPS = tag("ores/more_ores_two_drops");
        public static final TagKey<Block> MORE_ORES_THREE_DROPS = tag("ores/more_ores_three_drops");
        public static final TagKey<Block> MORE_ORES_FOUR_DROPS = tag("ores/more_ores_four_drops");
        public static final TagKey<Block> MORE_ORES_FIVE_DROPS = tag("ores/more_ores_five_drops");

        // Created More Ores's alternative drops - Modes Pickaxe's More Ores mode
        public static final TagKey<Block> MORE_ORES_MODES_PICKAXE_DROPS =
                tag("ores/more_ores_modes_pickaxe_drops");

        // Created Mobs blocks tags
        public static final TagKey<Block> MOBS_BLOCKS_DROPS = tag("blocks/mobs_blocks_drops");

        // Created Mccourse ores tags
        public static final TagKey<Block> PINK_ORES = tag("blocks/pink_ores");
        public static final TagKey<Block> ALEXANDRITE_ORES = tag("blocks/alexandrite_ores");
        public static final TagKey<Block> MCCOURSE_ORES_BLOCK = tag("blocks/mccourse_ores_block");
        public static final TagKey<Block> MCCOURSE_ORES = tag("blocks/mccourse_ores");

        // Created Farmer tag
        public static final TagKey<Block> FARMER_BONEMEAL_GROWABLES = tag("blocks/farmer_bonemeal_growables");
        public static final TagKey<Block> FARMER_CROPS_GROWABLES = tag("blocks/farmer_crops_growables");
        public static final TagKey<Block> FARMER_TREE_GROWABLES = tag("blocks/farmer_tree_growables");
        public static final TagKey<Block> FARMER_VERTICAL_GROWABLES = tag("blocks/farmer_vertical_growables");
        public static final TagKey<Block> FARMER_AGE_GROWABLES = tag("blocks/farmer_age_growables");

        // Crop replant tag
        public static final TagKey<Block> MUSHROOM_BLOCKS = tag("blocks/mushroom_blocks");
        public static final TagKey<Block> VERTICAL_BLOCKS = tag("blocks/vertical_blocks");
        public static final TagKey<Block> VERTICAL_GROW_BLOCKS = tag("blocks/vertical_grow_blocks");

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
        public static final TagKey<EntityType<?>> MCCOURSE_ENTITIES = tag("entities/mccourse_entities");
        public static final TagKey<EntityType<?>> BOSSES = tag("entities/bosses");
        public static final TagKey<EntityType<?>> NETHER = tag("entities/nether");
        public static final TagKey<EntityType<?>> END = tag("entities/end");
        public static final TagKey<EntityType<?>> OVERWORLD = tag("entities/overworld");
        public static final TagKey<EntityType<?>> MONSTERS = tag("entities/monsters");
        public static final TagKey<EntityType<?>> ANIMALS = tag("entities/animals");
        public static final TagKey<EntityType<?>> VILLAGER = tag("entities/villager");
        public static final TagKey<EntityType<?>> WATER_ANIMALS = tag("entities/water_animals");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MCCourseMod.MOD_ID, name)); // Pass entities id
        }

        // Pass entities id in Forge
        private static TagKey<EntityType<?>> forgeTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge", name));
        }
    }
}