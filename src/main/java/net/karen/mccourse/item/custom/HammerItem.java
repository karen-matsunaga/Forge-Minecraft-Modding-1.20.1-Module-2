package net.karen.mccourse.item.custom;

import net.minecraft.core.*;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.*;
import java.util.*;
import static net.karen.mccourse.util.ChatUtil.*;
import static net.karen.mccourse.util.Utils.*;

// Credits by Kaupenjoe - https://github.com/Kaupenjoe/Forge-Course-1.20.X/tree/22-customHammer
// Distributed under MIT - Using with some modifications

// Credits by Alireza Khodakarami (Jiraiyah)
// https://github.com/drkhodakarami/uio/blob/master/src/main/java/jiraiyah/uio/item/HammerItem.java
// Distributed under MIT - Using with some modifications
public class HammerItem extends DiggerItem implements Vanishable {
    private final int radius, distance;
    private final boolean infinite;

    public HammerItem(Tier tier, float attackDamageModifier, float attackSpeedModifier,
                      Properties properties, TagKey<Block> blockTags, int radius, boolean infinite, int distance) {
        super(attackDamageModifier, attackSpeedModifier, tier, blockTags, properties);
        this.radius = radius - 1; // Break X x Y RADIUS declared on ModItems
        this.infinite = infinite; // Insert UNBREAKABLE tag
        this.distance = distance; // Break X x Y x Z + straight break DISTANCE
    }

    public int getRadius() { return this.radius; } // Declared RADIUS activated on ModEvents

    public int getDistance() { return this.distance; } // Declared DISTANCE activated on ModEvents

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag); // Appears tooltip on screen of Hammer.
        int range = this.radius * 2 + 1, distance = this.distance;
        tooltipLine(list, "Hammer breaks: " + range + "x" + range + "x" + distance, red);
        list.add(CommonComponents.EMPTY);
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack stack, @NotNull Level level, @NotNull Player player) {
        super.onCraftedBy(stack, level, player);
        if (infinite) { stack.getOrCreateTag().putBoolean("Unbreakable", true); } // Added Unbreakable tag
    }

    // CUSTOM METHOD - Player to receive the blocks destroyed
    public static List<BlockPos> getBlocksToBeDestroyed(int distance, int radius,
                                                        BlockPos initialBlockPos, ServerPlayer player) {
        List<BlockPos> positions = new ArrayList<>();
        Vec3 eye = player.getEyePosition(1f), look = player.getViewVector(1f),
             reach = eye.add(look.scale(6f));
        BlockHitResult traceResult = player.level().clip(new ClipContext(eye, reach, collider, none, player));
        if (traceResult.getType() == HitResult.Type.MISS) { return positions; }
        // Determines the direction the player is facing - For each step along the direction (up to the desired distance)
        for (int i = 0; i <= distance; i++) { // Adds all blocks inside the X x Y x Z cube around the center position
            BlockPos offsetPos = initialBlockPos.relative(traceResult.getDirection(), i);
            for (int x = -radius; x <= radius; x++) {
                for (int y = -radius; y <= radius; y++) {
                    for (int z = -radius; z <= radius; z++) { positions.add(offsetPos.offset(x, y, z)); }
                }
            }
        }
        return positions;
    }
}