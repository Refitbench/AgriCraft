/*
 */
package com.agricraft.agricore.test.config;

import com.agricraft.agricore.config.AgriConfigCategory;
import com.agricraft.agricore.config.AgriConfigurable;
import com.agricraft.agricore.core.AgriCore;

import java.lang.reflect.Field;

/**
 *
 * @author RlonRyan
 */
public class DummyConfig {
	
	@AgriConfigurable(
			category = AgriConfigCategory.CORE,
			comment = "Is it true?",
			key = "is_true"
	)
	public static boolean isTrue = false;
	
	@AgriConfigurable(
			category = AgriConfigCategory.CORE,
			comment = "Give me an integer!",
			key = "the_int"
	)
	public static int theInt = 10;
	
	@AgriConfigurable(
			category = AgriConfigCategory.CORE,
			comment = "Float my boat!",
			key = "the_float"
	)
	public static float theFloat = -1.25f;
	
	@AgriConfigurable(
			category = AgriConfigCategory.CORE,
			comment = "1 <3 π!",
			key = "the_double"
	)
	public static double theDouble = Math.PI;
	
	@AgriConfigurable(
			category = AgriConfigCategory.CORE,
			comment = "Random String.",
			key = "the_string"
	)
	public static String theString = "The quick brown fox jumps over the lazy dog.";
	
	public static String asString() {
		final StringBuilder sb = new StringBuilder().append("Dummy Config:\n");
		for (Field f : DummyConfig.class.getFields()) {
			try {
				sb.append("\t- ").append(f.getName()).append(": ").append(f.get(null)).append("\n");
			} catch (IllegalAccessException | IllegalArgumentException e) {
				AgriCore.getCoreLogger().trace(e);
			}
		}
		return sb.toString();
	}
	
}
