package com.sts15.mineprocess;

import com.sts15.mineprocess.block.entity.ModBlockEntities;
import com.sts15.mineprocess.init.BlockInit;
import com.sts15.mineprocess.init.ConfiguredFeatureInit;
import com.sts15.mineprocess.init.ItemInit;
import com.sts15.mineprocess.init.PlacedFeatureInit;

import com.sts15.mineprocess.screen.ModMenuTypes;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraft.client.gui.screens.MenuScreens;
import com.sts15.mineprocess.screen.SpiralConcentratorScreen;

@Mod("mineprocess")
public class MineProcess {
	
	public static final String MOD_ID = "mineprocess";
	
	public static final CreativeModeTab MineProcess_TAB = new CreativeModeTab(MOD_ID) {
		
		@Override
		@OnlyIn(Dist.CLIENT)
		public ItemStack makeIcon() {
			return new ItemStack(ItemInit.TITANIUM_INGOT.get());
		}
		
		
	};
	
	public MineProcess() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		
		ItemInit.ITEMS.register(bus);
		BlockInit.BLOCKS.register(bus);
		ConfiguredFeatureInit.CONFIGURED_FEATURES.register(bus);
		PlacedFeatureInit.PLACED_FEATURES.register(bus);

		ModBlockEntities.register(bus);
		ModMenuTypes.register(bus);
		
		MinecraftForge.EVENT_BUS.register(this);
	}

	// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
	@Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ClientModEvents {
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			MenuScreens.register(ModMenuTypes.SPIRAL_CONCENTRATOR_MENU.get(), SpiralConcentratorScreen::new);
		}
	}
}
