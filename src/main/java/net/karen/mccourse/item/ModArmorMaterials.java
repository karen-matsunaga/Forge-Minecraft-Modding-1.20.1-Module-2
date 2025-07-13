package net.karen.mccourse.item;

import net.karen.mccourse.MCCourseMod;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    // Alexandrite's armor material
    ALEXANDRITE("alexandrite", 24, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 4);
        map.put(ArmorItem.Type.LEGGINGS, 5);
        map.put(ArmorItem.Type.CHESTPLATE, 7);
        map.put(ArmorItem.Type.HELMET, 5);
    }), 15, SoundEvents.ARMOR_EQUIP_NETHERITE, 3.0F, 0.1F,
            () -> Ingredient.of(ModItems.ALEXANDRITE.get())),
    // PINK armor material
    PINK("pink", 24, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 4);
        map.put(ArmorItem.Type.LEGGINGS, 5);
        map.put(ArmorItem.Type.CHESTPLATE, 7);
        map.put(ArmorItem.Type.HELMET, 5);
    }), 15, SoundEvents.ARMOR_EQUIP_DIAMOND, 3.0F, 0.1F,
            () -> Ingredient.of(ModItems.PINK.get())),
    // COPPER armor material
    COPPER("copper", 24, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 4);
        map.put(ArmorItem.Type.LEGGINGS, 5);
        map.put(ArmorItem.Type.CHESTPLATE, 7);
        map.put(ArmorItem.Type.HELMET, 5);
    }), 15, SoundEvents.ARMOR_EQUIP_DIAMOND, 3.0F, 0.1F,
            () -> Ingredient.of(Items.COPPER_INGOT)),
    // MINER armor material
    MINER("miner", 2, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.HELMET, 5);
    }), 1, SoundEvents.ANVIL_PLACE, 1.0F, 0.1F, () -> Ingredient.of(Items.TORCH)),
    // PHANTOM armor material
    PHANTOM("phantom", 2, Util.make(new EnumMap<>(ArmorItem.Type.class), (map) -> {
        map.put(ArmorItem.Type.BOOTS, 4);
    }), 1, SoundEvents.ANVIL_PLACE, 1.0F, 0.1F, () -> Ingredient.of(Items.PHANTOM_MEMBRANE)),
    // Future armor material ...
    ; // Custom armor

    public static final StringRepresentable.EnumCodec<ArmorMaterials> CODEC =
            StringRepresentable.fromEnum(ArmorMaterials::values);

    private static final EnumMap<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(
            new EnumMap<>(ArmorItem.Type.class), (enumMap) -> {
        enumMap.put(ArmorItem.Type.BOOTS, 13);
        enumMap.put(ArmorItem.Type.LEGGINGS, 15);
        enumMap.put(ArmorItem.Type.CHESTPLATE, 16);
        enumMap.put(ArmorItem.Type.HELMET, 11);
    });

    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> repairIngredient;

    ModArmorMaterials(String name, int durabilityMultiplier,
                      EnumMap<ArmorItem.Type, Integer> protectionTypeMap, int enchantmentValue,
                      SoundEvent soundEvent, float toughness, float knockbackRes,
                      Supplier<Ingredient> repairMaterial) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionFunctionForType = protectionTypeMap;
        this.enchantmentValue = enchantmentValue;
        this.sound = soundEvent;
        this.toughness = toughness;
        this.knockbackResistance = knockbackRes;
        this.repairIngredient = repairMaterial;
    }

    // Armor's durability
    public int getDurabilityForType(ArmorItem.@NotNull Type type) {
        return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
    }

    // Armor's defense
    public int getDefenseForType(ArmorItem.@NotNull Type type) {
        return this.protectionFunctionForType.get(type);
    }

    // Armor's enchantment value
    public int getEnchantmentValue() { return this.enchantmentValue; }

    // Armor's sound
    public @NotNull SoundEvent getEquipSound() { return this.sound; }

    // Armor's repair item
    public @NotNull Ingredient getRepairIngredient() { return this.repairIngredient.get(); }

    // Armor's name
    public @NotNull String getName() { return MCCourseMod.MOD_ID + ":" + this.name; }

    // Armor's toughness
    public float getToughness() { return this.toughness; }

    /**
     * Gets the percentage of knockback resistance provided by armor of the material.
     */
    // Armor's knockback resistance
    public float getKnockbackResistance() { return this.knockbackResistance; }

    public String getSerializedName() { return MCCourseMod.MOD_ID + ":" + this.name; }
}