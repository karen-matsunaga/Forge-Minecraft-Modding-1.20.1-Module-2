package net.karen.mccourse.item.custom;

import net.karen.mccourse.entity.custom.BouncyBallsProjectileEntity;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.util.Utils;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BouncyBallsItem extends Item {
    public BouncyBallsItem(Properties properties) { super(properties); }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand); // Player has Bouncy Balls item on hand
        float volume = 0.5F, pitch = 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F);
        Utils.neutralSound(level, player, SoundEvents.ENDER_PEARL_THROW, volume, pitch); // Sound when used item
        player.getCooldowns().addCooldown(this, 0); // Nothing cooldown
        if (!level.isClientSide()) {
            float x = player.getXRot(), y = player.getYRot(), z = 0.0F, velocity = 1.5F, inaccuracy = 1.0F;
            BouncyBallsProjectileEntity thrownBouncyBalls = new BouncyBallsProjectileEntity(level, player);
            thrownBouncyBalls.setItem(new ItemStack(ModItems.BOUNCY_BALLS_PARTICLES.get()));
            thrownBouncyBalls.shootFromRotation(player, x, y, z, velocity, inaccuracy);
            level.addFreshEntity(thrownBouncyBalls); // When hit appears Bouncy Balls particles
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) { itemstack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand)); }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}