package net.karen.mccourse.datagen;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.*;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.*;
import java.util.*;

public class ModItemModelProvider extends ItemModelProvider {
    private static final LinkedHashMap<ResourceKey<TrimMaterial>, Float> trimMaterials = new LinkedHashMap<>();
    static { trimMaterials.put(TrimMaterials.QUARTZ, 0.1F); trimMaterials.put(TrimMaterials.IRON, 0.2F);
        trimMaterials.put(TrimMaterials.NETHERITE, 0.3F); trimMaterials.put(TrimMaterials.REDSTONE, 0.4F);
        trimMaterials.put(TrimMaterials.COPPER, 0.5F); trimMaterials.put(TrimMaterials.GOLD, 0.6F);
        trimMaterials.put(TrimMaterials.EMERALD, 0.7F); trimMaterials.put(TrimMaterials.DIAMOND, 0.8F);
        trimMaterials.put(TrimMaterials.LAPIS, 0.9F); trimMaterials.put(TrimMaterials.AMETHYST, 1.0F); }

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MCCourseMod.MOD_ID, existingFileHelper);
    }

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

        // Alexandrite horse's armor
        simpleItem(ModItems.ALEXANDRITE_HORSE_ARMOR);

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
        handheldItem(ModItems.LAPIS_LAZULI_PAXEL);

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

        // Custom items
        simpleItem(ModItems.SPECIAL_METAL_DETECTOR);
        simpleItem(ModItems.VAULT);
        simpleItem(ModItems.DESTROYER);
        simpleItem(ModItems.MAGNET);
        simpleItem(ModItems.FARMER);
        simpleItem(ModItems.RESTORE);
        simpleItem(ModItems.KAUPEN_SMITHING_TEMPLATE);

        // Custom armor TRIM
        trimmedArmorItem(ModItems.ALEXANDRITE_HELMET); // ALEXANDRITE armor
        trimmedArmorItem(ModItems.ALEXANDRITE_CHESTPLATE);
        trimmedArmorItem(ModItems.ALEXANDRITE_LEGGINGS);
        trimmedArmorItem(ModItems.ALEXANDRITE_BOOTS);
        trimmedArmorItem(ModItems.COPPER_HELMET); // COPPER armor
        trimmedArmorItem(ModItems.COPPER_CHESTPLATE);
        trimmedArmorItem(ModItems.COPPER_LEGGINGS);
        trimmedArmorItem(ModItems.COPPER_BOOTS);
        trimmedArmorItem(ModItems.PINK_HELMET); // PINK armor
        trimmedArmorItem(ModItems.PINK_CHESTPLATE);
        trimmedArmorItem(ModItems.PINK_LEGGINGS);
        trimmedArmorItem(ModItems.PINK_BOOTS);
        trimmedArmorItem(ModItems.MINER_HELMET); // MINER armor

        // Level Charger
        simpleItem(ModItems.LEVEL_CHARGER_PLUS);
        simpleItem(ModItems.LEVEL_CHARGER_MINUS);
        simpleItem(ModItems.LEVEL_CHARGER_PLUS_FORTUNE);
        simpleItem(ModItems.LEVEL_CHARGER_MINUS_FORTUNE);

        // Farm items
        simpleItem(ModItems.INFINITE);
        simpleItem(ModItems.LUCKY_BOMB);
        simpleItem(ModItems.DESTROYER_TAG);
        simpleItem(ModItems.ULTRA_COMPACTOR);
        simpleItem(ModItems.PINK_ULTRA_COMPACTOR);
        simpleItem(ModItems.GROWTH);

        // Fishing Rod
        fishingRodWithCastOverride(ModItems.MCCOURSE_FISHING_ROD);

        // Alternate items
        alternateItem(ModItems.DATA_TABLET); // Data Tablet
        alternateItem(ModItems.MCCOURSE_BOTTLE); // Mccourse Bottle

        // Mccourse glass
        simpleBlockItemModel(ModBlocks.MCCOURSE_GLASS_PANE_BLOCK, ModBlocks.MCCOURSE_GLASS_BLOCK);
    }

    // Registry all sapling item's models
    private ItemModelBuilder saplingItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated"))
                .texture("layer0", new ResourceLocation(MCCourseMod.MOD_ID,"block/" + item.getId().getPath()));
    }

    // Registry all complex block's models
    private ItemModelBuilder complexBlock(Block block) {
        return withExistingParent(ForgeRegistries.BLOCKS.getKey(block).getPath(), new ResourceLocation(MCCourseMod.MOD_ID,
                "block/" + ForgeRegistries.BLOCKS.getKey(block).getPath()));
    }

    // Registry all fence's models
    public void fenceItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/fence_inventory"))
                .texture("texture",  new ResourceLocation(MCCourseMod.MOD_ID, "block/" +
                        ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    // Registry all wall's models
    public void wallItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/wall_inventory"))
                .texture("wall",  new ResourceLocation(MCCourseMod.MOD_ID, "block/" +
                        ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    // Registry all button's models
    public void buttonItem(RegistryObject<Block> block, RegistryObject<Block> baseBlock) {
        this.withExistingParent(ForgeRegistries.BLOCKS.getKey(block.get()).getPath(), mcLoc("block/button_inventory"))
                .texture("texture", new ResourceLocation(MCCourseMod.MOD_ID, "block/" +
                        ForgeRegistries.BLOCKS.getKey(baseBlock.get()).getPath()));
    }

    // Registry all tool's models
    private ItemModelBuilder handheldItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/handheld")).texture("layer0",
                new ResourceLocation(MCCourseMod.MOD_ID,"item/" + item.getId().getPath()));
    }

    // Registry all block's models like item
    private ItemModelBuilder simpleBlockItem(RegistryObject<Block> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MCCourseMod.MOD_ID,"item/" + item.getId().getPath()));
    }

    // Registry all block's models like block
    private ItemModelBuilder simpleBlockItemModel(RegistryObject<Block> blockName, RegistryObject<Block> blockResult) {
        String name = blockName.getId().getPath(), result = blockResult.getId().getPath();
        ResourceLocation item = new ResourceLocation("item/generated"),
                         block = new ResourceLocation(MCCourseMod.MOD_ID, "block/" + result);
        return withExistingParent(name, item).texture("layer0", block);
    }

    // Registry all item's models
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(), new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(MCCourseMod.MOD_ID,"item/" + item.getId().getPath()));
    }

    // Shoutout to El_Redstoniano for making this -> Registry all custom armor trims
    private void trimmedArmorItem(RegistryObject<Item> itemRegistryObject) {
        final String MOD_ID = MCCourseMod.MOD_ID;
        if (itemRegistryObject.get() instanceof ArmorItem armorItem) {
            trimMaterials.forEach((trimMaterial, value) -> {
                String armorType = switch (armorItem.getEquipmentSlot()) { case HEAD -> "helmet"; case CHEST -> "chestplate";
                case LEGS -> "leggings"; case FEET -> "boots"; default -> ""; };
                String currentTrimName = "item/" + armorItem + "_" + trimMaterial.location().getPath() + "_trim";
                ResourceLocation trimResLoc = new ResourceLocation("trims/items/" + armorType +
                        "_trim_" + trimMaterial.location().getPath()); // minecraft namespace
                /* This is used for making the ExistingFileHelper acknowledge that this texture exist,
                   so this will avoid an IllegalArgumentException */
                existingFileHelper.trackGenerated(trimResLoc, PackType.CLIENT_RESOURCES, ".png", "textures");
                // Trimmed armorItem files
                getBuilder(currentTrimName).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(MOD_ID, "item/" + armorItem)).texture("layer1", trimResLoc);
                // Non-trimmed armorItem file (normal variant)
                this.withExistingParent(itemRegistryObject.getId().getPath(), mcLoc("item/generated"))
                .override().model(new ModelFile.UncheckedModelFile(new ResourceLocation(MOD_ID, currentTrimName)))
                .predicate(mcLoc("trim_type"), value).end()
                .texture("layer0", new ResourceLocation(MOD_ID, "item/" + itemRegistryObject.getId().getPath()));
            });
        }
    }

    // Fishing Rod
    private void fishingRodWithCastOverride(RegistryObject<Item> item) {
        String itemName = item.getId().getPath();
        // Example: Mccourse Fishing Rod
        getBuilder(itemName).parent(new ModelFile.UncheckedModelFile("minecraft:item/handheld_rod"))
                  .texture("layer0", new ResourceLocation(MCCourseMod.MOD_ID, "item/" + itemName))
                  .override().predicate(new ResourceLocation("cast"), 1.0f)
                  .model(new ModelFile.UncheckedModelFile(new ResourceLocation(MCCourseMod.MOD_ID, "item/" + itemName + "_cast")))
                  .end();

        // Example: Mccourse Fishing Rod Cast
        getBuilder(itemName + "_cast").parent(new ModelFile.UncheckedModelFile("minecraft:item/fishing_rod"))
                                           .texture("layer0", new ResourceLocation(MCCourseMod.MOD_ID,
                                                   "item/" + itemName + "_cast"));
    }

    // Data tablet
    private void alternateItem(RegistryObject<Item> item) {
        String itemName = item.getId().getPath();
        // Example: Data Tablet
        getBuilder(itemName).parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(MCCourseMod.MOD_ID, "item/" + itemName + "_off"))
                .override().predicate(new ResourceLocation(MCCourseMod.MOD_ID, "on"), 1.0f)
                .model(new ModelFile.UncheckedModelFile(new ResourceLocation(MCCourseMod.MOD_ID, "item/" + itemName + "_on")))
                .end();

        // Example: Data Tablet On
        getBuilder(itemName + "_on").parent(new ModelFile.UncheckedModelFile("item/generated"))
                                         .texture("layer0", new ResourceLocation(MCCourseMod.MOD_ID, "item/" + itemName));
    }
}