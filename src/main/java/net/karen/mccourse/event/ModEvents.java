package net.karen.mccourse.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.command.*;
import net.karen.mccourse.effect.ModEffects;
import net.karen.mccourse.enchantment.*;
import net.karen.mccourse.item.*;
import net.karen.mccourse.item.custom.*;
import net.karen.mccourse.network.*;
import net.karen.mccourse.util.*;
import net.karen.mccourse.villager.*;
import net.minecraft.ChatFormatting;
import net.minecraft.client.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.*;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.*;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.*;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.*;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.item.*;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.item.trading.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.scores.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.item.ItemExpireEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.ExplosionEvent;
import net.minecraftforge.event.village.*;
import net.minecraftforge.eventbus.api.*;
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
    /* CUSTOM EVENT - Hammer's tool - Don't be a jerk License - Done with the help of
       https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java */
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>(); // Hammer's receive blocks range
    private static BlockPos lastSentPos = null; // Hammer Tick position
    private static int tickDelay = 0; // Hammer Tick delay

    @SubscribeEvent
    public static void onHammerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer(); // Player is using Hammer tool
        ItemStack mainHandItem = player.getMainHandItem();
        BlockPos initalBlockPos = event.getPos();
        if (HARVESTED_BLOCKS.contains(initalBlockPos)) { return; } // Different type of blocks
        // If player destroyed a block with Hammer tool
        if (mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer) {
            // Player's position to break a block with Hammer tool
            for (BlockPos pos : HammerItem.getBlocksToBeDestroyed(hammer.getRadius(), initalBlockPos, serverPlayer)) {
                boolean item = hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos));
                if (pos == initalBlockPos || !item) { continue; }
                // Have to add them to a Set otherwise, the same code right here will get called for each block!
                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos); // Player destroyed block with Hammer tool
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    @SubscribeEvent
    public static void onHammerTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (event.phase == TickEvent.Phase.END && player != null) {
            HitResult hit = mc.hitResult;
            HammerItem.clientTick();
            if (!(player.getMainHandItem().getItem() instanceof HammerItem)) { lastSentPos = null; return; }
            if (hit == null || hit.getType() != HitResult.Type.BLOCK) { return; } // Player hasn't HammerItem
            BlockPos pos = ((BlockHitResult) hit).getBlockPos();
            if (!pos.equals(lastSentPos) && tickDelay-- <= 0) { // Hammer render position
                lastSentPos = pos;
                tickDelay = 5;
                ModNetworks.PACKET_HANDLER.sendToServer(new ServerHammerBlockRenderMessage(pos));
            }
        }
    }

    @SubscribeEvent
    public static void onHammerRender(RenderLevelStageEvent event) { // Hammer Highlight Renderer blocks
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            HammerItem.renderHighlight(event.getPoseStack(), event.getCamera(), bufferSource); // Renderer block positions
            bufferSource.endBatch(); // Finish the drawing!
        }
    }

    // CUSTOM EVENT - Home's commands
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) { // Register all custom commands
        new SetHomeCommand(event.getDispatcher()); // SET HOME command
        new ReturnHomeCommand(event.getDispatcher()); // RETURN HOME command
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) { // If player dies is respawned where saved the SET HOME
        event.getEntity().getPersistentData().putIntArray("mccourse.homepos",
                event.getOriginal().getPersistentData().getIntArray("mccourse.homepos"));
    }

    // CUSTOM EVENT - An event example that to show if player hit on sheep entity using specific items
    private static void chat(String item, Player player) {
        // CUSTOM METHOD - Chat message on prompt
        MCCourseMod.LOGGER.info("Sheep was hit with {} by {}", item, player.getName().getString());
    }

    private static boolean item(Player player, Item item) {
        return player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == item; // CUSTOM METHOD - Used item
    }

    @SubscribeEvent
    public static void livingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Sheep) {
            if (event.getSource().getDirectEntity() instanceof Player player) {
                if (item(player, ModItems.ALEXANDRITE_AXE.get())) { chat("Alexandrite Axe", player); }
                else if (item(player, Items.DIAMOND)) { chat("Diamond", player); }
                else { chat("something else", player); }
            }
        }
    }

    // CUSTOM EVENT - Custom Villager's professions and Custom Villager Wandering trades
    private static VillagerTrades.ItemListing createTrade(List<Item> items,
                                                          List<Integer> levelCount, float multiplier) {
        return (pTrader, pRandom) -> new MerchantOffer(new ItemStack(items.get(0), levelCount.get(0)),
                new ItemStack(items.get(1), levelCount.get(1)), levelCount.get(2), levelCount.get(3), multiplier);
    }

    private static void normal(Int2ObjectMap<List<VillagerTrades.ItemListing>> trade,
                               List<Item> items, int level, List<Integer> levelCount, float multiplier) {
        trade.get(level).add(createTrade(items, levelCount, multiplier));
    }

    private static void wandering(List<VillagerTrades.ItemListing> trade,
                                  List<Item> items, List<Integer> levelCount, float multiplier) {
        trade.add(createTrade(items, levelCount, multiplier));
    }

    @SubscribeEvent
    public static void addNormalTrades(VillagerTradesEvent event) {
        Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades(); // List of all trades
        // Villager's FARM profession - List of all trades that the player can trade
        if (event.getType() == VillagerProfession.FARMER) {
            // Received KOHLRABI with Villager's level 1
            normal(trades, List.of(Items.EMERALD, ModItems.KOHLRABI.get()), 1, List.of(2, 6, 10, 2), 0.02f);
            // Received KOHLRABI SEEDS with Villager's level 2
            normal(trades, List.of(Items.EMERALD, ModItems.KOHLRABI_SEEDS.get()), 2, List.of(5, 1, 3, 2), 0.02f);
        }
        // Villager's TOOLSMITH profession - List of all trades that the player can trade
        if (event.getType() == VillagerProfession.TOOLSMITH) {
            // Received ALEXANDRITE PAXEL with Villager's level 3
            normal(trades, List.of(Items.EMERALD, ModItems.ALEXANDRITE_PAXEL.get()), 3, List.of(12, 1, 2, 5), 0.06f);
        }
        // Custom Villager's SOUNDMASTER profession - List of all trades that the player can trade
        if (event.getType() == ModVillagers.SOUND_MASTER.get()) {
            normal(trades, List.of(Items.EMERALD, ModBlocks.SOUND_BLOCK.get().asItem()), 1,
                    List.of(25, 1, 2, 5), 0.06f); // Received Sound Block with Villager's level 1
        }
    }

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        // List of all generic and rare trades that the player can trade because not exist levels
        List<VillagerTrades.ItemListing> generic = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rare = event.getRareTrades();
        // Received KOHLRABI like Generic Trades
        wandering(generic, List.of(Items.EMERALD, ModItems.KOHLRABI.get()), List.of(2, 6, 10, 2), 0.02f);
        // Received KOHLRABI SEEDS like Rare Trades
        wandering(rare, List.of(Items.EMERALD, ModItems.KOHLRABI_SEEDS.get()), List.of(5, 1, 3, 2), 0.02f);
        // Magic Book custom block
        wandering(rare, List.of(Items.EMERALD, ModBlocks.MAGIC_BOOK_BLOCK.get().asItem()), List.of(64, 1, 9, 10), 0.06f);
    }

    // CUSTOM EVENT - RAINBOW | AUTO SMELT | MORE ORES | MAGNETIC custom enchantments
    private static int enchant(ItemStack stack, Enchantment enchantment) {
        return stack.getEnchantmentLevel(enchantment);
    }

    public static boolean is(BlockState state, Block block,
                             float chance, ItemStack item, int type) {
        int moreOres = enchant(item, ModEnchantments.MORE_ORES.get());
        boolean hasEnchant = state.is(block) && (Math.random() < chance) && (moreOres > 6);
        switch (type) {
            case 1 -> hasEnchant = state.is(block) && (Math.random() < chance) && (moreOres < 6);
            case 2 -> hasEnchant = state.is(block) && (Math.random() < chance) && (moreOres == 6);
        }
        return hasEnchant;
    }

    private static void block(LevelAccessor world, BlockPos pos, Block block,
                              BlockEvent.BreakEvent event) {
        event.setCanceled(true);
        if (world instanceof ServerLevel serverLevel) { serverLevel.setBlockAndUpdate(pos, block.defaultBlockState()); }
        else { world.setBlock(pos, block.defaultBlockState(), 3); }
    }

    private static void dropXp(BlockState state, ServerLevel serverLevel, BlockPos pos, int fortune) {
        int exp = state.getExpDrop(serverLevel, serverLevel.random, pos, fortune, 0);
        if (exp > 0) { state.getBlock().popExperience(serverLevel, pos, exp); }
    }

    @SubscribeEvent
    public static void onBlockBreakWithCustomEnchantments(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        ItemStack tool = player.getMainHandItem();
        int fortune = enchant(tool, Enchantments.BLOCK_FORTUNE);
        int moreOres = enchant(tool, ModEnchantments.MORE_ORES.get());
        int multiplier = enchant(tool, ModEnchantments.MULTIPLIER.get());
        if (enchant(tool, ModEnchantments.RAINBOW.get()) > 0) { // * RAINBOW ENCHANTMENT *
            Map<Block, TagKey<Block>> rainbowMap = Map.of(Blocks.COAL_BLOCK, Tags.Blocks.ORES_COAL,
            Blocks.COPPER_BLOCK, Tags.Blocks.ORES_COPPER, Blocks.DIAMOND_BLOCK, Tags.Blocks.ORES_DIAMOND,
            Blocks.EMERALD_BLOCK, Tags.Blocks.ORES_EMERALD, Blocks.GOLD_BLOCK, Tags.Blocks.ORES_GOLD,
            Blocks.IRON_BLOCK, Tags.Blocks.ORES_IRON, Blocks.LAPIS_BLOCK, Tags.Blocks.ORES_LAPIS,
            Blocks.REDSTONE_BLOCK, Tags.Blocks.ORES_REDSTONE, Blocks.NETHERITE_BLOCK, Tags.Blocks.ORES_NETHERITE_SCRAP);
            for (Map.Entry<Block, TagKey<Block>> entry : rainbowMap.entrySet()) {
                // block(...) -> Blocks normal break || return; -> Other enchantments are not applied
                if (state.is(entry.getValue())) { block(world, pos, entry.getKey(), event); return; }
            }
        }
        if (world instanceof ServerLevel serverLevel) {
            boolean cancelVanillaDrop = false; // Adapt the drop according to the enchantment being true
            List<ItemStack> finalDrops = new ArrayList<>(); // Items caused by enchantments are stored in the list
            int oresFortune = serverLevel.random.nextInt(fortune + 1);
            if (moreOres > 0) { // * MORE ORES ENCHANTMENT *
                List<TagKey<Block>> oresTags = List.of(ModTags.Blocks.MORE_ORES_ONE_DROPS, ModTags.Blocks.MORE_ORES_TWO_DROPS,
                ModTags.Blocks.MORE_ORES_THREE_DROPS, ModTags.Blocks.MORE_ORES_FOUR_DROPS, ModTags.Blocks.MORE_ORES_FIVE_DROPS,
                ModTags.Blocks.MORE_ORES_SIX_DROPS);
                if (is(state, Blocks.STONE, 0.1f, tool, 1) || is(state, Blocks.NETHERRACK, 0.01f, tool, 2)) {
                    var blockTag = ForgeRegistries.BLOCKS.tags();
                    if (blockTag != null) { // Break block and ore chance drop
                        blockTag.getTag(oresTags.get(moreOres - 1)).getRandomElement(RandomSource.create()).ifPresent(block -> {
                                ItemStack drop = new ItemStack(block); // Increase ore drop with Multiplier enchantment
                                if (fortune > 0) { drop.setCount(drop.getCount() * (1 + oresFortune)); }
                                finalDrops.add(drop); });
                        cancelVanillaDrop = true;
                    }
                }
            }
            if (enchant(tool, ModEnchantments.AUTO_SMELT.get()) > 0) { // * AUTO SMELT ENCHANTMENT *
                serverLevel.getRecipeManager().getRecipeFor(RecipeType.SMELTING, new SimpleContainer(
                new ItemStack(state.getBlock())), serverLevel).ifPresent(recipe -> {
                    ItemStack result = recipe.getResultItem(serverLevel.registryAccess()).copy();
                    int drop = 1;
                    if (state.is(ModTags.Blocks.ALL_ORES) && fortune > 0) { drop += oresFortune; }
                    for (int i = 0; i < drop; i++) { finalDrops.add(result.copy()); } });
                cancelVanillaDrop = true;
            }
            if (multiplier > 1 && !finalDrops.isEmpty()) { // * MULTIPLIER ENCHANTMENT *
                List<ItemStack> multipliedDrops = new ArrayList<>();
                finalDrops.forEach(drop -> {
                    ItemStack multiplied = drop.copy(); // Copy ORIGINAL drop
                    if (drop.is(ModTags.Items.ALL_ORES_ITEMS)) {
                        multiplied.setCount(drop.getCount() * multiplier); // Duplicate drops with Multiplier
                        multipliedDrops.add(multiplied);
                    }
                    else { multipliedDrops.add(multiplied); }});
                finalDrops.clear(); // Remove the non-multiplied originals
                finalDrops.addAll(multipliedDrops); // Adds the multiplied values
            }
            if (enchant(tool, ModEnchantments.MAGNETIC.get()) > 0 && !state.isAir()) { // * MAGNETIC ENCHANTMENT *
                if (finalDrops.isEmpty()) { // FinalDrops empty list added all items on it is
                    finalDrops.addAll(Block.getDrops(state, serverLevel, pos, null, player, tool));
                }
                finalDrops.forEach(drop -> { // FinalDrops list added on Player's inventory
                    if (!player.getInventory().add(drop)) { player.drop(drop, false); }});
                block(serverLevel, pos, Blocks.AIR, event);
                dropXp(state, serverLevel, pos, fortune);
                return;
            }
            if (cancelVanillaDrop) { // FinalDrops list accumulate drop on world
                block(serverLevel, pos, Blocks.AIR, event);
                finalDrops.forEach(drop -> dropItem(serverLevel, pos, drop));
                dropXp(state, serverLevel, pos, fortune);
            }
        }
    }

    // Credits by Lykrast - https://github.com/Lykrast/MeetYourFight/blob/master/LICENSE - Distributed under MIT
    // CUSTOM EVENT - Glowing Mobs's custom enchantment - Using code with some modifications
    private static final Map<UUID, Boolean> glowingState = new HashMap<>(); // GLOWING MOBS state (ON/OFF)

    @SubscribeEvent
    public static void activatedGlowingMobsEnchantment(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (event.phase == TickEvent.Phase.END) {
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD); // Player has an item on HELMET slot
            UUID playerUUID = player.getUUID(); // Player UUID -> Detected GLOWING MOBS stage
            boolean isEnchanted = helmet.isEnchanted() && enchant(helmet, ModEnchantments.GLOWING_MOBS.get()) > 0;
            if (!isEnchanted) { glowingState.remove(playerUUID); } // Player hasn't GLOWING MOBS is disabled
            boolean current = glowingState.getOrDefault(playerUUID, false); // GLOWING MOBS default stage is FALSE
            if (KeyBinding.GLOWING_MOBS_KEY.consumeClick() && KeyBinding.GLOWING_MOBS_KEY.isDown()) { // Press [M] key input
                if (isEnchanted) { // Player has a HELMET inputted on slot and GLOWING MOBS enchantment level
                    boolean newState = !current; // Default stage is FALSE
                    glowingState.put(playerUUID, newState); // Adapted "newState" of "current" stage
                    messageStage(player, newState ? "Glowing Mobs: ON!" : "Glowing Mobs: OFF!",
                    newState ? ChatFormatting.GREEN : ChatFormatting.RED); // Toggle ON/OFF
                }
                else { messageStage(player, "Glowing Mobs: Enchanted helmet!", ChatFormatting.DARK_RED); } // Hasn't item
            }
            if (current && isEnchanted) {
                // Key (Color) -> Each group represent with some color. Value (Group tag name) -> Represent as Tag.
                Map<ChatFormatting, TagKey<EntityType<?>>> entitiesTag = Map.ofEntries(
                Map.entry(ChatFormatting.RED, ModTags.Entities.MONSTERS), // Monsters
                Map.entry(ChatFormatting.BLUE, ModTags.Entities.ANIMALS), // Animal and Flying entities
                Map.entry(ChatFormatting.YELLOW, ModTags.Entities.WATER_ANIMALS), // Water animals
                Map.entry(ChatFormatting.DARK_PURPLE, ModTags.Entities.VILLAGER)); // Villagers
                entitiesTag.forEach((color, tag) -> { // Added GLOWING effect for each GROUP
                    String teamName = tag.location().getPath();
                    List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class,
                    player.getBoundingBox().inflate(10), // Range of GLOWING effect on mobs
                    entity -> entity.getType().is(tag) && entity != player);
                    if (!entities.isEmpty()) { // Groups not empty
                        Scoreboard score = player.getScoreboard();
                        PlayerTeam team = score.getPlayerTeam(teamName);
                        // Added each entity on group with specif tag and color on entitiesTag
                        if (team == null) { team = score.addPlayerTeam(teamName); team.setColor(color); }
                        PlayerTeam finalTeam = team; // Each entity received GLOWING effect with specif color on entitiesTag
                        entities.forEach(entity -> { entity.addEffect(new MobEffectInstance(
                            MobEffects.GLOWING, 20, 1, true, false, false));
                            score.addPlayerToTeam(entity.getScoreboardName(), finalTeam);
                        });
                    }
                });
            }
        }
    }

    // CUSTOM EVENT - Enchantment tooltips
    private static MutableComponent description(String tooltip, ChatFormatting color,
                                                List<Boolean> curse) {
        return Component.translatable(tooltip).withStyle(Style.EMPTY.withColor(color).withBold(curse.get(0))
                .withItalic(curse.get(1)));
    }

    @SubscribeEvent
    public static void enchantmentTooltipDescriptions(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip();
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (!stack.isEmpty() && stack.isEnchanted() || stack.getItem() == Items.ENCHANTED_BOOK) {
            if (!enchantments.isEmpty()) {
                for (int i = 0; i < tooltip.size(); i++) {
                    String raw = ChatFormatting.stripFormatting(tooltip.get(i).getString()); // Detected old line
                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        Enchantment enchantment = entry.getKey();
                        int level = entry.getValue();
                        String expected = Component.translatable(enchantment.getDescriptionId()).getString();
                        if (raw != null && raw.startsWith(expected)) { // Raw has "expected" line replace old to new tooltip
                            boolean isCurse = enchantment.isCurse();
                            ChatFormatting color = isCurse ? ChatFormatting.RED :
                                switch (enchantment.category) { // Replace this line with custom styled version
                                    case ARMOR, ARMOR_HEAD, ARMOR_CHEST, ARMOR_LEGS, ARMOR_FEET -> ChatFormatting.GOLD;
                                    case DIGGER -> ChatFormatting.DARK_PURPLE; case TRIDENT -> ChatFormatting.AQUA;
                                    case BOW, CROSSBOW, WEAPON -> ChatFormatting.DARK_RED;
                                    case WEARABLE -> ChatFormatting.GREEN; case FISHING_ROD -> ChatFormatting.YELLOW;
                                    case BREAKABLE -> ChatFormatting.DARK_GREEN; case VANISHABLE -> ChatFormatting.RED; };
                            String enchant = enchantment.getDescriptionId();
                            String descriptionValue = enchant + ".desc"; // JSON file -> I18n = en_us.json
                            if (level > 0 || I18n.exists(descriptionValue)) {
                                MutableComponent name = description(enchant, color, List.of(!isCurse, isCurse))
                                        .append(CommonComponents.SPACE).append(Component.literal(String.valueOf(level)))
                                        .append(CommonComponents.NEW_LINE); // Enchantment Level with Arabic numeral
                                // Number line of enchantments and enchantment descriptions
                                tooltip.set(i, name.append(description(descriptionValue, color, List.of(false, false))));
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    // Credits by Parlack - Pickaxe modes - https://www.youtube.com/watch?v=pBo1c3hM3b0
    // CUSTOM EVENT - Custom Modes Pickaxe event GUI - Using code with some modifications
    private static void screen(RenderGuiOverlayEvent.Pre event, Minecraft mc, String message,
                               int x, int y, int color) {
        event.getGuiGraphics().drawString(mc.font, Component.literal(message).setStyle(Style.EMPTY.withColor(color)
                .applyFormat(ChatFormatting.BOLD)), x, y, color, false);
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void eventHandler(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        int x = 10;
        int y = event.getWindow().getGuiScaledHeight() - 30;
        if (player != null && player.getMainHandItem().getItem() instanceof ModesPickaxeItem modesPickaxe) {
            ModesPickaxe mode = modesPickaxe.getModeActual(); // Show text mode actual on screen
            String text = "Mode: "; // Renders text on overlay on same line
            screen(event, mc, text, x, y, 0xFFAA00);
            x += mc.font.width(text); // Mode Text
            screen(event, mc, mode.toString().replace("_", " "), x + 5, y, 0xFF5555); // Mode Type
            /* drawString method parameters:
               * mc.font: The source object used to draw the text; * modeText: The text component to be drawn;
               * X/Y: X/Y position when the text will be drawn on screen; * 0x0000FF: Text color on hexadecimal format;
               * FALSE: Boolean that indicates if the text should have a shadow (false = without shadow). */
        }
    }

    private static boolean slot(Player player, EquipmentSlot slot, TagKey<Item> item) {
        return player.getItemBySlot(slot).is(item); // Active Fly with Item
    }

    @SubscribeEvent
    public static void flyEffect(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if ((event.phase == TickEvent.Phase.END) && !player.level().isClientSide()) {
            Abilities abilities = player.getAbilities();
            boolean hasItem = (slot(player, EquipmentSlot.HEAD, ModTags.Items.HELMET_FLY) &&
            slot(player, EquipmentSlot.CHEST, ModTags.Items.CHESTPLATE_FLY) && // Player used FULL ARMOR or FLY EFFECT
            slot(player, EquipmentSlot.LEGS, ModTags.Items.LEGGINGS_FLY) &&
            slot(player, EquipmentSlot.FEET, ModTags.Items.BOOTS_FLY)) || player.hasEffect(ModEffects.FLY_EFFECT.get());
            if (hasItem) { if (!abilities.mayfly) { abilities.mayfly = true; } } // Player has FULL ARMOR or FLY EFFECT
            // Player hasn't FULL ARMOR or FLY EFFECT
            else { if (abilities.mayfly && !player.isCreative()) { abilities.mayfly = false; abilities.flying = false; } }
            player.onUpdateAbilities();
        }
    }

    // Credits by Parlack - Xray - World Renderer - https://www.youtube.com/watch?v=vT4suvo0CAs
    // CUSTOM EVENT - Glowing Blocks xray custom enchantment - Using code with some modifications
    private static void sendPacket(Player player,
                                   GlowingBlocksNetworkMessage.SyncedSavedData data) {
        ModNetworks.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() ->
                        (ServerPlayer) player), new GlowingBlocksNetworkMessage.SavedDataSyncMessage(data));
    }

    public static GlowingBlocksNetworkMessage.SyncedSavedData var(Player player, int type) {
        GlowingBlocksNetworkMessage.SyncedSavedData number = null;
        switch (type) { case 1 -> number = GlowingBlocksNetworkMessage.Map.get(player.level());
                        case 2 -> number = GlowingBlocksNetworkMessage.World.get(player.level()); }
        return number;
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity(); // Player is on world
        if (!player.level().isClientSide()) { sendPacket(player, var(player, 1)); sendPacket(player, var(player, 2)); }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity(); // Player is on [Overworld, Nether, End, etc.]
        if (!player.level().isClientSide()) { sendPacket(player, var(player, 2)); }
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
    private static int currentStage, targetStage = 0; // NONE: 0; SKY: 1; WORLD: 2.
    private static final Map<TagKey<Block>, Integer> renderColors = Map.ofEntries(
    Map.entry(Tags.Blocks.ORES_COAL, 0xFFa9a9a9), Map.entry(Tags.Blocks.ORES_COPPER, 0xFFff8c00),
    Map.entry(Tags.Blocks.ORES_DIAMOND, 0xFF00FEFF), Map.entry(Tags.Blocks.ORES_EMERALD, 0xFF31c831),
    Map.entry(Tags.Blocks.ORES_GOLD, 0xFFffd700), Map.entry(Tags.Blocks.ORES_IRON, 0xFFd3d3d3),
    Map.entry(Tags.Blocks.ORES_LAPIS, 0xFF0000ff), Map.entry(Tags.Blocks.ORES_REDSTONE, 0xFFb30000),
    Map.entry(Tags.Blocks.ORES_NETHERITE_SCRAP, 0xFFD22CF8), Map.entry(ModTags.Blocks.MCCOURSE_ORES, 0xFFffc0eb),
    Map.entry(ModTags.Blocks.SPECIAL_METAL_DETECTOR_VALUABLES, 0xFF157ccb));

    private static void add(double x, double y, double z, int color) {
        if (bufferBuilder == null || !bufferBuilder.building()) { return; } // Added all blocks shape with respective color
        if (format == DefaultVertexFormat.POSITION_COLOR) { bufferBuilder.vertex(x, y, z).color(color).endVertex(); }
    }

    private static boolean begin() { // Building all block shape with respective mode and format
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

    private static void clear() { // Before creating the blocks, cleaning is done
        if (vertexBuffer != null) { vertexBuffer.close(); vertexBuffer = null; }
    }

    private static void end() { // After creating the block
        if (bufferBuilder == null || !bufferBuilder.building()) { return; }
        if (vertexBuffer != null) { vertexBuffer.close(); }
        vertexBuffer = new VertexBuffer(VertexBuffer.Usage.STATIC);
        vertexBuffer.bind();
        vertexBuffer.upload(bufferBuilder.end());
        VertexBuffer.unbind();
    }

    private static void renderShape(VertexBuffer vertexBuffer, double x, double y, double z,
                                    int color) { // Render block shape
        if (currentStage == 0 || currentStage != targetStage) { return; }
        if (poseStack == null || projectionMatrix == null) { return; }
        if (vertexBuffer == null) { return; }
        float i, j, k;
        if (worldCoordinate) {
            Vec3 pos = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
            i = (float) (x - pos.x()); j = (float) (y - pos.y()); k = (float) (z - pos.z());
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
        ShaderInstance shader = vertexBuffer.getFormat().hasUV(0) ? GameRenderer.getPositionTexColorShader()
        : GameRenderer.getPositionColorShader();
        if (shader != null) { vertexBuffer.drawWithShader(poseStack.last().pose(), projectionMatrix, shader); }
        VertexBuffer.unbind();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
    }

    private static void stage(List<Integer> stage, List<Boolean> bool,
                              RenderLevelStageEvent event) { // CUSTOM METHOD - Render block shape on world
        currentStage = stage.get(0);
        RenderSystem.depthMask(bool.get(0));
        renderShapes(event);
        RenderSystem.enableCull();
        RenderSystem.depthMask(bool.get(1));
        currentStage = stage.get(1);
    }

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) { // Where render block shape on world
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) { stage(List.of(1, 0), List.of(false, true), event); }
        else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            stage(List.of(2, 0), List.of(true, true), event);
        }
    }

    private static void renderShapes(RenderLevelStageEvent event) {
        // Created block shape with all blocks and colors defined on renderColors variable
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
                        if (GlowingBlocksNetworkMessage.World.get(level).xray) {
                            double posX = Math.floor(pos.x + xi), posY = Math.floor(pos.y + i), posZ = Math.floor(pos.z + zi);
                            BlockPos position = BlockPos.containing(posX, posY, posZ);
                            int[][] cubeCoordinates = { {0,0,0},{1,0,0},{1,0,0},{1,0,1},{1,0,1},{0,0,1},
                            {0,0,1},{0,0,0},{0,0,0},{0,1,0},{1,0,0},{1,1,0},
                            {1,0,1},{1,1,1},{0,0,1},{0,1,1},{0,1,0},{1,1,0},
                            {1,1,0},{1,1,1},{1,1,1},{0,1,1},{0,1,1},{0,1,0}};
                            renderColors.forEach((key, value) -> {
                                if (level.getBlockState(position).is(key)) {
                                    RenderSystem.depthMask(false);
                                    RenderSystem.disableDepthTest();
                                    if (begin()) { for (int[] c : cubeCoordinates) { add(c[0], c[1], c[2], value); } end(); }
                                    if (currentStage == 2) {
                                        ModEvents.targetStage = 2;
                                        renderShape(vertexBuffer, posX, posY, posZ, value);
                                        targetStage = 0;
                                    }
                                }
                            });
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

    private static ItemStack has(Player player, EquipmentSlot slot) {
        return player.getItemBySlot(slot); // Xray items - Enchanted Helmet or Metal Detector
    }

    private static void change(GlowingBlocksNetworkMessage.World worldVar, boolean item,
                               LevelAccessor world) {
        if (worldVar.xray != item) { worldVar.xray = item; worldVar.syncData(world); }
    }

    private static void messageStage(Player player, String name, ChatFormatting color) {
        player.displayClientMessage(Component.translatable(name)
                .setStyle(Style.EMPTY.applyFormats(color, ChatFormatting.BOLD)), true);
    }

    @SubscribeEvent
    public static void activatedGlowingBlocksEnchantment(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        LevelAccessor world = player.level();
        ItemStack helmet = has(player, EquipmentSlot.HEAD);
        if (event.phase == TickEvent.Phase.END) {
            GlowingBlocksNetworkMessage.World worldVar = GlowingBlocksNetworkMessage.World.get(world);
            boolean hasItem = helmet.isEnchanted() && enchant(helmet, ModEnchantments.GLOWING_BLOCKS.get()) > 0 ||
                    has(player, EquipmentSlot.MAINHAND).is(ModItems.METAL_DETECTOR.get());
            if (KeyBinding.GLOWING_BLOCKS_KEY.isDown() && KeyBinding.GLOWING_BLOCKS_KEY.consumeClick()) {
                if (hasItem) { // Has enchanted HELMET or Metal Detector
                    boolean newState = !worldVar.xray; // Adapted "newState" of "worldVar.xray" stage
                    change(worldVar, newState, world);
                    messageStage(player, newState ? "Glowing Blocks: Activated" : "Glowing Blocks: Disabled",
                    newState ? ChatFormatting.GREEN : ChatFormatting.RED); // Toggle ON/OFF
                }
                else { // Hasn't item
                    messageStage(player, "Glowing Blocks: Enchanted helmet or Metal detector!", ChatFormatting.DARK_RED);
                }
            }
            if (!hasItem && worldVar.xray) { change(worldVar, false, world); } // Glowing Blocks disabled
        }
    }

    // CUSTOM EVENT - Decapitator
    private static boolean isBlock(BlockState state, TagKey<Block> block) {
        return state.is(block); // Check if it is a log or a leaf
    }

    @SubscribeEvent
    public static void decapitatorBlock(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        if (!level.isClientSide()) {
            BlockPos origin = event.getPos();
            BlockState originState = level.getBlockState(origin);
            Player player = event.getPlayer();
            ItemStack tool = player.getMainHandItem();
            if (isBlock(originState, BlockTags.LOGS)) { // Checks if the broken block is a log
                if (tool.getItem().isCorrectToolForDrops(originState)) { // Checks if you are using the correct tool
                    Set<BlockPos> visited = new HashSet<>(); // BFS (or DFS) search for connected logs and leaves
                    Queue<BlockPos> toVisit = new ArrayDeque<>();
                    toVisit.add(origin);
                    int maxDistance = 50; // Maximum search distance
                    int maxHeight = 512; // Height limit (e.g. 10 blocks above and below)
                    int logCount = 0;
                    while (!toVisit.isEmpty()) {
                        BlockPos pos = toVisit.poll();
                        // Check if it is already visited or within the height limit
                        if (!visited.add(pos) || Math.abs(pos.getY() - origin.getY()) > maxHeight) { continue; }
                        for (int dx = -1; dx <= 1; dx++) { // Check the surrounding blocks (relative to the current position)
                            for (int dy = -1; dy <= 1; dy++) {
                                for (int dz = -1; dz <= 1; dz++) {
                                    BlockPos offset = pos.offset(dx, dy, dz);
                                    // Limit horizontal distance
                                    if (visited.contains(offset) || offset.distManhattan(origin) > maxDistance) { continue; }
                                    BlockState neighborState = level.getBlockState(offset);
                                    if (isBlock(neighborState, BlockTags.LOGS) || isBlock(neighborState, BlockTags.LEAVES)) {
                                        toVisit.add(offset);
                                    }
                                }
                            }
                        }
                    }
                    for (BlockPos pos : visited) {
                        BlockState state = level.getBlockState(pos);
                        if (isBlock(state, BlockTags.LOGS) || isBlock(state, BlockTags.LEAVES)) {
                            level.destroyBlock(pos, true); // Drop the blocks
                            if (isBlock(state, BlockTags.LOGS)) { logCount++; } // Damage tool
                        }
                    }
                    if (logCount > 0) { // Applies damage proportional to the amount of logs broken
                        tool.hurtAndBreak(logCount, player, p -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
                    }
                }
            }
        }
    }

    // CUSTOM EVENT - Block Fly custom enchantment
    @SubscribeEvent
    public static void activatedBlockFlyEnchantment(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity(); // Entity is a player
        int efficiency = hasEnchant(Enchantments.BLOCK_EFFICIENCY, player);
        int blockFly = hasEnchant(ModEnchantments.BLOCK_FLY.get(), player);
        newSpeed(event, blockFly > 0, player, 5); // There is Block Fly enchantment -> OLD speed * NEW speed (5)
        // There is Block Fly and Efficiency enchantments -> OLD speed * (NEW speed (5) * efficiency level)
        newSpeed(event, blockFly > 0 && efficiency > 0, player, (5 + efficiency));
    }

    private static void newSpeed(PlayerEvent.BreakSpeed event, boolean hasEnchant,
                                 Player player, int value) {
        if (hasEnchant) { // CUSTOM METHOD - Set newSpeed adapt with Efficiency enchantment -> Fixed speed mining
            BlockState state = event.getState();
            if ((!player.onGround() && !player.isUnderWater()) || player.isUnderWater()) {
                event.setNewSpeed(event.getOriginalSpeed() * ((float) Math.sqrt(value) + 1));
            }
            if (state.is(ModTags.Blocks.BLOCK_FLY_BLOCK_SPEED)) {
                event.setNewSpeed(event.getOriginalSpeed() * 2.5F + ((float) Math.sqrt(value) + 1));
            }
        }
    }

    // CUSTOM EVENT - Mccourse Elevator advanced block
    private static void send(boolean response) {
        ModNetworks.PACKET_HANDLER.sendToServer(new MccourseElevatorKeyInputMessage(response)); // Client -> Server
    }

    @SubscribeEvent
    public static void activatedMccourseElevatorOnKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) { // Checks if the player is over the elevator
            BlockPos pos = BlockPos.containing(player.getX(), player.getY() - 1, player.getZ());
            if (player.level().getBlockState(pos).getBlock() == ModBlocks.MCCOURSE_ELEVATOR.get()) { // Detects JUMP or SHIFT
                if (InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_SPACE)) { send(true); }
                if (player.isShiftKeyDown()) { send(false); }
            }
        }
    }

    // CUSTOM EVENT - ETERNAL custom enchantment
    private static final Map<UUID, List<ItemStack>> preservedItems = new HashMap<>(); // Map of Main hand + Items
    private static final Map<UUID, List<ItemStack>> preservedArmor = new HashMap<>(); // Map of Armor
    private static final Map<UUID, List<ItemStack>> preservedOffhand = new HashMap<>(); // Map of Offhand
    private static final Map<UUID, int[]> preservedExperience = new HashMap<>(); // Map of Experience
    private static final Map<UUID, List<ItemStack>> preservedVault = new HashMap<>(); // Map of Vault items

    private static void setPreservedVault(NonNullList<ItemStack> type, List<ItemStack> store,
                                          List<ItemStack> vault) {
        for (int i = 0; i < type.size(); i++) {
            ItemStack inventory = type.get(i);
            // Copy of item WITH Eternal enchantment
            if (!inventory.isEmpty() && enchant(inventory, ModEnchantments.ETERNAL.get()) > 0) {
                store.set(i, inventory.copy());
            }
            // Copy of item WITHOUT Eternal enchantment
            else { vault.add(inventory.copy()); }
            // Added on typePreserve or vaultItems removes stack on Inventory, Armor and Offhand slots
            type.set(i, ItemStack.EMPTY);
        }
    }

    private static Component itemChatMessage(Player player, BlockPos pos, ChatFormatting color) {
        return Component.literal(player.getGameProfile().getName() + " died at [X: " +
        pos.getX() + ", Y: " + pos.getY() + ", Z: " + pos.getZ() + "] " + LocalTime.now().format(
        DateTimeFormatter.ofPattern("HH:mm:ss"))).withStyle(Style.EMPTY.withColor(color).withItalic(false));
    }

    private static void setRestoredVault(NonNullList<ItemStack> type, List<ItemStack> restored) {
        if (restored != null) { // Player receives items after death
            for (int i = 0; i < restored.size(); i++) {
                if (!restored.get(i).isEmpty()) { type.set(i, restored.get(i)); } // Added all items on Player inventory
            }
        }
    }

    @SubscribeEvent
    public static void activatedEternalEnchantmentOnPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) { // Entity is player
            UUID playerUUID = player.getUUID(); // Player UUID
            List<ItemStack> vaultItems = new ArrayList<>(); // Added rest items on Vault item
            // Added all Inventory slots, Armor slots and Offhand slot
            List<ItemStack> inventoryPreserve = new ArrayList<>(Collections.nCopies(36, ItemStack.EMPTY));
            List<ItemStack> armorPreserve = new ArrayList<>(Collections.nCopies(4, ItemStack.EMPTY));
            List<ItemStack> offhandPreserve = new ArrayList<>(Collections.nCopies(1, ItemStack.EMPTY));
            int[] experienceData = new int[] { player.experienceLevel, Float.floatToIntBits(player.experienceProgress),
            player.totalExperience }; // Added player experience
            // Get all items on Main inventory, Armor and Left hand or Offhand slots
            setPreservedVault(player.getInventory().items, inventoryPreserve, vaultItems);
            setPreservedVault(player.getInventory().armor, armorPreserve, vaultItems);
            setPreservedVault(player.getInventory().offhand, offhandPreserve, vaultItems);
            // Save items data
            preservedItems.put(playerUUID, inventoryPreserve);
            preservedArmor.put(playerUUID, armorPreserve);
            preservedOffhand.put(playerUUID, offhandPreserve);
            preservedExperience.put(playerUUID, experienceData);
            // Reset EXPERIENCE to prevent drop
            player.experienceLevel = 0;
            player.experienceProgress = 0;
            player.totalExperience = 0;
            // Display PLAYER NAME, Player death (X, Y and Z) positions and TIME showing (Hours::Minutes::Seconds)
            Component displayName = itemChatMessage(player, player.blockPosition(), ChatFormatting.GREEN);
            if (!vaultItems.isEmpty()) { // Saves items from the Vault
                ItemStack vaultItem = new ItemStack(ModItems.VAULT.get());
                CompoundTag vaultTag = new CompoundTag();
                ListTag itemListTag = new ListTag();
                vaultItems.forEach(item -> { CompoundTag itemTag = new CompoundTag(); item.save(itemTag);
                    itemListTag.add(itemTag); }); // Create VaultItem with the items data
                vaultTag.put("VaultItems", itemListTag); // Added information on Vault item
                vaultTag.putString("DisplayName", displayName.toString()); // Save custom name in NBT
                vaultItem.setTag(vaultTag);
                vaultItem.setHoverName(displayName);
                /* Temporarily saved for the clone event;
                   Add directly to the new Player's inventory in onClone()
                   Try adding to a free inventory slot. */
                preservedVault.computeIfAbsent(playerUUID, k -> new ArrayList<>()).add(vaultItem);
            }
        }
    }

    @SubscribeEvent
    public static void activatedEternalEnchantmentOnPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) { // Ensures that it only runs AFTER death
            UUID playerUUID = event.getOriginal().getUUID(); // Get Player UUID
            Player newPlayer = event.getEntity(); // Entity is Player -> After death
            Player original = event.getOriginal(); // Old player -> Before death
            BlockPos blockPos = original.blockPosition(); // Player position after death
            // Player death message on chat
            newPlayer.sendSystemMessage(itemChatMessage(newPlayer, blockPos, ChatFormatting.GOLD));
            // Restore all Inventory, Armor and Offhand saved slots
            setRestoredVault(newPlayer.getInventory().items, preservedItems.remove(playerUUID));
            setRestoredVault(newPlayer.getInventory().armor, preservedArmor.remove(playerUUID));
            setRestoredVault(newPlayer.getInventory().offhand, preservedOffhand.remove(playerUUID));
            // Restore Experience
            int[] experienceData = preservedExperience.remove(playerUUID); // Removed all experience saved
            if (experienceData != null) {
                newPlayer.experienceLevel = experienceData[0]; // Restored experience level
                newPlayer.experienceProgress = Float.intBitsToFloat(experienceData[1]); // Restored experience progress
                newPlayer.totalExperience = experienceData[2]; // Restored total experience
            }
            // Restores items with Vault Item
            List<ItemStack> savedVault = preservedVault.remove(playerUUID); // Remove all items WITHOUT Eternal saved on Vault
            // Added item on Inventory slot
            if (savedVault != null) { savedVault.forEach(item -> newPlayer.getInventory().add(item)); }
        }
    }

    // CUSTOM EVENT - Crop replant
    private static void damageToolIfHoe(ItemStack tool, Player player) {
        if (tool.getItem() instanceof HoeItem) { // CUSTOM METHOD - Damage tool
            tool.hurtAndBreak(1, player, (p) -> p.broadcastBreakEvent(InteractionHand.MAIN_HAND));
        }
    }

    private static void crop(Block block, BlockState state, Level level, BlockPos pos,
                             Player player, BlockEvent.BreakEvent event, ItemStack tool) {
        event.setCanceled(true); // Cancels pattern break
        Block.dropResources(state, level, pos, null, player, tool); // Drops items as if they had broken normally
        level.setBlock(pos, block.defaultBlockState(), 3); // Replants the initial stage of the plantation
        damageToolIfHoe(tool, player); // Spend tool durability
    }

    @SubscribeEvent
    public static void cropReplant(BlockEvent.BreakEvent event) { // Crop automatically replant
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getPlayer();
        if (!level.isClientSide() && !player.isCreative()) { // Only on server side and if player is not in creative mode
            ItemStack heldItem = player.getMainHandItem(); // Player has Hoe on main hand
            if (!(!heldItem.isEmpty() && heldItem.getItem() instanceof HoeItem)) { return; }
            Block block = state.getBlock();
            // Check if it is a plantation that can be replanted is Wheat, Carrot, Potato, Beet, etc.
            if (block instanceof CropBlock crop) {
                if (crop.isMaxAge(state)) { crop(crop, state, level, pos, player, event, heldItem); } // Check if it is ripe
            }
            else if (block.equals(Blocks.NETHER_WART) && state.getValue(NetherWartBlock.AGE).equals(3)) { // Nether Wart
                crop(Blocks.NETHER_WART, state, level, pos, player, event, heldItem);
            }
            else if (block.equals(Blocks.COCOA) && state.getValue(CocoaBlock.AGE).equals(2)) {
                crop(Blocks.COCOA, state, level, pos, player, event, heldItem);
            }
            else if (block.defaultBlockState().is(ModTags.Blocks.VERTICAL_BLOCKS)) { // Sugar cane, Bamboo or Cactus
                BlockPos basePos = pos.below(); // Only replant if there is correct soil below
                BlockState baseState = level.getBlockState(basePos);
                // Bamboo (Block) -> Grass; Cactus (Block) -> Sand; Sugar cane (Block) -> Grass, Sand or Dirt.
                if (baseState.is(ModTags.Blocks.VERTICAL_GROW_BLOCKS)) {
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
                    level.setBlock(pos, block.defaultBlockState(), 3); // Replant the original block
                    damageToolIfHoe(heldItem, player);
                }
            }
            else if (block.defaultBlockState().is(ModTags.Blocks.MUSHROOM_BLOCKS)) { // Mushroom, etc.
                BlockState baseState = level.getBlockState(pos.below());
                if (baseState.is(BlockTags.MUSHROOM_GROW_BLOCK)) { // Check if the soil is suitable
                    crop(block, state, level, pos, player, event, heldItem);
                }
            }
        }
    }

    // CUSTOM EVENT - ANVIL disenchanted event
    private static void dropItem(ServerLevel world, BlockPos pos, ItemStack stack) {
        // CUSTOM METHOD - Drop enchanted book and base item on ground [world]
        ItemEntity item = new ItemEntity(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, stack);
        item.setDeltaMovement(Vec3.ZERO);
        world.addFreshEntity(item);
    }

    @SubscribeEvent
    public static void anvilDisenchant(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            for (ServerLevel world : event.getServer().getAllLevels()) {
                for (Entity entity : world.getAllEntities()) {
                    if (entity instanceof FallingBlockEntity fallingBlock) {
                        BlockState state = fallingBlock.getBlockState(); // Anvil state
                        BlockPos pos = fallingBlock.blockPosition(); // Anvil position
                        if (!state.getBlock().defaultBlockState().is(BlockTags.ANVIL)) { continue; }
                        BlockPos blockBelow = pos.below(); // The item is below the anvil
                        // Pick up the items on the ground below the anvil - small area below the anvil
                        List<ItemEntity> itemsBelow = world.getEntitiesOfClass(ItemEntity.class,
                                new AABB(blockBelow).inflate(0.5));
                        if (itemsBelow.size() != 1) { continue; } // Processing only if there is exactly ONE item
                        ItemEntity itemEntity = itemsBelow.get(0); // First item of list
                        ItemStack item = itemEntity.getItem(); // Get real item
                        // Get all enchantments of the item
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
                        boolean isBook = item.is(Items.ENCHANTED_BOOK);
                        /* Ignore if item has no enchantment or if item is a book with only 1 enchantment
                           Only process if it's not a previously split book (to avoid infinite loop) */
                        if (enchantments.isEmpty() || (isBook && enchantments.size() == 1)) { continue; }
                        // Drop an enchanted book with the enchantments of tool, armor, etc.
                        if (item.isDamageableItem() && !isBook) {
                            ItemStack groupedBooks = new ItemStack(Items.ENCHANTED_BOOK);
                            // Added each enchantment found on tool, armor, etc.
                            enchantments.forEach((key, value) -> EnchantedBookItem.addEnchantment(groupedBooks,
                                    new EnchantmentInstance(key, value)));
                            // Set original item WITHOUT enchantments
                            ItemStack baseItem = item.copy();
                            EnchantmentHelper.setEnchantments(Map.of(), baseItem);
                            baseItem.removeTagKey("StoredEnchantments");
                            CompoundTag tag = baseItem.getTag(); // Clean up tag if empty
                            if (tag != null && baseItem.hasTag() && tag.isEmpty()) { baseItem.setTag(null); }
                            dropItem(world, blockBelow, groupedBooks); // Drop enchanted book WITH enchantments
                            dropItem(world, blockBelow, baseItem); // Drop item WITHOUT enchantments
                        }
                        else if (isBook) { // Book with multiple enchantments
                            enchantments.forEach((key, value) -> { // Split each enchantment into individual books
                                ItemStack singleBook = new ItemStack(Items.ENCHANTED_BOOK);
                                EnchantedBookItem.addEnchantment(singleBook, new EnchantmentInstance(key, value));
                                dropItem(world, blockBelow, singleBook); // Drop individual enchanted book
                            });
                        }
                        itemEntity.discard(); // Discard the original item
                    }
                }
            }
        }
    }

    // CUSTOM EVENT - RECOVER custom enchantment
    @SubscribeEvent
    public static void activatedRecoverEnchantment(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if ((event.phase == TickEvent.Phase.END) || !player.level().isClientSide()) {
            // Desired value for level.getGameTime() % X != 0 -> [0.5, 1, 2, 5] seconds -> X = [10, 20, 40, 100] ticks
            if (player.level().getGameTime() % 40 != 0) { return; } // Example: 1 second = 20 ticks
            List<NonNullList<ItemStack>> playerSlots = List.of(player.getInventory().items,
            player.getInventory().armor, player.getInventory().offhand);
            playerSlots.forEach(itemStacks -> itemStacks.forEach(stack -> {
                if (!stack.isEmpty() && stack.isDamaged() && enchant(stack, ModEnchantments.RECOVER.get()) > 0) {
                    int currentDamage = stack.getDamageValue();
                    stack.setDamageValue(currentDamage - Math.min(currentDamage, 10)); // Repairs 10 of damage at a time
                }
            }));
        }
    }

    // CUSTOM EVENT - NOTHING custom effect
    @SubscribeEvent
    public static void activatedNothingEffect(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Warden warden) { // Checks if there is a player with the effect active nearby
            List<Player> players = warden.level().getEntitiesOfClass(Player.class, warden.getBoundingBox().inflate(32));
            for (Player player : players) {
                if (player.hasEffect(ModEffects.NOTHING_EFFECT.get())) {
                    event.setCanceled(true); // Prevents the Warden from spawning
                    break;
                }
            }
        }
    }

    // CUSTOM EVENT - ELYTRA BOOST custom enchantment
    @SubscribeEvent
    public static void activatedElytraBoostEnchantment(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (!player.isCreative() && !player.level().isClientSide()) {
            ItemStack elytra = new ItemStack(Items.ELYTRA);
            int elytraBoost = enchant(elytra, ModEnchantments.ELYTRA_BOOST.get());
            if (elytra.isEnchanted() && has(player, EquipmentSlot.CHEST).is(elytra.getItem()) && elytraBoost > 0) {
                double boostFactor = 1.0 + (0.5 * elytraBoost); // Elytra speed (50% extra) per level
                player.setDeltaMovement(player.getDeltaMovement().multiply(boostFactor, 1.0, boostFactor));
                player.hurtMarked = true;
            }
        }
    }

    // CUSTOM EVENT - XP BOOST custom enchantment
    @SubscribeEvent
    public static void activatedXpBoostEnchantment(LivingExperienceDropEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (event.getAttackingPlayer() != null) { // Attacked entities
                int level = hasEnchant(ModEnchantments.XP_BOOST.get(), player);
                if (level > 0) {
                    int bonus = Math.round(event.getOriginalExperience() * (1.0f * level));
                    event.setDroppedExperience(event.getDroppedExperience() + bonus);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPickupXpBoostEnchantment(PlayerXpEvent.PickupXp event) {
        int level = hasEnchant(ModEnchantments.XP_BOOST.get(), event.getEntity()); // Mined blocks or Picked furnace items
        if (level > 0) { event.getOrb().value += Math.round(event.getOrb().getValue() * (1.0f * level)); }
    }

    private static int hasEnchant(Enchantment enchantment, Player player) {
        return EnchantmentHelper.getEnchantmentLevel(enchantment, player);
    }

    // CUSTOM EVENT - MULTIPLIER custom enchantment
    @SubscribeEvent
    public static void activatedMultiplierEnchantment(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack item = player.getMainHandItem();
            int level = enchant(item, ModEnchantments.MULTIPLIER.get());
            if (level > 1) {
                List<ItemEntity> originalDrops = new ArrayList<>(event.getDrops());
                for (ItemEntity drop : originalDrops) {
                    ItemStack stack = drop.getItem().copy();
                    stack.setCount(stack.getCount() * level); // Multiplier adapt on level
                    ItemEntity drops = new ItemEntity(drop.level(), drop.getX(), drop.getY(), drop.getZ(), stack);
                    drops.setDeltaMovement(Vec3.ZERO);
                    event.getDrops().add(drops);
                }
            }
        }
    }

    // CUSTOM EVENT - MOBS CRITICAL custom enchantment
    @SubscribeEvent
    public static void activatedMobsCriticalEnchantment(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack weapon = player.getMainHandItem();
            int mobsCritical = enchant(weapon, ModEnchantments.MOBS_CRITICAL.get());
            if (mobsCritical > 0 && (!(player.fallDistance > 0) || !player.onGround())) {
                float baseDamage = event.getAmount();
                event.setAmount(baseDamage + (baseDamage * (0.5F * mobsCritical))); // Critical damage (50% extra) per level
                player.level().playSound(null, player.blockPosition(), SoundEvents.PLAYER_ATTACK_CRIT,
                        SoundSource.PLAYERS, 1.0F, 1.0F); // Particle effect and sound
                ((ServerLevel) player.level()).sendParticles(ParticleTypes.CRIT, event.getEntity().getX(),
                        event.getEntity().getY(0.5), event.getEntity().getZ(), 5,
                        0.2, 0.2, 0.2, 0.1);
            }
        }
    }

    // CUSTOM EVENT - ANVIL enchantment compatibilities
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();   // Base item
        ItemStack right = event.getRight(); // Book or Second item
        if (!left.isEmpty() && !right.isEmpty()) {
            ItemStack output = left.copy(); // Result of Base item + Book or Second item
            boolean modified = false;
            Map<Enchantment, Integer> leftEnchantments = EnchantmentHelper.getEnchantments(left);
            Map<Enchantment, Integer> rightEnchantments = EnchantmentHelper.getEnchantments(right);
            for (Map.Entry<Enchantment, Integer> entry : rightEnchantments.entrySet()) {
                Enchantment ench = entry.getKey(); // Enchantment
                int rightLevel = entry.getValue(); // Enchantment level
                int leftLevel = leftEnchantments.getOrDefault(ench, 0); // Book or Second item enchantment level
                int newLevel = leftLevel == rightLevel ? rightLevel + 1 : Math.max(leftLevel, rightLevel); // Output with new level
                leftEnchantments.put(ench, Math.min(ench.getMaxLevel(), newLevel)); // Here we ignore the compatibility check.
                modified = true;
            }
            if (modified) { // Output item
                EnchantmentHelper.setEnchantments(leftEnchantments, output);
                event.setOutput(output);
                event.setCost(1); // Cost at levels
                event.setMaterialCost(1); // Cost of materials (e.g. books, diamonds etc.)
            }
        }
    }

    // CUSTOM EVENT - IMMORTAL custom enchantment
    private static void activatedImmortalEnchantment(ItemEntity entity, ItemStack item) {
        if (enchant(item, ModEnchantments.IMMORTAL.get()) > 0) {
            entity.setInvulnerable(true);
            entity.setUnlimitedLifetime(); // Does not disappear over time
            entity.setPickUpDelay(10); // It can be collected after 0.5s
        }
    }

    @SubscribeEvent
    public static void onItemToss(ItemTossEvent event) {
        ItemEntity entity = event.getEntity();
        if (!entity.level().isClientSide()) { activatedImmortalEnchantment(entity, entity.getItem()); }
    }

    @SubscribeEvent
    public static void onItemSpawn(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (!entity.level().isClientSide()) {
            if (entity instanceof ItemEntity itemEntity) { activatedImmortalEnchantment(itemEntity, itemEntity.getItem()); }
        }
    }

    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        event.getAffectedEntities().removeIf(entity -> entity instanceof ItemEntity itemEntity &&
                enchant(itemEntity.getItem(), ModEnchantments.IMMORTAL.get()) > 0);
    }

    @SubscribeEvent
    public static void onItemExpire(ItemExpireEvent event) { // Never disappears
        if (enchant(event.getEntity().getItem(), ModEnchantments.IMMORTAL.get()) > 0) { event.setCanceled(true); }
    }

    @SubscribeEvent
    public static void onEntityTick(TickEvent.LevelTickEvent event) {
        if (event.phase == TickEvent.Phase.END && !event.level.isClientSide()) {
            for (Entity entity : event.level.getEntities(null,
                    AABB.ofSize(new Vec3(0, -100, 0), 10000, 500, 10000))) {
                if (entity instanceof ItemEntity item && enchant(item.getItem(), ModEnchantments.IMMORTAL.get()) > 0) {
                    if (item.getY() < -64) {
                        Player nearestPlayer = event.level.getNearestPlayer(item, 64); // Find the nearest player
                        if (nearestPlayer != null) { // Teleports the item to the player
                            item.teleportTo(nearestPlayer.getX(), nearestPlayer.getY() + 1, nearestPlayer.getZ());
                            // Sets the speed for "flying to player"
                            Vec3 motion = nearestPlayer.position().subtract(item.position()).normalize().scale(0.5);
                            item.setDeltaMovement(motion);
                        }
                        // If no player nearby, pick up the item as before
                        else { item.teleportTo(item.getX(), 100, item.getZ()); }
                    }
                }
            }
        }
    }

    // CUSTOM EVENT - Item Teleport
    private static void teleportPlayerIfHoldingTool(Player player, ItemStack stack) {
        if (!player.level().isClientSide()) {
            if (!stack.isEmpty()) { // Check if holding a tool or fishing rod
                Item item = stack.getItem();
                if (item instanceof SwordItem || item instanceof PickaxeItem || item instanceof FishingRodItem) {
                    double reachDistance = 5.0; // How many blocks ahead to ray trace
                    Vec3 lookVec = player.getLookAngle(); // Get the direction the player is looking
                    Vec3 start = player.getEyePosition(); // It starts from the eyes
                    Vec3 end = start.add(lookVec.scale(reachDistance));
                    BlockHitResult hitResult = player.level().clip(new ClipContext( // Ray trace until it hits a block
                            start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player));
                    if (hitResult.getType() == HitResult.Type.BLOCK) {
                        BlockPos blockPos = hitResult.getBlockPos(); // Teleports to the top of the block hit (+1 height)
                        double x = blockPos.getX() + 0.5;
                        double y = blockPos.getY() + 1.0;
                        double z = blockPos.getZ() + 0.5;
                        ((ServerPlayer) player).teleportTo((ServerLevel) player.level(), x, y, z,
                                player.getYRot(), player.getXRot());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        // Teleport when using item (like fishing rod, sword, pickaxe, etc.)
        teleportPlayerIfHoldingTool(event.getEntity(), event.getItemStack());
    }

    // CUSTOM EVENT - Overlay: X Y Z coordinates and Light
    private static void renderLight(RenderGuiOverlayEvent event, Font font,
                                    String message, int y, int bool) {
        if (bool > 6) { event.getGuiGraphics().drawString(font, message, 10, y, 0x32FC76); } // Green color
        else { event.getGuiGraphics().drawString(font, message, 10, y, 0xFF1818); } // Red color
    }

    @SubscribeEvent
    public static void overlayCoordinateLight(RenderGuiOverlayEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (!mc.options.renderDebug && mc.screen == null) {
            if (player != null && mc.level != null) { // Render only when the player is in the game and not in the menu
                double x = player.getX(), y = player.getY(), z = player.getZ(); // Player x, y, z coordinates
                BlockPos pos = player.blockPosition();
                int blockLight = mc.level.getLightEngine().getLayerListener(LightLayer.BLOCK).getLightValue(pos);
                int skyLight = mc.level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(pos);
                int totalLight = Math.max(blockLight, skyLight);
                Font font = mc.font;
                GuiGraphics guiGraphics = event.getGuiGraphics();
                // Text to be displayed on screen
                guiGraphics.drawString(font, String.format("X: %.3f  Y: %.5f  Z: %.3f", x, y, z), 10, 10, 0xFFFFFF);
                renderLight(event, font, String.format("Light: %d", totalLight), 20, totalLight);
                renderLight(event, font, String.format("Sky: %d", skyLight), 30, skyLight);
                renderLight(event, font, String.format("Block: %d", blockLight), 40, blockLight);
            }
        }
    }

    // CUSTOM EVENT - ACCUMULATOR custom enchantment
    private static void setPlayerXP(Player player, Level level, int xp) {
        Vec3 position = new Vec3(player.getBlockX(), player.getBlockY(), player.getBlockZ());
        ExperienceOrb.award((ServerLevel) level, position, xp);
    }

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) { // Gain experience orb when mined block
        Level level = (Level) event.getLevel();
        if (!level.isClientSide()) {
            Player player = event.getPlayer();
            BlockState state = event.getState();
            // Checks if the broken block is one that usually does not give XP
            if (state.is(ModTags.Blocks.ACCUMULATOR_EXPERIENCE)) {
                ItemStack item = player.getMainHandItem();
                int accumulator = enchant(item, ModEnchantments.ACCUMULATOR.get());
                int multiplier = enchant(item, ModEnchantments.MULTIPLIER.get());
                int xp = 3; // Amount of XP you want to give - Default 3 experience orb
                if (accumulator > 0) { xp = 3 * accumulator; } // Gain 30 experience orb
                else if (multiplier > 0) { xp = 3 * accumulator * multiplier; } // Gain 300 experience orb
                setPlayerXP(player, level, xp);
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) { // Gain experience orb when mined block
        Player player = event.getEntity();
        Level level = player.level();
        if (!level.isClientSide()) { setPlayerXP(player, level, 10); }
    }

    @SubscribeEvent
    public static void onEntityKill(LivingDeathEvent event) { // Gain experience orb when killed entities
        if (event.getSource().getEntity() instanceof Player player) {
            Level level = player.level();
            if (!level.isClientSide()) { setPlayerXP(player, level, 10); }
        }
    }

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) { // Gain experience orb when fished
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) { if (!event.getDrops().isEmpty()) { setPlayerXP(player, level, 4); } }
    }
}