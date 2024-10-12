package net.karen.mccourse.item;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.item.custom.*;
import net.karen.mccourse.sound.ModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    // Register items
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, MCCourseMod.MOD_ID);

    // Register item in game
    // Ore's item
    public static final RegistryObject<Item> ALEXANDRITE = ITEMS.register("alexandrite",
            () -> new Item(new Item.Properties()));

    // Ore's raw
    public static final RegistryObject<Item> RAW_ALEXANDRITE = ITEMS.register("raw_alexandrite",
            () -> new Item(new Item.Properties()));

    // Custom Advanced Item
    public static final RegistryObject<Item> METAL_DETECTOR = ITEMS.register("metal_detector",
            () -> new MetalDetectorItem(new Item.Properties().durability(512)));

    // Foods
    public static final RegistryObject<Item> KOHLRABI = ITEMS.register("kohlrabi",
            () -> new Item(new Item.Properties().food(ModFoodProperties.KOHLRABI)));

    // Fuels
    public static final RegistryObject<Item> PEAT_BRICK = ITEMS.register("peat_brick",
            () -> new FuelItem(new Item.Properties(), 200));

    // Alexandrite tools - Sword, axe, pickaxe, shovel and hoe
    public static final RegistryObject<Item> ALEXANDRITE_SWORD = ITEMS.register("alexandrite_sword",
            () -> new SlowingSwordItem(ModToolTiers.ALEXANDRITE, 2, 3, new Item.Properties().durability(256)));

    public static final RegistryObject<Item> ALEXANDRITE_PICKAXE = ITEMS.register("alexandrite_pickaxe",
            () -> new PickaxeItem(ModToolTiers.ALEXANDRITE, 1, 2, new Item.Properties().durability(256)));

    public static final RegistryObject<Item> ALEXANDRITE_SHOVEL = ITEMS.register("alexandrite_shovel",
            () -> new ShovelItem(ModToolTiers.ALEXANDRITE, 2, 3, new Item.Properties().durability(256)));

    public static final RegistryObject<Item> ALEXANDRITE_AXE = ITEMS.register("alexandrite_axe",
            () -> new AxeItem(ModToolTiers.ALEXANDRITE, 2, 3, new Item.Properties().durability(256)));

    public static final RegistryObject<Item> ALEXANDRITE_HOE = ITEMS.register("alexandrite_hoe",
            () -> new HoeItem(ModToolTiers.ALEXANDRITE, 2, 3, new Item.Properties().durability(256)));

    // Alexandrite Paxel
    public static final RegistryObject<Item> ALEXANDRITE_PAXEL = ITEMS.register("alexandrite_paxel",
            () -> new PaxelItem(ModToolTiers.ALEXANDRITE, 2, 3, new Item.Properties().durability(256)));

    // Alexandrite Hammer
    public static final RegistryObject<Item> ALEXANDRITE_HAMMER = ITEMS.register("alexandrite_hammer",
            () -> new HammerItem(ModToolTiers.ALEXANDRITE, 2, 3, new Item.Properties().durability(256)));

    // Alexandrite Armor
    public static final RegistryObject<Item> ALEXANDRITE_HELMET = ITEMS.register("alexandrite_helmet",
            () -> new ModArmorItem(ModArmorMaterials.ALEXANDRITE, ArmorItem.Type.HELMET, new Item.Properties()));

    public static final RegistryObject<Item> ALEXANDRITE_CHESTPLATE = ITEMS.register("alexandrite_chestplate",
            () -> new ModArmorItem(ModArmorMaterials.ALEXANDRITE, ArmorItem.Type.CHESTPLATE, new Item.Properties()));

    public static final RegistryObject<Item> ALEXANDRITE_LEGGINGS = ITEMS.register("alexandrite_leggings",
            () -> new ModArmorItem(ModArmorMaterials.ALEXANDRITE, ArmorItem.Type.LEGGINGS, new Item.Properties()));

    public static final RegistryObject<Item> ALEXANDRITE_BOOTS = ITEMS.register("alexandrite_boots",
            () -> new ModArmorItem(ModArmorMaterials.ALEXANDRITE, ArmorItem.Type.BOOTS, new Item.Properties()));

    // Alexandrite Horse armor
    public static final RegistryObject<Item> ALEXANDRITE_HORSE_ARMOR = ITEMS.register("alexandrite_horse_armor",
            () -> new HorseArmorItem(12, new ResourceLocation(MCCourseMod.MOD_ID, "textures/entity/horse/armor/horse_armor_alexandrite.png"), new Item.Properties()));

    // Data Tablet item
    public static final RegistryObject<Item> DATA_TABLET = ITEMS.register("data_tablet",
            () -> new DataTabletItem(new Item.Properties().stacksTo(1)));

    // Kohlrabi's seeds
    public static final RegistryObject<Item> KOHLRABI_SEEDS = ITEMS.register("kohlrabi_seeds",
            () -> new ItemNameBlockItem(ModBlocks.KOHLRABI_CROP.get(), new Item.Properties()));

    // Bar Brawl's music disc
    public static final RegistryObject<Item> BAR_BRAWL_RECORD = ITEMS.register("bar_brawl_record",
            () -> new RecordItem(4, ModSounds.BAR_BRAWL, new Item.Properties().stacksTo(1), 2440));

    // Radiation Staff's custom item model
    public static final RegistryObject<Item> RADIATION_STAFF = ITEMS.register("radiation_staff",
            () -> new Item(new Item.Properties().durability(1024)));

    // Alexandrite's bow
    public static final RegistryObject<Item> ALEXANDRITE_BOW = ITEMS.register("alexandrite_bow",
            () -> new BowItem(new Item.Properties().durability(500)));

    // Alexandrite's shield
    public static final RegistryObject<Item> ALEXANDRITE_SHIELD = ITEMS.register("alexandrite_shield",
            () -> new ShieldItem(new Item.Properties().durability(500)));



    // Insert in MCCourseMod.java file
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
