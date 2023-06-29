package com.sts15.mineprocess.block.entity;

import com.sts15.mineprocess.block.custom.ArcFurnace;
import com.sts15.mineprocess.block.custom.SpiralConcentrator;
import com.sts15.mineprocess.init.ItemInit;
import com.sts15.mineprocess.screen.ArcFurnaceMenu;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArcFurnaceBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;
    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    public ArcFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ARC_FURNACE.get(), pos, state);
        this.data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> ArcFurnaceBlockEntity.this.progress;
                    case 1 -> ArcFurnaceBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ArcFurnaceBlockEntity.this.progress = value;
                    case 1 -> ArcFurnaceBlockEntity.this.maxProgress = value;
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
        return Component.literal("Arc Furnace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ArcFurnaceMenu(id, inventory, this, this.data);
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

    public static void tick(Level level, BlockPos pos, BlockState state, ArcFurnaceBlockEntity pEntity) {
        if(level.isClientSide()) {
            return;
        }

        if(hasRecipe(pEntity)) {
            // Start processing - set the block state to lit
            if (pEntity.progress == 0 && !state.getValue(ArcFurnace.LIT)) {
                level.setBlock(pos, state.setValue(ArcFurnace.LIT, true), 3);
            }

            pEntity.progress++;
            setChanged(level, pos, state);

            if(pEntity.progress >= pEntity.maxProgress) {
                craftItem(pEntity);

                // Check if there's another item to process before unlighting
                if (!hasRecipe(pEntity) && state.getValue(ArcFurnace.LIT)) {
                    // Finish processing - set the block state to unlit
                    level.setBlock(pos, state.setValue(ArcFurnace.LIT, false), 3);
                }

                pEntity.resetProgress();
            }
        } else {
            pEntity.resetProgress();
            setChanged(level, pos, state);

            // No valid recipe - set the block state to unlit
            if (state.getValue(ArcFurnace.LIT)) {
                level.setBlock(pos, state.setValue(ArcFurnace.LIT, false), 3);
            }
        }
    }

    private void resetProgress() {
        this.progress = 0;
    }

    private static void craftItem(ArcFurnaceBlockEntity pEntity) {
        if (hasRecipe(pEntity)) {
            ItemStack inputStack1 = pEntity.itemHandler.extractItem(0, 1, false); // Extract titanium ingot from slot 0

            // Depending on the item in slot 1, extract different quantities
            if (pEntity.itemHandler.getStackInSlot(1).getItem() == Items.DIAMOND) {
                pEntity.itemHandler.extractItem(1, 1, false); // Extract 1 diamond from slot 1
            } else if (pEntity.itemHandler.getStackInSlot(1).getItem() == Items.COAL_BLOCK) {
                pEntity.itemHandler.extractItem(1, 8, false); // Extract 8 coal blocks from slot 1
            }

            ItemStack outputStack = new ItemStack(ItemInit.CARBIDE_INGOT.get(), 1);

            // Insert the output into slot 2
            pEntity.itemHandler.insertItem(2, outputStack, false);

            pEntity.resetProgress();
        }
    }

    private static boolean hasRecipe(ArcFurnaceBlockEntity entity) {
        ItemStack stackInFirstSlot = entity.itemHandler.getStackInSlot(0);
        ItemStack stackInSecondSlot = entity.itemHandler.getStackInSlot(1);

        boolean hasInFirstSlot = stackInFirstSlot.getItem() == ItemInit.TITANIUM_INGOT.get();

        // Check if there is a diamond or 8 coal blocks in the second slot
        boolean hasInSecondSlot = (stackInSecondSlot.getItem() == Items.DIAMOND && stackInSecondSlot.getCount() >= 1)
                || (stackInSecondSlot.getItem() == Items.COAL_BLOCK && stackInSecondSlot.getCount() >= 8);

        boolean canInsertIntoOutput = canInsertAmountIntoOutputSlot(entity.itemHandler, new ItemStack(ItemInit.CARBIDE_INGOT.get(), 1));

        return hasInFirstSlot && hasInSecondSlot && canInsertIntoOutput;
    }

    private static boolean canInsertAmountIntoOutputSlot(IItemHandlerModifiable itemHandler, ItemStack outputStack) {
        ItemStack currentOutputStack = itemHandler.getStackInSlot(2);
        int spaceLeft = currentOutputStack.getMaxStackSize() - currentOutputStack.getCount();
        return spaceLeft >= outputStack.getCount();
    }
}
