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

// Credits by Alireza Khodakarami (Jiraiyah)
// https://github.com/drkhodakarami/uio/blob/master/src/main/java/jiraiyah/uio/item/HammerItem.java
// Distributed under MIT - Using with some modifications
public class HammerItem extends DiggerItem implements Vanishable {
    private final int radius;
    private final boolean infinite;

    public HammerItem(Tier tier, float pAttackDamageModifier, float pAttackSpeedModifier,
                      TagKey<Block> blockTags, Properties properties, int radius, boolean infinite) {
        super(pAttackDamageModifier, pAttackSpeedModifier, tier, blockTags, properties);
        this.radius = radius - 1; // Radius declared on ModItems
        this.infinite = infinite;
    }

    public int getRadius() { return this.radius; } // Declared radius activated on ModEvents

    // Player to receive the blocks destroyed
    public static List<BlockPos> getBlocksToBeDestroyed(int radius, BlockPos initalBlockPos,
                                                        ServerPlayer player) {
        List<BlockPos> positions = new ArrayList<>(); // Block direction position
        BlockHitResult traceResult = player.level().clip(new ClipContext(player.getEyePosition(1f),
                (player.getEyePosition(1f).add(player.getViewVector(1f).scale(6f))),
                ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));

        if (traceResult.getType() == HitResult.Type.MISS) { return positions; }

        // Directions in that player broken a block - (DOWN/UP, NORTH/SOUTH and EAST/WEST)
        // Check which face of the block was hit to determine the breaking plane
        blockPos(traceResult, List.of(Direction.DOWN, Direction.UP), positions, initalBlockPos, radius);
        blockPos(traceResult, List.of(Direction.NORTH, Direction.SOUTH), positions, initalBlockPos, radius);
        blockPos(traceResult, List.of(Direction.EAST, Direction.WEST), positions, initalBlockPos, radius);
        return positions;
    }

    // Appears tooltip on screen of Hammer.
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, level, components, tooltipFlag);
        components.add(Component.translatable("Hammer breaks: " + (this.radius * 2 + 1) + "x" + (this.radius * 2 + 1)));
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level level, @NotNull Player player) {
        super.onCraftedBy(stack, level, player);
        if (infinite) {
            CompoundTag tag = stack.getOrCreateTag(); // Added Unbreakable tag
            tag.putBoolean("Unbreakable", true);
        }
    }

    private static void blockPos(BlockHitResult hit, List<Direction> dir, List<BlockPos> pos,
                                 BlockPos block, int rad) {
        if (hit.getDirection() == dir.get(0) || hit.getDirection() == dir.get(1)) {
            for (int x = -rad; x <= rad; x++) {
                for (int y = -rad; y <= rad; y++) {
                    switch (dir.get(0)) {
                        case DOWN, UP -> pos.add(new BlockPos(block.getX() + x, block.getY(), block.getZ() + y));
                        case NORTH, SOUTH -> pos.add(new BlockPos(block.getX() + x, block.getY() + y, block.getZ()));
                        case EAST, WEST -> pos.add(new BlockPos(block.getX(), block.getY() + y, block.getZ() + x));
                    }
                }
            }
        }
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
            if (ticksRemaining == 0) { highlightedBlocks = List.of(); } // Clean Highlighted Blocks list
        }
    }

    // Render highlight blocks
    public static void renderHighlight(PoseStack pose, Camera camera, MultiBufferSource buffer) {
        if (!highlightedBlocks.isEmpty()) {
            Vec3 camPos = camera.getPosition(); // Player position
            highlightedBlocks.forEach(pos -> { AABB box = new AABB(pos).move(-camPos.x, -camPos.y, -camPos.z);
                drawBox(pose, buffer, box, 1f, 1f, 0f, 0.4f); }); // Highlighted blocks transparent Yellow
        }
    }

    // Draw Box to highlight blocks
    public static void drawBox(PoseStack pose, MultiBufferSource buffer, AABB box,
                               float r, float g, float b, float alpha) {
        VertexConsumer builder = buffer.getBuffer(RenderType.lines());
        LevelRenderer.renderLineBox(pose, builder, box, r, g, b, alpha);
    }
}