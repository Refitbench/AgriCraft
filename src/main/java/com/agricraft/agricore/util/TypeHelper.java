/*
 */
package com.agricraft.agricore.util;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A class to help with using types.
 *
 * @author RlonRyan
 */
public class TypeHelper {

    public static <T> T[] asArray(Collection<T> set, Class<T> type) {
        @SuppressWarnings("unchecked")
        final T[] array = (T[]) Array.newInstance(type, set.size());
        return set.toArray(array);
    }

    public static <T> List<T> asList(T... elements) {
        return Arrays.asList(elements);
    }

    public static <T> Set<T> asSet(T... elements) {
        return new HashSet<>(Arrays.asList(elements));
    }

    public static <T, C extends Collection<T>> C addAll(C collection, T... elements) {
        collection.addAll(Arrays.asList(elements));
        return collection;
    }

    public static boolean isAllTypes(Object obj, Class... types) {
        return isAllTypes(obj.getClass(), types);
    }

    public static boolean isAllTypes(Class clazz, Class... types) {
        for (Class type : types) {
            if (!isType(clazz, type)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAnyType(Object obj, Class... types) {
        return isAnyType(obj.getClass(), types);
    }

    public static boolean isAnyType(Class clazz, Class... types) {
        for (Class type : types) {
            if (isType(clazz, type)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isType(Object obj, Class type) {
        return isType(obj.getClass(), type);
    }

    public static boolean isType(Class clazz, Class type) {
        return type.isAssignableFrom(clazz);
    }

    public static boolean isNull(Object obj) {
        return obj == null;
    }

    public static boolean isNonNull(Object obj) {
        return obj != null;
    }

    public static <T> Optional<T> cast(Object obj, Class<T> type) {
        if (type.isAssignableFrom(obj.getClass())) {
            return Optional.of(type.cast(obj));
        } else {
            return Optional.empty();
        }
    }

}
