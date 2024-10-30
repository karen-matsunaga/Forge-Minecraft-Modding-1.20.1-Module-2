package net.karen.mccourse.util;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;

public class ModWoodTypes {
    // Register all custom wood types
    public static final WoodType WALNUT = WoodType.register(new WoodType(MCCourseMod.MOD_ID + ":walnut", BlockSetType.OAK));
}