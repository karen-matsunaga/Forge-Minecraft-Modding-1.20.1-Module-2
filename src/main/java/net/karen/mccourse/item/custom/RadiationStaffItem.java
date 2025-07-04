package net.karen.mccourse.item.custom;

import net.karen.mccourse.entity.custom.MagicProjectileEntity;
import net.karen.mccourse.sound.ModSounds;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import static net.karen.mccourse.util.Utils.*;

public class RadiationStaffItem extends Item {
    public RadiationStaffItem(Properties properties) { super(properties); }

    @Override // Magic Projectile only right click on mouse button
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand); // Player has RADIATION STAFF item
        neutralSound(level, player, ModSounds.METAL_DETECTOR_FOUND_ORE.get(), 1.5F, 1F); // Sound of Magic Projectile
        player.getCooldowns().addCooldown(this, 20);
        if (!level.isClientSide()) { // CLIENT and SERVER created Magic Projectile
            MagicProjectileEntity magicProjectile = new MagicProjectileEntity(level, player); // Added velocity and inaccuracy
            magicProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0.25F);
            level.addFreshEntity(magicProjectile); // Added Magic Projectile on world
        }
        player.awardStat(Stats.ITEM_USED.get(this)); // Item hurt
        if (!player.getAbilities().instabuild) { itemstack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand)); }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}