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
import net.karen.mccourse.painting.ModPaintings;
import net.karen.mccourse.particle.ModParticles;
import net.karen.mccourse.potion.BetterBrewingRecipe;
import net.karen.mccourse.potion.ModPotions;
import net.karen.mccourse.recipe.ModRecipes;
import net.karen.mccourse.screen.GemEmpoweringStationScreen;
import net.karen.mccourse.screen.KaupenFurnaceScreen;
import net.karen.mccourse.screen.ModMenuTypes;
import net.karen.mccourse.sound.ModSounds;
import net.karen.mccourse.util.ModWoodTypes;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
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

        // Register Creative Mode Tab
        ModCreativeModeTabs.register(modEventBus);

        // Register items
        ModItems.register(modEventBus);

        // Register blocks
        ModBlocks.register(modEventBus);

        // Register enchantments
        ModEnchantments.register(modEventBus);

        // Register sounds
        ModSounds.register(modEventBus);

        // Register loot tables
        ModLootModifiers.register(modEventBus);

        // Register paintings
        ModPaintings.register(modEventBus);

        // Register effects
        ModEffects.register(modEventBus);

        // Register potions
        ModPotions.register(modEventBus);

        // Register villagers
        ModVillagers.register(modEventBus);

        // Register particles
        ModParticles.register(modEventBus);

        // Register fluids
        ModFluidsTypes.register(modEventBus);
        ModFluids.register(modEventBus);

        // Register block entities
        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        // Register recipes
        ModRecipes.register(modEventBus);

        // Register entities
        ModEntities.register(modEventBus);

        // Register trunk placer types
        ModTrunkPlacerTypes.register(modEventBus);

        // Register foliage placer types
        ModFoliagePlacerTypes.register(modEventBus);

        // Register custom biomes
        ModTerraBlenderAPI.registerRegions();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            // Adding all seeds, flowers, etc. on composter block
            // Adding Kohlrabi's on composter block
            ComposterBlock.COMPOSTABLES.put(ModItems.KOHLRABI.get(), 0.35f);
            ComposterBlock.COMPOSTABLES.put(ModItems.KOHLRABI_SEEDS.get(), 0.20f);

            // Snapdragon's potted flower
            ((FlowerPotBlock) Blocks.FLOWER_POT).addPlant(ModBlocks.SNAPDRAGON.getId(), ModBlocks.POTTED_SNAPDRAGON);

            // Slimey's, Fly's, etc. custom potion recipes
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.SLIME_BALL, ModPotions.SLIMEY_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.EMERALD, ModPotions.FLY_POTION.get()));
            BrewingRecipeRegistry.addRecipe(new BetterBrewingRecipe(Potions.AWKWARD, Items.CARROT, ModPotions.HASTE_POTION.get()));

            // Added custom Surface Rules
            SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MOD_ID, ModSurfaceRules.makeRules());
        });
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            // First item
            event.accept(ModItems.ALEXANDRITE);
            // Second item
            event.accept(ModItems.RAW_ALEXANDRITE);
        }

        if(event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
            // Blocks
            // First block
            event.accept(ModBlocks.ALEXANDRITE_BLOCK);

            // Second block
            event.accept(ModBlocks.RAW_ALEXANDRITE_BLOCK);

            // Custom Advanced Block
            event.accept(ModBlocks.SOUND_BLOCK);

            // Ores
            event.accept(ModBlocks.ALEXANDRITE_ORE);
            event.accept(ModBlocks.DEEPSLATE_ALEXANDRITE_ORE);
            event.accept(ModBlocks.END_STONE_ALEXANDRITE_ORE);
            event.accept(ModBlocks.NETHER_ALEXANDRITE_ORE);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) { }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                // Register custom wood type
                Sheets.addWoodType(ModWoodTypes.WALNUT);

                ModItemProperties.addCustomItemProperties();

                // Adding Soap Water's source and flowing layers
                ItemBlockRenderTypes.setRenderLayer(ModFluids.SOURCE_SOAP_WATER.get(), RenderType.translucent());
                ItemBlockRenderTypes.setRenderLayer(ModFluids.FLOWING_SOAP_WATER.get(), RenderType.translucent());

                // Gem Empowering's menu
                MenuScreens.register(ModMenuTypes.GEM_EMPOWERING_MENU.get(), GemEmpoweringStationScreen::new);
                MenuScreens.register(ModMenuTypes.KAUPEN_FURNACE_MENU.get(), KaupenFurnaceScreen::new);

                EntityRenderers.register(ModEntities.RHINO.get(), RhinoRenderer::new); // Adding Rhino's custom entity renderer
                EntityRenderers.register(ModEntities.DICE_PROJECTILE.get(), ThrownItemRenderer::new); // Adding Dice Projectile's custom projectile entity renderer
                EntityRenderers.register(ModEntities.MAGIC_PROJECTILE.get(), MagicProjectileRenderer::new); // Adding Magic Projectile's custom projectile entity renderer
                EntityRenderers.register(ModEntities.BOUNCY_BALLS_PROJECTILE.get(), ThrownItemRenderer::new); // Adding Bouncy Balls Projectile's custom projectile entity renderer
                EntityRenderers.register(ModEntities.MOD_BOAT.get(), pContext -> new ModBoatRenderer(pContext, false)); // Adding Boat's custom projectile entity renderer
                EntityRenderers.register(ModEntities.MOD_CHEST_BOAT.get(), pContext -> new ModBoatRenderer(pContext, true)); // Adding Chest Boat's custom projectile entity renderer
            });
        }
    }
}