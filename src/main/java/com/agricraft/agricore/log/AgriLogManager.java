/*
 */
package com.agricraft.agricore.log;

import com.agricraft.agricore.core.AgriCore;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RlonRyan
 */
public class AgriLogManager {

    private final AgriLogAdapter adapter;

    private final Map<Object, AgriLogger> loggers;

    public AgriLogManager(AgriLogAdapter adapter) {
        this.adapter = adapter;
        this.loggers = new HashMap<>();
    }

    public AgriLogger getLogger(Object source) {
        if (!loggers.containsKey(source)) {
            AgriLogger logger = new AgriLogger(adapter, source);
            AgriCore.getConfig().addConfigurable(logger);
            loggers.put(source, logger);
        }
        return loggers.get(source);
    }

}
