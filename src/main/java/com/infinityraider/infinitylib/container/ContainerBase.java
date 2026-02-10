package com.infinityraider.infinitylib.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerBase extends Container {

    public static final int PLAYER_INVENTORY_SIZE = 36;

    public ContainerBase(InventoryPlayer inventory, int xOffset, int yOffset) {
        // Add the player's main inventory to the container.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                //new Slot(inventory, slot index, x coordinate, y coordinate)
                this.addSlotToContainer(new Slot(inventory, j + i * 9 + 9, xOffset + j * 18, yOffset + i * 18));
            }
        }

        // Add the player's hotbar inventory to the container.
        for (int i = 0; i < 9; i++) {
            //new Slot(inventory, slot index, x coordinate, y coordinate)
            this.addSlotToContainer(new Slot(inventory, i, xOffset + i * 18, 58 + yOffset));
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return true;
    }

    @Override
    protected boolean mergeItemStack(ItemStack stack, int start, int stop, boolean backwards) {
        boolean foundSlot = false;
        int slotIndex = backwards ? stop - 1 : start;
        Slot slot;
        ItemStack stackInSlot;
        //try to stack with existing stacks first
        if (stack.isStackable()) {
            while (stack.getCount() > 0 && (!backwards && slotIndex < stop || backwards && slotIndex >= start)) {
                slot = this.inventorySlots.get(slotIndex);
                stackInSlot = slot.getStack();
                if (stackInSlot != null && slot.isItemValid(stack) && stackInSlot.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getItemDamage() == stackInSlot.getItemDamage()) && ItemStack.areItemStackTagsEqual(stack, stackInSlot)) {
                    int combinedSize = stackInSlot.getCount() + stack.getCount();
                    if (combinedSize <= stack.getMaxStackSize()) {
                        stack.setCount(0);
                        stackInSlot.setCount(combinedSize);
                        slot.onSlotChanged();
                        foundSlot = true;
                    } else if (stackInSlot.getCount() < stack.getMaxStackSize()) {
                        stack.setCount(stack.getCount() - (stack.getMaxStackSize() - stackInSlot.getCount()));
                        stackInSlot.setCount(stack.getMaxStackSize());
                        slot.onSlotChanged();
                        foundSlot = true;
                    }
                }
                slotIndex = backwards ? slotIndex - 1 : slotIndex + 1;
            }
        }
        //put in empty slot
        if (stack.getCount() > 0) {
            slotIndex = backwards ? stop - 1 : start;
            while (!backwards && slotIndex < stop || backwards && slotIndex >= start && !foundSlot) {
                slot = this.inventorySlots.get(slotIndex);
                stackInSlot = slot.getStack();
                if (stackInSlot == null && slot.isItemValid(stack)) {
                    slot.putStack(stack.copy());
                    slot.onSlotChanged();
                    stack.setCount(0);
                    foundSlot = true;
                    break;
                }
                slotIndex = backwards ? slotIndex - 1 : slotIndex + 1;
            }
        }
        return foundSlot;
    }

}
