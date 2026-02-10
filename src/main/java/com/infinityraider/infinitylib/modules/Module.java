package com.infinityraider.infinitylib.modules;

import com.google.common.collect.ImmutableList;
import com.infinityraider.infinitylib.capability.ICapabilityImplementation;
import com.infinityraider.infinitylib.network.INetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public abstract class Module implements Comparable<Module> {
    private static HashSet<Module> activeModules = new HashSet<>();

    public static List<Module> getActiveModules() {
        return ImmutableList.copyOf(activeModules);
    }

    protected Module() {}

    public final void activate() {
        activeModules.add(this);
    }

    public void registerMessages(INetworkWrapper wrapper) {}

    public List<Object> getCommonEventHandlers() {
        return Collections.emptyList();
    }

    @SideOnly(Side.CLIENT)
    public List<Object> getClientEventHandlers() {
        return Collections.emptyList();
    }

    public List<ICapabilityImplementation> getCapabilities() {
        return Collections.emptyList();
    }

    public void init() {}

    @SideOnly(Side.CLIENT)
    public void initClient() {}

    public void postInit() {}

    @SideOnly(Side.CLIENT)
    public void postInitClient() {}

    @Override
    public int compareTo(Module other) {
        return this.getClass().getName().compareTo(other.getClass().getName());
    }
}
