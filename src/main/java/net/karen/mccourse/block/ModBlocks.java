package net.karen.mccourse.block;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.custom.*;
import net.karen.mccourse.fluid.ModFluids;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.sound.ModSounds;
import net.karen.mccourse.util.ModWoodTypes;
import net.karen.mccourse.worldgen.tree.WalnutTreeGrower;
import net.minecraft.core.*;
import net.minecraft.sounds.*;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.effect.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, MCCourseMod.MOD_ID);

    // Adding custom blocks
    // Blocks - First block
    public static final RegistryObject<Block> ALEXANDRITE_BLOCK = registerBlock("alexandrite_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    // Second block
    public static final RegistryObject<Block> RAW_ALEXANDRITE_BLOCK = registerBlock("raw_alexandrite_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    // Ores - Third block
    public static final RegistryObject<Block> ALEXANDRITE_ORE = registerBlock("alexandrite_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(2, 5)));

    // Fourth block
    public static final RegistryObject<Block> DEEPSLATE_ALEXANDRITE_ORE = registerBlock("deepslate_alexandrite_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(3, 7)));

    // Fifth block
    public static final RegistryObject<Block> END_STONE_ALEXANDRITE_ORE = registerBlock("end_stone_alexandrite_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(5, 8)));

    // Sixth block
    public static final RegistryObject<Block> NETHER_ALEXANDRITE_ORE = registerBlock("nether_alexandrite_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.NETHERRACK)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(3, 6)));

    // Custom Advanced Block - Seventh block
    public static final RegistryObject<Block> SOUND_BLOCK = registerBlock("sound_block",
            () -> new SoundBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    // Stair's block
    public static final RegistryObject<Block> ALEXANDRITE_STAIRS = registerBlock("alexandrite_stairs",
            () -> new StairBlock(() -> ModBlocks.ALEXANDRITE_BLOCK.get().defaultBlockState(),
                    BlockBehaviour.Properties.copy(Blocks.GRANITE_STAIRS).sound(SoundType.METAL)));

    // Slab's block
    public static final RegistryObject<Block> ALEXANDRITE_SLABS = registerBlock("alexandrite_slabs",
            () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.GRANITE_SLAB).sound(SoundType.METAL)));

    // Pressure Plate
    public static final RegistryObject<Block> ALEXANDRITE_PREASSURE_PLATE = registerBlock("alexandrite_pressure_plate",
            () -> new PressurePlateBlock(PressurePlateBlock.Sensitivity.EVERYTHING,
                    BlockBehaviour.Properties.copy(Blocks.GRANITE_STAIRS).sound(SoundType.METAL), BlockSetType.IRON));

    // Button block
    public static final RegistryObject<Block> ALEXANDRITE_BUTTON = registerBlock("alexandrite_button",
            () -> new ButtonBlock(BlockBehaviour.Properties.copy(Blocks.GRANITE_SLAB).sound(SoundType.METAL),
                    BlockSetType.IRON, 10, true));

    // Fence block
    public static final RegistryObject<Block> ALEXANDRITE_FENCE = registerBlock("alexandrite_fence",
            () -> new FenceBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    // Fence gate block
    public static final RegistryObject<Block> ALEXANDRITE_FENCE_GATE = registerBlock("alexandrite_fence_gate",
            () -> new FenceGateBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK), SoundEvents.FENCE_GATE_OPEN,
                    SoundEvents.FENCE_GATE_CLOSE));

    // Wall block
    public static final RegistryObject<Block> ALEXANDRITE_WALL = registerBlock("alexandrite_wall",
            () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK)));

    // Door block
    public static final RegistryObject<Block> ALEXANDRITE_DOOR = registerBlock("alexandrite_door",
            () -> new DoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), BlockSetType.IRON));

    // Trapdoor block
    public static final RegistryObject<Block> ALEXANDRITE_TRAPDOOR = registerBlock("alexandrite_trapdoor",
            () -> new TrapDoorBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion(), BlockSetType.IRON));

    // Alexandrite's custom lamp and custom sound
    public static final RegistryObject<Block> ALEXANDRITE_LAMP = registerBlock("alexandrite_lamp",
            () -> new AlexandriteLampBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE)
                    .sound(ModSounds.ALEXANDRITE_LAMP_SOUNDS)
                    .strength(1f)
                    .lightLevel(state -> state.getValue(AlexandriteLampBlock.CLICKED) ? 15 : 0)));

    // Kohlrabi's crop block
    public static final RegistryObject<Block> KOHLRABI_CROP = BLOCKS.register("kohlrabi_crop",
            () -> new KohlrabiCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT).noCollission().noOcclusion()));

    // Snapdragon's flower
    public static final RegistryObject<Block> SNAPDRAGON = registerBlock("snapdragon",
            () -> new FlowerBlock(() -> MobEffects.BLINDNESS, 6, BlockBehaviour.Properties.copy(Blocks.ALLIUM)));

    // Snapdragon's pot flower
    public static final RegistryObject<Block> POTTED_SNAPDRAGON = registerBlock("potted_snapdragon",
            () -> new FlowerPotBlock((() -> (FlowerPotBlock) Blocks.FLOWER_POT), SNAPDRAGON,
                    BlockBehaviour.Properties.copy(Blocks.POTTED_ALLIUM)));

    // Gem Empowering Station's custom block model
    public static final RegistryObject<Block> GEM_EMPOWERING_STATION = registerBlock("gem_empowering_station",
            () -> new GemEmpoweringStationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    // Soap Water Block custom fluid
    public static final RegistryObject<LiquidBlock> SOAP_WATER_BLOCK = BLOCKS.register("soap_water_block",
            () -> new LiquidBlock(ModFluids.SOURCE_SOAP_WATER, BlockBehaviour.Properties.copy(Blocks.WATER).noLootTable()));

    // Walnut custom wood
    public static final RegistryObject<Block> WALNUT_LOG = registerBlock("walnut_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LOG)));

    public static final RegistryObject<Block> WALNUT_WOOD = registerBlock("walnut_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WOOD)));

    public static final RegistryObject<Block> STRIPPED_WALNUT_LOG = registerBlock("stripped_walnut_log",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_LOG)));

    public static final RegistryObject<Block> STRIPPED_WALNUT_WOOD = registerBlock("stripped_walnut_wood",
            () -> new ModFlammableRotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.STRIPPED_OAK_WOOD)));

    public static final RegistryObject<Block> WALNUT_PLANKS = registerBlock("walnut_planks",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return true; }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 20; }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 5; }
            });

    public static final RegistryObject<Block> WALNUT_LEAVES = registerBlock("walnut_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)){
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return true; }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 60; }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 30; }
            });

    public static final RegistryObject<Block> WALNUT_SAPLING = registerBlock("walnut_sapling",
            () -> new ModSaplingBlock(new WalnutTreeGrower(), BlockBehaviour.Properties.copy(Blocks.OAK_SAPLING)));

    // Custom sign and hanging sign
    public static final RegistryObject<Block> WALNUT_SIGN = BLOCKS.register("walnut_sign",
            () -> new ModStandingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_SIGN), ModWoodTypes.WALNUT));

    public static final RegistryObject<Block> WALNUT_WALL_SIGN = BLOCKS.register("walnut_wall_sign",
            () -> new ModWallSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_SIGN), ModWoodTypes.WALNUT));

    public static final RegistryObject<Block> WALNUT_HANGING_SIGN = BLOCKS.register("walnut_hanging_sign",
            () -> new ModHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_HANGING_SIGN), ModWoodTypes.WALNUT));

    public static final RegistryObject<Block> WALNUT_WALL_HANGING_SIGN = BLOCKS.register("walnut_wall_hanging_sign",
            () -> new ModWallHangingSignBlock(BlockBehaviour.Properties.copy(Blocks.OAK_WALL_HANGING_SIGN), ModWoodTypes.WALNUT));

    // Dice custom block
    public static final RegistryObject<Block> DICE_BLOCK = BLOCKS.register("dice_block",
            () -> new DiceBlock(BlockBehaviour.Properties.copy(Blocks.STONE).noLootTable()));

    // Colored custom block
    public static final RegistryObject<Block> COLORED_LEAVES = registerBlock("colored_leaves",
            () -> new LeavesBlock(BlockBehaviour.Properties.copy(Blocks.OAK_LEAVES)) {
                @Override
                public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return true; }

                @Override
                public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 60; }

                @Override
                public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) { return 30; }
            });

    // Cattail custom crop block
    public static final RegistryObject<Block> CATTAIL_CROP = BLOCKS.register("cattail_crop",
            () -> new CattailCropBlock(BlockBehaviour.Properties.copy(Blocks.WHEAT)));

    // Kaupen custom portal block
    public static final RegistryObject<Block> KAUPEN_PORTAL = registerBlock("kaupen_portal",
            () -> new KaupenPortalBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_PORTAL)
                    .noLootTable().noOcclusion().noCollission()));

    // Ruby custom oxidizable block
    public static final RegistryObject<Block> RUBY_BLOCK = registerBlock("ruby_block",
            () -> new DegradableRubyBlock(GemDegradable.GemDegradationLevel.UNAFFECTED,
                    BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> RUBY_BLOCK_1 = registerBlock("ruby_block_1",
            () -> new DegradableRubyBlock(GemDegradable.GemDegradationLevel.EXPOSED,
                    BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> RUBY_BLOCK_2 = registerBlock("ruby_block_2",
            () -> new DegradableRubyBlock(GemDegradable.GemDegradationLevel.WEATHERED,
                    BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> RUBY_BLOCK_3 = registerBlock("ruby_block_3",
            () -> new DegradableRubyBlock(GemDegradable.GemDegradationLevel.DEGRADED,
                    BlockBehaviour.Properties.copy(Blocks.STONE)));

    public static final RegistryObject<Block> WAXED_RUBY_BLOCK = registerBlock("waxed_ruby_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> WAXED_RUBY_BLOCK_1 = registerBlock("waxed_ruby_block_1",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> WAXED_RUBY_BLOCK_2 = registerBlock("waxed_ruby_block_2",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));
    public static final RegistryObject<Block> WAXED_RUBY_BLOCK_3 = registerBlock("waxed_ruby_block_3",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.STONE)));

    // Kaupen custom furnace
    public static final RegistryObject<Block> KAUPEN_FURNACE_BLOCK = registerBlock("kaupen_furnace",
            () -> new KaupenFurnaceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)));

    // Custom block
    public static final RegistryObject<Block> ENDER_PEARL_BLOCK = registerBlock("ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> GREEN_ENDER_PEARL_BLOCK = registerBlock("green_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GREEN).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> LIME_GREEN_ENDER_PEARL_BLOCK = registerBlock("lime_green_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> BLACK_ENDER_PEARL_BLOCK = registerBlock("black_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLACK).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> MAGENTA_ENDER_PEARL_BLOCK = registerBlock("magenta_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_MAGENTA).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> PURPLE_ENDER_PEARL_BLOCK = registerBlock("purple_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> ORANGE_ENDER_PEARL_BLOCK = registerBlock("orange_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> PINK_ENDER_PEARL_BLOCK = registerBlock("pink_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> CYAN_ENDER_PEARL_BLOCK = registerBlock("cyan_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_CYAN).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> BROWN_ENDER_PEARL_BLOCK = registerBlock("brown_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> GRAY_ENDER_PEARL_BLOCK = registerBlock("gray_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_GRAY).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> RED_ENDER_PEARL_BLOCK = registerBlock("red_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_RED).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> YELLOW_ENDER_PEARL_BLOCK = registerBlock("yellow_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> BLUE_ENDER_PEARL_BLOCK = registerBlock("blue_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BLUE).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    public static final RegistryObject<Block> WHITE_ENDER_PEARL_BLOCK = registerBlock("white_ender_pearl_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_GRAY).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops()
                    .strength(5.0F, 6.0F)
                    .sound(SoundType.METAL)
                    .lightLevel(state -> 50) // Light block
            ));

    // Custom Mobs blocks
    public static final RegistryObject<Block> NETHER_STAR_BLOCK = registerBlock("nether_star_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.BELL)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> GUNPOWDER_BLOCK = registerBlock("gunpowder_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.CREEPER)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> ROTTEN_FLESH_BLOCK = registerBlock("rotten_flesh_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.ZOMBIE)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> BLAZE_ROD_BLOCK = registerBlock("blaze_rod_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.BIT)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> PHANTOM_MEMBRANE_BLOCK = registerBlock("phantom_membrane_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.BIT)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    // My custom ores
    public static final RegistryObject<Block> PINK_BLOCK = registerBlock("pink_block",
            () -> new Block(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).instrument(NoteBlockInstrument.IRON_XYLOPHONE)
                    .requiresCorrectToolForDrops().strength(5.0F, 6.0F).sound(SoundType.METAL)));

    public static final RegistryObject<Block> PINK_ORE = registerBlock("pink_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(2, 5)));

    public static final RegistryObject<Block> DEEPSLATE_PINK_ORE = registerBlock("deepslate_pink_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(3, 7)));

    public static final RegistryObject<Block> END_STONE_PINK_ORE = registerBlock("end_stone_pink_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.END_STONE)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(5, 8)));

    public static final RegistryObject<Block> NETHER_PINK_ORE = registerBlock("nether_pink_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.NETHERRACK)
                    .strength(5f).requiresCorrectToolForDrops(), UniformInt.of(3, 6)));

    // Disenchanted custom disenchanted block
    public static final RegistryObject<Block> DISENCHANTED_BLOCK = registerBlock("disenchanted",
            () -> new DisenchantedBlock(BlockBehaviour.Properties.of().sound(SoundType.METAL)
                    .strength(5f)
                    .requiresCorrectToolForDrops()));

    // Craft custom Crafting Table
    public static final RegistryObject<Block> CRAFT_CRAFTING_TABLE = registerBlock("craft_crafting_table",
            () -> new CraftCraftingTableBlock(BlockBehaviour.Properties.copy(Blocks.CRAFTING_TABLE)));

    // Mccourse Generator custom generator item
    public static final RegistryObject<Block> MCCOURSE_GENERATOR = registerBlock("mccourse_generator",
            () -> new MccourseGeneratorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.STONE)
                    .instrument(NoteBlockInstrument.BASEDRUM)
                    .strength(5F, 3600000.0F),
                    List.of(Items.DIAMOND, Items.ENCHANTED_BOOK)));

    // Elevator block
    public static final RegistryObject<Block> MCCOURSE_ELEVATOR = registerBlock("mccourse_elevator",
            () -> new MccourseElevatorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOL)
                    .sound(SoundType.WOOL)
                    .strength(5F, 1200.F)));

    // Magic block
    public static final RegistryObject<Block> MAGIC_BLOCK = registerBlock("magic",
            () -> new MagicBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> 15)
                    .sound(SoundType.LARGE_AMETHYST_BUD)
                    .strength(5F, 3600000.0F)));

    // Magic Book block
    public static final RegistryObject<Block> MAGIC_BOOK_BLOCK = registerBlock("magic_book",
            () -> new MagicBookBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> 15)
                    .sound(SoundType.ANVIL)
                    .strength(5F, 3600000.0F)));

    // Book Disenchanted block
    public static final RegistryObject<Block> BOOK_DISENCHANTED_BLOCK = registerBlock("book_disenchanted_block",
            () -> new BookDisenchantedBlock(BlockBehaviour.Properties.of()
                    .lightLevel(state -> 15)
                    .sound(SoundType.ANVIL)
                    .strength(5F, 3600000.0F)));

    // Register all custom blocks in the game
    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    // Register block as item
    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    // Register all blocks in game
    public static void register(IEventBus eventBus) { BLOCKS.register(eventBus); }
}