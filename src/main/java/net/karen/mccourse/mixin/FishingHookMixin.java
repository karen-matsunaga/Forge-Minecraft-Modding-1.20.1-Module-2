package net.karen.mccourse.mixin;

import net.karen.mccourse.item.ModItems;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mixin(FishingHook.class)
public abstract class FishingHookMixin {
    @Shadow private int timeUntilHooked, timeUntilLured, nibble;
    @Shadow @Nullable public abstract Player getPlayerOwner();

    // METHOD - Player fishes faster
    @Inject(method = "tick", at = @At("HEAD"))
    private void reduceFishingWaitTime(CallbackInfo ci) {
        Player player = getPlayerOwner(); // Player Fishing Rod OWNER
        if (player != null) {
            ItemStack fishingRod = player.getMainHandItem(); // Player has Mccourse Fishing Rod on MAIN HAND
            if (fishingRod.is(ModItems.MCCOURSE_FISHING_ROD.get())) {
                this.timeUntilLured = Math.min(this.timeUntilLured, 10); // Waiting time until a fish starts to approach
                this.nibble = Math.min(this.nibble, 10); // Hook swing time (fish agitation phase)
                this.timeUntilHooked = Math.min(this.timeUntilHooked, 20); // Time remaining until the fish actually bites the hook
            }
        }
    }

    // METHOD - Player catch fish
    @Inject(method = "retrieve", at = @At("HEAD"))
    private void onCustomFishing(ItemStack pStack, CallbackInfoReturnable<Integer> cir) {
        Player player = getPlayerOwner();
        if (player != null && !player.level().isClientSide()) {
            Player owner = getPlayerOwner();
            ItemStack held = owner.getMainHandItem();
            if (held.is(ModItems.MCCOURSE_FISHING_ROD.get())) {
                List<ItemStack> drops = new ArrayList<>();
                Level level = player.level();
                RandomSource random = level.random;
                int fishAmount = 2 + random.nextInt(4);
                drops.add(new ItemStack(Items.SALMON, fishAmount));
                if (random.nextFloat() < 0.25f) { drops.add(new ItemStack(Items.NAUTILUS_SHELL)); }
                for (ItemStack drop : drops) {
                    ItemEntity item = new ItemEntity(level, player.getX(), player.getY(), player.getZ(), drop);
                    level.addFreshEntity(item);
                }
            }
        }
    }
}