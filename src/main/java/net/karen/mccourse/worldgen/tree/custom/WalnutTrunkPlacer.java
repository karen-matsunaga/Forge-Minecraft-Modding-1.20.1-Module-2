package net.karen.mccourse.worldgen.tree.custom;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.karen.mccourse.worldgen.tree.ModTrunkPlacerTypes;
import net.minecraft.core.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.*;
import org.jetbrains.annotations.NotNull;
import java.util.List;
import java.util.function.BiConsumer;

public class WalnutTrunkPlacer extends TrunkPlacer {
    public static final Codec<WalnutTrunkPlacer> CODEC = RecordCodecBuilder.create(walnutTrunkPlacerInstance ->
           trunkPlacerParts(walnutTrunkPlacerInstance).apply(walnutTrunkPlacerInstance, WalnutTrunkPlacer::new));

    public WalnutTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected @NotNull TrunkPlacerType<?> type() { return ModTrunkPlacerTypes.WALNUT_TRUNK_PLACER.get(); }

    @Override
    public @NotNull List<FoliagePlacer.FoliageAttachment> placeTrunk(@NotNull LevelSimulatedReader level,
                                                                     @NotNull BiConsumer<BlockPos, BlockState> blockSetter,
                                                                     @NotNull RandomSource source, int freeTreeHeight,
                                                                     BlockPos pos, @NotNull TreeConfiguration config) {
        // THIS IS WHERE THE BLOCK PLACING LOGIC IS!
        setDirtAt(level, blockSetter, source, pos.below(), config);
        int heightA = source.nextInt(heightRandA, heightRandA + 3),
            heightB = source.nextInt(heightRandB - 1, heightRandB + 1), height = freeTreeHeight + heightA + heightB;
        for (int i = 0; i < height; i++) {
            placeLog(level, blockSetter, source, pos.above(i), config);
            if (i % 2 == 0 && source.nextBoolean()) {
                placeLogBlock(level, blockSetter, source, pos, i, Direction.NORTH, config);
                placeLogBlock(level, blockSetter, source, pos, i, Direction.SOUTH, config);
                placeLogBlock(level, blockSetter, source, pos, i, Direction.EAST, config);
                placeLogBlock(level, blockSetter, source, pos, i, Direction.WEST, config);
            }
        }
        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(pos.above(height), 0, false));
    }

    // CUSTOM METHOD - Place LOG block position
    private void placeLogBlock(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockSetter,
                               RandomSource source, BlockPos pos, int value, Direction direction, TreeConfiguration config) {
        if (source.nextFloat() > 0.25f) {
            for (int x = 0; x < 4; x++) { placeLog(level, blockSetter, source, pos.above(value).relative(direction, x), config); }
        }
    }
}