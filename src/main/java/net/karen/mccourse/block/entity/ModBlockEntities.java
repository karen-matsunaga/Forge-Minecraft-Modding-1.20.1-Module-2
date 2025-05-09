package net.karen.mccourse.block.entity;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MCCourseMod.MOD_ID);

    // Register all custom block entities
    public static final RegistryObject<BlockEntityType<GemEmpoweringStationBlockEntity>> GEM_EMPOWERING_STATION_BE =
            BLOCK_ENTITIES.register("gem_empowering_station_block_entity", () ->
                    BlockEntityType.Builder.of(GemEmpoweringStationBlockEntity::new,
                            ModBlocks.GEM_EMPOWERING_STATION.get()).build(null));

    // Register all custom sign
    public static final RegistryObject<BlockEntityType<ModSignBlockEntity>> MOD_SIGN =
            BLOCK_ENTITIES.register("mod_sign", () ->
                    BlockEntityType.Builder.of(ModSignBlockEntity::new,
                            ModBlocks.WALNUT_SIGN.get(), ModBlocks.WALNUT_WALL_SIGN.get()).build(null));

    public static final RegistryObject<BlockEntityType<ModHangingSignBlockEntity>> MOD_HANGING_SIGN =
            BLOCK_ENTITIES.register("mod_hanging_sign", () ->
                    BlockEntityType.Builder.of(ModHangingSignBlockEntity::new,
                            ModBlocks.WALNUT_HANGING_SIGN.get(), ModBlocks.WALNUT_WALL_HANGING_SIGN.get()).build(null));

    // Register all custom furnace
    public static final RegistryObject<BlockEntityType<KaupenFurnaceBlockEntity>> KAUPEN_FURNACE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("kaupen_furnace_block_entity", () ->
                    BlockEntityType.Builder.of(KaupenFurnaceBlockEntity::new,
                            ModBlocks.KAUPEN_FURNACE_BLOCK.get()).build(null));

    // Register all custom enchant
    public static final RegistryObject<BlockEntityType<DisenchantedBlockEntity>> DISENCHANTED_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("disenchanted_block_entity", () ->
                    BlockEntityType.Builder.of(DisenchantedBlockEntity::new,
                            ModBlocks.DISENCHANTED_BLOCK.get()).build(null)); // Disenchanted

    // Register all custom crafting table
    public static final RegistryObject<BlockEntityType<CraftCraftingTableBlockEntity>> CRAFT_CRAFTING_TABLE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("craft_crafting_table_block_entity", () ->
                    BlockEntityType.Builder.of(CraftCraftingTableBlockEntity::new,
                            ModBlocks.CRAFT_CRAFTING_TABLE.get()).build(null));

    // Register all block entities on Forge
    public static void register(IEventBus eventBus) { BLOCK_ENTITIES.register(eventBus); }
}