package net.karen.mccourse.item;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ForgeTier;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.registries.RegistryObject;
import java.util.List;

public class ModToolTiers { // Adding custom tool level
    // Alexandrite tier
    public static final Tier ALEXANDRITE = newTier(5, 1400, 11f, 3f, 26,
           ModTags.Blocks.NEEDS_ALEXANDRITE_TOOL, ModItems.ALEXANDRITE, "alexandrite", Tiers.NETHERITE);

    // Modes Pickaxe and Pink level
    public static final Tier PINK = newTier(4, 800, 11f, 3f, 30,
           ModTags.Blocks.NEEDS_PINK_TOOL, ModItems.PINK, "pink", Tiers.DIAMOND);

    // Copper tier
    public static final Tier COPPER = newVanillaTier(3, 500, 9f, 6f, 30,
           ModTags.Blocks.NEEDS_COPPER_TOOL, Items.COPPER_INGOT, "copper", Tiers.IRON);

    // Lapis Lazuli tier
    public static final Tier LAPIS_LAZULI = newVanillaTier(2, 200, 5f, 2f, 20,
           ModTags.Blocks.NEEDS_LAPIS_LAZULI_TOOL, Items.LAPIS_LAZULI, "lapis_lazuli", Tiers.WOOD);

    // Redstone tier
    public static final Tier REDSTONE = newVanillaTier(3, 400, 7f, 1f, 30,
           ModTags.Blocks.NEEDS_REDSTONE_TOOL, Items.REDSTONE, "redstone", Tiers.STONE);

    // CUSTOM METHOD - Register MOD items (Tier)
    public static Tier newTier(int level, int uses, float speed, float attack, int enchant,
                               TagKey<Block> block, RegistryObject<Item> item, String path, Tier tier) {
        return TierSortingRegistry.registerTier(new ForgeTier(level, uses, speed, attack, enchant, block,
                () -> Ingredient.of(item.get())), new ResourceLocation(MCCourseMod.MOD_ID, path), List.of(tier), List.of());
    }

    // CUSTOM METHOD - Register VANILLA items (Tier)
    public static Tier newVanillaTier(int level, int uses, float speed, float attack, int enchant,
                                      TagKey<Block> block, Item item, String path, Tier tier) {
        return TierSortingRegistry.registerTier(new ForgeTier(level, uses, speed, attack, enchant, block,
                () -> Ingredient.of(item.asItem())), new ResourceLocation(MCCourseMod.MOD_ID, path), List.of(tier), List.of());
    }
}