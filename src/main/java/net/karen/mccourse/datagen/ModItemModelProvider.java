package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) { super(output, MCCourseMod.MOD_ID, existingFileHelper); }

    // Registry all item's models
    @Override
    protected void registerModels() {
        // Items
        simpleItem(ModItems.ALEXANDRITE);
        simpleItem(ModItems.RAW_ALEXANDRITE);

        // Foods
        simpleItem(ModItems.KOHLRABI);

        // Custom Advanced Item
        simpleItem(ModItems.METAL_DETECTOR);

        // Custom Fuel Item
        simpleItem(ModItems.PEAT_BRICK);

        // Custom button block
        buttonItem(ModBlocks.ALEXANDRITE_BUTTON, ModBlocks.ALEXANDRITE_BLOCK);

        // Custom fence block
        fenceItem(ModBlocks.ALEXANDRITE_FENCE, ModBlocks.ALEXANDRITE_BLOCK);

        // Custom wall block
        wallItem(ModBlocks.ALEXANDRITE_WALL, ModBlocks.ALEXANDRITE_BLOCK);

        // Custom door block
        simpleBlockItem(ModBlocks.ALEXANDRITE_DOOR);

        // Alexandrite tools
        handheldItem(ModItems.ALEXANDRITE_SWORD);
        handheldItem(ModItems.ALEXANDRITE_PICKAXE);
        handheldItem(ModItems.ALEXANDRITE_SHOVEL);
        handheldItem(ModItems.ALEXANDRITE_AXE);
        handheldItem(ModItems.ALEXANDRITE_HOE);

        // Alexandrite paxel
        handheldItem(ModItems.ALEXANDRITE_PAXEL);

        // Alexandrite hammer
        handheldItem(ModItems.ALEXANDRITE_HAMMER);

        // Alexandrite player's armor
//        simpleItem(ModItems.ALEXANDRITE_HELMET);
//        simpleItem(ModItems.ALEXANDRITE_CHESTPLATE);
//        simpleItem(ModItems.ALEXANDRITE_LEGGINGS);
//        simpleItem(ModItems.ALEXANDRITE_BOOTS);

        // Alexandrite horse's armor
        simpleItem(ModItems.ALEXANDRITE_HORSE_ARMOR);

        // Data tablet item
        // simpleItem(ModItems.DATA_TABLET);

        // Kohlrabi's seeds
        simpleItem(ModItems.KOHLRABI_SEEDS);

        // Snapdragon's flower
        simpleBlockItem(ModBlocks.SNAPDRAGON);

        // Bar Brawl's music disc
        simpleItem(ModItems.BAR_BRAWL_RECORD);

        // Gem Empowering Station
        complexBlock(ModBlocks.GEM_EMPOWERING_STATION.get());

        // Soap Water Bucket fluid
        simpleItem(ModItems.SOAP_WATER_BUCKET);

        // Walnut's sapling block
        saplingItem(ModBlocks.WALNUT_SAPLING);

        // Walnut's sign block
        simpleItem(ModItems.WALNUT_SIGN);
        simpleItem(ModItems.WALNUT_HANGING_SIGN);

        // Rhino's custom egg
        withExistingParent(ModItems.RHINO_SPAWN_EGG.getId().getPath(), mcLoc("item/template_spawn_egg"));

        // Dice's custom item
        simpleItem(ModItems.DICE);

        // Boat's custom item
        simpleItem(ModItems.WALNUT_BOAT);
        simpleItem(ModItems.WALNUT_CHEST_BOAT);

        // Cattail's custom crop
        simpleItem(ModItems.CATTAIL);
        simpleItem(ModItems.CATTAIL_SEEDS);

        // My custom armor and tool
        simpleItem(ModItems.PINK_HELMET);
        simpleItem(ModItems.PINK_CHESTPLATE);
        simpleItem(ModItems.PINK_LEGGINGS);
        simpleItem(ModItems.PINK_BOOTS);

        handheldItem(ModItems.PINK_SWORD);
        handheldItem(ModItems.PINK_PAXEL);
        handheldItem(ModItems.PINK_PICKAXE);
        handheldItem(ModItems.PINK_SHOVEL);
        handheldItem(ModItems.PINK_AXE);
        handheldItem(ModItems.PINK_HOE);

        simpleItem(ModItems.COPPER_HELMET); // Copper
        simpleItem(ModItems.COPPER_CHESTPLATE);
        simpleItem(ModItems.COPPER_LEGGINGS);
        simpleItem(ModItems.COPPER_BOOTS);

        handheldItem(ModItems.COPPER_SWORD);
        handheldItem(ModItems.COPPER_PAXEL);
        handheldItem(ModItems.COPPER_PICKAXE);
        handheldItem(ModItems.COPPER_SHOVEL);
        handheldItem(ModItems.COPPER_AXE);
        handheldItem(ModItems.COPPER_HOE);

        handheldItem(ModItems.DIAMOND_PAXEL);
        handheldItem(ModItems.GOLD_PAXEL);
        handheldItem(ModItems.IRON_PAXEL);
        handheldItem(ModItems.STONE_PAXEL);
        handheldItem(ModItems.WOODEN_PAXEL);
        handheldItem(ModItems.NETHERITE_PAXEL);

        // My custom ender pearl
        simpleItem(ModItems.BOUNCY_BALLS);
        simpleItem(ModItems.BOUNCY_BALLS_PARTICLES);

        // Mining Modes
        handheldItem(ModItems.BLUE_MODES);
        handheldItem(ModItems.GREEN_MODES);
        handheldItem(ModItems.PURPLE_MODES);
        handheldItem(ModItems.ORANGE_MODES);
        handheldItem(ModItems.PINK_MODES);

        // Hammer
        handheldItem(ModItems.COPPER_HAMMER);
        handheldItem(ModItems.DIAMOND_HAMMER);
        handheldItem(ModItems.GOLD_HAMMER);
        handheldItem(ModItems.IRON_HAMMER);
        handheldItem(ModItems.NETHERITE_HAMMER);
        handheldItem(ModItems.PINK_HAMMER);
        handheldItem(ModItems.STONE_HAMMER);
        handheldItem(ModItems.WOODEN_HAMMER);
        handheldItem(ModItems.MCCOURSE_HAMMER);

        // My custom ore
        simpleItem(ModItems.PINK);

        // Luck custom generator enchanted book
        simpleItem(ModItems.LUCK);
        simpleItem(ModItems.PICKAXE_LUCK);
        simpleItem(ModItems.WEAPON_LUCK);
    }

    // Registry all sapling item's models
    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MCCourseMod.MOD_ID,"block/" + item.getId().getPath()));
    }

    // Registry all complex block's models
    private ItemModelBuilder complexBlock(Block block) {
        return withExistingParent(ForgeRegistries.BLOCKS.getKey(block).getPath(), new ResourceLocation(MCCourseMod.MOD_ID,
                "block/" + ForgeRegistries.BLOCKS.getKey(block).getPath()));
    }

    // Registry all fence's models
    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  new ResourceLocation(MCCourseMod.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    // Registry all wall's models
    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(MCCourseMod.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    // Registry all button's models
    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", new ResourceLocation(MCCourseMod.MOD_ID, "block/" + ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    // Registry all tool's models
    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(MCCourseMod.MOD_ID,"item/" + item.getId().getPath()));
    }

    // Registry all block's models
    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MCCourseMod.MOD_ID,"item/" + item.getId().getPath()));
    }

    // Registry all item's models
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MCCourseMod.MOD_ID,"item/" + item.getId().getPath()));
    }
}