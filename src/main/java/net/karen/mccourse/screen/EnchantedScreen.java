package net.karen.mccourse.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.*;

@OnlyIn(Dist.CLIENT)
public class EnchantedScreen extends AbstractContainerScreen<EnchantedMenu> {
    private static final ResourceLocation texture = new ResourceLocation("mccourse:textures/gui/enchanted_gui.png");
    private final Level world;
    private final int x, y, z;
    private final Player entity;

    public EnchantedScreen(EnchantedMenu container, Inventory inventory, Component text) {
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

        ItemStack bookToEnchant = menu.getSlot(0).getItem(); // Input Book
        ItemStack requiredItem = menu.getSlot(1).getItem();  // Input Item
//        ItemStack outputSlot = menu.getSlot(2).getItem(); // Output

        if (!bookToEnchant.isEmpty() && bookToEnchant.getItem() == Items.BOOK && !requiredItem.isEmpty()) {
            // Sets the items needed for each enchantment
            Map<Enchantment, Ingredient> requiredItemsMap = new HashMap<>();
            requiredItemsMap.put(Enchantments.BLOCK_FORTUNE, Ingredient.of(Items.DIAMOND));
            requiredItemsMap.put(Enchantments.BLOCK_EFFICIENCY, Ingredient.of(Items.EMERALD));
            requiredItemsMap.put(Enchantments.UNBREAKING, Ingredient.of(Items.IRON_INGOT));

            for (Map.Entry<Enchantment, Ingredient> entry : requiredItemsMap.entrySet()) {
                Enchantment enchantment = entry.getKey(); // Enchantment
                Ingredient ingredient = entry.getValue();  // Ingredient

                if (ingredient.test(requiredItem)) {
                    int level = requiredItem.getCount() / 64; // Sets the level by quantity
                    if (level > 0) {
                        ItemStack preview = new ItemStack(Items.ENCHANTED_BOOK);
                        EnchantedBookItem.addEnchantment(preview, new EnchantmentInstance(enchantment, level));

                        // Renders the visual
                        int x = this.leftPos + 138;
                        int y = this.topPos + 47;
                        guiGraphics.renderItem(preview, x, y);
                        // Tooltip if the mouse is over the top
                        if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                            guiGraphics.renderTooltip(this.font, preview, mouseX, mouseY);
                        }
                        break; // It only shows an enchantment
                    }
                }
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
        guiGraphics.drawString(this.font, Component.literal("Enchanted"), 53, 11, -12829636, false);
        guiGraphics.drawString(this.font, Component.literal("Item"), 24, 30, -12829636, false);
        guiGraphics.drawString(this.font, Component.literal("Resource"), 77, 30, -12829636, false);
    }

    @Override
    public void init() { super.init(); } // Container (GUI) is opened
}