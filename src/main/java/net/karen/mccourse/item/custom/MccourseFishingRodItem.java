package net.karen.mccourse.item.custom;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Vanishable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class MccourseFishingRodItem extends Item implements Vanishable {
    private static final Set<Enchantment> ALLOWED_ENCHANTMENTS = Set.of(Enchantments.FISHING_LUCK);

    public MccourseFishingRodItem(Item.Properties properties) { super(properties); }

    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player,
                                                           @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (!level.isClientSide()) {
            if (player.fishing != null) {
                int i = player.fishing.retrieve(item);
                item.hurtAndBreak(i, player, (player1) -> player1.broadcastBreakEvent(hand));
                song(level, player, SoundEvents.FISHING_BOBBER_RETRIEVE, 1.0F);
                player.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
            }
            else {
                song(level, player, SoundEvents.FISHING_BOBBER_THROW, 0.5F);
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

    // METHOD - Mccourse Fishing Rod can be enchanted
    @Override
    public boolean isEnchantable(@NotNull ItemStack stack) { return true; }

    @Override
    public int getEnchantmentValue(ItemStack stack) { return 1; }

    // METHOD - Mccourse Fishing Rod can be enchanted on Enchantment Table
    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return ALLOWED_ENCHANTMENTS.contains(enchantment) || enchantment.category == EnchantmentCategory.BREAKABLE ||
               super.canApplyAtEnchantingTable(stack, enchantment);
    }

    // CUSTOM METHOD - Fish sound
    private void song(Level level, Player player, SoundEvent sound, float volume) {
        level.playSound(null, player.getX(), player.getY(), player.getZ(), sound,
              SoundSource.NEUTRAL, volume, 0.4F / (level.getRandom().nextFloat() * 0.4F + 0.8F));
    }
}