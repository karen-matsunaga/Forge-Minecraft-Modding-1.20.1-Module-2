package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ModTags;
import net.karen.mccourse.util.Utils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

public class PaxelItem extends DiggerItem implements Vanishable {
    private final boolean lucky;
    public PaxelItem(Tier tier, float attackDamageModifier, float attackSpeedModifier,
                     Properties properties, boolean lucky) {
        super(attackDamageModifier, attackSpeedModifier, tier, ModTags.Blocks.PAXEL_MINEABLE, properties);
        this.lucky = lucky;
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        ItemStack itemStack = player.getItemInHand(Utils.mainHand);
        if (lucky) { itemStack.getOrCreateTag().putBoolean("LuckyBomb", true); }
        return super.onLeftClickEntity(stack, player, entity);
    }
}