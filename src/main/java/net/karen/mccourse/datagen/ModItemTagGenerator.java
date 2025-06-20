package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.*;
import java.util.concurrent.CompletableFuture;
import static net.karen.mccourse.block.ModBlocks.*;
import static net.karen.mccourse.item.ModItems.*;
import static net.karen.mccourse.util.ModTags.Items.*;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> future,
                               CompletableFuture<TagLookup<Block>> completableFuture,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, future, completableFuture, MCCourseMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // Add Item Tags here

        // Bar Brawl's music disc tag
        this.tag(ItemTags.MUSIC_DISCS).add(BAR_BRAWL_RECORD.get());

        // Walnut's custom wood tag
        this.tag(ItemTags.LOGS_THAT_BURN).add(ModBlocks.WALNUT_LOG.get().asItem(), ModBlocks.WALNUT_WOOD.get().asItem(),
                 ModBlocks.STRIPPED_WALNUT_LOG.get().asItem(),  ModBlocks.STRIPPED_WALNUT_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS).add(ModBlocks.WALNUT_PLANKS.get().asItem());

        // MCCOURSE custom items
        this.tag(MCCOURSE_ITEMS).add(PINK.get(), ALEXANDRITE.get(), RAW_ALEXANDRITE.get());

        this.tag(MCCOURSE_ORES_ITEMS).addTag(PINK_ORES_ITEMS).addTag(ALEXANDRITE_ORES_ITEMS);

        this.tag(ALEXANDRITE_ORES_ITEMS).add(ModBlocks.ALEXANDRITE_BLOCK.get().asItem(),
                 ModBlocks.ALEXANDRITE_ORE.get().asItem(), ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get().asItem(),
                 ModBlocks.END_STONE_ALEXANDRITE_ORE.get().asItem(), ModBlocks.NETHER_ALEXANDRITE_ORE.get().asItem());

        this.tag(PINK_ORES_ITEMS).add(ModBlocks.PINK_BLOCK.get().asItem(), ModBlocks.PINK_ORE.get().asItem(),
                 ModBlocks.DEEPSLATE_PINK_ORE.get().asItem(), ModBlocks.END_STONE_PINK_ORE.get().asItem(),
                 ModBlocks.NETHER_PINK_ORE.get().asItem());

        // Active Fly effect tag
        this.tag(HELMET_FLY).add(ALEXANDRITE_HELMET.get(), PINK_HELMET.get(), COPPER_HELMET.get());

        this.tag(CHESTPLATE_FLY).add(ALEXANDRITE_CHESTPLATE.get(), PINK_CHESTPLATE.get(), COPPER_CHESTPLATE.get());

        this.tag(LEGGINGS_FLY).add(ALEXANDRITE_LEGGINGS.get(), PINK_LEGGINGS.get(), COPPER_LEGGINGS.get());

        this.tag(BOOTS_FLY).add(ALEXANDRITE_BOOTS.get(), PINK_BOOTS.get(), COPPER_BOOTS.get());

        // Restore blacklist items tag
        this.tag(RESTORE_BLACKLIST_ITEMS).add(BLUE_MODES.get(), GREEN_MODES.get(), ORANGE_MODES.get(),
                 PINK_MODES.get(), PURPLE_MODES.get(), Items.ENCHANTED_BOOK);

        // Custom armors
        this.tag(ALEXANDRITE_ARMOR).add(ALEXANDRITE_HELMET.get(), ALEXANDRITE_CHESTPLATE.get(),
                 ALEXANDRITE_LEGGINGS.get(), ALEXANDRITE_BOOTS.get());

        this.tag(COPPER_ARMOR).add(COPPER_HELMET.get(), COPPER_CHESTPLATE.get(), COPPER_LEGGINGS.get(), COPPER_BOOTS.get());

        this.tag(PINK_ARMOR).add(PINK_HELMET.get(), PINK_CHESTPLATE.get(), PINK_LEGGINGS.get(), PINK_BOOTS.get());

        // Trimmable's armor item tag
        this.tag(ItemTags.TRIMMABLE_ARMOR).addTag(ALEXANDRITE_ARMOR).addTag(COPPER_ARMOR).addTag(PINK_ARMOR).add(MINER_HELMET.get());
        this.tag(ItemTags.TRIM_MATERIALS).add(ALEXANDRITE.get());
        this.tag(ItemTags.TRIM_TEMPLATES).add(KAUPEN_SMITHING_TEMPLATE.get());

        // Ores
        this.tag(MULTIPLIER_ORES).addTag(Tags.Items.ORES).addTag(MCCOURSE_ORES_ITEMS).addTag(ORE_BLOCK_ITEMS);

        this.tag(ORE_BLOCK_ITEMS).addTag(Tags.Items.STORAGE_BLOCKS_COAL).addTag(Tags.Items.STORAGE_BLOCKS_COPPER)
                .addTag(Tags.Items.STORAGE_BLOCKS_DIAMOND).addTag(Tags.Items.STORAGE_BLOCKS_EMERALD)
                .addTag(Tags.Items.STORAGE_BLOCKS_GOLD).addTag(Tags.Items.STORAGE_BLOCKS_IRON).addTag(Tags.Items.STORAGE_BLOCKS_LAPIS)
                .addTag(Tags.Items.STORAGE_BLOCKS_NETHERITE).addTag(Tags.Items.STORAGE_BLOCKS_REDSTONE);

        // Teleport items
        this.tag(TELEPORT_ITEMS).add(ALEXANDRITE_SWORD.get(), ALEXANDRITE_PAXEL.get());

        // Ultra Compactor Input
        this.tag(ULTRA_COMPACTOR_ITEMS).addTag(Tags.Items.INGOTS_COPPER).addTag(Tags.Items.INGOTS_GOLD).addTag(ItemTags.COALS)
                .addTag(Tags.Items.INGOTS_IRON).addTag(Tags.Items.INGOTS_NETHERITE).addTag(Tags.Items.GEMS_DIAMOND)
                .addTag(Tags.Items.GEMS_EMERALD).addTag(Tags.Items.GEMS_LAPIS).addTag(Tags.Items.DUSTS_REDSTONE)
                .addTag(MCCOURSE_ITEMS).addTag(Tags.Items.RAW_MATERIALS).add(Items.ENDER_PEARL, Items.BLAZE_ROD, Items.ROTTEN_FLESH,
                        Items.GUNPOWDER, Items.NETHER_STAR, Items.PHANTOM_MEMBRANE);

        // Ultra Compactor Output
        this.tag(ULTRA_COMPACTOR_RESULT).addTag(Tags.Items.STORAGE_BLOCKS_RAW_COPPER).addTag(Tags.Items.STORAGE_BLOCKS_RAW_GOLD)
                .addTag(Tags.Items.STORAGE_BLOCKS_RAW_IRON).addTag(ORE_BLOCK_ITEMS)
                .add(ALEXANDRITE_BLOCK.get().asItem(), RAW_ALEXANDRITE_BLOCK.get().asItem(),
                     PINK_BLOCK.get().asItem(), ENDER_PEARL_BLOCK.get().asItem(), NETHER_STAR_BLOCK.get().asItem(),
                     GUNPOWDER_BLOCK.get().asItem(), ROTTEN_FLESH_BLOCK.get().asItem(),
                     BLAZE_ROD_BLOCK.get().asItem(), PHANTOM_MEMBRANE_BLOCK.get().asItem());
    }

    @Override
    public @NotNull String getName() { return "Item Tags"; }
}