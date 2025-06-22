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

public class GrowthItem extends Item {
    public GrowthItem(Properties properties) { super(properties); }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack stack, Player player,
                                                           @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (!player.level().isClientSide()) {
            if (entity instanceof AgeableMob ageable && ageable.isBaby()) { // Checks if the target is a Baby Animal
                ageable.setAge(0); // 0 = Adult
                ageable.level().broadcastEntityEvent(ageable, (byte) 7); // Heart particles
                if (!player.isCreative()) { stack.shrink(1); } // Consumes item
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(this.getDescriptionId(stack)).withStyle(Utils.darkAqua);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.literal("Makes a baby animal an adult animal!").withStyle(Utils.aqua));
    }
}