package com.infinityraider.infinitylib.utility.inventory;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Simple class to wrap an IInventory as an IItemHandler while maintaining IInventory functionality
 */
public class ItemHandlerInventory implements IInventoryItemHandler {
    private final IInventory inventory;

    public ItemHandlerInventory(IInventory inventory) {
        this.inventory = inventory;
    }

    public IInventory getInventory() {
        return inventory;
    }

    @Override
    public int getSizeInventory() {
        return this.getInventory().getSizeInventory();
    }

    @Nullable
    @Override
    public ItemStack getStackInSlot(int index) {
        return  this.getInventory().getStackInSlot(index);
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.getInventory().getInventoryStackLimit();
    }

    @Nullable
    @Override
    public ItemStack decrStackSize(int index, int count) {
        return  this.getInventory().decrStackSize(index, count);
    }

    @Nullable
    @Override
    public ItemStack removeStackFromSlot(int index) {
        return  this.getInventory().removeStackFromSlot(index);
    }

    @Override
    public void setInventorySlotContents(int index, @Nullable ItemStack stack) {
        this.getInventory().setInventorySlotContents(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.getInventory().getInventoryStackLimit();
    }

    @Override
    public void markDirty() {
        this.getInventory().markDirty();
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return this.getInventory().isUsableByPlayer(player);
    }

    @Override
    public void openInventory(EntityPlayer player) {
        this.getInventory().openInventory(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        this.getInventory().closeInventory(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return this.getInventory().isItemValidForSlot(index, stack);
    }

    @Override
    public int getField(int id) {
        return this.getInventory().getField(id);
    }

    @Override
    public void setField(int id, int value) {
        this.getInventory().setField(id, value);
    }

    @Override
    public int getFieldCount() {
        return this.getInventory().getFieldCount();
    }

    @Override
    public void clear() {
        this.getInventory().clear();
    }

    @Override
    public String getName() {
        return this.getInventory().getName();
    }

    @Override
    public boolean hasCustomName() {
        return this.getInventory().hasCustomName();
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.getInventory().getDisplayName();
    }
}
