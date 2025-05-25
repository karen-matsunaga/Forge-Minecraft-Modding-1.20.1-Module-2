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
import org.jetbrains.annotations.NotNull;

public class RadiationStaffItem extends Item {
    public RadiationStaffItem(Properties properties) { super(properties); }

    @Override // Magic Projectile only right click on mouse button
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand); // Player has RADIATION STAFF item
        level.playSound(null, player.getX(), player.getY(), player.getZ(),
                ModSounds.METAL_DETECTOR_FOUND_ORE.get(), SoundSource.NEUTRAL, 1.5F, 1F); // Sound of Magic Projectile
        player.getCooldowns().addCooldown(this, 40);
        if (!level.isClientSide()) { // CLIENT and SERVER created Magic Projectile
            MagicProjectileEntity magicProjectile = new MagicProjectileEntity(level, player); // Added velocity and inaccuracy
            magicProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0.25F);
            level.addFreshEntity(magicProjectile); // Added Magic Projectile on world
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        // Player is on Creative mode
        if (!player.getAbilities().instabuild) { itemstack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand)); }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}