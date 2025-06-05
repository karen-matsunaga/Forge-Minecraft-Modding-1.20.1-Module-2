package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.*;
import java.util.concurrent.CompletableFuture;
import static net.karen.mccourse.item.ModItems.*;
import static net.karen.mccourse.util.ModTags.Items.*;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> future,
                               CompletableFuture<TagLookup<Block>> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
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
        this.tag(MCCOURSE_ITEMS).add(PINK.get(), ALEXANDRITE.get());

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
    }

    @Override
    public @NotNull String getName() { return "Item Tags"; }
}