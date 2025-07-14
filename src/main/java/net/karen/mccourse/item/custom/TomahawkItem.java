package net.karen.mccourse.item.custom;

import net.karen.mccourse.entity.custom.TomahawkProjectileEntity;
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

public class TomahawkItem extends Item {
    public TomahawkItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Utils.neutralSoundValue(level, player, SoundEvents.SNOWBALL_THROW, 0.0F);
        if (!level.isClientSide()) {
            TomahawkProjectileEntity tomahawkProjectile = new TomahawkProjectileEntity(player, level);
            tomahawkProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0F);
            level.addFreshEntity(tomahawkProjectile);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) { itemstack.shrink(1); }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}