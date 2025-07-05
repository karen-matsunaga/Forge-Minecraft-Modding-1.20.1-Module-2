package net.karen.mccourse.worldgen.tree.custom;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.karen.mccourse.worldgen.tree.ModFoliagePlacerTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.*;
import org.jetbrains.annotations.NotNull;

public class WalnutFoliagePlacer extends FoliagePlacer {
    public static final Codec<WalnutFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) ->
            foliagePlacerParts(instance).and(Codec.intRange(0, 16).fieldOf("height").forGetter(fp -> fp.height))
                                        .apply(instance, WalnutFoliagePlacer::new));
    protected final int height;

    public WalnutFoliagePlacer(IntProvider radius, IntProvider offset, int height) {
        super(radius, offset); this.height = height;
    }

    @Override
    protected @NotNull FoliagePlacerType<?> type() { return ModFoliagePlacerTypes.WALNUT_FOLIAGE_PLACER.get(); }

    @Override
    protected void createFoliage(@NotNull LevelSimulatedReader level, @NotNull FoliageSetter foliageSetter,
                                 @NotNull RandomSource source, @NotNull TreeConfiguration config, int maxFreeTreeHeight,
                                 @NotNull FoliageAttachment attachment, int foliageHeight, int foliageRadius, int offset) {
        // Creating the foliage
        // attachment.pos() is the first position ABOVE the last places log
        // tryPlaceLeaf() // places one leave at given position!
        int x = attachment.pos().getX(), y = attachment.pos().getY(), z = attachment.pos().getZ();
        for (int i = 0; i < 4; i++) {
            BlockPos pos = new BlockPos(x, y + i, z);
            this.placeLeavesRow(level, foliageSetter, source, config, attachment.pos().above(i),
                         2, i + 1, attachment.doubleTrunk());
            tryPlaceLeaf(level, foliageSetter, source, config, pos);
        }
    }

    @Override
    public int foliageHeight(@NotNull RandomSource source, int height, @NotNull TreeConfiguration config) { return this.height; }

    @Override
    protected boolean shouldSkipLocation(@NotNull RandomSource source, int localX, int localY, int localZ,
                                         int range, boolean large) { return false; }
}