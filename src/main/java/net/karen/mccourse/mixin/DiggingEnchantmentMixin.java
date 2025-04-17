package net.karen.mccourse.mixin;

import net.minecraft.world.item.enchantment.DiggingEnchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(DiggingEnchantment.class)
public class DiggingEnchantmentMixin {
   /**
    * @author
    * @reason
    */
   @Overwrite
   public int getMaxLevel() {
       return 10;
   }
}