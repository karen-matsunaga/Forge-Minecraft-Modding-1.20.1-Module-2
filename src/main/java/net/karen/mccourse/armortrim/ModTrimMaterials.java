package net.karen.mccourse.armortrim;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.item.ModItems;
import net.minecraft.Util;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import java.util.Map;

public class ModTrimMaterials {
    public static final ResourceKey<TrimMaterial> ALEXANDRITE =
            ResourceKey.create(Registries.TRIM_MATERIAL, new ResourceLocation(MCCourseMod.MOD_ID, "alexandrite"));

    public static void bootstrap(BootstapContext<TrimMaterial> context) {
        register(context, ALEXANDRITE, ModItems.ALEXANDRITE.get(), Style.EMPTY.withColor(TextColor.parseColor("#031cfc")), 0.8F);
    }

    private static void register(BootstapContext<TrimMaterial> context, ResourceKey<TrimMaterial> trimKey, Item item,
                                 Style style, float itemModelIndex) {
        TrimMaterial trimmaterial = TrimMaterial.create(trimKey.location().getPath(), item, itemModelIndex,
                Component.translatable(Util.makeDescriptionId("trim_material", trimKey.location())).withStyle(style), Map.of());
        context.register(trimKey, trimmaterial);
    }
}