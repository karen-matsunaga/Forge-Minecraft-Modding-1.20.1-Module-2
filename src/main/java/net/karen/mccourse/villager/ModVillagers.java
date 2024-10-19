package net.karen.mccourse.villager;

import com.google.common.collect.ImmutableSet;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {
    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(ForgeRegistries.POI_TYPES, MCCourseMod.MOD_ID); // Registry all Poi Types

    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS =
            DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, MCCourseMod.MOD_ID); // Registry all Villager's professions

    // Registry all Poi and Villager's professions - Create custom villagers
    public static final RegistryObject<PoiType> SOUND_POI = POI_TYPES.register("sound_poi",
            () -> new PoiType(ImmutableSet.copyOf(ModBlocks.SOUND_BLOCK.get().getStateDefinition().getPossibleStates()),
            1, 1)); // Poi types

    public static final RegistryObject<VillagerProfession> SOUND_MASTER = VILLAGER_PROFESSIONS.register("soundmaster",
            () -> new VillagerProfession("soundmaster", x -> x.get() == SOUND_POI.get(), x -> x.get() == SOUND_POI.get(), ImmutableSet.of(), ImmutableSet.of(),
                    SoundEvents.VILLAGER_WORK_WEAPONSMITH)); // Villager's professions

    public static void register(IEventBus eventBus) {  // Registry all villagers on Forge
        POI_TYPES.register(eventBus);
        VILLAGER_PROFESSIONS.register(eventBus);
    }
}
