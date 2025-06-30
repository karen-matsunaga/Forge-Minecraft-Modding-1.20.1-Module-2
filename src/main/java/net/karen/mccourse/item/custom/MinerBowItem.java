package net.karen.mccourse.item.custom;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class MinerBowItem extends BowItem {
    public MinerBowItem(Properties properties) { super(properties); }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, Level world,
                             @NotNull LivingEntity shooter, int timeLeft) {
        if (!world.isClientSide() && shooter instanceof Player player) {
            Arrow arrow = new Arrow(world, player);
            float velocity = BowItem.getPowerForTime(stack.getUseDuration() - timeLeft); // Firing direction - 0.0 a 1.0
            if (velocity < 0.1F) { return; } // Very weak, does not launch
            // Set arrow direction
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 3.0F, 1.0F);
            arrow.setBaseDamage(0); // Damage 0
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            arrow.getPersistentData().putBoolean("MiningArrow", true);
            world.addFreshEntity(arrow);
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                 SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }
}