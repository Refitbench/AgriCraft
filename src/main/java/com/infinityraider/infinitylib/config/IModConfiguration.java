package com.infinityraider.infinitylib.config;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IModConfiguration {
    void initializeConfiguration(InfinityConfigurationHandler configurationHandler);

    @SideOnly(Side.CLIENT)
    void initializeConfigurationClient(InfinityConfigurationHandler configurationHandler);
}