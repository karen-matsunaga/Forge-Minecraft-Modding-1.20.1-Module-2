package net.karen.mccourse.item.custom;

import net.karen.mccourse.entity.custom.BouncyBallsProjectileEntity;
import net.karen.mccourse.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import static net.karen.mccourse.util.Utils.*;

public class BouncyBallsItem extends Item {
    public BouncyBallsItem(Properties properties) { super(properties); }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand); // Player has Bouncy Balls item on hand
        float pitch = 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F);
        neutralSound(level, player, SoundEvents.ENDER_PEARL_THROW, 0.5F, pitch); // Sound when used item
        player.getCooldowns().addCooldown(this, 0); // Nothing cooldown
        if (!level.isClientSide()) {
            BouncyBallsProjectileEntity thrownBouncyBalls = new BouncyBallsProjectileEntity(level, player);
            thrownBouncyBalls.setItem(new ItemStack(ModItems.BOUNCY_BALLS_PARTICLES.get()));
            thrownBouncyBalls.shootFromRotation(player,  player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(thrownBouncyBalls); // When hit appears Bouncy Balls particles
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) { itemstack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand)); }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}