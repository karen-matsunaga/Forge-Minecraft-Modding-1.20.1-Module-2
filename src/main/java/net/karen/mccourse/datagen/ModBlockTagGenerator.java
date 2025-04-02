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
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                @Nullable ExistingFileHelper existingFileHelper) { super(output, lookupProvider, MCCourseMod.MOD_ID, existingFileHelper); }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Add Block Tags here
        this.tag(ModTags.Blocks.METAL_DETECTOR_VALUABLES)
                .add(ModBlocks.ALEXANDRITE_ORE.get(), ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(),
                ModBlocks.END_STONE_ALEXANDRITE_ORE.get(), ModBlocks.NETHER_ALEXANDRITE_ORE.get()).addTag(Tags.Blocks.ORES);

        // Add Mineable's tags

        // Pickaxe's tag, Alexandrite hammer's tag
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ALEXANDRITE_BLOCK.get(),
                        ModBlocks.RAW_ALEXANDRITE_BLOCK.get(),
                        // Ores
                        ModBlocks.ALEXANDRITE_ORE.get(),
                        ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(),
                        ModBlocks.END_STONE_ALEXANDRITE_ORE.get(),
                        ModBlocks.NETHER_ALEXANDRITE_ORE.get(),
                        ModBlocks.SOUND_BLOCK.get(),
                        // Stairs
                        ModBlocks.ALEXANDRITE_STAIRS.get(),
                        // Slabs
                        ModBlocks.ALEXANDRITE_SLABS.get());

        // Iron's tool tag
        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.ALEXANDRITE_BLOCK.get(),
                        ModBlocks.RAW_ALEXANDRITE_BLOCK.get(),
                        // Ores
                        ModBlocks.ALEXANDRITE_ORE.get(),
                        // Custom Advanced Block
                        ModBlocks.SOUND_BLOCK.get(),
                        // Stairs
                        ModBlocks.ALEXANDRITE_STAIRS.get(),
                        // Slabs
                        ModBlocks.ALEXANDRITE_SLABS.get());

        // Diamond's tool tag
        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(),
                        ModBlocks.END_STONE_ALEXANDRITE_ORE.get());

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
                .add(ModBlocks.WALNUT_LOG.get())
                .add(ModBlocks.WALNUT_WOOD.get())
                .add(ModBlocks.STRIPPED_WALNUT_LOG.get())
                .add(ModBlocks.STRIPPED_WALNUT_WOOD.get());

        this.tag(BlockTags.PLANKS)
                .add(ModBlocks.WALNUT_PLANKS.get());

        // More Ores's tags
        this.tag(ModTags.Blocks.MORE_ORES_ONE_DROPS) // More Ores I
                .add(Blocks.COAL_ORE)
                .add(Blocks.COPPER_ORE);

        this.tag(ModTags.Blocks.MORE_ORES_TWO_DROPS) // More Ores II
                .add(Blocks.IRON_ORE)
                .add(Blocks.LAPIS_ORE);

        this.tag(ModTags.Blocks.MORE_ORES_THREE_DROPS) // More Ores III
                .add(Blocks.REDSTONE_ORE)
                .add(Blocks.GOLD_ORE);

        this.tag(ModTags.Blocks.MORE_ORES_FOUR_DROPS) // More Ores IV
                .add(Blocks.DIAMOND_ORE)
                .add(Blocks.EMERALD_ORE);

        this.tag(ModTags.Blocks.MORE_ORES_FIVE_DROPS) // More Ores V
                .add(Blocks.ANCIENT_DEBRIS)
                .add(Blocks.NETHER_GOLD_ORE)
                .add(Blocks.NETHER_QUARTZ_ORE);

        // Modes Pickaxe's tag
        this.tag(ModTags.Blocks.MORE_ORES_MODES_PICKAXE_DROPS) // Modes Pickaxe's Mores Ores mode
                .add(Blocks.COAL_ORE)
                .add(Blocks.COPPER_ORE)
                .add(Blocks.IRON_ORE)
                .add(Blocks.LAPIS_ORE)
                .add(Blocks.REDSTONE_ORE)
                .add(Blocks.GOLD_ORE)
                .add(Blocks.DIAMOND_ORE)
                .add(Blocks.EMERALD_ORE)
                .add(Blocks.ANCIENT_DEBRIS)
                .add(Blocks.NETHER_QUARTZ_ORE);
    }

    @Override
    public String getName() { return "Block Tags"; }
}