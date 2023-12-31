package com.sts15.mineprocess.block.entity;

import com.sts15.mineprocess.block.custom.SpiralConcentrator;
import com.sts15.mineprocess.init.ItemInit;
import com.sts15.mineprocess.screen.SpiralConcentratorMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpiralConcentratorBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    protected final ContainerData data;
    private boolean active;
    private int progress = 0;
    private int maxProgress = 78;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public SpiralConcentratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SPIRAL_CONCENTRATOR.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> SpiralConcentratorBlockEntity.this.progress;
                    case 1 -> SpiralConcentratorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> SpiralConcentratorBlockEntity.this.progress = value;
                    case 1 -> SpiralConcentratorBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Spiral Concentrator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new SpiralConcentratorMenu(id, inventory, this, this.data);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHandler.cast();
        }

        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SpiralConcentratorBlockEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }

        if(hasRecipe(pEntity)) {
            // Start processing - set the block state to lit
            if (pEntity.progress == 0 && !state.getValue(SpiralConcentrator.LIT)) {
                level.setBlock(pos, state.setValue(SpiralConcentrator.LIT, true), 3);
            }

            pEntity.progress++;
            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);

                // Check if there's another item to process before unlighting
                if (!hasRecipe(pEntity) && state.getValue(SpiralConcentrator.LIT)) {
                    // Finish processing - set the block state to unlit
                    level.setBlock(pos, state.setValue(SpiralConcentrator.LIT, false), 3);
                }

                pEntity.resetProgress();
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);

            // No valid recipe - set the block state to unlit
            if (state.getValue(SpiralConcentrator.LIT)) {
                level.setBlock(pos, state.setValue(SpiralConcentrator.LIT, false), 3);
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(SpiralConcentratorBlockEntity pEntity) {

        if(hasRecipe(pEntity)) {
            pEntity.itemHandler.extractItem(1, 1, false);
            pEntity.itemHandler.setStackInSlot(2, new ItemStack(ItemInit.MAGNESIUM_DUST.get(),
                    pEntity.itemHandler.getStackInSlot(2).getCount() + 4));

            pEntity.resetProgress();
        }
    }

    private static boolean hasRecipe(SpiralConcentratorBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.itemHandler.getSlots());
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            inventory.setItem(i, entity.itemHandler.getStackInSlot(i));
        }

        boolean hasInFirstSlot = entity.itemHandler.getStackInSlot(1).getItem() == ItemInit.TITANIUM_INGOT.get();

        return hasInFirstSlot && canInsertAmountIntoOutputSlot(inventory) &&
                canInsertItemIntoOutputSlot(inventory, new ItemStack(ItemInit.MAGNESIUM_DUST.get(), 4));
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, ItemStack stack) {
        return inventory.getItem(2).getItem() == stack.getItem() || inventory.getItem(2).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(2).getMaxStackSize() > inventory.getItem(2).getCount();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
