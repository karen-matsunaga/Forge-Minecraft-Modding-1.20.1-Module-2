package net.karen.mccourse.datagen.loot;

import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.item.ModItems;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    // Adding loot table's items and blocks
    @Override
    protected void generate() {
        // Blocks
        this.dropSelf(ModBlocks.ALEXANDRITE_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_ALEXANDRITE_BLOCK.get());
        this.dropSelf(ModBlocks.SOUND_BLOCK.get());


        // Ores
        this.add(ModBlocks.ALEXANDRITE_ORE.get(),
                block -> createOreDrop(ModBlocks.ALEXANDRITE_ORE.get(), ModItems.ALEXANDRITE.get()));

        this.add(ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(),
                block -> createOreDrop(ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(), ModItems.ALEXANDRITE.get()));

        this.add(ModBlocks.END_STONE_ALEXANDRITE_ORE.get(),
                block -> createOreDrop(ModBlocks.END_STONE_ALEXANDRITE_ORE.get(), ModItems.ALEXANDRITE.get()));

        this.add(ModBlocks.NETHER_ALEXANDRITE_ORE.get(),
                block -> createOreDrop(ModBlocks.NETHER_ALEXANDRITE_ORE.get(), ModItems.ALEXANDRITE.get()));

        // Stairs
        this.dropSelf(ModBlocks.ALEXANDRITE_STAIRS.get());

        // Slabs - Drops are different because to put twice blocks
        this.add(ModBlocks.ALEXANDRITE_SLABS.get(),
                block -> createSlabItemTable(ModBlocks.ALEXANDRITE_SLABS.get()));

        // Pressure Plate block
        this.dropSelf(ModBlocks.ALEXANDRITE_PREASSURE_PLATE.get());

        // Button block
        this.dropSelf(ModBlocks.ALEXANDRITE_BUTTON.get());

        // Fence and fence gate block
        this.dropSelf(ModBlocks.ALEXANDRITE_FENCE.get());
        this.dropSelf(ModBlocks.ALEXANDRITE_FENCE_GATE.get());

        // Wall block
        this.dropSelf(ModBlocks.ALEXANDRITE_WALL.get());

        // Door block
        this.add(ModBlocks.ALEXANDRITE_DOOR.get(),
                block -> createDoorTable(ModBlocks.ALEXANDRITE_DOOR.get()));

        // Trapdoor block
        this.dropSelf(ModBlocks.ALEXANDRITE_TRAPDOOR.get());


    }

    // Return all registries in deferred registry
    @Override
    protected Iterable<Block> getKnownBlocks() {

        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
