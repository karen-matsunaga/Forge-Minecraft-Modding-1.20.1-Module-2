package net.karen.mccourse.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.command.ReturnHomeCommand;
import net.karen.mccourse.command.SetHomeCommand;
import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.item.custom.HammerItem;
import net.karen.mccourse.villager.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.*;

@Mod.EventBusSubscriber(modid = MCCourseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    // Done with the help of https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java
    // Don't be a jerk License
    // CUSTOM EVENT - Hammer's tool
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>(); // Hammer's tool EVENT

    @SubscribeEvent
    public static void onHammerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer(); // Player is using Hammer tool
        ItemStack mainHandItem = player.getMainHandItem();

        if (mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer) { // If player destroyed a block with Hammer tool
            BlockPos initalBlockPos = event.getPos();
            if (HARVESTED_BLOCKS.contains(initalBlockPos)) {
                return;
            }

            for (BlockPos pos : HammerItem.getBlocksToBeDestroyed(1, initalBlockPos, serverPlayer)) { // Player's position to break a block with Hammer tool
                if (pos == initalBlockPos || !hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                    continue;
                }

                // Have to add them to a Set otherwise, the same code right here will get called for each block!
                HARVESTED_BLOCKS.add(pos); // Player destroyed block with Hammer tool
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    // CUSTOM EVENT - Home's commands
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) { // Register all custom commands
        new SetHomeCommand(event.getDispatcher());
        new ReturnHomeCommand(event.getDispatcher());
        ConfigCommand.register(event.getDispatcher());
    }

    // If player's dies is respawned where saved the set home
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getEntity().getPersistentData().putIntArray("mccourse.homepos",
                event.getOriginal().getPersistentData().getIntArray("mccourse.homepos"));
    }

    // CUSTOM EVENT - An event example that to show if player hit on sheep entity using specific items
    @SubscribeEvent
    public static void livingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Sheep) {
            if (event.getSource().getDirectEntity() instanceof Player player) {
                if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == ModItems.ALEXANDRITE_AXE.get()) {
                    MCCourseMod.LOGGER.info("Sheep was hit with Alexandrite Axe by " + player.getName().getString());
                } else if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.DIAMOND) {
                    MCCourseMod.LOGGER.info("Sheep was hit with DIAMOND by " + player.getName().getString());
                } else {
                    MCCourseMod.LOGGER.info("Sheep was hit with something else by " + player.getName().getString());
                }
            }
        }
    }

    // CUSTOM EVENT - Custom Villager's professions trade
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        // Villager's farm profession
        if (event.getType() == VillagerProfession.FARMER) {
            // List of all trades
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // List of all trades that the player can trade
            ItemStack stack = new ItemStack(ModItems.KOHLRABI.get(), 6);  // Received KOHLRABI with Villager's level 1
            int villagerLevel = 1;
            trades.get(villagerLevel).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2), stack, 10, 2, 0.02f));

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(  // Received KOHLRABI SEEDS with Villager's level 2
                    new ItemStack(Items.EMERALD, 5),
                    new ItemStack(ModItems.KOHLRABI_SEEDS.get()), 3, 2, 0.02f));
        }

        // Villager's toolsmith profession
        if (event.getType() == VillagerProfession.TOOLSMITH) {
            // List of all trades
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // List of all trades that the player can trade
            ItemStack stack = new ItemStack(ModItems.ALEXANDRITE_PAXEL.get(), 1); // Received ALEXANDRITE PAXEL with Villager's level 3
            int villagerLevel = 3;
            trades.get(villagerLevel).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12), stack, 2, 5, 0.06f));
        }

        // Custom Villager's soundmaster profession
        if (event.getType() == ModVillagers.SOUND_MASTER.get()) {
            // List of all trades
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // List of all trades that the player can trade
            ItemStack stack = new ItemStack(ModBlocks.SOUND_BLOCK.get(), 1); // Received ALEXANDRITE PAXEL with Villager's level 3
            int villagerLevel = 1;
            trades.get(villagerLevel).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 25), stack, 2, 5, 0.06f));
        }

    }

    // CUSTOM EVENT - Custom Villager Wandering
    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        // List of all trades that the player can trade - Generic and Rare trades because not exist levels
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        // List of all generic and rare trades
        ItemStack stack = new ItemStack(ModItems.KOHLRABI.get(), 6);
        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 2), stack, 10, 2, 0.02f));

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 5),
                new ItemStack(ModItems.KOHLRABI_SEEDS.get()), 3, 2, 0.02f));
    }

    // CUSTOM EVENT - More Ores custom enchantment
    @SubscribeEvent
    public static void activatedMoreOresEnchantment(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        Entity entity = event.getPlayer();

        if (!(entity instanceof LivingEntity livingEntity)) { return; } // Player is an entity

        ItemStack mainHandItem = livingEntity.getMainHandItem(); // Player has a tool on main hand
        int moreOresLevel = mainHandItem.getEnchantmentLevel(ModEnchantments.MORE_ORES.get()); // More Ores enchantment level
        int fortuneLevel = mainHandItem.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE); // Fortune enchantment level

        if (!mainHandItem.isEnchanted() || moreOresLevel < 1) { return; } // Player has More Ores enchantment

        int extraDrops = moreOresLevel + fortuneLevel; // Default is 1, but can to increase number of extra drops using More Ores and Fortune enchantments

        BlockState blockState = world.getBlockState(pos); // Check if the block is STONE's tags and has a small chance to drop ores

        // If player breaks a STONE's tags and random is small of 0.1 receive some random ores according to enchantment level
        if (blockState.is(Tags.Blocks.STONE) && Math.random() < 0.1 && world instanceof ServerLevel serverLevel) {  // Ores generated on world
            for (int i = 0; i < extraDrops; i++) { // Number of random ores are generated by block mined on any position
                Block randomOre = Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(Tags.Blocks.ORES).getRandomElement(RandomSource.create()).orElse(Blocks.AIR);

                // Create a new ItemEntity with the randomly ORES's tags on randomOre
                ItemEntity entityToSpawn = new ItemEntity(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(randomOre));
                serverLevel.addFreshEntity(entityToSpawn); // All drops generated by block
            }
        }
    }

    // Credits by Parlack - https://www.youtube.com/watch?v=YOLHn23HU5w
    // Used some lines code with modifications
    // CUSTOM EVENT - Magnetic custom enchantment
    @SubscribeEvent
    public static void activatedMagneticEnchantment(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        if (player != null && event.getState().getBlock() != Blocks.AIR) {
            Level world = player.level();
            ItemStack mainHandItem = player.getMainHandItem(); // Player has a tool on main hand
            int magneticLevel = mainHandItem.getEnchantmentLevel(ModEnchantments.MAGNETIC.get()); // Magnetic enchantment
            int moreOresEnchanted = mainHandItem.getEnchantmentLevel(ModEnchantments.MORE_ORES.get()); // More Ores enchantment

            if (!mainHandItem.isEnchanted() || magneticLevel < 1) { return; } // Player has Magnetic enchantment

            event.setCanceled(true);
            BlockPos pos = event.getPos();
            BlockState state = event.getState();

            if (moreOresEnchanted > 0) { // Player has More Ores enchantment level
                Block.getDrops(state, (ServerLevel) world, pos, null, player, mainHandItem) // Ores are generated on world
                        .forEach(drop -> {
                            if (player.getInventory().add(drop)) {
                                player.drop(drop, true); // Ores doesn't added drop on Player's inventory
                            }
                        });
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            } else { // Player has Magnetic enchantment level
                Block.getDrops(state, (ServerLevel) world, pos, null, player, mainHandItem) // Blocks are generated on Player's inventory
                        .forEach(drop -> {
                            if (!player.getInventory().add(drop)) {
                                player.drop(drop, false); // Blocks does added drop on Player's inventory
                            }
                        });
                world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }
        }
    }

}