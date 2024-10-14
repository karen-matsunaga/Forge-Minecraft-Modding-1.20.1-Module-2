package net.karen.mccourse.effect;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, MCCourseMod.MOD_ID);

    // Registry all effects
    // Slimey's effect
    public static final RegistryObject<MobEffect> SLIMEY_EFFECT = MOB_EFFECTS.register("slimey",
            () -> new SlimeyEffect(MobEffectCategory.NEUTRAL, 0x36ebab).addAttributeModifier(Attributes.MOVEMENT_SPEED,
                    "7107DE5E-7CE8-4030-940E-514C1F160890", -0.25f, AttributeModifier.Operation.MULTIPLY_TOTAL));

    // Fly's effect
    public static final RegistryObject<MobEffect> FLY_EFFECT = MOB_EFFECTS.register("fly",
            () -> new FlyEffect(MobEffectCategory.BENEFICIAL, 0xFFFF00)
                    // Fly's effect
                    .addAttributeModifier(Attributes.FLYING_SPEED,
                            "f81d4fae-7dec-11d0-a765-00a0c91e6bf7", 1.00f, AttributeModifier.Operation.MULTIPLY_TOTAL)
                    // Speed's effect
                    .addAttributeModifier(Attributes.MOVEMENT_SPEED, "f81d4fae-7dec-11d0-a765-00a0c91e6bf8",
                            1.00f, AttributeModifier.Operation.MULTIPLY_TOTAL));

    // Registry all effects on Forge
    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }


}
