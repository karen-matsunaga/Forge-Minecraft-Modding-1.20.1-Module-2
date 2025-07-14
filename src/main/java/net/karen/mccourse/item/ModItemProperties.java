package net.karen.mccourse.item;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;

public class ModItemProperties {
    // Registry all Item Properties
    public static void addCustomItemProperties() {
        // Custom ALTERNATE item (TRUE -> Texture 1 | FALSE -> Texture 2) -> NUMBER values
        makeAlternateItem(ModItems.MCCOURSE_BOTTLE.get()); // Mccourse Bottle item

        // Custom TRADE item (TRUE -> Texture 1 | FALSE -> Texture 2)
        makeTradeItem(ModItems.DATA_TABLET.get()); // Data tablet item

        // Custom SHIELD
        makeShield(ModItems.ALEXANDRITE_SHIELD.get()); // Alexandrite's shield

        // Custom BOW
        makeBow(ModItems.ALEXANDRITE_BOW.get()); // Alexandrite's bow
        makeBow(ModItems.MINER_BOW.get()); // Miner's bow

        // Custom FISHING ROD
        makeFishingRod(ModItems.MCCOURSE_FISHING_ROD.get()); // Mccourse Fishing Rod

        // CUSTOM ELYTRA - Diamond Elytra
        makeElytra(ModItems.DIAMOND_ELYTRA.get());
    }

    // CUSTOM METHOD - Alexandrite shield (CUSTOM SHIELD)
    private static void makeShield(Item item) {
        ItemProperties.register(item, new ResourceLocation("blocking"),
            (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }

    // CUSTOM METHOD - Data Tablet item
    private static void makeTradeItem(Item item) {
        ItemProperties.register(item, new ResourceLocation(MCCourseMod.MOD_ID, "on"),
                               (stack, level, entity, seed) -> stack.hasTag() ? 1f : 0f);
    }

    // CUSTOM METHOD - Alexandrite's bow animation (CUSTOM BOW)
    private static void makeBow(Item item) {
        // Bow PULL animation
        ItemProperties.register(item, new ResourceLocation("pull"), (stack, level, entity, i) -> {
            if (entity == null) { return 0.0F; }
            else {
                int duration = stack.getUseDuration(), remain = entity.getUseItemRemainingTicks();
                return entity.getUseItem() != stack ? 0.0F : (float) (duration - remain) / 20.0F;
            }
        });
        // Bow PULLING animation
        ItemProperties.register(item, new ResourceLocation("pulling"),
            (stack, level, entity, i) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }

    // CUSTOM METHOD - Mccourse Fishing Rod (CUSTOM FISHING ROD)
    private static void makeFishingRod(Item item) {
        ItemProperties.register(item, new ResourceLocation("cast"),
            (stack, level, entity, i) -> {
                if (entity == null) { return 0.0F; }
                else {
                    boolean flag = entity.getMainHandItem() == stack, flag1 = entity.getOffhandItem() == stack;
                    if (entity.getMainHandItem().getItem() instanceof FishingRodItem) { flag1 = false; }
                    return (flag || flag1) && entity instanceof Player && ((Player) entity).fishing != null ? 1.0F : 0.0F;
                }
            });
    }

    // CUSTOM METHOD - Alternate item (NBT Numbers)
    private static void makeAlternateItem(Item item) {
        ItemProperties.register(item, new ResourceLocation(MCCourseMod.MOD_ID, "on"),
            (stack, level, entity, seed) -> {
                CompoundTag get = stack.getTag();
                boolean getTag = get != null, hasTag = stack.hasTag();
                if (getTag && hasTag && get.getInt("StoredLevels") > 0) { return 1.0F; } // Has XP
                return 0.0F; // Hasn't XP
            });
    }

    // CUSTOM METHOD - Custom Elytra
    private static void makeElytra(Item item) {
        ItemProperties.register(item, new ResourceLocation("broken"),
            (stack, level, entity, i) -> ElytraItem.isFlyEnabled(stack) ? 0.0F : 1.0F);
    }
}