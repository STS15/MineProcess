package com.sts15.mineprocess.item;

import com.sts15.mineprocess.init.ItemInit;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class TitaniumMaterial implements Tier {

    @Override
    public int getUses() {
        return 1500;  // durability
    }

    @Override
    public float getSpeed() {
        return 7.0F;  // mining speed equivalent to diamond
    }

    @Override
    public float getAttackDamageBonus() {
        return 3.0F;  // attack damage bonus equivalent to diamond
    }

    @Override
    public int getLevel() {
        return 3;  // harvest level equivalent to diamond
    }

    @Override
    public int getEnchantmentValue() {
        return 15;  // enchantability
    }

    @Override
    public Ingredient getRepairIngredient() {
        return Ingredient.of(ItemInit.TITANIUM_INGOT.get());
    }
}