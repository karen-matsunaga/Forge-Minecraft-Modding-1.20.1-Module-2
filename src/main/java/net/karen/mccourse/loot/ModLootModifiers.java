package net.karen.mccourse.loot;

import com.mojang.serialization.Codec;
import net.karen.mccourse.MCCourseMod;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifiers {
    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> LOOT_MODIFIERS_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MCCourseMod.MOD_ID);

    // Registry all loot modifiers in CODEC
    public static final RegistryObject<Codec<? extends IGlobalLootModifier>> ADD_ITEM =
            LOOT_MODIFIERS_SERIALIZERS.register("add_item", AddItemModifier.CODEC);


    // Registry all loot modifiers on Forge
    public static void register(IEventBus eventBus) {
        LOOT_MODIFIERS_SERIALIZERS.register(eventBus);
    }

}
