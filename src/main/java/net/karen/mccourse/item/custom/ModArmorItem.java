package net.karen.mccourse.item.custom;

import com.google.common.collect.ImmutableMap;
import net.karen.mccourse.item.ModArmorMaterials;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ModArmorItem extends ArmorItem {
    // Specific armor material to mob effect instance that applied in player
    private static final Map<ArmorMaterial, List<MobEffectInstance>> MATERIAL_TO_EFFECT_MAP =
    (new ImmutableMap.Builder<ArmorMaterial, List<MobEffectInstance>>())
        .put(ModArmorMaterials.ALEXANDRITE, Arrays.asList( // ALEXANDRITE CUSTOM ARMOR
                // Added all custom effects or vanilla effects on player only all armor slots
                new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 1, false, false),
                new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 1, false, false),
                new MobEffectInstance(MobEffects.REGENERATION, 300, 4, false, false),
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 1, false, false),
                new MobEffectInstance(MobEffects.DIG_SPEED, 300, 1, false, false)))
        .put(ModArmorMaterials.PINK, Arrays.asList( // PINK CUSTOM ARMOR
                new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 1, false, false),
                new MobEffectInstance(MobEffects.JUMP, 300, 1, false, false),
                new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 300, 1, false, false),
                new MobEffectInstance(MobEffects.DIG_SPEED, 300, 1, false, false),
                new MobEffectInstance(MobEffects.DAMAGE_BOOST, 300, 1, false, false),
                new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 300, 1, false, false)))
        .build();

    public ModArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) { // Apply effect if player using all parts of armor
        if (!level.isClientSide() && hasFullSuitOfArmorOn(player)) { evaluateArmorEffects(player); }
    }

    private void evaluateArmorEffects(Player player) {
        // Player is using same armor material applies all effects
        MATERIAL_TO_EFFECT_MAP.forEach((key, value) -> {
            if (hasPlayerCorrectArmorOn(key, player)) { addEffectToPlayer(player, value); }
        });
    }

    private void addEffectToPlayer(Player player, List<MobEffectInstance> effects) {
        effects.forEach(effect -> {
            if (player.getEffect(effect.getEffect()) == null) { // Player not to receive the effects it is adding
                player.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier()));
            }
        });
    }

    // Player is using same armor material [Boots, Leggings, Chestplate and Helmet]
    private boolean hasPlayerCorrectArmorOn(ArmorMaterial mapArmorMaterial, Player player) {
        for (ItemStack armorStack : player.getArmorSlots()) { if (!(armorStack.getItem() instanceof ArmorItem)) { return false; } }
        return ((ArmorItem) player.getInventory().getArmor(0).getItem()).getMaterial() == mapArmorMaterial &&  // Boots
        ((ArmorItem) player.getInventory().getArmor(1).getItem()).getMaterial() == mapArmorMaterial && // Leggings
        ((ArmorItem) player.getInventory().getArmor(2).getItem()).getMaterial() == mapArmorMaterial && // Chestplate
        ((ArmorItem) player.getInventory().getArmor(3).getItem()).getMaterial() == mapArmorMaterial; // Helmet
    }

    private boolean hasFullSuitOfArmorOn(Player player) {
        // Player is using all parts [Boots 0, Leggings 1, Chestplate 2 and Helmet 3]
        return !player.getInventory().getArmor(0).isEmpty() && !player.getInventory().getArmor(1).isEmpty()
        && !player.getInventory().getArmor(2).isEmpty() && !player.getInventory().getArmor(3).isEmpty();
    }
}