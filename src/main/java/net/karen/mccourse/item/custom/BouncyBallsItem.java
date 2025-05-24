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
    public BouncyBallsItem(Properties properties) { super(properties); }

    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENDER_PEARL_THROW,
                SoundSource.NEUTRAL, 0.5F, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
        player.getCooldowns().addCooldown(this, 0); // Nothing cooldown
        if (!level.isClientSide()) {
            BouncyBallsProjectileEntity thrownbouncyballs = new BouncyBallsProjectileEntity(level, player);
            thrownbouncyballs.setItem(new ItemStack(ModItems.BOUNCY_BALLS_PARTICLES.get()));
            thrownbouncyballs.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 1.0F);
            level.addFreshEntity(thrownbouncyballs);
        }

        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) { itemstack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand)); }

        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}