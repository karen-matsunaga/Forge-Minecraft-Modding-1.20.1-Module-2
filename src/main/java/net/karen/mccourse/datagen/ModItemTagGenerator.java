package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> future,
                               CompletableFuture<TagLookup<Block>> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, future, completableFuture, MCCourseMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        // Add Item Tags here

        // Trimmable's armor item tag
        this.tag(ItemTags.TRIMMABLE_ARMOR).add(ModItems.ALEXANDRITE_HELMET.get(), ModItems.ALEXANDRITE_CHESTPLATE.get(),
                 ModItems.ALEXANDRITE_LEGGINGS.get(), ModItems.ALEXANDRITE_BOOTS.get());

        // Bar Brawl's music disc tag
        this.tag(ItemTags.MUSIC_DISCS).add(ModItems.BAR_BRAWL_RECORD.get());

        // Walnut's custom wood tag
        this.tag(ItemTags.LOGS_THAT_BURN).add(ModBlocks.WALNUT_LOG.get().asItem(), ModBlocks.WALNUT_WOOD.get().asItem(),
                 ModBlocks.STRIPPED_WALNUT_LOG.get().asItem(),  ModBlocks.STRIPPED_WALNUT_WOOD.get().asItem());

        this.tag(ItemTags.PLANKS).add(ModBlocks.WALNUT_PLANKS.get().asItem());

        // MCCOURSE custom items
        this.tag(ModTags.Items.MCCOURSE_ITEMS).add(ModItems.PINK.get(), ModItems.ALEXANDRITE.get());

        this.tag(ModTags.Items.MCCOURSE_ORES_ITEMS).addTag(ModTags.Items.PINK_ORES_ITEMS)
                .addTag(ModTags.Items.ALEXANDRITE_ORES_ITEMS);

        this.tag(ModTags.Items.ALEXANDRITE_ORES_ITEMS).add(ModBlocks.ALEXANDRITE_BLOCK.get().asItem(),
                 ModBlocks.ALEXANDRITE_ORE.get().asItem(), ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get().asItem(),
                 ModBlocks.END_STONE_ALEXANDRITE_ORE.get().asItem(), ModBlocks.NETHER_ALEXANDRITE_ORE.get().asItem());

        this.tag(ModTags.Items.PINK_ORES_ITEMS).add(ModBlocks.PINK_BLOCK.get().asItem(), ModBlocks.PINK_ORE.get().asItem(),
                 ModBlocks.DEEPSLATE_PINK_ORE.get().asItem(), ModBlocks.END_STONE_PINK_ORE.get().asItem(),
                 ModBlocks.NETHER_PINK_ORE.get().asItem());

        // Active Fly effect tag
        this.tag(ModTags.Items.HELMET_FLY)
                .add(ModItems.ALEXANDRITE_HELMET.get(), ModItems.PINK_HELMET.get(), ModItems.COPPER_HELMET.get());

        this.tag(ModTags.Items.CHESTPLATE_FLY)
                .add(ModItems.ALEXANDRITE_CHESTPLATE.get(), ModItems.PINK_CHESTPLATE.get(), ModItems.COPPER_CHESTPLATE.get());

        this.tag(ModTags.Items.LEGGINGS_FLY)
                .add(ModItems.ALEXANDRITE_LEGGINGS.get(), ModItems.PINK_LEGGINGS.get(), ModItems.COPPER_LEGGINGS.get());

        this.tag(ModTags.Items.BOOTS_FLY)
                .add(ModItems.ALEXANDRITE_BOOTS.get(), ModItems.PINK_BOOTS.get(), ModItems.COPPER_BOOTS.get());

        // Restore blacklist items tag
        this.tag(ModTags.Items.RESTORE_BLACKLIST_ITEMS)
                .add(ModItems.BLUE_MODES.get(), ModItems.GREEN_MODES.get(), ModItems.ORANGE_MODES.get(),
                     ModItems.PINK_MODES.get(), ModItems.PURPLE_MODES.get(), Items.ENCHANTED_BOOK);
    }

    @Override
    public @NotNull String getName() { return "Item Tags"; }
}