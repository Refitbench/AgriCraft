package com.infinityraider.infinitylib.capability;

import com.infinityraider.infinitylib.utility.ISerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.concurrent.Callable;

public interface ICapabilityImplementation<C extends ICapabilityProvider, V extends ISerializable> {
    Capability<V> getCapability();

    boolean shouldApplyCapability(C carrier);

    V createNewValue(C carrier);

    ResourceLocation getCapabilityKey();

    Class<C> getCarrierClass();

    Class<V> getCapabilityClass();
}
