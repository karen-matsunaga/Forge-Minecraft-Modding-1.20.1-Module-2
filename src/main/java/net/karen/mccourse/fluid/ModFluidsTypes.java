package net.karen.mccourse.fluid;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;

public class ModFluidsTypes {
    public static final ResourceLocation WATER_STILL_RL = new ResourceLocation("block/water_still");
    public static final ResourceLocation WATER_FLOWING_RL = new ResourceLocation("block/water_flow");
    public static final ResourceLocation WATER_OVERLAY_RL = new ResourceLocation("block/water_overlay");

    // Registry all fluid types
    public static final DeferredRegister<FluidType> FLUID_TYPES =
            DeferredRegister.create(ForgeRegistries.Keys.FLUID_TYPES, MCCourseMod.MOD_ID);

    // Registry all custom fluid colors types
    public static final RegistryObject<FluidType> SOAP_WATER_FLUID_TYPE = registerFluidType("soap_water_fluid",
            new BaseFluidType(WATER_STILL_RL, WATER_FLOWING_RL, WATER_OVERLAY_RL, 0xA1E038D0,
                    new Vector3f(224f / 255f, 56f / 255f, 208f / 255f),
                    FluidType.Properties.create().lightLevel(2).viscosity(5).density(15)));


    // Custom method to register custom fluid type
    private static RegistryObject<FluidType> registerFluidType(String name, FluidType fluidType) {
        return FLUID_TYPES.register(name, () -> fluidType);
    }

    // Registry all custom fluids types on Forge
    public static void register(IEventBus eventBus) {
        FLUID_TYPES.register(eventBus);
    }
}
