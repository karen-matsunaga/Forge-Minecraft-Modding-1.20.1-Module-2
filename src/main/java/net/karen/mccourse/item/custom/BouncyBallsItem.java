package net.karen.mccourse.item.custom;

import net.karen.mccourse.entity.custom.BouncyBallsProjectileEntity;
import net.karen.mccourse.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class BouncyBallsItem extends Item {
    public BouncyBallsItem(Properties pProperties) { super(pProperties); }

    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(),
                SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
        pPlayer.getCooldowns().addCooldown(this, 0); // Nothing cooldown
        if (!pLevel.isClientSide) {
            BouncyBallsProjectileEntity thrownbouncyballs = new BouncyBallsProjectileEntity(pLevel, pPlayer);
            thrownbouncyballs.setItem(new ItemStack(ModItems.BOUNCY_BALLS_PARTICLES.get()));
            thrownbouncyballs.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
            pLevel.addFreshEntity(thrownbouncyballs);
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) { itemstack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pHand)); }

        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }
}