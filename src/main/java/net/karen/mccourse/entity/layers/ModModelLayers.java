package net.karen.mccourse.entity.layers;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    // Registry all custom entities model layers
    public static final ModelLayerLocation RHINO_LAYER = new ModelLayerLocation(
            new ResourceLocation(MCCourseMod.MOD_ID, "rhino_layer"), "rhino_layer");
}