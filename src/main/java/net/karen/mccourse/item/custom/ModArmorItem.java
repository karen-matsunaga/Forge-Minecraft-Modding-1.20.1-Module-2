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
                            // Fly effect
                            new MobEffectInstance(ModEffects.FLY_EFFECT.get(), 200, 2, false, false, true),
                            // Strength effect
                            new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2, false, false, true),
                            // Night vision effect
                            new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 2, false, false, true),
                            // Haste effect
                            new MobEffectInstance(MobEffects.DIG_SPEED, 200, 2, false, false, true)
                    ))
                    .build();

    public ModArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    // Apply effect if player using all parts of armor
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide() && hasFullSuitOfArmorOn(player)) {
            evaluateArmorEffects(player);
        }
    }

    /// If player is using same armor material applies all effects
    private void evaluateArmorEffects(Player player) {
        for (Map.Entry<ArmorMaterial, List<MobEffectInstance>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            List<MobEffectInstance> effects = entry.getValue();

            if (hasPlayerCorrectArmorOn(mapArmorMaterial, player)) {
                for (MobEffectInstance effect : effects) {
                    addEffectToPlayer(player, effect);
                }
            }
        }
    }

    // If player not to receive the effects it is adding
    private void addEffectToPlayer(Player player, MobEffectInstance effect) {
        boolean hasPlayerEffect = player.hasEffect(effect.getEffect());

        if (!hasPlayerEffect) {
            player.addEffect(new MobEffectInstance(effect.getEffect(),
                    effect.getDuration(), effect.getAmplifier()));
        }
    }

    // If player using same armor material
    private boolean hasPlayerCorrectArmorOn(ArmorMaterial mapArmorMaterial, Player player) {
        for (ItemStack armorStack : player.getArmorSlots()) {
            if (!(armorStack.getItem() instanceof ArmorItem)) {
                return false;
            }
        }

        ArmorItem boots = ((ArmorItem) player.getInventory().getArmor(0).getItem());
        ArmorItem leggings = ((ArmorItem) player.getInventory().getArmor(1).getItem());
        ArmorItem chestplate = ((ArmorItem) player.getInventory().getArmor(2).getItem());
        ArmorItem helmet = ((ArmorItem) player.getInventory().getArmor(3).getItem());

        return boots.getMaterial() == mapArmorMaterial && leggings.getMaterial() == mapArmorMaterial
                && chestplate.getMaterial() == mapArmorMaterial && helmet.getMaterial() == mapArmorMaterial;
    }

    // If player using all parts (Helmet, Chestplate, Leggings and Boots)
    private boolean hasFullSuitOfArmorOn(Player player) {
        ItemStack boots = player.getInventory().getArmor(0);
        ItemStack leggings = player.getInventory().getArmor(1);
        ItemStack chestplate = player.getInventory().getArmor(2);
        ItemStack helmet = player.getInventory().getArmor(3);

        return !boots.isEmpty() && !leggings.isEmpty() && !chestplate.isEmpty() && !helmet.isEmpty();
    }
}