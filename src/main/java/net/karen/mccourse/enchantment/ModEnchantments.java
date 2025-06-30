package net.karen.mccourse.enchantment;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class ModEnchantments {
    // Registry all custom enchantments - Books and enchantment's levels are adding automatically in game
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MCCourseMod.MOD_ID);

    // Lightning Striker enchantment - SWORD enchantment
    public static final RegistryObject<Enchantment> LIGHTNING_STRIKER = ENCHANTMENTS.register("lightning_striker",
            () -> new LightningStrikerEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    // Glowing Mobs enchantment - HELMET enchantment
    public static final RegistryObject<Enchantment> GLOWING_MOBS = ENCHANTMENTS.register("glowing_mobs",
            () -> new GlowingMobsEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));

    // Glowing Blocks enchantment - HELMET enchantment
    public static final RegistryObject<Enchantment> GLOWING_BLOCKS = ENCHANTMENTS.register("glowing_blocks",
            () -> new GlowingBlocksEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD));

    // More Ores enchantment - PICKAXE, HAMMER, PAXEL enchantment
    public static final RegistryObject<Enchantment> MORE_ORES = ENCHANTMENTS.register("more_ores",
            () -> new MoreOresEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));

    // Magnetic enchantment - PICKAXE, HAMMER, PAXEL enchantment
    public static final RegistryObject<Enchantment> MAGNETIC = ENCHANTMENTS.register("magnetic",
            () -> new MagneticEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));

    // Auto Smelt enchantment - PICKAXE, HAMMER, PAXEL enchantment
    public static final RegistryObject<Enchantment> AUTO_SMELT = ENCHANTMENTS.register("auto_smelt",
            () -> new AutoSmeltEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));

    // Rainbow enchantment - PICKAXE, HAMMER, PAXEL enchantment
    public static final RegistryObject<Enchantment> RAINBOW = ENCHANTMENTS.register("rainbow",
            () -> new RainbowEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));

    // Block Fly enchantment - PICKAXE, HAMMER, PAXEL enchantment
    public static final RegistryObject<Enchantment> BLOCK_FLY = ENCHANTMENTS.register("block_fly",
            () -> new BlockFlyEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND));

    // Eternal enchantment - ARMOR and TOOLS enchantment
    public static final RegistryObject<Enchantment> ETERNAL = ENCHANTMENTS.register("eternal",
            () -> new EternalEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    // Recover enchantment - ARMOR and TOOLS enchantment
    public static final RegistryObject<Enchantment> RECOVER = ENCHANTMENTS.register("recover",
            () -> new RecoverEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    // Elytra Boost enchantment - ELYTRA enchantment
    public static final RegistryObject<Enchantment> ELYTRA_BOOST = ENCHANTMENTS.register("elytra_boost",
            () -> new ElytraBoostEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));

    // Peaceful Mobs enchantment - LEGGINGS enchantment
    public static final RegistryObject<Enchantment> PEACEFUL_MOBS = ENCHANTMENTS.register("peaceful_mobs",
            () -> new PeacefulMobsEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));

    // Xp Boost enchantment - ARMOR and TOOLS enchantment
    public static final RegistryObject<Enchantment> XP_BOOST = ENCHANTMENTS.register("xp_boost",
            () -> new XpBoostEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    // Multiplier enchantment - TOOLS enchantment
    public static final RegistryObject<Enchantment> MULTIPLIER = ENCHANTMENTS.register("multiplier",
            () -> new MultiplierEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.MAINHAND));

    // Mobs Critical enchantment - SWORD enchantment
    public static final RegistryObject<Enchantment> MOBS_CRITICAL = ENCHANTMENTS.register("mobs_critical",
            () -> new MobsCriticalEnchantment(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND));

    // Immortal enchantment - ARMOR and TOOLS enchantment
    public static final RegistryObject<Enchantment> IMMORTAL = ENCHANTMENTS.register("immortal",
            () -> new ImmortalEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    // Accumulator enchantment - ARMOR and TOOLS enchantment
    public static final RegistryObject<Enchantment> ACCUMULATOR = ENCHANTMENTS.register("accumulator",
            () -> new AccumulatorEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    // Magnetism enchantment - LEGGINGS enchantment
    public static final RegistryObject<Enchantment> MAGNETISM = ENCHANTMENTS.register("magnetism",
            () -> new MagnetismEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));

    // Unlock enchantment - ARMOR and TOOLS enchantment
    public static final RegistryObject<Enchantment> UNLOCK = ENCHANTMENTS.register("unlock",
            () -> new UnlockEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    // Better Fishing enchantment - FISHING ROD enchantment
    public static final RegistryObject<Enchantment> BETTER_FISHING = ENCHANTMENTS.register("better_fishing",
            () -> new BetterFishingEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.FISHING_ROD, EquipmentSlot.MAINHAND));

    // Lightstring enchantment - BOW enchantment
    public static final RegistryObject<Enchantment> LIGHTSTRING = ENCHANTMENTS.register("lightstring",
            () -> new LightstringEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND));

    // Registry all enchantments in game
    public static void register(IEventBus eventBus) { ENCHANTMENTS.register(eventBus); }
}