package net.karen.mccourse.item.custom;

import net.karen.mccourse.entity.custom.TorchBallProjectileEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import static net.karen.mccourse.util.Utils.neutralSoundValue;

public class TorchBallItem extends Item {
    public TorchBallItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand); // When used Torch Ball item pressed right mouse button
        neutralSoundValue(level, player, SoundEvents.REDSTONE_TORCH_BURNOUT, 0.0F);
        player.getCooldowns().addCooldown(this, 0); // Nothing cooldown
        if (!level.isClientSide()) {
            TorchBallProjectileEntity torchBallProjectile = new TorchBallProjectileEntity(level, player);
            torchBallProjectile.setItem(new ItemStack(Blocks.TORCH.asItem()));
            torchBallProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0F);
            level.addFreshEntity(torchBallProjectile);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) { itemstack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand)); }
        return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
    }
}