package net.karen.mccourse.potion;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS =
            DeferredRegister.create(ForgeRegistries.POTIONS, MCCourseMod.MOD_ID);

    // Registry all potions

    // Slimey's potion register
    public static final RegistryObject<Potion> SLIMEY_POTION = POTIONS.register("slimey_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.SLIMEY_EFFECT.get(), 200, 0)));

    // Fly's potion register
    public static final RegistryObject<Potion> FLY_POTION = POTIONS.register("fly_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.FLY_EFFECT.get(), 200, 0)));

    // Registry all potions on Forge
    public static void register(IEventBus eventBus) {
        POTIONS.register(eventBus);
    }

}
