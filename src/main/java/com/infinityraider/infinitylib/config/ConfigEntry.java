package com.infinityraider.infinitylib.config;

import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.util.function.Function;

public abstract class ConfigEntry<T> {
    private final String name;
    private final InfinityConfigurationHandler configuration;
    private final ConfigCategory category;

    private final T defaultValue;
    private final String comment;

    private Property property;
    private T value;

    protected ConfigEntry(String name, InfinityConfigurationHandler config, ConfigCategory category, T defaultValue, String comment) {
        this.name = name;
        this.configuration = config;
        this.category = category;
        this.defaultValue = defaultValue;
        this.comment = comment;
        this.configuration.addEntry(this);
    }

    public String getName() {
        return this.name;
    }

    public InfinityConfigurationHandler getConfigurationHandler() {
        return this.configuration;
    }

    public Configuration getConfiguration() {
        return this.getConfigurationHandler().getConfiguration();
    }

    public ConfigCategory getCategory() {
        return this.category;
    }

    public String getCategoryName() {
        return this.getCategory().getName();
    }

    public T getDefaultValue() {
        return this.defaultValue;
    }

    public String getComment() {
        return this.comment;
    }

    public Property getProperty() {
        return this.property;
    }

    public T getValue() {
        return this.property == null ? this.getDefaultValue() : this.value;
    }

    void initialize() {
        this.property = this.fetchProperty();
        this.value = this.conversionFunction().apply(this.getProperty());
    }

    protected abstract Property fetchProperty();

    protected abstract Function<Property, T> conversionFunction();

    public static ConfigEntry<String> String(String name, InfinityConfigurationHandler config, ConfigCategory category, String defaultValue, String comment) {
        return new ConfigEntry<String>(name, config, category, defaultValue, comment) {
            @Override
            protected Property fetchProperty() {
                return this.getConfiguration().get(this.getCategoryName(), this.getName(), this.getDefaultValue(), this.getComment());
            }

            @Override
            protected Function<Property, String> conversionFunction() {
                return Property::getString;
            }
        };
    }

    public static ConfigEntry<Boolean> Boolean(String name, InfinityConfigurationHandler config, ConfigCategory category, boolean defaultValue, String comment) {
        return new ConfigEntry<Boolean>(name, config, category, defaultValue, comment) {
            @Override
            protected Property fetchProperty() {
                return this.getConfiguration().get(this.getCategoryName(), this.getName(), this.getDefaultValue(), this.getComment());
            }

            @Override
            protected Function<Property, Boolean> conversionFunction() {
                return Property::getBoolean;
            }
        };
    }

    public static ConfigEntry<Integer> Integer(String name, InfinityConfigurationHandler config, ConfigCategory category, int defaultValue, int minValue, int maxValue, String comment) {
        return new ConfigEntry<Integer>(name, config, category, defaultValue, comment) {
            @Override
            protected Property fetchProperty() {
                return this.getConfiguration().get(this.getCategoryName(), this.getName(), this.getDefaultValue(), this.getComment(), minValue, maxValue);
            }

            @Override
            protected Function<Property, Integer> conversionFunction() {
                return Property::getInt;
            }
        };
    }

    public static ConfigEntry<Long> Long(String name, InfinityConfigurationHandler config, ConfigCategory category, long defaultValue, long minValue, long maxValue, String comment) {
        return new ConfigEntry<Long>(name, config, category, defaultValue, comment) {
            @Override
            protected Property fetchProperty() {
                return this.getConfiguration().get(this.getCategoryName(), this.getName(), this.getDefaultValue(), this.getComment(), minValue, maxValue);
            }

            @Override
            protected Function<Property, Long> conversionFunction() {
                return Property::getLong;
            }
        };
    }

    public static ConfigEntry<Float> Float(String name, InfinityConfigurationHandler config, ConfigCategory category, float defaultValue, float minValue, float maxValue, String comment) {
        return new ConfigEntry<Float>(name, config, category, defaultValue, comment) {
            @Override
            protected Property fetchProperty() {
                return this.getConfiguration().get(this.getCategoryName(), this.getName(), this.getDefaultValue(), this.getComment(), minValue, maxValue);
            }

            @Override
            protected Function<Property, Float> conversionFunction() {
                return (prop) -> (float) prop.getDouble();
            }
        };
    }

    public static ConfigEntry<Double> Double(String name, InfinityConfigurationHandler config, ConfigCategory category, double defaultValue, double minValue, double maxValue, String comment) {
        return new ConfigEntry<Double>(name, config, category, defaultValue, comment) {
            @Override
            protected Property fetchProperty() {
                return this.getConfiguration().get(this.getCategoryName(), this.getName(), this.getDefaultValue(), this.getComment(), minValue, maxValue);
            }

            @Override
            protected Function<Property, Double> conversionFunction() {
                return Property::getDouble;
            }
        };
    }

    public static ConfigEntry<String[]> StringArray(String name, InfinityConfigurationHandler config, ConfigCategory category, String[] defaultValue, String comment) {
        return new ConfigEntry<String[]>(name, config, category, defaultValue, comment) {
            @Override
            protected Property fetchProperty() {
                return this.getConfiguration().get(this.getCategoryName(), this.getName(), this.getDefaultValue(), this.getComment());
            }

            @Override
            protected Function<Property, String[]> conversionFunction() {
                return Property::getStringList;
            }
        };
    }

    public static ConfigEntry<boolean[]> BoolArray(String name, InfinityConfigurationHandler config, ConfigCategory category, boolean[] defaultValue, String comment) {
        return new ConfigEntry<boolean[]>(name, config, category, defaultValue, comment) {
            @Override
            protected Property fetchProperty() {
                return this.getConfiguration().get(this.getCategoryName(), this.getName(), this.getDefaultValue(), this.getComment());
            }

            @Override
            protected Function<Property, boolean[]> conversionFunction() {
                return Property::getBooleanList;
            }
        };
    }

    public static ConfigEntry<int[]> IntArray(String name, InfinityConfigurationHandler config, ConfigCategory category, int[] defaultValue, String comment) {
        return new ConfigEntry<int[]>(name, config, category, defaultValue, comment) {
            @Override
            protected Property fetchProperty() {
                return this.getConfiguration().get(this.getCategoryName(), this.getName(), this.getDefaultValue(), this.getComment());
            }

            @Override
            protected Function<Property, int[]> conversionFunction() {
                return Property::getIntList;
            }
        };
    }

    public static ConfigEntry<double[]> DoubleArray(String name, InfinityConfigurationHandler config, ConfigCategory category, double[] defaultValue, String comment) {
        return new ConfigEntry<double[]>(name, config, category, defaultValue, comment) {
            @Override
            protected Property fetchProperty() {
                return this.getConfiguration().get(this.getCategoryName(), this.getName(), this.getDefaultValue(), this.getComment());
            }

            @Override
            protected Function<Property, double[]> conversionFunction() {
                return Property::getDoubleList;
            }
        };
    }
}