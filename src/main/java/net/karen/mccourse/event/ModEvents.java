package net.karen.mccourse.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.command.ReturnHomeCommand;
import net.karen.mccourse.command.SetHomeCommand;
import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.item.ModesPickaxe;
import net.karen.mccourse.item.custom.HammerItem;
import net.karen.mccourse.item.custom.ModesPickaxeItem;
import net.karen.mccourse.util.ModTags;
import net.karen.mccourse.villager.ModVillagers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.ConfigCommand;

import java.util.*;

@Mod.EventBusSubscriber(modid = MCCourseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    // Done with the help of https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java - Don't be a jerk License
    // CUSTOM EVENT - Hammer's tool
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>(); // Hammer's receive blocks range

    @SubscribeEvent
    public static void onHammerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer(); // Player is using Hammer tool
        ItemStack mainHandItem = player.getMainHandItem();

        if (mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer) { // If player destroyed a block with Hammer tool
            BlockPos initalBlockPos = event.getPos();
            if (HARVESTED_BLOCKS.contains(initalBlockPos)) { return; }

            for (BlockPos pos : HammerItem.getBlocksToBeDestroyed(1, initalBlockPos, serverPlayer)) { // Player's position to break a block with Hammer tool
                if (pos == initalBlockPos || !hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) { continue; }

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

    // CUSTOM EVENT - Rainbow custom enchantment
    @SubscribeEvent
    public static void activatedRainbowEnchantment(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        Entity entity = event.getPlayer();

        if (!(entity instanceof LivingEntity livingEntity)) { return; } // Player is an entity

        ItemStack mainHandItem = livingEntity.getMainHandItem(); // Player has tool on main hand
        int rainbowLevel = mainHandItem.getEnchantmentLevel(ModEnchantments.RAINBOW.get()); // Player has tool with Rainbow enchantment

        if (!mainHandItem.isEnchanted() || rainbowLevel < 1) { return; } // Player's tool doesn't have Rainbow enchantment

        BlockState blockState = world.getBlockState(pos);
        BlockPos blockPos = BlockPos.containing(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5); // Block position
        Block block = blockState.getBlock();

        Map<Block, Block> rainbowOres = new HashMap<>(); // KEY = ore AND VALUE = block ore
        rainbowOres.put(Blocks.COAL_ORE, Blocks.COAL_BLOCK);
        rainbowOres.put(Blocks.DEEPSLATE_COAL_ORE, Blocks.COAL_BLOCK);
        rainbowOres.put(Blocks.COPPER_ORE, Blocks.COPPER_BLOCK);
        rainbowOres.put(Blocks.DEEPSLATE_COPPER_ORE, Blocks.COPPER_BLOCK);
        rainbowOres.put(Blocks.DIAMOND_ORE, Blocks.DIAMOND_BLOCK);
        rainbowOres.put(Blocks.DEEPSLATE_DIAMOND_ORE, Blocks.DIAMOND_BLOCK);
        rainbowOres.put(Blocks.EMERALD_ORE, Blocks.EMERALD_BLOCK);
        rainbowOres.put(Blocks.DEEPSLATE_EMERALD_ORE, Blocks.EMERALD_BLOCK);
        rainbowOres.put(Blocks.GOLD_ORE, Blocks.GOLD_BLOCK);
        rainbowOres.put(Blocks.DEEPSLATE_GOLD_ORE, Blocks.GOLD_BLOCK);
        rainbowOres.put(Blocks.IRON_ORE, Blocks.IRON_BLOCK);
        rainbowOres.put(Blocks.DEEPSLATE_IRON_ORE, Blocks.IRON_BLOCK);
        rainbowOres.put(Blocks.LAPIS_ORE, Blocks.LAPIS_BLOCK);
        rainbowOres.put(Blocks.DEEPSLATE_LAPIS_ORE, Blocks.LAPIS_BLOCK);
        rainbowOres.put(Blocks.REDSTONE_ORE, Blocks.REDSTONE_BLOCK);
        rainbowOres.put(Blocks.DEEPSLATE_REDSTONE_ORE, Blocks.REDSTONE_BLOCK);
        rainbowOres.put(Blocks.ANCIENT_DEBRIS, Blocks.NETHERITE_BLOCK);

        // Check if the block is in rainbowOres and has Rainbow level 1
        if (rainbowLevel == 1 && rainbowOres.containsKey(block)) {
            world.setBlock(blockPos, rainbowOres.get(block).defaultBlockState(), 3); // Create VALUE block
            event.setCanceled(true); // Ore not break and replaced with block on rainbowOres
        }
    }

    // Credits by Shadow of Fire - https://github.com/Shadows-of-Fire/Apotheosis/blob/1.20/LICENSE - Distributed under MIT
    // CUSTOM EVENT - More Ores custom enchantment - Using code with some modifications
    @SubscribeEvent
    public static void activatedMoreOresEnchantment(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        Entity entity = event.getPlayer();

        if (!(entity instanceof LivingEntity livingEntity)) { return; } // Player is an entity

        ItemStack mainHandItem = livingEntity.getMainHandItem(); // Player has a tool on main hand
        int moreOresLevel = mainHandItem.getEnchantmentLevel(ModEnchantments.MORE_ORES.get()); // More Ores enchantment level

        if (!mainHandItem.isEnchanted() || moreOresLevel < 1) { return; } // Player has More Ores enchantment

        BlockState blockState = world.getBlockState(pos); // Check if the block is STONE's tags and has a small chance to drop ores

        List<TagKey<Block>> ores = List.of(ModTags.Blocks.MORE_ORES_ONE_DROPS, ModTags.Blocks.MORE_ORES_TWO_DROPS,
        ModTags.Blocks.MORE_ORES_THREE_DROPS, ModTags.Blocks.MORE_ORES_FOUR_DROPS, ModTags.Blocks.MORE_ORES_FIVE_DROPS);

        if (world instanceof ServerLevel serverLevel) {  // Ores generated on world
            for (int i = 1; i < 2; i++) { // Number of random ores are generated by block mined on any position
                if (moreOresLevel < 5 && blockState.is(Blocks.STONE) && Math.random() < 0.1 || moreOresLevel == 5 && blockState.is(Blocks.NETHERRACK) && Math.random() < 0.01) {
                    Block randomOre = Objects.requireNonNull(ForgeRegistries.BLOCKS.tags()).getTag(ores.get(moreOresLevel-1)).getRandomElement(RandomSource.create()).orElse(Blocks.AIR);
                    // Create a new ItemEntity with the randomly ORES's tags on randomOre
                    ItemEntity entityToSpawn = new ItemEntity(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(randomOre));
                    serverLevel.addFreshEntity(entityToSpawn); // All drops generated by block
                }
            }
        }
    }

    // Credits by Parlack - https://www.youtube.com/watch?v=YOLHn23HU5w
    // CUSTOM EVENT - Magnetic custom enchantment - Using code with some modifications
    @SubscribeEvent
    public static void activatedMagneticEnchantment(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockState state = event.getState(); // Block state = AIR
        if (player != null && state.getBlock() != Blocks.AIR) {
            Level world = player.level();
            ItemStack mainHandItem = player.getMainHandItem(); // Player has a tool on main hand
            int magneticLevel = mainHandItem.getEnchantmentLevel(ModEnchantments.MAGNETIC.get()); // Magnetic enchantment

            if (!mainHandItem.isEnchanted() || magneticLevel < 1) { return; } // Player has Magnetic enchantment

            event.setCanceled(true); // Prevents drop in the world = DEFAULT is dropped on the ground
            BlockPos pos = event.getPos(); // Block position = (X, Y, Z)

            // Player has Magnetic custom enchantment
            Block.getDrops(state, (ServerLevel) world, pos, null, player, mainHandItem) // Blocks are generated on Player's inventory
                    .forEach(drop -> { if (!player.getInventory().add(drop)) { player.drop(drop, false); }}); // Blocks does added drop on Player's inventory
            world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState()); // Remove broken block position
        }
    }

    // CUSTOM EVENT - Auto Smelt custom enchantment
    @SubscribeEvent
    public static void activatedAutoSmeltEnchantment(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        Entity entity = event.getPlayer();

        if (!(entity instanceof LivingEntity livingEntity)) { return; } // Player is an entity

        ItemStack mainHandItem = livingEntity.getMainHandItem(); // Player has a tool on main hand
        int autoSmeltLevel = mainHandItem.getEnchantmentLevel(ModEnchantments.AUTO_SMELT.get()); // Auto Smelt enchantment level
        int fortuneLevel = mainHandItem.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);

        if (!mainHandItem.isEnchanted() || autoSmeltLevel < 1) { return; } // Player has Auto Smelt enchantment

        BlockPos blockPos = BlockPos.containing(pos.getX(), pos.getY(), pos.getZ()); // Player x, y, and z coordinates

        if (!mainHandItem.getItem().isCorrectToolForDrops(world.getBlockState(blockPos))) { return; } // Player used tool

        // Check if there is a casting recipe for the block
        if (world instanceof Level level) {
            ItemStack smeltResult = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING, new SimpleContainer(new ItemStack(world.getBlockState(blockPos).getBlock())), level)
                    .map(recipe -> recipe.getResultItem(level.registryAccess()).copy())
                    .orElse(ItemStack.EMPTY);

            if (!smeltResult.isEmpty()) {
                int dropAmount = 1; // Only Auto Smelt enchantment
                if (fortuneLevel > 0) { dropAmount += level.random.nextInt(fortuneLevel + 1); } // Fortune enchantment random drop amount
                // Replaces the block with air and drops the molten item
                if (world instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < dropAmount; i++) {
                        ItemEntity entityToSpawn = new ItemEntity(serverLevel, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, smeltResult);
                        serverLevel.addFreshEntity(entityToSpawn);
                    } // Auto Smelt item drops
                }
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            } else {
                // Drop normal resources if there is no foundry revenue
                Block.dropResources(world.getBlockState(blockPos), world, blockPos, null);
                world.destroyBlock(blockPos, false);
            }
        }
    }

    // Credits by Lykrast - https://github.com/Lykrast/MeetYourFight/blob/master/LICENSE - Distributed under MIT
    // CUSTOM EVENT - Glowing Mobs's custom enchantment - Using code with some modifications
    @SubscribeEvent
    public static void activatedGlowingMobsEnchantment(LivingEvent event) {
        int GLOWING_EYES = 30; // Range of Glowing effect on mobs
        LivingEntity livingEntity = event.getEntity();

        if (!(livingEntity instanceof Player)) { return; } // Player is an entity

        ItemStack helmet = livingEntity.getItemBySlot(EquipmentSlot.HEAD); // Player has an item on helmet slot
        int glowingMobsLevel = helmet.getEnchantmentLevel(ModEnchantments.GLOWING_MOBS.get()); // Glowing Mobs enchantment level

        if (!helmet.isEnchanted() || glowingMobsLevel < 1) { return; } // Player has a helmet inputted on slot and Glowing Mobs enchantment level

        Map<Class<? extends LivingEntity>, String> entityTag = Map.of(Monster.class, "GlowingMonsterTag",
        Animal.class, "GlowingAnimalTag", AbstractVillager.class, "GlowingVillagerTag",
        WaterAnimal.class, "GlowingWaterAnimalTag", AmbientCreature.class, "GlowingAmbientCreatureTag",
        Allay.class, "GlowingAllayTag", AbstractGolem.class, "GlowingAbstractGolemTag",
        FlyingMob.class, "GlowingFlyingMobTag", EnderDragon.class, "GlowingEnderDragonTag",
        Slime.class, "GlowingSlimeTag"); // Entities groups -> Classes as tags

        Map<String, ChatFormatting> entityColors = Map.of("GlowingMonsterTag", ChatFormatting.RED,
        "GlowingFlyingMobTag", ChatFormatting.RED, "GlowingEnderDragonTag", ChatFormatting.RED,
        "GlowingSlimeTag", ChatFormatting.RED, "GlowingAnimalTag", ChatFormatting.BLUE,
        "GlowingAmbientCreatureTag", ChatFormatting.DARK_BLUE, "GlowingAllayTag", ChatFormatting.DARK_BLUE,
        "GlowingWaterAnimalTag", ChatFormatting.YELLOW, "GlowingVillagerTag", ChatFormatting.DARK_PURPLE,
        "GlowingAbstractGolemTag", ChatFormatting.DARK_PURPLE); // Entities colors -> Each class represent with some color

        for (Map.Entry<Class<? extends LivingEntity>, String> entry : entityTag.entrySet()) {
            List<? extends LivingEntity> entities = livingEntity.level().getEntitiesOfClass(entry.getKey(), livingEntity.getBoundingBox().inflate(GLOWING_EYES));
            PlayerTeam tag = ((Player) livingEntity).getScoreboard().getPlayerTeam(entry.getValue());
            if (tag == null) { // Added each entity on group with specif tag and color on entityTag and entityColors
                tag = ((Player) livingEntity).getScoreboard().addPlayerTeam(entry.getValue());
                tag.setColor(entityColors.get(entry.getValue()));
            }
            for (LivingEntity entity : entities) { // Each entity received Glowing effect with specif color on entityColors
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 1, true, false, false));
                ((Player) livingEntity).getScoreboard().addPlayerToTeam(entity.getScoreboardName(), tag);
            }
        }
    }

    // CUSTOM EVENT - Custom Item Tooltip with custom enchantment description
    @SubscribeEvent
    public static void enchantmentDescription(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack(); // Player has a tool
        List<Component> tooltip = event.getToolTip(); // Player has a tooltip description on tool

        if (!itemStack.isEnchanted()) { return; } // Tool is enchanted

        Map<Enchantment, String> enchantDesc = Map.of(
        ModEnchantments.AUTO_SMELT.get(), "§a§lAuto Smelt = §r§aTransform all items that can be roasted on furnace",
        ModEnchantments.GLOWING_MOBS.get(), "§e§lGlowing Mobs = §r§eAnimals and enemies detector",
        ModEnchantments.LIGHTNING_STRIKER.get(), "§c§lLightning Striker = §r§cWhen player hit on animals or enemies appears lightning",
        ModEnchantments.MAGNETIC.get(), "§a§lMagnetic = §r§aWhen mined blocks automatically store on Player's inventory",
        ModEnchantments.MORE_ORES.get(), "§a§lMore Ores = §r§aIncrease amount drop of vanilla ores",
        ModEnchantments.RAINBOW.get(), "§a§lRainbow = §r§aReplace ore turned on block ore");

        if (Screen.hasShiftDown()) {
            for (Map.Entry<Enchantment, String> enchEntry : enchantDesc.entrySet()) {
                if (tooltip != null && itemStack.getEnchantmentLevel(enchEntry.getKey()) > 0) {
                    tooltip.add(CommonComponents.EMPTY);
                    tooltip.add(Component.literal(enchEntry.getValue())); // SHIFT pressed and tool has custom enchantments
                }
            }
        }
        else {
            tooltip.add(CommonComponents.EMPTY);
            tooltip.add(Component.literal("Press §e§lSHIFT§r to more information about enchantments")); // SHIFT not pressed
        }
    }

    // Credits by Parlack - Pickaxe modes - https://www.youtube.com/watch?v=pBo1c3hM3b0
    // CUSTOM EVENT - Custom Modes Pickaxe event GUI - Using code with some modifications
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void eventHandler(RenderGuiOverlayEvent.Pre event) {
        int h = event.getWindow().getGuiScaledHeight();
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;

        if (player != null) {
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof ModesPickaxeItem modesPickaxe) {
                ModesPickaxe mode = modesPickaxe.getModoAtual();

                // Show text mode actual on screen
                Component modeText = Component.literal("Mode actual: ").setStyle(Style.EMPTY.withColor(0xFFAA00)
                        .applyFormat(ChatFormatting.BOLD)); // Gold color
                Component modeType = Component.literal(mode.toString().replace("_", " ")).setStyle(Style.EMPTY.withColor(0xFF5555)
                        .applyFormat(ChatFormatting.BOLD)); // Red color

                // Renders text on overlay on same line
                int x = 10;
                int y = h - 30;
                event.getGuiGraphics().drawString(mc.font, modeText, x, y, 0xFFAA00, false); // Mode Text
                x += mc.font.width(modeText);
                event.getGuiGraphics().drawString(mc.font, modeType, x, y, 0xFF5555, false); // Mode Type

                // drawString method parameters:
                // - mc.font: The source object used to draw the text.
                // - modeText: The text component to be drawn.
                // - x: X position when the text will be drawn on screen.
                // - y: Y position when the text will be drawn on screen.
                // - 0x0000FF: Text color on hexadecimal format.
                // - false: Boolean that indicates if the text should have a shadow (false = without shadow).
            }
        }
    }
}