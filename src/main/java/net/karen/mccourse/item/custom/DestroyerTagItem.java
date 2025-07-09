package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ModTags;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.Collection;
import java.util.List;

import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class DestroyerTagItem extends Item {
    private final String nameTag;
    public DestroyerTagItem(Properties properties, String nameTag) {
        super(properties);
        this.nameTag = nameTag;
    }

    public String getNameTag() { return this.nameTag; }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level,
                                                           @NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack getItem = player.getItemInHand(hand), mainHand = player.getMainHandItem();
        if (!level.isClientSide() && !mainHand.isEmpty() && mainHand != getItem) {
            CompoundTag getTag = mainHand.getTag();
            if (mainHand.hasTag() && getTag != null && getTag.getBoolean(nameTag)) {
                mainHand.removeTagKey(nameTag); // Removed Tag
                Collection<Item> items = ForgeRegistries.ITEMS.getValues();
                @Nullable ITagManager<Item> tagItem = ForgeRegistries.ITEMS.tags();
                items.forEach(item -> { // Restore item from removed tag
                    if (tagItem != null && tagItem.getTag(ModTags.Items.DESTROYER_TAG_ITEMS).contains(item)) {
                        if (item.toString().contains(nameTag)) {
                            dropFish(level, player.getX(), player.getY(), player.getZ(), new ItemStack(item));
                        }
                    }
                });
                player(player, "Removed " + itemLines(nameTag.replace("_", " ")) + " tag!", green);
                consumeInfinite(player, getItem);
                return InteractionResultHolder.success(getItem);
            }
            else { player(player, "Item without " + itemLines(nameTag.replace("_", " ")) + "!", red); }
        }
        return InteractionResultHolder.pass(getItem);
    }

    @Override
    public @NotNull Component getName(ItemStack stack) {
        return componentTranslatable(itemLines(stack.getDescriptionId().replace("_", " ")), darkRed);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        String format = stack.getDescriptionId().replace("_", " ");
        tooltipLine(list, "Removed " + itemLines(format) + "tag!", red);
    }
}