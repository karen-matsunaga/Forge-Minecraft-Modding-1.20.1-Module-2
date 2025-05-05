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
import java.util.Objects;

public class MetalDetectorItem extends Item {
    TagKey<Block> type;
    public MetalDetectorItem(Properties pProperties, TagKey<Block> type) {
        super(pProperties);
        this.type = type;
    }

    public TagKey<Block> getType() { return type; }

    // Function of Metal Detector item
    @Override
    public @NotNull InteractionResult useOn(UseOnContext pContext) {
        // Client and Server sides
        if(!pContext.getLevel().isClientSide()) {
            // Block that player clicked
            BlockPos positionClicked = pContext.getClickedPos();

            // Detected position of block
            Player player = pContext.getPlayer();

            // Starting always false when not found an ore
            boolean foundBlock = false;

            // Block of current layer up to layer -64
            for (int i = 0; i <= positionClicked.getY() + 64; i++) {
                // Checked if found some ore
                BlockState blockState = pContext.getLevel().getBlockState(positionClicked.below(i));

                // Custom method if found ore
                if(isValuableBlock(blockState)) {
                    // Detected coordinates of block clicked
                    outputValuableCoordinates(positionClicked.below(i), player, blockState.getBlock());
                    foundBlock = true;

                    // If found ore the information is recorded in data tablet item
                    if(InventoryUtil.hasPlayerStackInInventory(Objects.requireNonNull(player), ModItems.DATA_TABLET.get())) {
                        addDataToDataTablet(player, positionClicked.below(i), blockState.getBlock());
                    }

                    // If found ore it is sounded
                    pContext.getLevel().playSeededSound(null, player.getX(), player.getY(), player.getZ(),
                            ModSounds.METAL_DETECTOR_FOUND_ORE.get(), SoundSource.BLOCKS, 1f, 1f, 0);

                    // If found ore it is particles
                    spawnFoundParticles(pContext, positionClicked, blockState);

                    // Finished loop
                    break;
                }
            }
            // Output message if not found ore
            if(!foundBlock) { outputNoValuableFound(Objects.requireNonNull(player)); }
        }

        // Durability of Metal Detector item hurt
        pContext.getItemInHand().hurtAndBreak(1, Objects.requireNonNull(pContext.getPlayer()),
                player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return InteractionResult.SUCCESS;
    }

    // Method that created custom particles if found an ore
    private void spawnFoundParticles(UseOnContext pContext, BlockPos positionClicked, BlockState blockState) {
        for(int i = 0; i < 20; i++) {
            ServerLevel level = (ServerLevel) pContext.getLevel();
            // Position of block and spawn particle
            level.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, blockState),
                    positionClicked.getX() + 0.5d, positionClicked.getY() + 1, positionClicked.getZ() + 0.5d, 1,
                    Math.cos(i * 18) * 0.15d, 0.15d, Math.sin(i * 18) * 0.15d, 0.1);
        }
    }

    // Data Tablet function
    private void addDataToDataTablet(Player player, BlockPos below, Block block) {
        ItemStack dataTablet = player.getInventory().getItem(InventoryUtil.getFirstInventoryIndex(player, ModItems.DATA_TABLET.get()));

        CompoundTag data = new CompoundTag();
        data.putString("mccourse.found_ore", "Valuable Found: " + I18n.get(block.getDescriptionId())
                + " at [X: " + below.getX() + ", Y: " + below.getY() + ", Z: " + below.getZ() + "]");

        dataTablet.setTag(data); // Added x, y, z coordinates on Data Tablet
    }

    // When player press Shift keyword appears more information about Metal Detector item
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.mccourse.metal_detector.tooltip.shift")); // If press Shift keyword
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.mccourse.metal_detector.tooltip")); // If not press Shift keyword
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    // Output message if not found ore -> Screen
    private void outputNoValuableFound(Player player) {
        player.displayClientMessage(Component.translatable("item.mccourse.metal_detector.no_valuable_values"), true);
    }

    // Output message if found ore -> Screen
    private void outputValuableCoordinates(BlockPos pos, Player player, Block block) {
        String description = I18n.get(block.getDescriptionId());
        ChatFormatting color = ChatFormatting.WHITE;
        Map<TagKey<Block>, ChatFormatting> oreColors = Map.ofEntries(Map.entry(Tags.Blocks.ORES_DIAMOND, ChatFormatting.AQUA),
        Map.entry(Tags.Blocks.ORES_GOLD, ChatFormatting.GOLD), Map.entry(Tags.Blocks.ORES_COPPER, ChatFormatting.GOLD),
        Map.entry(Tags.Blocks.ORES_IRON, ChatFormatting.GRAY), Map.entry(Tags.Blocks.ORES_EMERALD, ChatFormatting.DARK_GREEN),
        Map.entry(Tags.Blocks.ORES_REDSTONE, ChatFormatting.DARK_RED), Map.entry(Tags.Blocks.ORES_LAPIS, ChatFormatting.DARK_BLUE),
        Map.entry(Tags.Blocks.ORES_COAL, ChatFormatting.BLACK), Map.entry(ModTags.Blocks.MCCOURSE_ORES, ChatFormatting.LIGHT_PURPLE),
        Map.entry(BlockTags.FEATURES_CANNOT_REPLACE, ChatFormatting.RED));

        for (Map.Entry<TagKey<Block>, ChatFormatting> entry : oreColors.entrySet()) {
            if (block.defaultBlockState().is(entry.getKey())) { color = entry.getValue(); break; }
        }

        player.displayClientMessage(Component.literal("Valuable Found: ")
                        .append(Component.literal(description).withStyle(ChatFormatting.BOLD, color))
                        .append(Component.literal(" at [X: "))
                        .append(Component.literal(String.valueOf(pos.getX())).withStyle(color, ChatFormatting.BOLD))
                        .append(Component.literal(", Y: "))
                        .append(Component.literal(String.valueOf(pos.getY())).withStyle(color, ChatFormatting.BOLD))
                        .append(Component.literal(", Z: "))
                        .append(Component.literal(String.valueOf(pos.getZ())).withStyle(color, ChatFormatting.BOLD))
                        .append(Component.literal("]")),
                true);
    }

    // Custom method that identifies all blocks added it is
    // All blocks added in metal_detector_valuables.json
    private boolean isValuableBlock(BlockState blockState) { return blockState.is(getType()); }
}