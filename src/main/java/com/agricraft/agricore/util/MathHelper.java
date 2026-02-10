/*
 */
package com.agricraft.agricore.util;

/**
 *
 * @author RlonRyan
 */
public class MathHelper {

    /**
     * Brings an integer into a specified range.
     *
     * @param value The value to bring into the range.
     * @param min The minimum value, inclusive.
     * @param max The maximum value, inclusive.
     * @return The in-bounded value.
     */
    public static int inRange(int value, int min, int max) {
        return value < min ? min : value > max ? max : value;
    }

    /**
     * Brings an integer into a specified range.
     *
     * @param value The value to bring into the range.
     * @param min The minimum value, inclusive.
     * @param max The maximum value, inclusive.
     * @return The in-bounded value.
     */
    public static float inRange(float value, float min, float max) {
        return value < min ? min : value > max ? max : value;
    }

    /**
     * Brings an integer into a specified range.
     *
     * @param value The value to bring into the range.
     * @param min The minimum value, inclusive.
     * @param max The maximum value, inclusive.
     * @return The in-bounded value.
     */
    public static double inRange(double value, double min, double max) {
        return value < min ? min : value > max ? max : value;
    }

    /**
     * Attempts to parse an integer string, returning the given default value in
     * the case that a {@link NumberFormatException} is thrown.
     *
     * @param s The string to be parsed.
     * @param or The default value to be used, in the case of an error.
     * @return The parsed value, or the default value in the case of an error.
     */
    public static int parseIntOr(String s, int or) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return or;
        }
    }

}
