package com.infinityraider.infinitylib.config;

import com.infinityraider.infinitylib.InfinityLib;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ModConfiguration implements IModConfiguration {
    private static final ModConfiguration INSTANCE = new ModConfiguration();

    public static ModConfiguration getInstance() {
        return INSTANCE;
    }

    //debug
    public ConfigEntry<Boolean> debug;

    private ModConfiguration() {}

    public boolean debug() {
        return this.debug == null ? false : this.debug.getValue();
    }

    @Override
    public void initializeConfiguration(InfinityConfigurationHandler handler) {
        debug = ConfigEntry.Boolean("debug", handler, Categories.DEBUG.getCategory(), false, "Set to true if you wish to enable debug mode");
        InfinityLib.instance.getLogger().debug("Configuration Loaded");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void initializeConfigurationClient(InfinityConfigurationHandler handler) {}

    public enum Categories {
        DEBUG;

        private final ConfigCategory category;

        Categories() {
            this.category = new ConfigCategory(this.name());
        }

        public ConfigCategory getCategory() {
            return this.category;
        }

        public static List<ConfigCategory> getCategories() {
            return Arrays.stream(values()).map(Categories::getCategory).collect(Collectors.toList());
        }
    }
}
