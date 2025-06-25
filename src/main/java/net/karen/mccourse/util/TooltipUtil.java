package net.karen.mccourse.util;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import java.util.List;
import static net.minecraft.client.renderer.MultiBufferSource.*;

public class TooltipUtil implements ClientTooltipComponent, TooltipComponent {
    private static List<Component> itemLine;
    private int scrollOffset = 0;
    private static final int MAX_LINES_VISIBLE = 5, LINE_HEIGHT = 10;

    public TooltipUtil(List<Component> itemLine) {
        TooltipUtil.itemLine = itemLine;
    }

    public void scrollUp() {
        scrollOffset = Math.max(0, scrollOffset - 1);
    }

    public void scrollDown() {
        scrollOffset = Math.min(Math.max(0, itemLine.size() - MAX_LINES_VISIBLE), scrollOffset + 1);
    }

    @Override
    public int getHeight() {
        return Math.min(itemLine.size(), MAX_LINES_VISIBLE) * LINE_HEIGHT;
    }

    @Override
    public int getWidth(Font font) {
        return itemLine.stream().mapToInt(font::width).max().orElse(0);
    }

    @Override
    public void renderText(@NotNull Font font, int x, int y, @NotNull Matrix4f matrix, @NotNull BufferSource bufferSource) {
        for (int i = 0; i < MAX_LINES_VISIBLE; i++) {
            int index = i + scrollOffset;
            if (index >= itemLine.size()) { break; }
            font.drawInBatch(itemLine.get(index).getString(), x, y + i * LINE_HEIGHT, 0xFFFFFF, false,
            matrix, bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        }
    }
}