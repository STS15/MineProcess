package com.sts15.mineprocess.init;

import com.google.common.base.Supplier;
import com.sts15.mineprocess.MineProcess;

import com.sts15.mineprocess.item.ModArmorMaterials;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MineProcess.MOD_ID);
	
	// Add to creative tab
	public static final RegistryObject<Item> TITANIUM_INGOT = register("titanium_ingot", () -> new Item(new Item.Properties().tab(MineProcess.MineProcess_TAB)));
	public static final RegistryObject<Item> MAGNESIUM_DUST = register("magnesium_dust", () -> new Item(new Item.Properties().tab(MineProcess.MineProcess_TAB)));
	public static final RegistryObject<Item> TITANIUM_PICKAXE = register("titanium_pickaxe",
			() -> new PickaxeItem(Tiers.DIAMOND, 4, -2.8f,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
			));
	public static final RegistryObject<Item> TITANIUM_SWORD = register("titanium_sword",
			() -> new SwordItem(Tiers.DIAMOND, 7, -2.4f,
			      new Item.Properties()
			      .tab(MineProcess.MineProcess_TAB)
			      .stacksTo(1)
			      ));
	public static final RegistryObject<Item> TITANIUM_WAR_AXE = register("titanium_war_axe",
			() -> new AxeItem(Tiers.DIAMOND, 9, -3.2f,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
			));
	public static final RegistryObject<Item> TITANIUM_DAGGER = register("titanium_dagger",
			() -> new SwordItem(Tiers.DIAMOND, 5, -1.5f,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
			));

	public static final RegistryObject<Item> TITANIUM_HELMET = register("titanium_helmet",
			() -> new ArmorItem(ModArmorMaterials.TITANIUM, EquipmentSlot.HEAD,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
							.fireResistant()
			));
	public static final RegistryObject<Item> TITANIUM_CHESTPLATE = register("titanium_chestplate",
			() -> new ArmorItem(ModArmorMaterials.TITANIUM, EquipmentSlot.CHEST,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
							.fireResistant()
			));
	public static final RegistryObject<Item> TITANIUM_LEGGINGS = register("titanium_leggings",
			() -> new ArmorItem(ModArmorMaterials.TITANIUM, EquipmentSlot.LEGS,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
							.fireResistant()
			));
	public static final RegistryObject<Item> TITANIUM_BOOTS = register("titanium_boots",
			() -> new ArmorItem(ModArmorMaterials.TITANIUM, EquipmentSlot.FEET,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
							.fireResistant()
			));
	
	
	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
		return ITEMS.register(name, item);
	}
}
