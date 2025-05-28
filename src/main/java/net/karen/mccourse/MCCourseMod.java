package net.karen.mccourse;

import com.mojang.logging.LogUtils;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.effect.ModEffects;
import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.block.entity.ModBlockEntities;
import net.karen.mccourse.entity.ModEntities;
import net.karen.mccourse.entity.client.MagicProjectileRenderer;
import net.karen.mccourse.entity.client.ModBoatRenderer;
import net.karen.mccourse.entity.client.RhinoRenderer;
import net.karen.mccourse.fluid.ModFluids;
import net.karen.mccourse.fluid.ModFluidsTypes;
import net.karen.mccourse.item.ModCreativeModeTabs;
import net.karen.mccourse.item.ModItemProperties;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.loot.ModLootModifiers;
import net.karen.mccourse.network.*;
import net.karen.mccourse.painting.ModPaintings;
import net.karen.mccourse.particle.ModParticles;
import net.karen.mccourse.potion.ModPotions;
import net.karen.mccourse.recipe.ModRecipes;
import net.karen.mccourse.screen.*;
import net.karen.mccourse.sound.ModSounds;
import net.karen.mccourse.util.ModWoodTypes;
import net.karen.mccourse.potion.ModPotionsRecipes;
import net.karen.mccourse.villager.ModVillagers;
import net.karen.mccourse.worldgen.biome.ModTerraBlenderAPI;
import net.karen.mccourse.worldgen.biome.surface.ModSurfaceRules;
import net.karen.mccourse.worldgen.tree.ModFoliagePlacerTypes;
import net.karen.mccourse.worldgen.tree.ModTrunkPlacerTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import terrablender.api.SurfaceRuleManager;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MCCourseMod.MOD_ID)
public class MCCourseMod {
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "mccourse";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    public MCCourseMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModCreativeModeTabs.register(modEventBus); // Register CREATIVE MODE TAB
        ModItems.register(modEventBus);  // Register ITEMS
        ModBlocks.register(modEventBus); // Register BLOCKS
        ModEnchantments.register(modEventBus); // Register ENCHANTMENTS
        ModSounds.register(modEventBus); // Register SOUNDS
        ModLootModifiers.register(modEventBus);  // Register LOOT TABLES
        ModPaintings.register(modEventBus); // Register PAINTINGS
        ModEffects.register(modEventBus); // Register EFFECTS
        ModPotions.register(modEventBus); // Register POTIONS
        ModVillagers.register(modEventBus); // Register VILLAGERS
        ModParticles.register(modEventBus); // Register PARTICLES
        ModFluidsTypes.register(modEventBus); // Register FLUIDS TYPES
        ModFluids.register(modEventBus); // Register FLUIDS
        ModBlockEntities.register(modEventBus); // Register BLOCK ENTITIES
        ModMenuTypes.register(modEventBus); // Register BLOCK ENTITY MENU TYPES
        ModRecipes.register(modEventBus); // Register RECIPES
        ModEntities.register(modEventBus); // Register MOBS ENTITIES
        ModTrunkPlacerTypes.register(modEventBus); // Register TRUNK PLACER TYPES
        ModFoliagePlacerTypes.register(modEventBus); // Register FOLIAGE PLACER TYPES
        ModTerraBlenderAPI.registerRegions(); // Register CUSTOM BIOMES
        modEventBus.addListener(this::commonSetup); // Register the COMMON SETUP method for MOD LOADING
        MinecraftForge.EVENT_BUS.register(this); // Register ourselves for SERVER and other GAME EVENTS we are interested in
        modEventBus.addListener(this::addCreative); // Register the ITEM to CREATIVE TAB
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ShapedRecipe.setCraftingSize(7, 7); // Craft Crafting Table 7x7 size

            // Adding all seeds, flowers, etc. on composter block
            // Adding Kohlrabi's on composter block
            ComposterBlock.COMPOSTABLES.put(ModItems.KOHLRABI.get(), 0.35f);
            ComposterBlock.COMPOSTABLES.put(ModItems.KOHLRABI_SEEDS.get(), 0.20f);

            // Snapdragon's potted flower
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.SNAPDRAGON.getId(), ModBlocks.POTTED_SNAPDRAGON);

            // Slimey's, Fly's, etc. custom potion recipes
            ModPotionsRecipes.addRecipe(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.SLIMEY_POTION.get());
            ModPotionsRecipes.addRecipe(Potions.AWKWARD, Items.EMERALD, ModPotions.FLY_POTION.get());
            ModPotionsRecipes.addRecipe(ModPotions.FLY_POTION.get(), Blocks.EMERALD_BLOCK, ModPotions.FLY_PLUS_POTION.get());
            ModPotionsRecipes.addRecipe(Potions.AWKWARD, Items.CARROT, ModPotions.HASTE_POTION.get());
            ModPotionsRecipes.addRecipe(Potions.AWKWARD, Items.GLOWSTONE, ModPotions.NOTHING_POTION.get());

            // Added custom Surface Rules
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());

            // All ModNetwork messages
            // Disenchanted block event network message
            ModNetworks.addNetworkMessage(DisenchantedGuiSlotMessage.class, DisenchantedGuiSlotMessage::buffer,
                    DisenchantedGuiSlotMessage::new, DisenchantedGuiSlotMessage::handler);
            // Glowing Blocks Block Shape Renderer
            ModNetworks.addNetworkMessage(GlowingBlocksNetworkMessage.SavedDataSyncMessage.class,
                    GlowingBlocksNetworkMessage.SavedDataSyncMessage::buffer, GlowingBlocksNetworkMessage.SavedDataSyncMessage::new,
                    GlowingBlocksNetworkMessage.SavedDataSyncMessage::handler);
            // Mccourse Elevator key input message
            ModNetworks.addNetworkMessage(MccourseElevatorKeyInputMessage.class, MccourseElevatorKeyInputMessage::buffer,
                    MccourseElevatorKeyInputMessage::new, MccourseElevatorKeyInputMessage::handler);
            // Hammer Preview Block
            ModNetworks.addNetworkMessage(ClientHammerBlockRenderMessage.class, ClientHammerBlockRenderMessage::buffer,
                    ClientHammerBlockRenderMessage::new, ClientHammerBlockRenderMessage::handler); // CLIENT
            ModNetworks.addNetworkMessage(ServerHammerBlockRenderMessage.class, ServerHammerBlockRenderMessage::buffer,
                    ServerHammerBlockRenderMessage::new, ServerHammerBlockRenderMessage::handler); // SERVER
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) { // Items
            event.accept(ModItems.ALEXANDRITE); // First item
            event.accept(ModItems.RAW_ALEXANDRITE); // Second item
        }
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) { // Blocks
            event.accept(ModBlocks.ALEXANDRITE_BLOCK); // First block
            event.accept(ModBlocks.RAW_ALEXANDRITE_BLOCK); // Second block
            event.accept(ModBlocks.SOUND_BLOCK); // Custom Advanced Block
            event.accept(ModBlocks.ALEXANDRITE_ORE); // Ores
            event.accept(ModBlocks.DEEPSLATE_ALEXANDRITE_ORE);
            event.accept(ModBlocks.END_STONE_ALEXANDRITE_ORE);
            event.accept(ModBlocks.NETHER_ALEXANDRITE_ORE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {}

    // You can use EventBusSubscriber to automatically register all static methods
    // in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID,
            bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                Sheets.addWoodType(ModWoodTypes.WALNUT); // Register custom wood type
                ModItemProperties.addCustomItemProperties(); // Register Item properties

                // Adding Soap Water's source and flowing layers
                ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SOAP_WATER.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SOAP_WATER.get(), RenderType.translucent());

                // Gem Empowering's menu -> Menu Types - Added all custom screens
                MenuScreens.register(ModMenuTypes.GEM_EMPOWERING_MENU.get(), GemEmpoweringStationScreen::new);
                MenuScreens.register(ModMenuTypes.KAUPEN_FURNACE_MENU.get(), KaupenFurnaceScreen::new);
                MenuScreens.register(ModMenuTypes.DISENCHANTED_MENU.get(), DisenchantedScreen::new);
                MenuScreens.register(ModMenuTypes.CRAFT_CRAFTING_TABLE_MENU.get(), CraftCraftingTableScreen::new);

                // Added all custom entity renderers
                // Adding Rhino's custom entity renderer
                EntityRenderers.register(ModEntities.RHINO.get(), RhinoRenderer::new);
                // Adding Dice Projectile's custom projectile entity renderer
                EntityRenderers.register(ModEntities.DICE_PROJECTILE.get(), ThrownItemRenderer::new);
                // Adding Magic Projectile's custom projectile entity renderer
                EntityRenderers.register(ModEntities.MAGIC_PROJECTILE.get(), MagicProjectileRenderer::new);
                // Adding Bouncy Balls Projectile's custom projectile entity renderer
                EntityRenderers.register(ModEntities.BOUNCY_BALLS_PROJECTILE.get(), ThrownItemRenderer::new);
                // Adding Boat's custom projectile entity renderer
                EntityRenderers.register(ModEntities.MOD_BOAT.get(), context -> new ModBoatRenderer(context, false));
                // Adding Chest Boat's custom projectile entity renderer
                EntityRenderers.register(ModEntities.MOD_CHEST_BOAT.get(), context -> new ModBoatRenderer(context, true));
            });
        }
    }
}