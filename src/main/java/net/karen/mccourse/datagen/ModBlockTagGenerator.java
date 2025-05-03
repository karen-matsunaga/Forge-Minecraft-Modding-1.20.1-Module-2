package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                @Nullable ExistingFileHelper existingFileHelper) { super(output, lookupProvider, MCCourseMod.MOD_ID, existingFileHelper); }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // Add Block Tags here
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
                .addTag(Tags.Blocks.ORES)
                .addTag(Tags.Blocks.ORES_IN_GROUND_DEEPSLATE)
                .addTag(ModTags.Blocks.MCCOURSE_ORES);

        // Add Mineable's tags
        // Pickaxe's tag, Alexandrite hammer's tag
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.SOUND_BLOCK.get(), // Blocks
                    ModBlocks.ALEXANDRITE_STAIRS.get(), // Stairs
                    ModBlocks.ALEXANDRITE_SLABS.get(), // Slabs
                    ModBlocks.GEM_EMPOWERING_STATION.get(), ModBlocks.KAUPEN_FURNACE_BLOCK.get(),
                    ModBlocks.GREEN_ENDER_PEARL_BLOCK.get(), ModBlocks.BLACK_ENDER_PEARL_BLOCK.get(), // Custom blocks
                    ModBlocks.MAGENTA_ENDER_PEARL_BLOCK.get(), ModBlocks.PURPLE_ENDER_PEARL_BLOCK.get(),
                    ModBlocks.ORANGE_ENDER_PEARL_BLOCK.get(), ModBlocks.PINK_ENDER_PEARL_BLOCK.get(),
                    ModBlocks.CYAN_ENDER_PEARL_BLOCK.get(), ModBlocks.BROWN_ENDER_PEARL_BLOCK.get(),
                    ModBlocks.GRAY_ENDER_PEARL_BLOCK.get(), ModBlocks.RED_ENDER_PEARL_BLOCK.get(),
                    ModBlocks.LIME_GREEN_ENDER_PEARL_BLOCK.get(), ModBlocks.YELLOW_ENDER_PEARL_BLOCK.get(),
                    ModBlocks.BLUE_ENDER_PEARL_BLOCK.get(), ModBlocks.WHITE_ENDER_PEARL_BLOCK.get(),
                    ModBlocks.DISENCHANTED_BLOCK.get(), ModBlocks.CRAFT_CRAFTING_TABLE.get())
                .addTag(ModTags.Blocks.MOBS_BLOCKS_DROPS)
                .addTag(ModTags.Blocks.MCCOURSE_ORES)
                .addTag(ModTags.Blocks.MCCOURSE_ORES_BLOCK);

        // Iron's tool tag
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.ALEXANDRITE_ORE.get(), ModBlocks.PINK_ORE.get(), // Ores
                     ModBlocks.SOUND_BLOCK.get(), // Custom Advanced Block
                     ModBlocks.ALEXANDRITE_STAIRS.get(), // Stairs
                     ModBlocks.ALEXANDRITE_SLABS.get()) // Slabs
                .addTag(ModTags.Blocks.MCCOURSE_ORES_BLOCK);

        // Diamond's tool tag
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(), ModBlocks.END_STONE_ALEXANDRITE_ORE.get(),
                        ModBlocks.DEEPSLATE_PINK_ORE.get(), ModBlocks.END_STONE_PINK_ORE.get());

        // Fence's tag
        this.tag(BlockTags.FENCES)
                .add(ModBlocks.ALEXANDRITE_FENCE.get());

        // Wall's tag
        this.tag(BlockTags.WALLS)
                .add(ModBlocks.ALEXANDRITE_WALL.get());

        // Fence Gate's tag
        this.tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.ALEXANDRITE_FENCE_GATE.get());

        // Alexandrite tool's tag
        this.tag(ModTags.Blocks.NEEDS_ALEXANDRITE_TOOL)
                .add(ModBlocks.NETHER_ALEXANDRITE_ORE.get());

        // Alexandrite Paxel's tag
        this.tag(ModTags.Blocks.PAXEL_MINEABLE)
                .addTag(BlockTags.MINEABLE_WITH_PICKAXE)
                .addTag(BlockTags.MINEABLE_WITH_SHOVEL)
                .addTag(BlockTags.MINEABLE_WITH_AXE);

        // Walnut's tag
        this.tag(BlockTags.LOGS_THAT_BURN)
                .add(ModBlocks.WALNUT_LOG.get(), ModBlocks.WALNUT_WOOD.get(),
                     ModBlocks.STRIPPED_WALNUT_LOG.get(), ModBlocks.STRIPPED_WALNUT_WOOD.get());

        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.WALNUT_PLANKS.get());

        // More Ores's tags
        this.tag(ModTags.Blocks.MORE_ORES_ONE_DROPS) // More Ores I
                .add(Blocks.COAL_ORE, Blocks.COPPER_ORE);

        this.tag(ModTags.Blocks.MORE_ORES_TWO_DROPS) // More Ores II
                .add(Blocks.IRON_ORE, Blocks.LAPIS_ORE);

        this.tag(ModTags.Blocks.MORE_ORES_THREE_DROPS) // More Ores III
                .add(Blocks.REDSTONE_ORE, Blocks.GOLD_ORE);

        this.tag(ModTags.Blocks.MORE_ORES_FOUR_DROPS) // More Ores IV
                .add(Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE);

        this.tag(ModTags.Blocks.MORE_ORES_FIVE_DROPS) // More Ores V
                .add(Blocks.ANCIENT_DEBRIS, Blocks.NETHER_GOLD_ORE, Blocks.NETHER_QUARTZ_ORE);

        // Modes Pickaxe's tag
        this.tag(ModTags.Blocks.MORE_ORES_MODES_PICKAXE_DROPS) // Modes Pickaxe's Mores Ores mode
                .add(Blocks.COAL_ORE, Blocks.COPPER_ORE, Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.REDSTONE_ORE,
                     Blocks.GOLD_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.ANCIENT_DEBRIS, Blocks.NETHER_QUARTZ_ORE);

        // Mobs Blocks
        this.tag(ModTags.Blocks.MOBS_BLOCKS_DROPS)
                .add(ModBlocks.ENDER_PEARL_BLOCK.get(), ModBlocks.NETHER_STAR_BLOCK.get(), ModBlocks.GUNPOWDER_BLOCK.get(),
                     ModBlocks.ROTTEN_FLESH_BLOCK.get(), ModBlocks.BLAZE_ROD_BLOCK.get());

        // My custom ores tags
        this.tag(ModTags.Blocks.PINK_ORES)
                .add(ModBlocks.PINK_ORE.get(), ModBlocks.DEEPSLATE_PINK_ORE.get(),
                     ModBlocks.END_STONE_PINK_ORE.get(), ModBlocks.NETHER_PINK_ORE.get());

        this.tag(ModTags.Blocks.ALEXANDRITE_ORES)
                .add(ModBlocks.ALEXANDRITE_ORE.get(), ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(),
                     ModBlocks.END_STONE_ALEXANDRITE_ORE.get(), ModBlocks.NETHER_ALEXANDRITE_ORE.get());

        this.tag(ModTags.Blocks.MCCOURSE_ORES)
                .addTag(ModTags.Blocks.PINK_ORES)
                .addTag(ModTags.Blocks.ALEXANDRITE_ORES);

        this.tag(ModTags.Blocks.MCCOURSE_ORES_BLOCK)
                .add(ModBlocks.PINK_BLOCK.get(), ModBlocks.ALEXANDRITE_BLOCK.get(), ModBlocks.RAW_ALEXANDRITE_BLOCK.get());
    }

    @Override
    public @NotNull String getName() { return "Block Tags"; }
}