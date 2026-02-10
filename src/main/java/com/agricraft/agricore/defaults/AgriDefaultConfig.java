/*
 */
package com.agricraft.agricore.defaults;

import com.agricraft.agricore.config.AgriConfigAdapter;

/**
 *
 * @author Ryan
 */
public class AgriDefaultConfig implements AgriConfigAdapter {

    @Override
    public void load() {
        // Nope.
    }

    @Override
    public void save() {
        // Nope.
    }

    @Override
    public boolean getBoolean(String name, String category, boolean defaultValue, String comment) {
        return defaultValue;
    }

    @Override
    public int getInt(String name, String category, int defaultValue, int minValue, int maxValue, String comment) {
        return defaultValue;
    }

    @Override
    public float getFloat(String name, String category, float defaultValue, float minValue, float maxValue, String comment) {
        return defaultValue;
    }

    @Override
    public double getDouble(String name, String category, double defaultValue, double minValue, double maxValue, String comment) {
        return defaultValue;
    }

    @Override
    public String getString(String name, String category, String defaultValue, String comment) {
        return defaultValue;
    }

    @Override
    public String getLocation() {
        return "";
    }
    
}
