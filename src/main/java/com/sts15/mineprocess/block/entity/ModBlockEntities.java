package com.sts15.mineprocess.block.entity;

import com.sts15.mineprocess.MineProcess;
import com.sts15.mineprocess.init.BlockInit;
import com.sts15.mineprocess.init.ItemInit;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MineProcess.MOD_ID);

    public static final RegistryObject<BlockEntityType<SpiralConcentratorBlockEntity>> SPIRAL_CONCENTRATOR =
            BLOCK_ENTITIES.register("spiral_concentrator", () ->
                    BlockEntityType.Builder.of(SpiralConcentratorBlockEntity::new,
                            BlockInit.SPIRAL_CONCENTRATOR.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}