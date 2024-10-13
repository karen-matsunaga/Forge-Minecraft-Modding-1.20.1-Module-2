package net.karen.mccourse.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class AddItemModifier extends LootModifier {

    public static final Supplier<Codec<AddItemModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(inst -> codecStart(inst)
                    .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
                    .apply(inst, AddItemModifier::new))
    );

    private final Item item;

    public AddItemModifier(LootItemCondition[] conditionsIn, Item item) {
        super(conditionsIn);
        this.item = item;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Get the tool used by the player
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);

        // Check for Silk Touch enchantment
        if (tool != null && tool.getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
            // If Silk Touch is present, return the block itself
            if (context.getQueriedLootTableId().equals(Blocks.DIAMOND_ORE.getLootTable())) {
                generatedLoot.add(new ItemStack(Items.DIAMOND, 1)); // Drop the diamond block and a diamond
                return generatedLoot;
            }
        }

        // Check for Fortune enchantment
        if (tool != null && tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE) > 0) {
            // Fortune's enchantment level
            int fortuneLevel = tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
            // Drops diamond ores randomly
            RandomSource random = context.getRandom();

            // Example: Modify diamond ore drops with Fortune
            if (context.getQueriedLootTableId().equals(Blocks.DIAMOND_ORE.getLootTable())) {
                int baseDrop = 1;
                int count = 1 + random.nextInt(fortuneLevel + 1); // Calculate extra drops based on Fortune level
                int totalDiamonds = baseDrop + count;  // Add the item with increased count
                generatedLoot.add(new ItemStack(this.item, totalDiamonds)); // Drop the diamond ores and some diamonds
                return generatedLoot;
            }
        }

        // No Silk Touch or Fortune - add the item normally and default is 1
        generatedLoot.add(new ItemStack(this.item, 1));
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}
