package net.karen.mccourse.item.custom;

import com.google.common.collect.ImmutableMap;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.item.ModArmorMaterials;
import net.karen.mccourse.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import java.util.List;
import java.util.Map;
import static net.karen.mccourse.util.Utils.has;
import static net.karen.mccourse.util.Utils.helmet;

public class ModHelmetItem extends ArmorItem {
    // Specific armor material to mob effect instance that applied in player
    private static final Map<ArmorMaterial, List<MobEffectInstance>> MATERIAL_TO_EFFECT_MAP =
            (new ImmutableMap.Builder<ArmorMaterial, List<MobEffectInstance>>())
                 // MINER CUSTOM ARMOR - Added all custom effects or vanilla effects on player used only helmet
                 .put(ModArmorMaterials.MINER, List.of(helmet(MobEffects.DIG_SPEED, 200, 1))).build();

    public ModHelmetItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) { // Apply effect if player using all parts of armor
        if (!level.isClientSide() && !player.getInventory().getArmor(3).isEmpty()) { // Player is using Helmet
            evaluateArmorEffects(player);
            if (has(player, EquipmentSlot.HEAD).is(ModItems.MINER_HELMET.get())) { // Miner Block
                BlockPos pos = player.blockPosition();
                boolean blockAir = level.getBlockState(pos).isAir();
                if (blockAir) { level.setBlock(pos, ModBlocks.MINER_BLOCK.get().defaultBlockState(), 3); }
            }
        }
    }

    // CUSTOM METHOD - Player is using same armor material applies all effects
    private void evaluateArmorEffects(Player player) {
        MATERIAL_TO_EFFECT_MAP.forEach((key, value) -> { if (isWearingHelmet(key, player)) { addEffectToPlayer(player, value); }});
    }

    // CUSTOM METHOD - Added effect on Player
    private void addEffectToPlayer(Player player, List<MobEffectInstance> effects) {
        effects.forEach(effect -> {
             if (player.getEffect(effect.getEffect()) == null) { // Player not to receive the effects it is adding
                 player.addEffect(helmet(effect.getEffect(), effect.getDuration(), effect.getAmplifier()));
            }
        });
    }

    // CUSTOM METHOD - Player is using same armor material [Helmet]
    private boolean isWearingHelmet(ArmorMaterial mapArmorMaterial, Player player) {
        return ((ArmorItem) player.getInventory().getArmor(3).getItem()).getMaterial() == mapArmorMaterial; // Helmet
    }
}