package net.karen.mccourse.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.command.ReturnHomeCommand;
import net.karen.mccourse.command.SetHomeCommand;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.item.custom.HammerItem;
import net.karen.mccourse.villager.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = MCCourseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    // Done with the help of https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java
    // Don't be a jerk License
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>(); // Hammer's tool EVENT
    @SubscribeEvent
    public static void onHammerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer(); // Player is using Hammer tool
        ItemStack mainHandItem = player.getMainHandItem();

        if(mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer) { // If player destroyed a block with Hammer tool
            BlockPos initalBlockPos = event.getPos();
            if (HARVESTED_BLOCKS.contains(initalBlockPos)) { return; }

            for (BlockPos pos : HammerItem.getBlocksToBeDestroyed(1, initalBlockPos, serverPlayer)) { // Player's position to break a block with Hammer tool
                if(pos == initalBlockPos || !hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) { continue; }

                // Have to add them to a Set otherwise, the same code right here will get called for each block!
                HARVESTED_BLOCKS.add(pos); // Player destroyed block with Hammer tool
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    // CUSTOM EVENT - Home's commands EVENT
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
        if(event.getEntity() instanceof Sheep) {
            if(event.getSource().getDirectEntity() instanceof Player player) {
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
}