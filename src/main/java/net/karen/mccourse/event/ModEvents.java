package net.karen.mccourse.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.command.ReturnHomeCommand;
import net.karen.mccourse.command.SetHomeCommand;
import net.karen.mccourse.effect.ModEffects;
import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.item.ModesPickaxe;
import net.karen.mccourse.item.custom.HammerItem;
import net.karen.mccourse.item.custom.ModesPickaxeItem;
import net.karen.mccourse.network.MccourseElevatorKeyInputMessage;
import net.karen.mccourse.network.ModNetworks;
import net.karen.mccourse.network.GlowingBlocksNetworkMessage;
import net.karen.mccourse.network.ServerHammerBlockRenderMessage;
import net.karen.mccourse.util.ModTags;
import net.karen.mccourse.villager.ModVillagers;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.ConfigCommand;
import org.joml.Matrix4f;
import org.lwjgl.glfw.GLFW;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Mod.EventBusSubscriber(modid = MCCourseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    // Don't be a jerk License - Done with the help of
    // https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java
    // CUSTOM EVENT - Hammer's tool
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>(); // Hammer's receive blocks range

    @SubscribeEvent
    public static void onHammerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer(); // Player is using Hammer tool
        ItemStack mainHandItem = player.getMainHandItem();
        BlockPos initalBlockPos = event.getPos();

        if (HARVESTED_BLOCKS.contains(initalBlockPos)) { return; }

        // If player destroyed a block with Hammer tool
        if (mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer) {
            int radius = hammer.getRadius(); // Radius declared on ModItems with HammerItem class
            // Player's position to break a block with Hammer tool
            for (BlockPos pos : HammerItem.getBlocksToBeDestroyed(radius, initalBlockPos, serverPlayer)) {
                if (pos == initalBlockPos ||
                        !hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos))) {
                    continue;
                }
                // Have to add them to a Set otherwise, the same code right here will get called for each block!
                HARVESTED_BLOCKS.add(pos); // Player destroyed block with Hammer tool
                serverPlayer.gameMode.destroyBlock(pos);
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    // Hammer Tick
    private static BlockPos lastSentPos = null;
    private static int tickDelay = 0;

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END || Minecraft.getInstance().player == null) { return; }
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        ItemStack held = player.getMainHandItem();
        HammerItem.clientTick();

        if (!(held.getItem() instanceof HammerItem)) {
            lastSentPos = null;
            return;
        }

        HitResult hit = mc.hitResult;
        if (hit == null || hit.getType() != HitResult.Type.BLOCK) { return; }

        BlockPos pos = ((BlockHitResult) hit).getBlockPos();

        if (!pos.equals(lastSentPos) && tickDelay-- <= 0) {
            lastSentPos = pos;
            tickDelay = 5;
            ModNetworks.PACKET_HANDLER.sendToServer(new ServerHammerBlockRenderMessage(pos));
        }
    }

    // Hammer Highlight Renderer blocks
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES) return;
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();

        HammerItem.renderHighlight(event.getPoseStack(), event.getCamera(), bufferSource);
        bufferSource.endBatch(); // Finish the drawing!
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
                    MCCourseMod.LOGGER.info("Sheep was hit with Alexandrite Axe by {}",
                            player.getName().getString());
                } else if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.DIAMOND) {
                    MCCourseMod.LOGGER.info("Sheep was hit with DIAMOND by {}",
                            player.getName().getString());
                } else {
                    MCCourseMod.LOGGER.info("Sheep was hit with something else by {}",
                            player.getName().getString());
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
            // Received KOHLRABI with Villager's level 1
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),
                    new ItemStack(ModItems.KOHLRABI.get(), 6), 10, 2, 0.02f));

            // Received KOHLRABI SEEDS with Villager's level 2
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),
                    new ItemStack(ModItems.KOHLRABI_SEEDS.get()), 3, 2, 0.02f));
        }

        // Villager's toolsmith profession
        if (event.getType() == VillagerProfession.TOOLSMITH) {
            // List of all trades
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // List of all trades that the player can trade
            // Received ALEXANDRITE PAXEL with Villager's level 3
            trades.get(3).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 12),
                    new ItemStack(ModItems.ALEXANDRITE_PAXEL.get(), 1),
                    2, 5, 0.06f));
        }

        // Custom Villager's soundmaster profession
        if (event.getType() == ModVillagers.SOUND_MASTER.get()) {
            // List of all trades
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // List of all trades that the player can trade
            // Received Sound Block with Villager's level 1
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 25),
                    new ItemStack(ModBlocks.SOUND_BLOCK.get(), 1), 2, 5, 0.06f));
        }
    }

    // CUSTOM EVENT - Custom Villager Wandering
    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        // List of all trades that the player can trade - Generic and Rare trades because not exist levels
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        // List of all generic and rare trades
        // Received KOHLRABI like Generic Trades
        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(Items.EMERALD, 2),
                new ItemStack(ModItems.KOHLRABI.get(), 6), 10, 2, 0.02f));

        // Received KOHLRABI SEEDS like Rare Trades
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

        // Player has tool with Rainbow enchantment
        int rainbowLevel = mainHandItem.getEnchantmentLevel(ModEnchantments.RAINBOW.get());

        // Player's tool doesn't have Rainbow enchantment
        if (!mainHandItem.isEnchanted() || rainbowLevel < 1) { return; }

        BlockState blockState = world.getBlockState(pos);
        BlockPos blockPos = BlockPos.containing(pos.getX(), pos.getY(), pos.getZ()); // Block position

        // Block (Key) / Block Tag (Value)
        Map<Block, TagKey<Block>> rainbowBlock = Map.of(Blocks.COAL_BLOCK, Tags.Blocks.ORES_COAL,
        Blocks.COPPER_BLOCK, Tags.Blocks.ORES_COPPER, Blocks.DIAMOND_BLOCK, Tags.Blocks.ORES_DIAMOND,
        Blocks.EMERALD_BLOCK, Tags.Blocks.ORES_EMERALD, Blocks.GOLD_BLOCK, Tags.Blocks.ORES_GOLD,
        Blocks.IRON_BLOCK, Tags.Blocks.ORES_IRON, Blocks.LAPIS_BLOCK, Tags.Blocks.ORES_LAPIS,
        Blocks.REDSTONE_BLOCK, Tags.Blocks.ORES_REDSTONE, Blocks.NETHERITE_BLOCK,
        Tags.Blocks.ORES_NETHERITE_SCRAP);

        for (Map.Entry<Block, TagKey<Block>> rainbowEntry : rainbowBlock.entrySet()) {
            if (rainbowLevel == 1 && blockState.is(rainbowEntry.getValue())) {
                world.setBlock(blockPos, rainbowEntry.getKey().defaultBlockState(), 3); // Create KEY block
                event.setCanceled(true); // Ore not break and replaced with block on rainbowOres
            }
        }
    }

    // Credits by Shadow of Fire - https://github.com/Shadows-of-Fire/Apotheosis/blob/1.20/LICENSE
    // Distributed under MIT
    // CUSTOM EVENT - More Ores custom enchantment - Using code with some modifications
    @SubscribeEvent
    public static void activatedMoreOresEnchantment(BlockEvent.BreakEvent event) {
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        Entity entity = event.getPlayer();

        if (!(entity instanceof LivingEntity livingEntity)) { return; } // Player is an entity

        ItemStack mainHandItem = livingEntity.getMainHandItem(); // Player has a tool on main hand

        // More Ores enchantment level
        int moreOresLevel = mainHandItem.getEnchantmentLevel(ModEnchantments.MORE_ORES.get());

        if (!mainHandItem.isEnchanted() || moreOresLevel < 1) { return; } // Player has More Ores enchantment

        // Check if the block is STONE's tags and has a small chance to drop ores
        BlockState blockState = world.getBlockState(pos);

        List<TagKey<Block>> ores = List.of(ModTags.Blocks.MORE_ORES_ONE_DROPS, ModTags.Blocks.MORE_ORES_TWO_DROPS,
        ModTags.Blocks.MORE_ORES_THREE_DROPS, ModTags.Blocks.MORE_ORES_FOUR_DROPS,
        ModTags.Blocks.MORE_ORES_FIVE_DROPS);

        if (world instanceof ServerLevel serverLevel) {  // Ores generated on world
            for (int i = 1; i < 2; i++) { // Number of random ores are generated by block mined on any position
                if (moreOresLevel < 5 && blockState.is(Blocks.STONE) && Math.random() < 0.1 ||
                        moreOresLevel == 5 && blockState.is(Blocks.NETHERRACK) && Math.random() < 0.01) {
                    // Create a new ItemEntity with the randomly ORES's tags on randomOre
                    ItemEntity entityToSpawn = new ItemEntity(serverLevel, pos.getX() + 0.5,
                            pos.getY() + 0.5, pos.getZ() + 0.5,
                            new ItemStack(Objects.requireNonNull(ForgeRegistries.BLOCKS.tags())
                                    .getTag(ores.get(moreOresLevel-1))
                                    .getRandomElement(RandomSource.create()).orElse(Blocks.AIR)));
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

            // Magnetic enchantment
            int magneticLevel = mainHandItem.getEnchantmentLevel(ModEnchantments.MAGNETIC.get());

            // Player has Magnetic custom enchantment
            if (!mainHandItem.isEnchanted() || magneticLevel < 1) { return; }

            event.setCanceled(true); // Prevents drop in the world = DEFAULT is dropped on the ground
            BlockPos pos = event.getPos(); // Block position = (X, Y, Z)

            // Blocks are generated on Player's inventory
            Block.getDrops(state, (ServerLevel) world, pos, null, player, mainHandItem)
                    .forEach(drop -> { if (!player.getInventory().add(drop)) {
                        player.drop(drop, false);
                    }}); // Blocks does added drop on Player's inventory
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
        // Auto Smelt and Fortune enchantment levels
        int autoSmeltLevel = mainHandItem.getEnchantmentLevel(ModEnchantments.AUTO_SMELT.get());
        int fortuneLevel = mainHandItem.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);

        if (!mainHandItem.isEnchanted() || autoSmeltLevel < 1) { return; } // Player has Auto Smelt enchantment

        BlockPos blockPos = BlockPos.containing(pos.getX(), pos.getY(), pos.getZ()); // Player x, y, and z coordinates

        // Player used tool
        if (!mainHandItem.getItem().isCorrectToolForDrops(world.getBlockState(blockPos))) { return; }

        // Check if there is a casting recipe for the block
        if (world instanceof Level level) {
            ItemStack smeltResult = level.getRecipeManager()
                    .getRecipeFor(RecipeType.SMELTING,
                            new SimpleContainer(
                                    new ItemStack(world.getBlockState(blockPos).getBlock())), level)
                    .map(recipe -> recipe.getResultItem(level.registryAccess()).copy())
                    .orElse(ItemStack.EMPTY);

            if (!smeltResult.isEmpty()) { // Has recipe
                int dropAmount = 1; // Only Auto Smelt enchantment
                // Fortune enchantment random drop amount
                if (fortuneLevel > 0) { dropAmount += level.random.nextInt(fortuneLevel + 1); }
                // Replaces the block with air and drops the molten item
                if (world instanceof ServerLevel serverLevel) {
                    for (int i = 0; i < dropAmount; i++) {
                        ItemEntity entityToSpawn = new ItemEntity(serverLevel, pos.getX() + 0.5,
                                pos.getY() + 0.5, pos.getZ() + 0.5, smeltResult);
                        serverLevel.addFreshEntity(entityToSpawn);
                    } // Auto Smelt item drops
                }
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            } else { // Not have recipe
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
        int GLOWING_EYES = 10; // Range of Glowing effect on mobs
        LivingEntity livingEntity = event.getEntity();

        if (!(livingEntity instanceof Player)) { return; } // Player is an entity

        ItemStack helmet = livingEntity.getItemBySlot(EquipmentSlot.HEAD); // Player has an item on helmet slot

        // Glowing Mobs enchantment level
        int glowingMobsLevel = helmet.getEnchantmentLevel(ModEnchantments.GLOWING_MOBS.get());

        // Player has a helmet inputted on slot and Glowing Mobs enchantment level
        if (!helmet.isEnchanted() || glowingMobsLevel < 1) { return; }

        // Key - Class || Value - Group tag name
        Map<Class<? extends LivingEntity>, String> entityTag = Map.of(Monster.class, "GlowingMonsterTag",
        Animal.class, "GlowingAnimalTag", AbstractVillager.class, "GlowingVillagerTag",
        WaterAnimal.class, "GlowingWaterAnimalTag", AmbientCreature.class, "GlowingAmbientCreatureTag",
        Allay.class, "GlowingAllayTag", AbstractGolem.class, "GlowingAbstractGolemTag",
        FlyingMob.class, "GlowingFlyingMobTag", EnderDragon.class, "GlowingEnderDragonTag",
        Slime.class, "GlowingSlimeTag"); // Entities groups -> Classes as tags

        for (Map.Entry<Class<? extends LivingEntity>, String> entry : entityTag.entrySet()) {
            ChatFormatting entitiesColor = switch (entry.getValue()) {
                // Monsters
                case "GlowingMonsterTag", "GlowingFlyingMobTag", "GlowingEnderDragonTag",
                     "GlowingSlimeTag" -> ChatFormatting.RED;
                // Flying entities
                case "GlowingAnimalTag", "GlowingAmbientCreatureTag", "GlowingAllayTag" -> ChatFormatting.BLUE;
                // Water animals
                case "GlowingWaterAnimalTag" -> ChatFormatting.YELLOW;
                // Villagers
                case "GlowingVillagerTag", "GlowingAbstractGolemTag" -> ChatFormatting.DARK_PURPLE;
                default -> ChatFormatting.WHITE; }; // Entities colors -> Each class represent with some color

            List<? extends LivingEntity> entities = livingEntity.level().getEntitiesOfClass(entry.getKey(),
                    livingEntity.getBoundingBox().inflate(GLOWING_EYES));
            PlayerTeam tag = ((Player) livingEntity).getScoreboard().getPlayerTeam(entry.getValue());

            // Added each entity on group with specif tag and color on entityTag and entityColors
            if (tag == null) {
                tag = ((Player) livingEntity).getScoreboard().addPlayerTeam(entry.getValue());
                tag.setColor(entitiesColor);
            }

            // Each entity received Glowing effect with specif color on entityColors
            for (LivingEntity entity : entities) {
                entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 1,
                        true, false, false));
                ((Player) livingEntity).getScoreboard().addPlayerToTeam(entity.getScoreboardName(), tag);
            }
        }
    }

    // CUSTOM EVENT - Custom Enchantment's tooltips
    @SubscribeEvent
    public static void enchantmentTooltipDescriptions(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

        if (!stack.isEmpty() && stack.isEnchanted() || stack.getItem() == Items.ENCHANTED_BOOK) {
            if (enchantments.isEmpty()) { return; }
            for (int i = 0; i < tooltip.size(); i++) {
                String raw = ChatFormatting.stripFormatting(tooltip.get(i).getString()); // Detected line
                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    int level = entry.getValue();
                    String expected = Component.translatable(enchantment.getDescriptionId()).getString();
                    // Raw equals expected line replace old tooltip to new tooltip
                    if (raw != null && raw.startsWith(expected)) {
                        ChatFormatting color = enchantment.isCurse() ? ChatFormatting.RED :
                                switch (enchantment.category) {
                                    case ARMOR, ARMOR_HEAD, ARMOR_CHEST, ARMOR_LEGS,
                                         ARMOR_FEET -> ChatFormatting.GOLD;
                                    case DIGGER -> ChatFormatting.DARK_PURPLE;
                                    case BOW, CROSSBOW, WEAPON -> ChatFormatting.DARK_RED;
                                    case TRIDENT -> ChatFormatting.AQUA;
                                    case WEARABLE -> ChatFormatting.GREEN;
                                    case BREAKABLE -> ChatFormatting.DARK_GREEN;
                                    case VANISHABLE -> ChatFormatting.RED;
                                    case FISHING_ROD -> ChatFormatting.YELLOW;
                                }; // Replace this line with custom styled version
                        boolean isCurse = enchantment.isCurse();
                        String descriptionValue = enchantment.getDescriptionId() + ".desc";

                        /* Enchantment Levels with Arabic numerals and Enchantment Descriptions with
                           JSON file -> I18n = en_us.json */
                        if (level > 0 || enchantment.getMaxLevel() > 0 || I18n.exists(descriptionValue)) {
                            MutableComponent name = Component.translatable(enchantment.getDescriptionId())
                                    .withStyle(Style.EMPTY.withColor(color).withBold(!isCurse).withItalic(isCurse))
                                    .append(CommonComponents.SPACE).append(Component.literal(String.valueOf(level)))
                                    .append(CommonComponents.NEW_LINE);

                            MutableComponent desc = Component.translatable(descriptionValue)
                                    .withStyle(Style.EMPTY.withColor(color).withBold(false)
                                            .withItalic(false));

                            // Number line of enchantment names and enchantment descriptions
                            tooltip.set(i, name.append(desc));
                        }
                        break;
                    }
                }
            }
        }
    }

    // Credits by Parlack - Pickaxe modes - https://www.youtube.com/watch?v=pBo1c3hM3b0
    // CUSTOM EVENT - Custom Modes Pickaxe event GUI - Using code with some modifications
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void eventHandler(RenderGuiOverlayEvent.Pre event) {
        int h = event.getWindow().getGuiScaledHeight();
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        int x = 10;
        int y = h - 30;

        if (player != null && player.getMainHandItem().getItem() instanceof ModesPickaxeItem modesPickaxe) {
                ModesPickaxe mode = modesPickaxe.getModeActual(); // Show text mode actual on screen
                Component modeText = Component.literal("Mode actual: ")
                        .setStyle(Style.EMPTY.withColor(0xFFAA00)
                        .applyFormat(ChatFormatting.BOLD)); // Gold color
                Component modeType = Component.literal(mode.toString().replace("_", " "))
                        .setStyle(Style.EMPTY.withColor(0xFF5555)
                        .applyFormat(ChatFormatting.BOLD)); // Red color

                // Renders text on overlay on same line
                // Mode Text
                event.getGuiGraphics().drawString(mc.font, modeText, x, y, 0xFFAA00, false);
                x += mc.font.width(modeText);
                // Mode Type
                event.getGuiGraphics().drawString(mc.font, modeType, x, y, 0xFF5555, false);

                // drawString method parameters:
                // - mc.font: The source object used to draw the text.
                // - modeText: The text component to be drawn.
                // - x: X position when the text will be drawn on screen.
                // - y: Y position when the text will be drawn on screen.
                // - 0x0000FF: Text color on hexadecimal format.
                // - false: Boolean that indicates if the text should have a shadow (false = without shadow).
        }
    }

    // Active Fly with Item
    @SubscribeEvent
    public static void flyEffect(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide) return;
        boolean hasArmor = player.getItemBySlot(EquipmentSlot.HEAD).is(ModTags.Items.HELMET_FLY) &&
                player.getItemBySlot(EquipmentSlot.CHEST).is(ModTags.Items.CHESTPLATE_FLY) &&
                player.getItemBySlot(EquipmentSlot.LEGS).is(ModTags.Items.LEGGINGS_FLY) &&
                player.getItemBySlot(EquipmentSlot.FEET).is(ModTags.Items.BOOTS_FLY); // Player used FULL ARMOR
        boolean hasFlyEffect = player.hasEffect(ModEffects.FLY_EFFECT.get()) ||
                player.hasEffect(ModEffects.OVERPOWER_FLY_EFFECT.get()); // Player has FLY EFFECT
        if (hasArmor || hasFlyEffect) { // Player has FULL ARMOR or FLY EFFECT
            if (!player.getAbilities().mayfly) {
                player.getAbilities().mayfly = true;
                player.onUpdateAbilities();
            }
        }
        else {
            if (player.getAbilities().mayfly && !player.isCreative()) { // Player hasn't FULL ARMOR or FLY EFFECT
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
    }

    // Credits by Parlack - Xray - World Renderer - https://www.youtube.com/watch?v=vT4suvo0CAs
    // CUSTOM EVENT - Glowing Blocks xray custom enchantment - Using code with some modifications
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) { // Player is on world
        if (!event.getEntity().level().isClientSide()) {
            GlowingBlocksNetworkMessage.SyncedSavedData mapData =
                    GlowingBlocksNetworkMessage.MapVariables.get(event.getEntity().level());
            GlowingBlocksNetworkMessage.SyncedSavedData worldData =
                    GlowingBlocksNetworkMessage.WorldVariables.get(event.getEntity().level());
            if (mapData != null) {
                ModNetworks.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() ->
                                (ServerPlayer) event.getEntity()),
                        new GlowingBlocksNetworkMessage.SavedDataSyncMessage(mapData));
            }
            if (worldData != null) {
                ModNetworks.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() ->
                                (ServerPlayer) event.getEntity()),
                        new GlowingBlocksNetworkMessage.SavedDataSyncMessage(worldData));
            }
        }
    }

    // Player is on [Overworld, Nether, End, etc.]
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (!event.getEntity().level().isClientSide()) {
            GlowingBlocksNetworkMessage.SyncedSavedData worldData =
                    GlowingBlocksNetworkMessage.WorldVariables.get(event.getEntity().level());
            if (worldData != null) {
                ModNetworks.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() ->
                                (ServerPlayer) event.getEntity()),
                        new GlowingBlocksNetworkMessage.SavedDataSyncMessage(worldData));
            }
        }
    }

    // Render Level block shape variables
    private static BufferBuilder bufferBuilder = null;
    private static VertexBuffer vertexBuffer = null;
    private static VertexFormat.Mode mode = null;
    private static VertexFormat format = null;
    private static PoseStack poseStack = null;
    private static Matrix4f projectionMatrix = null;
    private static final boolean worldCoordinate = true;
    private static final Vec3 offset = Vec3.ZERO;
    private static int currentStage, targetStage = 0; // NONE: 0, SKY: 1, WORLD: 2
    private static final Map<TagKey<Block>, Integer> renderColors = Map.ofEntries(
    Map.entry(Tags.Blocks.ORES_COAL, 0xFFa9a9a9), Map.entry(Tags.Blocks.ORES_COPPER, 0xFFff8c00),
    Map.entry(Tags.Blocks.ORES_DIAMOND, 0xFF00FEFF), Map.entry(Tags.Blocks.ORES_EMERALD, 0xFF31c831),
    Map.entry(Tags.Blocks.ORES_GOLD, 0xFFffd700), Map.entry(Tags.Blocks.ORES_IRON, 0xFFd3d3d3),
    Map.entry(Tags.Blocks.ORES_LAPIS, 0xFF0000ff), Map.entry(Tags.Blocks.ORES_REDSTONE, 0xFFb30000),
    Map.entry(Tags.Blocks.ORES_NETHERITE_SCRAP, 0xFFD22CF8), Map.entry(ModTags.Blocks.MCCOURSE_ORES, 0xFFffc0eb));

    // Added all blocks shape with respective color
    private static void add(double x, double y, double z, int color) {
        if (bufferBuilder == null || !bufferBuilder.building()) { return; }
        if (format == DefaultVertexFormat.POSITION_COLOR) { bufferBuilder.vertex(x, y, z).color(color).endVertex(); }
    }

    // Building all block shape with respective mode and format
    private static boolean begin() {
        if (ModEvents.bufferBuilder == null || !ModEvents.bufferBuilder.building()) {
            clear();
            if (vertexBuffer == null) {
                ModEvents.mode = VertexFormat.Mode.DEBUG_LINES;
                ModEvents.format = DefaultVertexFormat.POSITION_COLOR;
                ModEvents.bufferBuilder = Tesselator.getInstance().getBuilder();
                ModEvents.bufferBuilder.begin(mode, format);
                return true;
            }
        }
        return false;
    }

    // Before creating the blocks, cleaning is done
    private static void clear() { if (vertexBuffer != null) {
        vertexBuffer.close(); vertexBuffer = null; }
    }

    // After creating the block
    private static void end() {
        if (bufferBuilder == null || !bufferBuilder.building()) { return; }
        if (vertexBuffer != null) { vertexBuffer.close(); }
        vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        vertexBuffer.bind();
        vertexBuffer.upload(bufferBuilder.end());
        VertexBuffer.unbind();
    }

    // Render block shape
    private static void renderShape(VertexBuffer vertexBuffer,
                                    double x, double y, double z, int color) {
        if (currentStage == 0 || currentStage != targetStage) { return; }
        if (poseStack == null || projectionMatrix == null) { return; }
        if (vertexBuffer == null) { return; }
        float i, j, k;
        if (worldCoordinate) {
            Vec3 pos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            i = (float) (x - pos.x());
            j = (float) (y - pos.y());
            k = (float) (z - pos.z());
        }
        else { i = (float) x; j = (float) y; k = (float) z; }
        poseStack.pushPose();
        poseStack.translate(i, j, k);
        poseStack.mulPose(Axis.YN.rotationDegrees(0));
        poseStack.mulPose(Axis.XP.rotationDegrees(0));
        poseStack.mulPose(Axis.ZN.rotationDegrees(0));
        poseStack.scale(1, 1, 1);
        poseStack.translate(offset.x(), offset.y(), offset.z());
        RenderSystem.setShaderColor((color >> 16 & 255) / 255.0F, (color >> 8 & 255) / 255.0F,
                (color & 255) / 255.0F, (color >>> 24) / 255.0F);
        vertexBuffer.bind();
        vertexBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix,
                Objects.requireNonNull(vertexBuffer.getFormat().hasUV(0)
                ? GameRenderer.getPositionTexColorShader() : GameRenderer.getPositionColorShader()));
        VertexBuffer.unbind();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    // Where render block shape on world
    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            currentStage = 1;
            RenderSystem.depthMask(false);
            renderShapes(event);
            RenderSystem.enableCull();
            RenderSystem.depthMask(true);
            currentStage = 0;
        } else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            currentStage = 2;
            RenderSystem.depthMask(true);
            renderShapes(event);
            RenderSystem.enableCull();
            RenderSystem.depthMask(true);
            currentStage = 0;
        }
    }

    // Created block shape with all blocks and colors defined on renderColors variable
    private static void renderShapes(RenderLevelStageEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        Entity entity = minecraft.gameRenderer.getMainCamera().getEntity();
        if (level != null) {
            poseStack = event.getPoseStack();
            projectionMatrix = event.getProjectionMatrix();
            Vec3 pos = entity.getPosition(event.getPartialTick());
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            int RadiusSquare = 8; // Horizontal and Vertical radius square
            for (int i = -RadiusSquare; i <= RadiusSquare; i++) {
                for (int xi = -RadiusSquare; xi <= RadiusSquare; xi++) {
                    for (int zi = -RadiusSquare; zi <= RadiusSquare; zi++) {
                        // Execute the desired statements within the square/cube
                        if (GlowingBlocksNetworkMessage.WorldVariables.get(level).xray) {
                            double posX = Math.floor(pos.x + xi);
                            double posY = Math.floor(pos.y + i);
                            double posZ = Math.floor(pos.z + zi);
                            BlockPos position = BlockPos.containing(posX, posY, posZ);
                            BlockState block = level.getBlockState(position);
                            int[][] cubeCoordinates = { {0,0,0},{1,0,0},{1,0,0},{1,0,1},{1,0,1},{0,0,1},
                                    {0,0,1},{0,0,0},{0,0,0},{0,1,0},{1,0,0},{1,1,0},
                                    {1,0,1},{1,1,1},{0,0,1},{0,1,1},{0,1,0},{1,1,0},
                                    {1,1,0},{1,1,1},{1,1,1},{0,1,1},{0,1,1},{0,1,0}};
                            for (Map.Entry<TagKey<Block>, Integer> entry : renderColors.entrySet()) {
                                if (block.is(entry.getKey())) {
                                    RenderSystem.depthMask(false);
                                    RenderSystem.disableDepthTest();
                                    if (begin()) {
                                        for (int[] c : cubeCoordinates) { add(c[0], c[1], c[2], entry.getValue()); }
                                        end();
                                    }
                                    if (currentStage == 2) {
                                        ModEvents.targetStage = 2;
                                        renderShape(vertexBuffer, posX, posY, posZ, entry.getValue());
                                        targetStage = 0;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableBlend();
            RenderSystem.enableDepthTest();
        }
    }

    // Xray items - Enchanted Helmet or Metal Detector
    @SubscribeEvent
    public static void activatedGlowingBlocksEnchantment(TickEvent.PlayerTickEvent event) {
        LevelAccessor world = event.player.level();
        ItemStack metal = event.player.getItemBySlot(EquipmentSlot.MAINHAND); // Player has used Metal Detector
        ItemStack helmet = event.player.getItemBySlot(EquipmentSlot.HEAD); // Player has used helmet

        // Player has used enchanted helmet
        int glowingBlocksLevel = helmet.getEnchantmentLevel(ModEnchantments.GLOWING_BLOCKS.get());
        // Player has used enchanted Helmet or Metal Detector
        if (event.phase == TickEvent.Phase.END) {
            GlowingBlocksNetworkMessage.WorldVariables.get(world).xray = helmet.isEnchanted() &&
                    glowingBlocksLevel > 0 || metal.is(ModItems.METAL_DETECTOR.get());
            // Update information player has enchanted Helmet or Metal Detector
            GlowingBlocksNetworkMessage.WorldVariables.get(world).syncData(world);
        }
    }

    // CUSTOM EVENT - Decapitator
    @SubscribeEvent
    public static void decapitatorBlock(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        if (level.isClientSide) { return; }

        BlockPos origin = event.getPos();
        BlockState originState = level.getBlockState(origin);
        Player player = event.getPlayer();

        if (!isLog(originState)) { return; } // Checks if the broken block is a log

        // Checks if you are using the correct tool
        if (!player.getMainHandItem().getItem().isCorrectToolForDrops(originState)) { return; }

        Set<BlockPos> connected = findConnectedLogsAndLeaves(level, origin);

        int logCount = 0;
        for (BlockPos pos : connected) {
            BlockState state = level.getBlockState(pos);
            if (isLog(state) || isLeaf(state)) {
                level.destroyBlock(pos, true); // Drop the blocks
                if (isLog(state)) logCount++;
            }
        }

        // Applies damage proportional to the amount of logs broken
        if (logCount > 0) {
            ItemStack tool = player.getMainHandItem();
            tool.hurtAndBreak(logCount, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        }
    }

    // Check if it is a log
    private static boolean isLog(BlockState state) { return state.is(BlockTags.LOGS); }

    // Check if it's a leaf
    private static boolean isLeaf(BlockState state) { return state.is(BlockTags.LEAVES); }

    // BFS (or DFS) search for connected logs and leaves
    private static Set<BlockPos> findConnectedLogsAndLeaves(Level level, BlockPos start) {
        Set<BlockPos> visited = new HashSet<>();
        Queue<BlockPos> toVisit = new ArrayDeque<>();
        toVisit.add(start);

        int maxDistance = 50; // Maximum search distance
        int maxHeight = 512; // Height limit (e.g. 10 blocks above and below)

        while (!toVisit.isEmpty()) {
            BlockPos pos = toVisit.poll();
            if (!visited.add(pos)) { continue; } // Already visited

            // Check if it is within the height limit
            if (Math.abs(pos.getY() - start.getY()) > maxHeight) { continue; }

            // Check the surrounding blocks (relative to the current position)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    for (int dz = -1; dz <= 1; dz++) {
                        BlockPos offset = pos.offset(dx, dy, dz);
                        if (visited.contains(offset)) { continue; }

                        if (offset.distManhattan(start) > maxDistance) { continue; } // Limit horizontal distance

                        BlockState neighborState = level.getBlockState(offset);
                        if (isLog(neighborState) || isLeaf(neighborState)) { toVisit.add(offset); }
                    }
                }
            }
        }
        return visited;
    }

    // CUSTOM EVENT - Block Fly custom enchantment
    @SubscribeEvent
    public static void activatedBlockFlyEnchantment(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity(); // Entity is a player
        // Player has Block Fly enchantment
        if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BLOCK_FLY.get(), player) > 0) {
            if ((!player.onGround() && !player.isUnderWater()) || player.isUnderWater()) {
                float oldSpeed = event.getOriginalSpeed(); // Old speed
                event.setNewSpeed(oldSpeed * 5); // New speed -> Fixed speed mining
            }
        }
    }

    // CUSTOM EVENT - Mccourse Elevator advanced block
    @SubscribeEvent
    public static void activatedMccourseElevatorOnKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) { return; }
        Player player = mc.player;

        // Checks if the player is over the elevator
        BlockPos pos = BlockPos.containing(player.getX(), player.getY() - 1, player.getZ());
        BlockState state = player.level().getBlockState(pos);
        if (state.getBlock() != ModBlocks.MCCOURSE_ELEVATOR.get()) { return; }

        // Detects jump
        if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_SPACE)) {
            ModNetworks.PACKET_HANDLER.sendToServer(new MccourseElevatorKeyInputMessage(true));
        }

        // Detects shift/crouch
        if (player.isShiftKeyDown()) {
            ModNetworks.PACKET_HANDLER.sendToServer(new MccourseElevatorKeyInputMessage(false));
        }
    }

    // CUSTOM EVENT - Protected Item custom enchantment
    private static final Map<UUID, List<ItemStack>> preservedItems = new HashMap<>(); // Map of Main hand + Items
    private static final Map<UUID, List<ItemStack>> preservedArmor = new HashMap<>(); // Map of Armor
    private static final Map<UUID, ItemStack> preservedOffhand = new HashMap<>(); // Map of Offhand
    private static final Map<UUID, int[]> preservedExperience = new HashMap<>(); // Map of Experience
    private static final Map<UUID, List<ItemStack>> preservedVault = new HashMap<>(); // Map of Vault items

    // Player normally drop all items when death
    @SubscribeEvent
    public static void activatedProtectedItemEnchantmentOnPlayerDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof Player player)) { return; } // Entity is player

        UUID playerUUID = player.getUUID(); // Player id
        List<ItemStack> vaultItems = new ArrayList<>(); // Added rest items on Vault item

        // Added all Inventory slots
        List<ItemStack> inventoryPreserve = new ArrayList<>(Collections.nCopies(36, ItemStack.EMPTY));
        // Added all Armor slots
        List<ItemStack> armorPreserve = new ArrayList<>(Collections.nCopies(4, ItemStack.EMPTY));
        ItemStack offhandPreserve = ItemStack.EMPTY; // Added Offhand slot

        int[] experienceData = new int[] { player.experienceLevel, Float.floatToIntBits(player.experienceProgress),
                player.totalExperience }; // Added player experience

        // Main inventory
        for (int i = 0; i < player.getInventory().items.size(); i++) {
            ItemStack inventory = player.getInventory().items.get(i);
            if (!inventory.isEmpty() && inventory.getEnchantmentLevel(ModEnchantments.PROTECTED_ITEM.get()) > 0) {
                inventoryPreserve.add(inventory.copy()); // Copy of item with Protected Item enchantment
                // Added on inventoryPreserve removes stack on Inventory slot
                player.getInventory().items.set(i, ItemStack.EMPTY);
            }
            else {
                vaultItems.add(inventory.copy()); // Copy of item without Protected Item enchantment
                // Added on vaultItems removes stack on Inventory slot
                player.getInventory().items.set(i, ItemStack.EMPTY);
            }
        }

        // Armor
        for (int i = 0; i < player.getInventory().armor.size(); i++) {
            ItemStack armor = player.getInventory().armor.get(i);
            if (!armor.isEmpty() && armor.getEnchantmentLevel(ModEnchantments.PROTECTED_ITEM.get()) > 0) {
                armorPreserve.set(i, armor.copy()); // Copy of item with Protected Item enchantment
                // Added on armorPreserve removes stack on Armor slot
                player.getInventory().armor.set(i, ItemStack.EMPTY);
            }
            else {
                vaultItems.add(armor.copy()); // Copy of item without Protected Item enchantment
                // Added on vaultItems removes stack on Inventory slot
                player.getInventory().armor.set(i, ItemStack.EMPTY);
            }
        }

        // Left hand or Offhand
        ItemStack offhand = player.getInventory().offhand.get(0);
        if (!offhand.isEmpty() && offhand.getEnchantmentLevel(ModEnchantments.PROTECTED_ITEM.get()) > 0) {
            offhandPreserve = offhand.copy(); // Copy of item with Protected Item enchantment
            // Added on offhandPreserve removes stack on Offhand slot
            player.getInventory().offhand.set(0, ItemStack.EMPTY);
        }
        else {
            vaultItems.add(offhand.copy()); // Copy of item without Protected Item enchantment
            // Added on vaultItems removes stack on Inventory slot
            player.getInventory().offhand.set(0, ItemStack.EMPTY);
        }

        // Save data
        preservedItems.put(playerUUID, inventoryPreserve);
        preservedArmor.put(playerUUID, armorPreserve);
        preservedOffhand.put(playerUUID, offhandPreserve);
        preservedExperience.put(playerUUID, experienceData);

        // Reset to prevent drop
        player.experienceLevel = 0;
        player.experienceProgress = 0;
        player.totalExperience = 0;

        // Get position and time
        BlockPos pos = player.blockPosition(); // Player X, Y and Z positions
        // Display PLAYER NAME, death (X, Y and Z) positions and TIME showing (Hours::Minutes::Seconds)
        String displayName = "Vault of " + player.getGameProfile().getName() +
                " [X: " + pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ() + "] - " +
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // Saves items from the Vault
        if (!vaultItems.isEmpty()) {
            ItemStack vaultItem = new ItemStack(ModItems.VAULT.get());
            CompoundTag vaultTag = new CompoundTag();
            ListTag itemListTag = new ListTag();
            // Create VaultItem with the items data
            for (ItemStack item : vaultItems) {
                CompoundTag itemTag = new CompoundTag();
                item.save(itemTag);
                itemListTag.add(itemTag);
            }

            // Added information on Vault item
            vaultTag.put("VaultItems", itemListTag);
            vaultTag.putString("DisplayName", displayName); // Save custom name in NBT
            vaultItem.setTag(vaultTag);
            vaultItem.setHoverName(Component.literal(displayName));

            // Temporarily saved for the clone event
            // Add directly to the new player's inventory in onClone()
            // Try adding to a free inventory slot
            preservedVault.computeIfAbsent(playerUUID, k -> new ArrayList<>()).add(vaultItem);
        }
    }

    // Player receives items after death
    @SubscribeEvent
    public static void activatedProtectedItemEnchantmentOnPlayerClone(PlayerEvent.Clone event) {
        if (!event.isWasDeath()) { return; } // Ensures that it only runs after death

        UUID playerUUID = event.getOriginal().getUUID(); // Get Player id
        Player newPlayer = event.getEntity(); // Entity is Player

        Player original = event.getOriginal(); // Old player
        BlockPos blockPos = original.blockPosition(); // Player position after death

        // Player death message on chat
        newPlayer.sendSystemMessage(Component.translatable(newPlayer.getGameProfile().getName() +
                " died at [X: " + blockPos.getX() + ", Y: " + blockPos.getY() + ", Z: " + blockPos.getZ() + "]"));

        // Restore items
        List<ItemStack> savedItems = preservedItems.remove(playerUUID); // Removed all Inventory slots saved
        if (savedItems != null) {
            for (ItemStack stack : savedItems) {
                newPlayer.getInventory().add(stack); // Added all items on Player inventory
            }
        }

        // Restore Armor
        List<ItemStack> savedArmor = preservedArmor.remove(playerUUID); // Removed all Armor slots saved
        if (savedArmor != null) {
            for (int i = 0; i < savedArmor.size(); i++) {
                if (!savedArmor.get(i).isEmpty()) {
                    newPlayer.getInventory().armor.set(i, savedArmor.get(i)); // Added all items on Armor slots
                }
            }
        }

        // Restore Offhand
        ItemStack offhand = preservedOffhand.remove(playerUUID); // Removed Offhand slot saved
        if (offhand != null && !offhand.isEmpty()) {
            newPlayer.getInventory().offhand.set(0, offhand); // Added item on Offhand slot
        }

        // Restore Experience
        int[] experienceData = preservedExperience.remove(playerUUID); // Removed all experience saved
        if (experienceData != null) {
            newPlayer.experienceLevel = experienceData[0]; // Restored experience level
            newPlayer.experienceProgress = Float.intBitsToFloat(experienceData[1]); // Restored experience progress
            newPlayer.totalExperience = experienceData[2]; // Restored total experience
        }

        // Restores items with Vault Item
        // Remove all items without Protected Item saved on Vault
        List<ItemStack> savedVault = preservedVault.remove(playerUUID);
        if (savedVault != null) {
            for (ItemStack item : savedVault) {
                newPlayer.getInventory().add(item); // Added item on Inventory slot
            }
        }
    }

    // CUSTOM EVENT - Crop replant
    // Custom method - Damage tool
    private static void damageToolIfHoe(ItemStack tool, Player player) {
        if (tool.getItem() instanceof HoeItem) {
            tool.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        }
    }

    // Crop automatically replant
    @SubscribeEvent
    public static void cropReplant(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getPlayer();

        // Only on server side and if player is not in creative mode
        if (level.isClientSide() || player.isCreative()) { return; }

        ItemStack heldItem = player.getMainHandItem();
        boolean isAllowed = heldItem.isEmpty() || heldItem.getItem() instanceof HoeItem;

        if (!isAllowed) { return; }

        Block block = state.getBlock();

        // Check if it is a plantation that can be replanted
        // Wheat, Carrot, Potato, Beet, etc.
        if (block instanceof CropBlock crop) {
            // Check if it is ripe
            if (crop.isMaxAge(state)) {
                // Cancels pattern break
                event.setCanceled(true);
                // Drops items as if they had broken normally
                Block.dropResources(state, level, pos, null, player, heldItem);
                // Replants the initial stage of the plantation
                level.setBlock(pos, crop.defaultBlockState(), 3);
                // Spend tool durability
                damageToolIfHoe(heldItem, player);
            }
        }
        // Nether Wart
        else if (block == Blocks.NETHER_WART) {
            if (state.getValue(NetherWartBlock.AGE) == 3) {
                event.setCanceled(true);
                Block.dropResources(state, level, pos, null, player, heldItem);
                level.setBlock(pos, Blocks.NETHER_WART.defaultBlockState(), 3);
                damageToolIfHoe(heldItem, player);
            }
        }
        // Sugar cane, Bamboo or Cactus
        else if (block == Blocks.SUGAR_CANE || block == Blocks.BAMBOO || block == Blocks.CACTUS) {
            BlockPos basePos = pos.below();
            BlockState baseState = level.getBlockState(basePos);
            // Only replant if there is correct soil below
            boolean canReplant = ((block == Blocks.SUGAR_CANE && (baseState.is(Blocks.GRASS_BLOCK)
                            || baseState.is(Blocks.DIRT) || baseState.is(Blocks.SAND))) ||
                            (block == Blocks.BAMBOO && baseState.is(Blocks.GRASS_BLOCK)) ||
                            (block == Blocks.CACTUS && baseState.is(Blocks.SAND)));

            if (canReplant) {
                // Check if the bottom block is the same and only break the top one
                BlockPos topPos = pos;
                while (level.getBlockState(topPos.above()).is(block)) { topPos = topPos.above(); }

                event.setCanceled(true);

                // Break everything from top to bottom, except the base (to replant)
                BlockPos current = topPos;
                while (!current.equals(basePos)) {
                    BlockState bState = level.getBlockState(current);
                    Block.dropResources(bState, level, current, null, player, heldItem);
                    level.setBlock(current, Blocks.AIR.defaultBlockState(), 3);
                    current = current.below();
                }
                // Replant the original block
                level.setBlock(pos, block.defaultBlockState(), 3);
                damageToolIfHoe(heldItem, player);
            }
        }
        // Mushroom, etc.
        else if (block == Blocks.RED_MUSHROOM || block == Blocks.BROWN_MUSHROOM ||
                block == Blocks.CRIMSON_FUNGUS || block == Blocks.WARPED_FUNGUS) {

            BlockPos basePos = pos.below();
            BlockState baseState = level.getBlockState(basePos);

            // Check if the soil is suitable
            boolean validSoil = baseState.is(Blocks.MYCELIUM) || baseState.is(Blocks.NETHERRACK) ||
                    baseState.is(Blocks.WARPED_NYLIUM) || baseState.is(Blocks.CRIMSON_NYLIUM);

            if (validSoil) {
                event.setCanceled(true);
                Block.dropResources(state, level, pos, null, player, heldItem);
                level.setBlock(pos, block.defaultBlockState(), 3);
                damageToolIfHoe(heldItem, player);
            }
        }
    }

    // CUSTOM EVENT - Anvil disenchanted event
    @SubscribeEvent
    public static void anvilDisenchant(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (ServerLevel world : event.getServer().getAllLevels()) {
                for (Entity entity : world.getAllEntities()) {
                    if (entity instanceof FallingBlockEntity fallingBlockEntity) {
                        BlockState state = fallingBlockEntity.getBlockState(); // Anvil state
                        BlockPos pos = fallingBlockEntity.blockPosition(); // Anvil position
                        // List of blocks that accept disenchanted items
                        List<Block> anvils = List.of(Blocks.ANVIL, Blocks.CHIPPED_ANVIL, Blocks.DAMAGED_ANVIL);

                        // Check if the dropped block is an anvil
                        if (!anvils.contains(state.getBlock())) { continue; }

                        BlockPos blockBelow = pos.below(); // The item is below the anvil

                        // Pick up the items on the ground below the anvil - small area below the anvil
                        List<ItemEntity> itemsBelow = world.getEntitiesOfClass(ItemEntity.class,
                                new AABB(pos.below()).inflate(0.25));

                        // Armors, tools or enchanted books drops
                        for (ItemEntity itemEntity : itemsBelow) {
                            ItemStack item = itemEntity.getItem(); // Get real item
                            // Get all enchantments of the item
                            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);

                            // Ignore if item has no enchantments
                            if (enchantments.isEmpty()) { return; }

                            // Only process if it's not a previously split book (to avoid infinite loop)
                            if (item.is(Items.ENCHANTED_BOOK) && enchantments.size() == 1) { return; }

                            // Drop an enchanted book with the enchantments of tool, armor, etc.
                            if (!item.is(Items.ENCHANTED_BOOK)) {
                                // It's a tool/armor/etc.
                                ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
                                // Added each enchantment found on tool, armor, etc.
                                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                    EnchantedBookItem.addEnchantment(enchantedBook,
                                            new EnchantmentInstance(entry.getKey(), entry.getValue()));
                                }
                                // Drop enchanted book
                                world.addFreshEntity(new ItemEntity(world, blockBelow.getX() + 0.5,
                                        blockBelow.getY() + 1, blockBelow.getZ() + 0.5, enchantedBook));

                                // Drop the base item without enchantments
                                ItemStack baseItem = item.copy();

                                // Set original item without enchantments
                                EnchantmentHelper.setEnchantments(Map.of(), baseItem);
                                baseItem.removeTagKey("StoredEnchantments");

                                // Clean up tag if empty
                                if (baseItem.hasTag() && Objects.requireNonNull(baseItem.getTag()).isEmpty()) {
                                    baseItem.setTag(null);
                                }

                                // Drop the original item
                                world.addFreshEntity(new ItemEntity(world, blockBelow.getX() + 0.5,
                                        blockBelow.getY() + 1, blockBelow.getZ() + 0.5, baseItem));
                            }
                            // Split each enchantment into individual books
                            else {
                                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                    ItemStack singleBook = new ItemStack(Items.ENCHANTED_BOOK);
                                    // Added an enchantment found on enchanted book
                                    EnchantedBookItem.addEnchantment(singleBook,
                                            new EnchantmentInstance(entry.getKey(), entry.getValue()));
                                    // Drop individual enchanted book
                                    world.addFreshEntity(new ItemEntity(world, blockBelow.getX() + 0.5,
                                            blockBelow.getY() + 1, blockBelow.getZ() + 0.5, singleBook));
                                }
                            }
                            // Remove the original item (to avoid reprocessing)
                            itemEntity.discard();
                        }
                    }
                }
            }
        }
    }
}