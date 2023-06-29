package com.sts15.mineprocess.init;

import com.google.common.base.Supplier;
import com.sts15.mineprocess.MineProcess;
import com.sts15.mineprocess.item.ModArmorMaterials;
import com.sts15.mineprocess.item.TitaniumMaterial;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemInit {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MineProcess.MOD_ID);
	
	// Add to creative tab
	public static final RegistryObject<Item> TITANIUM_INGOT = register("titanium_ingot", () -> new Item(new Item.Properties().tab(MineProcess.MineProcess_TAB)));
	public static final RegistryObject<Item> CARBIDE_INGOT = register("carbide_ingot", () -> new Item(new Item.Properties().tab(MineProcess.MineProcess_TAB)));
	public static final RegistryObject<Item> MAGNESIUM_DUST = register("magnesium_dust", () -> new Item(new Item.Properties().tab(MineProcess.MineProcess_TAB)));

	public static final RegistryObject<Item> TITANIUM_PICKAXE = register("titanium_pickaxe",
			() -> new PickaxeItem(new TitaniumMaterial(), 4, -2.8f,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
			));
	public static final RegistryObject<Item> TITANIUM_SWORD = register("titanium_sword",
			() -> new SwordItem(new TitaniumMaterial(), 7, -2.4f,
			      new Item.Properties()
			      .tab(MineProcess.MineProcess_TAB)
			      .stacksTo(1)
			      ));
	public static final RegistryObject<Item> TITANIUM_WAR_AXE = register("titanium_war_axe",
			() -> new AxeItem(new TitaniumMaterial(), 9, -3.2f,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
			));
	public static final RegistryObject<Item> TITANIUM_DAGGER = register("titanium_dagger",
			() -> new SwordItem(new TitaniumMaterial(), 5, -1.5f,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)

			));

	public static final RegistryObject<Item> CARBIDE_HELMET = register("carbide_helmet",
			() -> new ArmorItem(ModArmorMaterials.CARBIDE, EquipmentSlot.HEAD,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
							.fireResistant()
			));
	public static final RegistryObject<Item> CARBIDE_CHESTPLATE = register("carbide_chestplate",
			() -> new ArmorItem(ModArmorMaterials.CARBIDE, EquipmentSlot.CHEST,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
							.fireResistant()
			));
	public static final RegistryObject<Item> CARBIDE_LEGGINGS = register("carbide_leggings",
			() -> new ArmorItem(ModArmorMaterials.CARBIDE, EquipmentSlot.LEGS,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
							.fireResistant()
			));
	public static final RegistryObject<Item> CARBIDE_BOOTS = register("carbide_boots",
			() -> new ArmorItem(ModArmorMaterials.CARBIDE, EquipmentSlot.FEET,
					new Item.Properties()
							.tab(MineProcess.MineProcess_TAB)
							.stacksTo(1)
							.fireResistant()
			));
	
	
	private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
		return ITEMS.register(name, item);
	}
}
