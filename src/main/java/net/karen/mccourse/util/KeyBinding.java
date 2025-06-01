package net.karen.mccourse.util;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class KeyBinding {
    public static final String KEY_CATEGORY_MCCOURSE = "key.category.mccourse"; // Mccourse custom category
    public static final String KEY_GLOWING_BLOCKS = "key.mccourse_glowing_blocks"; // Glowing Blocks custom key input
    public static final String KEY_GLOWING_MOBS = "key.mccourse_glowing_mobs"; // Glowing Mobs custom key input

    // Register all custom key binding
    public static final KeyMapping GLOWING_BLOCKS_KEY = new KeyMapping(KEY_GLOWING_BLOCKS, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_G, KEY_CATEGORY_MCCOURSE);

    public static final KeyMapping GLOWING_MOBS_KEY = new KeyMapping(KEY_GLOWING_MOBS, KeyConflictContext.IN_GAME,
            InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, KEY_CATEGORY_MCCOURSE);
}