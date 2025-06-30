package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ChatUtil;
import net.karen.mccourse.util.Utils;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

public class MinerBowItem extends BowItem {
    private final int radius, depth;
    public MinerBowItem(Properties properties, int radius, int depth) {
        super(properties);
        this.radius = radius;
        this.depth = depth;
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
            arrow.setBaseDamage(0); // Damage 0
            arrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            CompoundTag tag = arrow.getPersistentData();
            tag.putBoolean("MiningArrow", true);
            Direction dir = Direction.getNearest(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z);
            tag.putInt("MiningDirection", dir.ordinal()); // Saves player direction
            tag.putUUID("ShooterUUID", player.getUUID()); // Save whoever shot
            var item = ForgeRegistries.ITEMS.getKey(stack.getItem()); // Saves which item was used to fire
            if (item != null) { tag.putString("ShooterBow", item.toString()); }
            world.addFreshEntity(arrow);
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level,
                                @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        ChatUtil.tooltipLine(list, "Break " + getRadius() + " x " + getRadius() + " x " + getDepth(), Utils.red);
    }
}