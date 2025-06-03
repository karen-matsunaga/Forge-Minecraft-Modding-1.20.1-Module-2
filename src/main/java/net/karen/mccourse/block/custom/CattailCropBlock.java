package net.karen.mccourse.block.custom;

import net.karen.mccourse.item.ModItems;
import net.minecraft.core.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.phys.shapes.*;
import net.minecraftforge.common.*;
import org.jetbrains.annotations.NotNull;
import static net.minecraftforge.common.ForgeHooks.*;

public class CattailCropBlock extends CropBlock {
    public static final int FIRST_STAGE_MAX_AGE = 7;
    public static final int SECOND_STAGE_MAX_AGE = 1;
    private static final VoxelShape[] SHAPE_BY_AGE =
            new VoxelShape[]{
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 8);

    public CattailCropBlock(Properties pProperties) { super(pProperties); }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE_BY_AGE[this.getAge(state)];
    }

    @Override
    public void randomTick(@NotNull BlockState state, ServerLevel level, @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (level.getRawBrightness(pos, 0) >= 9) {
            int currentAge = this.getAge(state);
            if (currentAge < this.getMaxAge()) {
                float f = getGrowthSpeed(this, level, pos);
                if (onCropsGrowPre(level, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                    if (currentAge == FIRST_STAGE_MAX_AGE) {
                        if (level.getBlockState(pos.above(1)).is(Blocks.AIR)) {
                            level.setBlock(pos.above(1), this.getStateForAge(currentAge + 1), 2);
                        }
                    }
                    else { level.setBlock(pos, this.getStateForAge(currentAge + 1), 2); }
                    onCropsGrowPost(level, pos, state);
                }
            }
        }
    }

    @Override
    public void growCrops(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
        int nextAge = this.getAge(state) + this.getBonemealAgeIncrease(level);
        int maxAge = this.getMaxAge();
        if (nextAge > maxAge) { nextAge = maxAge; }
        if (this.getAge(state) == FIRST_STAGE_MAX_AGE && level.getBlockState(pos.above(1)).is(Blocks.AIR)) {
            level.setBlock(pos.above(1), this.getStateForAge(nextAge), 2);
        }
        else { level.setBlock(pos, this.getStateForAge(nextAge - SECOND_STAGE_MAX_AGE), 2); }
    }

    @Override
    public boolean canSustainPlant(@NotNull BlockState state, @NotNull BlockGetter world,
                                   @NotNull BlockPos pos, @NotNull Direction facing, @NotNull IPlantable plantable) {
        return super.mayPlaceOn(state, world, pos);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState state, @NotNull LevelReader level, @NotNull BlockPos pos) {
        return super.canSurvive(state, level, pos) || (level.getBlockState(pos.below(1)).is(this) &&
                level.getBlockState(pos.below(1)).getValue(AGE) == 7);
    }

    @Override
    public int getMaxAge() { return FIRST_STAGE_MAX_AGE + SECOND_STAGE_MAX_AGE; }

    @Override
    protected @NotNull ItemLike getBaseSeedId() { return ModItems.CATTAIL_SEEDS.get(); }

    @Override
    public @NotNull IntegerProperty getAgeProperty() { return AGE; }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(AGE); }
}