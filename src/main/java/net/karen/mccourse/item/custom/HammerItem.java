package net.karen.mccourse.item.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

// Credits by Kaupenjoe - https://github.com/Kaupenjoe/Forge-Course-1.20.X/tree/22-customHammer
// Distributed under MIT - Using with some modifications

// Credits by Alireza Khodakarami (Jiraiyah) - https://github.com/drkhodakarami/uio/blob/master/src/main/java/jiraiyah/uio/item/HammerItem.java
// Distributed under MIT - Using with some modifications
public class HammerItem extends DiggerItem implements Vanishable {
    private final int radius;

    public HammerItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, TagKey<Block> blockTags,
                      Properties pProperties, int radius) {
        super(pAttackDamageModifier, pAttackSpeedModifier, pTier, blockTags, pProperties);
        this.radius = radius - 1; // Radius declared on ModItems
    }

    public int getRadius() { return this.radius; } // Declared radius activated on ModEvents

    // Player to receive the blocks destroyed
    public static List<BlockPos> getBlocksToBeDestroyed(int radius, BlockPos initalBlockPos, ServerPlayer player) {
        List<BlockPos> positions = new ArrayList<>();

        BlockHitResult traceResult = player.level().clip(new ClipContext(player.getEyePosition(1f),
                (player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f))),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (traceResult.getType() == HitResult.Type.MISS) { return positions; }

        // Directions in that player broken a block - (DOWN/UP, NORTH/SOUTH and EAST/WEST)
        // Check which face of the block was hit to determine the breaking plane
        if (traceResult.getDirection() == Direction.DOWN || traceResult.getDirection() == Direction.UP) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY(), initalBlockPos.getZ() + y));
                }
            }
        }

        if (traceResult.getDirection() == Direction.NORTH || traceResult.getDirection() == Direction.SOUTH) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX() + x, initalBlockPos.getY() + y, initalBlockPos.getZ()));
                }
            }
        }

        if (traceResult.getDirection() == Direction.EAST || traceResult.getDirection() == Direction.WEST) {
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    positions.add(new BlockPos(initalBlockPos.getX(), initalBlockPos.getY() + y, initalBlockPos.getZ() + x));
                }
            }
        }
        return positions;
    }

    // Appears tooltip on screen of Hammer, Paxel, etc.
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("Hammer breaks: " + (this.radius * 2 + 1) + "x" + (this.radius * 2 + 1)));
    }
}