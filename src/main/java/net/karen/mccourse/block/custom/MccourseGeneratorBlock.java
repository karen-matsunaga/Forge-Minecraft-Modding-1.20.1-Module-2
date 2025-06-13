package net.karen.mccourse.block.custom;

import net.karen.mccourse.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

public class MccourseGeneratorBlock extends Block {
    private final String type; // TYPE of drop (RANDOM or ALL)
    private final TagKey<Block> blocks; // BLOCK TAG to generate drops
    public MccourseGeneratorBlock(Properties properties, String type, TagKey<Block> blocks) {
        super(properties);
        this.type = type;
        this.blocks = blocks;
    }

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
                        var tags = blockTag.getTag(blocks);
                        if (type.contains("RANDOM")) { // DROP RANDOM BLOCK
                            tags.getRandomElement(RandomSource.create()).ifPresent(drop -> drops(level, pos, drop.asItem()));
                        }
                        // DROP ALL BLOCK CONTAINED ON BLOCK TAG
                        else if (type.contains("ALL")) { tags.forEach(drop -> drops(level, pos, drop.asItem())); }
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
        return player.getMainHandItem().is(ModItems.PINK_PICKAXE.get()); // Checks if the player is holding the item is PINK PICKAXE
    }

    private static void drops(Level level, BlockPos pos, Item drop) {
        ItemEntity drops = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(drop.asItem()));
        drops.setDeltaMovement(Vec3.ZERO);
        level.addFreshEntity(drops); // CUSTOM METHOD - Drop BLOCKS on ground
    }
}