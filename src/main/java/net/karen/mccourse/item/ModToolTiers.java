package net.karen.mccourse.item;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;

import java.util.List;

public class ModToolTiers {
    // Adding custom tool level
    public static final Tier ALEXANDRITE = TierSortingRegistry.registerTier(
            new ForgeTier(5, 1400, 11f, 3f, 26,
                    ModTags.Blocks.NEEDS_ALEXANDRITE_TOOL, () -> Ingredient.of(ModItems.ALEXANDRITE.get())),
            new ResourceLocation(MCCourseMod.MOD_ID, "alexandrite"), List.of(Tiers.NETHERITE), List.of());

    // Modes Pickaxe and Pink level
    public static final Tier PINK = TierSortingRegistry.registerTier(
            new ForgeTier(4, 0, 11f, 3f, 26,
                    ModTags.Blocks.NEEDS_PINK_TOOL, () -> Ingredient.of(Items.DIAMOND)),
            new ResourceLocation(MCCourseMod.MOD_ID, "pink"), List.of(Tiers.DIAMOND), List.of());

    public static final Tier COPPER = TierSortingRegistry.registerTier(
            new ForgeTier(5, -1, 20f, 6f, 30,
                    ModTags.Blocks.NEEDS_COPPER_TOOL, () -> Ingredient.of(Items.COBBLESTONE)),
            new ResourceLocation(MCCourseMod.MOD_ID, "cobblestone"), List.of(Tiers.IRON), List.of());
}