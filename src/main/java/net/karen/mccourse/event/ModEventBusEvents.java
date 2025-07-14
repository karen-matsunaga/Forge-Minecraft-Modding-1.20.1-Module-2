package net.karen.mccourse.event;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.entity.ModEntities;
import net.karen.mccourse.entity.client.MagicProjectileModel;
import net.karen.mccourse.entity.client.RhinoModel;
import net.karen.mccourse.entity.client.TomahawkProjectileModel;
import net.karen.mccourse.entity.custom.RhinoEntity;
import net.karen.mccourse.entity.layers.ElytraOverpowerLayer;
import net.karen.mccourse.entity.layers.ModModelLayers;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.ElytraModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MCCourseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    // CUSTOM EVENT - Registry all custom entities layers
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.RHINO_LAYER, RhinoModel::createBodyLayer); // Rhino's layer
        event.registerLayerDefinition(ModModelLayers.MAGIC_PROJECTILE_LAYER,
                                      MagicProjectileModel::createBodyLayer); // Magic Projectile's layer
        event.registerLayerDefinition(ModModelLayers.WALNUT_BOAT_LAYER, BoatModel::createBodyModel); // Boat's layer
        event.registerLayerDefinition(ModModelLayers.WALNUT_CHEST_BOAT_LAYER,
                                      ChestBoatModel::createBodyModel); // Chest Boat's layer
        event.registerLayerDefinition(ModModelLayers.DIAMOND_ELYTRA_LAYER, ElytraModel::createLayer); // Diamond Elytra layer
        event.registerLayerDefinition(ModModelLayers.TOMAHAWK_LAYER, TomahawkProjectileModel::createBodyLayer); // Tomahawk layer
    }

    // CUSTOM EVENT - Registry all custom entities layers
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.AddLayers event) {
       EntityModelSet entityModel = event.getEntityModels(); // Entity Model
       event.getSkins().forEach(skin -> { // Access each Elytra Model
           LivingEntityRenderer<? extends Player, ? extends EntityModel<? extends Player>> entityRenderer = event.getSkin(skin);
           if (entityRenderer instanceof PlayerRenderer player) { // Added custom Elytra LAYER on Player Renderer
               player.addLayer(new ElytraOverpowerLayer<>(player, entityModel)); // Added all custom Elytra LAYER
           }
       });
    }

    // CUSTOM EVENT - Registry all custom entities attributes
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.RHINO.get(), RhinoEntity.createAttributes().build());
    }

    // CUSTOM EVENT - Register all custom entities spawn placement
    @SubscribeEvent
    public static void registerSpawnPlacement(SpawnPlacementRegisterEvent event) {
        event.register(ModEntities.RHINO.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                       Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}