package com.sts15.mineprocess.init;

import java.util.function.Function;

import com.google.common.base.Supplier;
import com.sts15.mineprocess.MineProcess;
import com.sts15.mineprocess.block.custom.SpiralConcentrator;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
	// Deferred Registers
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MineProcess.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = ItemInit.ITEMS;
	
	// Blocks
	public static final RegistryObject<Block> TITANIUM_BLOCK = register("titanium_block",
			() -> new Block(BlockBehaviour.Properties.of(
					Material.METAL, 
					MaterialColor.COLOR_GRAY)
					.strength(3.0f)
					.sound(SoundType.METAL)
					.requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(MineProcess.MineProcess_TAB))
	);

	public static final RegistryObject<Block> MAGNESIUM_BLOCK = register("magnesium_block",
			() -> new Block(BlockBehaviour.Properties.of(
							Material.SAND,
							MaterialColor.COLOR_GRAY)
					.strength(3.0f)
					.sound(SoundType.SAND)
					.requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(MineProcess.MineProcess_TAB))
	);
	
	public static final RegistryObject<Block> TITANIUM_ORE = register("titanium_ore",
			() -> new Block(BlockBehaviour.Properties.of(
					Material.STONE, 
					MaterialColor.COLOR_GRAY)
					.strength(3.0f)
					.sound(SoundType.NETHERITE_BLOCK)
					.requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(MineProcess.MineProcess_TAB))
	);

	public static final RegistryObject<Block> MAGNESIUM_ORE = register("magnesium_ore",
			() -> new Block(BlockBehaviour.Properties.of(
							Material.STONE,
							MaterialColor.COLOR_GRAY)
					.strength(3.0f)
					.sound(SoundType.SAND)
					.requiresCorrectToolForDrops()),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(MineProcess.MineProcess_TAB))
	);
	
	public static final RegistryObject<Block> SPIRAL_CONCENTRATOR = register("spiral_concentrator",
			() -> new SpiralConcentrator(BlockBehaviour.Properties
					.copy(Blocks.FURNACE)
					.sound(SoundType.STONE)),
			object -> () -> new BlockItem(object.get(), new Item.Properties().tab(MineProcess.MineProcess_TAB)));

	

	// Supplier and Registry objects
	private static <T extends Block> RegistryObject<T> registerBlock(final String name,
			final Supplier<? extends T> block) {
		return BLOCKS.register(name, block);
	}

	private static <T extends Block> RegistryObject<T> register(final String name, final Supplier<? extends T> block,
			Function<RegistryObject<T>, Supplier<? extends Item>> item) {
		RegistryObject<T> obj = registerBlock(name, block);
		ITEMS.register(name, item.apply(obj));
		return obj;
	}

}
