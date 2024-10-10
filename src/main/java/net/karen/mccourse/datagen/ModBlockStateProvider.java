package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.custom.AlexandriteLampBlock;
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

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MCCourseMod.MOD_ID, exFileHelper);
    }

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

    }

    // Method to generate custom crop automatically in .JSON file models/blocks/name.json
    public void makeCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> states(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] states(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((KohlrabiCropBlock) block).getAgeProperty()),
                new ResourceLocation(MCCourseMod.MOD_ID, "block/" + textureName + state.getValue(((KohlrabiCropBlock) block).getAgeProperty()))).renderType("cutout"));

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
                + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath() + appendix));
    }


    // Method to call the blocks
    private void blockItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockItem(blockRegistryObject.get(), new ModelFile.UncheckedModelFile("mccourse:block/"
                + ForgeRegistries.BLOCKS.getKey(blockRegistryObject.get()).getPath()));
    }


    // Method to easy registry a block
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
}
