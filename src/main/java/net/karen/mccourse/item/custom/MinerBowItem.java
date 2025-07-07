package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ChatUtil;
import net.karen.mccourse.util.Utils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class MinerBowItem extends BowItem {
    private final int radius, depth;
    private final boolean lucky;
    public MinerBowItem(Properties properties, int radius, int depth, boolean lucky) {
        super(properties);
        this.radius = radius;
        this.depth = depth;
        this.lucky = lucky;
    }

    public int getRadius() { return radius; }

    public int getDepth() { return depth; }

    @Override
    public void releaseUsing(@NotNull ItemStack stack, Level world, @NotNull LivingEntity shooter, int timeLeft) {
        if (!world.isClientSide() && shooter instanceof Player player) {
            float velocity = BowItem.getPowerForTime(stack.getUseDuration() - timeLeft); // Firing direction - 0.0 a 1.0
            if (velocity < 0.1F) { return; } // Very weak, does not launch
            Arrow arrow = new Arrow(world, player); // Set arrow direction
            arrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, velocity * 3.0F, 1.0F);
            arrow.setBaseDamage(2.0);
            arrow.setCritArrow(true);
            arrow.pickup = AbstractArrow.Pickup.ALLOWED;
            CompoundTag tag = arrow.getPersistentData();
            tag.putBoolean("MiningArrow", true);
            if (lucky) { tag.putBoolean("LuckyBomb", true); }
            Direction dir = Direction.getNearest(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z);
            tag.putInt("MiningDirection", dir.ordinal()); // Saves player direction
            tag.putUUID("ShooterUUID", player.getUUID()); // Save whoever shot
            var item = ForgeRegistries.ITEMS.getKey(stack.getItem()); // Saves which item was used to fire
            if (item != null) { tag.putString("ShooterBow", item.toString()); }
            world.addFreshEntity(arrow);
            Utils.playerSound(world, player, SoundEvents.ARROW_SHOOT, 1.0F, 1.0F);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        int rad = getRadius(), dep = getDepth();
        ChatUtil.tooltipLine(list, "Blocks: " + "§6" + rad + "§c x " + "§6" + rad + "§c x " + "§6" + dep, Utils.red);
    }
}