package net.karen.mccourse.enchantment;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import org.jetbrains.annotations.NotNull;

public class LightningStrikerEnchantment extends Enchantment {
    protected LightningStrikerEnchantment(Rarity pRarity, EnchantmentCategory pCategory, EquipmentSlot... pApplicableSlots) { super(pRarity, pCategory, pApplicableSlots); }

    // Lightning Striker method
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

    @Override
    public int getMaxLevel() { return 2; } // Lightning Striker's enchantment max level

    @Override
    public @NotNull Component getFullname(int pLevel) {
        if (pLevel > 0) {
            return Component.translatable(this.getDescriptionId()).withStyle(ChatFormatting.BOLD, ChatFormatting.RED) // Colors used on description name
                    .append(CommonComponents.SPACE) // Separate words and numbers
                    .append(Component.translatable("enchantment.level." + pLevel)); // Name and level enchantment on item, and chat
        }
        return super.getFullname(pLevel);
    }
}