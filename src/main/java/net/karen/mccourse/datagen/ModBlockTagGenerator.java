package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.*;
import org.jetbrains.annotations.*;
import java.util.concurrent.CompletableFuture;
import static net.karen.mccourse.block.ModBlocks.*;
import static net.karen.mccourse.util.ModTags.Blocks.*;
import static net.minecraftforge.common.Tags.Blocks.*;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output,
                                CompletableFuture<HolderLookup.Provider> lookupProvider,
                                @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MCCourseMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // Add Block Tags here
        // Metal Detector tags
        this.tag(METAL_DETECTOR_VALUABLES).addTag(ORES).addTag(ORES_IN_GROUND_DEEPSLATE).addTag(MCCOURSE_ORES);
        this.tag(METAL_DETECTOR_COLORS).addTag(ORES_GOLD).addTag(ORES_COPPER);
        this.tag(SPECIAL_METAL_DETECTOR_VALUABLES).add(Blocks.SPAWNER, Blocks.END_PORTAL_FRAME, Blocks.CHEST, Blocks.TRAPPED_CHEST);

        // Add Mineable's tags - Pickaxe's tag, Alexandrite hammer's tag, etc.
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ALEXANDRITE_STAIRS.get(), ALEXANDRITE_SLABS.get(), // Stairs and slabs
                     GEM_EMPOWERING_STATION.get(), KAUPEN_FURNACE_BLOCK.get(), DICE_BLOCK.get(), SOUND_BLOCK.get(), // Custom blocks
                     GREEN_ENDER_PEARL_BLOCK.get(), BLACK_ENDER_PEARL_BLOCK.get(), MAGENTA_ENDER_PEARL_BLOCK.get(),
                     PURPLE_ENDER_PEARL_BLOCK.get(), ORANGE_ENDER_PEARL_BLOCK.get(), PINK_ENDER_PEARL_BLOCK.get(),
                     CYAN_ENDER_PEARL_BLOCK.get(), BROWN_ENDER_PEARL_BLOCK.get(), GRAY_ENDER_PEARL_BLOCK.get(),
                     RED_ENDER_PEARL_BLOCK.get(), LIME_GREEN_ENDER_PEARL_BLOCK.get(), YELLOW_ENDER_PEARL_BLOCK.get(),
                     BLUE_ENDER_PEARL_BLOCK.get(), WHITE_ENDER_PEARL_BLOCK.get(),
                     DISENCHANTED_BLOCK.get(), CRAFT_CRAFTING_TABLE.get(), MCCOURSE_GENERATOR.get(), MCCOURSE_ELEVATOR.get(),
                     MAGIC_BLOCK.get(), MAGIC_BOOK_BLOCK.get(), BOOK_DISENCHANTED_BLOCK.get(),
                     RUBY_BLOCK.get(), RUBY_BLOCK_1.get(), RUBY_BLOCK_2.get(), RUBY_BLOCK_3.get(),
                     WAXED_RUBY_BLOCK.get(), WAXED_RUBY_BLOCK_1.get(), WAXED_RUBY_BLOCK_2.get(), WAXED_RUBY_BLOCK_3.get())
                .addTag(MOBS_BLOCKS_DROPS).addTag(MCCOURSE_ORES).addTag(MCCOURSE_ORES_BLOCK);

        // Iron's tool tag
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ALEXANDRITE_ORE.get(), PINK_ORE.get(), // Ores
                     SOUND_BLOCK.get(), // Custom Advanced Block
                     ALEXANDRITE_STAIRS.get(), ALEXANDRITE_SLABS.get()) // Stairs and slabs
                .addTag(MCCOURSE_ORES_BLOCK);

        // Diamond's tool tag
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL).add(DEEPSLATE_ALEXANDRITE_ORE.get(), END_STONE_ALEXANDRITE_ORE.get(),
                 DEEPSLATE_PINK_ORE.get(), END_STONE_PINK_ORE.get());

        // Fence's tag
        this.tag(BlockTags.FENCES).add(ALEXANDRITE_FENCE.get());

        // Wall's tag
        this.tag(BlockTags.WALLS).add(ALEXANDRITE_WALL.get());

        // Fence Gate's tag
        this.tag(BlockTags.FENCE_GATES).add(ALEXANDRITE_FENCE_GATE.get());

        // Alexandrite tool's tag
        this.tag(NEEDS_ALEXANDRITE_TOOL).add(NETHER_ALEXANDRITE_ORE.get());

        // Alexandrite Paxel's tag
        this.tag(PAXEL_MINEABLE).addTag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(BlockTags.MINEABLE_WITH_SHOVEL).addTag(BlockTags.MINEABLE_WITH_AXE);

        // Walnut's tag
        this.tag(BlockTags.LOGS_THAT_BURN).add(WALNUT_LOG.get(), WALNUT_WOOD.get(),
                 STRIPPED_WALNUT_LOG.get(), STRIPPED_WALNUT_WOOD.get());

        this.tag(BlockTags.PLANKS).add(WALNUT_PLANKS.get());

        // More Ores's tags -> More Ores I, II, III, IV, V
        this.tag(MORE_ORES_ONE_DROPS).add(Blocks.COAL_ORE, Blocks.COPPER_ORE);
        this.tag(MORE_ORES_TWO_DROPS).add(Blocks.IRON_ORE, Blocks.LAPIS_ORE);
        this.tag(MORE_ORES_THREE_DROPS).add(Blocks.REDSTONE_ORE, Blocks.GOLD_ORE);
        this.tag(MORE_ORES_FOUR_DROPS).add(Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE);
        this.tag(MORE_ORES_FIVE_DROPS).add(Blocks.ANCIENT_DEBRIS, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE);

        // Modes Pickaxe's tag -  Modes Pickaxe's Mores Ores mode
        this.tag(MORE_ORES_MODES_PICKAXE_DROPS).addTag(ORES_IN_GROUND_STONE)
                .addTag(ORES_IN_GROUND_NETHERRACK).addTag(ORES_NETHERITE_SCRAP);

        // Mobs Blocks
        this.tag(MOBS_BLOCKS_DROPS).add(ENDER_PEARL_BLOCK.get(), NETHER_STAR_BLOCK.get(),
                 GUNPOWDER_BLOCK.get(), ROTTEN_FLESH_BLOCK.get(), BLAZE_ROD_BLOCK.get(), PHANTOM_MEMBRANE_BLOCK.get());

        // Mccourse ores tags
        this.tag(PINK_ORES).add(PINK_ORE.get(), DEEPSLATE_PINK_ORE.get(), END_STONE_PINK_ORE.get(), NETHER_PINK_ORE.get());
        this.tag(ALEXANDRITE_ORES).add(ALEXANDRITE_ORE.get(), DEEPSLATE_ALEXANDRITE_ORE.get(),
                 END_STONE_ALEXANDRITE_ORE.get(), NETHER_ALEXANDRITE_ORE.get());
        this.tag(MCCOURSE_ORES).addTag(PINK_ORES).addTag(ALEXANDRITE_ORES);
        this.tag(MCCOURSE_ORES_BLOCK).add(PINK_BLOCK.get(), ALEXANDRITE_BLOCK.get(), RAW_ALEXANDRITE_BLOCK.get());

        // Farmer block tag
        this.tag(FARMER_BONEMEAL_GROWABLES).addTag(FARMER_CROPS_GROWABLES).addTag(FARMER_TREE_GROWABLES);
        this.tag(FARMER_CROPS_GROWABLES).add(KOHLRABI_CROP.get(), CATTAIL_CROP.get()).addTag(BlockTags.CROPS);
        this.tag(FARMER_TREE_GROWABLES).addTag(BlockTags.SAPLINGS);
        this.tag(FARMER_VERTICAL_GROWABLES).addTag(VERTICAL_BLOCKS);
        this.tag(FARMER_AGE_GROWABLES).add(Blocks.NETHER_WART, Blocks.TWISTING_VINES, Blocks.WEEPING_VINES,
                 Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);

        // Crop replant tags
        // Mushroom crop
        this.tag(MUSHROOM_BLOCKS).add(Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.CRIMSON_FUNGUS, Blocks.WARPED_FUNGUS);

        // Vertical crop
        this.tag(VERTICAL_BLOCKS).add(Blocks.BAMBOO, Blocks.SUGAR_CANE, Blocks.CACTUS);
        this.tag(VERTICAL_GROW_BLOCKS).add(Blocks.GRASS_BLOCK, Blocks.SAND, Blocks.DIRT);
    }

    @Override
    public @NotNull String getName() { return "Block Tags"; }
}