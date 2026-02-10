package com.infinityraider.infinitylib.capability;

import com.infinityraider.infinitylib.utility.ISerializable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityStorage<V extends ISerializable> implements Capability.IStorage<V> {
    @Override
    public NBTBase writeNBT(Capability<V> capability, V instance, EnumFacing side) {
        return instance != null ? instance.writeToNBT() : null;
    }

    @Override
    public void readNBT(Capability<V> capability, V instance, EnumFacing side, NBTBase nbt) {
        if(instance != null && (nbt instanceof NBTTagCompound)) {
            instance.readFromNBT((NBTTagCompound) nbt);
        }
    }
}