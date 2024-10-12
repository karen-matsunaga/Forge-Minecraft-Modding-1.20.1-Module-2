package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> future,
                               CompletableFuture<TagLookup<Block>> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, future, completableFuture, MCCourseMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Add Item Tags here

        // Trimmable's armor item tag
        this.tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.ALEXANDRITE_HELMET.get(),
                        ModItems.ALEXANDRITE_CHESTPLATE.get(),
                        ModItems.ALEXANDRITE_LEGGINGS.get(),
                        ModItems.ALEXANDRITE_BOOTS.get());

        // Bar Brawl's music disc
        this.tag(ItemTags.MUSIC_DISCS)
                .add(ModItems.BAR_BRAWL_RECORD.get());

    }


    @Override
    public String getName() {
        return "Item Tags";
    }
}
