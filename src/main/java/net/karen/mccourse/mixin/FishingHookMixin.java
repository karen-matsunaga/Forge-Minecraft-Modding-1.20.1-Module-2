package net.karen.mccourse.mixin;

import net.karen.mccourse.enchantment.ModEnchantments;
import net.karen.mccourse.item.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;
import javax.annotation.Nullable;
import java.util.*;
import static net.karen.mccourse.util.Utils.*;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Shadow private int timeUntilHooked, timeUntilLured, nibble;
    @Shadow @Nullable public abstract Player getPlayerOwner();

    @Inject(method = "tick", at = @At("HEAD"))
    private void reduceFishingWaitTime(CallbackInfo ci) { // DEFAULT METHOD - Player fishes faster
        Player player = getPlayerOwner(); // Player Fishing Rod OWNER
        if (player != null && !player.level().isClientSide()) {
            ItemStack fishingRod = player.getMainHandItem(); // Player has Mccourse Fishing Rod on MAIN HAND
            if (fishingRod.is(ModItems.MCCOURSE_FISHING_ROD.get())) {
                this.timeUntilLured = Math.min(this.timeUntilLured, 10); // Waiting time until a fish starts to approach
                this.nibble = Math.min(this.nibble, 10); // Hook swing time (fish agitation phase)
                this.timeUntilHooked = Math.min(this.timeUntilHooked, 20); // Time remaining until the fish actually bites the hook
            }
        }
    }

    @Inject(method = "retrieve", at = @At("HEAD"))
    private void onCustomFishing(ItemStack pStack, CallbackInfoReturnable<Integer> cir) { // DEFAULT METHOD - Player catch fish
        Player player = getPlayerOwner(); // Fishing Rod's owner
        if (player != null && !player.level().isClientSide()) {
            ItemStack held = player.getMainHandItem(); // Player with Fishing Rod
            int betterFishing = enchant(held, ModEnchantments.BETTER_FISHING.get()); // Item Better Fishing enchantment
            List<ItemStack> drops = new ArrayList<>(); // Saves all items generated from fishing
            Level level = player.level();
            RandomSource random = level.random; // Random chance drop item
            if (held.is(ModItems.MCCOURSE_FISHING_ROD.get())) {
                mccourseFishingRodDrops(random, 0.1f, 2, 2, drops, Items.SALMON); // Random drop chance
                mccourseFishingRodDrops(random, 0.25f, 1, 1, drops, Items.NAUTILUS_SHELL);
                drops.forEach(drop -> dropFish(level, player.getX(), player.getY(), player.getZ(), drop));
            }
            if (betterFishing > 0) { // "Rare" loots
                float rareChance = 0.05f + 0.10f * betterFishing; // 5% base + 10% per level
                mccourseFishingRodDrops(random, 0.1f, 1, 3, drops, Items.COD);
                mccourseFishingRodDrops(random, rareChance, 1, 1, drops, ModItems.LEVEL_CHARGER_MINUS.get());
                drops.forEach(drop -> dropFish(level, player.getX(), player.getY(), player.getZ(), drop));
            }
        }
    }
}