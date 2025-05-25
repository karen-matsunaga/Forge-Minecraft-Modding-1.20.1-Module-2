package net.karen.mccourse.item.custom;

import net.karen.mccourse.item.ModItems;
import net.karen.mccourse.sound.ModSounds;
import net.karen.mccourse.util.InventoryUtil;
import net.karen.mccourse.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

import static net.minecraft.ChatFormatting.*;

public class MetalDetectorItem extends Item {
    TagKey<Block> type;
    public MetalDetectorItem(Properties properties, TagKey<Block> type) {
        super(properties);
        this.type = type;
    }

    public TagKey<Block> getType() { return type; }

    // Function of Metal Detector item
    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer(); // Detected position of block
        Level level = context.getLevel();
        // Client and Server sides
        if (player != null) {
            if (!level.isClientSide()) {
                BlockPos positionClicked = context.getClickedPos(); // Block that player clicked
                boolean foundBlock = false; // Starting always false when not found an ore
                // Block of current layer up to layer -64
                for (int i = 0; i <= positionClicked.getY() + 64; i++) {
                    // Checked if found some ore
                    BlockState blockState = level.getBlockState(positionClicked.below(i));
                    // Custom method if found ore
                    if (isValuableBlock(blockState)) {
                        // Detected coordinates of block clicked
                        outputValuableCoordinates(positionClicked.below(i), player, blockState.getBlock());
                        foundBlock = true;
                        // If found ore the information is recorded in data tablet item
                        if (InventoryUtil.hasPlayerStackInInventory(player, ModItems.DATA_TABLET.get())) {
                            addDataToDataTablet(player, positionClicked.below(i), blockState.getBlock());
                        }
                        // If found ore it is sounded
                        level.playSeededSound(null, player.getX(), player.getY(), player.getZ(),
                                ModSounds.METAL_DETECTOR_FOUND_ORE.get(), SoundSource.BLOCKS, 1f, 1f, 0);

                        spawnFoundParticles(context, positionClicked, blockState); // If found ore it is particles
                        break; // Finished loop
                    }
                }
                if (!foundBlock) { outputNoValuableFound(player); } // Output message if not found ore
            }

            // Durability of Metal Detector item hurt
            context.getItemInHand().hurtAndBreak(1, player, hurt -> hurt.broadcastBreakEvent(hurt.getUsedItemHand()));
        }
        return InteractionResult.SUCCESS;
    }

    // Method that created custom particles if found an ore
    private void spawnFoundParticles(UseOnContext context, BlockPos positionClicked,
                                     BlockState blockState) {
        for (int i = 0; i < 20; i++) {
            ServerLevel level = (ServerLevel) context.getLevel();
            // Position of block and spawn particle
            level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState), positionClicked.getX() + 0.5d,
                    positionClicked.getY() + 1, positionClicked.getZ() + 0.5d, 1,
                    Math.cos(i * 18) * 0.15d, 0.15d, Math.sin(i * 18) * 0.15d, 0.1);
        }
    }

    // Data Tablet function
    private void addDataToDataTablet(Player player, BlockPos below, Block block) {
        ItemStack dataTablet = player.getInventory().getItem(
                InventoryUtil.getFirstInventoryIndex(player, ModItems.DATA_TABLET.get()));
        CompoundTag data = new CompoundTag();
        data.putString("mccourse.found_ore", "Valuable Found: " + I18n.get(block.getDescriptionId())
                + " at [X: " + below.getX() + ", Y: " + below.getY() + ", Z: " + below.getZ() + "]");
        dataTablet.setTag(data); // Added x, y, z coordinates on Data Tablet
    }

    // When player press Shift keyword appears more information about Metal Detector item
    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
        if (Screen.hasShiftDown()) { components.add(json("tooltip.mccourse.metal_detector.tooltip.shift")); }
        else { components.add(json("tooltip.mccourse.metal_detector.tooltip")); } // If NOT press SHIFT keyword
        super.appendHoverText(stack, level, components, tooltipFlag);
    }

    // Output message if not found ore -> Screen
    private void outputNoValuableFound(Player player) {
        message(player, json("item.mccourse.metal_detector.no_valuable_values"));
    }

    // Output message if found ore -> Screen
    private void outputValuableCoordinates(BlockPos pos, Player player, Block block) {
        ChatFormatting color = WHITE;
        Map<TagKey<Block>, ChatFormatting> oreColors = Map.ofEntries(Map.entry(Tags.Blocks.ORES_DIAMOND, AQUA),
        Map.entry(ModTags.Blocks.METAL_DETECTOR_COLORS, GOLD), Map.entry(Tags.Blocks.ORES_IRON, GRAY),
        Map.entry(Tags.Blocks.ORES_EMERALD, DARK_GREEN), Map.entry(Tags.Blocks.ORES_REDSTONE, DARK_RED),
        Map.entry(Tags.Blocks.ORES_LAPIS, DARK_BLUE), Map.entry(Tags.Blocks.ORES_COAL, BLACK),
        Map.entry(ModTags.Blocks.MCCOURSE_ORES, LIGHT_PURPLE), Map.entry(BlockTags.FEATURES_CANNOT_REPLACE, RED));

        for (Map.Entry<TagKey<Block>, ChatFormatting> entry : oreColors.entrySet()) {
            if (block.defaultBlockState().is(entry.getKey())) { color = entry.getValue(); break; }
        }

        message(player, text("Ore found: ").append(screen(I18n.get(block.getDescriptionId()), color))
                .append(text(" at [X: ")).append(screen(String.valueOf(pos.getX()), color))
                .append(text(", Y: ")).append(screen(String.valueOf(pos.getY()), color))
                .append(text(", Z: ")).append(screen(String.valueOf(pos.getZ()), color)).append(text("]")));
    }

    // Custom method that identifies ALL BLOCKS added in metal_detector_valuables.json (CUSTOM TAGS)
    private boolean isValuableBlock(BlockState blockState) { return blockState.is(getType()); }

    private MutableComponent text(String name) { return Component.literal(name); }

    private MutableComponent json(String name) { return Component.translatable(name); }

    private MutableComponent screen(String name, ChatFormatting color) {
        return Component.literal(name).withStyle(color, BOLD);
    }

    private void message(Player player, Component text) { player.displayClientMessage(text,true); }
}