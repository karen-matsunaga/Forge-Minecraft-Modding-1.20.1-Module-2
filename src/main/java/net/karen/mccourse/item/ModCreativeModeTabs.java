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

                        // Stairs
                        output.accept(ModBlocks.ALEXANDRITE_STAIRS.get());

                        // Slabs
                        output.accept(ModBlocks.ALEXANDRITE_SLABS.get());

                        // Pressure Plate
                        output.accept(ModBlocks.ALEXANDRITE_PREASSURE_PLATE.get());

                        // Button
                        output.accept(ModBlocks.ALEXANDRITE_BUTTON.get());

                        // Fence and Fence Gate
                        output.accept(ModBlocks.ALEXANDRITE_FENCE.get());

                        output.accept(ModBlocks.ALEXANDRITE_FENCE_GATE.get());

                        // Wall block
                        output.accept(ModBlocks.ALEXANDRITE_WALL.get());

                        // Door block
                        output.accept(ModBlocks.ALEXANDRITE_DOOR.get());

                        // Trapdoor block
                        output.accept(ModBlocks.ALEXANDRITE_TRAPDOOR.get());

                        // Alexandrite tools
                        output.accept(ModItems.ALEXANDRITE_SWORD.get());
                        output.accept(ModItems.ALEXANDRITE_PICKAXE.get());
                        output.accept(ModItems.ALEXANDRITE_SHOVEL.get());
                        output.accept(ModItems.ALEXANDRITE_AXE.get());
                        output.accept(ModItems.ALEXANDRITE_HOE.get());

                        // Alexandrite paxel
                        output.accept(ModItems.ALEXANDRITE_PAXEL.get());


                    }).build());

    // Registry Creative Mode Tab
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
