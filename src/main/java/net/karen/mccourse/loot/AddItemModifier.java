package net.karen.mccourse.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
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

import java.util.Arrays;
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
        List<ResourceLocation> ores = Arrays.asList(
                Blocks.COAL_ORE.getLootTable(), Blocks.IRON_ORE.getLootTable(), Blocks.GOLD_ORE.getLootTable(), Blocks.LAPIS_ORE.getLootTable(), // Stone ORES
                Blocks.NETHER_QUARTZ_ORE.getLootTable(), Blocks.NETHER_GOLD_ORE.getLootTable(), // Nether ORES
                Blocks.DEEPSLATE_COAL_ORE.getLootTable(), Blocks.DEEPSLATE_IRON_ORE.getLootTable(), Blocks.DEEPSLATE_GOLD_ORE.getLootTable(), // Deepslate ORES
                Blocks.DEEPSLATE_LAPIS_ORE.getLootTable()); // All ores with loot table modified

        // If mined ORES with Silk Touch's enchantment
        if (context.getParamOrNull(LootContextParams.TOOL) != null && context.getParamOrNull(LootContextParams.TOOL).getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
            // Loop through each item in the list and drop itself an item
            for (Item item : items) { if (ores.contains(context.getQueriedLootTableId())) { generatedLoot.add(new ItemStack(item)); } }
        }

        // If mined ORES with Fortune's enchantment
        if (context.getParamOrNull(LootContextParams.TOOL) != null && context.getParamOrNull(LootContextParams.TOOL).getEnchantmentLevel(Enchantments.BLOCK_FORTUNE) > 0) {
            int fortuneLevel = context.getParamOrNull(LootContextParams.TOOL).getEnchantmentLevel(Enchantments.BLOCK_FORTUNE); // Fortune's enchantment level
            int drops = context.getRandom().nextInt(fortuneLevel) + UniformGenerator.between(1.0f, 2.0f).getInt(context); // Drops randomly

            // Loop through each item in the list and drop items
            for (Item item : items) { if (ores.contains(context.getQueriedLootTableId()) ) { generatedLoot.add(new ItemStack(item, drops)); } }

            for (ItemStack itemStack : generatedLoot) { // All modifications of ore's loot tables
                ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 2).build().apply(itemStack, context);
                ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE).build().apply(itemStack, context);
                ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE).build().apply(itemStack, context);
            }
        }
        for (LootItemCondition condition : this.conditions) { if (!condition.test(context)) { return generatedLoot; } } // Apply loot conditions, and add item on the list
        for (Item item : items) { generatedLoot.add(new ItemStack(item)); } // No enchantment add item on the list normally, update the data, and return the list
        return generatedLoot; // Return normal loot modifier even if to exist 1000 items
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() { return CODEC.get(); }
}