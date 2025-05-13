package net.karen.mccourse.enchantment;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {
    public static final DeferredRegister<Enchantment> ENCHANTMENTS =
            DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MCCourseMod.MOD_ID);

    // Registry all custom enchantments - Books and enchantment's levels are adding automatically in game
    // Lightning Striker enchantment - SWORD enchantment
    public static final RegistryObject<Enchantment> LIGHTNING_STRIKER =
            ENCHANTMENTS.register("lightning_striker",
                    () -> new LightningStrikerEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON,
                            EquipmentSlot.MAINHAND));

    // Glowing Mobs enchantment - HELMET enchantment
    public static final RegistryObject<Enchantment> GLOWING_MOBS =
            ENCHANTMENTS.register("glowing_mobs",
                    () -> new GlowingMobsEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_HEAD,
                            EquipmentSlot.HEAD));

    public static final RegistryObject<Enchantment> GLOWING_BLOCKS =
            ENCHANTMENTS.register("glowing_blocks",
                    () -> new GlowingBlocksEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.ARMOR_HEAD,
                            EquipmentSlot.HEAD));

    // More Ores enchantment - PICKAXE, HAMMER, PAXEL enchantment
    public static final RegistryObject<Enchantment> MORE_ORES =
            ENCHANTMENTS.register("more_ores",
                    () -> new MoreOresEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER,
                            EquipmentSlot.MAINHAND));

    // Magnetic enchantment - PICKAXE, HAMMER, PAXEL enchantment
    public static final RegistryObject<Enchantment> MAGNETIC =
            ENCHANTMENTS.register("magnetic",
                    () -> new MagneticEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER,
                            EquipmentSlot.MAINHAND));

    // Auto Smelt enchantment - PICKAXE, HAMMER, PAXEL enchantment
    public static final RegistryObject<Enchantment> AUTO_SMELT =
            ENCHANTMENTS.register("auto_smelt",
                    () -> new AutoSmeltEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER,
                            EquipmentSlot.MAINHAND));

    // Rainbow enchantment - PICKAXE, HAMMER, PAXEL enchantment
    public static final RegistryObject<Enchantment> RAINBOW =
            ENCHANTMENTS.register("rainbow",
                    () -> new RainbowEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER,
                            EquipmentSlot.MAINHAND));

    // Block Fly enchantment - PICKAXE, HAMMER, PAXEL enchantment
    public static final RegistryObject<Enchantment> BLOCK_FLY =
            ENCHANTMENTS.register("block_fly",
                    () -> new BlockFlyEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.DIGGER,
                            EquipmentSlot.MAINHAND));

    // Protected Item enchantment - Armor and Tools enchantment
    public static final RegistryObject<Enchantment> PROTECTED_ITEM =
            ENCHANTMENTS.register("protected_item",
                    () -> new ProtectedItemEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.BREAKABLE,
                            EquipmentSlot.values()));

    // Overpower mending enchantment - Armor and Tools enchantment
    public static final RegistryObject<Enchantment> OVERPOWER_MENDING =
            ENCHANTMENTS.register("overpower_mending",
                    () -> new OverpowerMendingEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.BREAKABLE,
                            EquipmentSlot.values()));

    // Registry all enchantments in game
    public static void register(IEventBus eventBus) { ENCHANTMENTS.register(eventBus); }
}