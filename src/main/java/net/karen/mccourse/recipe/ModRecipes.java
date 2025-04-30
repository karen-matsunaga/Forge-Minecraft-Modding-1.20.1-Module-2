package net.karen.mccourse.recipe;

import net.karen.mccourse.MCCourseMod;
import net.karen.mccourse.screen.CraftCraftingTableMenu;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, MCCourseMod.MOD_ID);

    // Register all custom recipes SERIALIZERS
    public static final RegistryObject<RecipeSerializer<GemEmpoweringRecipe>> GEM_EMPOWERING_SERIALIZER =
            SERIALIZERS.register("gem_empowering", () -> GemEmpoweringRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<KaupenFurnaceRecipe>> KAUPEN_FURNACE_SERIALIZER =
            SERIALIZERS.register("kaupen_furnace", () -> KaupenFurnaceRecipe.Serializer.INSTANCE);

    public static final RegistryObject<RecipeSerializer<CraftRecipe>> CRAFT_SERIALIZER =
            SERIALIZERS.register("craft", () -> CraftRecipe.Serializer.INSTANCE);

//    public static final RegistryObject<RecipeSerializer<CraftCraftingTableRecipe>> CRAFT_CRAFTING_TABLE_SERIALIZER =
//            SERIALIZERS.register("craft_crafting_table", () -> CraftCraftingTableRecipe.Serializer.INSTANCE);

    // Register all recipes on Forge
    public static void register(IEventBus eventBus) { SERIALIZERS.register(eventBus); }
}