package net.karen.mccourse.item;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
           DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MCCourseMod.MOD_ID);

    // Show all items, blocks in Creative Mode
    // First Creative Mode Tab
    public static final RegistryObject<CreativeModeTab> COURSE_TAB = CREATIVE_MODE_TABS.register("course_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ALEXANDRITE.get()))
                    .title(Component.translatable("creativetab.course_tab"))
                    .displayItems((displayParameters, output) -> {
                        // Items
                        output.accept(ModItems.ALEXANDRITE.get());
                        output.accept(ModItems.RAW_ALEXANDRITE.get());

                        // Custom Advanced Item
                        output.accept(ModItems.METAL_DETECTOR.get());

                        // Custom Advanced Block
                        output.accept(ModBlocks.SOUND_BLOCK.get());

                        // Blocks
                        output.accept(ModBlocks.ALEXANDRITE_BLOCK.get());
                        output.accept(ModBlocks.RAW_ALEXANDRITE_BLOCK.get());

                        // Ores
                        output.accept(ModBlocks.ALEXANDRITE_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_ALEXANDRITE_ORE.get());
                        output.accept(ModBlocks.END_STONE_ALEXANDRITE_ORE.get());
                        output.accept(ModBlocks.NETHER_ALEXANDRITE_ORE.get());

                        // Foods
                        output.accept(ModItems.KOHLRABI.get());

                        // Fuels
                        output.accept(ModItems.PEAT_BRICK.get());

                    }).build());

    // Second Creative Mode Tab
//    public static final RegistryObject<CreativeModeTab> COURSE_TAB_2 = CREATIVE_MODE_TABS.register("course_tab_2",
//            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.ALEXANDRITE.get()))
//                    .title(Component.translatable("creativetab.course_tab_2"))
//                    .displayItems((displayParameters, output) -> {
//                        // Items
//                        output.accept(ModItems.ALEXANDRITE.get());
//                        output.accept(ModItems.RAW_ALEXANDRITE.get());
//
//                        // Blocks
//                        output.accept(ModBlocks.ALEXANDRITE_BLOCK.get());
//                        output.accept(ModBlocks.RAW_ALEXANDRITE_BLOCK.get());
//
//                    }).build());


    // Registry Creative Mode Tab
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
