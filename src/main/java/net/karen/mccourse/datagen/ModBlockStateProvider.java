package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.custom.AlexandriteLampBlock;
import net.karen.mccourse.block.custom.CattailCropBlock;
import net.karen.mccourse.block.custom.KohlrabiCropBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;
import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) { super(output, MCCourseMod.MOD_ID, exFileHelper); }

    // Registry all blocks - JSON file is created automatically
    @Override
    protected void registerStatesAndModels() {
        // Blocks
        blockWithItem(ModBlocks.ALEXANDRITE_BLOCK);
        blockWithItem(ModBlocks.RAW_ALEXANDRITE_BLOCK);

        // Ores
        blockWithItem(ModBlocks.ALEXANDRITE_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_ALEXANDRITE_ORE);
        blockWithItem(ModBlocks.END_STONE_ALEXANDRITE_ORE);
        blockWithItem(ModBlocks.NETHER_ALEXANDRITE_ORE);

        // Custom Advanced Block
        blockWithItem(ModBlocks.SOUND_BLOCK);

        // Stair's block - Alexandrite Stairs having Alexandrite Block's texture associated
        stairsBlock((StairBlock) ModBlocks.ALEXANDRITE_STAIRS.get(), blockTexture(ModBlocks.ALEXANDRITE_BLOCK.get()));

        // Slab's block - Alexandrite Slabs having Alexandrite Block's texture associated
        slabBlock((SlabBlock) ModBlocks.ALEXANDRITE_SLABS.get(), blockTexture(ModBlocks.ALEXANDRITE_BLOCK.get()), blockTexture(ModBlocks.ALEXANDRITE_BLOCK.get()));

        // Stairs
        blockItem(ModBlocks.ALEXANDRITE_STAIRS);

        // Slab
        blockItem(ModBlocks.ALEXANDRITE_SLABS);

        // Pressure Plate
        pressurePlateBlock((PressurePlateBlock) ModBlocks.ALEXANDRITE_PREASSURE_PLATE.get(), blockTexture(ModBlocks.ALEXANDRITE_BLOCK.get()));

        // Button
        buttonBlock((ButtonBlock) ModBlocks.ALEXANDRITE_BUTTON.get(), blockTexture(ModBlocks.ALEXANDRITE_BLOCK.get()));

        // Pressure Plate
        blockItem(ModBlocks.ALEXANDRITE_PREASSURE_PLATE);

        // Button
        blockItem(ModBlocks.ALEXANDRITE_BUTTON);

        // Fence and fence gate
        fenceBlock((FenceBlock) ModBlocks.ALEXANDRITE_FENCE.get(), blockTexture(ModBlocks.ALEXANDRITE_BLOCK.get()));
        fenceGateBlock((FenceGateBlock) ModBlocks.ALEXANDRITE_FENCE_GATE.get(), blockTexture(ModBlocks.ALEXANDRITE_BLOCK.get()));

        // Wall
        wallBlock((WallBlock) ModBlocks.ALEXANDRITE_WALL.get(), blockTexture(ModBlocks.ALEXANDRITE_BLOCK.get()));

        // Fence
        blockItem(ModBlocks.ALEXANDRITE_FENCE_GATE);

        // Door - Two halves of door (bottom and top)
        doorBlockWithRenderType((DoorBlock) ModBlocks.ALEXANDRITE_DOOR.get(), modLoc("block/alexandrite_door_bottom"), modLoc("block/alexandrite_door_top"), "cutout");

        // Trapdoor
        trapdoorBlockWithRenderType((TrapDoorBlock) ModBlocks.ALEXANDRITE_TRAPDOOR.get(), modLoc("block/alexandrite_trapdoor"), true, "cutout");

        // Trapdoor
        blockItem(ModBlocks.ALEXANDRITE_TRAPDOOR, "_bottom");

        // Custom lamp
        customLamp();

        // Kohlrabi's stages texture
        makeCrop(((KohlrabiCropBlock) ModBlocks.KOHLRABI_CROP.get()), "kohlrabi_stage", "kohlrabi_stage");


        // Snapdragon's flower
        simpleBlock(ModBlocks.SNAPDRAGON.get(),
                models().cross(blockTexture(ModBlocks.SNAPDRAGON.get()).getPath(), blockTexture(ModBlocks.SNAPDRAGON.get())).renderType("cutout"));

        // Gem Empowering Station's custom block model - Created JSON file automatically on model folder
        horizontalBlock(ModBlocks.GEM_EMPOWERING_STATION.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/gem_empowering_station")));

        // Walnut's custom wood
        logBlock(((RotatedPillarBlock) ModBlocks.WALNUT_LOG.get()));
        axisBlock(((RotatedPillarBlock) ModBlocks.WALNUT_WOOD.get()), blockTexture(ModBlocks.WALNUT_LOG.get()), blockTexture(ModBlocks.WALNUT_LOG.get()));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_WALNUT_LOG.get(), new ResourceLocation(MCCourseMod.MOD_ID, "block/stripped_walnut_log"),
                new ResourceLocation(MCCourseMod.MOD_ID, "block/stripped_walnut_log_top"));
        axisBlock((RotatedPillarBlock) ModBlocks.STRIPPED_WALNUT_WOOD.get(), new ResourceLocation(MCCourseMod.MOD_ID, "block/stripped_walnut_log"),
                new ResourceLocation(MCCourseMod.MOD_ID, "block/stripped_walnut_log"));

        blockItem(ModBlocks.WALNUT_LOG);
        blockItem(ModBlocks.WALNUT_WOOD);
        blockItem(ModBlocks.STRIPPED_WALNUT_LOG);
        blockItem(ModBlocks.STRIPPED_WALNUT_WOOD);

        blockWithItem(ModBlocks.WALNUT_PLANKS);

        leavesBlock(ModBlocks.WALNUT_LEAVES);
        saplingBlock(ModBlocks.WALNUT_SAPLING);

        // Walnut's custom sign
        signBlock(((StandingSignBlock) ModBlocks.WALNUT_SIGN.get()), ((WallSignBlock) ModBlocks.WALNUT_WALL_SIGN.get()),
                blockTexture(ModBlocks.WALNUT_PLANKS.get()));

        hangingSignBlock(ModBlocks.WALNUT_HANGING_SIGN.get(), ModBlocks.WALNUT_WALL_HANGING_SIGN.get(),
                blockTexture(ModBlocks.WALNUT_PLANKS.get()));

        // Colored custom block
        leavesBlock(ModBlocks.COLORED_LEAVES);

        // Cattail custom crop
        makeCattailCrop(((CattailCropBlock) ModBlocks.CATTAIL_CROP.get()), "cat_tail_stage", "cat_tail_stage");

        // Kaupen custom portal
        blockWithItem(ModBlocks.KAUPEN_PORTAL);

        // Ruby custom oxidizable block
        blockWithItem(ModBlocks.RUBY_BLOCK);
        blockWithItem(ModBlocks.RUBY_BLOCK_1);
        blockWithItem(ModBlocks.RUBY_BLOCK_2);
        blockWithItem(ModBlocks.RUBY_BLOCK_3);

        blockWithItem(ModBlocks.WAXED_RUBY_BLOCK);
        blockWithItem(ModBlocks.WAXED_RUBY_BLOCK_1);
        blockWithItem(ModBlocks.WAXED_RUBY_BLOCK_2);
        blockWithItem(ModBlocks.WAXED_RUBY_BLOCK_3);

        // My custom block
        blockWithItem(ModBlocks.ENDER_PEARL_BLOCK);

        blockWithItem(ModBlocks.GREEN_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.BLACK_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.MAGENTA_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.PURPLE_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.ORANGE_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.PINK_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.CYAN_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.BROWN_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.GRAY_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.RED_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.LIME_GREEN_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.YELLOW_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.BLUE_ENDER_PEARL_BLOCK);
        blockWithItem(ModBlocks.WHITE_ENDER_PEARL_BLOCK);

        blockWithItem(ModBlocks.NETHER_STAR_BLOCK);
        blockWithItem(ModBlocks.GUNPOWDER_BLOCK);
        blockWithItem(ModBlocks.ROTTEN_FLESH_BLOCK);
        blockWithItem(ModBlocks.BLAZE_ROD_BLOCK);

        // My custom ore
        blockWithItem(ModBlocks.PINK_BLOCK);
        blockWithItem(ModBlocks.PINK_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_PINK_ORE);
        blockWithItem(ModBlocks.END_STONE_PINK_ORE);
        blockWithItem(ModBlocks.NETHER_PINK_ORE);

        // My Disenchanted custom block
        blockWithItem(ModBlocks.DISENCHANTED_BLOCK);

        // My Craft custom crafting table
        registerCustomSidedCube(ModBlocks.CRAFT_CRAFTING_TABLE);

        // Block generator
        blockWithItem(ModBlocks.MCCOURSE_GENERATOR);
        blockWithItem(ModBlocks.MCCOURSE_ELEVATOR);
    }

    // Method to generate custom sign automatically in .JSON file models/blocks/name_(wall, hanging, sign).json
    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    private String name(Block block) { return key(block).getPath(); }

    private ResourceLocation key(Block block) { return ForgeRegistries.BLOCKS.getKey(block); }

    // Method to generate custom leaves automatically in .JSON file models/blocks/name_leaves.json
    private void leavesBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(),
                models().singleTexture(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get())).getPath(),
                        new ResourceLocation("minecraft:block/leaves"),
                        "all", blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    // Method to generate custom sapling automatically in .JSON file models/blocks/name_sapling.json
    private void saplingBlock(RegistryObject<Block> blockRegistryObject) {
        simpleBlock(blockRegistryObject.get(),
                models().cross(Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get())).getPath(),
                        blockTexture(blockRegistryObject.get())).renderType("cutout"));
    }

    // Method to generate custom crop automatically in .JSON file models/blocks/name.json
    public void makeCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> states(state, block, modelName, textureName);
        getVariantBuilder(block).forAllStates(function);
    }

    // Kohlrabi's crop block automatically created models/block/json.file
    private ConfiguredModel[] states(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((KohlrabiCropBlock) block).getAgeProperty()),
                new ResourceLocation(MCCourseMod.MOD_ID, "block/" + textureName +
                        state.getValue(((KohlrabiCropBlock) block).getAgeProperty()))).renderType("cutout"));
        return models;
    }

    // Cattail's crop block automatically created models/block/json.file
    public void makeCattailCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> cattailStates(state, block, modelName, textureName);
        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] cattailStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((CattailCropBlock) block).getAgeProperty()),
                new ResourceLocation(MCCourseMod.MOD_ID, "block/" + textureName +
                        state.getValue(((CattailCropBlock) block).getAgeProperty()))).renderType("cutout"));
        return models;
    }

    // Method to generate custom lamp automatically in .JSON file
    private void customLamp() {
        getVariantBuilder(ModBlocks.ALEXANDRITE_LAMP.get()).forAllStates(state -> {
            if(state.getValue(AlexandriteLampBlock.CLICKED)) {
                return new ConfiguredModel[]{new ConfiguredModel(models().cubeAll("alexandrite_lamp_on",
                        new ResourceLocation(MCCourseMod.MOD_ID, "block/" + "alexandrite_lamp_on")))};
            } else {
                return new ConfiguredModel[]{new ConfiguredModel(models().cubeAll("alexandrite_lamp_off",
                        new ResourceLocation(MCCourseMod.MOD_ID, "block/" +"alexandrite_lamp_off")))};
            }
        });
        simpleBlockItem(ModBlocks.ALEXANDRITE_LAMP.get(), models().cubeAll("alexandrite_lamp_on",
                new ResourceLocation(MCCourseMod.MOD_ID, "block/" +"alexandrite_lamp_on")));
    }

    // Method to use trapdoor block
    private void blockItem(RegistryObject<Block> blockRegistryObject, String appendix) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("mccourse:block/"
                + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get())).getPath() + appendix));
    }

    // Method to call the blocks
    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("mccourse:block/"
                + Objects.requireNonNull(ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get())).getPath()));
    }

    // Method to easy registry a block
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

    // Method for adding a block with multiple textures
    private void registerCustomSidedCube(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
        ResourceLocation key = ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get());
        String baseTexture = "block/" + Objects.requireNonNull(key).getPath();
        models().withExistingParent(key.getPath(), mcLoc("block/cube"))
                .texture("down", modLoc(baseTexture))
                .texture("up", modLoc(baseTexture + "_top"))
                .texture("north", modLoc(baseTexture + "_front"))
                .texture("south", modLoc(baseTexture + "_side"))
                .texture("west", modLoc(baseTexture + "_front"))
                .texture("east", modLoc(baseTexture + "_side"))
                .texture("particle", modLoc(baseTexture + "_front"));
    }
}