package com.infinityraider.infinitylib.utility;

import net.minecraft.nbt.NBTTagCompound;

public interface ISerializable {
    void readFromNBT(NBTTagCompound tag);

    NBTTagCompound writeToNBT();
}
