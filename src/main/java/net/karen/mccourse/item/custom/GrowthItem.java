package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import static net.karen.mccourse.util.ChatUtil.*;

public class GrowthItem extends Item {
    public GrowthItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player,
                                                           @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (!player.level().isClientSide() && entity instanceof AgeableMob ageable && ageable.isBaby()) {
            ageable.setAge(0); // 0 = Adult -> Checks if the target is a Baby Animal
            ageable.level().broadcastEntityEvent(ageable, (byte) 7); // Heart particles
            if (!player.isCreative()) { stack.shrink(1); } // Consumes item
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return componentTranslatable(this.getDescriptionId(stack), Utils.darkAqua);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        tooltipLine(list, "Makes a baby animal an adult animal!", Utils.aqua);
    }
}