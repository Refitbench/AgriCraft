package com.infinityraider.infinitylib.utility.inventory;

import com.infinityraider.infinitylib.reference.Names;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

/**
 * IInventory interface to automatically have methods implemented to read/write inventory data from/to nbt
 */
public interface IInventorySerializable extends IInventory {
    default NBTTagCompound writeInventoryToNBT(NBTTagCompound tag) {
        NBTTagList list = new NBTTagList();
        for(int i = 0; i < this.getSizeInventory(); i++) {
            NBTTagCompound tagAt = new NBTTagCompound();
            ItemStack stack = this.getStackInSlot(i);
            boolean flag = !stack.isEmpty() && stack.getCount() > 0;
            tagAt.setBoolean(Names.NBT.FLAG, flag);
            if(flag) {
                stack.writeToNBT(tagAt);
            }
            list.appendTag(tagAt);
        }
        tag.setTag(Names.NBT.LIST, list);
        return tag;
    }

    default NBTTagCompound readInventoryFromNBT(NBTTagCompound tag) {
        if(tag.hasKey(Names.NBT.LIST)) {
            NBTTagList list = tag.getTagList(Names.NBT.LIST, 10);
            for(int i = 0; i < this.getSizeInventory(); i++) {
                NBTTagCompound tagAt = list.getCompoundTagAt(i);
                if(tagAt.getBoolean(Names.NBT.FLAG)) {
                    ItemStack stack = new ItemStack(tagAt);
                    this.setInventorySlotContents(i, stack);
                } else {
                    this.setInventorySlotContents(i, ItemStack.EMPTY);
                }
            }
        }
        return tag;
    }
}
