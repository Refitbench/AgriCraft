/*
 */
package com.agricraft.agricore.defaults;

import com.agricraft.agricore.lang.AgriTranslationAdapter;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 *
 * @author RlonRyan
 */
public class AgriDefaultTranslator implements AgriTranslationAdapter {

    private Locale locale;
    private ResourceBundle messages;

    public AgriDefaultTranslator() {
        setLocale(new Locale("EN", "US"));
    }

    public final void setLocale(Locale locale) {
        try {
            this.locale = locale;
            this.messages = ResourceBundle.getBundle("com.agricraft.agricore.localization.dictionary", locale);
        } catch (MissingResourceException e) {
            // Do nothing.
        }
    }

    @Override
    public String translateKey(String key) {
        try {
            return this.messages.getString(key);
        } catch (NullPointerException | MissingResourceException e) {
            return key;
        }
    }

    @Override
    public String getLocale() {
        return this.locale.toString();
    }

}
