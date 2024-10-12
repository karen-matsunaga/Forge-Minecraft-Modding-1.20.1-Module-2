package net.karen.mccourse.painting;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPaintings {
    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, MCCourseMod.MOD_ID);

    // Adding all paintings - One block = 16; Two blocks = 32
    public static final RegistryObject<PaintingVariant> SAW_THEM = PAINTING_VARIANTS.register("saw_them",
            () -> new PaintingVariant(16, 16));

    public static final RegistryObject<PaintingVariant> SHRIMP = PAINTING_VARIANTS.register("shrimp",
            () -> new PaintingVariant(32, 16));

    public static final RegistryObject<PaintingVariant> WORLD = PAINTING_VARIANTS.register("world",
            () -> new PaintingVariant(32, 32));

    // Registry all on Forge
    public static void register(IEventBus eventBus) {
        PAINTING_VARIANTS.register(eventBus);
    }
}
