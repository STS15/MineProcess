package com.sts15.mineprocess.init;
import com.sts15.mineprocess.MineProcess;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class PlacedFeatureInit {

	public static final DeferredRegister<PlacedFeature> PLACED_FEATURES =
            DeferredRegister.create(Registry.PLACED_FEATURE_REGISTRY, MineProcess.MOD_ID);
	
	public static final RegistryObject<PlacedFeature> TITANIUM_ORE_REPLACEMENT = PLACED_FEATURES.register("titanium_ore_placement",
            () -> new PlacedFeature(ConfiguredFeatureInit.TITANIUM_ORE.getHolder().get(),
                    commonOrePlacement(7, HeightRangePlacement.triangle(
                            VerticalAnchor.bottom(),
                            VerticalAnchor.absolute(40)
                    ))));

    public static final RegistryObject<PlacedFeature> MAGNESIUM_ORE_REPLACEMENT = PLACED_FEATURES.register("magnesium_ore_placement",
            () -> new PlacedFeature(ConfiguredFeatureInit.MAGNESIUM_ORE.getHolder().get(),
                    commonOrePlacement(7, HeightRangePlacement.triangle(
                            VerticalAnchor.bottom(),
                            VerticalAnchor.absolute(16)
                    ))));
	
	private static List<PlacementModifier> commonOrePlacement(int countPerChunk, PlacementModifier height) {
        return orePlacement(CountPlacement.of(countPerChunk), height);
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier count, PlacementModifier height) {
        return List.of(count, InSquarePlacement.spread(), height, BiomeFilter.biome());
    }
	
}
