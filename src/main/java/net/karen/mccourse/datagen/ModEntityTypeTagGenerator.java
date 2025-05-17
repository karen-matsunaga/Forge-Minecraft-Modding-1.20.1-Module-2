package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.entity.ModEntities;
import net.karen.mccourse.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagGenerator extends EntityTypeTagsProvider {
    public ModEntityTypeTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                     @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MCCourseMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(ModTags.Entities.MCCOURSE_ENTITIES)
                .add(ModEntities.RHINO.get());

        this.tag(ModTags.Entities.MCCOURSE_BOSSES)
                .add(EntityType.ELDER_GUARDIAN, EntityType.WITHER, EntityType.ENDER_DRAGON);
    }

    @Override
    public @NotNull String getName() { return "Entity Type Tags"; }
}