package net.karen.mccourse.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
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
import java.util.Map;
import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {
    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ForgeRegistries.ITEMS.getCodec().listOf()
                            .fieldOf("items").forGetter(m -> m.items)).apply(inst, AddItemModifier::new)));

    private final List<Item> items;

    public AddItemModifier(LootItemCondition[] conditionsIn, List<Item> items) {
        super(conditionsIn);
        this.items = items;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot,
                                                          LootContext context) {
        // Clear old's loot tables
        generatedLoot.clear();

        // All ores with loot table modified
        List<ResourceLocation> ores = Arrays.asList(Blocks.COAL_ORE.getLootTable(),
                Blocks.DEEPSLATE_COAL_ORE.getLootTable());

        ItemStack contextTool = context.getParamOrNull(LootContextParams.TOOL);

        if (contextTool != null) {
            // If mined ORES with Silk Touch's enchantment
            int silkTouch = contextTool.getEnchantmentLevel(Enchantments.SILK_TOUCH);
            if (silkTouch > 0) {
                // Loop through each item in the list and drop itself an item
                for (Item item : items) {
                    if (ores.contains(context.getQueriedLootTableId())) {
                        generatedLoot.add(new ItemStack(item));
                    }
                }
            }

            // If mined ORES with Fortune's enchantment
            int fortune = contextTool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE); // Fortune's enchantment level
            if (fortune > 0) {
                // Drops randomly
                int drops = context.getRandom().nextInt(fortune) +
                        UniformGenerator.between(1.0f, 2.0f).getInt(context);

                // Loop through each item in the list and drop items
                for (Item item : items) {
                    if (ores.contains(context.getQueriedLootTableId())) {
                        generatedLoot.add(new ItemStack(item, drops));
                    }
                }

                // All modifications of ore's loot tables
                Enchantment fortuneEnchantment = Enchantments.BLOCK_FORTUNE;
                for (ItemStack itemStack : generatedLoot) {
                    ApplyBonusCount.addUniformBonusCount(fortuneEnchantment, 2)
                            .build().apply(itemStack, context);
                    ApplyBonusCount.addOreBonusCount(fortuneEnchantment).build().apply(itemStack, context);
                    ApplyBonusCount.addUniformBonusCount(fortuneEnchantment).build().apply(itemStack, context);
                }
            }
        }

        // Apply loot conditions, and add item on the list
        for (LootItemCondition condition : this.conditions) {
            if (!condition.test(context)) {
                return generatedLoot;
            }
        }

        // No enchantment add item on the list normally, update the data, and return the list
        for (Item item : items) {
            ItemStack itemStack = new ItemStack(item);
            if (itemStack.is(ModItems.ORANGE_MODES.get())) {
                Map<Enchantment, Integer> enchantment = Map.of(Enchantments.BLOCK_EFFICIENCY, 10,
                        Enchantments.BLOCK_FORTUNE, 10, ModEnchantments.OVERPOWER_MENDING.get(), 1,
                        Enchantments.UNBREAKING, 10);
                for (Map.Entry<Enchantment, Integer> all : enchantment.entrySet()) {
                    itemStack.enchant(all.getKey(), all.getValue());
                }
                generatedLoot.add(itemStack);
            }
            else {
                generatedLoot.add(new ItemStack(item));
            }
        }

        // Return normal loot modifier even if to exist 1000 items
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() { return CODEC.get(); }
}