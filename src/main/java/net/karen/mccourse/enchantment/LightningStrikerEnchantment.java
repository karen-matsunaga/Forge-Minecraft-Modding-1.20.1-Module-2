package net.karen.mccourse.enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LightningStrikerEnchantment extends Enchantment {
    protected LightningStrikerEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) {
        super(pRarity, pCategory, pApplicableSlots);
    }

    // Lightning Striker function
    @Override
    public void doPostAttack(LivingEntity pAttacker, Entity pTarget, int pLevel) {
        // If player hit on client or server
        if(!pAttacker.level().isClientSide()) {
            ServerLevel level = ((ServerLevel) pAttacker.level());
            BlockPos position = pTarget.blockPosition();

            // Lightning Striker's enchantment levels

            // One Lightning Bolt
            if (pLevel == 1) {
                EntityType.LIGHTNING_BOLT.spawn(level, null, (Player) null, position,
                        MobSpawnType.TRIGGERED, true, true);
            }

            // Twice Lightning Bolt
            if (pLevel == 2) {
                EntityType.LIGHTNING_BOLT.spawn(level, null, (Player) null, position,
                        MobSpawnType.TRIGGERED, true, true);
                EntityType.LIGHTNING_BOLT.spawn(level, null, (Player) null, position,
                        MobSpawnType.TRIGGERED, true, true);
            }
        }

        super.doPostAttack(pAttacker, pTarget, pLevel);
    }

    // Lightning Striker's enchantment max level
    @Override
    public int getMaxLevel() {
        return 2;
    }
}
