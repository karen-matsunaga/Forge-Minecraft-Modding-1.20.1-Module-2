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
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
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
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
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
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
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
import java.util.function.Predicate;

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
                boolean item = hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos));
                if (pos == initalBlockPos || !item) { continue; }
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
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (event.phase == TickEvent.Phase.END && player != null) {
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
    }

    // Hammer Highlight Renderer blocks
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            HammerItem.renderHighlight(event.getPoseStack(), event.getCamera(), bufferSource);
            bufferSource.endBatch(); // Finish the drawing!
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
                    MCCourseMod.LOGGER.info("Sheep was hit with Alexandrite Axe by {}", player.getName().getString());
                }
                else if (player.getItemInHand(InteractionHand.MAIN_HAND).getItem() == Items.DIAMOND) {
                    MCCourseMod.LOGGER.info("Sheep was hit with DIAMOND by {}", player.getName().getString());
                }
                else {
                    MCCourseMod.LOGGER.info("Sheep was hit with something else by {}", player.getName().getString());
                }
            }
        }
    }

    // CUSTOM EVENT - Custom Villager's professions trade
    private static void villagerTrades(Int2ObjectMap<List<VillagerTrades.ItemListing>> trade,
                              List<Item> items, int level, List<Integer> levelCount, float multiplier) {
        trade.get(level).add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(items.get(0), levelCount.get(0)),
                new ItemStack(items.get(1), levelCount.get(1)), levelCount.get(2), levelCount.get(3), multiplier));
    }

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        // Villager's farm profession
        if (event.getType() == VillagerProfession.FARMER) {
            // List of all trades
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // List of all trades that the player can trade
            // Received KOHLRABI with Villager's level 1
            villagerTrades(trades, List.of(Items.EMERALD, ModItems.KOHLRABI.get()), 1,
                    List.of(2, 6, 10, 2), 0.02f);

            // Received KOHLRABI SEEDS with Villager's level 2
            villagerTrades(trades, List.of(Items.EMERALD, ModItems.KOHLRABI_SEEDS.get()), 2,
                    List.of(5, 1, 3, 2), 0.02f);
        }

        // Villager's toolsmith profession
        if (event.getType() == VillagerProfession.TOOLSMITH) {
            // List of all trades
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // List of all trades that the player can trade
            // Received ALEXANDRITE PAXEL with Villager's level 3
            villagerTrades(trades, List.of(Items.EMERALD, ModItems.ALEXANDRITE_PAXEL.get()), 3,
                    List.of(12, 1, 2, 5), 0.06f);
        }

        // Custom Villager's soundmaster profession
        if (event.getType() == ModVillagers.SOUND_MASTER.get()) {
            // List of all trades
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // List of all trades that the player can trade
            // Received Sound Block with Villager's level 1
            villagerTrades(trades, List.of(Items.EMERALD, ModBlocks.SOUND_BLOCK.get().asItem()), 1,
                    List.of(25, 1, 2, 5), 0.06f);
        }
    }

    // CUSTOM EVENT - Custom Villager Wandering
    private static void wanderingTrades(List<VillagerTrades.ItemListing> trade,
                                       List<Item> items, List<Integer> levelCount, float multiplier) {
        trade.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(items.get(0), levelCount.get(0)),
                new ItemStack(items.get(1), levelCount.get(1)), levelCount.get(2), levelCount.get(3), multiplier));
    }

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        // List of all trades that the player can trade - Generic and Rare trades because not exist levels
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        // List of all generic and rare trades
        // Received KOHLRABI like Generic Trades
        wanderingTrades(genericTrades, List.of(Items.EMERALD, ModItems.KOHLRABI.get()),
                List.of(2, 6, 10, 2), 0.02f);

        // Received KOHLRABI SEEDS like Rare Trades
        wanderingTrades(rareTrades, List.of(Items.EMERALD, ModItems.KOHLRABI_SEEDS.get()),
                List.of(5, 1, 3, 2), 0.02f);

        // Magic Book custom block
        wanderingTrades(rareTrades, List.of(Items.EMERALD, ModBlocks.MAGIC_BOOK_BLOCK.get().asItem()),
                List.of(64, 1, 9, 10), 0.06f);
    }

    // CUSTOM EVENT - RAINBOW | AUTO SMELT | MORE ORES | MAGNETIC custom enchantments
    private static void block(LevelAccessor world, BlockPos pos, Block block,
                              BlockEvent.BreakEvent event) {
        world.setBlock(pos, block.defaultBlockState(), 3);
        event.setCanceled(true);
    }

    public static boolean hasBlock(BlockState state, Block block, float chance) {
        return state.is(block) && Math.random() < chance;
    }

    @SubscribeEvent
    public static void onBlockBreakWithCustomEnchantments(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        ItemStack tool = player.getMainHandItem();
        int moreOres = tool.getEnchantmentLevel(ModEnchantments.MORE_ORES.get());
        int fortune = tool.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);

        // RAINBOW ENCHANTMENT
        if (tool.getEnchantmentLevel(ModEnchantments.RAINBOW.get()) > 0) {
            Map<Block, TagKey<Block>> rainbowMap = Map.of(Blocks.COAL_BLOCK, Tags.Blocks.ORES_COAL,
            Blocks.COPPER_BLOCK, Tags.Blocks.ORES_COPPER, Blocks.DIAMOND_BLOCK, Tags.Blocks.ORES_DIAMOND,
            Blocks.EMERALD_BLOCK, Tags.Blocks.ORES_EMERALD, Blocks.GOLD_BLOCK, Tags.Blocks.ORES_GOLD,
            Blocks.IRON_BLOCK, Tags.Blocks.ORES_IRON, Blocks.LAPIS_BLOCK, Tags.Blocks.ORES_LAPIS,
            Blocks.REDSTONE_BLOCK, Tags.Blocks.ORES_REDSTONE, Blocks.NETHERITE_BLOCK, Tags.Blocks.ORES_NETHERITE_SCRAP);

            for (Map.Entry<Block, TagKey<Block>> entry : rainbowMap.entrySet()) {
                if (state.is(entry.getValue())) {
                    block(world, pos, entry.getKey(), event); // Blocks normal break
                    return; // Other enchantments are not applied
                }
            }
        }

        if (world instanceof ServerLevel serverLevel) {
            boolean cancelVanillaDrop = false; // Adapt the drop according to the enchantment being true
            List<ItemStack> finalDrops = new ArrayList<>(); // Items caused by enchantments are stored in the list

            // AUTO SMELT ENCHANTMENT
            if (tool.getEnchantmentLevel(ModEnchantments.AUTO_SMELT.get()) > 0) {
                Optional<SmeltingRecipe> recipe = serverLevel.getRecipeManager().getRecipeFor(RecipeType.SMELTING,
                        new SimpleContainer(new ItemStack(state.getBlock())), serverLevel);

                if (recipe.isPresent()) { // Has recipe
                    ItemStack result = recipe.get().getResultItem(serverLevel.registryAccess()).copy();
                    int count = 1 + (fortune > 0 ? serverLevel.random.nextInt(fortune + 1) : 0);
                    for (int i = 0; i < count; i++) { finalDrops.add(result.copy()); }
                    cancelVanillaDrop = true;
                }
            }

            // MORE ORES ENCHANTMENT
            if (moreOres > 0) {
                List<TagKey<Block>> oresTags = List.of(ModTags.Blocks.MORE_ORES_ONE_DROPS, ModTags.Blocks.MORE_ORES_TWO_DROPS,
                ModTags.Blocks.MORE_ORES_THREE_DROPS, ModTags.Blocks.MORE_ORES_FOUR_DROPS, ModTags.Blocks.MORE_ORES_FIVE_DROPS);

                if (hasBlock(state, Blocks.STONE, 0.1f) && moreOres < 5 ||
                        hasBlock(state, Blocks.NETHERRACK, 0.01f) && moreOres == 5) {
                    var tagManager = ForgeRegistries.BLOCKS.tags();
                    if (tagManager != null) {
                        tagManager.getTag(oresTags.get(moreOres - 1)).getRandomElement(RandomSource.create())
                                .ifPresent(block -> finalDrops.add(new ItemStack(block)));
                        cancelVanillaDrop = true;
                    }
                }
            }

            // MAGNETIC ENCHANTMENT
            if (tool.getEnchantmentLevel(ModEnchantments.MAGNETIC.get()) > 0 && !state.isAir()) {
                if (finalDrops.isEmpty()) { // FinalDrops empty list added all items on it is
                    finalDrops.addAll(Block.getDrops(state, serverLevel, pos, null, player, tool));
                }
                for (ItemStack drop : finalDrops) { // FinalDrops list added on Player's inventory
                    if (!player.getInventory().add(drop)) { player.drop(drop, false); }
                }
                block(serverLevel, pos, Blocks.AIR, event);
                return;
            }

            if (cancelVanillaDrop) { // FinalDrops list accumulate drop on world
                for (ItemStack drop : finalDrops) { dropItem(serverLevel, pos, drop); }
                block(serverLevel, pos, Blocks.AIR, event);
            }
        }
    }

    // Credits by Lykrast - https://github.com/Lykrast/MeetYourFight/blob/master/LICENSE - Distributed under MIT
    // CUSTOM EVENT - Glowing Mobs's custom enchantment - Using code with some modifications
    @SubscribeEvent
    public static void activatedGlowingMobsEnchantment(LivingEvent event) {
        int GLOWING_EYES = 10; // Range of Glowing effect on mobs
        if (event.getEntity() instanceof Player player) { // Player is an entity
            ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD); // Player has an item on helmet slot
            // Player has a helmet inputted on slot and Glowing Mobs enchantment level
            if (helmet.isEnchanted() || helmet.getEnchantmentLevel(ModEnchantments.GLOWING_MOBS.get()) > 0) {
                // Key - Entities colors -> Each group represent with some color (Color)
                // Value - Entities groups -> Represent as Tag (Group tag name)
                Map<ChatFormatting, TagKey<EntityType<?>>> entitiesTag = Map.ofEntries(
                Map.entry(ChatFormatting.RED, ModTags.Entities.MONSTERS), // Monsters
                Map.entry(ChatFormatting.BLUE, ModTags.Entities.ANIMALS), // Animal and Flying entities
                Map.entry(ChatFormatting.YELLOW, ModTags.Entities.WATER_ANIMALS), // Water animals
                Map.entry(ChatFormatting.DARK_PURPLE, ModTags.Entities.VILLAGER)); // Villagers

                for (Map.Entry<ChatFormatting, TagKey<EntityType<?>>> entry : entitiesTag.entrySet()) {
                    TagKey<EntityType<?>> tagValue = entry.getValue();
                    String teamName = tagValue.location().getPath();
                    List<LivingEntity> entities = player.level().getEntitiesOfClass(LivingEntity.class,
                    player.getBoundingBox().inflate(GLOWING_EYES), entity -> entity.getType().is(tagValue) && entity != player);

                    if (!entities.isEmpty()) {
                        PlayerTeam team = player.getScoreboard().getPlayerTeam(teamName);
                        // Added each entity on group with specif tag and color on entitiesTag
                        if (team == null) {
                            team = player.getScoreboard().addPlayerTeam(teamName);
                            team.setColor(entry.getKey()); // Color
                        }

                        // Each entity received Glowing effect with specif color on entityColors
                        for (LivingEntity entity : entities) {
                            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 1,
                            true, false, false));
                            player.getScoreboard().addPlayerToTeam(entity.getScoreboardName(), team);
                        }
                    }
                }
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
            if (!enchantments.isEmpty()) {
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
                                case ARMOR, ARMOR_HEAD, ARMOR_CHEST, ARMOR_LEGS, ARMOR_FEET -> ChatFormatting.GOLD;
                                case DIGGER -> ChatFormatting.DARK_PURPLE;
                                case BOW, CROSSBOW, WEAPON -> ChatFormatting.DARK_RED;
                                case TRIDENT -> ChatFormatting.AQUA;
                                case WEARABLE -> ChatFormatting.GREEN;
                                case BREAKABLE -> ChatFormatting.DARK_GREEN;
                                case VANISHABLE -> ChatFormatting.RED;
                                case FISHING_ROD -> ChatFormatting.YELLOW; }; // Replace this line with custom styled version
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
        int h = event.getWindow().getGuiScaledHeight();
        int x = 10;
        int y = h - 30;

        if (player != null && player.getMainHandItem().getItem() instanceof ModesPickaxeItem modesPickaxe) {
            ModesPickaxe mode = modesPickaxe.getModeActual(); // Show text mode actual on screen
            // Renders text on overlay on same line
            String text = "Mode: ";
            screen(event, mc, text, x, y, 0xFFAA00);
            x += mc.font.width(text); // Mode Text
            screen(event, mc, mode.toString().replace("_", " "), x + 5, y, 0xFF5555); // Mode Type

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
    private static boolean fullArmor(Player player, EquipmentSlot slot, TagKey<Item> item) {
        return player.getItemBySlot(slot).is(item);
    }

    private static boolean fly(Player player, MobEffect effect) { return player.hasEffect(effect); }

    @SubscribeEvent
    public static void flyEffect(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if ((event.phase == TickEvent.Phase.END) && !player.level().isClientSide()) {
            Abilities abilities = player.getAbilities();
            // Player used FULL ARMOR
            boolean hasArmor = fullArmor(player, EquipmentSlot.HEAD, ModTags.Items.HELMET_FLY) &&
            fullArmor(player, EquipmentSlot.CHEST, ModTags.Items.CHESTPLATE_FLY) &&
            fullArmor(player, EquipmentSlot.LEGS, ModTags.Items.LEGGINGS_FLY) &&
            fullArmor(player, EquipmentSlot.FEET, ModTags.Items.BOOTS_FLY);

            // Player has FLY EFFECT
            boolean hasFlyEffect = fly(player, ModEffects.FLY_EFFECT.get()) || fly(player, ModEffects.OVERPOWER_FLY_EFFECT.get());

            // Player has FULL ARMOR or FLY EFFECT
            if (hasArmor || hasFlyEffect) { if (!abilities.mayfly) { abilities.mayfly = true; } }
            // Player hasn't FULL ARMOR or FLY EFFECT
            else {
                if (abilities.mayfly && !player.isCreative()) { abilities.mayfly = false; abilities.flying = false; }
            }
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

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) { // Player is on world
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            GlowingBlocksNetworkMessage.SyncedSavedData mapData =
                    GlowingBlocksNetworkMessage.MapVariables.get(player.level());
            GlowingBlocksNetworkMessage.SyncedSavedData worldData =
                    GlowingBlocksNetworkMessage.WorldVariables.get(player.level());
            if (mapData != null) { sendPacket(player, mapData); }
            if (worldData != null) { sendPacket(player, worldData); }
        }
    }

    // Player is on [Overworld, Nether, End, etc.]
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        if (!player.level().isClientSide()) {
            GlowingBlocksNetworkMessage.SyncedSavedData worldData =
                    GlowingBlocksNetworkMessage.WorldVariables.get(player.level());
            if (worldData != null) { sendPacket(player, worldData); }
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
    Map.entry(Tags.Blocks.ORES_NETHERITE_SCRAP, 0xFFD22CF8), Map.entry(ModTags.Blocks.MCCOURSE_ORES, 0xFFffc0eb),
    Map.entry(ModTags.Blocks.SPECIAL_METAL_DETECTOR_VALUABLES, 0xFF157ccb));

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
    private static void clear() {
        if (vertexBuffer != null) {
            vertexBuffer.close();
            vertexBuffer = null;
        }
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
    private static void renderShape(VertexBuffer vertexBuffer, double x, double y, double z,
                                    int color) {
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
        else {
            i = (float) x;
            j = (float) y;
            k = (float) z;
        }
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

    // CUSTOM METHOD - Render block shape on world
    private static void renderLevel(List<Integer> stage, List<Boolean> bool,
                                    RenderLevelStageEvent event) {
        currentStage = stage.get(0);
        RenderSystem.depthMask(bool.get(0));
        renderShapes(event);
        RenderSystem.enableCull();
        RenderSystem.depthMask(bool.get(1));
        currentStage = stage.get(1);
    }

    // Where render block shape on world
    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) {
            renderLevel(List.of(1, 0), List.of(false, true), event);
        }
        else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            renderLevel(List.of(2, 0), List.of(true, true), event);
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
                            int[][] cubeCoordinates = { {0,0,0},{1,0,0},{1,0,0},{1,0,1},{1,0,1},{0,0,1},
                                    {0,0,1},{0,0,0},{0,0,0},{0,1,0},{1,0,0},{1,1,0},
                                    {1,0,1},{1,1,1},{0,0,1},{0,1,1},{0,1,0},{1,1,0},
                                    {1,1,0},{1,1,1},{1,1,1},{0,1,1},{0,1,1},{0,1,0}};
                            for (Map.Entry<TagKey<Block>, Integer> entry : renderColors.entrySet()) {
                                if (level.getBlockState(position).is(entry.getKey())) {
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

    @SubscribeEvent
    public static void decapitatorBlock(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        if (!level.isClientSide()) {
            BlockPos origin = event.getPos();
            BlockState originState = level.getBlockState(origin);
            Player player = event.getPlayer();
            // Checks if the broken block is a log
            if (isLog(originState)) {
                // Checks if you are using the correct tool
                if (player.getMainHandItem().getItem().isCorrectToolForDrops(originState)) {
                    Set<BlockPos> connected = findConnectedLogsAndLeaves(level, origin);
                    int logCount = 0;
                    for (BlockPos pos : connected) {
                        BlockState state = level.getBlockState(pos);
                        if (isLog(state) || isLeaf(state)) {
                            level.destroyBlock(pos, true); // Drop the blocks
                            if (isLog(state)) { logCount++; }
                        }
                    }
                    // Applies damage proportional to the amount of logs broken
                    if (logCount > 0) {
                        ItemStack tool = player.getMainHandItem();
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
        // Player has Block Fly enchantment
        if (EnchantmentHelper.getEnchantmentLevel(ModEnchantments.BLOCK_FLY.get(), player) > 0) {
            if ((!player.onGround() && !player.isUnderWater()) || player.isUnderWater()) {
                float oldSpeed = event.getOriginalSpeed(); // Old speed
                event.setNewSpeed(oldSpeed * 5); // New speed -> Fixed speed mining
            }
        }
    }

    // CUSTOM EVENT - Mccourse Elevator advanced block
    private static void send(boolean response) {
        ModNetworks.PACKET_HANDLER.sendToServer(new MccourseElevatorKeyInputMessage(response));
    }

    @SubscribeEvent
    public static void activatedMccourseElevatorOnKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) {
            // Checks if the player is over the elevator
            BlockPos pos = BlockPos.containing(player.getX(), player.getY() - 1, player.getZ());
            if (player.level().getBlockState(pos).getBlock() == ModBlocks.MCCOURSE_ELEVATOR.get()) {
                // Detects JUMP
                if (InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_SPACE)) { send(true); }
                // Detects SHIFT/crouch
                if (player.isShiftKeyDown()) { send(false); }
            }
        }
    }

    // CUSTOM EVENT - Eternal custom enchantment
    private static final Map<UUID, List<ItemStack>> preservedItems = new HashMap<>(); // Map of Main hand + Items
    private static final Map<UUID, List<ItemStack>> preservedArmor = new HashMap<>(); // Map of Armor
    private static final Map<UUID, List<ItemStack>> preservedOffhand = new HashMap<>(); // Map of Offhand
    private static final Map<UUID, int[]> preservedExperience = new HashMap<>(); // Map of Experience
    private static final Map<UUID, List<ItemStack>> preservedVault = new HashMap<>(); // Map of Vault items

    private static void setPreservedVault(NonNullList<ItemStack> type, List<ItemStack> preserved,
                                          List<ItemStack> vault) {
        for (int i = 0; i < type.size(); i++) {
            ItemStack inventory = type.get(i);
            if (!inventory.isEmpty() && inventory.getEnchantmentLevel(ModEnchantments.ETERNAL.get()) > 0) {
                preserved.set(i, inventory.copy()); // Copy of item with Eternal enchantment
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

    // Player receives items after death
    private static void setRestoredVault(NonNullList<ItemStack> type, List<ItemStack> restored) {
        if (restored != null) {
            for (int i = 0; i < restored.size(); i++) {
                if (!restored.get(i).isEmpty()) { type.set(i, restored.get(i)); } // Added all items on Player inventory
            }
        }
    }

    // Player normally drop all items when death
    @SubscribeEvent
    public static void activatedEternalEnchantmentOnPlayerDeath(LivingDeathEvent event) {
        // Entity is player
        if (event.getEntity() instanceof Player player) {
            UUID playerUUID = player.getUUID(); // Player id
            List<ItemStack> vaultItems = new ArrayList<>(); // Added rest items on Vault item
            // Added all Inventory slots, Armor slots and Offhand slot
            List<ItemStack> inventoryPreserve = new ArrayList<>(Collections.nCopies(36, ItemStack.EMPTY));
            List<ItemStack> armorPreserve = new ArrayList<>(Collections.nCopies(4, ItemStack.EMPTY));
            List<ItemStack> offhandPreserve = new ArrayList<>(Collections.nCopies(1, ItemStack.EMPTY));
            int[] experienceData = new int[] { player.experienceLevel, Float.floatToIntBits(player.experienceProgress),
            player.totalExperience }; // Added player experience

            // Get all items on inventory, armor and offhand slots
            // Main inventory, Armor and Left hand or Offhand
            setPreservedVault(player.getInventory().items, inventoryPreserve, vaultItems);
            setPreservedVault(player.getInventory().armor, armorPreserve, vaultItems);
            setPreservedVault(player.getInventory().offhand, offhandPreserve, vaultItems);

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
            Component displayName = itemChatMessage(player, pos, ChatFormatting.GREEN);

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
                // Save custom name in NBT
                vaultTag.putString("DisplayName", displayName.toString());
                vaultItem.setTag(vaultTag);
                vaultItem.setHoverName(displayName);

                // Temporarily saved for the clone event
                // Add directly to the new player's inventory in onClone()
                // Try adding to a free inventory slot
                preservedVault.computeIfAbsent(playerUUID, k -> new ArrayList<>()).add(vaultItem);
            }
        }
    }

    @SubscribeEvent
    public static void activatedEternalEnchantmentOnPlayerClone(PlayerEvent.Clone event) {
        // Ensures that it only runs after death
        if (event.isWasDeath()) {
            UUID playerUUID = event.getOriginal().getUUID(); // Get Player id
            Player newPlayer = event.getEntity(); // Entity is Player -> After death
            Player original = event.getOriginal(); // Old player -> Before death
            BlockPos blockPos = original.blockPosition(); // Player position after death

            // Player death message on chat
            newPlayer.sendSystemMessage(itemChatMessage(newPlayer, blockPos, ChatFormatting.GOLD));

            // Restore Inventory, Armor and Offhand slots
            // Remove all Inventory, Armor and Offhand saved slots
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
            if (savedVault != null) {
                for (ItemStack item : savedVault) { newPlayer.getInventory().add(item); } // Added item on Inventory slot
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

    private static void crop(Block block, BlockState state, Level level, BlockPos pos,
                             Player player, BlockEvent.BreakEvent event, ItemStack tool) {
        event.setCanceled(true); // Cancels pattern break
        Block.dropResources(state, level, pos, null, player, tool); // Drops items as if they had broken normally
        level.setBlock(pos, block.defaultBlockState(), 3); // Replants the initial stage of the plantation
        damageToolIfHoe(tool, player); // Spend tool durability
    }

    private static final Map<Block, Predicate<BlockState>> validSoils = Map.of(
    Blocks.BAMBOO, bs -> bs.is(Blocks.GRASS_BLOCK), Blocks.CACTUS, bs -> bs.is(Blocks.SAND),
    Blocks.SUGAR_CANE, bs -> bs.is(Blocks.GRASS_BLOCK) || bs.is(Blocks.DIRT) || bs.is(Blocks.SAND));

    // Crop automatically replant
    @SubscribeEvent
    public static void cropReplant(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = level.getBlockState(pos);
        Player player = event.getPlayer();

        // Only on server side and if player is not in creative mode
        if (!level.isClientSide() && !player.isCreative()) {
            ItemStack heldItem = player.getMainHandItem(); // Player has Hoe on main hand
            if (!(!heldItem.isEmpty() && heldItem.getItem() instanceof HoeItem)) { return; }
            Block block = state.getBlock();
            // Check if it is a plantation that can be replanted is Wheat, Carrot, Potato, Beet, etc.
            if (block instanceof CropBlock crop) {
                if (crop.isMaxAge(state)) { crop(crop, state, level, pos, player, event, heldItem); } // Check if it is ripe
            }
            // Nether Wart
            else if (block.equals(Blocks.NETHER_WART) && state.getValue(NetherWartBlock.AGE).equals(3)) {
                crop(Blocks.NETHER_WART, state, level, pos, player, event, heldItem);
            }
            // Sugar cane, Bamboo or Cactus
            else if (validSoils.containsKey(block)) {
                // Only replant if there is correct soil below
                BlockPos basePos = pos.below();
                BlockState baseState = level.getBlockState(basePos);
                // 1. Bamboo (Block) -> Grass (Test) - 2. Cactus (Block) -> Sand (Test)
                // 3. Sugar cane (Block) -> Grass, Sand or Dirt (Test)
                if (validSoils.get(block).test(baseState)) {
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
            else if (block.defaultBlockState().is(ModTags.Blocks.MUSHROOM_BLOCKS)) {
                BlockState baseState = level.getBlockState(pos.below());
                // Check if the soil is suitable
                if (baseState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
                    crop(block, state, level, pos, player, event, heldItem);
                }
            }
        }
    }

    // CUSTOM EVENT - Anvil disenchanted event
    // CUSTOM METHOD - Drop enchanted book and base item on ground [world]
    private static void dropItem(ServerLevel world, BlockPos pos, ItemStack stack) {
        world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1,
                pos.getZ() + 0.5, stack));
    }

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
                        // The item is below the anvil
                        BlockPos blockBelow = pos.below();
                        // Pick up the items on the ground below the anvil - small area below the anvil
                        List<ItemEntity> itemsBelow = world.getEntitiesOfClass(ItemEntity.class,
                                new AABB(blockBelow).inflate(0.5));

                        // Group books divided with 1 enchantment (without duplicating)
                        List<ItemEntity> singleEnchantBooks = itemsBelow.stream()
                                .filter(itemEntity -> {
                                    // Get enchanted book with 1 enchantment
                                    ItemStack stack = itemEntity.getItem();
                                    if (!stack.is(Items.ENCHANTED_BOOK)) { return false; }
                                    // Added enchanted book with 1 enchantment on list
                                    Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);
                                    return enchants.size() == 1;
                                }).toList();

                        // If single enchant book has more for 1
                        if (singleEnchantBooks.size() > 1) {
                            // Create an enchanted book
                            ItemStack newGrouped = new ItemStack(Items.ENCHANTED_BOOK);
                            // Create a map to transfer all enchantments to new grouped enchanted book
                            Map<Enchantment, Integer> collected = new HashMap<>();

                            // Each enchanted book
                            for (ItemEntity itemEntity : singleEnchantBooks) {
                                ItemStack stack = itemEntity.getItem();
                                Map<Enchantment, Integer> enchants = EnchantmentHelper.getEnchantments(stack);

                                // Access map with all enchantments
                                for (Map.Entry<Enchantment, Integer> entry : enchants.entrySet()) {
                                    Enchantment ench = entry.getKey();
                                    int level = entry.getValue();

                                    // If not already added, add
                                    if (!collected.containsKey(ench)) {
                                        // Added on map
                                        collected.put(ench, level);
                                        // Added on new enchanted book
                                        EnchantedBookItem.addEnchantment(newGrouped, new EnchantmentInstance(ench, level));
                                        // Remove original item
                                        itemEntity.discard();
                                    }
                                }
                            }

                            // All enchantments added on new enchanted book
                            if (!collected.isEmpty()) {
                                // Ensures books are grouped
                                newGrouped.getOrCreateTag().putBoolean("Grouped", true);
                                dropItem(world, blockBelow, newGrouped);
                            }
                        }

                        // Process the normal items
                        for (ItemEntity itemEntity : itemsBelow) {
                            // It has already been discarded in the grouping
                            if (itemEntity.isRemoved()) { continue; }
                            // Get real item
                            ItemStack item = itemEntity.getItem();
                            // Get all enchantments of the item
                            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(item);
                            boolean isBook = item.is(Items.ENCHANTED_BOOK);

                            // Ignore if item has no enchantment or if item is a book with only 1 enchantment
                            // Only process if it's not a previously split book (to avoid infinite loop)
                            if (enchantments.isEmpty() || (isBook && enchantments.size() == 1)) { continue; }

                            // Drop an enchanted book with the enchantments of tool, armor, etc.
                            if (item.isDamageableItem() && !isBook) {
                                ItemStack groupedBooks = new ItemStack(Items.ENCHANTED_BOOK);
                                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                    // Added each enchantment found on tool, armor, etc.
                                    EnchantedBookItem.addEnchantment(groupedBooks,
                                            new EnchantmentInstance(entry.getKey(), entry.getValue()));
                                }

                                // Ensures books are grouped
                                groupedBooks.getOrCreateTag().putBoolean("Grouped", true);

                                // Set original item without enchantments
                                ItemStack baseItem = item.copy();
                                EnchantmentHelper.setEnchantments(Map.of(), baseItem);
                                baseItem.removeTagKey("StoredEnchantments");

                                // Clean up tag if empty
                                if (baseItem.getTag() != null && baseItem.hasTag() && baseItem.getTag().isEmpty()) {
                                    baseItem.setTag(null);
                                }

                                // Drop enchanted book with enchantments and item WITHOUT enchantments
                                dropItem(world, blockBelow, groupedBooks);
                                dropItem(world, blockBelow, baseItem);
                            }

                            // Book with multiple enchantments (not marked as Grouped=true)
                            else if (isBook) {
                                // Split each enchantment into individual books
                                for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                                    ItemStack singleBook = new ItemStack(Items.ENCHANTED_BOOK);
                                    EnchantedBookItem.addEnchantment(singleBook,
                                            new EnchantmentInstance(entry.getKey(), entry.getValue()));
                                    if (singleBook.getTag() != null) {
                                        singleBook.getTag().remove("Grouped");
                                    }
                                    // Drop individual enchanted book
                                    dropItem(world, blockBelow, singleBook);
                                }
                            }

                            // Discard the original item
                            itemEntity.discard();
                        }
                    }
                }
            }
        }
    }

    // CUSTOM EVENT - Overpower Mending custom enchantment
    @SubscribeEvent
    public static void activatedOverpowerMendingEnchantment(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if ((event.phase == TickEvent.Phase.END) || !player.level().isClientSide()) {
            // Desired value for level.getGameTime() % X != 0 (1 second = 20 ticks)
            // Once every [0.5, 1, 2, 5] seconds -> X = [10, 20, 40, 100] ticks
            if (player.level().getGameTime() % 100 != 0) { return; }
            List<NonNullList<ItemStack>> playerSlots = List.of(player.getInventory().items,
                    player.getInventory().armor, player.getInventory().offhand);
            for (NonNullList<ItemStack> itemStacks : playerSlots) {
                for (ItemStack stack : itemStacks) {
                    if (!stack.isEmpty() && stack.isDamaged() &&
                            stack.getEnchantmentLevel(ModEnchantments.OVERPOWER_MENDING.get()) > 0) {
                        int currentDamage = stack.getDamageValue();
                        // Repairs 5 point of damage at a time
                        int repairAmount = Math.min(currentDamage, 1);
                        stack.setDamageValue(currentDamage - repairAmount);
                    }
                }
            }
        }
    }

    // CUSTOM EVENT - NOTHING EFFECT
    @SubscribeEvent
    public static void activatedNothingEffect(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Warden wardenEntity) {
            Level level = wardenEntity.level();
            // Checks if there is a player with the effect active nearby
            List<Player> players = level.getEntitiesOfClass(Player.class,
                    wardenEntity.getBoundingBox().inflate(32));
            for (Player player : players) {
                if (player.hasEffect(ModEffects.NOTHING_EFFECT.get())) {
                    event.setCanceled(true); // Prevents the Warden from spawning
                    break;
                }
            }
        }
    }
}