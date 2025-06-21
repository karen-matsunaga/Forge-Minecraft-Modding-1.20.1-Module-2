package net.karen.mccourse.item;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;

public class ModItemProperties {
    // Registry all Item Properties
    public static void addCustomItemProperties() {
        // Data tablet's item
        ItemProperties.register(ModItems.DATA_TABLET.get(), new ResourceLocation(MCCourseMod.MOD_ID, "on"),
                (pStack, pLevel, pEntity, pSeed) -> pStack.hasTag() ? 1f : 0f);

        // Alexandrite's shield
        ItemProperties.register(ModItems.ALEXANDRITE_SHIELD.get(), new ResourceLocation("blocking"),
                (p_174575_, p_174576_, p_174577_, p_174578_) -> {
            return p_174577_ != null && p_174577_.isUsingItem() && p_174577_.getUseItem() == p_174575_ ? 1.0F : 0.0F;
        });

        // Alexandrite's bow
        makeBow(ModItems.ALEXANDRITE_BOW.get());
        makeFishingRod(ModItems.MCCOURSE_FISHING_ROD.get());
    }

    // Alexandrite's bow animation
    private static void makeBow(Item item) {
        ItemProperties.register(item, new ResourceLocation("pull"), (p_174635_, p_174636_, p_174637_, p_174638_) -> {
            if (p_174637_ == null) { return 0.0F; }
            else {
                return p_174637_.getUseItem() != p_174635_ ? 0.0F : (float)(p_174635_.getUseDuration() -
                        p_174637_.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(item, new ResourceLocation("pulling"), (p_174630_, p_174631_, p_174632_, p_174633_) -> {
            return p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F;
        });
    }

    // Mccourse Fishing Rod
    private static void makeFishingRod(Item item) {
        ItemProperties.register(item, new ResourceLocation("cast"),
                (p_174585_, p_174586_, p_174587_, p_174588_) -> {
            if (p_174587_ == null) {
                return 0.0F;
            } else {
                boolean flag = p_174587_.getMainHandItem() == p_174585_;
                boolean flag1 = p_174587_.getOffhandItem() == p_174585_;
                if (p_174587_.getMainHandItem().getItem() instanceof FishingRodItem) {
                    flag1 = false;
                }
                return (flag || flag1) && p_174587_ instanceof Player && ((Player)p_174587_).fishing != null ? 1.0F : 0.0F;
            }
        });
    }
}