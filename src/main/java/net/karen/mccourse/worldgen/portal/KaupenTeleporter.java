package net.karen.mccourse.worldgen.portal;

import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.custom.KaupenPortalBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.util.ITeleporter;
import java.util.function.Function;

public class KaupenTeleporter implements ITeleporter {
    public static BlockPos thisPos = BlockPos.ZERO;
    public static boolean insideDimension = true;

    public KaupenTeleporter(BlockPos pos, boolean insideDim) {
        thisPos = pos; insideDimension = insideDim;
    }

    @Override
    public Entity placeEntity(Entity entity, ServerLevel cWorld, ServerLevel dWorld, float yaw, Function<Boolean, Entity> posEntity) {
        entity = posEntity.apply(false); // REPOSITION entity
        int posY = 61;
        if (!insideDimension) { posY = thisPos.getY(); }
        BlockPos pos = new BlockPos(thisPos.getX(), posY, thisPos.getZ()); // Destination POSITION
        int tries = 0, x = pos.getX(), y = pos.getY(), z = pos.getZ(); // WHILE loop
        // Destination WORLD
        while (isBlock(dWorld, pos) && isBlock(dWorld, pos.above()) && (tries < 25)) { pos = pos.above(2); tries++; }
        entity.setPos(x, y, z);
        if (insideDimension) {
            boolean doSetBlock = true;
            BlockPos below = pos.below(10).west(10), above = pos.above(10).east(10);
            for (BlockPos checkPos : BlockPos.betweenClosed(below, above)) {
                if (dWorld.getBlockState(checkPos).getBlock() instanceof KaupenPortalBlock) { doSetBlock = false; break; }
            }
            if (doSetBlock) { dWorld.setBlock(pos, ModBlocks.KAUPEN_PORTAL.get().defaultBlockState(), 3); }
        }
        return entity;
    }

    // CUSTOM METHOD - Detected block
    private boolean isBlock(ServerLevel serverLevel, BlockPos position) {
        BlockState block = serverLevel.getBlockState(position);
        return (block.getBlock() != Blocks.AIR) && !block.canBeReplaced(Fluids.WATER);
    }
}