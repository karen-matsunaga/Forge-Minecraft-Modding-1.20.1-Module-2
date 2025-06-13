package net.karen.mccourse.datagen.loot;

import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.custom.*;
import net.karen.mccourse.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;
import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() { super(Set.of(), FeatureFlags.REGISTRY.allFlags()); }

    // Adding loot table's items and blocks
    @Override
    protected void generate() {
        // Blocks
        this.dropSelf(ModBlocks.ALEXANDRITE_BLOCK.get());
        this.dropSelf(ModBlocks.RAW_ALEXANDRITE_BLOCK.get());
        this.dropSelf(ModBlocks.SOUND_BLOCK.get());

        // Ores
        this.add(ModBlocks.ALEXANDRITE_ORE.get(),
                block -> createOreDrop(ModBlocks.ALEXANDRITE_ORE.get(), ModItems.RAW_ALEXANDRITE.get()));

        this.add(ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(),
                block -> createOreDrop(ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get(), ModItems.RAW_ALEXANDRITE.get()));

        this.add(ModBlocks.END_STONE_ALEXANDRITE_ORE.get(),
                block -> createOreDrop(ModBlocks.END_STONE_ALEXANDRITE_ORE.get(), ModItems.RAW_ALEXANDRITE.get()));

        this.add(ModBlocks.NETHER_ALEXANDRITE_ORE.get(),
                block -> createOreDrop(ModBlocks.NETHER_ALEXANDRITE_ORE.get(), ModItems.RAW_ALEXANDRITE.get()));

        // Stairs
        this.dropSelf(ModBlocks.ALEXANDRITE_STAIRS.get());

        // Slabs - Drops are different because to put twice blocks
        this.add(ModBlocks.ALEXANDRITE_SLABS.get(), block -> createSlabItemTable(ModBlocks.ALEXANDRITE_SLABS.get()));

        // Pressure Plate block
        this.dropSelf(ModBlocks.ALEXANDRITE_PREASSURE_PLATE.get());

        // Button block
        this.dropSelf(ModBlocks.ALEXANDRITE_BUTTON.get());

        // Fence and fence gate block
        this.dropSelf(ModBlocks.ALEXANDRITE_FENCE.get());
        this.dropSelf(ModBlocks.ALEXANDRITE_FENCE_GATE.get());

        // Wall block
        this.dropSelf(ModBlocks.ALEXANDRITE_WALL.get());

        // Door block
        this.add(ModBlocks.ALEXANDRITE_DOOR.get(), block -> createDoorTable(ModBlocks.ALEXANDRITE_DOOR.get()));

        // Trapdoor block
        this.dropSelf(ModBlocks.ALEXANDRITE_TRAPDOOR.get());

        // Custom lamp block
        this.dropSelf(ModBlocks.ALEXANDRITE_LAMP.get());

        // Kohlrabi's seeds drop
        LootItemCondition.Builder $$1 = LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.KOHLRABI_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(KohlrabiCropBlock.AGE, 6));
        this.add(ModBlocks.KOHLRABI_CROP.get(), this.createCropDrops(ModBlocks.KOHLRABI_CROP.get(),
                ModItems.KOHLRABI.get(), ModItems.KOHLRABI_SEEDS.get(), $$1));

        // Snapdragon's flower and pot flower
        this.dropSelf(ModBlocks.SNAPDRAGON.get());
        this.add(ModBlocks.POTTED_SNAPDRAGON.get(), createPotFlowerItemTable(ModBlocks.POTTED_SNAPDRAGON.get()));

        // Gem Empowering Station's custom block model
        this.dropSelf(ModBlocks.GEM_EMPOWERING_STATION.get());

        // Walnut's custom wood
        this.dropSelf(ModBlocks.WALNUT_LOG.get());
        this.dropSelf(ModBlocks.WALNUT_WOOD.get());
        this.dropSelf(ModBlocks.STRIPPED_WALNUT_LOG.get());
        this.dropSelf(ModBlocks.STRIPPED_WALNUT_WOOD.get());
        this.dropSelf(ModBlocks.WALNUT_PLANKS.get());
        this.dropSelf(ModBlocks.WALNUT_SAPLING.get());

        this.add(ModBlocks.WALNUT_LEAVES.get(), block ->
                createLeavesDrops(block, ModBlocks.WALNUT_SAPLING.get(), NORMAL_LEAVES_SAPLING_CHANCES));

        // Walnut's custom sign
        this.add(ModBlocks.WALNUT_SIGN.get(), block -> createSingleItemTable(ModItems.WALNUT_SIGN.get()));
        this.add(ModBlocks.WALNUT_WALL_SIGN.get(), block -> createSingleItemTable(ModItems.WALNUT_SIGN.get()));
        this.add(ModBlocks.WALNUT_HANGING_SIGN.get(), block -> createSingleItemTable(ModItems.WALNUT_HANGING_SIGN.get()));
        this.add(ModBlocks.WALNUT_WALL_HANGING_SIGN.get(), block -> createSingleItemTable(ModItems.WALNUT_HANGING_SIGN.get()));

        // Colored custom block
        this.dropSelf(ModBlocks.COLORED_LEAVES.get());

        // Cattail custom crop
        // THIS IF ONLY TOP BLOCK SHOULD DROP SOMETHING
        //LootItemCondition.Builder lootitemcondition$builder2 =
        // LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.CATTAIL_CROP.get())
        //        .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CattailCropBlock.AGE, 8));
        //this.add(ModBlocks.CATTAIL_CROP.get(), this.createCropDrops(ModBlocks.CATTAIL_CROP.get(),
        //        ModItems.CATTAIL.get(), ModItems.CATTAIL_SEEDS.get(), lootitemcondition$builder2));

        LootItemCondition.Builder lootitemcondition$builder2 = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.CATTAIL_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CattailCropBlock.AGE, 7))
                .or(LootItemBlockStatePropertyCondition.hasBlockStateProperties(ModBlocks.CATTAIL_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CattailCropBlock.AGE, 8)));

        this.add(ModBlocks.CATTAIL_CROP.get(), createCropDrops(ModBlocks.CATTAIL_CROP.get(), ModItems.CATTAIL.get(),
                ModItems.CATTAIL_SEEDS.get(), lootitemcondition$builder2));

        // Ruby custom oxidizable block
        this.dropSelf(ModBlocks.RUBY_BLOCK.get());
        this.dropSelf(ModBlocks.RUBY_BLOCK_1.get());
        this.dropSelf(ModBlocks.RUBY_BLOCK_2.get());
        this.dropSelf(ModBlocks.RUBY_BLOCK_3.get());

        this.dropSelf(ModBlocks.WAXED_RUBY_BLOCK.get());
        this.dropSelf(ModBlocks.WAXED_RUBY_BLOCK_1.get());
        this.dropSelf(ModBlocks.WAXED_RUBY_BLOCK_2.get());
        this.dropSelf(ModBlocks.WAXED_RUBY_BLOCK_3.get());

        // Kaupen custom furnace
        this.dropSelf(ModBlocks.KAUPEN_FURNACE_BLOCK.get());

        // My custom block
        this.dropSelf(ModBlocks.ENDER_PEARL_BLOCK.get());

        this.dropSelf(ModBlocks.GREEN_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.BLACK_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.MAGENTA_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.PURPLE_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.ORANGE_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.PINK_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.CYAN_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.BROWN_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.GRAY_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.RED_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.LIME_GREEN_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.YELLOW_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.BLUE_ENDER_PEARL_BLOCK.get());
        this.dropSelf(ModBlocks.WHITE_ENDER_PEARL_BLOCK.get());

        this.dropSelf(ModBlocks.NETHER_STAR_BLOCK.get());
        this.dropSelf(ModBlocks.GUNPOWDER_BLOCK.get());
        this.dropSelf(ModBlocks.ROTTEN_FLESH_BLOCK.get());
        this.dropSelf(ModBlocks.BLAZE_ROD_BLOCK.get());
        this.dropSelf(ModBlocks.PHANTOM_MEMBRANE_BLOCK.get());

        // My custom ores
        this.dropSelf(ModBlocks.PINK_BLOCK.get());
        this.add(ModBlocks.PINK_ORE.get(), block -> createOreDrop(ModBlocks.PINK_ORE.get(), ModItems.PINK.get()));
        this.add(ModBlocks.DEEPSLATE_PINK_ORE.get(), block -> createOreDrop(ModBlocks.DEEPSLATE_PINK_ORE.get(), ModItems.PINK.get()));
        this.add(ModBlocks.END_STONE_PINK_ORE.get(), block -> createOreDrop(ModBlocks.END_STONE_PINK_ORE.get(), ModItems.PINK.get()));
        this.add(ModBlocks.NETHER_PINK_ORE.get(), block -> createOreDrop(ModBlocks.NETHER_PINK_ORE.get(), ModItems.PINK.get()));

        // My disenchanted block
        this.dropSelf(ModBlocks.DISENCHANTED_BLOCK.get());

        // My CRAFTING TABLE block
        this.dropSelf(ModBlocks.CRAFT_CRAFTING_TABLE.get());
        this.dropSelf(ModBlocks.MCCOURSE_GENERATOR.get());
        this.dropSelf(ModBlocks.MCCOURSE_ELEVATOR.get());
        this.dropSelf(ModBlocks.MAGIC_ENCHANTED_BLOCK.get());
        this.dropSelf(ModBlocks.MAGIC_DISENCHANTED_BLOCK.get());
    }

    // Custom ore's drops
    protected LootTable.@NotNull Builder createOreDrop(@NotNull Block block, @NotNull Item item) {
        return createSilkTouchDispatchTable(block, this.applyExplosionDecay(block,
                LootItem.lootTableItem(item).apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0f, 5.0f)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                        .apply(ApplyBonusCount.addUniformBonusCount(Enchantments.BLOCK_FORTUNE, 1))));
    }

    // Return all registries in deferred registry
    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}