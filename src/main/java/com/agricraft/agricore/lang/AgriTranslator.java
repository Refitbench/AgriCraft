/*
 */
package com.agricraft.agricore.lang;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author RlonRyan
 */
public class AgriTranslator {

    private final AgriTranslationAdapter adapter;

    public AgriTranslator(AgriTranslationAdapter adapter) {
        this.adapter = adapter;
    }

    public String translateKeyOrDefault(String key, String def) {
        final String trans = adapter.translateKey(key);
        return trans.equals(key) ? def : trans;
    }

    public String translate(Object key) {
        return adapter.translateKey(String.valueOf(key));
    }

    public String translateIf(Object message, boolean condition) {
        return condition ? translate(message) : String.valueOf(message);
    }

    public List<String> translate(List<?> message) {
        if (message != null) {
            List<String> translation = new ArrayList<>(message.size());
            message.forEach(e -> translation.add(translate(e)));
            return translation;
        } else {
            return null;
        }
    }

    public List<String> translateIf(List<String> message, boolean condition) {
        return condition ? translate(message) : message;
    }

    public String getLocale() {
        return adapter.getLocale();
    }

}
