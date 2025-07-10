package net.karen.mccourse.item.custom;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.*;
import java.util.List;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class DestroyerTagItem extends Item {
    private final String nameTag;
    public DestroyerTagItem(Properties properties, String nameTag) {
        super(properties);
        this.nameTag = nameTag;
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack offHand = player.getItemInHand(hand), mainHand = player.getMainHandItem();
        if (!level.isClientSide() && !mainHand.isEmpty() && mainHand != offHand) {
            CompoundTag getTag = mainHand.getTag();
            if (mainHand.isEmpty() || !mainHand.hasTag()) { // Item WITHOUT tag
                player(player, "Item without " + itemLines(nameTag) + "!", red);
                return InteractionResultHolder.fail(offHand);
            }
            if (mainHand.hasTag() && getTag != null && getTag.getBoolean(nameTag)) {
                mainHand.removeTagKey(nameTag); // Removed Tag
                player(player, "Removed " + itemLines(nameTag) + " tag!", green);
                consumeInfinite(player, offHand);
                return InteractionResultHolder.success(offHand);
            }
        }
        return InteractionResultHolder.pass(offHand);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) { return componentTranslatable(stack.getDescriptionId(), darkRed); }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        tooltipLine(list, "Removed " + itemLines(nameTag) + " tag!", red);
    }
}