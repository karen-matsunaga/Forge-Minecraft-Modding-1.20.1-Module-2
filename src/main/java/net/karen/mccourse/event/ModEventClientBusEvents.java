package net.karen.mccourse.event;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.entity.ModBlockEntities;
import net.karen.mccourse.block.entity.renderer.GemEmpoweringBlockEntityRenderer;
import net.karen.mccourse.util.ImageTooltipComponent;
import net.karen.mccourse.particle.*;
import net.karen.mccourse.util.KeyBinding;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.blockentity.*;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import java.util.function.Function;

@Mod.EventBusSubscriber(modid = MCCourseMod.MOD_ID,
                        bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
    // CUSTOM EVENT - Register custom particles event
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.ALEXANDRITE_PARTICLES.get(), AlexandriteParticles.Provider::new); // Alexandrite
        event.registerSpriteSet(ModParticles.BOUNCY_BALLS_PARTICLES.get(), BouncyBallsParticles.Provider::new); // Bouncy balls
    }

    // CUSTOM EVENT - Register custom block entity renderer
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        // Register custom BER (Block Entity Renderer)
        event.registerBlockEntityRenderer(ModBlockEntities.GEM_EMPOWERING_STATION_BE.get(),
                                          GemEmpoweringBlockEntityRenderer::new); // Gem Empowering Station block entity
        // Register custom sign
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_SIGN.get(), SignRenderer::new); // Sign
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_HANGING_SIGN.get(), HangingSignRenderer::new); // Hanging Sing
    }

    // CUSTOM EVENT - Register custom colored blocks
    @SubscribeEvent
    public static void registerColoredBlocks(RegisterColorHandlersEvent.Block event) {
        event.register((state, level, pos, tintIndex) -> level != null && pos != null
                        ? BiomeColors.getAverageFoliageColor(level, pos)
                        : FoliageColor.getDefaultColor(), ModBlocks.COLORED_LEAVES.get());
    }

    // CUSTOM EVENT - Register custom colored blocks
    @SubscribeEvent
    public static void registerColoredItems(RegisterColorHandlersEvent.Item event) {
        event.register((stack, tintIndex) -> {
            BlockState state = ((BlockItem)stack.getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(state, null, null, tintIndex); },
            ModBlocks.COLORED_LEAVES.get());
    }

    // CUSTOM EVENT - Register custom Key Input
    @SubscribeEvent
    public static void registerKeyInput(RegisterKeyMappingsEvent event) {
        event.register(KeyBinding.GLOWING_BLOCKS_KEY); // Glowing Blocks custom enchantment
        event.register(KeyBinding.GLOWING_MOBS_KEY); // Glowing Mobs custom enchantment
        event.register(KeyBinding.MCCOURSE_BOTTLE_STORED_TEN_LEVELS_KEY); // Mccourse Bottle stored 10 levels
        event.register(KeyBinding.MCCOURSE_BOTTLE_RESTORED_TEN_LEVELS_KEY); // Mccourse Bottle restored 10 levels
        event.register(KeyBinding.MCCOURSE_BOTTLE_STORED_HUNDRED_LEVELS_KEY); // Mccourse Bottle stored 100 levels
        event.register(KeyBinding.MCCOURSE_BOTTLE_RESTORED_HUNDRED_LEVELS_KEY); // Mccourse Bottle restored 100 levels
        event.register(KeyBinding.UNLOCK_KEY); // Unlock custom enchantment
    }

    // CUSTOM EVENT - Register custom image tooltip
    @SubscribeEvent
    public static void registerTooltip(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(ImageTooltipComponent.class, Function.identity()); // Unlock custom enchantment
    }
}