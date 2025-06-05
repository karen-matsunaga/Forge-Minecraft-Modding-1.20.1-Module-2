package net.karen.mccourse.item.custom;

import com.google.common.collect.ImmutableMap;
import net.karen.mccourse.item.ModArmorMaterials;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.util.List;
import java.util.Map;

public class ModHelmetItem extends ArmorItem {
    // Specific armor material to mob effect instance that applied in player
    private static final Map<ArmorMaterial, List<MobEffectInstance>> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, List<MobEffectInstance>>())
                    // MINER CUSTOM ARMOR - Added all custom effects or vanilla effects on player used only helmet
                    .put(ModArmorMaterials.MINER, List.of(effect(MobEffects.NIGHT_VISION, 200, 1))).build();

    public ModHelmetItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) { // Apply effect if player using all parts of armor
        boolean hasHelmet =  !player.getInventory().getArmor(3).isEmpty(); // Player is using Helmet
        if (!level.isClientSide() && hasHelmet) { evaluateArmorEffects(player); }
    }

    private void evaluateArmorEffects(Player player) {
        // Player is using same armor material applies all effects
        MATERIAL_TO_EFFECT_MAP.forEach((key, value) -> { if (isWearingHelmet(key, player)) { addEffectToPlayer(player, value); }});
    }

    private void addEffectToPlayer(Player player, List<MobEffectInstance> effects) {
        effects.forEach(effect -> {
            if (player.getEffect(effect.getEffect()) == null) { // Player not to receive the effects it is adding
                player.addEffect(effect(effect.getEffect(), effect.getDuration(), effect.getAmplifier()));
            }
        });
    }

    // Player is using same armor material [Helmet]
    private boolean isWearingHelmet(ArmorMaterial mapArmorMaterial, Player player) {
        return ((ArmorItem) player.getInventory().getArmor(3).getItem()).getMaterial() == mapArmorMaterial; // Helmet
    }

    private static MobEffectInstance effect(MobEffect effect, int duration, int amplifier) {
        return new MobEffectInstance(effect, duration, amplifier, false, false);
    }
}