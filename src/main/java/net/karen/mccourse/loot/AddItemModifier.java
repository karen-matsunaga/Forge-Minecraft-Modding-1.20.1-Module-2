package net.karen.mccourse.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.loot.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    private final List<Item> items;
    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() ->
           RecordCodecBuilder.create(inst -> codecStart(inst).and(ForgeRegistries.ITEMS.getCodec().listOf()
                             .fieldOf("items").forGetter(m -> m.items)).apply(inst, AddItemModifier::new)));

    public AddItemModifier(LootItemCondition[] conditionsIn, List<Item> items) {
        super(conditionsIn);
        this.items = items;
    }

    // All tools, armors and enchanted books - KEY = Items || VALUE = MAP (Enchantment, Enchantment level)
    private final Map<Item, Map<Enchantment, Integer>> itemEnchantments = Map.ofEntries(
            Map.entry(Items.DIAMOND_SWORD, Map.of(Enchantments.SHARPNESS, 3, Enchantments.UNBREAKING, 2)),
            Map.entry(Items.ENCHANTED_BOOK, Map.of(Enchantments.MOB_LOOTING, 2)),
            Map.entry(Items.IRON_PICKAXE, Map.of(Enchantments.BLOCK_EFFICIENCY, 4)),
            Map.entry(ModItems.MCCOURSE_HAMMER.get(), Map.of(Enchantments.BLOCK_EFFICIENCY, 10,
            Enchantments.BLOCK_FORTUNE, 10, ModEnchantments.RECOVER.get(), 1, Enchantments.UNBREAKING, 10)));

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.clear(); // Clear old's loot tables
        // All ores with loot table modified
        List<ResourceLocation> ores = Arrays.asList(Blocks.COAL_ORE.getLootTable(), Blocks.DEEPSLATE_COAL_ORE.getLootTable());
        ItemStack contextTool = context.getParamOrNull(LootContextParams.TOOL);
        ResourceLocation location = context.getQueriedLootTableId();
        if (contextTool != null) {
            int silkTouch = contextTool.getEnchantmentLevel(Enchantments.SILK_TOUCH); // If mined ORES with Silk Touch's enchantment
            if (silkTouch > 0) { // Silk Touch's enchantment level
                for (Item item : items) { // Loop through each item in the list and drop itself an item
                    if (ores.contains(location)) { generatedLoot.add(new ItemStack(item)); }
                }
            }
            int fortune = contextTool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE); // If mined ORES with Fortune's enchantment
            if (fortune > 0) { // Fortune's enchantment level
                // Drops randomly
                int drops = context.getRandom().nextInt(fortune) + UniformGenerator.between(1.0f, 2.0f).getInt(context);
                for (Item item : items) { // Loop through each item in the list and drop items
                    if (ores.contains(location)) { generatedLoot.add(new ItemStack(item, drops)); }
                }
                // All modifications of ore's loot tables
                Enchantment fortuneEnchantment = Enchantments.BLOCK_FORTUNE;
                for (ItemStack itemStack : generatedLoot) {
                    ApplyBonusCount.addUniformBonusCount(fortuneEnchantment, 2).build().apply(itemStack, context);
                    ApplyBonusCount.addOreBonusCount(fortuneEnchantment).build().apply(itemStack, context);
                    ApplyBonusCount.addUniformBonusCount(fortuneEnchantment).build().apply(itemStack, context);
                }
            }
        }
        for (LootItemCondition condition : this.conditions) { // Apply loot conditions, and add item on the list
            if (!condition.test(context)) { return generatedLoot; }
        }
        // No enchantment add item on the list normally, update the data, and return the list
        for (Item item : items) { // All tools, armors and enchanted books
            if (itemEnchantments.containsKey(item)) { generatedLoot.add(createEnchantedItem(item)); }
            else { generatedLoot.add(new ItemStack(item)); } // Items WITHOUT enchantment
        }
        return generatedLoot; // Return normal loot modifier even if to exist 1000 items
    }

    // CUSTOM METHOD - Enchanted TOOLS, ARMORS or ENCHANTED BOOKS
    private ItemStack createEnchantedItem(Item item) {
        ItemStack stack = new ItemStack(item);
        Map<Enchantment, Integer> enchantments = itemEnchantments.get(item);
        if (enchantments != null) { enchantments.forEach(stack::enchant); }
        return stack;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() { return CODEC.get(); }
}