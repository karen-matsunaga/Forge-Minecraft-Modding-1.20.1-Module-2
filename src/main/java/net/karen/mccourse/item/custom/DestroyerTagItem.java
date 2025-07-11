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
    private final String nameTag; // Name tag -> Ex: Unbreakable, Lucky Bomb, etc.

    public DestroyerTagItem(Properties properties, String nameTag) {
        super(properties);
        this.nameTag = nameTag;
    }

    public String getNameTag() { return this.nameTag; } // Used on Destroyer Tag Slot Message Network

    // DEFAULT METHOD - Action when pressed right-click mouse button
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack offHand = player.getItemInHand(hand), mainHand = player.getMainHandItem();
        if (!level.isClientSide() && !mainHand.isEmpty() && mainHand != offHand) {
            CompoundTag getTag = mainHand.getTag(); // Used item on MAIN HAND inventory
            boolean notNull = getTag != null, notTag = notNull && !getTag.getBoolean(nameTag);
            // Item WITHOUT tag
            if (notTag) { return fail(player, "Item without " + itemLines(nameTag) + " tag!", red, offHand); }
            else if (notNull && getTag.getBoolean(nameTag)) { // Removed Unbreakable tag or Lucky Bomb tag
                mainHand.removeTagKey(nameTag);
                player(player, "Removed " + itemLines(nameTag) + " tag!", green);
                consumeInfinite(player, offHand); // Consumes OFFHAND item Destroyer Unbreakable Tag or Destroyer Lucky Bomb Tag
                return InteractionResultHolder.success(offHand);
            }
        }
        player(player, "Hold the tool in your main hand!", red); // Tool or armor on MAIN HAND
        return InteractionResultHolder.pass(offHand);
    }

    // DEFAULT METHOD - Name of item
    @Override
    public @NotNull Component getName(ItemStack stack) { return componentTranslatable(stack.getDescriptionId(), darkRed); }

    // DEFAULT METHOD - Added more information about item
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        tooltipLine(list, "Removed " + itemLines(nameTag) + " tag!", red);
    }
}