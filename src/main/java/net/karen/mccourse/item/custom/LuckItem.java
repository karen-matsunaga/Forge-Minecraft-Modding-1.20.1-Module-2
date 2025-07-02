package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ChatUtil;
import net.karen.mccourse.util.Utils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.*;
import java.util.stream.Collectors;
import static net.minecraft.world.item.enchantment.EnchantmentCategory.*;

public class LuckItem extends Item {
    // Number of books, enchantments by book, enchantment level, enchantment type
    private final int BOOKS_TO_GENERATE, ENCHANTMENTS_PER_BOOK, ENCHANTMENT_LEVEL, ENCHANTMENT_TYPE;
    private final EnchantmentCategory ENCHANTMENT_CATEGORY; // Enchantments category

    public LuckItem(Properties properties, int book, int enchant, int level,
                    EnchantmentCategory category, int type) {
        super(properties);
        this.BOOKS_TO_GENERATE = book;
        this.ENCHANTMENTS_PER_BOOK = enchant;
        this.ENCHANTMENT_LEVEL = level;
        this.ENCHANTMENT_CATEGORY = category;
        this.ENCHANTMENT_TYPE = type;
    }

    @Override
    public void appendHoverText(ItemStack item, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        String name = item.getDescriptionId().replace("item.mccourse.", "").replace("_", " ");
        ChatUtil.tooltipLine(list, "Good luck! " + name.toUpperCase(), Utils.purple); // Added description of Luck item
        super.appendHoverText(item, level, list, flag);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand); // Player get item on main hand
        if (!world.isClientSide()) { // Player press Right-click ACTIVATED item
            Random random = new Random(); // Activated random enchantment
            for (int i = 0; i < BOOKS_TO_GENERATE; i++) { // Book quantity
                ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK); // Enchanted book to add the enchantments
                Map<Enchantment, Integer> books = new HashMap<>(); // List to store the book's enchantments
                List<Enchantment> enchantmentType = getEnchantmentsByCategory(ENCHANTMENT_CATEGORY);
                for (int j = 0; j < ENCHANTMENTS_PER_BOOK; j++) { // Adding various enchantments on the book
                    switch (ENCHANTMENT_TYPE) {
                        case 0 -> { // Enchantments are RANDOM
                            Enchantment randomEnchantment = getRandomEnchantment(random);
                            if (randomEnchantment != null) { books.put(randomEnchantment, randomEnchantment.getMaxLevel()); } }
                        case 1 -> { // SOME enchantments
                            Enchantment randomEnchant = enchantmentType.get(random.nextInt(enchantmentType.size()));
                            if (randomEnchant.getMaxLevel() == 1) { books.put(randomEnchant, randomEnchant.getMaxLevel()); }
                            else if (randomEnchant.getMaxLevel() >= 1) { books.put(randomEnchant, ENCHANTMENT_LEVEL); } }
                        case 2 -> enchantmentType.forEach(enchant -> books.put(enchant, ENCHANTMENT_LEVEL)); // ALL enchantments
                    }
                }
                EnchantmentHelper.setEnchantments(books, enchantedBook); // Applies the enchantments to the book
                // Give the book to the player -> If inventory is full drop on ground
                if (!player.getInventory().add(enchantedBook)) { player.drop(enchantedBook, false); }
            }
        }
        itemStack.shrink(1); // Consumes the used item
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }

    // CUSTOM METHOD - Enchantment random (Mixing categories: only sword, only armor, etc.)
    private Enchantment getRandomEnchantment(Random random) {
        List<Enchantment> allEnchantments = new ArrayList<>(); // Separating enchantments by TYPE
        allEnchantments.addAll(getEnchantmentsByCategory(WEAPON)); // Sword category
        allEnchantments.addAll(getEnchantmentsByCategory(ARMOR)); // Armor category
        allEnchantments.addAll(getEnchantmentsByCategory(DIGGER)); // Pickaxe category
        allEnchantments.addAll(getEnchantmentsByCategory(BREAKABLE)); // Breakable category
        allEnchantments.addAll(getEnchantmentsByCategory(FISHING_ROD)); // Fishing category
        allEnchantments.addAll(getEnchantmentsByCategory(TRIDENT)); // Trident category
        allEnchantments.addAll(getEnchantmentsByCategory(ARMOR_FEET)); // Armor Feet category
        allEnchantments.addAll(getEnchantmentsByCategory(ARMOR_CHEST)); // Armor Chest category
        allEnchantments.addAll(getEnchantmentsByCategory(ARMOR_HEAD)); // Armor Head category
        allEnchantments.addAll(getEnchantmentsByCategory(ARMOR_LEGS)); // Armor Legs category
        allEnchantments.addAll(getEnchantmentsByCategory(BOW)); // Bow category
        allEnchantments.addAll(getEnchantmentsByCategory(CROSSBOW)); // Crossbow category
        if (allEnchantments.isEmpty()) { return null; } // If the list is empty, it ignores and returns nothing
        return allEnchantments.get(random.nextInt(allEnchantments.size())); // Returns a random enchantment from the list
    }

    // CUSTOM METHOD - Enchantment Category (Filter by category and added category in a list)
    private List<Enchantment> getEnchantmentsByCategory(EnchantmentCategory category) {
        return ForgeRegistries.ENCHANTMENTS.getValues().stream().filter(enchantment -> enchantment.category == category)
                                                                .collect(Collectors.toList());
    }
}