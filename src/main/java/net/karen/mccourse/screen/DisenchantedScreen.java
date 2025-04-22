package net.karen.mccourse.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;

import java.util.Map;

public class DisenchantedScreen extends AbstractContainerScreen<DisenchantedMenu> {
    private static final ResourceLocation texture = new ResourceLocation("mccourse:textures/gui/disenchanted_gui.png");
    private final Level world;
    private final int x, y, z;
    private final Player entity;

    public DisenchantedScreen(DisenchantedMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.world = container.world;
        this.x = container.x;
        this.y = container.y;
        this.z = container.z;
        this.entity = container.entity;
        this.imageWidth = 176;
        this.imageHeight = 166;
    }

    // Render item before player received enchanted book
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        ItemStack inputItem = menu.getSlot(0).getItem(); // Item
        ItemStack book = menu.getSlot(1).getItem(); // Book

        if (!inputItem.isEmpty() && inputItem.isEnchanted() && book.getItem() == Items.BOOK) {
            // Creates the enchanted book with the same enchantments
            Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(inputItem);
            ItemStack preview = new ItemStack(Items.ENCHANTED_BOOK);
            EnchantmentHelper.setEnchantments(enchants, preview);

            // Renders the fake item (Visual)
            guiGraphics.renderItem(preview, this.leftPos + 138, this.topPos + 47);

            // Checks if the mouse is on top of the slot
            if (mouseX >= this.leftPos + 138 && mouseX < this.leftPos + 138 + 16 && mouseY >= this.topPos + 47 && mouseY < this.topPos + 47 + 16) {
                guiGraphics.renderTooltip(this.font, preview, mouseX, mouseY); // Tooltip with enchantments!
            }
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    // Render background GUI
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, this.imageWidth, this.imageHeight);
        RenderSystem.disableBlend();
    }

    // Player pressed ESC or E key buttons closed Container
    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) { this.minecraft.player.closeContainer(); return true; }
        return super.keyPressed(key, b, c);
    }

    // Render text GUI
    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.literal("Disenchanted"), 53, 11, -12829636, false);
        guiGraphics.drawString(this.font, Component.literal("Item"), 24, 30, -12829636, false);
        guiGraphics.drawString(this.font, Component.literal("Book"), 77, 30, -12829636, false);
    }

    @Override
    public void init() { super.init(); } // Container (GUI) is opened
}