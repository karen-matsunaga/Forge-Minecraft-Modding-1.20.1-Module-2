package net.karen.mccourse.item.custom;

import com.google.common.collect.ImmutableMap;
import net.karen.mccourse.effect.ModEffects;
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
                    .put(ModArmorMaterials.ALEXANDRITE, Arrays.asList(
                            // Added all custom effects or vanilla effects on player only all armor slots
                            new MobEffectInstance(MobEffects.DAMAGE_BOOST, -1, 1, false, false, true),
                            new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 1, false, false, true),
                            new MobEffectInstance(ModEffects.FLY_EFFECT.get(), -1, 1, false, false, true),
                            new MobEffectInstance(MobEffects.REGENERATION, -1, 4, false, false, true),
                            new MobEffectInstance(MobEffects.FIRE_RESISTANCE, -1, 1, false, false, true),
                            new MobEffectInstance(MobEffects.DIG_SPEED, -1, 1, false, false, true))).build();

    public ModArmorItem(ArmorMaterial material, Type type, Properties properties) { super(material, type, properties); }

    // Apply effect if player using all parts of armor
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide() && hasFullSuitOfArmorOn(player)) { evaluateArmorEffects(player); } // Add effect
        else { removeArmorEffects(player); } // Remove effect
    }

    // If player is using same armor material applies all effects
    private void evaluateArmorEffects(Player player) {
        for (Map.Entry<ArmorMaterial, List<MobEffectInstance>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            List<MobEffectInstance> effects = entry.getValue();
            if (hasPlayerCorrectArmorOn(mapArmorMaterial, player)) { for (MobEffectInstance effect : effects) { addEffectToPlayer(player, effect); } }
        }
    }

    // If player not using full armor effect are removed
    private void removeArmorEffects(Player player) {
        for (Map.Entry<ArmorMaterial, List<MobEffectInstance>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            List<MobEffectInstance> effects = entry.getValue();
            if (!hasPlayerCorrectArmorOn(mapArmorMaterial, player)) { for (MobEffectInstance effect : effects) { removeEffectToPlayer(player, effect); } }
        }
    }

    // If player not to receive the effects it is adding
    private void addEffectToPlayer(Player player, MobEffectInstance effect) {
        // Player has effects and used all armor slots (Helmet, Chestplate, Leggings and Boots)
        boolean hasPlayerEffect = player.hasEffect(effect.getEffect());
        if (!hasPlayerEffect) {
            player.addEffect(new MobEffectInstance(effect.getEffect(), effect.getDuration(), effect.getAmplifier()));
        } // Player received effects
    }

    // If player received effects not using armor
    private void removeEffectToPlayer(Player player, MobEffectInstance effect) {
        boolean hasPlayerEffect = player.hasEffect(effect.getEffect());
        // Player has effects and not used all armor slots (Helmet, Chestplate, Leggings and Boots)
        if (hasPlayerEffect) { player.removeEffect(effect.getEffect()); } // Player remove effects
    }

    // If player using same armor material (Helmet, Chestplate, Leggings and Boots)
    private boolean hasPlayerCorrectArmorOn(ArmorMaterial mapArmorMaterial, Player player) {
        for (ItemStack armorStack : player.getArmorSlots()) { if (!(armorStack.getItem() instanceof ArmorItem)) { return false; } }
        ArmorItem boots = ((ArmorItem) player.getInventory().getArmor(0).getItem()); // Boots
        ArmorItem leggings = ((ArmorItem) player.getInventory().getArmor(1).getItem()); // Leggings
        ArmorItem chestplate = ((ArmorItem) player.getInventory().getArmor(2).getItem()); // Chestplate
        ArmorItem helmet = ((ArmorItem) player.getInventory().getArmor(3).getItem()); // Helmet
        return boots.getMaterial() == mapArmorMaterial && leggings.getMaterial() == mapArmorMaterial
                && chestplate.getMaterial() == mapArmorMaterial && helmet.getMaterial() == mapArmorMaterial;
    }

    // If player using all parts (Helmet, Chestplate, Leggings and Boots)
    private boolean hasFullSuitOfArmorOn(Player player) {
        ItemStack boots = player.getInventory().getArmor(0); // Boots
        ItemStack leggings = player.getInventory().getArmor(1); // Leggings
        ItemStack chestplate = player.getInventory().getArmor(2); // Chestplate
        ItemStack helmet = player.getInventory().getArmor(3); // Helmet
        return !boots.isEmpty() && !leggings.isEmpty() && !chestplate.isEmpty() && !helmet.isEmpty();
    }
}