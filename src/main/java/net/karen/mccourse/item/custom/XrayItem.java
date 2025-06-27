package net.karen.mccourse.item.custom;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.network.GlowingBlocksNetworkMessage;
import net.karen.mccourse.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.Tags;
import org.joml.Matrix4f;
import java.util.List;
import java.util.Map;
import static net.karen.mccourse.util.Utils.enchant;
import static net.karen.mccourse.util.Utils.has;

public class XrayItem {
    // Render Level block shape variables
    private static BufferBuilder bufferBuilder = null;
    private static VertexBuffer vertexBuffer = null;
    private static VertexFormat format = null;
    private static PoseStack poseStack = null;
    private static Matrix4f projectionMatrix = null;
    private static final boolean worldCoordinate = true;
    private static final Vec3 offset = Vec3.ZERO;
    private static int currentStage, targetStage = 0; // NONE: 0; SKY: 1; WORLD: 2.
    private static final Map<String, Map<TagKey<Block>, Integer>> renderColors = Map.of("glowing",
    Map.ofEntries(Map.entry(Tags.Blocks.ORES_COAL, 0xFFa9a9a9), Map.entry(Tags.Blocks.ORES_COPPER, 0xFFff8c00),
    Map.entry(Tags.Blocks.ORES_DIAMOND, 0xFF00FEFF), Map.entry(Tags.Blocks.ORES_EMERALD, 0xFF31c831),
    Map.entry(Tags.Blocks.ORES_GOLD, 0xFFffd700), Map.entry(Tags.Blocks.ORES_IRON, 0xFFd3d3d3),
    Map.entry(Tags.Blocks.ORES_LAPIS, 0xFF0000ff), Map.entry(Tags.Blocks.ORES_REDSTONE, 0xFFb30000),
    Map.entry(Tags.Blocks.ORES_NETHERITE_SCRAP, 0xFFD22CF8), Map.entry(ModTags.Blocks.MCCOURSE_ORES, 0xFFffc0eb)),
    "detector", Map.ofEntries(Map.entry(ModTags.Blocks.SPECIAL_METAL_DETECTOR_VALUABLES, 0xFF157ccb)));

    // CUSTOM METHOD - Added all blocks shape with respective color
    public static void add(double x, double y, double z, int color) {
        if (bufferBuilder == null || !bufferBuilder.building()) { return; }
        if (format == DefaultVertexFormat.POSITION_COLOR) { bufferBuilder.vertex(x, y, z).color(color).endVertex(); }
    }

    // CUSTOM METHOD - Building all block shape with respective mode and format
    public static boolean begin() {
        if (bufferBuilder == null || !bufferBuilder.building()) {
            clear();
            if (vertexBuffer == null) {
                VertexFormat.Mode mode = VertexFormat.Mode.DEBUG_LINES;
                format = DefaultVertexFormat.POSITION_COLOR;
                bufferBuilder = Tesselator.getInstance().getBuilder();
                bufferBuilder.begin(mode, format);
                return true;
            }
        }
        return false;
    }

    // CUSTOM METHOD - Before creating the blocks, cleaning is done
    public static void clear() {
        if (vertexBuffer != null) { vertexBuffer.close(); vertexBuffer = null; }
    }

    // CUSTOM METHOD - After creating the block
    public static void end() {
        if (bufferBuilder == null || !bufferBuilder.building()) { return; }
        if (vertexBuffer != null) { vertexBuffer.close(); }
        vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        vertexBuffer.bind();
        vertexBuffer.upload(bufferBuilder.end());
        VertexBuffer.unbind();
    }

    // CUSTOM METHOD - Render block shape
    public static void renderShape(VertexBuffer vertexBuffer,
                                   double x, double y, double z, int color) {
        if (currentStage == 0 || currentStage != targetStage) { return; }
        if (poseStack == null || projectionMatrix == null) { return; }
        if (vertexBuffer == null) { return; }
        float i, j, k;
        if (worldCoordinate) {
            Vec3 pos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            i = (float) (x - pos.x()); j = (float) (y - pos.y()); k = (float) (z - pos.z());
        }
        else { i = (float) x; j = (float) y; k = (float) z; }
        poseStack.pushPose();
        poseStack.translate(i, j, k);
        poseStack.mulPose(Axis.YN.rotationDegrees(0));
        poseStack.mulPose(Axis.XP.rotationDegrees(0));
        poseStack.mulPose(Axis.ZN.rotationDegrees(0));
        poseStack.scale(1, 1, 1);
        poseStack.translate(offset.x(), offset.y(), offset.z());
        RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F,
        (color & 255) / 255.0F, (color >>> 24) / 255.0F);
        vertexBuffer.bind();
        ShaderInstance shader = vertexBuffer.getFormat().hasUV(0) ? GameRenderer.getPositionTexColorShader()
                                                                        : GameRenderer.getPositionColorShader();
        if (shader != null) { vertexBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shader); }
        VertexBuffer.unbind();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    // CUSTOM METHOD - Render block shape on world
    public static void stage(List<Integer> stage, List<Boolean> bool, RenderLevelStageEvent event) {
        currentStage = stage.get(0);
        RenderSystem.depthMask(bool.get(0));
        renderShapes(event);
        RenderSystem.enableCull();
        RenderSystem.depthMask(bool.get(1));
        currentStage = stage.get(1);
    }

    // CUSTOM METHOD - Created block shape with all blocks and colors defined on renderColors variable
    public static void renderShapes(RenderLevelStageEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        Entity entity = minecraft.gameRenderer.getMainCamera().getEntity();
        if (level != null) {
            poseStack = event.getPoseStack();
            projectionMatrix = event.getProjectionMatrix();
            Vec3 pos = entity.getPosition(event.getPartialTick());
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int RadiusSquare = 5; // Horizontal and Vertical radius square
            for (int i = -RadiusSquare; i <= RadiusSquare; i++) {
                for (int xi = -RadiusSquare; xi <= RadiusSquare; xi++) {
                    for (int zi = -RadiusSquare; zi <= RadiusSquare; zi++) {
                        // Execute the desired statements within the square/cube
                        if (GlowingBlocksNetworkMessage.World.get(level).xray) {
                            double posX = Math.floor(pos.x + xi), posY = Math.floor(pos.y + i), posZ = Math.floor(pos.z + zi);
                            BlockPos position = BlockPos.containing(posX, posY, posZ);
                            int[][] cubeCoordinates = {
                            {0,0,0},{1,0,0},{1,0,0},{1,0,1},{1,0,1},{0,0,1}, {0,0,1},{0,0,0},{0,0,0},{0,1,0},{1,0,0},{1,1,0},
                            {1,0,1},{1,1,1},{0,0,1},{0,1,1},{0,1,0},{1,1,0}, {1,1,0},{1,1,1},{1,1,1},{0,1,1},{0,1,1},{0,1,0}};
                            Map<TagKey<Block>, Integer> render = getActiveRenderColors(Minecraft.getInstance().player);
                            if (!render.isEmpty()) {
                                render.forEach((key, value) -> {
                                    if (level.getBlockState(position).is(key)) {
                                        RenderSystem.depthMask(false);
                                        RenderSystem.disableDepthTest();
                                        if (begin()) { for (int[] c : cubeCoordinates) { add(c[0], c[1], c[2], value); } end(); }
                                        if (currentStage == 2) {
                                            targetStage = 2;
                                            renderShape(vertexBuffer, posX, posY, posZ, value);
                                            targetStage = 0;
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }
    }

    // CUSTOM METHOD - Select ITEM mode
    public static String getActiveMode(Player player) {
        ItemStack helmet = has(player, EquipmentSlot.HEAD), hand = has(player, EquipmentSlot.MAINHAND);
        boolean hasGlowing = helmet.isEnchanted() && enchant(helmet, ModEnchantments.GLOWING_BLOCKS.get()) > 0,
                hasMetal = hand.is(ModItems.METAL_DETECTOR.get()), hasSpecial = hand.is(ModItems.SPECIAL_METAL_DETECTOR.get());
        if (hasSpecial) { return "detector"; } // Specific blocks = Detector mode
        if (hasGlowing || hasMetal) { return "glowing"; } // Ores blocks = Glowing mode
        return "none"; // Default mode
    }

    // CUSTOM METHOD - Select ITEM render blocks
    public static Map<TagKey<Block>, Integer> getActiveRenderColors(Player player) {
        return renderColors.getOrDefault(getActiveMode(player), Map.of());
    }
}