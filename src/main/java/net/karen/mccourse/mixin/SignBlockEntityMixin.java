package net.karen.mccourse.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.entity.SignText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(SignBlockEntity.class)
public abstract class SignBlockEntityMixin {
    @Inject(method = "setMessages", at = @At("HEAD"), cancellable = true)
    private void injectColorCodes(Player player, List<FilteredText> filteredTexts, SignText text,
                                  CallbackInfoReturnable<SignText> cir) {
        List<Component> newLines = new ArrayList<>(); // List return text with color
        for (FilteredText filter : filteredTexts) {
            String raw = filter.raw(); // Get text WITHOUT color
            String textColor = raw.replaceAll("&([0-9a-fklmnor])", "§$1"); // REPLACE & to §
            newLines.add(Component.literal(textColor)); // Text with color
        }
        SignText newSignText = text;
        for (int i = 0; i < newLines.size(); i++) {
            newSignText = newSignText.setMessage(i, newLines.get(i)); // Add text WITH color on sign
        }
        cir.setReturnValue(newSignText); // Return sign text with color
    }
}