package com.sts15.mineprocess.init;

import com.google.common.base.Suppliers;
import com.sts15.mineprocess.MineProcess;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

public class ConfiguredFeatureInit {
	
	public static final DeferredRegister<ConfiguredFeature<?, ?>> CONFIGURED_FEATURES =
            DeferredRegister.create(Registry.CONFIGURED_FEATURE_REGISTRY, MineProcess.MOD_ID);
	
	private static final Supplier<List<OreConfiguration.TargetBlockState>> TITANIUM_ORE_REPLACEMENT = Suppliers.memoize(() ->
    List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, BlockInit.TITANIUM_ORE.get().defaultBlockState())));
	
	public static final RegistryObject<ConfiguredFeature<?, ?>> TITANIUM_ORE = CONFIGURED_FEATURES.register("titanium_ore",
            () -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(TITANIUM_ORE_REPLACEMENT.get(), 11)));

	private static final Supplier<List<OreConfiguration.TargetBlockState>> MAGNESIUM_ORE_REPLACEMENT = Suppliers.memoize(() ->
			List.of(OreConfiguration.target(OreFeatures.STONE_ORE_REPLACEABLES, BlockInit.MAGNESIUM_ORE.get().defaultBlockState()),
					OreConfiguration.target(OreFeatures.DEEPSLATE_ORE_REPLACEABLES, BlockInit.MAGNESIUM_ORE_DEEPSLATE.get().defaultBlockState())));

	public static final RegistryObject<ConfiguredFeature<?, ?>> MAGNESIUM_ORE = CONFIGURED_FEATURES.register("magnesium_ore",
			() -> new ConfiguredFeature<>(Feature.ORE, new OreConfiguration(MAGNESIUM_ORE_REPLACEMENT.get(), 11)));
	
}
