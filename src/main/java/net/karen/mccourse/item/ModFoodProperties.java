package net.karen.mccourse.item;

import net.minecraft.world.effect.*;
import net.minecraft.world.food.FoodProperties;
import static net.karen.mccourse.util.Utils.effect;

public class ModFoodProperties {
    // Registry all custom foods
    public static final FoodProperties KOHLRABI = registerFoodEffect(3, 0.25F, MobEffects.MOVEMENT_SPEED,
                                                                     200, 0, 0.1F);

    // CUSTOM METHOD - Registry all custom foods WITH effect
    private static FoodProperties registerFoodEffect(int nutrition, float saturation,
                                                     MobEffect mobEffect, int duration, int amplifier, float probability) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation)
                   .effect(() -> effect(mobEffect, duration, amplifier), probability).build();
    }

    // CUSTOM METHOD - Registry all custom foods WITHOUT effect
    private static FoodProperties registerFood(int nutrition, float saturation) {
        return new FoodProperties.Builder().nutrition(nutrition).saturationMod(saturation).build();
    }
}