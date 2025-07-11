package net.karen.mccourse.item.custom;

import net.karen.mccourse.item.ModItems;
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

public class InfiniteItem extends Item {
    private final String nameTag;

    public InfiniteItem(Properties properties, String nameTag) {
        super(properties);
        this.nameTag = nameTag;
    }

    public String getNameTag() { return this.nameTag; } // Used on Infinite Network

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack offHand = player.getItemInHand(hand), mainHand = player.getMainHandItem();
        if (!level.isClientSide() && !mainHand.isEmpty() && mainHand != offHand) {
            CompoundTag getTag =  mainHand.getTag(), createTag = mainHand.getOrCreateTag();
            String split = splitWord(nameTag), upper = upperString(split);
            if (getTag != null && getTag.getBoolean(nameTag)) { // Item has Unbreakable tag or Lucky Bomb tag
                return fail(player, "This item is already " + upper + " tag!", yellow, offHand);
            }
            else if (getTag != null && !getTag.getBoolean(nameTag)) { // Apply Unbreakable tag or Lucky Bomb tag
                createTag.putBoolean(nameTag, true);
                player(player, "Added " + upper + " tag!", green);
                consumeInfinite(player, offHand);
                return InteractionResultHolder.success(offHand);
            }
        }
        player(player, "Hold the tool in your main hand!", red);
        return InteractionResultHolder.pass(offHand);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) { return componentTranslatable(stack.getDescriptionId(), aqua); }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        String message = "Click on item to your tools or armors transform on ",
                  item = this.toString().replace("_", " ");
        if (stack.is(ModItems.INFINITE.get())) { tooltipLine(list, message + itemLines(item) + " durability!", purple); }
        if (stack.is(ModItems.LUCKY_BOMB.get())) { tooltipLine(list, message + itemLines(item) + " tag!", gold); }
        super.appendHoverText(stack, pLevel, list, flag);
    }
}