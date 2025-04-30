package net.karen.mccourse.item.custom;

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
    private final int BOOKS_TO_GENERATE, ENCHANTMENTS_PER_BOOK, ENCHANTMENT_LEVEL, ENCHANTMENT_TYPE; // Number of books
    private final EnchantmentCategory ENCHANTMENT_CATEGORY; // Enchantments category

    public LuckItem(Properties pProperties, int book, int enchant, int level, EnchantmentCategory category, int type) {
        super(pProperties);
        this.BOOKS_TO_GENERATE = book;
        this.ENCHANTMENTS_PER_BOOK = enchant;
        this.ENCHANTMENT_LEVEL = level;
        this.ENCHANTMENT_CATEGORY = category;
        this.ENCHANTMENT_TYPE = type;
    }

    // Player press Right-click activated item
    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand); // Player get item on main hand

        if (!world.isClientSide()) {
            Random random = new Random(); // Activated random enchantment

            // Book quantity
            for (int i = 0; i < BOOKS_TO_GENERATE; i++) {
                ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK); // Enchanted book to add the enchantments
                Map<Enchantment, Integer> enchantments = new HashMap<>(); // List to store the book's enchantments
                List<Enchantment> enchantmentType = getEnchantmentsByCategory(ENCHANTMENT_CATEGORY);

                // Adding various enchantments on the book
                for (int j = 0; j < ENCHANTMENTS_PER_BOOK; j++) {
                    switch (ENCHANTMENT_TYPE) {
                        case 0:
                            Enchantment randomEnchantment = getRandomEnchantment(random); // Enchantments are random
                            if (randomEnchantment != null) { enchantments.put(randomEnchantment, randomEnchantment.getMaxLevel()); }
                        break;
                        case 1: // I want some enchantments
                            Enchantment randomEnchant = enchantmentType.get(random.nextInt(enchantmentType.size()));
                            if (randomEnchant.getMaxLevel() == 1) { enchantments.put(randomEnchant, randomEnchant.getMaxLevel()); }
                            else if (randomEnchant.getMaxLevel() > 1) { enchantments.put(randomEnchant, ENCHANTMENT_LEVEL); }
                        break;
                        case 2: // I want all enchantments
                            for (Enchantment custom : enchantmentType) { enchantments.put(custom, ENCHANTMENT_LEVEL); }
                        break;
                    }
                }

                // Applies the enchantments to the book
                EnchantmentHelper.setEnchantments(enchantments, enchantedBook);

                // Give the book to the player -> If inventory is full drop on ground
                if (!player.getInventory().add(enchantedBook)) { player.drop(enchantedBook, false); }
            }
        }
        itemStack.shrink(1); // Consumes the used item
        return InteractionResultHolder.sidedSuccess(itemStack, world.isClientSide());
    }

    // Custom method - Enchantment random
    private Enchantment getRandomEnchantment(Random random) {
        // Separating enchantments by type
        // Mixing categories: only sword, only armor, etc.
        List<Enchantment> allEnchantments = new ArrayList<>();
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

        // Returns a random enchantment from the list
        return allEnchantments.get(random.nextInt(allEnchantments.size()));
    }

    // Custom method - Enchantment Category
    private List<Enchantment> getEnchantmentsByCategory(EnchantmentCategory category) {
        return ForgeRegistries.ENCHANTMENTS.getValues().stream()
                .filter(enchantment -> enchantment.category == category)  // Filter by category
                        .collect(Collectors.toList());  // Added category in a list
    }

    // Added description of Luck item
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(Component.literal("Congratulations! Good luck! " +
                pStack.getDescriptionId().replace("item.mccourse.", "").replace("_", " ")));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}