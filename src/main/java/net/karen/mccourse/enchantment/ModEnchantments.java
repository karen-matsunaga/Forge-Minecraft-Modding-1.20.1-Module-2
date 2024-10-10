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
    public static final RegistryObject<Enchantment> LIGHTNING_STRIKER =
            ENCHANTMENTS.register("lightning_striker",
                    () -> new LightningStrikerEnchantment(Enchantment.Rarity.COMMON, EnchantmentCategory.WEAPON,
                            EquipmentSlot.MAINHAND));

    // Registry all enchantments in game
    public static void register(IEventBus eventBus) {
        ENCHANTMENTS.register(eventBus);
    }
}
