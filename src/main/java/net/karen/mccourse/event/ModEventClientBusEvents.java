package net.karen.mccourse.event;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.entity.ModBlockEntities;
import net.karen.mccourse.block.entity.renderer.GemEmpoweringBlockEntityRenderer;
import net.karen.mccourse.particle.AlexandriteParticles;
import net.karen.mccourse.particle.ModParticles;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = MCCourseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventClientBusEvents {

    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticles.ALEXANDRITE_PARTICLES.get(), AlexandriteParticles.Provider::new);
    }

    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.GEM_EMPOWERING_STATION_BE.get(),
                GemEmpoweringBlockEntityRenderer::new);
    }

}