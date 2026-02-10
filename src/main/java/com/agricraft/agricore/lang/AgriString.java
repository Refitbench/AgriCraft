/*
 */
package com.agricraft.agricore.lang;

import com.agricraft.agricore.core.AgriCore;
import com.google.gson.annotations.SerializedName;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author RlonRyan
 */
public class AgriString {

    private final Map<String, String> translations;

    @SerializedName(value = "default", alternate = {"normal"})
    private final String normal;

    public AgriString() {
        this.translations = new HashMap<>();
        this.normal = "Testing 1, 2, 3.";
    }

    public AgriString(String value) {
        this.translations = new HashMap<>(0);
        this.normal = value;
    }

    @Override
    public String toString() {
        return this.translations.getOrDefault(AgriCore.getTranslator().getLocale(), normal);
    }

}
