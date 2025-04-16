package net.karen.mccourse.item;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.entity.ModEntities;
import net.karen.mccourse.entity.custom.ModBoatEntity;
import net.karen.mccourse.fluid.ModFluids;
import net.karen.mccourse.item.custom.*;
import net.karen.mccourse.sound.ModSounds;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

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
            () -> new MetalDetectorItem(new Item.Properties().durability(0)));

    // Foods
    public static final RegistryObject<Item> KOHLRABI = ITEMS.register("kohlrabi",
            () -> new Item(new Item.Properties().food(ModFoodProperties.KOHLRABI)));

    // Fuels
    public static final RegistryObject<Item> PEAT_BRICK = ITEMS.register("peat_brick",
            () -> new FuelItem(new Item.Properties(), 200));

    // Alexandrite tools - Sword, axe, pickaxe, shovel and hoe
    public static final RegistryObject<Item> ALEXANDRITE_SWORD = ITEMS.register("alexandrite_sword",
            () -> new SlowingSwordItem(ModToolTiers.ALEXANDRITE, 2, 3,
                    new Item.Properties().durability(2304)));

    public static final RegistryObject<Item> ALEXANDRITE_PICKAXE = ITEMS.register("alexandrite_pickaxe",
            () -> new PickaxeItem(ModToolTiers.ALEXANDRITE, 1, 2,
                    new Item.Properties().durability(2304)));

    public static final RegistryObject<Item> ALEXANDRITE_SHOVEL = ITEMS.register("alexandrite_shovel",
            () -> new ShovelItem(ModToolTiers.ALEXANDRITE, 2, 3,
                    new Item.Properties().durability(2304)));

    public static final RegistryObject<Item> ALEXANDRITE_AXE = ITEMS.register("alexandrite_axe",
            () -> new AxeItem(ModToolTiers.ALEXANDRITE, 2, 3,
                    new Item.Properties().durability(2304)));

    public static final RegistryObject<Item> ALEXANDRITE_HOE = ITEMS.register("alexandrite_hoe",
            () -> new HoeItem(ModToolTiers.ALEXANDRITE, 2, 3,
                    new Item.Properties().durability(2304)));

    // Alexandrite Paxel
    public static final RegistryObject<Item> ALEXANDRITE_PAXEL = ITEMS.register("alexandrite_paxel",
            () -> new PaxelItem(ModToolTiers.ALEXANDRITE, 2, 3,
                    new Item.Properties().durability(2304)));

    // Alexandrite Hammer
    public static final RegistryObject<Item> ALEXANDRITE_HAMMER = ITEMS.register("alexandrite_hammer",
            () -> new HammerItem(ModToolTiers.ALEXANDRITE, 2, 3, BlockTags.MINEABLE_WITH_PICKAXE,
                    new Item.Properties().durability(2304)).setRadius(2));

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
            () -> new HorseArmorItem(12, new ResourceLocation(MCCourseMod.MOD_ID,
                    "textures/entity/horse/armor/horse_armor_alexandrite.png"), new Item.Properties()));

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
            () -> new RadiationStaffItem(new Item.Properties().durability(1024)));

    // Alexandrite's bow
    public static final RegistryObject<Item> ALEXANDRITE_BOW = ITEMS.register("alexandrite_bow",
            () -> new BowItem(new Item.Properties().durability(2304)));

    // Alexandrite's shield
    public static final RegistryObject<Item> ALEXANDRITE_SHIELD = ITEMS.register("alexandrite_shield",
            () -> new ShieldItem(new Item.Properties().durability(2304)));

    // Soap Water Bucket custom fluid
    public static final RegistryObject<Item> SOAP_WATER_BUCKET = ITEMS.register("soap_water_bucket",
            () -> new BucketItem(ModFluids.SOURCE_SOAP_WATER, new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    // Walnut's custom sign
    public static final RegistryObject<Item> WALNUT_SIGN = ITEMS.register("walnut_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.WALNUT_SIGN.get(), ModBlocks.WALNUT_WALL_SIGN.get()));
    public static final RegistryObject<Item> WALNUT_HANGING_SIGN = ITEMS.register("walnut_hanging_sign",
            () -> new HangingSignItem(ModBlocks.WALNUT_HANGING_SIGN.get(), ModBlocks.WALNUT_WALL_HANGING_SIGN.get(),
                    new Item.Properties().stacksTo(16)));

    // Rhino's custom egg
    public static final RegistryObject<Item> RHINO_SPAWN_EGG = ITEMS.register("rhino_spawn_egg",
            () -> new ForgeSpawnEggItem(ModEntities.RHINO, 0x7e9680, 0xc5d1c5,
                    new Item.Properties()));

    // Dice Projectile's item
    public static final RegistryObject<Item> DICE = ITEMS.register("dice",
            () -> new DiceItem(new Item.Properties()));

    // Boat's item
    public static final RegistryObject<Item> WALNUT_BOAT = ITEMS.register("walnut_boat",
            () -> new ModBoatItem(false, ModBoatEntity.Type.WALNUT, new Item.Properties()));

    public static final RegistryObject<Item> WALNUT_CHEST_BOAT = ITEMS.register("walnut_chest_boat",
            () -> new ModBoatItem(true, ModBoatEntity.Type.WALNUT, new Item.Properties()));

    // Cattail custom crop
    public static final RegistryObject<Item> CATTAIL = ITEMS.register("cattail",
            () -> new Item(new Item.Properties()));
    
    public static final RegistryObject<Item> CATTAIL_SEEDS = ITEMS.register("cattail_seeds",
            () -> new ItemNameBlockItem(ModBlocks.CATTAIL_CROP.get(), new Item.Properties()));

    // Pink's custom armor
    public static final RegistryObject<Item> PINK_HELMET = ITEMS.register("pink_helmet",
            () -> new ModArmorItem(ModArmorMaterials.PINK, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_CHESTPLATE = ITEMS.register("pink_chestplate",
            () -> new ModArmorItem(ModArmorMaterials.PINK, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_LEGGINGS = ITEMS.register("pink_leggings",
            () -> new ModArmorItem(ModArmorMaterials.PINK, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_BOOTS = ITEMS.register("pink_boots",
            () -> new ModArmorItem(ModArmorMaterials.PINK, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));

    // Copper custom armor
    public static final RegistryObject<Item> COPPER_HELMET = ITEMS.register("copper_helmet",
            () -> new ModArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate",
            () -> new ModArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_LEGGINGS = ITEMS.register("copper_leggings",
            () -> new ModArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS, new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_BOOTS = ITEMS.register("copper_boots",
            () -> new ModArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));

    // Pink's custom tool
    public static final RegistryObject<Item> PINK_SWORD = ITEMS.register("pink_sword",
            () -> new SlowingSwordItem(ModToolTiers.PINK, 2, 3,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_PAXEL = ITEMS.register("pink_paxel",
            () -> new PaxelItem(ModToolTiers.PINK, 1, 2,
                    new Item.Properties().fireResistant()));

    // Pink's custom modes pickaxe
    public static final RegistryObject<Item> PINK_PICKAXE = ITEMS.register("pink_pickaxe",
            () -> new PickaxeItem(ModToolTiers.PINK, 1, 2,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_SHOVEL = ITEMS.register("pink_shovel",
            () -> new ShovelItem(ModToolTiers.PINK, 2, 3,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_AXE = ITEMS.register("pink_axe",
            () -> new AxeItem(ModToolTiers.PINK, 2, 3,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_HOE = ITEMS.register("pink_hoe",
            () -> new HoeItem(ModToolTiers.PINK, 2, 3,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_SWORD = ITEMS.register("copper_sword",
            () -> new SwordItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe",
            () -> new PickaxeItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_SHOVEL = ITEMS.register("copper_shovel",
            () -> new ShovelItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_AXE = ITEMS.register("copper_axe",
            () -> new AxeItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_HOE = ITEMS.register("copper_hoe",
            () -> new HoeItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_PAXEL = ITEMS.register("copper_paxel",
            () -> new PaxelItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> DIAMOND_PAXEL = ITEMS.register("diamond_paxel",
            () -> new PaxelItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> GOLD_PAXEL = ITEMS.register("gold_paxel",
            () -> new PaxelItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> IRON_PAXEL = ITEMS.register("iron_paxel",
            () -> new PaxelItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> STONE_PAXEL = ITEMS.register("stone_paxel",
            () -> new PaxelItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> WOODEN_PAXEL = ITEMS.register("wooden_paxel",
            () -> new PaxelItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> NETHERITE_PAXEL = ITEMS.register("netherite_paxel",
            () -> new PaxelItem(ModToolTiers.COPPER, 1, 1,
                    new Item.Properties().fireResistant()));

    // Mining Custom Item
    public static final RegistryObject<Item> MINING_MODES = ITEMS.register("mining_modes",
            () -> new ModesPickaxeItem(ModToolTiers.PINK, 2, 3,
                    new Item.Properties().fireResistant().durability(0))
                    .setLevel(10) // Level enchantment
                    .setEnchantment(List.of(Enchantments.BLOCK_EFFICIENCY, Enchantments.BLOCK_FORTUNE,
                            Enchantments.UNBREAKING, Enchantments.MENDING) // List of enchantments
                    ));

    public static final RegistryObject<Item> COPPER_HAMMER = ITEMS.register("copper_hammer",
            () -> new HammerItem(ModToolTiers.COPPER, 2, 3, BlockTags.MINEABLE_WITH_PICKAXE,
                    new Item.Properties().durability(0).fireResistant()).setRadius(3));

    public static final RegistryObject<Item> DIAMOND_HAMMER = ITEMS.register("diamond_hammer",
            () -> new HammerItem(Tiers.DIAMOND, 2, 3, BlockTags.MINEABLE_WITH_PICKAXE,
                    new Item.Properties().durability(0).fireResistant()).setRadius(3));

    public static final RegistryObject<Item> GOLD_HAMMER = ITEMS.register("gold_hammer",
            () -> new HammerItem(Tiers.GOLD, 2, 3, BlockTags.MINEABLE_WITH_PICKAXE,
                    new Item.Properties().durability(0).fireResistant()).setRadius(2));

    public static final RegistryObject<Item> IRON_HAMMER = ITEMS.register("iron_hammer",
            () -> new HammerItem(Tiers.IRON, 2, 3, BlockTags.MINEABLE_WITH_PICKAXE,
                    new Item.Properties().durability(0).fireResistant()).setRadius(2));

    public static final RegistryObject<Item> NETHERITE_HAMMER = ITEMS.register("netherite_hammer",
            () -> new HammerItem(Tiers.NETHERITE, 2, 3, BlockTags.MINEABLE_WITH_PICKAXE,
                    new Item.Properties().durability(0).fireResistant()).setRadius(4));

    public static final RegistryObject<Item> PINK_HAMMER = ITEMS.register("pink_hammer",
            () -> new HammerItem(ModToolTiers.PINK, 2, 3, BlockTags.MINEABLE_WITH_PICKAXE,
                    new Item.Properties().durability(0).fireResistant()).setRadius(5));

    public static final RegistryObject<Item> STONE_HAMMER = ITEMS.register("stone_hammer",
            () -> new HammerItem(Tiers.STONE, 2, 3, BlockTags.MINEABLE_WITH_PICKAXE,
                    new Item.Properties().durability(0).fireResistant()).setRadius(2));

    public static final RegistryObject<Item> WOODEN_HAMMER = ITEMS.register("wooden_hammer",
            () -> new HammerItem(Tiers.WOOD, 2, 3, BlockTags.MINEABLE_WITH_PICKAXE,
                    new Item.Properties().durability(0).fireResistant()).setRadius(2));

    // Ore custom item
    public static final RegistryObject<Item> PINK = ITEMS.register("pink",
            () -> new Item(new Item.Properties()));

    // Bouncy ball's custom Ender pearls
    public static final RegistryObject<Item> BOUNCY_BALLS = ITEMS.register("bouncy_balls",
            () -> new BouncyBallsItem(new Item.Properties().fireResistant().durability(-1)));

    public static final RegistryObject<Item> BOUNCY_BALLS_PARTICLES = ITEMS.register("bouncy_balls_particles",
            () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));

    // Insert in MCCourseMod.java file
    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }
}