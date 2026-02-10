/*
 */
package com.agricraft.agricore.util;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Nonnull;

/**
 *
 * @author Ryan
 */
public final class StringUtil {

	public static final Pattern TOKENIZER = Pattern.compile("\"([^\"]*)\"|(\\S+)");

	/**
	 * Tokenizes the given input string using spaces as delimiters, but ignoring
	 * spaces that are surrounded by quotes.
	 *
	 * @param input the string to tokenize.
	 * @return the tokenized string.
	 */
	public static final Deque<String> tokenize(@Nonnull String input) {
		final Matcher m = TOKENIZER.matcher(input);
		final Deque<String> tokens = new ArrayDeque<>();
		while (m.find()) {
			tokens.add(m.group());
		}
		return tokens;
	}
    
    public static final String increaseIndent(@Nonnull String input) {
        return input.replaceAll("(?m)^", "\t");
    }
    
    public static final String decreaseIndent(@Nonnull String input) {
        return input.replaceAll("(?m)^\t", "");
    }
    
    public static final String trimStart(@Nonnull String input) {
        return input.replaceAll("^\\s+", "");
    }
    
    public static final String trimEnd(@Nonnull String input) {
        return input.replaceAll("\\s+$", "");
    }

    private StringUtil() {
        // Empty private constructor to prevent instantiation.
    }

}
