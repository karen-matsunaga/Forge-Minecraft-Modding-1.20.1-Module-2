package net.karen.mccourse.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ForgeRegistries.ITEMS.getCodec().listOf().fieldOf("items").forGetter(m -> m.items)).apply(inst, AddItemModifier::new)));

    private final List<Item> items;

    public AddItemModifier(LootItemCondition[] conditionsIn, List<Item> items) { super(conditionsIn); this.items = items; }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.clear(); // Clear old's loot tables
        if (context.getParamOrNull(LootContextParams.TOOL) != null && context.getParamOrNull(LootContextParams.TOOL).getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
            for (Item item : items) {  // Loop through each item in the list
                if (context.getQueriedLootTableId().equals(Blocks.DIAMOND_ORE.getLootTable())) { // If mined DIAMOND ORE with Silk Touch's enchantment
                        generatedLoot.add(new ItemStack(item)); // Drop itself
                } else if (context.getQueriedLootTableId().equals(Blocks.ANCIENT_DEBRIS.getLootTable())) { // If mined ANCIENT DEBRIS with Silk Touch's enchantment
                        generatedLoot.add(new ItemStack(item)); // Drop itself
                }
            }
        }

        if (context.getParamOrNull(LootContextParams.TOOL) != null && context.getParamOrNull(LootContextParams.TOOL).getEnchantmentLevel(Enchantments.BLOCK_FORTUNE) > 0) {
            int fortuneLevel = context.getParamOrNull(LootContextParams.TOOL).getEnchantmentLevel(Enchantments.BLOCK_FORTUNE); // Fortune's enchantment level
            int drops = context.getRandom().nextInt(fortuneLevel) + UniformGenerator.between(1.0f, 2.0f).getInt(context); // Drops randomly

            for (Item item : items) {  // Loop through each item in the list
                if (context.getQueriedLootTableId().equals(Blocks.DIAMOND_ORE.getLootTable())) { // If mined DIAMOND ORE with Fortune's enchantment
                        generatedLoot.add(new ItemStack(item, drops)); // Drop multiple diamond's ores and diamonds
                } else if (context.getQueriedLootTableId().equals(Blocks.ANCIENT_DEBRIS.getLootTable())) { // If mined ANCIENT DEBRIS with Fortune's enchantment
                        generatedLoot.add(new ItemStack(item, drops)); // Drop multiple ancient debris's ores and netherite scraps
                }
            }

            for (ItemStack itemStack : generatedLoot) { // All modifications of ore's loot tables
                UniformGenerator.between(4.0F, 5.0F).getInt(context);
                ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 4).build().apply(itemStack, context);
                ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE).build().apply(itemStack, context);
                ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE).build().apply(itemStack, context);
            }
        }

        for (LootItemCondition condition : this.conditions) { if (!condition.test(context)) { return generatedLoot; } } // Apply loot conditions, and, add item on the list

        for (Item item : items) { generatedLoot.add(new ItemStack(item)); } // No enchantment add item on the list normally, update the data, and, return the list

        return generatedLoot; // Return normal loot modifier even if to exist 1000 items
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() { return CODEC.get(); }

}