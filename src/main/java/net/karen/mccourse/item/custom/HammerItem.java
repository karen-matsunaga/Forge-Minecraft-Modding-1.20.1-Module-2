package net.karen.mccourse.item.custom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
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
    private final boolean infinite;

    public HammerItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, TagKey<Block> blockTags,
                      Properties pProperties, int radius, boolean infinite) {
        super(pAttackDamageModifier, pAttackSpeedModifier, pTier, blockTags, pProperties);
        this.radius = radius - 1; // Radius declared on ModItems
        this.infinite = infinite;
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

    // Appears tooltip on screen of Hammer.
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(Component.translatable("Hammer breaks: " + (this.radius * 2 + 1) + "x" + (this.radius * 2 + 1)));
    }

    // Hammer Highlight Block
    private static List<BlockPos> highlightedBlocks = List.of(); // Preview Blocks
    private static int ticksRemaining = 0;

    public static void setHighlightedBlocks(List<BlockPos> blocks) {
        highlightedBlocks = blocks;
        ticksRemaining = 20; // 1 second highlight disappears
    }

    public static void clientTick() {
        if (ticksRemaining > 0) {
            ticksRemaining--;
            if (ticksRemaining == 0) {
                highlightedBlocks = List.of(); // Clean Highlighted Blocks list
            }
        }
    }

    // Render highlight blocks
    public static void renderHighlight(PoseStack poseStack, Camera camera, MultiBufferSource bufferSource) {
        if (highlightedBlocks.isEmpty()) { return; }
        Vec3 camPos = camera.getPosition(); // Player position
        // Highlighted Blocks Color
        for (BlockPos pos : highlightedBlocks) {
            AABB box = new AABB(pos).move(-camPos.x, -camPos.y, -camPos.z);
            drawBox(poseStack, bufferSource, box, 1f, 1f, 0f, 0.4f); // Transparent Yellow
        }
    }

    // Draw Box to highlight blocks
    public static void drawBox(PoseStack stack, MultiBufferSource buffer, AABB box,
                               float r, float g, float b, float alpha) {
        VertexConsumer builder = buffer.getBuffer(RenderType.lines());
        LevelRenderer.renderLineBox(stack, builder, box, r, g, b, alpha);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level level, @NotNull Player player) {
        super.onCraftedBy(stack, level, player);
        if (infinite) {
            ItemStack itemStack = new ItemStack(stack.getItem()); // Get item
            CompoundTag tag = itemStack.getOrCreateTag(); // Added Unbreakable tag
            tag.putBoolean("Unbreakable", true);
            if (!player.getInventory().add(itemStack)) {
                player.drop(itemStack, false);  // If the inventory is full, drop to the ground
            }
        }
    }
}