package net.karen.mccourse.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
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
                    .and(ForgeRegistries.ITEMS.getCodec().listOf().fieldOf("items").forGetter(m -> m.items))
                    .apply(inst, AddItemModifier::new)));

    private final List<Item> items;

    public AddItemModifier(LootItemCondition[] conditionsIn, List<Item> items) {
        super(conditionsIn);
        this.items = items;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.clear();
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);  // Get the tool used by the player

        if (tool != null && tool.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) { // Check for Silk Touch enchantment
            for (Item item : items) {  // Loop through each item in the list
                if (context.getQueriedLootTableId().equals(Blocks.DIAMOND_ORE.getLootTable())) { // If mined DIAMOND ORE
                        generatedLoot.add(new ItemStack(item));
                } else if (context.getQueriedLootTableId().equals(Blocks.ANCIENT_DEBRIS.getLootTable())) { // If mined ANCIENT DEBRIS
                        generatedLoot.add(new ItemStack(item));
                }
            }
        }

        // Check for Fortune enchantment
        if (tool != null && tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE) > 0) {
            int fortuneLevel = tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE); // Fortune's enchantment level
            int drops = context.getRandom().nextInt(fortuneLevel) + UniformGenerator.between(1.0f, 2.0f).getInt(context); // Drops randomly

            for (Item item : items) {  // Loop through each item in the list
                if (context.getQueriedLootTableId().equals(Blocks.DIAMOND_ORE.getLootTable())) { // If mined DIAMOND ORE
                        generatedLoot.add(new ItemStack(item, drops)); // Drop multiple diamond's ores and diamonds
                } else if (context.getQueriedLootTableId().equals(Blocks.ANCIENT_DEBRIS.getLootTable())) { // If mined ANCIENT DEBRIS
                        generatedLoot.add(new ItemStack(item, drops)); // Drop multiple ancient debris's ores and netherite scraps
                }
            }
        }

        // Apply loot conditions and add each item in the list
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }

        // No Silk Touch or Fortune - add each item in the list normally and update the data and return the list
        for (Item item : items) {
            generatedLoot.add(new ItemStack(item));
        }

        // Return normal loot modifier even if to exist 1000 items
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
