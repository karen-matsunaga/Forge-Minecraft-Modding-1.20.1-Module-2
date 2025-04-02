package net.karen.mccourse.util;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    // Items
    public static class Items {
        // Created Item's tags HERE ...

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

        // Created Alexandrite Tool Level's tag
        public static final TagKey<Block> NEEDS_ALEXANDRITE_TOOL = tag("needs_alexandrite_tool");

        public static final TagKey<Block> NEEDS_PINK_TOOL = tag("needs_pink_tool");

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

        // Pass block id
        private static TagKey<Block> tag(String name) { return BlockTags.create(new ResourceLocation(MCCourseMod.MOD_ID, name)); }

        // Pass block id in Forge
        private static TagKey<Block> forgeTag(String name) { return BlockTags.create(new ResourceLocation("forge", name)); }
    }
}