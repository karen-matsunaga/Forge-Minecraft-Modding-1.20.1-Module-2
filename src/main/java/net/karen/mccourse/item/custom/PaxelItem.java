package net.karen.mccourse.item.custom;

import net.karen.mccourse.util.ModTags;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Vanishable;

public class PaxelItem extends DiggerItem implements Vanishable {
    public PaxelItem(Tier tier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties properties) {
        super(pAttackDamageModifier, pAttackSpeedModifier, tier, ModTags.Blocks.PAXEL_MINEABLE, properties);
    }
}