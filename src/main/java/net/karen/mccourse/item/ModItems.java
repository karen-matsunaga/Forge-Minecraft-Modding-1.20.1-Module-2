package net.karen.mccourse.item;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.entity.ModEntities;
import net.karen.mccourse.entity.custom.ModBoatEntity;
import net.karen.mccourse.fluid.ModFluids;
import net.karen.mccourse.item.custom.*;
import net.karen.mccourse.sound.ModSounds;
import net.karen.mccourse.util.ModTags;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import java.util.function.Supplier;

public class ModItems {
    // Register items
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MCCourseMod.MOD_ID);

    // Register item in game
    // Ore's item
    public static final RegistryObject<Item> ALEXANDRITE = ITEMS.register("alexandrite",
            () -> new Item(new Item.Properties()));

    // Ore's raw
    public static final RegistryObject<Item> RAW_ALEXANDRITE = ITEMS.register("raw_alexandrite",
            () -> new Item(new Item.Properties()));

    // Custom Advanced Item
    public static final RegistryObject<Item> METAL_DETECTOR = ITEMS.register("metal_detector",
            () -> new MetalDetectorItem(new Item.Properties().fireResistant().stacksTo(1),
                    ModTags.Blocks.METAL_DETECTOR_VALUABLES));

    // Foods
    public static final RegistryObject<Item> KOHLRABI = ITEMS.register("kohlrabi",
            () -> new Item(new Item.Properties().food(ModFoodProperties.KOHLRABI)));

    // Fuels
    public static final RegistryObject<Item> PEAT_BRICK = ITEMS.register("peat_brick",
            () -> new FuelItem(new Item.Properties(), 200));

    // Alexandrite tools - Sword, axe, pickaxe, shovel and hoe
    public static final RegistryObject<Item> ALEXANDRITE_SWORD = ITEMS.register("alexandrite_sword",
            () -> new SlowingSwordItem(ModToolTiers.ALEXANDRITE, 2, 3,
                    new Item.Properties().durability(2304).fireResistant()));

    public static final RegistryObject<Item> ALEXANDRITE_PICKAXE = ITEMS.register("alexandrite_pickaxe",
            () -> new PickaxeItem(ModToolTiers.ALEXANDRITE, 1, 2,
                    new Item.Properties().durability(2304).fireResistant()));

    public static final RegistryObject<Item> ALEXANDRITE_SHOVEL = ITEMS.register("alexandrite_shovel",
            () -> new ShovelItem(ModToolTiers.ALEXANDRITE, 2, 3,
                    new Item.Properties().durability(2304).fireResistant()));

    public static final RegistryObject<Item> ALEXANDRITE_AXE = ITEMS.register("alexandrite_axe",
            () -> new AxeItem(ModToolTiers.ALEXANDRITE, 2, 3,
                    new Item.Properties().durability(2304).fireResistant()));

    public static final RegistryObject<Item> ALEXANDRITE_HOE = ITEMS.register("alexandrite_hoe",
            () -> new HoeItem(ModToolTiers.ALEXANDRITE, 2, 3,
                    new Item.Properties().durability(2304).fireResistant()));

    // Alexandrite Paxel
    public static final RegistryObject<Item> ALEXANDRITE_PAXEL = ITEMS.register("alexandrite_paxel",
            () -> new PaxelItem(ModToolTiers.ALEXANDRITE, 2, 3,
                  new Item.Properties().durability(2304).fireResistant(), false));

    // Alexandrite Hammer
    public static final RegistryObject<Item> ALEXANDRITE_HAMMER = ITEMS.register("alexandrite_hammer",
            () -> new HammerItem(ModToolTiers.ALEXANDRITE, 2, 3,
                  new Item.Properties().durability(2304).fireResistant(),
                  BlockTags.MINEABLE_WITH_PICKAXE,2, false, 1));

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
            () -> new RecordItem(4, ModSounds.BAR_BRAWL, new Item.Properties().stacksTo(1),
                    2440));

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
            () -> new BucketItem(ModFluids.SOURCE_SOAP_WATER,
                    new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));

    // Walnut's custom sign
    public static final RegistryObject<Item> WALNUT_SIGN = ITEMS.register("walnut_sign",
            () -> new SignItem(new Item.Properties().stacksTo(16), ModBlocks.WALNUT_SIGN.get(),
                    ModBlocks.WALNUT_WALL_SIGN.get()));
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
            () -> new ModArmorItem(ModArmorMaterials.PINK, ArmorItem.Type.HELMET,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_CHESTPLATE = ITEMS.register("pink_chestplate",
            () -> new ModArmorItem(ModArmorMaterials.PINK, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_LEGGINGS = ITEMS.register("pink_leggings",
            () -> new ModArmorItem(ModArmorMaterials.PINK, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_BOOTS = ITEMS.register("pink_boots",
            () -> new ModArmorItem(ModArmorMaterials.PINK, ArmorItem.Type.BOOTS,
                    new Item.Properties().fireResistant()));

    // Copper custom armor
    public static final RegistryObject<Item> COPPER_HELMET = ITEMS.register("copper_helmet",
            () -> new ModArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.HELMET,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate",
            () -> new ModArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_LEGGINGS = ITEMS.register("copper_leggings",
            () -> new ModArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> COPPER_BOOTS = ITEMS.register("copper_boots",
            () -> new ModArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.BOOTS,
                    new Item.Properties().fireResistant()));

    // Pink's custom tool
    public static final RegistryObject<Item> PINK_SWORD = ITEMS.register("pink_sword",
            () -> new SlowingSwordItem(ModToolTiers.PINK, 2, 3,
                    new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> PINK_PAXEL = ITEMS.register("pink_paxel",
            () -> new PaxelItem(ModToolTiers.PINK, 1, 2,
                  new Item.Properties().fireResistant(), false));

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

    // Copper's custom tool
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

    // Custom paxel
    public static final RegistryObject<Item> COPPER_PAXEL = ITEMS.register("copper_paxel",
            () -> new PaxelItem(ModToolTiers.COPPER, 1, 1,
                  new Item.Properties().fireResistant(), false));

    public static final RegistryObject<Item> DIAMOND_PAXEL = ITEMS.register("diamond_paxel",
            () -> new PaxelItem(Tiers.DIAMOND, 1, 1,
                  new Item.Properties().fireResistant(), false));

    public static final RegistryObject<Item> GOLD_PAXEL = ITEMS.register("gold_paxel",
            () -> new PaxelItem(Tiers.GOLD, 1, 1,
                  new Item.Properties().fireResistant(), true));

    public static final RegistryObject<Item> IRON_PAXEL = ITEMS.register("iron_paxel",
            () -> new PaxelItem(Tiers.IRON, 1, 1,
                  new Item.Properties().fireResistant(), false));

    public static final RegistryObject<Item> STONE_PAXEL = ITEMS.register("stone_paxel",
            () -> new PaxelItem(Tiers.STONE, 1, 1,
                    new Item.Properties().fireResistant(), false));

    public static final RegistryObject<Item> WOODEN_PAXEL = ITEMS.register("wooden_paxel",
            () -> new PaxelItem(Tiers.WOOD, 1, 1,
                  new Item.Properties().fireResistant(), false));

    public static final RegistryObject<Item> NETHERITE_PAXEL = ITEMS.register("netherite_paxel",
            () -> new PaxelItem(Tiers.NETHERITE, 1, 1,
                  new Item.Properties().fireResistant(), true));

    // Lapis Lazuli Paxel
    public static final RegistryObject<Item> LAPIS_LAZULI_PAXEL = registerItem("lapis_lazuli_paxel",
            () -> new PaxelItem(ModToolTiers.LAPIS_LAZULI, 1, 1,
                  new Item.Properties().fireResistant(), false));

    // Modes's custom pickaxes
    public static final RegistryObject<Item> BLUE_MODES = ITEMS.register("blue_modes",
            () -> new ModesPickaxeItem(ModToolTiers.PINK, 2, 3,
                  new Item.Properties().fireResistant().durability(0), false));

    public static final RegistryObject<Item> GREEN_MODES = ITEMS.register("green_modes",
            () -> new ModesPickaxeItem(ModToolTiers.COPPER, 3, 4,
                  new Item.Properties().fireResistant().durability(0), false));

    public static final RegistryObject<Item> ORANGE_MODES = ITEMS.register("orange_modes",
            () -> new ModesPickaxeItem(ModToolTiers.ALEXANDRITE, 5, 6,
                  new Item.Properties().fireResistant().durability(0), true));

    public static final RegistryObject<Item> PINK_MODES = ITEMS.register("pink_modes",
            () -> new ModesPickaxeItem(ModToolTiers.ALEXANDRITE, 6, 7,
                  new Item.Properties().fireResistant().durability(0), false));

    public static final RegistryObject<Item> PURPLE_MODES = ITEMS.register("purple_modes",
            () -> new ModesPickaxeItem(ModToolTiers.ALEXANDRITE, 4, 5,
                  new Item.Properties().fireResistant().durability(0), true));

    // Custom hammer
    public static final RegistryObject<Item> COPPER_HAMMER = ITEMS.register("copper_hammer",
            () -> new HammerItem(ModToolTiers.COPPER, 2, 3,
                  new Item.Properties().durability(0).fireResistant(),
                  BlockTags.MINEABLE_WITH_PICKAXE,3, false, 2));

    public static final RegistryObject<Item> DIAMOND_HAMMER = ITEMS.register("diamond_hammer",
            () -> new HammerItem(Tiers.DIAMOND, 2, 3,
                  new Item.Properties().durability(0).fireResistant(),
                  BlockTags.MINEABLE_WITH_PICKAXE,3, false, 3));

    public static final RegistryObject<Item> GOLD_HAMMER = ITEMS.register("gold_hammer",
            () -> new HammerItem(Tiers.GOLD, 2, 3,
                  new Item.Properties().durability(0).fireResistant(),
                  BlockTags.MINEABLE_WITH_PICKAXE, 2, false, 1));

    public static final RegistryObject<Item> IRON_HAMMER = ITEMS.register("iron_hammer",
            () -> new HammerItem(Tiers.IRON, 2, 3,
                  new Item.Properties().durability(0).fireResistant(),
                  BlockTags.MINEABLE_WITH_PICKAXE,2, false, 1));

    public static final RegistryObject<Item> NETHERITE_HAMMER = ITEMS.register("netherite_hammer",
            () -> new HammerItem(Tiers.NETHERITE, 2, 3,
                  new Item.Properties().fireResistant(), BlockTags.MINEABLE_WITH_PICKAXE,4, true, 5));

    public static final RegistryObject<Item> PINK_HAMMER = ITEMS.register("pink_hammer",
            () -> new HammerItem(ModToolTiers.PINK, 2, 3,
                  new Item.Properties().fireResistant(), BlockTags.MINEABLE_WITH_PICKAXE,5, true, 10));

    public static final RegistryObject<Item> STONE_HAMMER = ITEMS.register("stone_hammer",
            () -> new HammerItem(Tiers.STONE, 2, 3,
                  new Item.Properties().fireResistant(), BlockTags.MINEABLE_WITH_PICKAXE,2, false, 2));

    public static final RegistryObject<Item> WOODEN_HAMMER = ITEMS.register("wooden_hammer",
            () -> new HammerItem(Tiers.WOOD, 2, 3,
                  new Item.Properties().fireResistant(), BlockTags.MINEABLE_WITH_PICKAXE,2, false, 2));

    public static final RegistryObject<Item> MAGNET = ITEMS.register("magnet",
            () -> new MagnetItem(new Item.Properties().fireResistant().stacksTo(1), 10));

    // Ore custom item
    public static final RegistryObject<Item> PINK = ITEMS.register("pink",
            () -> new Item(new Item.Properties().fireResistant().stacksTo(64)));

    // Bouncy ball's custom Ender pearls
    public static final RegistryObject<Item> BOUNCY_BALLS = ITEMS.register("bouncy_balls",
            () -> new BouncyBallsItem(new Item.Properties().fireResistant().stacksTo(1)));

    public static final RegistryObject<Item> BOUNCY_BALLS_PARTICLES = ITEMS.register("bouncy_balls_particles",
            () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));

    // Luck custom generator enchanted book
    public static final RegistryObject<Item> LUCK = ITEMS.register("luck",
            () -> new LuckItem(new Item.Properties().stacksTo(64).fireResistant(),
                    3, 3, 0, null, 0));

    public static final RegistryObject<Item> PICKAXE_LUCK = ITEMS.register("pickaxe_luck",
            () -> new LuckItem(new Item.Properties().stacksTo(64).fireResistant(),
                    1, 2, 10, EnchantmentCategory.DIGGER, 1));

    public static final RegistryObject<Item> WEAPON_LUCK = ITEMS.register("weapon_luck",
            () -> new LuckItem(new Item.Properties().stacksTo(64).fireResistant(),
                    1, 1, 6, EnchantmentCategory.WEAPON, 2));

    // Mccourse custom items
    public static final RegistryObject<Item> MCCOURSE_HAMMER = ITEMS.register("mccourse_hammer",
            () -> new MccourseHammerItem(ModToolTiers.PINK, 3, 1,
                  new Item.Properties().durability(10000).fireResistant(),
                    BlockTags.MINEABLE_WITH_PICKAXE, 2));

    public static final RegistryObject<Item> SPECIAL_METAL_DETECTOR = ITEMS.register("special_metal_detector",
            () -> new MetalDetectorItem(new Item.Properties().fireResistant().stacksTo(1),
                    ModTags.Blocks.SPECIAL_METAL_DETECTOR_VALUABLES));

    public static final RegistryObject<Item> VAULT = ITEMS.register("vault",
            () -> new VaultItem(new Item.Properties().fireResistant().stacksTo(1)));

    public static final RegistryObject<Item> DESTROYER = ITEMS.register("destroyer",
            () -> new DestroyerItem(new Item.Properties().fireResistant().stacksTo(1)));

    public static final RegistryObject<Item> FARMER = ITEMS.register("farmer",
            () -> new FarmerItem(new Item.Properties().fireResistant()));

    public static final RegistryObject<Item> RESTORE = ITEMS.register("restore",
            () -> new RestoreItem(new Item.Properties().fireResistant().stacksTo(64)));

    // MINER helmet
    public static final RegistryObject<Item> MINER_HELMET = ITEMS.register("miner_helmet",
            () -> new ModHelmetItem(ModArmorMaterials.MINER, ArmorItem.Type.HELMET, new Item.Properties().fireResistant()));

    // Custom trim
    public static final RegistryObject<Item> KAUPEN_SMITHING_TEMPLATE =
            ITEMS.register("kaupen_armor_trim_smithing_template",
            () -> SmithingTemplateItem.createArmorTrimTemplate(new ResourceLocation(MCCourseMod.MOD_ID, "kaupen")));

    // Level Charger Plus
    public static final RegistryObject<Item> LEVEL_CHARGER_PLUS = ITEMS.register("level_charger_plus",
            () -> new LevelChargerItem(new Item.Properties().fireResistant().stacksTo(64),
                    1, null));

    // Level Charger Minus
    public static final RegistryObject<Item> LEVEL_CHARGER_MINUS = ITEMS.register("level_charger_minus",
            () -> new LevelChargerItem(new Item.Properties().fireResistant().stacksTo(64),
                    -1, null));

    // Level Charger Plus Fortune
    public static final RegistryObject<Item> LEVEL_CHARGER_PLUS_FORTUNE =
            ITEMS.register("level_charger_plus_fortune",
            () -> new LevelChargerItem(new Item.Properties().fireResistant().stacksTo(64),
                    1, Enchantments.BLOCK_FORTUNE));

    // Level Charger Minus Fortune
    public static final RegistryObject<Item> LEVEL_CHARGER_MINUS_FORTUNE =
            ITEMS.register("level_charger_minus_fortune",
            () -> new LevelChargerItem(new Item.Properties().fireResistant().stacksTo(64),
                    -1, Enchantments.BLOCK_FORTUNE));

    // Mccourse Bottle
    public static final RegistryObject<Item> MCCOURSE_BOTTLE = ITEMS.register("mccourse_bottle",
            () -> new MccourseBottleItem(new Item.Properties().fireResistant().stacksTo(1),
                    100000, 1));

    // Infinite
    public static final RegistryObject<Item> INFINITE = ITEMS.register("infinite",
            () -> new InfiniteItem(new Item.Properties().stacksTo(64).fireResistant(), "Unbreakable"));

    // Lucky Bomb
    public static final RegistryObject<Item> LUCKY_BOMB = registerItem("lucky_bomb",
            () -> new InfiniteItem(new Item.Properties().stacksTo(64).fireResistant(), "LuckyBomb"));

    // Destroyer Tag
    public static final RegistryObject<Item> DESTROYER_UNBREAKABLE_TAG = registerItem("destroyer_unbreakable_tag",
            () -> new DestroyerTagItem(new Item.Properties().stacksTo(64).fireResistant(), "Unbreakable"));

    // Destroyer Lucky Bomb Tag
    public static final RegistryObject<Item> DESTROYER_LUCKY_BOMB_TAG = registerItem("destroyer_lucky_bomb_tag",
            () -> new DestroyerTagItem(new Item.Properties().stacksTo(64).fireResistant(), "LuckyBomb"));

    // Ultra Compactor
    public static final RegistryObject<Item> ULTRA_COMPACTOR = ITEMS.register("ultra_compactor",
            () -> new UltraCompactorItem(new Item.Properties().fireResistant().stacksTo(1).fireResistant(),
                  true, ModTags.Items.ULTRA_COMPACTOR_ITEMS, ModTags.Items.ULTRA_COMPACTOR_RESULT));

    // Pink Ultra Compactor
    public static final RegistryObject<Item> PINK_ULTRA_COMPACTOR = ITEMS.register("pink_ultra_compactor",
            () -> new UltraCompactorItem(new Item.Properties().fireResistant().stacksTo(1).fireResistant(),
                  false, ModTags.Items.PINK_ULTRA_COMPACTOR_ITEMS, ModTags.Items.PINK_ULTRA_COMPACTOR_RESULT));

    // Mccourse Fishing Rod
    public static final RegistryObject<Item> MCCOURSE_FISHING_ROD = ITEMS.register("mccourse_fishing_rod",
            () -> new MccourseFishingRodItem(new Item.Properties().durability(100).fireResistant()));

    // Growth
    public static final RegistryObject<Item> GROWTH = ITEMS.register("growth",
            () -> new GrowthItem(new Item.Properties().stacksTo(64).fireResistant()));

    // Miner Bow
    public static final RegistryObject<Item> MINER_BOW = ITEMS.register("miner_bow",
            () -> new MinerBowItem(new Item.Properties().fireResistant().durability(10000), 1, 3, true));

    // Torch Ball
    public static final RegistryObject<Item> TORCH_BALL = registerItem("torch_ball",
            () -> new TorchBallItem(new Item.Properties().fireResistant().stacksTo(1)));

    // Saturation Gem Effect
    public static final RegistryObject<Item> SATURATION_GEM_EFFECT = registerItem("saturation_gem_effect",
            () -> new GemEffectItem(new Item.Properties().fireResistant().stacksTo(1),
                                    MobEffects.SATURATION, 200, 0));

    // Phantom boots
    public static final RegistryObject<Item> PHANTOM_BOOTS = registerItem("phantom_boots",
            () -> new ModArmorItem(ModArmorMaterials.PHANTOM, ArmorItem.Type.BOOTS, new Item.Properties().fireResistant()));

    // CUSTOM ELYTRA - Diamond Elytra
    public static final RegistryObject<Item> DIAMOND_ELYTRA = registerItem("diamond_elytra",
            () -> new ElytraOverpower(new Item.Properties().fireResistant().durability(10000).rarity(Rarity.EPIC)));

    // Tomahawk
    public static final RegistryObject<Item> TOMAHAWK = registerItem("tomahawk",
            () -> new TomahawkItem(new Item.Properties().fireResistant().stacksTo(16)));

    // DEFAULT METHOD - Insert in MCCourseMod.java file
    public static void register(IEventBus eventBus) { ITEMS.register(eventBus); }

    // CUSTOM METHOD - Register custom items
    public static RegistryObject<Item> registerItem(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }
}