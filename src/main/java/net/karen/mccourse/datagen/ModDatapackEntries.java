package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.armortrim.ModTrimMaterials;
import net.karen.mccourse.armortrim.ModTrimPatterns;
import net.karen.mccourse.worldgen.ModBiomeModifiers;
import net.karen.mccourse.worldgen.ModConfiguredFeatures;
import net.karen.mccourse.worldgen.ModPlacedFeatures;
import net.karen.mccourse.worldgen.biome.ModBiomes;
import net.karen.mccourse.worldgen.dimension.ModDimensions;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModDatapackEntries extends DatapackBuiltinEntriesProvider {
    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            // Mod Trim Gen
            .add(Registries.TRIM_MATERIAL, ModTrimMaterials::bootstrap) // Custom Trim Materials
            .add(Registries.TRIM_PATTERN, ModTrimPatterns::bootstrap) // Custom Trim Patterns
            // Mod World Gen
            .add(Registries.CONFIGURED_FEATURE, ModConfiguredFeatures::bootstrap) // Custom Configured Features
            .add(Registries.PLACED_FEATURE, ModPlacedFeatures::bootstrap) // Custom Placed Features
            .add(Registries.BIOME, ModBiomes::boostrap) // Custom Biomes
            .add(Registries.LEVEL_STEM, ModDimensions::bootstrapStem) // Custom Level Stem
            .add(Registries.DIMENSION_TYPE, ModDimensions::bootstrapType) // Custom Dimensions
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, ModBiomeModifiers::bootstrap); // Custom Biomes Modifiers

    public ModDatapackEntries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(MCCourseMod.MOD_ID));
    }
}