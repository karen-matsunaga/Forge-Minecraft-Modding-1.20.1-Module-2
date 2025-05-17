package net.karen.mccourse.util;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    // Items
    public static class Items {
        // Created Item's tags HERE ...
        // Ores item
        public static final TagKey<Item> MCCOURSE_ITEMS = tag("blocks/mccourse_items");

        // Created Pink ores tags
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

        // Pass item id
        private static TagKey<Item> tag(String name) { return ItemTags.create(new ResourceLocation(MCCourseMod.MOD_ID, name)); }

        // Pass item id in Forge
        private static TagKey<Item> forgeTag(String name) { return ItemTags.create(new ResourceLocation("forge", name)); }
    }

    // Blocks
    public static class Blocks {
        // Created Block's tags HERE
        // Created METAL DETECTOR's tag
        public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");

        public static final TagKey<Block> SPECIAL_METAL_DETECTOR_VALUABLES = tag("special_metal_detector_valuables");

        // Created Alexandrite Tool Level's tag
        public static final TagKey<Block> NEEDS_ALEXANDRITE_TOOL = tag("needs_alexandrite_tool");

        public static final TagKey<Block> NEEDS_PINK_TOOL = tag("needs_pink_tool");

        public static final TagKey<Block> NEEDS_COPPER_TOOL = tag("needs_copper_tool");

        // Created Alexandrite Paxel's tag
        public static final TagKey<Block> PAXEL_MINEABLE = tag("mineable/paxel");

        // Created More Ores's drops
        public static final TagKey<Block> MORE_ORES_ONE_DROPS = tag("ores/more_ores_one_drops"); // More Ores I
        public static final TagKey<Block> MORE_ORES_TWO_DROPS = tag("ores/more_ores_two_drops"); // More Ores II
        public static final TagKey<Block> MORE_ORES_THREE_DROPS = tag("ores/more_ores_three_drops"); // More Ores III
        public static final TagKey<Block> MORE_ORES_FOUR_DROPS = tag("ores/more_ores_four_drops"); // More Ores IV
        public static final TagKey<Block> MORE_ORES_FIVE_DROPS = tag("ores/more_ores_five_drops"); // More Ores V

        // Created More Ores's alternative drops
        public static final TagKey<Block> MORE_ORES_MODES_PICKAXE_DROPS = tag("ores/more_ores_modes_pickaxe_drops"); // Modes Pickaxe's More Ores mode

        public static final TagKey<Block> MOBS_BLOCKS_DROPS = tag("blocks/mobs_blocks_drops");

        // Created Pink ores tags
        public static final TagKey<Block> PINK_ORES = tag("blocks/pink_ores");
        public static final TagKey<Block> ALEXANDRITE_ORES = tag("blocks/alexandrite_ores");

        public static final TagKey<Block> MCCOURSE_ORES_BLOCK = tag("blocks/mccourse_ores_block");
        public static final TagKey<Block> MCCOURSE_ORES = tag("blocks/mccourse_ores");

        // Created Farmer tag
        public static final TagKey<Block> FARMER_INSTANT_GROWABLES = tag("blocks/farmer_instant_growables");

        // Pass block id
        private static TagKey<Block> tag(String name) { return BlockTags.create(new ResourceLocation(MCCourseMod.MOD_ID, name)); }

        // Pass block id in Forge
        private static TagKey<Block> forgeTag(String name) { return BlockTags.create(new ResourceLocation("forge", name)); }
    }

    // Entities
    public static class Entities {
        // Created Entities's tags HERE
        public static final TagKey<EntityType<?>> MCCOURSE_ENTITIES = tag("entities/mccourse_entities");

        public static final TagKey<EntityType<?>> MCCOURSE_BOSSES = tag("entities/mccourse_bosses");

        // Pass entities id
        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(MCCourseMod.MOD_ID, name));
        }

        // Pass entities id in Forge
        private static TagKey<EntityType<?>> forgeTag(String name) {
            return TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge", name));
        }
    }
}