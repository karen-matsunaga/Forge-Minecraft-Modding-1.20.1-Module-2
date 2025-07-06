package net.karen.mccourse.item.custom;

import net.karen.mccourse.entity.custom.DiceProjectileEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import static net.karen.mccourse.util.Utils.*;

public class DiceItem extends Item {
    public DiceItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand); // When used Dice item pressed right mouse button
        neutralSoundValue(level, player, SoundEvents.SNOWBALL_THROW, 0.0F);
        if (!level.isClientSide()) {
            DiceProjectileEntity diceProjectile = new DiceProjectileEntity(level, player);
            diceProjectile.setItem(itemstack);
            diceProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0F);
            level.addFreshEntity(diceProjectile);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) { itemstack.shrink(1); }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}