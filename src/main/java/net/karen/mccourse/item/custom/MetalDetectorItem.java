package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ModTags;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MetalDetectorItem extends Item {
    public MetalDetectorItem(Properties pProperties) {
        super(pProperties);
    }

    // Function of Metal Detector item
    @Override
    public InteractionResult useOn(UseOnContext pContext) {
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

                    // Finished loop
                    break;
                }
            }

            // Output message if not found ore
            if(!foundBlock) {
                outputNoValuableFound(player);
            }

        }

        // Durability of Metal Detector item hurt
        pContext.getItemInHand().hurtAndBreak(1, pContext.getPlayer(),
                player -> player.broadcastBreakEvent(player.getUsedItemHand()));

        return InteractionResult.SUCCESS;
    }

    // When player press Shift keyword appears more information about Metal Detector item
    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        // If press Shift keyword
        if(Screen.hasShiftDown()) {
            pTooltipComponents.add(Component.translatable("tooltip.mccourse.metal_detector.tooltip.shift"));
        } else {
            // If not press Shift keyword
            pTooltipComponents.add(Component.translatable("tooltip.mccourse.metal_detector.tooltip"));
        }

        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    // Output message if not found ore
    private void outputNoValuableFound(Player player) {
        player.sendSystemMessage(Component.translatable("item.mccourse.metal_detector.no_valuable_values"));
    }

    // Output message if found ore
    private void outputValuableCoordinates(BlockPos below, Player player, Block block) {
        player.sendSystemMessage(Component.literal("Valuable Found: " + I18n.get(block.getDescriptionId())
                + " at (" + below.getX() + ", " + below.getY() + ", " + below.getZ() + " )"));
    }

    // Custom method that identifies all blocks added it is
    // All blocks added in metal_detector_valuables.json
    private boolean isValuableBlock(BlockState blockState) {
        return blockState.is(ModTags.Blocks.METAL_DETECTOR_VALUABLES);
    }

}
