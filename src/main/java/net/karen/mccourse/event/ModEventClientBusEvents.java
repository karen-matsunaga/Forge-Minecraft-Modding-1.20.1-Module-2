package net.karen.mccourse.event;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.entity.ModBlockEntities;
import net.karen.mccourse.block.entity.renderer.GemEmpoweringBlockEntityRenderer;
import net.karen.mccourse.particle.AlexandriteParticles;
import net.karen.mccourse.particle.BouncyBallsParticles;
import net.karen.mccourse.particle.ModParticles;
import net.karen.mccourse.util.KeyBinding;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.blockentity.HangingSignRenderer;
import net.minecraft.client.renderer.blockentity.SignRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MCCourseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) { // Register custom particles event
        event.registerSpriteSet(ModParticles.ALEXANDRITE_PARTICLES.get(), AlexandriteParticles.Provider::new);
        event.registerSpriteSet(ModParticles.BOUNCY_BALLS_PARTICLES.get(), BouncyBallsParticles.Provider::new); // Bouncy balls
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) { // Register custom BER
        // Register custom block entity renderer
        event.registerBlockEntityRenderer(ModBlockEntities.GEM_EMPOWERING_STATION_BE.get(),
                GemEmpoweringBlockEntityRenderer::new);

        // Register custom sign
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_SIGN.get(), SignRenderer::new);
        event.registerBlockEntityRenderer(ModBlockEntities.MOD_HANGING_SIGN.get(), HangingSignRenderer::new);
    }

    // Register custom colored blocks
    @SubscribeEvent
    public static void registerColoredBlocks(RegisterColorHandlersEvent.Block event) {
        event.register((pState, pLevel, pPos, pTintIndex) -> pLevel != null && pPos != null
                ? BiomeColors.getAverageFoliageColor(pLevel, pPos) : FoliageColor.getDefaultColor(), ModBlocks.COLORED_LEAVES.get());
    }

    @SubscribeEvent
    public static void registerColoredItems(RegisterColorHandlersEvent.Item event) {
        event.register((pStack, pTintIndex) -> {
            BlockState state = ((BlockItem)pStack.getItem()).getBlock().defaultBlockState();
            return event.getBlockColors().getColor(state, null, null, pTintIndex);
        }, ModBlocks.COLORED_LEAVES.get());
    }

    @SubscribeEvent
    public static void registerKeyInput(RegisterKeyMappingsEvent event) {
        // Register custom Key Input
        event.register(KeyBinding.GLOWING_BLOCKS_KEY); // Glowing Blocks
        event.register(KeyBinding.GLOWING_MOBS_KEY); // Glowing Mobs
        event.register(KeyBinding.MCCOURSE_BOTTLE_STORED_TEN_LEVELS_KEY); // Mccourse Bottle stored 10 levels
        event.register(KeyBinding.MCCOURSE_BOTTLE_RESTORED_TEN_LEVELS_KEY); // Mccourse Bottle restored 10 levels
        event.register(KeyBinding.MCCOURSE_BOTTLE_STORED_HUNDRED_LEVELS_KEY); // Mccourse Bottle stored 100 levels
        event.register(KeyBinding.MCCOURSE_BOTTLE_RESTORED_HUNDRED_LEVELS_KEY); // Mccourse Bottle restored 100 levels
    }
}