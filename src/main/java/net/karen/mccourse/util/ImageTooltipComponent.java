package net.karen.mccourse.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.NotNull;

public class ImageTooltipComponent implements ClientTooltipComponent, TooltipComponent {
    private final ResourceLocation texture; // IMAGE appears on screen
    private final int width, height; // WIDTH and HEIGHT of texture
    private final Component text; // TEXT appears next to ICON

    public ImageTooltipComponent(ResourceLocation texture, int width, int height, Component text) {
        this.texture = texture;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    @Override
    public void renderImage(@NotNull Font font, int x, int y, @NotNull GuiGraphics graphics) {
        PoseStack poseStack = graphics.pose();
        // Render Image
        poseStack.pushPose();
        graphics.blit(texture, x, y, 0, 0, width, height, width, height); // Render TEXTURE
        poseStack.popPose();
        // Render Text
        poseStack.pushPose();
        int textX = x + width + 4; // 4px image spacing
        int textY = y + (height - font.lineHeight) / 2; // Center vertically
        graphics.drawString(font, text, textX, textY, 0xFFFFFF, false); // Render TEXT
        poseStack.popPose();
    }

    @Override
    public int getHeight() { return height; } // Image height

    @Override
    public int getWidth(@NotNull Font font) { return width + 4 + font.width(text); } // Image width + spacing + text width
}