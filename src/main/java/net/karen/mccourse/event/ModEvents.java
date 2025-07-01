package net.karen.mccourse.event;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.datafixers.util.Either;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.block.ModBlocks;
import net.karen.mccourse.block.custom.MagicDisenchantedBlock;
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
import net.minecraft.client.gui.*;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.*;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
import net.minecraft.world.scores.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.*;
import net.minecraftforge.event.entity.*;
import net.minecraftforge.event.entity.item.*;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.*;
import net.minecraftforge.event.level.*;
import net.minecraftforge.event.village.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.command.ConfigCommand;
import org.lwjgl.glfw.GLFW;
import java.util.*;
import static net.karen.mccourse.item.custom.MccourseBottleItem.createMccourseBottleWithXP;
import static net.karen.mccourse.item.custom.XrayItem.*;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;
import static net.minecraft.network.chat.CommonComponents.*;

@Mod.EventBusSubscriber(modid = MCCourseMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    /* CUSTOM EVENT - Hammer's tool - Don't be a jerk License - Done with the help of
       https://github.com/CoFH/CoFHCore/blob/1.19.x/src/main/java/cofh/core/event/AreaEffectEvents.java */
    private static final Set<BlockPos> HARVESTED_BLOCKS = new HashSet<>(); // Hammer's receive blocks range

    @SubscribeEvent
    public static void onHammerUsage(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer(); // Player is using Hammer tool
        ItemStack mainHandItem = player.getMainHandItem();
        BlockPos initalBlockPos = event.getPos();
        if (HARVESTED_BLOCKS.contains(initalBlockPos)) { return; } // Different type of blocks
        // If player destroyed a block with Hammer tool
        if (mainHandItem.getItem() instanceof HammerItem hammer && player instanceof ServerPlayer serverPlayer) {
            // Player's position to break a block with Hammer tool
            for (BlockPos pos : HammerItem.getBlocksToBeDestroyed(hammer.getDistance(),
                hammer.getRadius(), initalBlockPos, serverPlayer)) {
                boolean item = hammer.isCorrectToolForDrops(mainHandItem, event.getLevel().getBlockState(pos));
                if (pos == initalBlockPos || !item) { continue; }
                // Have to add them to a Set otherwise, the same code right here will get called for each block!
                HARVESTED_BLOCKS.add(pos);
                serverPlayer.gameMode.destroyBlock(pos); // Player destroyed block with Hammer tool
                HARVESTED_BLOCKS.remove(pos);
            }
        }
    }

    // CUSTOM EVENT - Home's commands
    @SubscribeEvent
    public static void onCommandsRegister(RegisterCommandsEvent event) { // Register all custom commands
        new SetHomeCommand(event.getDispatcher()); // SET HOME command
        new ReturnHomeCommand(event.getDispatcher()); // RETURN HOME command
        new DeleteHomeCommand(event.getDispatcher()); // DELETE HOME command
        new ListHomesCommand(event.getDispatcher()); // LIST ALL HOMES command
        ConfigCommand.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) { // If player dies is respawned where saved the SET HOME
        event.getEntity().getPersistentData().putIntArray("mccourse.homepos",
                event.getOriginal().getPersistentData().getIntArray("mccourse.homepos"));
    }

    // CUSTOM EVENT - An event example that to show if player hit on sheep entity using specific items
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
            // Received Iron pickaxe with Efficiency 4
            trades.get(2).add(normalTrade(createMccourseBottleWithXP(1000), new ItemStack(Items.IRON_PICKAXE),
                    () -> createEnchantedItem(Items.IRON_PICKAXE, Enchantments.BLOCK_EFFICIENCY, 4),
                    20, 100, 0.08f));
            // Received Enchanted Book with Fortune 4
            trades.get(3).add(normalTrade(createMccourseBottleWithXP(500), new ItemStack(Items.BOOK),
                    () -> createEnchantedBook(Enchantments.BLOCK_FORTUNE, 4), 30, 50, 1f));
        }
    }

    @SubscribeEvent
    public static void addWanderingTrades(WandererTradesEvent event) {
        // List of all generic and rare trades that the player can trade because not exist levels
        List<VillagerTrades.ItemListing> generic = event.getGenericTrades(), rare = event.getRareTrades();
        // Received KOHLRABI like Generic Trades
        wandering(generic, List.of(Items.EMERALD, ModItems.KOHLRABI.get()), List.of(2, 6, 10, 2), 0.02f);
        // Received KOHLRABI SEEDS like Rare Trades
        wandering(rare, List.of(Items.EMERALD, ModItems.KOHLRABI_SEEDS.get()), List.of(5, 1, 3, 2), 0.01f);
    }

    // CUSTOM EVENT - RAINBOW | AUTO SMELT | MORE ORES | MAGNETIC | MULTIPLIER | ACCUMULATOR custom enchantments
    @SubscribeEvent
    public static void onPlayerWakeUp(PlayerWakeUpEvent event) { // Gain experience orb when sleep
        Player player = event.getEntity();
        Level level = player.level();
        if (!level.isClientSide()) { setPlayerXP(player, level, 10); }
    }

    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) { // Gain experience orb when fished
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) { if (!event.getDrops().isEmpty()) { setPlayerXP(player, level, 4); } }
    }

    @SubscribeEvent
    public static void onBlockBreakWithCustomEnchantments(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        LevelAccessor world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        ItemStack tool = player.getMainHandItem();
        Level level = (Level) event.getLevel();
        int fortune = enchant(tool, Enchantments.BLOCK_FORTUNE), multiplier = enchant(tool, ModEnchantments.MULTIPLIER.get()),
        moreOres = enchant(tool, ModEnchantments.MORE_ORES.get()), accumulator = enchant(tool, ModEnchantments.ACCUMULATOR.get());
        var blockTag = ForgeRegistries.BLOCKS.tags();
        if (!level.isClientSide() && world instanceof ServerLevel serverLevel) {
            boolean cancelVanillaDrop = false; // Adapt the drop according to the enchantment being true
            List<ItemStack> finalDrops = new ArrayList<>(); // Items caused by enchantments are stored in the list
            int oresFortune = serverLevel.random.nextInt(fortune + 1);
            if (enchant(tool, ModEnchantments.RAINBOW.get()) > 0) { // * RAINBOW ENCHANTMENT *
                Map<Block, TagKey<Block>> rainbowMap = Map.ofEntries(Map.entry(Blocks.COAL_BLOCK, Tags.Blocks.ORES_COAL),
                Map.entry(Blocks.COPPER_BLOCK, Tags.Blocks.ORES_COPPER), Map.entry(Blocks.DIAMOND_BLOCK, Tags.Blocks.ORES_DIAMOND),
                Map.entry(Blocks.EMERALD_BLOCK, Tags.Blocks.ORES_EMERALD), Map.entry(Blocks.GOLD_BLOCK, Tags.Blocks.ORES_GOLD),
                Map.entry(Blocks.IRON_BLOCK, Tags.Blocks.ORES_IRON), Map.entry(Blocks.LAPIS_BLOCK, Tags.Blocks.ORES_LAPIS),
                Map.entry(Blocks.REDSTONE_BLOCK, Tags.Blocks.ORES_REDSTONE),
                Map.entry(Blocks.NETHERITE_BLOCK, Tags.Blocks.ORES_NETHERITE_SCRAP),
                Map.entry(ModBlocks.ALEXANDRITE_BLOCK.get(), ModTags.Blocks.ALEXANDRITE_ORES),
                Map.entry(ModBlocks.PINK_BLOCK.get(), ModTags.Blocks.PINK_ORES));
                for (Map.Entry<Block, TagKey<Block>> entry : rainbowMap.entrySet()) {
                    // block(...) -> Blocks normal break || return; -> Other enchantments are not applied
                    if (state.is(entry.getValue())) { block(world, pos, entry.getKey(), event); return; }
                }
                if (state.is(ModTags.Blocks.RAINBOW_DROPS)) {
                    ItemStack rainbowDrop = new ItemStack(state.getBlock());
                    if (fortune > 0) { rainbowDrop.setCount(rainbowDrop.getCount() * (1 + oresFortune)); }
                    finalDrops.add(rainbowDrop);
                    cancelVanillaDrop = true;
                }
            }
            if (moreOres > 0) { // * MORE ORES ENCHANTMENT *
                List<TagKey<Block>> oresTags = List.of(ModTags.Blocks.MORE_ORES_ONE_DROPS, ModTags.Blocks.MORE_ORES_TWO_DROPS,
                ModTags.Blocks.MORE_ORES_THREE_DROPS, ModTags.Blocks.MORE_ORES_FOUR_DROPS, ModTags.Blocks.MORE_ORES_FIVE_DROPS,
                ModTags.Blocks.MORE_ORES_SIX_DROPS);
                if (blockTag != null) {
                    if (is(state, Blocks.STONE, 0.1f, tool, 1) ||
                        is(state, Blocks.NETHERRACK, 0.01f, tool, 2)) {
                        blockTag.getTag(oresTags.get(moreOres - 1)).getRandomElement(RandomSource.create()).ifPresent(block -> {
                            ItemStack drop = new ItemStack(block); // Increase ore drop with Multiplier enchantment
                            if (fortune > 0) { drop.setCount(drop.getCount() * (1 + oresFortune)); }
                            finalDrops.add(drop); // Break block and ore chance drop
                        });
                        cancelVanillaDrop = true;
                    }
                    else if (is(state, Blocks.STONE, 0.05f, tool, 3)) {
                        blockTag.getTag(ModTags.Blocks.MORE_ORES_ALL_DROPS).forEach(block -> {
                            ItemStack drop = new ItemStack(block);
                            if (fortune > 0) { drop.setCount(drop.getCount() * (1 + oresFortune)); }
                            finalDrops.add(drop);
                        });
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
                    if (drop.is(ModTags.Items.MULTIPLIER_ORES)) {
                        multiplied.setCount(drop.getCount() * multiplier); // Duplicate drops with Multiplier
                        multipliedDrops.add(multiplied);
                    }
                    else { multipliedDrops.add(multiplied); }});
                finalDrops.clear(); // Remove the non-multiplied originals
                finalDrops.addAll(multipliedDrops); // Adds the multiplied values
            }
            if (accumulator > 0 && !player.level().isClientSide()) { // * ACCUMULATOR ENCHANTMENT *
                // Gain experience orb when mined block and checks if the broken block is one that usually does not give XP
                if (state.is(ModTags.Blocks.ACCUMULATOR_EXPERIENCE)) {
                    int xp = accumulator; // Amount of XP you want to give - Default gain 1 experience orb per level
                    if (multiplier > 1) { xp = accumulator * multiplier; } // Gain 2 experience orb per level
                    setPlayerXP(player, level, xp);
                }
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
            ItemStack helmet = has(player, EquipmentSlot.HEAD); // Player has an item on HELMET slot
            UUID playerUUID = player.getUUID(); // Player UUID -> Detected GLOWING MOBS stage
            boolean isEnchanted = helmet.isEnchanted() && enchant(helmet, ModEnchantments.GLOWING_MOBS.get()) > 0;
            if (!isEnchanted) { glowingState.remove(playerUUID); } // Player hasn't GLOWING MOBS is disabled
            boolean current = glowingState.getOrDefault(playerUUID, false); // GLOWING MOBS default stage is FALSE
            if (KeyBinding.GLOWING_MOBS_KEY.consumeClick() && KeyBinding.GLOWING_MOBS_KEY.isDown()) { // Press [M] key input
                if (isEnchanted) { // Player has a HELMET inputted on slot and GLOWING MOBS enchantment level
                    boolean newState = !current; // Default stage is FALSE
                    glowingState.put(playerUUID, newState); // Adapted "newState" of "current" stage
                    glow(player, newState, "Mobs: ON!", "Mobs: OFF!"); // Toggle ON/OFF
                }
                else { invalidMessage(player, "Mobs: Enchanted helmet!"); } // Hasn't item
            }
            if (current && isEnchanted) {
                // Key (Color) -> Each group represent with some color. Value (Group tag name) -> Represent as Tag.
                Map<ChatFormatting, TagKey<EntityType<?>>> entitiesTag = Map.ofEntries(
                // Monsters, Animal | Flying entities, Water animals and Villagers
                Map.entry(red, ModTags.Entities.MONSTERS), Map.entry(blue, ModTags.Entities.ANIMALS),
                Map.entry(yellow, ModTags.Entities.WATER_ANIMALS), Map.entry(darkPurple, ModTags.Entities.VILLAGER));
                entitiesTag.forEach((color, tag) -> { // Added GLOWING effect for each GROUP
                    String teamName = tag.location().getPath();
                    List<LivingEntity> entities = getPlayer(player, tag); // Range of GLOWING effect on mobs
                    if (!entities.isEmpty()) { // Groups not empty
                        Scoreboard score = player.getScoreboard();
                        PlayerTeam team = score.getPlayerTeam(teamName);
                        // Added each entity on group with specif tag and color on entitiesTag
                        if (team == null) { team = score.addPlayerTeam(teamName); team.setColor(color); }
                        PlayerTeam finalTeam = team; // Each entity received GLOWING effect with specif color on entitiesTag
                        entities.forEach(entity -> {
                            entity.addEffect(effect(MobEffects.GLOWING, 20, 1));
                            score.addPlayerToTeam(entity.getScoreboardName(), finalTeam);
                        });
                    }
                });
            }
        }
    }

    // CUSTOM EVENT - Enchantment tooltips
    @SubscribeEvent
    public static void enchantmentTooltipDescriptions(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        List<Component> tooltip = event.getToolTip(); // Original line
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if (!stack.isEmpty() && stack.isEnchanted() || stack.getItem() == Items.ENCHANTED_BOOK) {
            if (!enchantments.isEmpty()) {
                for (int i = 0; i < tooltip.size(); i++) {
                    String raw = ChatFormatting.stripFormatting(tooltip.get(i).getString()); // Detected old line
                    for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                        Enchantment enchantment = entry.getKey(); // Enchantment name
                        int level = entry.getValue(); // Enchantment level
                        String expected = standardTranslatable(enchantment.getDescriptionId()).getString();
                        if (raw != null && raw.startsWith(expected)) { // Raw has "expected" line replace old to new tooltip
                            boolean isCurse = enchantment.isCurse();
                            ChatFormatting color = isCurse ? red :
                                switch (enchantment.category) { // Replace this line with custom styled version
                                    case ARMOR, ARMOR_HEAD, ARMOR_CHEST, ARMOR_LEGS, ARMOR_FEET -> gold; case FISHING_ROD -> yellow;
                                    case DIGGER -> darkPurple; case BREAKABLE -> darkGreen; case TRIDENT -> aqua;
                                    case WEAPON -> darkRed; case BOW, CROSSBOW -> green; default -> gray; };
                            // JSON file -> I18n = en_us.json
                            String enchant = enchantment.getDescriptionId(), descriptionValue = enchant + ".desc";
                            if (level > 0 || I18n.exists(descriptionValue)) {
                                // Enchantment Level with Arabic numeral + Enchantment compatibility + Enchantment description
                                MutableComponent name = description(enchant, color, List.of(!isCurse, isCurse))
                                .append(standardLiteral(" " + level + " ")).append(icon(isCurse, enchantment)),
                                desc = description(descriptionValue, color, List.of(false, false));
                                // Number line of enchantments and enchantment descriptions
                                tooltip.set(i, name);
                                tooltip.add(i + 1, desc);
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (enchant(stack, ModEnchantments.UNLOCK.get()) > 0) { tooltip.add(EMPTY); } // Item with UNLOCK enchantment
    }

    // Credits by Parlack - Pickaxe modes - https://www.youtube.com/watch?v=pBo1c3hM3b0
    // CUSTOM EVENT - Custom Modes Pickaxe event GUI - Using code with some modifications
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void eventHandler(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        int x = 10 , y = event.getWindow().getGuiScaledHeight() - 30;
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

    // CUSTOM EVENT - Overlay: X Y Z coordinates and Light
    @SubscribeEvent
    public static void overlayCoordinateLight(RenderGuiOverlayEvent event) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;
        if (!mc.options.renderDebug && mc.screen == null) {
            if (player != null && mc.level != null) { // Render only when the player is in the game and not in the menu
                double x = player.getX(), y = player.getY(), z = player.getZ(); // Player x, y, z coordinates
                BlockPos pos = player.blockPosition();
                int blockLight = light(mc, LightLayer.BLOCK, pos), skyLight = light(mc, LightLayer.SKY, pos),
                    totalLight = Math.max(blockLight, skyLight);
                Font font = mc.font;
                GuiGraphics guiGraphics = event.getGuiGraphics();
                // Text to be displayed on screen - LIGHT, SKY, BLOCK
                Component coordinate = ChatUtil.literal(x, y, z), light = ChatUtil.numbers(totalLight, skyLight, blockLight);
                guiGraphics.drawString(font, coordinate, 10, 10, 0x5597DF); // X, Y, Z
                guiGraphics.drawString(font, light, 10, 20, 0xDBE947); // LIGHT
            }
        }
    }

    // CUSTOM EVENT - FLY custom effect
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

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) { // Where render block shape on world
        if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_SKY) { stage(List.of(1, 0), List.of(false, true), event); }
        else if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_PARTICLES) {
            stage(List.of(2, 0), List.of(true, true), event);
        }
    }

    @SubscribeEvent
    public static void activatedGlowingBlocksEnchantment(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        LevelAccessor world = player.level();
        if (event.phase == TickEvent.Phase.END) {
            GlowingBlocksNetworkMessage.World worldVar = GlowingBlocksNetworkMessage.World.get(world);
            String mode = getActiveMode(player), nameMode = mode.toUpperCase(); // Item mode
            boolean hasItem = !mode.equals("none"); // Default mode
            if (KeyBinding.GLOWING_BLOCKS_KEY.isDown() && KeyBinding.GLOWING_BLOCKS_KEY.consumeClick()) {
                if (hasItem) { // Has enchanted HELMET or Metal Detector
                    boolean newState = !worldVar.xray; // Adapted "newState" of "worldVar.xray" stage
                    change(worldVar, newState, world); // Toggle ON/OFF
                    glow(player, newState, "Blocks: " + nameMode + " ON!", "Blocks: " + nameMode + " OFF!");
                }
                else { invalidMessage(player, "Blocks: Enchanted helmet or Metal detector!"); } // Hasn't item
            }
            if (!hasItem && worldVar.xray) { change(worldVar, false, world); } // Glowing Blocks disabled
        }
    }

    // CUSTOM EVENT - Decapitator
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
                    // Maximum search distance + Height limit (e.g. 10 blocks above and below)
                    int maxDistance = 50, maxHeight = 512, logCount = 0;
                    while (!toVisit.isEmpty()) {
                        BlockPos pos = toVisit.poll();
                        // Check if it is already visited or within the height limit
                        if (!visited.add(pos) || Math.abs(pos.getY() - origin.getY()) > maxHeight) { continue; }
                        for (int dx = -1; dx <= 1; dx++) { // Check the surrounding blocks (relative to the current position)
                            for (int dy = -1; dy <= 1; dy++) {
                                for (int dz = -1; dz <= 1; dz++) {
                                    BlockPos offset = pos.offset(dx, dy, dz); // Limit horizontal distance
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
                    // Applies damage proportional to the amount of logs broken
                    if (logCount > 0) { hurtTool(tool, logCount, player); }
                }
            }
        }
    }

    // CUSTOM EVENT - Block Fly custom enchantment
    @SubscribeEvent
    public static void activatedBlockFlyEnchantment(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity(); // Entity is a player
        int efficiency = hasEnchant(Enchantments.BLOCK_EFFICIENCY, player),
            blockFly = hasEnchant(ModEnchantments.BLOCK_FLY.get(), player);
        newSpeed(event, blockFly > 0, player, 5); // There is Block Fly enchantment -> OLD speed * NEW speed (5)
        // There is Block Fly and Efficiency enchantments -> OLD speed * (NEW speed (5) * efficiency level)
        newSpeed(event, blockFly > 0 && efficiency > 0, player, (5 + efficiency));
    }

    // CUSTOM EVENT - Mccourse Elevator advanced block
    @SubscribeEvent
    public static void activatedMccourseElevatorOnKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null) { // Checks if the player is over the elevator
            BlockPos pos = BlockPos.containing(player.getX(), player.getY() - 1, player.getZ());
            if (player.level().getBlockState(pos).getBlock() == ModBlocks.MCCOURSE_ELEVATOR.get()) { // Detects JUMP or SHIFT
                if (InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_SPACE)) {
                    network(new MccourseElevatorKeyInputMessage(true));
                }
                if (player.isShiftKeyDown()) { network(new MccourseElevatorKeyInputMessage(false)); }
            }
        }
    }

    // CUSTOM EVENT - ETERNAL custom enchantment
    // Map of Main hand + Items, Armor, Offhand, Experience and Vault items
    private static final Map<UUID, List<ItemStack>> preservedItems = new HashMap<>(),
    preservedArmor = new HashMap<>(), preservedOffhand = new HashMap<>(), preservedVault = new HashMap<>();
    private static final Map<UUID, int[]> preservedExperience = new HashMap<>();

    @SubscribeEvent
    public static void activatedEternalEnchantmentOnPlayerDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof Player player) { // Entity is player
            UUID playerUUID = player.getUUID(); // Player UUID
            // Added all Inventory slots, Armor slots and Offhand slot + Added rest items on Vault item
            List<ItemStack> vaultItems = new ArrayList<>(), inventoryPreserve = new ArrayList<>(Collections.nCopies(36, empty)),
            armorPreserve = new ArrayList<>(Collections.nCopies(4, empty)),
            offhandPreserve = new ArrayList<>(Collections.nCopies(1, empty));
            int[] experienceData = new int[] { player.experienceLevel, Float.floatToIntBits(player.experienceProgress),
            player.totalExperience }; // Added player experience
            // Get all items on Main inventory, Armor and Left hand or Offhand slots
            setPreservedVault(player.getInventory().items, inventoryPreserve, vaultItems);
            setPreservedVault(player.getInventory().armor, armorPreserve, vaultItems);
            setPreservedVault(player.getInventory().offhand, offhandPreserve, vaultItems);
            preservedItems.put(playerUUID, inventoryPreserve); // Save items data from preserved ITEMS, ARMOR and OFFHAND
            preservedArmor.put(playerUUID, armorPreserve);
            preservedOffhand.put(playerUUID, offhandPreserve);
            preservedExperience.put(playerUUID, experienceData);
            player.experienceLevel = 0; // Reset EXPERIENCE to prevent drop
            player.experienceProgress = 0;
            player.totalExperience = 0;
            // Display PLAYER NAME, Player death (X, Y and Z) positions and TIME showing (Hours::Minutes::Seconds)
            Component displayName = itemChatMessage(player, player.blockPosition(), green);
            if (!vaultItems.isEmpty()) { // Saves items from the Vault
                ItemStack vaultItem = new ItemStack(ModItems.VAULT.get());
                CompoundTag vaultTag = new CompoundTag();
                ListTag itemListTag = new ListTag();
                vaultItems.forEach(item -> { // Create VaultItem with the items data
                    CompoundTag itemTag = new CompoundTag(); item.save(itemTag); itemListTag.add(itemTag); });
                vaultTag.put("VaultItems", itemListTag); // Added information on Vault item
                vaultTag.putString("DisplayName", displayName.toString()); // Save custom name in NBT
                vaultItem.setTag(vaultTag);
                vaultItem.setHoverName(displayName);
                /* Temporarily saved for the clone event; Add directly to the new Player's inventory in onClone()
                   Try adding to a free inventory slot. */
                preservedVault.computeIfAbsent(playerUUID, k -> new ArrayList<>()).add(vaultItem);
            }
        }
    }

    @SubscribeEvent
    public static void activatedEternalEnchantmentOnPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) { // Ensures that it only runs AFTER death
            UUID playerUUID = event.getOriginal().getUUID(); // Get Player UUID
            // Entity is Player -> After death | Old player -> Before death
            Player newPlayer = event.getEntity(), original = event.getOriginal();
            BlockPos blockPos = original.blockPosition(); // Player position after death
            newPlayer.sendSystemMessage(itemChatMessage(newPlayer, blockPos, gold)); // Player death message on chat
            // Restore all Inventory, Armor and Offhand saved slots
            setRestoredVault(newPlayer.getInventory().items, preservedItems.remove(playerUUID));
            setRestoredVault(newPlayer.getInventory().armor, preservedArmor.remove(playerUUID));
            setRestoredVault(newPlayer.getInventory().offhand, preservedOffhand.remove(playerUUID));
            int[] experienceData = preservedExperience.remove(playerUUID); // Restore Experience and removed all experience saved
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
            else if (cropAge(state, Blocks.NETHER_WART, NetherWartBlock.AGE, 3)) { // Nether Wart
                crop(Blocks.NETHER_WART, state, level, pos, player, event, heldItem);
            }
            else if (cropAge(state, Blocks.COCOA, CocoaBlock.AGE, 2)) { // Cocoa
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
                        List<ItemEntity> itemsBelow = getItem(world, blockBelow);
                        if (itemsBelow.size() != 1) { continue; } // Processing only if there is exactly ONE item
                        ItemEntity itemEntity = itemsBelow.get(0); // First item of list
                        ItemStack item = itemEntity.getItem(); // Get real item
                        // Get all enchantments of the item
                        Map<Enchantment, Integer> enchantments = getEnch(item);
                        boolean isBook = item.is(Items.ENCHANTED_BOOK);
                        /* Ignore if item has no enchantment or if item is a book with only 1 enchantment
                           Only process if it's not a previously split book (to avoid infinite loop) */
                        if (enchantments.isEmpty() || (isBook && enchantments.size() == 1)) { continue; }
                        // Drop an enchanted book with the enchantments of tool, armor, etc.
                        if (item.isDamageableItem() && !isBook) {
                            // Added each enchantment found on tool, armor, etc. + Drop enchanted book WITH enchantments
                            groupedEnch(enchantments, world, blockBelow);
                            ItemStack baseItem = item.copy(); // Set original item WITHOUT enchantments
                            MagicDisenchantedBlock.enchant(world, blockBelow, baseItem); // Drop item WITHOUT enchantments
                        }
                        // Book with multiple enchantments - Split each enchantment into individual books
                        else if (isBook) { individualEnch(enchantments, world, pos); } // Drop individual enchanted book
                        itemEntity.discard(); // Discard the original item
                    }
                }
            }
        }
    }

    // CUSTOM EVENT - ANVIL enchantment compatibilities
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft(), right = event.getRight(); // LEFT -> Base item | RIGHT -> Book or Second item
        if (!left.isEmpty() && !right.isEmpty()) {
            ItemStack output = left.copy(); // Result of Base item + Book or Second item
            boolean modified = false;
            Map<Enchantment, Integer> leftEnch = getEnch(left), rightEnch = getEnch(right);
            for (Map.Entry<Enchantment, Integer> entry : rightEnch.entrySet()) {
                Enchantment ench = entry.getKey(); // Enchantment
                // LEFT -> Enchantment level || RIGHT -> Book or Second item enchantment level
                int rightLvl = entry.getValue(), leftLvl = leftEnch.getOrDefault(ench, 0),
                    newLevel = leftLvl == rightLvl ? rightLvl + 1 : Math.max(leftLvl, rightLvl); // Output with new level
                leftEnch.put(ench, Math.min(ench.getMaxLevel(), newLevel)); // Here we ignore the compatibility check.
                modified = true;
            }
            if (modified) { // Output item
                EnchantmentHelper.setEnchantments(leftEnch, output);
                event.setOutput(output);
                event.setCost(1); // Cost at levels
                event.setMaterialCost(1); // Cost of materials (e.g. books, diamonds etc.)
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
            List<NonNullList<ItemStack>> playerSlots =
            List.of(player.getInventory().items, player.getInventory().armor, player.getInventory().offhand);
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

    // CUSTOM EVENT - MULTIPLIER custom enchantment
    @SubscribeEvent
    public static void activatedMultiplierEnchantment(LivingDropsEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            int level = enchant(player.getMainHandItem(), ModEnchantments.MULTIPLIER.get());
            if (level > 1) {
                List<ItemEntity> originalDrops = new ArrayList<>(event.getDrops());
                originalDrops.forEach(drop -> {
                    ItemStack stack = drop.getItem().copy();
                    stack.setCount(stack.getCount() * level); // Multiplier adapt on level
                    dropWorld(event, drop.level(), drop.getX(), drop.getY(), drop.getZ(), stack); });
            }
        }
    }

    // CUSTOM EVENT - MOBS CRITICAL custom enchantment
    @SubscribeEvent
    public static void activatedMobsCriticalEnchantment(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            int mobsCritical = enchant(player.getMainHandItem(), ModEnchantments.MOBS_CRITICAL.get());
            if (mobsCritical > 0 && !(player.fallDistance > 0 || !player.onGround())) {
                float baseDamage = event.getAmount();
                event.setAmount(baseDamage + (baseDamage * (0.5F * mobsCritical))); // Critical damage (50% extra) per level
                sound(player, SoundEvents.PLAYER_ATTACK_CRIT, 1.0F, 1.0F); // Particle effect and sound
                particle(player, event.getEntity());
            }
        }
    }

    // CUSTOM EVENT - IMMORTAL custom enchantment
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
            for (Entity entity : getRadiusItem(event)) {
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

    // CUSTOM EVENT - TELEPORT item
    @SubscribeEvent
    public static void teleportOnItemRightClick(PlayerInteractEvent.RightClickItem event) {
        Player player = event.getEntity();
        ItemStack stack = event.getItemStack();
        if (!player.level().isClientSide()) { // Teleport when using item (like tools, etc.)
            if (!stack.isEmpty() && stack.is(ModTags.Items.TELEPORT_ITEMS)) { // Check if holding a tool
                // 1. Get the direction the player is looking; 2. It starts from the eyes;
                // 3. Block render distance. How many blocks ahead to ray trace (reach distance).
                Vec3 look = player.getLookAngle(), start = player.getEyePosition(), end = start.add(look.scale(5.0));
                BlockHitResult hitResult = hitBlock(player, start, end); // Ray trace until it hits a block
                if (hitResult.getType() == HitResult.Type.BLOCK) {
                    BlockPos blockPos = hitResult.getBlockPos(); // Teleports to the top of the block hit (+1 height)
                    double x = blockPos.getX() + 0.5, y = blockPos.getY() + 1.0, z = blockPos.getZ() + 0.5;
                    float xRot = player.getXRot(), yRot = player.getYRot();
                    ((ServerPlayer) player).teleportTo((ServerLevel) player.level(), x, y, z, yRot, xRot);
                }
            }
        }
    }

    // CUSTOM EVENT - MCCOURSE BOTTLE item
    @SubscribeEvent
    public static void mccourseBottleLeftShiftInputEvent(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player != null && mc.level != null && (event.phase == TickEvent.Phase.END)) {
            if (mc.options.keyAttack.isDown()) {
                if (player.getMainHandItem().getItem() instanceof MccourseBottleItem) {
                    boolean shift = InputConstants.isKeyDown(mc.getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT);
                    int amount = shift ? MccourseBottleItem.storeXp : 1; // Pressed LEFT click + SHIFT
                    network(new MccourseBottleKeyInputMessage(MccourseBottleActionItem.STORE, amount));
                }
            }
        }
    }

    @SubscribeEvent
    public static void mccourseBottleKeysBNInputEvent(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) { return; }
        boolean shift = Screen.hasShiftDown();
        int storeAmount = shift ? 100 : 10, restoreAmount = shift ? 100 : 10; // Pressed SHIFT + B | SHIFT + N
        if (KeyBinding.MCCOURSE_BOTTLE_STORED_TEN_LEVELS_KEY.consumeClick()) { // Send to server -> STORE
            network(new MccourseBottleKeyInputMessage(MccourseBottleActionItem.STORE, storeAmount));
        }
        if (KeyBinding.MCCOURSE_BOTTLE_RESTORED_TEN_LEVELS_KEY.consumeClick()) { // Send to server -> RESTORE
            network(new MccourseBottleKeyInputMessage(MccourseBottleActionItem.RESTORED, restoreAmount));
        }
    }

    // CUSTOM EVENT - INFINITE and LEVEL CHARGER items
    @SubscribeEvent
    public static void itemOnMouseClick(ScreenEvent.MouseButtonPressed.Pre event) {
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) { return; }
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) { return; }
        ItemStack carried = player.containerMenu.getCarried();
        if (!(carried.getItem() instanceof InfiniteItem || carried.getItem() instanceof LevelChargerItem)) { return; }
        double mouseX = event.getMouseX(), mouseY = event.getMouseY();
        int button = event.getButton();
        if (button != 0) { return; }
        for (Slot slot : screen.getMenu().slots) {
            int x = screen.getGuiLeft() + slot.x, y = screen.getGuiTop() + slot.y;
            if (mouseX >= x && mouseX < x + 16 && mouseY >= y && mouseY < y + 16) {
                ItemStack target = slot.getItem();
                if (target.isEmpty() || target == carried) { return; }
                network(new InfiniteInventorySlotMessage(slot.index)); // Send to the server -> Infinite item
                network(new LevelChargerInventorySlotMessage(slot.index)); // Send to the server -> Level Charger item
                if (!player.getAbilities().instabuild) { // Consume item on client (immediate visual effect)
                    carried.shrink(1);
                    player.containerMenu.broadcastChanges();
                }
                event.setCanceled(true);
                return;
            }
        }
    }

    // CUSTOM EVENT - MAGNETISM enchantment
    @SubscribeEvent
    public static void activatedMagnetismEnchantment(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        if (player.level().isClientSide() || event.phase != TickEvent.Phase.END) { return; }
        ItemStack legging = player.getItemBySlot(EquipmentSlot.LEGS);
        int level = enchant(legging, ModEnchantments.MAGNETISM.get());
        // To not run all the time, only every TICK_INTERVAL = 20 ticks
        if (level <= 0 || player.tickCount % 20 != 0) { return; } // 1 second (20 ticks)
        double range = 5.0 + level * 2; // Range increases with level
        // Search for items near the player
        List<ItemEntity> items = player.level().getEntitiesOfClass(ItemEntity.class, player.getBoundingBox().inflate(range));
        for (ItemEntity itemEntity : items) {
            if (itemEntity.isRemoved() || !itemEntity.isAlive()) { continue; }
            ItemStack stack = itemEntity.getItem().copy();
            if (player.getInventory().add(stack)) {
                itemEntity.remove(Entity.RemovalReason.DISCARDED);
                sound(player, SoundEvents.ITEM_PICKUP, 0.2F, ((player.getRandom().nextFloat() -
                player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F); // Play sound
            }
        }
        // Search for experience orbs near the player
        List<ExperienceOrb> orbs = player.level().getEntitiesOfClass(ExperienceOrb.class, player.getBoundingBox().inflate(range));
        for (ExperienceOrb orb : orbs) {
            if (!orb.isAlive() || orb.isRemoved()) { continue; }
            int xpValue = orb.getValue();
            player.giveExperiencePoints(xpValue); // Adds XP directly to the player
            orb.discard(); // Remove the orb from the world
            // Play sound
            sound(player, SoundEvents.EXPERIENCE_ORB_PICKUP, 0.1F, 0.5F + player.getRandom().nextFloat());
        }
    }

    // CUSTOM EVENT - UNLOCK custom enchantment
    @SubscribeEvent
    public static void activatedUnlockOnKeyPress(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || !KeyBinding.UNLOCK_KEY.isDown() || !KeyBinding.UNLOCK_KEY.consumeClick()) { return; }
        if (mc.screen == null) {
            ItemStack main = player.getMainHandItem(), off = player.getOffhandItem();
            Utils.unlockOnKeyPress(main, UnlockEnchantmentAction.MAIN, player.getInventory().selected); // MAIN HAND
            Utils.unlockOnKeyPress(off, UnlockEnchantmentAction.OFFHAND, 0); // OFFHAND
            for (int i = 0; i < player.getInventory().armor.size(); i++) { // ARMOR
                ItemStack armorItem = player.getInventory().armor.get(i);
                Utils.unlockOnKeyPress(armorItem, UnlockEnchantmentAction.ARMOR, i);
            }
        }
    }

    @SubscribeEvent
    public static void activatedUnlockOnGuiKeyPress(ScreenEvent.KeyPressed.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) { return; }
        if (player == null) { return; }
        if (event.getKeyCode() != KeyBinding.UNLOCK_KEY.getKey().getValue()) { return; }
        Slot hovered = screen.getSlotUnderMouse();
        if (hovered == null || !hovered.hasItem()) {
            tradeMessage(player, "No item under mouse!");
            return;
        }
        ItemStack hoveredStack = hovered.getItem();
        UnlockEnchantmentAction type = null;
        int index = -1;
        Inventory inv = player.getInventory();
        List<List<ItemStack>> sections = List.of(inv.items, inv.armor, inv.offhand);
        UnlockEnchantmentAction[] types = { UnlockEnchantmentAction.MAIN, UnlockEnchantmentAction.ARMOR,
                UnlockEnchantmentAction.OFFHAND};
        for (int sectionIndex = 0; sectionIndex < sections.size(); sectionIndex++) { // Sections list
            List<ItemStack> section = sections.get(sectionIndex);
            for (int i = 0; i < section.size(); i++) { // Section list
                if (ItemStack.isSameItemSameTags(section.get(i), hoveredStack)) {
                    type = types[sectionIndex];
                    index = i;
                    break;
                }
            }
            if (type != null) { break; }
        }
        if (type != null) {
            boolean locked = false;
            if (hoveredStack.getTag() != null) { locked = hoveredStack.getTag().getBoolean("Locked"); }
            network(new UnlockNetworkMessage(!locked, type, index));
            event.setCanceled(true); // Prevents other mods or the game from consuming the key
        }
        else { tradeMessage(player, "Slot does not belong to player's inventory!"); }
    }

    @SubscribeEvent
    public static void activatedUnlockOnDropKey(ScreenEvent.KeyPressed.Pre event) {
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (!(event.getScreen() instanceof AbstractContainerScreen<?> screen)) { return; }
        if (player == null) { return; }
        if (event.getKeyCode() != mc.options.keyDrop.getKey().getValue()) { return; } // Check if the PRESSED key is DROP
        Slot hoveredSlot = screen.getSlotUnderMouse();
        if (hoveredSlot == null) { return; }
        ItemStack stack = hoveredSlot.getItem();
        if (stack.isEmpty()) { return; }
        if (stack.getTag() != null && stack.hasTag() && stack.getTag().getBoolean("Locked")) { // Check if the item is LOCKED
            event.setCanceled(true); // Prevents item movement
            tradeMessage(player, "\uD83D\uDD12 This item is locked!");
        }
    }

    @SubscribeEvent
    public static void activatedUnlockItemToss(ItemTossEvent event) {
        ItemStack stack = event.getEntity().getItem(), safeCopy = stack.copy(); // Original item and make a safe copy first
        Player player = event.getPlayer();
        if (stack.getTag() != null && stack.hasTag() && stack.getTag().getBoolean("Locked")) {
            event.setCanceled(true);
            // If the item is LOCKED, and you try to play the item a warning is shown on the screen
            tradeMessage(player, "\uD83D\uDD12 This item is locked!");
            boolean added = player.getInventory().add(safeCopy);
            if (!added) {
                for (int i = 0; i < player.getInventory().armor.size(); i++) { // Try to put in ARMOR slots
                    unlockItemToss(player.getInventory().armor, i, safeCopy);
                }
                unlockItemToss(player.getInventory().offhand, 0, safeCopy); // Try to put it in OFFHAND
                if (!safeCopy.isEmpty()) { player.spawnAtLocation(safeCopy); } // If there is any left, throw it on the floor
            }
        }
    }

    @SubscribeEvent
    public static void activatedUnlockOnRenderTooltip(RenderTooltipEvent.GatherComponents event) {
        ItemStack item = event.getItemStack();
        if (enchant(item, ModEnchantments.UNLOCK.get()) > 0) { // Item contains UNLOCK enchantment
            List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements(); // Item TOOLTIP
            if (item.getTag() != null && item.hasTag()) {
                boolean locked = item.getTag().getBoolean("Locked"); // Locked NBT change stage
                image(elements, "textures/misc/unlock_on.png", 9, 9,
                        "§cItem locked! §7- Press §eV§7 §cto unlock", locked); // LOCKED
                image(elements, "textures/misc/unlock_off.png", 9, 9,
                        "§aItem unlocked! §7- Press §eV§7 §ato lock", !locked); // UNLOCKED
            }
        }
    }

    // CUSTOM EVENT - TOOLTIP position
    private static int offsetX = 0, offsetY = 0;
    private static boolean hoveringTooltip = false;

    @SubscribeEvent
    public static void onRenderTooltipPre(RenderTooltipEvent.Pre event) {
        if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>) {
            int mouseX = event.getX(), mouseY = event.getY(), height = event.getComponents().size() * 10,
                width = event.getFont().width(event.getComponents().get(0).toString()) + 10;
            // Set flag if mouse is over tooltip
            double mx = Minecraft.getInstance().mouseHandler.xpos() / Minecraft.getInstance().getWindow().getGuiScale(),
                   my = Minecraft.getInstance().mouseHandler.ypos() / Minecraft.getInstance().getWindow().getGuiScale();
            hoveringTooltip = mx >= mouseX && mx <= mouseX + width && my >= mouseY && my <= mouseY + height;
            event.setX(event.getX() + offsetX); // Apply offset
            event.setY(event.getY() + offsetY);
        }
    }

    @SubscribeEvent
    public static void onMouseScroll(ScreenEvent.MouseScrolled.Pre event) {
        if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>) {
            if (hoveringTooltip) { // Scroll moves the tooltip
                double delta = event.getScrollDelta();
                if (Screen.hasShiftDown()) { offsetX += delta > 0 ? 10 : -10; } // Side
                else { offsetY += delta > 0 ? 10 : -10; } // Up/down
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void onKeyPress(ScreenEvent.KeyPressed.Pre event) {
        if (Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>) {
            if (event.getKeyCode() == GLFW.GLFW_KEY_KP_DIVIDE) { // Divide key to reset
                offsetX = 0;
                offsetY = 0;
            }
        }
    }

    // CUSTOM EVENT - MINER Bow
    @SubscribeEvent
    public static void onArrowHitBlock(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof Arrow arrow)) { return; }
        CompoundTag tag = arrow.getPersistentData();
        if (!tag.getBoolean("MiningArrow")) { return; }
        Level level = arrow.level();
        if (level.isClientSide()) { return; }
        HitResult hitResult = event.getRayTraceResult();
        if (!(hitResult instanceof BlockHitResult blockHit)) { return; }
        Direction forward = Direction.values()[tag.getInt("MiningDirection")]; // Direction saved on shooting
        Direction.Axis axis = forward.getAxis();
        Direction right = (axis == Direction.Axis.X) ? Direction.SOUTH : Direction.EAST,
                     up = (axis == Direction.Axis.Y) ? Direction.NORTH : Direction.UP;
        BlockPos startPos = blockHit.getBlockPos();
        if (!(tag.hasUUID("ShooterUUID") && level instanceof ServerLevel serverLevel)) { return; }
        Player shooter = serverLevel.getPlayerByUUID(tag.getUUID("ShooterUUID"));
        if (shooter == null) { return; }
        // Retrieves the actual item used and ensures it is a MinerBowItem
        ItemStack main = shooter.getMainHandItem(), off = shooter.getOffhandItem(), usedBow;
        int radius, depth;
        if (main.getItem() instanceof MinerBowItem minerBow) {
            usedBow = main;
            radius = minerBow.getRadius();
            depth = minerBow.getDepth();
        }
        else if (off.getItem() instanceof MinerBowItem minerBow) {
            usedBow = off;
            radius = minerBow.getRadius();
            depth = minerBow.getDepth();
        }
        else { return; }
        int blocksBroken = 0;
        for (int i = 0; i < depth; i++) {
            BlockPos depthPos = startPos.relative(forward, i);
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    BlockPos targetPos = depthPos.relative(right, x).relative(up, y);
                    BlockState state = level.getBlockState(targetPos);
                    var blocks = ForgeRegistries.BLOCKS.tags();
                    if (blocks != null &&
                        (state.isAir() || blocks.getTag(ModTags.Blocks.MINER_BOW_BLACKLIST).contains(state.getBlock()))) {
                        continue;
                    }
                    if (state.getDestroySpeed(level, targetPos) < 0) { continue; }
                    level.destroyBlock(targetPos, true);
                    blocksBroken++;
                }
            }
        }
        usedBow.hurt(blocksBroken, shooter.getRandom(), shooter instanceof ServerPlayer ? (ServerPlayer) shooter : null);
        arrow.discard();
    }

    @SubscribeEvent
    public static void onUsingItem(LivingEntityUseItemEvent.Tick event) {
        if (!(event.getEntity() instanceof Player)) { return; }
        ItemStack stack = event.getItem();
        if (!(stack.getItem() instanceof BowItem)) { return; }
        int level = enchant(stack, ModEnchantments.LIGHTSTRING.get());
        if (level <= 0) { return; }
        int newDuration = event.getDuration() - level; // Decreases usage time (ex: 20 → 15)
        event.setDuration(newDuration);
    }
}