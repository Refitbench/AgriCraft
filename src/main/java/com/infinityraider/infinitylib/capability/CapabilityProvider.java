package com.infinityraider.infinitylib.capability;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public final class CapabilityProvider<C> implements ICapabilitySerializable<NBTTagCompound> {
    private final Capability<C> capability;
    private final C value;

    public CapabilityProvider(Capability<C> capability, C value) {
        this.capability = capability;
        this.value = value;
    }

    public Capability<C> getCapability() {
        return this.capability;
    }

    public C getCapabilityValue() {
        return value;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) this.getCapability().getStorage().writeNBT(this.getCapability(), this.getCapabilityValue(), null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        this.getCapability().getStorage().readNBT(this.getCapability(), this.getCapabilityValue(), null, nbt);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return this.getCapability() != null && capability == this.getCapability();
    }

    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return this.hasCapability(capability, facing) ? this.getCapability().cast(this.value) : null;
    }
}
