package net.karen.mccourse.item.custom;

import net.karen.mccourse.entity.custom.MagicProjectileEntity;
import net.karen.mccourse.sound.ModSounds;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RadiationStaffItem extends Item {
    public RadiationStaffItem(Properties pProperties) { super(pProperties); }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) { // Magic Projectile only right click on mouse button
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand); // Player has RADIATION STAFF item
        pLevel.playSound(null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), ModSounds.METAL_DETECTOR_FOUND_ORE.get(), SoundSource.NEUTRAL,
                1.5F, 1F); // Sound of Magic Projectile
        pPlayer.getCooldowns().addCooldown(this, 40);

        if(!pLevel.isClientSide()) { // CLIENT / SERVER created Magic Projectile
            MagicProjectileEntity magicProjectile = new MagicProjectileEntity(pLevel, pPlayer);
            magicProjectile.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 0.25F); // Added velocity and inaccuracy
            pLevel.addFreshEntity(magicProjectile); // Added Magic Projectile on world
        }

        pPlayer.awardStat(Stats.ITEM_USED.get(this));
        if (!pPlayer.getAbilities().instabuild) { itemstack.hurtAndBreak(1, pPlayer, p -> p.broadcastBreakEvent(pUsedHand)); } // Player is on Creative mode
        return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
    }
}