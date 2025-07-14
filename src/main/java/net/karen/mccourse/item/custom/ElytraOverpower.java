package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import static net.karen.mccourse.util.ChatUtil.*;

public class ElytraOverpower extends ElytraItem {
    private static final int[] COLORS = { 0xff5555, 0xffaa00, 0xffff55, 0x55ff55, 0x55ffff, 0x5555ff, 0xff55ff };

    public ElytraOverpower(Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        MobEffectInstance effect = new MobEffectInstance(MobEffects.REGENERATION, 200, 4,
                                                true, true, true);
        boolean isElytra = player.getInventory().getArmor(2).is(this);
        if (isElytra) { player.addEffect(effect); } // Effect Activated
        if (!isElytra) { player.removeEffect(effect.getEffect()); } // Effect Disabled
        super.onArmorTick(stack, level, player);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) { return componentTranslatable(stack.getDescriptionId(), Utils.aqua); }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        String message = " with more durability and receive an effect! \nEffect: REGENERATION V.";
        tooltipLineLiteralRGB(list, COLORS, stack, message);
        super.appendHoverText(stack, level, list, flag);
    }
}