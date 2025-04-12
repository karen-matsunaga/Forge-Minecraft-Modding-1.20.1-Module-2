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

                        // Alexandrite hammer
                        output.accept(ModItems.ALEXANDRITE_HAMMER.get());

                        // Alexandrite player's armor
                        output.accept(ModItems.ALEXANDRITE_HELMET.get());
                        output.accept(ModItems.ALEXANDRITE_CHESTPLATE.get());
                        output.accept(ModItems.ALEXANDRITE_LEGGINGS.get());
                        output.accept(ModItems.ALEXANDRITE_BOOTS.get());

                        // Alexandrite horse's armor
                        output.accept(ModItems.ALEXANDRITE_HORSE_ARMOR.get());

                        // Alexandrite custom lamp
                        output.accept(ModBlocks.ALEXANDRITE_LAMP.get());

                        // Custom data tablet
                        output.accept(ModItems.DATA_TABLET.get());

                        // Kohlrabi's seeds
                        output.accept(ModItems.KOHLRABI_SEEDS.get());

                        // Snapdragon's flower and potted flower
                        output.accept(ModBlocks.SNAPDRAGON.get());
                        output.accept(ModBlocks.POTTED_SNAPDRAGON.get());

                        // Bar Brawl's music disc
                        output.accept(ModItems.BAR_BRAWL_RECORD.get());

                        // Gem Empowering Station's custom block model
                        output.accept(ModBlocks.GEM_EMPOWERING_STATION.get());

                        // Radiation Staff's custom item model
                        output.accept(ModItems.RADIATION_STAFF.get());

                        // Alexandrite's bow
                        output.accept(ModItems.ALEXANDRITE_BOW.get());

                        // Alexandrite's shield
                        output.accept(ModItems.ALEXANDRITE_SHIELD.get());

                        // Soup Water's custom bucket and custom fluid
                        output.accept(ModItems.SOAP_WATER_BUCKET.get());

                        // Walnut's custom wood
                        output.accept(ModBlocks.WALNUT_LOG.get());
                        output.accept(ModBlocks.WALNUT_WOOD.get());
                        output.accept(ModBlocks.STRIPPED_WALNUT_LOG.get());
                        output.accept(ModBlocks.STRIPPED_WALNUT_WOOD.get());
                        output.accept(ModBlocks.WALNUT_PLANKS.get());
                        output.accept(ModBlocks.WALNUT_LEAVES.get());
                        output.accept(ModBlocks.WALNUT_SAPLING.get());

                        // Walnut's custom sign
                        output.accept(ModBlocks.WALNUT_SIGN.get());
                        output.accept(ModBlocks.WALNUT_HANGING_SIGN.get());

                        // Rhino's custom egg
                        output.accept(ModItems.RHINO_SPAWN_EGG.get());

                        // Dice's custom projectile item
                        output.accept(ModItems.DICE.get());

                        // Boats's custom boats
                        output.accept(ModItems.WALNUT_BOAT.get());
                        output.accept(ModItems.WALNUT_CHEST_BOAT.get());

                        // Colored custom block
                        output.accept(ModBlocks.COLORED_LEAVES.get());

                        // Cattail's custom crop
                        output.accept(ModItems.CATTAIL_SEEDS.get());
                        output.accept(ModItems.CATTAIL.get());

                        // Kaupen's custom portal
                        output.accept(ModBlocks.KAUPEN_PORTAL.get());

                        // Ruby's custom oxidizable block
                        output.accept(ModBlocks.RUBY_BLOCK.get());
                        output.accept(ModBlocks.RUBY_BLOCK_1.get());
                        output.accept(ModBlocks.RUBY_BLOCK_2.get());
                        output.accept(ModBlocks.RUBY_BLOCK_3.get());

                        output.accept(ModBlocks.WAXED_RUBY_BLOCK.get());
                        output.accept(ModBlocks.WAXED_RUBY_BLOCK_1.get());
                        output.accept(ModBlocks.WAXED_RUBY_BLOCK_2.get());
                        output.accept(ModBlocks.WAXED_RUBY_BLOCK_3.get());

                        // Kaupen's custom furnace
                        output.accept(ModBlocks.KAUPEN_FURNACE_BLOCK.get());

                        // My custom armor and tool
                        output.accept(ModItems.PINK_HELMET.get());
                        output.accept(ModItems.PINK_CHESTPLATE.get());
                        output.accept(ModItems.PINK_LEGGINGS.get());
                        output.accept(ModItems.PINK_BOOTS.get());

                        output.accept(ModItems.PINK_SWORD.get());
                        output.accept(ModItems.PINK_PICKAXE.get());
                        output.accept(ModItems.PINK_SHOVEL.get());
                        output.accept(ModItems.PINK_AXE.get());
                        output.accept(ModItems.PINK_HOE.get());

                        // My custom ender pearl
                        output.accept(ModItems.BOUNCY_BALLS.get());
                        output.accept(ModItems.BOUNCY_BALLS_PARTICLES.get());

                        // My custom Mining Paxel
                        output.accept(ModItems.MINING_PAXEL.get());

                        // My custom block
                        output.accept(ModBlocks.ENDER_PEARL_BLOCK.get());
                        output.accept(ModBlocks.NETHER_STAR_BLOCK.get());
                        output.accept(ModBlocks.GUNPOWDER_BLOCK.get());
                        output.accept(ModBlocks.ROTTEN_FLESH_BLOCK.get());

                        // My custom item
                        output.accept(ModItems.COPPER_HAMMER.get());
                        output.accept(ModItems.DIAMOND_HAMMER.get());
                        output.accept(ModItems.GOLD_HAMMER.get());
                        output.accept(ModItems.IRON_HAMMER.get());
                        output.accept(ModItems.NETHERITE_HAMMER.get());
                        output.accept(ModItems.PINK_HAMMER.get());
                        output.accept(ModItems.WOODEN_HAMMER.get());
                        output.accept(ModItems.STONE_HAMMER.get());

                        // My custom ore
                        output.accept(ModItems.PINK.get());
                        output.accept(ModBlocks.PINK_BLOCK.get());
                        output.accept(ModBlocks.PINK_ORE.get());
                        output.accept(ModBlocks.DEEPSLATE_PINK_ORE.get());
                        output.accept(ModBlocks.END_STONE_PINK_ORE.get());
                        output.accept(ModBlocks.NETHER_PINK_ORE.get());
                    }).build());

    // Registry Creative Mode Tab
    public static void register(IEventBus eventBus) { CREATIVE_MODE_TABS.register(eventBus); }
}