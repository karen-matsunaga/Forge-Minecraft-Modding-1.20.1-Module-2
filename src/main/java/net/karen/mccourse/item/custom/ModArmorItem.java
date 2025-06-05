package net.karen.mccourse.item.custom;

import com.google.common.collect.ImmutableMap;
import net.karen.mccourse.item.ModArmorMaterials;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import java.util.*;

public class ModArmorItem extends ArmorItem {
    // Specific armor material to mob effect instance that applied in player
    private static final Map<ArmorMaterial, List<MobEffectInstance>> MATERIAL_TO_EFFECT_MAP =
    (new ImmutableMap.Builder<ArmorMaterial, List<MobEffectInstance>>())
        // ALEXANDRITE CUSTOM ARMOR - Added all custom effects or vanilla effects on player only all armor slots
        .put(ModArmorMaterials.ALEXANDRITE, Arrays.asList(effect(MobEffects.DAMAGE_BOOST, 200, 1),
             effect(MobEffects.NIGHT_VISION, 200, 1), effect(MobEffects.REGENERATION, 200, 4),
             effect(MobEffects.FIRE_RESISTANCE, 200, 1), effect(MobEffects.DIG_SPEED, 200, 1)))
        // PINK CUSTOM ARMOR
        .put(ModArmorMaterials.PINK, Arrays.asList(effect(MobEffects.NIGHT_VISION, 200, 1),
             effect(MobEffects.JUMP, 200, 1), effect(MobEffects.FIRE_RESISTANCE, 200, 1),
             effect(MobEffects.DAMAGE_BOOST, 200, 1), effect(MobEffects.DAMAGE_RESISTANCE, 200, 1)))
        .build();

    public ModArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) { // Apply effect if player using all parts of armor
        if (!level.isClientSide() && hasFullSuitOfArmorOn(player)) { evaluateArmorEffects(player); }
    }

    private void evaluateArmorEffects(Player player) {
        MATERIAL_TO_EFFECT_MAP.forEach((key, value) -> {
            if (hasPlayerCorrectArmorOn(key, player)) { addEffectToPlayer(player, value); }
        });
    } // Player is using same armor material applies all effects

    private void addEffectToPlayer(Player player, List<MobEffectInstance> effects) {
        effects.forEach(effect -> { // Player not to receive the effects it is adding
            if (player.getEffect(effect.getEffect()) == null) {
                player.addEffect(effect(effect.getEffect(), effect.getDuration(), effect.getAmplifier()));
            }
        });
    }

    // Player is using same armor material [Boots, Leggings, Chestplate and Helmet]
    private boolean hasPlayerCorrectArmorOn(ArmorMaterial armorMaterial, Player player) {
        for (ItemStack armorStack : player.getArmorSlots()) { if (!(armorStack.getItem() instanceof ArmorItem)) { return false; } }
        return getArmor(player, 0, armorMaterial) && getArmor(player, 1, armorMaterial) &&
               getArmor(player, 2, armorMaterial) && getArmor(player, 3, armorMaterial);
    }

    private boolean hasFullSuitOfArmorOn(Player player) {
        return hasArmor(player, 0) && hasArmor(player, 1) && hasArmor(player, 2) && hasArmor(player, 3);
    } // Player is using all parts [Boots 0, Leggings 1, Chestplate 2 and Helmet 3]

    private static MobEffectInstance effect(MobEffect effect, int duration, int amplifier) {
        return new MobEffectInstance(effect, duration, amplifier, false, false);
    }

    private boolean hasArmor(Player player, int slot) { return !player.getInventory().getArmor(slot).isEmpty(); }

    private boolean getArmor(Player player, int slot, ArmorMaterial armorMaterial) {
        return ((ArmorItem) player.getInventory().getArmor(slot).getItem()).getMaterial() == armorMaterial;
    }
}