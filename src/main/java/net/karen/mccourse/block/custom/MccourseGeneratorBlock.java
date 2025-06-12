package net.karen.mccourse.block.custom;

import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class MccourseGeneratorBlock extends Block {
    public MccourseGeneratorBlock(Properties properties) { super(properties); }

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player,
                                       boolean willHarvest, FluidState fluid) {
        boolean destroy = player.isCreative() || player.getMainHandItem().is(ModItems.PINK_PICKAXE.get());
        if (!level.isClientSide()) {
            // ENABLES destruction on CREATIVE mode or uses PINK PICKAXE, but not received DROP items
            if (destroy) { return level.destroyBlock(pos, false); }
            else {
                if (state.getBlock().equals(this)) { // Is Mccourse Generator custom block
                    var blockTag = ForgeRegistries.BLOCKS.tags();
                    if (blockTag != null) {
                        blockTag.getTag(ModTags.Blocks.ALL_ORES).getRandomElement(RandomSource.create()).ifPresent(drop -> {
                            ItemEntity drops = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(drop));
                            drops.setDeltaMovement(Vec3.ZERO);
                            level.addFreshEntity(drops); });
                    }
                }
                level.sendBlockUpdated(pos, state, state, 3); // PREVENTS destruction of block
                return false;
            }
        }
        return destroy; // On the SERVER side, it allows only if you are creative
    }

    @Override
    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        // Checks if the player is holding the specific item is PINK PICKAXE
        return player.getMainHandItem().is(ModItems.PINK_PICKAXE.get());
    }
}