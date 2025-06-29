package net.karen.mccourse.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class MinerBlock extends Block {
    public MinerBlock(Properties properties) { super(properties); }

    @Override
    public void onPlace(@NotNull BlockState state, Level level, @NotNull BlockPos pos,
                        @NotNull BlockState oldState, boolean isMoving) {
        if (!level.isClientSide()) { level.scheduleTick(pos, this, 60); }
    }

    @Override
    public void tick(@NotNull BlockState state, ServerLevel level,
                     @NotNull BlockPos pos, @NotNull RandomSource random) {
        if (level.getBlockState(pos).getBlock() == this) { level.removeBlock(pos, false); }
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState pState) { return RenderShape.INVISIBLE; }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos,
                                        @NotNull CollisionContext pContext) { return Shapes.empty(); }
}