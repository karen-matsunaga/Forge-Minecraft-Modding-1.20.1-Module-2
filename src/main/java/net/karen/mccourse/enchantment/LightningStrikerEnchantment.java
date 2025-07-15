package net.karen.mccourse.enchantment;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.*;
import org.jetbrains.annotations.NotNull;

public class LightningStrikerEnchantment extends Enchantment {
    protected LightningStrikerEnchantment(Rarity rarity, EnchantmentCategory category,
                                          EquipmentSlot... equipmentSlots) { super(rarity, category, equipmentSlots); }

    // DEFAULT METHOD - Lightning Striker function
    @Override
    public void doPostAttack(LivingEntity attacker, @NotNull Entity target, int integer) {
        if(!attacker.level().isClientSide()) { // If player hit on SERVER side
            ServerLevel level = ((ServerLevel) attacker.level()); // Entity target (Animal, Mobs, Player, etc.)
            BlockPos position = target.blockPosition(); // Lightning position
            if (integer == 1) { lightningBolt(level, position); } // One Lightning Bolt -> LIGHTNING STRIKER level 1
            if (integer == 2) { // Twice Lightning Bolt -> LIGHTNING STRIKER level 2
                lightningBolt(level, position);
                lightningBolt(level, position);
            }
        }
        super.doPostAttack(attacker, target, integer);
    }

    @Override
    public int getMaxLevel() { return 2; } // Lightning Striker's enchantment max level

    // CUSTOM METHOD - Spawn Lightning Bolt
    private void lightningBolt(ServerLevel level, BlockPos position) {
        EntityType.LIGHTNING_BOLT.spawn(level, null, (Player) null, position, MobSpawnType.TRIGGERED,
                                        true, true);
    }
}