package net.karen.mccourse.item.custom;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import org.jetbrains.annotations.NotNull;

public class ElytraOverpower extends ElytraItem {
    public ElytraOverpower(Properties properties) {
        super(properties);
        DispenserBlock.registerBehavior(this, ArmorItem.DISPENSE_ITEM_BEHAVIOR);
    }

    @Override
    public void inventoryTick(@NotNull ItemStack stack, @NotNull Level level,
                              @NotNull Entity entity, int slotId, boolean isSelected) {
        Player player = (Player) entity;
        MobEffectInstance effect = new MobEffectInstance(MobEffects.REGENERATION, 200, 4,
                                true, true, true);
        boolean isElytra = player.getInventory().getArmor(2).is(this);
        if (isElytra) { player.addEffect(effect); } // Effect Activated
        if (!isElytra) { player.removeEffect(effect.getEffect()); } // Effect Disabled
        super.inventoryTick(stack, level, entity, slotId, isSelected);
    }
}