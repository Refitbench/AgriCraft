/*
 */
package com.agricraft.agricore.config;

import com.agricraft.agricore.core.AgriCore;
import com.agricraft.agricore.util.ReflectionHelper;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author RlonRyan
 */
public class AgriConfig {

    private final AgriConfigAdapter provider;

    private final Map<Object, List<Field>> configurables;

    private final List<AgriConfigListener> configurationListeners;

    public AgriConfig(AgriConfigAdapter provider) {
        this.configurables = new HashMap<>();
        this.configurationListeners = new ArrayList<>();
        this.provider = provider;
    }

    public void load() {
        //AgriCore.getCoreLogger().debug("Loading Config!");
        this.provider.load();
        this.configurables.forEach(
                (configurable, fields) -> fields.forEach(
                        (field) -> handleConfigurable(configurable instanceof Class ? null : configurable, field)
                )
        );
        this.configurationListeners.forEach(AgriConfigListener::onConfigChanged);
        //AgriCore.getCoreLogger().debug("Loaded Config!");
    }

    public void save() {
        //AgriCore.getCoreLogger().debug("Saving Config!");
        this.provider.save();
        //AgriCore.getCoreLogger().debug("Config Saved!");
    }

    public String getLocation() {
        return this.provider.getLocation();
    }

    public final synchronized void addListener(AgriConfigListener listener) {
        this.configurationListeners.add(listener);
    }

    public final synchronized void removeListener(AgriConfigListener listener) {
        if (listener != null) {
            this.configurationListeners.remove(listener);
        }
    }

    public final synchronized void addConfigurable(Object configurable) {
        if (!configurables.containsKey(configurable)) {
            List<Field> fields = new ArrayList<>();
            ReflectionHelper.forEachFieldIn(configurable, AgriConfigurable.class, (field, anno) -> {
                if (Modifier.isFinal(field.getModifiers())) {
                    AgriCore.getCoreLogger().error("Configurable Field: " + field.getName() + " is final!");
                } else {
                    handleConfigurable(configurable, field);
                    fields.add(field);
                }
            });
            configurables.put(configurable, fields);
        }
    }

    protected final void handleConfigurable(Object configurable, Field f) {

        //AgriCore.getCoreLogger().debug("Loading Configurable Field: " + configurable.toString());
        final AgriConfigurable anno = f.getAnnotation(AgriConfigurable.class);
        try {

            f.setAccessible(true);
            Object obj = f.get(configurable);

            String key = anno.key();
            String comment = anno.comment();

            final String category = anno.category().name();

            if (configurable instanceof AgriConfigurableInstance) {
                AgriConfigurableInstance ins = (AgriConfigurableInstance) configurable;
                key = ins.resolve(key).replaceAll("\\s+", "_").toLowerCase();
                comment = ins.resolve(comment);
            }

            if (obj instanceof String) {
                f.set(configurable, provider.getString(key, category, (String) obj, comment));
            } else if (obj instanceof Boolean) {
                f.set(configurable, provider.getBoolean(key, category, (boolean) obj, comment));
            } else if (obj instanceof Integer) {
                int min = Integer.parseInt(anno.min());
                int max = Integer.parseInt(anno.max());
                f.set(configurable, provider.getInt(key, category, (int) obj, min, max, comment));
            } else if (obj instanceof Float) {
                float min = Float.parseFloat(anno.min());
                float max = Float.parseFloat(anno.max());
                f.set(configurable, provider.getFloat(key, category, (float) obj, min, max, comment));
            } else if (obj instanceof Double) {
                double min = Double.parseDouble(anno.min());
                double max = Double.parseDouble(anno.max());
                f.set(configurable, provider.getDouble(key, category, (double) obj, min, max, comment));
            } else {
                AgriCore.getCoreLogger().debug("Bad Type: " + f.getType().toString());
            }

        } catch (NumberFormatException e) {
            AgriCore.getCoreLogger().debug("Invalid parameter bound!");
        } catch (IllegalAccessException | IllegalArgumentException | SecurityException e) {
            AgriCore.getCoreLogger().trace(e);
        }

    }

    public boolean getBoolean(String name, AgriConfigCategory category, boolean defaultValue, String comment) {
        return this.provider.getBoolean(name, category.name(), defaultValue, comment);
    }

    public int getInt(String name, AgriConfigCategory category, int defaultValue, int minValue, int maxValue, String comment) {
        return this.provider.getInt(name, category.name(), defaultValue, minValue, maxValue, comment);
    }

    public float getFloat(String name, AgriConfigCategory category, float defaultValue, float minValue, float maxValue, String comment) {
        return this.provider.getFloat(name, category.name(), defaultValue, minValue, maxValue, comment);
    }

    public double getDouble(String name, AgriConfigCategory category, double defaultValue, double minValue, double maxValue, String comment) {
        return this.provider.getDouble(name, category.name(), defaultValue, minValue, maxValue, comment);
    }

    public String getString(String name, AgriConfigCategory category, String defaultValue, String comment) {
        return this.provider.getString(name, category.name(), defaultValue, comment);
    }

    @Override
    public String toString() {
        return "\nAgriConfig:\n" + this.provider.toString().replaceAll("\\{", "{\n\t").replaceAll("}", "\n}\n").replaceAll(", ", ",\n\t");
    }

}
