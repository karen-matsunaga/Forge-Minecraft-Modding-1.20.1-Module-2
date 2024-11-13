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

public class ModHelmetArmorItem extends ArmorItem {
    // Specific armor material to mob effect instance that applied in player
    private static final Map<ArmorMaterial, List<MobEffectInstance>> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, List<MobEffectInstance>>())
                    .put(ModArmorMaterials.PINK, Arrays.asList(
                            // Glowing effect
                            new MobEffectInstance(MobEffects.GLOWING, 200, 2, false, false, true),
                            // Strength effect
                            new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 2, false, false, true),
                            // Night vision effect
                            new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 2, false, false, true)
                    ))
                    .build();

    public ModHelmetArmorItem(ArmorMaterial material, Type type, Properties properties) { super(material, type, properties); }

    // Apply effect if player using all parts of armor
    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        if (!level.isClientSide() && hasHelmetOn(player)) { evaluateArmorEffects(player); }
    }

    /// If player is using same armor material applies all effects
    private void evaluateArmorEffects(Player player) {
        for (Map.Entry<ArmorMaterial, List<MobEffectInstance>> entry : MATERIAL_TO_EFFECT_MAP.entrySet()) {
            ArmorMaterial mapArmorMaterial = entry.getKey();
            List<MobEffectInstance> effects = entry.getValue();

            if (hasPlayerCorrectArmorOn(mapArmorMaterial, player)) { for (MobEffectInstance effect : effects) { addEffectToPlayer(player, effect); } }
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
        if (!(player.getInventory().getArmor(3).getItem() instanceof ArmorItem helmet)) { return false; }
        return helmet.getMaterial() == mapArmorMaterial;
    }

    // If player using Helmet part
    private boolean hasHelmetOn(Player player) { ItemStack helmet = player.getInventory().getArmor(3); return !helmet.isEmpty(); }
}