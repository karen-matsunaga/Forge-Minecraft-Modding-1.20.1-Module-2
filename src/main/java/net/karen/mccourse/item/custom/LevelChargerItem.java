package net.karen.mccourse.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.*;
import java.util.*;
import net.karen.mccourse.util.*;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

public class LevelChargerItem extends Item {
    private final int changeAmount; // +1 (Increase) or -1 (Decrease) enchantment level
    private final Enchantment enchantment; // Increase or decrease enchantment level (Ex: Fortune +1 or -1)

    public LevelChargerItem(Properties properties, int changeAmount, Enchantment enchantment) {
        super(properties);
        this.changeAmount = changeAmount;
        this.enchantment = enchantment;
    }

    // DEFAULT METHOD - Level Charger item used on Main hand + RIGHT CLICK
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player player, @NotNull InteractionHand hand) {
        InteractionHand otherHand = (hand == mainHand) ? offhand : mainHand; // Player's MAIN HAND and OFFHAND
        ItemStack changerStack = player.getItemInHand(hand), targetStack = player.getItemInHand(otherHand);
        if (applyTo(player, targetStack, changerStack) && changerStack.is(ModTags.Items.LEVEL_CHARGER_ALL)) {
            itemHurt(player, ModTags.Items.LEVEL_CHARGER_GENERAL, changerStack); // GENERAL enchantment
            itemHurt(player, ModTags.Items.LEVEL_CHARGER_SPECIFIC, changerStack); // SPECIFIC enchantment
            changerStack.shrink(1);
            return InteractionResultHolder.success(changerStack);
        }
        else { return InteractionResultHolder.fail(changerStack); }
    }

    // DEFAULT METHOD - Added TOOLTIP on all Level Charger
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        boolean value = (changeAmount == 1);
        tooltipLines(list, stack, (value ? " increase +" : " decrease ") + changeAmount + " level.", value ? green : red);
        super.appendHoverText(stack, level, list, flag);
    }

    // DEFAULT METHOD - Added NAME on all Level Charger -> Translatable en_us.json
    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Component baseName = super.getName(stack);
        if (stack.is(ModTags.Items.LEVEL_CHARGER_PLUS_ENCHANT)) { return baseName.copy().withStyle(green); }
        else if (stack.is(ModTags.Items.LEVEL_CHARGER_MINUS_ENCHANT)) { return baseName.copy().withStyle(red); }
        return baseName;
    }

    // CUSTOM METHOD - Apply Level Charger Plus, Level Charger Minus, etc. on enchanted book, armor or tool
    public static boolean applyTo(Player player, ItemStack targetStack, ItemStack changerStack) {
        if (!(changerStack.getItem() instanceof LevelChargerItem self)) { return false; }
        Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(targetStack); // Enchanted books, armors or tools
        Enchantment enchant = self.enchantment;
        if (enchants.isEmpty()) { return fail(player, "The item has no enchantments!", darkRed); } // Empty enchants
        if (enchant != null && !enchants.containsKey(enchant)) { // General or specific enchantment types
            return fail(player, "This item doesn't have the required enchantment!", gray);
        }
        if (self.changeAmount < 0) { // Check enchantment levels
            boolean allMin = enchants.entrySet().stream().filter(e -> enchant == null || e.getKey() == enchant)
                                     .allMatch(e -> e.getValue() <= 1); // All enchantment are with min level is 1
            if (allMin) { return fail(player, "All enchantments are already at level 1!", aqua); }
        }
        if (!player.level().isClientSide()) { // Applies new enchantment levels on Enchanted books, armors or tools
            Map<Enchantment, Integer> newEnchants = new HashMap<>(enchants);
            enchants.forEach((enc, lvl) -> { // Armors or tools -> New enchantment level
                if (enchant == null || enc == enchant) { newEnchants.put(enc, Math.max(1, lvl + self.changeAmount)); }});
            if (targetStack.getItem() instanceof EnchantedBookItem) { // Enchanted books
                ItemStack newBook = new ItemStack(Items.ENCHANTED_BOOK);
                newEnchants.forEach((enc, lvl) -> EnchantedBookItem.addEnchantment(newBook, new EnchantmentInstance(enc, lvl)));
                player.setItemInHand(InteractionHand.OFF_HAND, newBook);
            }
            else { EnchantmentHelper.setEnchantments(newEnchants, targetStack); }
        }
        return true;
    }

    // CUSTOM METHOD - Message when consumed Level Charger (Plus / Minus) items
    private void itemHurt(Player player, TagKey<Item> items, ItemStack chargerStack) {
        String pos = "Increased +", neg = "Decreased ", screen = changeAmount + " level(s)!";
        var itemsTag = ForgeRegistries.ITEMS.tags();
        if (itemsTag != null) {
            if (itemsTag.getTag(items).contains(chargerStack.getItem())) {
                String name = chargerStack.getItem().getDescriptionId();
                boolean isPlus = name.contains("plus"), isMinus = name.contains("minus"),
                        isNull = (enchantment == null), notNull = (enchantment != null);
                if (isNull) { playerStyleBool(player, isPlus, isMinus, pos + screen, neg + screen, green, red); }
                if (notNull) {
                    String item = itemLine(enchantment.getDescriptionId(), vanilla, "", mod, ""),
                           upper = itemLines(item), message = changeAmount + " " + upper + " level!";
                    playerStyleBool(player, isPlus, isMinus, pos + message, neg + message, green, red);
                }
            }
        }
    }
}