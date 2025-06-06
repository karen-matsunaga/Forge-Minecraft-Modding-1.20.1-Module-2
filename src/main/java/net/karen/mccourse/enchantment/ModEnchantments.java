package net.karen.mccourse.enchantment;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MCCourseMod.MOD_ID);

    // Registry all custom enchantments - Books and enchantment's levels are adding automatically in game
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

    // Eternal enchantment - Armor and Tools enchantment
    public static final RegistryObject<Enchantment> ETERNAL = ENCHANTMENTS.register("eternal",
            () -> new EternalEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    // Recover enchantment - Armor and Tools enchantment
    public static final RegistryObject<Enchantment> RECOVER = ENCHANTMENTS.register("recover",
            () -> new RecoverEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    // Elytra Boost enchantment - Elytra enchantment
    public static final RegistryObject<Enchantment> ELYTRA_BOOST = ENCHANTMENTS.register("elytra_boost",
            () -> new ElytraBoostEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_CHEST, EquipmentSlot.CHEST));

    // Peaceful Mobs enchantment - Leggings enchantment
    public static final RegistryObject<Enchantment> PEACEFUL_MOBS = ENCHANTMENTS.register("peaceful_mobs",
            () -> new PeacefulMobs(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS));

    // Experience enchantment - Armor and Tools enchantment
    public static final RegistryObject<Enchantment> XP_BOOST = ENCHANTMENTS.register("xp_boost",
            () -> new XpBoostEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentCategory.BREAKABLE, EquipmentSlot.values()));

    // Registry all enchantments in game
    public static void register(IEventBus eventBus) { ENCHANTMENTS.register(eventBus); }
}