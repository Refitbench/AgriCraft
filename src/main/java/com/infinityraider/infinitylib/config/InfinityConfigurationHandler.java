package com.infinityraider.infinitylib.config;

import com.infinityraider.infinitylib.InfinityMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class InfinityConfigurationHandler {
    private final InfinityMod mod;
    private Configuration config;

    private final Map<String, ConfigEntry> entries;

    public InfinityConfigurationHandler(InfinityMod mod) {
        this.mod = mod;
        this.entries = new HashMap<>();
        MinecraftForge.EVENT_BUS.register(this);
    }

    public InfinityMod getMod() {
        return this.mod;
    }

    public Configuration getConfiguration() {
        return this.config;
    }

    void addEntry(ConfigEntry entry) {
        this.entries.put(entry.getName(), entry);
    }

    public List<IConfigElement> getConfigElements() {
        return this.entries.values().stream().map(entry -> new ConfigElement(entry.getProperty())).collect(Collectors.toList());
    }

    public InfinityConfigurationHandler updateEntries() {
        this.entries.values().forEach(ConfigEntry::initialize);
        if(this.config.hasChanged()) {
            this.config.save();
        }
        return this;
    }

    public InfinityConfigurationHandler initializeConfiguration() {
        if(this.config == null) {
            this.config = new Configuration(this.getSuggestedConfigurationFile());
        }
        this.getMod().getConfiguration().initializeConfiguration(this);
        return this.updateEntries();
    }

    public InfinityConfigurationHandler initializeConfigurationClient() {
        if(this.config == null) {
            this.config = new Configuration(this.getSuggestedConfigurationFile());
        }
        this.getMod().getConfiguration().initializeConfigurationClient(this);
        return this.updateEntries();
    }

    public File getSuggestedConfigurationFile() {
        return new File(Loader.instance().getConfigDir(), this.getMod().getModId().toLowerCase() + ".cfg");
    }

    @SubscribeEvent
    @SuppressWarnings("unused")
    public void onConfigurationChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
        if(this.config.hasChanged()) {
            this.config.save();
        }
    }
}