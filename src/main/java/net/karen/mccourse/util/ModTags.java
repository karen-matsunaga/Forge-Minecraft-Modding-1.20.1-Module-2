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

        // Pass item id
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(MCCourseMod.MOD_ID, name));
        }

        // Pass item id in Forge
        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("forge", name));
        }

    }

    // Blocks
    public static class Blocks {

        // Created METAL DETECTOR's tag
        public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");

        // Pass block id
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(MCCourseMod.MOD_ID, name));
        }

        // Pass block id in Forge
        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("forge", name));
        }
    }

}
