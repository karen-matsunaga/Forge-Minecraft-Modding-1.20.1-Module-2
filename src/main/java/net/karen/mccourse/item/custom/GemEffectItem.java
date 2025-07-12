package net.karen.mccourse.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.*;
import java.util.List;
import static net.karen.mccourse.util.ChatUtil.*;

public class GemEffectItem extends Item {
    private final MobEffect effect;
    private final int duration, amplifier;
    private static final int[] COLORS = { 0xff5555, 0xffaa00, 0xffff55, 0x55ff55, 0x55ffff, 0x5555ff, 0xff55ff };

    public GemEffectItem(Properties properties, MobEffect effect, int duration, int amplifier) {
        super(properties);
        this.effect = effect;
        this.duration = duration;
        this.amplifier = amplifier;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) { return tooltipLineTranslatableRGB(COLORS, stack); }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        tooltipLineLiteralRGB(list, COLORS, stack, setGemEffectStage(stack) ? " Activated" : " Disable"); // Tooltip message
    }

    @Override
    public boolean isFoil(@NotNull ItemStack stack) { return setGemEffectStage(stack); }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack mainHand = player.getItemInHand(hand); // Gem Effect item has on MAIN HAND slot
        if (!level.isClientSide() && !mainHand.isEmpty() && mainHand.getItem() instanceof GemEffectItem) {
            mainHand.getOrCreateTag().putBoolean("GemEffect", !setGemEffectStage(mainHand)); // Gem Effect Stage
            messageLiteralRGB(player, COLORS, mainHand, setGemEffectStage(mainHand) ? " Activated" : " Disable"); // Screen message
        }
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, mainHand);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level, @NotNull Entity entity,
                              int slotId, boolean isSelected) {
        MobEffectInstance effect = new MobEffectInstance(this.effect, this.duration, this.amplifier,
                                                true, true, true);
        Player player = (Player) entity;
        if (setGemEffectStage(stack)) { player.addEffect(effect); } // Added effect
        if (!setGemEffectStage(stack)) { player.removeEffect(effect.getEffect()); } // Removed effect
    }

    // CUSTOM METHOD - Set NBT boolean stage
    private boolean setGemEffectStage(ItemStack stack) {
       return stack.getTag() != null && stack.getOrCreateTag().contains("GemEffect") && stack.getTag().getBoolean("GemEffect");
    }
}