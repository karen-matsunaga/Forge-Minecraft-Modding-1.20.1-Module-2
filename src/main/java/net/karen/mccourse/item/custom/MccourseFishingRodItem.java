package net.karen.mccourse.item.custom;

import net.karen.mccourse.enchantment.ModEnchantments;
import net.minecraft.network.chat.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.*;
import org.jetbrains.annotations.*;
import java.util.List;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;
import static net.minecraft.world.item.enchantment.EnchantmentCategory.*;

public class MccourseFishingRodItem extends Item implements Vanishable {
    public MccourseFishingRodItem(Item.Properties properties) { super(properties); }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand); // Mccourse Fishing Rod on main hand
        if (!level.isClientSide()) {
            if (player.fishing != null) {
                item.hurtAndBreak(player.fishing.retrieve(item), player, (player1) -> player1.broadcastBreakEvent(hand));
                neutralSoundValue(level, player, SoundEvents.FISHING_BOBBER_THROW, 0.5F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            }
            else {
                neutralSoundValue(level, player, SoundEvents.FISHING_BOBBER_THROW, 0.0F);
                int lure = EnchantmentHelper.getFishingSpeedBonus(item), luck = EnchantmentHelper.getFishingLuckBonus(item);
                level.addFreshEntity(new FishingHook(player, level, luck, lure));
                player.awardStat(Stats.ITEM_USED.get(this));
                player.gameEvent(GameEvent.ITEM_INTERACT_START);
            }
        }
        return InteractionResultHolder.sidedSuccess(item, level.isClientSide());
    }

    @Override
    public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
        return ToolActions.DEFAULT_FISHING_ROD_ACTIONS.contains(toolAction);
    }

    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) { return true; } // Mccourse Fishing Rod can be enchanted

    @Override
    public int getEnchantmentValue(ItemStack stack) { return 1; }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        List<Enchantment> ALLOWED_ENCHANTMENTS = List.of(Enchantments.FISHING_LUCK, ModEnchantments.BETTER_FISHING.get());
        return ALLOWED_ENCHANTMENTS.contains(enchantment) || enchantment.category == BREAKABLE ||
               super.canApplyAtEnchantingTable(stack, enchantment); // Mccourse Fishing Rod can be enchanted on Enchantment Table
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return Component.translatable(this.getDescriptionId(stack)).withStyle(purple); // Appears on name item
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level pLevel,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        tooltipLine(list, "More faster than vanilla Fishing Rod", white); // Appears on tooltip
        tooltipLine(list, "Exclusive drops as Salmon, etc.", darkGray);
        list.add(CommonComponents.EMPTY);
    }
}