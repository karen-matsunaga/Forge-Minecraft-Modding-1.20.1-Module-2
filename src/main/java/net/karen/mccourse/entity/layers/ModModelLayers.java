package net.karen.mccourse.entity.layers;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    // Registry all custom entities model layers
    public static final ModelLayerLocation RHINO_LAYER = new ModelLayerLocation(
            new ResourceLocation(MCCourseMod.MOD_ID, "rhino_layer"), "rhino_layer");

    // Registry all custom entities projectiles model layers
    public static final ModelLayerLocation MAGIC_PROJECTILE_LAYER = new ModelLayerLocation(
            new ResourceLocation(MCCourseMod.MOD_ID, "magic_projectile_layer"), "magic_projectile_layer");

    // Registry all custom boats
    public static final ModelLayerLocation WALNUT_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(MCCourseMod.MOD_ID, "boat/walnut"), "main"); // Custom boat

    public static final ModelLayerLocation WALNUT_CHEST_BOAT_LAYER = new ModelLayerLocation(
            new ResourceLocation(MCCourseMod.MOD_ID, "chest_boat/walnut"), "main"); // Custom chest boat

    // Registry all custom elytra - DIAMOND ELYTRA
    public static ModelLayerLocation DIAMOND_ELYTRA_LAYER = new ModelLayerLocation(
            new ResourceLocation(MCCourseMod.MOD_ID, "diamond_elytra_layer"), "main");
}