package net.karen.mccourse.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.NotNull;

public class ImageTooltipComponent implements ClientTooltipComponent, TooltipComponent {
    private final ResourceLocation texture; // ICON
    private final int width, height; // WIDTH AND HEIGHT OF TEXTURE
    private final Component text;

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
        graphics.blit(texture, x, y, 0, 0, width, height, width, height); // RENDER TEXTURE
        poseStack.popPose();
        // Render Text
        poseStack.pushPose();
        int textX = x + width + 4; // 4px image spacing
        int textY = y + (height - font.lineHeight) / 2; // Center vertically
        graphics.drawString(font, text, textX, textY, 0xFFFFFF, false);
        poseStack.popPose();
    }

    @Override
    public int getHeight() { return height; }

    @Override
    public int getWidth(@NotNull Font font) { return width + 4 + font.width(text); } // Image width + spacing + text width
}