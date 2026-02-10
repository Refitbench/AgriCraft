/*
 */
package com.agricraft.agricore.log;

import com.agricraft.agricore.config.AgriConfigCategory;
import com.agricraft.agricore.config.AgriConfigurable;
import com.agricraft.agricore.config.AgriConfigurableInstance;
import java.util.Objects;

/**
 *
 * @author RlonRyan
 */
public class AgriLogger implements AgriConfigurableInstance {

    private final AgriLogAdapter adapter;
    private final Object source;

    @AgriConfigurable(key = "${log} Logging", category = AgriConfigCategory.LOGGING, comment = "Set to true to enable logging on the ${log} channel.")
    private boolean enabled = true;

    AgriLogger(AgriLogAdapter adapter, Object source) {
        this.adapter = adapter;
        this.source = source;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void all(Object format, Object... objects) {
        if (this.enabled) {
            adapter.all(source, Objects.toString(format), objects);
        }
    }

    public void severe(Object format, Object... objects) {
        if (this.enabled) {
            adapter.severe(source, Objects.toString(format), objects);
        }
    }

    public void info(Object format, Object... objects) {
        if (this.enabled) {
            adapter.info(source, Objects.toString(format), objects);
        }
    }

    public void warn(Object format, Object... objects) {
        if (this.enabled) {
            adapter.warn(source, Objects.toString(format), objects);
        }
    }

    public void debug(Object format, Object... objects) {
        if (this.enabled) {
            adapter.debug(source, Objects.toString(format), objects);
        }
    }

    public void error(Object format, Object... objects) {
        if (this.enabled) {
            adapter.error(source, Objects.toString(format), objects);
        }
    }

    public void trace(Exception e) {
        if (this.enabled) {
            adapter.trace(source, e);
        }
    }

    @Override
    public String resolve(String input) {
        return input.replaceAll("\\$\\{log\\}", String.valueOf(source));
    }

}
