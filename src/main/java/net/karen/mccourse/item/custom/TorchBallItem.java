package net.karen.mccourse.item.custom;

import net.karen.mccourse.entity.custom.TorchBallProjectileEntity;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.*;
import java.util.List;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class TorchBallItem extends Item {
    public TorchBallItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand); // When used Torch Ball item pressed right mouse button
        neutralSoundValue(level, player, SoundEvents.REDSTONE_TORCH_BURNOUT, 0.0F);
        player.getCooldowns().addCooldown(this, 0); // Nothing cooldown
        if (!level.isClientSide()) {
            TorchBallProjectileEntity torchBallProjectile = new TorchBallProjectileEntity(level, player);
            torchBallProjectile.setItem(new ItemStack(Blocks.TORCH.asItem()));
            torchBallProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 1.5F, 0F);
            level.addFreshEntity(torchBallProjectile);
        }
        player.awardStat(Stats.ITEM_USED.get(this));
        if (!player.getAbilities().instabuild) { item.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand)); }
        return InteractionResultHolder.sidedSuccess(item, level.isClientSide());
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) { return componentTranslatable(stack.getDescriptionId(), gold); }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        String upper = itemLine(stack.getDescriptionId(), "item.mccourse.", "", "_", " ");
        tooltipLine(list, itemLines(upper) + " when hit added torch!", yellow);
    }
}